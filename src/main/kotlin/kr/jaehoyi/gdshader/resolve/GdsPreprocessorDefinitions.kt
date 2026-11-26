package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import kr.jaehoyi.gdshader.GdsLexerAdapter
import kr.jaehoyi.gdshader.psi.GdsTokenSets
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.impl.GdsBuiltinDefine
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement

object GdsPreprocessorDefinitions {
    private const val MAX_INCLUDE_DEPTH = 25

    private val DIRECTIVES_KEY = Key.create<CachedValue<List<Directive>>>("GDS_PREPROCESSOR_DIRECTIVES")
    private val LINE_CONTINUATION_REGEX = Regex("""\\[ \t]*(\r\n|\r|\n)""")
    private val DEFINE_PREFIX_REGEX = Regex("""^\s*#define[ \t]+""")
    private val UNDEF_REGEX = Regex("""^\s*#undef[ \t]+([A-Za-z_][A-Za-z0-9_]*)""")
    private val IDENTIFIER_REGEX = Regex("""[A-Za-z_][A-Za-z0-9_]*""")

    fun getVisibleMacros(place: PsiElement): List<GdsMacro> {
        val site = GdsMacroExpansion.expansionSite(place)
        val file = site.containingFile ?: return emptyList()
        val table = LinkedHashMap<String, GdsMacro>()
        collectMacros(file, site.textOffset, table, mutableSetOf(), 0)
        return table.values.toList()
    }

    fun resolveObjectMacro(
        place: PsiElement,
        name: String,
    ): GdsMacroElement? =
        getVisibleMacros(place)
            // Function-like macros only expand when called.
            .firstOrNull { it.name == name && !it.isFunctionLike }
            ?.let { GdsMacroElement(it) }

    fun resolveBuiltinDefine(
        place: PsiElement,
        name: String,
    ): GdsBuiltinDefine? {
        if (name !in GdsConditionalDirectives.BUILTIN_DEFINES) return null
        return GdsBuiltinDefine(place.manager, name, GdsConditionalDirectives.BUILTIN_DEFINE_VALUES[name])
    }

    fun resolveCallableMacro(
        place: PsiElement,
        name: String,
    ): GdsMacroElement? =
        getVisibleMacros(place)
            .firstOrNull { it.name == name }
            ?.let { GdsMacroElement(it) }

    fun parseMacro(element: PsiElement): GdsMacro? {
        if (element.node?.elementType != GdsTypes.PP_DEFINE_LINE) return null
        // Highlighting may inspect an incomplete PSI tree while the user edits a directive.
        val define = parseDefine(element.text ?: return null) ?: return null
        return GdsMacro(
            name = define.name,
            parameters = define.parameters,
            body = define.body,
            element = element,
            nameOffset = define.nameOffset,
        )
    }

    fun parseDefine(rawText: String): Define? {
        // Same-length replacement keeps offsets into the original text valid.
        val text = LINE_CONTINUATION_REGEX.replace(rawText) { " ".repeat(it.value.length) }

        val prefix = DEFINE_PREFIX_REGEX.find(text) ?: return null
        val nameStart = prefix.range.last + 1
        val nameMatch = IDENTIFIER_REGEX.find(text, nameStart)?.takeIf { it.range.first == nameStart } ?: return null
        var index = nameMatch.range.last + 1

        var parameters: List<String>? = null
        // A function-like macro requires "(" immediately after the name, without whitespace.
        if (index < text.length && text[index] == '(') {
            val close = text.indexOf(')', index)
            if (close < 0) return null
            val paramText = text.substring(index + 1, close).trim()
            parameters =
                if (paramText.isEmpty()) {
                    emptyList()
                } else {
                    paramText.split(',').map { it.trim() }
                }
            if (parameters.any { !IDENTIFIER_REGEX.matches(it) }) return null
            index = close + 1
        }

        return Define(nameMatch.value, parameters, tokenize(text.substring(index)), nameMatch.range.first)
    }

    data class Define(
        val name: String,
        val parameters: List<String>?,
        val body: List<MacroToken>,
        val nameOffset: Int,
    )

    private fun tokenize(text: String): List<MacroToken> {
        val lexer = GdsLexerAdapter()
        lexer.start(text)
        val tokens = mutableListOf<MacroToken>()
        var pendingSpace = false
        while (true) {
            val type = lexer.tokenType ?: break
            if (type == TokenType.WHITE_SPACE || type in GdsTokenSets.COMMENTS) {
                pendingSpace = true
            } else {
                tokens += MacroToken(type, lexer.tokenText, pendingSpace && tokens.isNotEmpty())
                pendingSpace = false
            }
            lexer.advance()
        }
        return tokens
    }

    private fun collectMacros(
        file: PsiFile,
        maxOffset: Int?,
        table: MutableMap<String, GdsMacro>,
        visiting: MutableSet<PsiFile>,
        depth: Int,
    ) {
        if (depth > MAX_INCLUDE_DEPTH || !visiting.add(file)) return
        try {
            for (directive in getDirectives(file)) {
                if (maxOffset != null && directive.offset >= maxOffset) break
                when (directive) {
                    is Directive.Define -> {
                        table.remove(directive.macro.name)
                        table[directive.macro.name] = directive.macro
                    }
                    is Directive.Undef -> table.remove(directive.name)
                    is Directive.Include -> {
                        val included = GdsPathUtil.resolvePath(file, directive.path) ?: continue
                        if (included.virtualFile?.extension != "gdshaderinc") continue
                        collectMacros(included, null, table, visiting, depth + 1)
                    }
                }
            }
        } finally {
            visiting.remove(file)
        }
    }

    private fun getDirectives(file: PsiFile): List<Directive> =
        CachedValuesManager.getCachedValue(file, DIRECTIVES_KEY) {
            CachedValueProvider.Result.create(computeDirectives(file), PsiModificationTracker.MODIFICATION_COUNT)
        }

    private fun computeDirectives(file: PsiFile): List<Directive> =
        SyntaxTraverser
            .psiTraverser(file)
            .mapNotNull { element ->
                when (element.node.elementType) {
                    GdsTypes.PP_DEFINE_LINE -> parseMacro(element)?.let { Directive.Define(element.textOffset, it) }
                    GdsTypes.PP_UNDEF_LINE ->
                        UNDEF_REGEX.find(element.text)?.let { Directive.Undef(element.textOffset, it.groupValues[1]) }
                    GdsTypes.PP_INCLUDE_LINE ->
                        GdsPathUtil.findIncludePath(element)?.let { Directive.Include(element.textOffset, it.path) }
                    else -> null
                }
            }.toList()

    private sealed interface Directive {
        val offset: Int

        data class Define(
            override val offset: Int,
            val macro: GdsMacro,
        ) : Directive

        data class Undef(
            override val offset: Int,
            val name: String,
        ) : Directive

        data class Include(
            override val offset: Int,
            val path: String,
        ) : Directive
    }

    data class MacroToken(
        val type: IElementType,
        val text: String,
        val precededBySpace: Boolean,
    )

    data class GdsMacro(
        val name: String,
        val parameters: List<String>?,
        val body: List<MacroToken>,
        val element: PsiElement,
        val nameOffset: Int,
    ) {
        val isFunctionLike: Boolean get() = parameters != null

        val bodyText: String get() = joinTokens(body.map { it.text to it.precededBySpace })

        val signatureText: String
            get() = if (parameters == null) name else "$name(${parameters.joinToString(", ")})"

        fun substitute(arguments: List<String>): String? {
            val params = parameters ?: return null
            if (params.size != arguments.size) return null
            return joinTokens(
                body.map { token ->
                    val index = if (token.type == GdsTypes.IDENTIFIER) params.indexOf(token.text) else -1
                    val text = if (index >= 0) arguments[index] else token.text
                    text to token.precededBySpace
                },
            )
        }

        // Excludes the body so that two lookups of the same #define compare equal.
        override fun equals(other: Any?): Boolean = other is GdsMacro && other.element == element && other.name == name

        override fun hashCode(): Int = 31 * element.hashCode() + name.hashCode()

        private fun joinTokens(parts: List<Pair<String, Boolean>>): String =
            buildString {
                for ((text, space) in parts) {
                    if (space) append(' ')
                    append(text)
                }
            }
    }
}

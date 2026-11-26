package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import kr.jaehoyi.gdshader.GdsLexerAdapter
import kr.jaehoyi.gdshader.psi.GdsTokenSets
import kr.jaehoyi.gdshader.psi.GdsTypes

// A condition is only evaluated when everything it depends on is known from the file itself. A macro may
// also come from an including or included file, and CURRENT_RENDERER depends on the project, so such
// branches stay UNKNOWN and are parsed like active code.
object GdsConditionalDirectives {
    enum class Status { ACTIVE, INACTIVE, UNKNOWN }

    data class DirectiveToken(
        val type: IElementType,
        val range: TextRange,
        val text: String,
    )

    data class Branch(
        val chain: Int,
        val index: Int,
        val range: TextRange,
    )

    class Analysis(
        val inactiveRanges: List<TextRange>,
        val branches: List<Branch>,
        val problems: Map<Int, String>,
        val statuses: Map<Int, Status>,
    ) {
        fun statusAt(directiveOffset: Int): Status = statuses[directiveOffset] ?: Status.ACTIVE

        fun areExclusive(
            first: Int,
            second: Int,
        ): Boolean {
            val firstPath = branchPath(first)
            val secondPath = branchPath(second)
            for (i in 0 until minOf(firstPath.size, secondPath.size)) {
                val a = firstPath[i]
                val b = secondPath[i]
                if (a == b) continue
                return a.chain == b.chain
            }
            return false
        }

        private fun branchPath(offset: Int): List<Branch> = branches.filter { it.range.containsOffset(offset) }.sortedBy { it.range.startOffset }
    }

    val DIRECTIVES: TokenSet =
        TokenSet.create(
            GdsTypes.PP_IF_LINE,
            GdsTypes.PP_IFDEF_LINE,
            GdsTypes.PP_IFNDEF_LINE,
            GdsTypes.PP_ELIF_LINE,
            GdsTypes.PP_ELSE_LINE,
            GdsTypes.PP_ENDIF_LINE,
            GdsTypes.PP_DEFINE_LINE,
            GdsTypes.PP_UNDEF_LINE,
            GdsTypes.PP_INCLUDE_LINE,
            GdsTypes.PP_ERROR_LINE,
        )

    // Godot 4.7.2, ShaderPreprocessor::preprocess. CURRENT_RENDERER has no value because it depends on the project.
    val BUILTIN_DEFINE_VALUES: Map<String, Int?> =
        mapOf(
            "CURRENT_RENDERER" to null,
            "RENDERER_COMPATIBILITY" to 0,
            "RENDERER_MOBILE" to 1,
            "RENDERER_FORWARD_PLUS" to 2,
        )

    val BUILTIN_DEFINES: Set<String> = BUILTIN_DEFINE_VALUES.keys

    private val ANALYSIS_KEY = Key.create<CachedValue<Analysis>>("GDS_CONDITIONAL_DIRECTIVES")
    private val DIRECTIVE_NAME_REGEX = Regex("""^\s*#\s*[A-Za-z]+""")
    private val LINE_CONTINUATION_REGEX = Regex("""\\[ \t]*(\r\n|\r|\n)""")
    private const val MAX_EXPANSION_DEPTH = 32

    fun getAnalysis(file: PsiFile): Analysis =
        CachedValuesManager.getCachedValue(file, ANALYSIS_KEY) {
            val tokens =
                SyntaxTraverser
                    .psiTraverser(file)
                    .filter { it.node?.elementType in DIRECTIVES }
                    .map { DirectiveToken(it.node.elementType, it.textRange, it.text) }
                    .toList()
            CachedValueProvider.Result.create(analyze(tokens, file.textLength), PsiModificationTracker.MODIFICATION_COUNT)
        }

    fun areExclusive(
        first: PsiElement,
        second: PsiElement,
    ): Boolean {
        val file = first.containingFile ?: return false
        if (file != second.containingFile) return false
        val firstRange = first.textRange ?: return false
        val secondRange = second.textRange ?: return false
        return getAnalysis(file).areExclusive(firstRange.startOffset, secondRange.startOffset)
    }

    private sealed interface MacroState {
        data class Defined(
            val body: List<Pair<IElementType, String>>?,
        ) : MacroState

        data object Undefined : MacroState

        data object Uncertain : MacroState
    }

    private class Frame(
        val chain: Int,
        val start: DirectiveToken,
        val parentStatus: Status,
    ) {
        var index = 0
        var anyTrue = false
        var anyUnknown = false
        var elseSeen = false
        var status = Status.ACTIVE
        var contentStart = 0
    }

    fun analyze(
        tokens: List<DirectiveToken>,
        textLength: Int,
    ): Analysis {
        val inactiveRanges = mutableListOf<TextRange>()
        val branches = mutableListOf<Branch>()
        val problems = mutableMapOf<Int, String>()
        val statuses = mutableMapOf<Int, Status>()
        val macros = mutableMapOf<String, MacroState>()
        val stack = ArrayDeque<Frame>()
        var skipDepth = 0
        var chainCount = 0

        fun effective(): Status = stack.lastOrNull()?.let { combine(it.parentStatus, it.status) } ?: Status.ACTIVE

        fun closeBranch(
            frame: Frame,
            end: Int,
        ) {
            val range = TextRange(frame.contentStart, maxOf(frame.contentStart, end))
            if (frame.status == Status.INACTIVE) {
                if (!range.isEmpty) inactiveRanges += range
            } else {
                branches += Branch(frame.chain, frame.index, range)
            }
        }

        fun openBranch(
            frame: Frame,
            directive: DirectiveToken,
            condition: Status,
        ) {
            frame.status =
                when {
                    frame.parentStatus == Status.INACTIVE || frame.anyTrue || condition == Status.INACTIVE -> Status.INACTIVE
                    condition == Status.ACTIVE && !frame.anyUnknown -> Status.ACTIVE
                    else -> Status.UNKNOWN
                }
            if (frame.status == Status.ACTIVE) frame.anyTrue = true
            if (frame.status == Status.UNKNOWN) frame.anyUnknown = true
            frame.contentStart = directive.range.endOffset
        }

        for (directive in tokens) {
            val type = directive.type
            val isOpening = type == GdsTypes.PP_IF_LINE || type == GdsTypes.PP_IFDEF_LINE || type == GdsTypes.PP_IFNDEF_LINE

            if (skipDepth > 0) {
                if (isOpening) skipDepth++
                if (type == GdsTypes.PP_ENDIF_LINE) skipDepth--
                continue
            }

            val current = effective()
            statuses[directive.range.startOffset] = current

            when {
                isOpening -> {
                    if (current == Status.INACTIVE) {
                        skipDepth++
                        continue
                    }
                    val frame = Frame(chainCount++, directive, current)
                    stack.addLast(frame)
                    openBranch(frame, directive, evaluateDirective(directive, macros))
                }
                type == GdsTypes.PP_ELIF_LINE || type == GdsTypes.PP_ELSE_LINE -> {
                    val frame = stack.lastOrNull()
                    val name = if (type == GdsTypes.PP_ELIF_LINE) "elif" else "else"
                    if (frame == null || frame.elseSeen) {
                        problems[directive.range.startOffset] = "Unmatched '$name' directive."
                        continue
                    }
                    closeBranch(frame, directive.range.startOffset)
                    statuses[directive.range.startOffset] = frame.parentStatus
                    frame.index++
                    if (type == GdsTypes.PP_ELSE_LINE) {
                        frame.elseSeen = true
                        openBranch(frame, directive, Status.ACTIVE)
                    } else {
                        openBranch(frame, directive, if (frame.anyTrue) Status.INACTIVE else evaluateDirective(directive, macros))
                    }
                }
                type == GdsTypes.PP_ENDIF_LINE -> {
                    val frame = stack.removeLastOrNull()
                    if (frame == null) {
                        problems[directive.range.startOffset] = "Unmatched 'endif' directive."
                        continue
                    }
                    closeBranch(frame, directive.range.startOffset)
                    statuses[directive.range.startOffset] = frame.parentStatus
                }
                current == Status.INACTIVE -> Unit
                type == GdsTypes.PP_DEFINE_LINE ->
                    GdsPreprocessorDefinitions.parseDefine(directive.text)?.let { define ->
                        macros[define.name] =
                            if (current == Status.UNKNOWN) {
                                MacroState.Uncertain
                            } else {
                                MacroState.Defined(define.body.takeIf { define.parameters == null }?.map { it.type to it.text })
                            }
                    }
                type == GdsTypes.PP_UNDEF_LINE ->
                    identifierAfterDirective(directive.text)?.let { name ->
                        macros[name] = if (current == Status.UNKNOWN) MacroState.Uncertain else MacroState.Undefined
                    }
                type == GdsTypes.PP_INCLUDE_LINE -> macros.replaceAll { _, _ -> MacroState.Uncertain }
            }
        }

        while (stack.isNotEmpty()) {
            val frame = stack.removeLast()
            problems[frame.start.range.startOffset] = "Unmatched conditional statement."
            closeBranch(frame, textLength)
        }

        return Analysis(inactiveRanges.sortedBy { it.startOffset }, branches, problems, statuses)
    }

    private fun combine(
        parent: Status,
        own: Status,
    ): Status =
        when {
            parent == Status.INACTIVE || own == Status.INACTIVE -> Status.INACTIVE
            parent == Status.UNKNOWN || own == Status.UNKNOWN -> Status.UNKNOWN
            else -> Status.ACTIVE
        }

    private fun evaluateDirective(
        directive: DirectiveToken,
        macros: Map<String, MacroState>,
    ): Status {
        when (directive.type) {
            GdsTypes.PP_IFDEF_LINE, GdsTypes.PP_IFNDEF_LINE -> {
                val name = identifierAfterDirective(directive.text) ?: return Status.UNKNOWN
                val defined = isDefined(name, macros) ?: return Status.UNKNOWN
                val expected = directive.type == GdsTypes.PP_IFDEF_LINE
                return if (defined == expected) Status.ACTIVE else Status.INACTIVE
            }
        }
        val prefix = DIRECTIVE_NAME_REGEX.find(directive.text) ?: return Status.UNKNOWN
        val condition = directive.text.substring(prefix.range.last + 1)
        val value = ConditionEvaluator(tokenize(condition), macros).evaluateAll() ?: return Status.UNKNOWN
        return if (truthy(value)) Status.ACTIVE else Status.INACTIVE
    }

    private fun isDefined(
        name: String,
        macros: Map<String, MacroState>,
    ): Boolean? {
        if (name in BUILTIN_DEFINES) return true
        return when (macros[name]) {
            is MacroState.Defined -> true
            MacroState.Undefined -> false
            else -> null
        }
    }

    private fun identifierAfterDirective(text: String): String? {
        val prefix = DIRECTIVE_NAME_REGEX.find(text) ?: return null
        return Regex("""^\s*([A-Za-z_][A-Za-z0-9_]*)""")
            .find(text.substring(prefix.range.last + 1))
            ?.groupValues
            ?.get(1)
    }

    private fun tokenize(text: String): List<Pair<IElementType, String>> {
        val lexer = GdsLexerAdapter()
        lexer.start(LINE_CONTINUATION_REGEX.replace(text, " "))
        val tokens = mutableListOf<Pair<IElementType, String>>()
        while (true) {
            val type = lexer.tokenType ?: break
            if (type != TokenType.WHITE_SPACE && type !in GdsTokenSets.COMMENTS) tokens += type to lexer.tokenText
            lexer.advance()
        }
        return tokens
    }

    private fun truthy(value: Any): Boolean =
        when (value) {
            is Boolean -> value
            is Long -> value != 0L
            is Double -> value != 0.0
            else -> false
        }

    // Returns null when the value cannot be known.
    private class ConditionEvaluator(
        private val tokens: List<Pair<IElementType, String>>,
        private val macros: Map<String, MacroState>,
        private val depth: Int = 0,
    ) {
        private var position = 0
        private var failed = false

        fun evaluateAll(): Any? {
            if (depth > MAX_EXPANSION_DEPTH || tokens.isEmpty()) return null
            val value = parseBinary(0)
            if (failed || position != tokens.size) return null
            return value
        }

        private fun peek(): Pair<IElementType, String>? = tokens.getOrNull(position)

        private fun fail(): Any? {
            failed = true
            return null
        }

        private fun parseBinary(level: Int): Any? {
            if (level >= BINARY_LEVELS.size) return parseUnary()
            var left = parseBinary(level + 1)
            while (!failed) {
                val operator = peek()?.second ?: break
                if (operator !in BINARY_LEVELS[level]) break
                position++
                val right = parseBinary(level + 1)
                left = applyBinary(operator, left, right)
            }
            return left
        }

        private fun parseUnary(): Any? {
            val token = peek() ?: return fail()
            return when (token.second) {
                "!" -> {
                    position++
                    parseUnary()?.let { !truthy(it) }
                }
                "-" -> {
                    position++
                    when (val value = parseUnary()) {
                        is Long -> -value
                        is Double -> -value
                        null -> null
                        else -> fail()
                    }
                }
                "+" -> {
                    position++
                    parseUnary()
                }
                else -> parsePrimary()
            }
        }

        private fun parsePrimary(): Any? {
            val (type, text) = peek() ?: return fail()
            position++
            return when (type) {
                GdsTypes.INT_CONSTANT, GdsTypes.UINT_CONSTANT -> parseInteger(text) ?: fail()
                GdsTypes.FLOAT_CONSTANT -> text.trimEnd('f', 'F').toDoubleOrNull() ?: fail()
                GdsTypes.TRUE -> true
                GdsTypes.FALSE -> false
                GdsTypes.PARENTHESIS_OPEN -> {
                    val value = parseBinary(0)
                    if (peek()?.first != GdsTypes.PARENTHESIS_CLOSE) return fail()
                    position++
                    value
                }
                GdsTypes.IDENTIFIER -> if (text == "defined") parseDefined() else expand(text)
                else -> fail()
            }
        }

        private fun parseDefined(): Any? {
            val parenthesized = peek()?.first == GdsTypes.PARENTHESIS_OPEN
            if (parenthesized) position++
            val name = peek()?.takeIf { it.first == GdsTypes.IDENTIFIER }?.second ?: return fail()
            position++
            if (parenthesized) {
                if (peek()?.first != GdsTypes.PARENTHESIS_CLOSE) return fail()
                position++
            }
            return isDefined(name, macros)
        }

        private fun expand(name: String): Any? {
            BUILTIN_DEFINE_VALUES[name]?.let { return it.toLong() }
            val state = macros[name] as? MacroState.Defined ?: return null
            val body = state.body ?: return null
            return ConditionEvaluator(body, macros, depth + 1).evaluateAll()
        }

        private fun parseInteger(text: String): Long? {
            val digits = text.trimEnd('u', 'U')
            return if (digits.startsWith("0x") || digits.startsWith("0X")) {
                digits.substring(2).toLongOrNull(16)
            } else {
                digits.toLongOrNull()
            }
        }

        private fun applyBinary(
            operator: String,
            left: Any?,
            right: Any?,
        ): Any? {
            if (operator == "&&") {
                if (left != null && !truthy(left)) return false
                if (right != null && !truthy(right)) return false
                return if (left == null || right == null) null else true
            }
            if (operator == "||") {
                if (left != null && truthy(left)) return true
                if (right != null && truthy(right)) return true
                return if (left == null || right == null) null else false
            }
            if (left == null || right == null) return null
            val l = numeric(left)
            val r = numeric(right)
            if (l is Long && r is Long) {
                return when (operator) {
                    "==" -> l == r
                    "!=" -> l != r
                    "<" -> l < r
                    ">" -> l > r
                    "<=" -> l <= r
                    ">=" -> l >= r
                    "+" -> l + r
                    "-" -> l - r
                    "*" -> l * r
                    "/" -> if (r == 0L) fail() else l / r
                    "%" -> if (r == 0L) fail() else l % r
                    else -> fail()
                }
            }
            val a = (l as? Number)?.toDouble() ?: return fail()
            val b = (r as? Number)?.toDouble() ?: return fail()
            return when (operator) {
                "==" -> a == b
                "!=" -> a != b
                "<" -> a < b
                ">" -> a > b
                "<=" -> a <= b
                ">=" -> a >= b
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                "/" -> a / b
                else -> fail()
            }
        }

        private fun numeric(value: Any): Any = if (value is Boolean) (if (value) 1L else 0L) else value

        private companion object {
            val BINARY_LEVELS =
                listOf(
                    setOf("||"),
                    setOf("&&"),
                    setOf("==", "!="),
                    setOf("<", ">", "<=", ">="),
                    setOf("+", "-"),
                    setOf("*", "/", "%"),
                )
        }
    }
}

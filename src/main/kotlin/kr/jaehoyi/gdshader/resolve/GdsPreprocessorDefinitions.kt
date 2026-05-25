package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import kr.jaehoyi.gdshader.psi.GdsConstantEvaluator
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsTypes

object GdsPreprocessorDefinitions {
    private val DEFINE_REGEX = Regex("""^\s*#define\s+([A-Za-z_][A-Za-z0-9_]*)\s+(.+?)\s*$""")

    fun getVisibleLiteralDefines(place: PsiElement): List<LiteralDefine> {
        val file = place.containingFile ?: return emptyList()
        return findVisibleDefines(place, file)
            .mapNotNull { define ->
                val value = define.literalValue ?: return@mapNotNull null
                LiteralDefine(
                    name = define.name,
                    value = value,
                    element = define.element,
                )
            }
    }

    fun resolveLiteralDefine(
        place: PsiElement,
        name: String,
    ): PsiElement? =
        getVisibleLiteralDefines(place)
            .lastOrNull { it.name == name }
            ?.element

    fun evaluateLiteralDefine(element: PsiElement): Any? {
        val define = parseDefine(element) ?: return null
        val value = define.literalValue ?: return null
        val expression = GdsElementFactory.createExpression(element.project, value) ?: return null
        return GdsConstantEvaluator.evaluate(expression)
    }

    private fun findVisibleDefines(
        place: PsiElement,
        file: PsiFile,
    ): List<Define> {
        val defines = mutableListOf<Define>()

        defines += findDefinesInFile(file, place.textOffset)

        for (includedFile in GdsIncludeManager.getIncludedFiles(file)) {
            defines += findDefinesInFile(includedFile, null)
        }

        return defines
    }

    private fun findDefinesInFile(
        file: PsiFile,
        maxOffset: Int?,
    ): List<Define> =
        SyntaxTraverser
            .psiTraverser(file)
            .filter { it.node.elementType == GdsTypes.PP_DEFINE_LINE }
            .filter { maxOffset == null || it.textOffset < maxOffset }
            .mapNotNull { parseDefine(it) }
            .toList()

    private fun parseDefine(element: PsiElement): Define? {
        val match = DEFINE_REGEX.matchEntire(element.text) ?: return null
        return Define(
            element = element,
            name = match.groupValues[1],
            literalValue = match.groupValues[2].takeIf { isSimpleLiteral(it) },
        )
    }

    private fun isSimpleLiteral(text: String): Boolean {
        val trimmed = text.trim()
        if (trimmed == "true" || trimmed == "false") return true
        if (trimmed.matches(Regex("""[+-]?(?:\d+\.\d*|\.\d+)(?:[eE][+-]?\d+)?[fF]?"""))) return true
        if (trimmed.matches(Regex("""[+-]?\d+[eE][+-]?\d+[fF]?"""))) return true
        if (trimmed.matches(Regex("""[+-]?0[xX][0-9a-fA-F]+[uU]?"""))) return true
        if (trimmed.matches(Regex("""[+-]?\d+[uU]?"""))) return true
        return false
    }

    private data class Define(
        val element: PsiElement,
        val name: String,
        val literalValue: String?,
    )

    data class LiteralDefine(
        val name: String,
        val value: String,
        val element: PsiElement,
    )
}

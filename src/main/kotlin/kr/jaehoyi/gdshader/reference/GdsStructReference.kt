package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.resolve.GdsPreprocessorDefinitions
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsStructReference(
    element: PsiElement,
    textRange: TextRange,
) : GdsReference<PsiElement>(element, textRange) {
    private val key: String = element.text

    override fun resolve(): PsiElement? =
        GdsResolver.findStructDeclaration(element, key)
            ?: GdsPreprocessorDefinitions.resolveObjectMacro(element, key)

    override fun handleElementRename(newElementName: String): PsiElement? {
        val identifier = GdsElementFactory.createIdentifier(element.project, newElementName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
}

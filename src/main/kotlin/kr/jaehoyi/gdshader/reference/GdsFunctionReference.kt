package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsFunctionReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange) {
    
    private val key: String = element.text

    override fun resolve(): PsiElement? {
        var result: PsiElement? = null

        GdsResolver.processFunctionDeclaration(element) { element ->
            if (element.name == key) {
                result = element
                return@processFunctionDeclaration false
            }
            return@processFunctionDeclaration true
        }
        
        return result
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        val identifier = GdsElementFactory.createIdentifier(element.project, newElementName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
    
}
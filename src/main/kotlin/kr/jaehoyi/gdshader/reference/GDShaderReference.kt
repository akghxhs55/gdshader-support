package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import kr.jaehoyi.gdshader.psi.GDShaderElementFactory
import kr.jaehoyi.gdshader.resolve.GDShaderResolver

class GDShaderReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange) {
    
    private val key: String = element.text

    override fun resolve(): PsiElement? {
        var result: PsiElement? = null

        GDShaderResolver.processDeclarations(element) { element ->
            if (element.name == key) {
                result = element
                return@processDeclarations false
            }
            return@processDeclarations true
        }
        
        return result
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        val identifier = GDShaderElementFactory.createIdentifier(element.project, newElementName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
    
}
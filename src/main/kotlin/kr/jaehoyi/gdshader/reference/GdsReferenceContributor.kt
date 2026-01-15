package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        val includeLinePattern = PlatformPatterns.psiElement(GdsTypes.PP_INCLUDE_LINE)
        
        registrar.registerReferenceProvider(includeLinePattern,
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference?>
                    = getIncludeReference(element)
            }
        )
    }
    
    private fun getIncludeReference(element: PsiElement): Array<out PsiReference> {
        val text = element.text
        
        val regex = Regex("[\"']([^\"']*)[\"']")
        val matchResult = regex.find(text) ?: return PsiReference.EMPTY_ARRAY
        
        val pathPart = matchResult.groups[1] ?: return PsiReference.EMPTY_ARRAY
        
        val range = TextRange(pathPart.range.first, pathPart.range.last + 1)
        
        return arrayOf(GdsFileReference(element, range))
    }
    
}
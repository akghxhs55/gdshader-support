package kr.jaehoyi.gdshader.reference

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

        registrar.registerReferenceProvider(
            includeLinePattern,
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ): Array<out PsiReference?> = getIncludeReferences(element)
            },
        )
    }

    private fun getIncludeReferences(element: PsiElement): Array<out PsiReference> =
        GdsIncludeReferenceSet.create(element)?.allReferences
            ?: PsiReference.EMPTY_ARRAY
}

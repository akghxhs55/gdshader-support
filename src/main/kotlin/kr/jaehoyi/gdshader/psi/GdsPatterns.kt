package kr.jaehoyi.gdshader.psi

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext

object GdsPatterns {

    val TOP_LEVEL: ElementPattern<PsiElement> = PlatformPatterns.psiElement(GdsTypes.IDENTIFIER)
        .withParent(GdsFile::class.java)
        .with(object : PatternCondition<PsiElement>("AfterTopLevelSeperator") {
            override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
                val prevLeaf = PsiTreeUtil.prevVisibleLeaf(element)
                return prevLeaf == null || prevLeaf.elementType == GdsTypes.SEMICOLON || prevLeaf.elementType == GdsTypes.CURLY_BRACKET_CLOSE || prevLeaf.elementType in GdsTokenSets.COMMENTS
            }
        })
    
    val AFTER_PRECISION: ElementPattern<PsiElement> = StandardPatterns.or(
        PlatformPatterns.psiElement().afterLeaf(PlatformPatterns.psiElement(GdsTypes.PRECISION_HIGH)),
        PlatformPatterns.psiElement().afterLeaf(PlatformPatterns.psiElement(GdsTypes.PRECISION_MEDIUM)),
        PlatformPatterns.psiElement().afterLeaf(PlatformPatterns.psiElement(GdsTypes.PRECISION_LOW))
    )
    
}
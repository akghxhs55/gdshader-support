package kr.jaehoyi.gdshader.completion

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.StandardPatterns.or
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderTypes

object GDShaderPatterns {

    val TOP_LEVEL: ElementPattern<PsiElement> = psiElement()
        .withParent(GDShaderFile::class.java)
        .with(object : PatternCondition<PsiElement>("AfterTopLevelSeperator") {
            override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
                val prevLeaf = PsiTreeUtil.prevVisibleLeaf(element)
                return prevLeaf == null || prevLeaf.elementType == GDShaderTypes.SEMICOLON || prevLeaf.elementType == GDShaderTypes.CURLY_BRACKET_CLOSE
            }
        })
    
    val AFTER_PRECISION: ElementPattern<PsiElement> = or(
        psiElement().afterLeaf(psiElement(GDShaderTypes.PRECISION_HIGH)),
        psiElement().afterLeaf(psiElement(GDShaderTypes.PRECISION_MEDIUM)),
        psiElement().afterLeaf(psiElement(GDShaderTypes.PRECISION_LOW))
    )
    
}
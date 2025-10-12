package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.GDShaderUtil
import kr.jaehoyi.gdshader.psi.GDShaderBlockBody
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderForStatement
import kr.jaehoyi.gdshader.psi.GDShaderItem
import kr.jaehoyi.gdshader.psi.GDShaderType
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderTypeCompletionContributor : CompletionContributor() {
    
    private val typeProvider = object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            for (type in GDShaderUtil.builtinTypes) {
                result.addElement(
                    LookupElementBuilder.create(type)
                        .withBoldness(true)
                        .withTypeText("type")
                )
            }
        }
    }
    
    init {
        // Various contexts
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderType::class.java),
            typeProvider
        )
        
        // Function declaration return type
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderFile::class.java)
                .with(object : PatternCondition<PsiElement>("firstKeywordInItem") {
                    override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
                        val prev = PsiTreeUtil.skipWhitespacesAndCommentsBackward(element)
                        return prev == null || prev is GDShaderItem
                    }
                }),
            typeProvider
        )
        
        // Function declaration return type after precision
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderFile::class.java)
                .andOr(
                    psiElement().afterLeaf(psiElement(GDShaderTypes.PRECISION_HIGH)),
                    psiElement().afterLeaf(psiElement(GDShaderTypes.PRECISION_MEDIUM)),
                    psiElement().afterLeaf(psiElement(GDShaderTypes.PRECISION_LOW))
                ),
            typeProvider
        )
        
        // Local variable declaration
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderBlockBody::class.java),
            typeProvider
        )
        
        // Statement initializer
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderForStatement::class.java)
                .afterLeaf(psiElement(GDShaderTypes.PARENTHESIS_OPEN)),
            typeProvider
        )
    }
    
}
package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.isAncestor
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderForStatement
import kr.jaehoyi.gdshader.psi.GDShaderItem
import kr.jaehoyi.gdshader.psi.GDShaderParameter
import kr.jaehoyi.gdshader.psi.GDShaderStructMember
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVaryingDeclaration

class GDShaderPrecisionCompletionContributor : CompletionContributor() {
    
    private val provider = object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val precisions = listOf("lowp", "mediump", "highp")

            result.addAllElements(precisions.map {
                LookupElementBuilder.create(it)
                    .withBoldness(true)
            })
        }
    }
    
    init {
        // Function Declaration
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
            provider
        )
        
        // Uniform Declaration
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderUniformDeclaration::class.java)
                .afterLeaf(psiElement(GDShaderTypes.UNIFORM)),
            provider
        )

        // Constant Declaration
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderConstantDeclaration::class.java)
                .afterLeaf(psiElement(GDShaderTypes.CONST)),
            provider
        )

        // Varying Declaration
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderVaryingDeclaration::class.java)
                .afterLeaf(PlatformPatterns.or(
                    psiElement(GDShaderTypes.VARYING),
                    psiElement(GDShaderTypes.INTERPOLATION_FLAT),
                    psiElement(GDShaderTypes.INTERPOLATION_SMOOTH)
                )),
            provider
        )

        // Local Variable Declaration
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderBlock::class.java),
            provider
        )
        
        // Function Parameter
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderParameter::class.java)
                .and(
                    PlatformPatterns.or(
                        psiElement().afterLeaf(psiElement(GDShaderTypes.PARENTHESIS_OPEN)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.COMMA)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.CONST)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.ARG_IN)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.ARG_OUT)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.ARG_INOUT))
                    )
                ),
            provider
        )

        // Struct Member
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderStructMember::class.java)
                .with(object : PatternCondition<PsiElement>("firstKeywordInMember") {
                    override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
                        val parent = PsiTreeUtil.getParentOfType(element, GDShaderStructMember::class.java)
                            ?: return false
                        return parent.firstChild.isAncestor(element)
                    }
                }),
            provider
        )

        // For statement initializer
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderForStatement::class.java)
                .afterLeaf(psiElement(GDShaderTypes.PARENTHESIS_OPEN)),
            provider
        )
    }
    
}
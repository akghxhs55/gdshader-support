package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.GDShaderUtil
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderForStatement
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
        
        // Global context
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderFile::class.java),
            typeProvider
        )

        // Local variable declaration
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderBlock::class.java),
            typeProvider
        )
        
        // For statement initializer
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderForStatement::class.java)
                .afterLeaf(psiElement(GDShaderTypes.PARENTHESIS_OPEN)),
            typeProvider
        )
    }
}
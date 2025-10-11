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
import kr.jaehoyi.gdshader.psi.GDShaderStencilModeName
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderStencilModeCompletionContributor : CompletionContributor() {
    
    init {
        extend(
            CompletionType.BASIC,
            psiElement(GDShaderTypes.IDENTIFIER)
                .withParent(GDShaderStencilModeName::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    for (stencilMode in GDShaderUtil.stencilModeMap.flatMap { it.value }) {
                        result.addElement(
                            LookupElementBuilder.create(stencilMode)
                                .withTypeText("Stencil Mode")
                                .withBoldness(true)
                        )
                    }
                }
            }
        )
    }
    
}
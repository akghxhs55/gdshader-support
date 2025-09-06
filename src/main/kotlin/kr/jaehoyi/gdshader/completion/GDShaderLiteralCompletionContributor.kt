package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderVariableName

class GDShaderLiteralCompletionContributor : CompletionContributor() {
    private val provider = object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val keywords = listOf("true", "false")

            result.addAllElements(keywords.map {
                LookupElementBuilder.create(it)
                    .withBoldness(true)
            })
        }
    }
    
    init {
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderVariableName::class.java),
            provider
        )
        
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderBlock::class.java),
            provider
        )
    }
}
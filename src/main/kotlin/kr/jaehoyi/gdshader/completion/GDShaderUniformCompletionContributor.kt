package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderHint
import kr.jaehoyi.gdshader.psi.GDShaderHintSection
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderUniformCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                .withParent(GDShaderFile::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    result.addElement(
                        LookupElementBuilder.create("uniform")
                            .withBoldness(true)
                            .withTypeText("Keyword")
                    )
                }
            }
        )
        
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().inside(GDShaderHintSection::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    result.addElement(
                        LookupElementBuilder.create("hint_range")
                            .withBoldness(true)
                            .withTypeText("Hint")
                    )
                }
            }
        )
    }
}
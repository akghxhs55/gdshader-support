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

class GDShaderKeywordCompletionContributor : CompletionContributor() {
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
                    val globalKeywords = listOf(
                        "shader_type", "render_mode", "uniform_group", "uniform", "const", "varying", "struct",
                        "global", "instance"
                    )
                    
                    result.addAllElements(globalKeywords.map { 
                        LookupElementBuilder.create(it)
                            .withTypeText("Keyword")
                            .withBoldness(true)
                    })
                }
            }
        )
        
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
                    
                }
            }
        )
    }
}
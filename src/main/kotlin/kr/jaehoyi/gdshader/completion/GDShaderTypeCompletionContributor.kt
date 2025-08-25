package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.GDShaderUtil
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderType

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
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                .inside(GDShaderType::class.java),
            typeProvider
        )
        
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                .withParent(GDShaderFile::class.java),
            typeProvider
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                .inside(GDShaderBlock::class.java),
            typeProvider
        )
    }
}
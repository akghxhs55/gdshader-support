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
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeName
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderShaderTypeCompletionContributor : CompletionContributor() {
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
                    result.addElement(LookupElementBuilder
                        .create("shader_type")
                        .withBoldness(true)
                        .withTypeText("Keyword")
                    )
                }
            }
        )
        
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(GDShaderTypes.IDENTIFIER)
                .withParent(GDShaderShaderTypeName::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    for (type in GDShaderUtil.shaderTypes) {
                        result.addElement(
                            LookupElementBuilder.create(type)
                                .withBoldness(true)
                                .withTypeText("Shader Type")
                        )
                    }
                }
            }
        )
    }
}
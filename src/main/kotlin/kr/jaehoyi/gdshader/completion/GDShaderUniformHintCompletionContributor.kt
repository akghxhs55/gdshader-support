package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.GDShaderUtil
import kr.jaehoyi.gdshader.psi.GDShaderHintSection
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration

class GDShaderUniformHintCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderHintSection::class.java)
                .afterLeaf(PlatformPatterns.or(
                    psiElement(GDShaderTypes.COLON),
                    psiElement(GDShaderTypes.COMMA)
                )),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val uniformDeclaration = parameters.position.parentOfType<GDShaderUniformDeclaration>() ?: return
                    val typeName = uniformDeclaration.type?.text
                    
                    for (hint in GDShaderUtil.uniformHintMap[typeName] ?: emptySet()) {
                        result.addElement(
                            LookupElementBuilder.create(hint)
                                .withTypeText("Uniform Hint")
                                .withBoldness(true)
                        )
                    }
                }
            }
        )
    }
}
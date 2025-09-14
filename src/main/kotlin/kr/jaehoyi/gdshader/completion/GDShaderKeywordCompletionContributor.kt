package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderParameter
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderVaryingDeclaration

class GDShaderKeywordCompletionContributor : CompletionContributor() {
    init {
        // Global Keywords
        extend(
            CompletionType.BASIC,
            psiElement()
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
                            .withBoldness(true)
                    })
                }
            }
        )
        
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(GDShaderBlock::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    result.addElement(LookupElementBuilder.create("const").withBoldness(true))
                }
            }
        )
        
        // Varying Qualifiers (flat, smooth)
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderVaryingDeclaration::class.java)
                .afterLeaf(psiElement(GDShaderTypes.VARYING)),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val qualifiers = listOf("flat", "smooth")
                    
                    result.addAllElements(qualifiers.map { 
                        LookupElementBuilder.create(it)
                            .withBoldness(true)
                    })
                }
            }
        )
        
        // Function Parameter Qualifiers (const, in, out, inout)
        extend(
            CompletionType.BASIC,
            psiElement()
                .inside(GDShaderParameter::class.java)
                .and(
                    PlatformPatterns.or(
                        psiElement().afterLeaf(psiElement(GDShaderTypes.PARENTHESIS_OPEN)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.COMMA)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.CONST))
                    )
                ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val previousLeaf = PsiTreeUtil.prevVisibleLeaf(parameters.position)
                    
                    if (previousLeaf != null && previousLeaf.node.elementType == GDShaderTypes.CONST) {
                        result.addElement(
                            LookupElementBuilder.create("in")
                                .withBoldness(true)
                        )
                    }
                    else {
                        val paramQualifiers = listOf("const", "in", "out", "inout")
                        result.addAllElements(paramQualifiers.map {
                            LookupElementBuilder.create(it)
                                .withBoldness(true)
                        })
                    }
                }
            }
        )
    }
}
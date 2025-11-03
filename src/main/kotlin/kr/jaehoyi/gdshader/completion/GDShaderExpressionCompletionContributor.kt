package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameRef

class GDShaderExpressionCompletionContributor : CompletionContributor() {
    
    private val provider = object : CompletionProvider<CompletionParameters>() {
        
        private val booleanLookupElements: List<LookupElement> by lazy {
            val booleanKeywords = listOf("true", "false")
            booleanKeywords.map {
                LookupElementBuilder.create(it)
                    .withBoldness(true)
            }
        }
        
        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val position = parameters.position
            
            val ref = PsiTreeUtil.getParentOfType(position, GDShaderVariableNameRef::class.java)
            if (ref != null) {
                result.addAllElements(booleanLookupElements)
                return
            }
            
        }
    }
    
    init {
        extend(
            CompletionType.BASIC,
            psiElement(),
            provider
        )
    }
    
}
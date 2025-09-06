package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderCaseClause
import kr.jaehoyi.gdshader.psi.GDShaderControlStatement
import kr.jaehoyi.gdshader.psi.GDShaderIfStatement
import kr.jaehoyi.gdshader.psi.GDShaderStatementBody
import kr.jaehoyi.gdshader.psi.GDShaderSwitchBlock

class GDShaderControlFlowCompletionContributor : CompletionContributor() {
    init {
        // Basic control flow keywords
        extend(
            CompletionType.BASIC,
            psiElement()
                .withParent(psiElement(GDShaderBlock::class.java)),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val keywords = listOf(
                        "if", "for", "while", "do", "switch", "break", "continue", "return", "discard"
                    )
                    
                    result.addAllElements(keywords.map { 
                        LookupElementBuilder.create(it)
                            .withBoldness(true)
                    })
                }
            }
        )

        // "else" keyword
        extend(
            CompletionType.BASIC,
            psiElement().with(object : PatternCondition<PsiElement>("afterIfWithoutFinalElse") {
                override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
                    val previousSibling = PsiTreeUtil.skipWhitespacesAndCommentsBackward(element)

                    if (previousSibling !is GDShaderStatementBody) return false
                    
                    val controlStatement = PsiTreeUtil.getChildOfType(previousSibling, GDShaderControlStatement::class.java)
                        ?: return false
                    
                    var currentIfStatement = PsiTreeUtil.getChildOfType(controlStatement, GDShaderIfStatement::class.java)
                        ?: return false
                    
                    while (true) {
                        val elseClause = currentIfStatement.elseClause
                        
                        if (elseClause == null || elseClause.ifStatement == null) {
                            break
                        }
                        
                        currentIfStatement = elseClause.ifStatement!!
                    }
                    
                    val hasBody = currentIfStatement.statementBody != null
                    val hasElse = currentIfStatement.elseClause != null
                    
                    return hasBody && !hasElse
                }
            }),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    result.addElement(LookupElementBuilder.create("else")
                        .withBoldness(true)
                    )
                }
            }
        )
        
        // "case", "default" keyword
        extend(
            CompletionType.BASIC,
            psiElement()
                .with(
                    object : PatternCondition<PsiElement>("afterCaseClause" ) {
                        override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
                            val previousSibling = PsiTreeUtil.skipWhitespacesAndCommentsBackward(element)
                            return previousSibling == null || previousSibling is GDShaderCaseClause
                        }
                    }
                )
                .inside(GDShaderSwitchBlock::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val keywords = listOf("case", "default")
                    
                    result.addAllElements(keywords.map { 
                        LookupElementBuilder.create(it)
                            .withBoldness(true)
                    })
                }
            }
        )
    }
}
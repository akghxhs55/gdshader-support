package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter
import kr.jaehoyi.gdshader.psi.GDShaderFunctionCall
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration

class GDShaderFunctionAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderFunctionDeclaration -> annotateFunctionDeclaration(element, holder)
            is GDShaderFunctionCall -> annotateFunctionCall(element, holder)
            else -> return
        }
    }
    
    private fun annotateFunctionDeclaration(element: GDShaderFunctionDeclaration, holder: AnnotationHolder) {
        val functionNameElement = element.functionName
        
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(functionNameElement)
            .textAttributes(GDShaderSyntaxHighlighter.FUNCTION)
            .create()
    }
    
    private fun annotateFunctionCall(element: GDShaderFunctionCall, holder: AnnotationHolder) {
        val functionNameElement = element.functionName

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(functionNameElement)
            .textAttributes(GDShaderSyntaxHighlighter.FUNCTION)
            .create()
    }
}
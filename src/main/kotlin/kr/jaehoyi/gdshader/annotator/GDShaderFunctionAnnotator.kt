package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter
import kr.jaehoyi.gdshader.psi.GDShaderFunctionCall
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderFunctionAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderFunctionDeclaration -> annotateFunctionDeclaration(element, holder)
            is GDShaderFunctionCall -> annotateFunctionCall(element, holder)
            else -> return
        }
    }
    
    private fun annotateFunctionDeclaration(element: GDShaderFunctionDeclaration, holder: AnnotationHolder) {
        val functionNameElement = element.node.findChildByType(GDShaderTypes.IDENTIFIER)?.psi
        
        if (functionNameElement == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Function declaration must have a name")
                .range(element)
                .create()
            return
        }
        
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
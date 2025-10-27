package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter
import kr.jaehoyi.gdshader.psi.GDShaderFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderFunctionNameRef

class GDShaderFunctionAnnotator : Annotator {
    
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderFunctionNameDecl -> annotateFunctionNameDecl(element, holder)
            is GDShaderFunctionNameRef -> annotateFunctionNameDecl(element, holder)
            else -> return
        }
    }
    
    private fun annotateFunctionNameDecl(element: GDShaderFunctionNameDecl, holder: AnnotationHolder) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(GDShaderSyntaxHighlighter.FUNCTION)
            .create()
    }
    
    private fun annotateFunctionNameDecl(element: GDShaderFunctionNameRef, holder: AnnotationHolder) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(GDShaderSyntaxHighlighter.FUNCTION)
            .create()
    }
    
}
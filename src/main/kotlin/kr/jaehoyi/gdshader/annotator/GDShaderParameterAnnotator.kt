package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration

class GDShaderParameterAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderFunctionDeclaration -> annotateFunctionDeclaration(element, holder)
            else -> return
        }
    }

    private fun annotateFunctionDeclaration(element: GDShaderFunctionDeclaration, holder: AnnotationHolder) {
        val parameters = element.parameterList?.parameterList
        if (parameters != null) {
            for (parameter in parameters) {
                val parameterName = parameter.parameterName ?: continue
                
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(parameterName)
                    .textAttributes(GDShaderSyntaxHighlighter.PARAMETER)
                    .create()
            }
        }
    }
}
package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GDShaderGlobalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderPrimary
import kr.jaehoyi.gdshader.psi.impl.GDShaderPsiImplUtil

class GDShaderVariableAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderGlobalVariableDeclaration -> annotateGlobalVariableDeclaration(element, holder)
            is GDShaderLocalVariableDeclaration -> annotateLocalVariableDeclaration(element, holder)
            is GDShaderPrimary -> annotatePrimary(element, holder)
            else -> return
        }
    }

    private fun annotateGlobalVariableDeclaration(element: GDShaderGlobalVariableDeclaration, holder: AnnotationHolder) {
        for (variableNameElement in GDShaderPsiImplUtil.getVariableNameElements(element)) {
            holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
                .range(variableNameElement)
                .textAttributes(kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter.GLOBAL_VARIABLE)
                .create()
        }
    }
    
    private fun annotateLocalVariableDeclaration(element: GDShaderLocalVariableDeclaration, holder: AnnotationHolder) {
        for (variableNameElement in GDShaderPsiImplUtil.getVariableNameElements(element)) {
            holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
                .range(variableNameElement)
                .textAttributes(kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter.LOCAL_VARIABLE)
                .create()
        }
    }
    
    private fun annotatePrimary(element: GDShaderPrimary, holder: AnnotationHolder) {
    // TODO: Highlight only if the variable is global
        
//        val variableName = element.variableName
//        if (variableName != null) {
//            holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
//                .range(element)
//                .textAttributes(kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter.GLOBAL_VARIABLE)
//                .create()
//        }
    }
}
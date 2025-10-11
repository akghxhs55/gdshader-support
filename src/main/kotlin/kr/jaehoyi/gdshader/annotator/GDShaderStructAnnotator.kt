package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GDShaderStructDeclaration

class GDShaderStructAnnotator : Annotator {
    
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderStructDeclaration -> annotateStructDeclaration(element, holder)
            else -> return
        }
    }
    
    private fun annotateStructDeclaration(element: GDShaderStructDeclaration, holder: AnnotationHolder) {
        val structName = element.structName ?: return
        holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
            .range(structName)
            .textAttributes(kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter.STRUCT)
            .create()
    }
    
}
package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GDShaderStructNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderStructNameRef

class GDShaderStructAnnotator : Annotator {
    
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderStructNameDecl -> annotateStructNameDecl(element, holder)
            is GDShaderStructNameRef -> annotateStructNameRef(element, holder)
            else -> return
        }
    }
    
    private fun annotateStructNameDecl(element: GDShaderStructNameDecl, holder: AnnotationHolder) {
        holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter.STRUCT)
            .create()
    }
    
    private fun annotateStructNameRef(element: GDShaderStructNameRef, holder: AnnotationHolder) {
        holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter.STRUCT)
            .create()
    }
    
}
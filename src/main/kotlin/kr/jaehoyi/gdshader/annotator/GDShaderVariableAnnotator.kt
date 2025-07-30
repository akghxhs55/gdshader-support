package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GDShaderGlobalVariableDeclaration
import kr.jaehoyi.gdshader.psi.impl.GDShaderPsiImplUtil

class GDShaderVariableAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GDShaderGlobalVariableDeclaration) {
            for (variableNameElement in GDShaderPsiImplUtil.getVariableNameElements(element)) {
                holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
                    .range(variableNameElement)
                    .textAttributes(kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter.GLOBAL_VARIABLE)
                    .create()
                
                println(variableNameElement.text)
            }
        }
    }
}
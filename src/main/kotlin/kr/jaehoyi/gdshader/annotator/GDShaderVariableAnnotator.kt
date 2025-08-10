package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVaryingDeclaration

class GDShaderVariableAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderUniformDeclaration -> annotateUniformDeclaration(element, holder)
            is GDShaderConstantDeclaration -> annotateConstantDeclaration(element, holder)
            is GDShaderVaryingDeclaration -> annotateVaryingDeclaration(element, holder)
            is GDShaderLocalVariableDeclaration -> annotateLocalVariableDeclaration(element, holder)
            else -> return
        }
    }
    
    private fun annotateUniformDeclaration(element: GDShaderUniformDeclaration, holder: AnnotationHolder) {
        val variableName = element.variableName
        
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(variableName.textRange)
            .textAttributes(GDShaderSyntaxHighlighter.UNIFORM_VARIABLE)
            .create()
    }
    
    private fun annotateConstantDeclaration(element: GDShaderConstantDeclaration, holder: AnnotationHolder) {
        val variableNames = element.variableDeclaratorList.variableDeclaratorList
        
        for (variableName in variableNames) {
            val nameElement = variableName.variableName
            
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(nameElement.textRange)
                .textAttributes(GDShaderSyntaxHighlighter.CONSTANT)
                .create()
        }
    }
    
    private fun annotateVaryingDeclaration(element: GDShaderVaryingDeclaration, holder: AnnotationHolder) {
        val variableName = element.variableName
        
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(variableName.textRange)
            .textAttributes(GDShaderSyntaxHighlighter.VARYING_VARIABLE)
            .create()
    }
    
    private fun annotateLocalVariableDeclaration(element: GDShaderLocalVariableDeclaration, holder: AnnotationHolder) {
        val variableNames = element.variableDeclaratorList.variableDeclaratorList
        
        for (variableName in variableNames) {
            val nameElement = variableName.variableName
            
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(nameElement.textRange)
                .textAttributes(GDShaderSyntaxHighlighter.LOCAL_VARIABLE)
                .create()
        }
    }
}
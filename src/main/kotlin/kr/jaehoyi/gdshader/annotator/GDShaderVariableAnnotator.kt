package kr.jaehoyi.gdshader.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVaryingVariableDeclaration

class GDShaderVariableAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GDShaderUniformDeclaration -> annotateUniformVariableDeclaration(element, holder)
            is GDShaderConstantDeclaration -> annotateConstVariableDeclaration(element, holder)
            is GDShaderVaryingVariableDeclaration -> annotateVaryingVariableDeclaration(element, holder)
            is GDShaderLocalVariableDeclaration -> annotateLocalVariableDeclaration(element, holder)
            else -> return
        }
    }
    
    private fun annotateUniformVariableDeclaration(element: GDShaderUniformDeclaration, holder: AnnotationHolder) {
        val variableName = element.variableName
        
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(variableName.textRange)
            .textAttributes(GDShaderSyntaxHighlighter.UNIFORM_VARIABLE)
            .create()
    }
    
    private fun annotateConstVariableDeclaration(element: GDShaderConstantDeclaration, holder: AnnotationHolder) {
        val variableNames = element.variableDeclaratorList.variableDeclaratorList
        
        for (variableName in variableNames) {
            val nameElement = variableName.variableName
            
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(nameElement.textRange)
                .textAttributes(GDShaderSyntaxHighlighter.CONSTANT)
                .create()
        }
    }
    
    private fun annotateVaryingVariableDeclaration(element: GDShaderVaryingVariableDeclaration, holder: AnnotationHolder) {
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
package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclarator
import kr.jaehoyi.gdshader.psi.GDShaderFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderFunctionNameRef
import kr.jaehoyi.gdshader.psi.GDShaderLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GDShaderParameter
import kr.jaehoyi.gdshader.psi.GDShaderStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderStructMemberNameRef
import kr.jaehoyi.gdshader.psi.GDShaderStructNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderStructNameRef
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameRef
import kr.jaehoyi.gdshader.psi.GDShaderVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GDShaderLightVariable

class GDShaderHighlightingAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val colorAttribute = when (element) {
            is GDShaderVariableNameDecl -> {
                when (element.parent) {
                    is GDShaderUniformDeclaration -> GDShaderSyntaxHighlighter.UNIFORM_VARIABLE
                    is GDShaderVaryingDeclaration -> GDShaderSyntaxHighlighter.VARYING_VARIABLE
                    is GDShaderConstantDeclarator -> GDShaderSyntaxHighlighter.CONSTANT
                    is GDShaderLocalVariableDeclarator -> GDShaderSyntaxHighlighter.LOCAL_VARIABLE
                    is GDShaderParameter -> GDShaderSyntaxHighlighter.PARAMETER
                    else -> return
                }
            }
            is GDShaderVariableNameRef -> {
                val target = element.reference.resolve() ?: return
                if (target is GDShaderLightVariable) {
                    if (target.variableSpec.isMutable) {
                        GDShaderSyntaxHighlighter.BUILTIN_VARIABLE
                    } else {
                        GDShaderSyntaxHighlighter.BUILTIN_CONSTANT
                    }
                }
                else {
                    when (target.parent) {
                        is GDShaderUniformDeclaration -> GDShaderSyntaxHighlighter.UNIFORM_VARIABLE
                        is GDShaderVaryingDeclaration -> GDShaderSyntaxHighlighter.VARYING_VARIABLE
                        is GDShaderConstantDeclarator -> GDShaderSyntaxHighlighter.CONSTANT
                        is GDShaderLocalVariableDeclarator -> GDShaderSyntaxHighlighter.LOCAL_VARIABLE
                        is GDShaderParameter -> GDShaderSyntaxHighlighter.PARAMETER
                        else -> {
                            return
                        }
                    }
                }
            }
            is GDShaderFunctionNameDecl -> GDShaderSyntaxHighlighter.FUNCTION
            is GDShaderFunctionNameRef -> {
                GDShaderSyntaxHighlighter.FUNCTION
            }
            is GDShaderStructNameDecl -> GDShaderSyntaxHighlighter.STRUCT
            is GDShaderStructNameRef -> GDShaderSyntaxHighlighter.STRUCT
            is GDShaderStructMemberNameDecl -> GDShaderSyntaxHighlighter.STRUCT_MEMBER
            is GDShaderStructMemberNameRef -> GDShaderSyntaxHighlighter.STRUCT_MEMBER
            else -> return
        }
        
        holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(colorAttribute)
            .create()
    }
    
}
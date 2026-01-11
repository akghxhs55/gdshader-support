package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructNameRef
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable

class GdsHighlightingAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val colorAttribute = when (element) {
            is GdsVariableNameDecl -> {
                when (element.parent) {
                    is GdsUniformDeclaration -> GdsSyntaxHighlighter.UNIFORM_VARIABLE
                    is GdsVaryingDeclaration -> GdsSyntaxHighlighter.VARYING_VARIABLE
                    is GdsConstantDeclarator -> GdsSyntaxHighlighter.CONSTANT
                    is GdsLocalVariableDeclarator -> GdsSyntaxHighlighter.LOCAL_VARIABLE
                    is GdsParameter -> GdsSyntaxHighlighter.PARAMETER
                    else -> return
                }
            }
            is GdsVariableNameRef -> {
                val target = element.reference.resolve() ?: return
                if (target is GdsLightVariable) {
                    if (target.variableSpec.isMutable) {
                        GdsSyntaxHighlighter.BUILTIN_VARIABLE
                    } else {
                        GdsSyntaxHighlighter.BUILTIN_CONSTANT
                    }
                }
                else {
                    when (target.parent) {
                        is GdsUniformDeclaration -> GdsSyntaxHighlighter.UNIFORM_VARIABLE
                        is GdsVaryingDeclaration -> GdsSyntaxHighlighter.VARYING_VARIABLE
                        is GdsConstantDeclarator -> GdsSyntaxHighlighter.CONSTANT
                        is GdsLocalVariableDeclarator -> GdsSyntaxHighlighter.LOCAL_VARIABLE
                        is GdsParameter -> GdsSyntaxHighlighter.PARAMETER
                        else -> {
                            return
                        }
                    }
                }
            }
            is GdsFunctionNameDecl -> GdsSyntaxHighlighter.FUNCTION
            is GdsFunctionNameRef -> {
                GdsSyntaxHighlighter.FUNCTION
            }
            is GdsStructNameDecl -> GdsSyntaxHighlighter.STRUCT
            is GdsStructNameRef -> GdsSyntaxHighlighter.STRUCT
            is GdsStructMemberNameDecl -> GdsSyntaxHighlighter.STRUCT_MEMBER
            is GdsStructMemberNameRef -> GdsSyntaxHighlighter.STRUCT_MEMBER
            else -> return
        }
        
        holder.newSilentAnnotation(com.intellij.lang.annotation.HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(colorAttribute)
            .create()
    }
    
}
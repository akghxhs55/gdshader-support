package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructNameRef
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable

class GdsHighlightingAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (element.node?.elementType == GdsTypes.PP_INACTIVE_CODE) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES)
                .create()
            return
        }

        val colorAttribute =
            when (element) {
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
                        // Colour by "compile-time constant" (PI, TAU, E), not by writability: read-only
                        // inputs such as TIME are still variables.
                        if (target.variableSpec is ConstantSpec) {
                            GdsSyntaxHighlighter.BUILTIN_CONSTANT
                        } else {
                            GdsSyntaxHighlighter.BUILTIN_VARIABLE
                        }
                    } else {
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
                    if (element.reference.resolve() is GdsStructNameDecl) {
                        GdsSyntaxHighlighter.STRUCT
                    } else {
                        GdsSyntaxHighlighter.FUNCTION_CALL
                    }
                }
                is GdsStructNameDecl -> GdsSyntaxHighlighter.STRUCT
                is GdsStructNameRef -> {
                    // While code is being typed, any identifier in a type position is parsed as a struct
                    // name. Only colour it once it names a real struct; include files are exempt because
                    // their structs may be declared by the file that includes them.
                    if (!isIncludeFile(element) && element.reference.resolve() == null) return
                    GdsSyntaxHighlighter.STRUCT
                }
                is GdsStructMemberNameDecl -> GdsSyntaxHighlighter.STRUCT_MEMBER
                is GdsStructMemberNameRef -> GdsSyntaxHighlighter.STRUCT_MEMBER
                else -> return
            }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(colorAttribute)
            .create()
    }

    private fun isIncludeFile(element: PsiElement): Boolean = element.containingFile?.virtualFile?.extension == "gdshaderinc"
}

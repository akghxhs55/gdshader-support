package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.model.MemberAccessible
import kr.jaehoyi.gdshader.model.VectorType
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsPostfixExpr
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef
import kr.jaehoyi.gdshader.psi.GdsStructNameRef
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.resolve.GdsArrayMethodResolver
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives

class GdsUnresolvedReferenceAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.UNRESOLVED_REFERENCE)) return

        when (element) {
            is GdsVariableNameRef -> if (!isIncludeFile(element)) checkVariableRef(element, holder)
            is GdsFunctionNameRef -> checkFunctionRef(element, holder)
            is GdsStructNameRef -> if (!isIncludeFile(element)) checkStructRef(element, holder)
            is GdsStructMemberNameRef -> checkStructMemberRef(element, holder)
        }
    }

    private fun isIncludeFile(element: PsiElement): Boolean = element.containingFile?.virtualFile?.extension == "gdshaderinc"

    private fun checkVariableRef(
        element: GdsVariableNameRef,
        holder: AnnotationHolder,
    ) {
        if (element.reference.resolve() != null) return

        holder
            .newAnnotation(HighlightSeverity.WARNING, "Unresolved reference '${element.text}'")
            .range(element)
            .create()
    }

    private fun checkFunctionRef(
        element: GdsFunctionNameRef,
        holder: AnnotationHolder,
    ) {
        if (element.reference.resolve() != null) return

        val call = element.parent as? GdsFunctionCall
        if (call != null && GdsArrayMethodResolver.isMemberCall(call)) {
            if (GdsArrayMethodResolver.receiverType(call) == null) return
        } else if (isIncludeFile(element)) {
            return
        }

        holder
            .newAnnotation(HighlightSeverity.WARNING, "Unresolved reference '${element.text}'")
            .range(element)
            .create()
    }

    private fun checkStructRef(
        element: GdsStructNameRef,
        holder: AnnotationHolder,
    ) {
        if (element.reference.resolve() != null) return

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Unresolved reference '${element.text}'")
            .range(element)
            .create()
    }

    private fun checkStructMemberRef(
        element: GdsStructMemberNameRef,
        holder: AnnotationHolder,
    ) {
        // GdsDeclarationAnnotator reports a built-in define name as a parse error.
        if (element.text in GdsConditionalDirectives.BUILTIN_DEFINES) return
        val postfixExpr = element.parent as? GdsPostfixExpr ?: return
        val receiverType = GdsExpressionTypeInference.inferTypeBefore(postfixExpr, element) ?: return

        if (receiverType is MemberAccessible && receiverType.resolveMember(element.text) != null) return

        val message =
            if (receiverType is VectorType && receiverType.hasMixedSwizzleSets(element.text)) {
                "Cannot combine symbols from different sets in expression '.${element.text}'"
            } else {
                "Invalid member for '${receiverType.name}' expression: '.${element.text}'"
            }

        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }
}

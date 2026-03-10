package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.model.StructType
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsPostfixExpr
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef
import kr.jaehoyi.gdshader.psi.GdsStructNameRef
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsUnresolvedReferenceAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (isIncludeFile(element)) return

        when (element) {
            is GdsVariableNameRef -> checkVariableRef(element, holder)
            is GdsFunctionNameRef -> checkFunctionRef(element, holder)
            is GdsStructNameRef -> checkStructRef(element, holder)
            is GdsStructMemberNameRef -> checkStructMemberRef(element, holder)
        }
    }

    private fun isIncludeFile(element: PsiElement): Boolean {
        return element.containingFile?.virtualFile?.extension == "gdshaderinc"
    }

    private fun checkVariableRef(element: GdsVariableNameRef, holder: AnnotationHolder) {
        if (element.reference.resolve() != null) return

        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference '${element.text}'")
            .range(element)
            .create()
    }

    private fun checkFunctionRef(element: GdsFunctionNameRef, holder: AnnotationHolder) {
        val enclosingFunction = element.parentOfType<GdsFunctionDeclaration>() ?: return
        FunctionContext.fromText(enclosingFunction.functionNameDecl.text)

        if (element.reference.resolve() != null) return

        if (resolveAsStruct(element)) return

        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference '${element.text}'")
            .range(element)
            .create()
    }

    private fun resolveAsStruct(element: PsiElement): Boolean {
        val name = element.text
        var found = false
        GdsResolver.processStructDeclaration(element) { decl ->
            if (decl.name == name) {
                found = true
                false
            } else {
                true
            }
        }
        return found
    }

    private fun checkStructRef(element: GdsStructNameRef, holder: AnnotationHolder) {
        if (element.reference.resolve() != null) return

        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference '${element.text}'")
            .range(element)
            .create()
    }

    private fun checkStructMemberRef(element: GdsStructMemberNameRef, holder: AnnotationHolder) {
        // Only check struct member access when the receiver type is a known StructType.
        // Vector swizzles (vec.xyz), matrix indexing, etc. also use struct_member_name_ref
        // but GdsStructMemberReference only resolves StructType members, returning null for others.
        val postfixExpr = element.parent as? GdsPostfixExpr ?: return
        val receiverType = GdsExpressionTypeInference.inferTypeBefore(postfixExpr, element)
        if (receiverType !is StructType) return

        if (element.reference.resolve() != null) return

        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference '${element.text}'")
            .range(element)
            .create()
    }
}

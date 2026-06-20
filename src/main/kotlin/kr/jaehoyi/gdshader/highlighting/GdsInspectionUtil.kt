package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInsight.daemon.HighlightDisplayKey
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.psi.PsiElement

object GdsInspectionUtil {
    const val DECLARATION_VALIDATION = "GdsDeclarationValidation"
    const val EXPRESSION_VALIDATION = "GdsExpressionValidation"
    const val FUNCTION_CALL_VALIDATION = "GdsFunctionCallValidation"
    const val STATEMENT_VALIDATION = "GdsStatementValidation"
    const val UNRESOLVED_REFERENCE = "GdsUnresolvedReference"

    fun isEnabled(
        element: PsiElement,
        shortName: String,
    ): Boolean {
        val key = HighlightDisplayKey.find(shortName) ?: return true
        val profile = InspectionProjectProfileManager.getInstance(element.project).currentProfile
        return profile.isToolEnabled(key, element)
    }
}

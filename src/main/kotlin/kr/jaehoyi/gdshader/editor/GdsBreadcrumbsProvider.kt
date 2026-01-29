package kr.jaehoyi.gdshader.editor

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import kr.jaehoyi.gdshader.GdsLanguage
import kr.jaehoyi.gdshader.psi.*

class GdsBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages(): Array<Language> = arrayOf(GdsLanguage)

    override fun acceptElement(element: PsiElement): Boolean {
        return element is GdsFunctionDeclaration ||
                element is GdsStructDeclaration ||
                element is GdsUniformGroupDeclaration ||
                element is GdsRenderModeDeclaration ||
                element is GdsIfStatement ||
                element is GdsForStatement ||
                element is GdsWhileStatement ||
                element is GdsDoWhileStatement ||
                element is GdsSwitchStatement
    }

    override fun getElementInfo(element: PsiElement): String {
        return when (element) {
            is GdsFunctionDeclaration -> element.functionNameDecl.name
            is GdsStructDeclaration -> element.structNameDecl?.name ?: "struct"
            is GdsUniformGroupDeclaration -> element.uniformGroupNameList.firstOrNull()?.text ?: "group"
            is GdsRenderModeDeclaration -> "render_mode"
            is GdsIfStatement -> "if"
            is GdsForStatement -> "for"
            is GdsWhileStatement -> "while"
            is GdsDoWhileStatement -> "do...while"
            is GdsSwitchStatement -> "switch"
            else -> "..."
        }
    }
}

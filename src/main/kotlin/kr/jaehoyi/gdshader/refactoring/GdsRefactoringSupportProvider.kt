package kr.jaehoyi.gdshader.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl

class GdsRefactoringSupportProvider : RefactoringSupportProvider() {

    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean =
        element is GdsVariableNameDecl ||
        element is GdsFunctionNameDecl ||
        element is GdsStructNameDecl ||
        element is GdsStructMemberNameDecl
    
}
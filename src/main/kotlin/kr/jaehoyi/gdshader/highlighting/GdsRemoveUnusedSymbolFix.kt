package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import kr.jaehoyi.gdshader.GdsBundle
import kr.jaehoyi.gdshader.psi.GdsArgumentList
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaratorList
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaratorList
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsParameterList
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsRemoveUnusedSymbolFix(private val symbolName: String) : LocalQuickFix {

    override fun getName(): String =
        GdsBundle.message("inspection.unused.symbol.fix", symbolName)

    override fun getFamilyName(): String =
        GdsBundle.message("inspection.unused.symbol.fix.family")

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement ?: return

        when (element) {
            is GdsVariableNameDecl -> {
                when (val parent = element.parent) {
                    is GdsLocalVariableDeclarator -> removeLocalVariable(parent)
                    is GdsConstantDeclarator -> removeConstant(parent)
                    is GdsParameter -> removeParameter(parent, project)
                }
            }
            is GdsFunctionNameDecl -> removeFunction(element)
        }
    }

    private fun removeLocalVariable(declarator: GdsLocalVariableDeclarator) {
        val declaratorList = declarator.parent as? GdsLocalVariableDeclaratorList ?: return
        if (declaratorList.localVariableDeclaratorList.size == 1) {
            (declaratorList.parent as? GdsLocalVariableDeclaration)?.delete()
        } else {
            removeFromList(declarator)
        }
    }

    private fun removeConstant(declarator: GdsConstantDeclarator) {
        val declaratorList = declarator.parent as? GdsConstantDeclaratorList ?: return
        if (declaratorList.constantDeclaratorList.size == 1) {
            (declaratorList.parent as? GdsConstantDeclaration)?.delete()
        } else {
            removeFromList(declarator)
        }
    }

    private fun removeParameter(parameter: GdsParameter, project: Project) {
        val parameterList = parameter.parent as? GdsParameterList ?: return
        val functionDecl = parameterList.parent as? GdsFunctionDeclaration ?: return
        val functionNameDecl = functionDecl.functionNameDecl

        val paramIndex = parameterList.parameterList.indexOf(parameter)
        if (paramIndex < 0) return

        val scope = GlobalSearchScope.projectScope(project)
        ReferencesSearch.search(functionNameDecl, scope, false).forEach { ref ->
            val nameRef = ref.element as? GdsFunctionNameRef ?: return@forEach
            val functionCall = nameRef.parent as? GdsFunctionCall ?: return@forEach
            removeArgumentAt(functionCall.argumentList, paramIndex)
        }

        removeFromList(parameter)
    }

    private fun removeArgumentAt(argumentList: GdsArgumentList?, paramIndex: Int) {
        val args = argumentList?.initializerList ?: return
        if (paramIndex >= args.size) return
        val arg = args[paramIndex]
        if (args.size == 1) {
            arg.delete()
        } else {
            removeFromList(arg)
        }
    }

    private fun removeFromList(element: PsiElement) {
        val prevComma = findAdjacentComma(element, searchForward = false)
        val nextComma = findAdjacentComma(element, searchForward = true)

        if (prevComma != null) {
            prevComma.delete()
        } else {
            nextComma?.delete()
        }
        element.delete()
    }

    private fun findAdjacentComma(element: PsiElement, searchForward: Boolean): PsiElement? {
        var sibling = if (searchForward) element.nextSibling else element.prevSibling
        while (sibling != null) {
            if (sibling.node.elementType == GdsTypes.COMMA) return sibling
            if (sibling.text.isBlank()) {
                sibling = if (searchForward) sibling.nextSibling else sibling.prevSibling
                continue
            }
            break
        }
        return null
    }

    private fun removeFunction(nameDecl: GdsFunctionNameDecl) {
        (nameDecl.parent as? GdsFunctionDeclaration)?.delete()
    }
}
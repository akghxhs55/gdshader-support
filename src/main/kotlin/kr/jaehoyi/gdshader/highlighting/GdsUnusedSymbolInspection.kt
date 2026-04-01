package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import kr.jaehoyi.gdshader.GdsBundle
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVisitor
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

class GdsUnusedSymbolInspection : LocalInspectionTool() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor {
        return object : GdsVisitor() {
            override fun visitVariableNameDecl(element: GdsVariableNameDecl) {
                val parent = element.parent
                if (parent !is GdsLocalVariableDeclarator &&
                    parent !is GdsConstantDeclarator &&
                    parent !is GdsParameter
                ) {
                    return
                }

                val scope = GlobalSearchScope.projectScope(element.project)
                val hasReferences = ReferencesSearch.search(element, scope, false).findFirst() != null

                if (!hasReferences) {
                    val kind =
                        when (parent) {
                            is GdsParameter -> GdsBundle.message("inspection.unused.symbol.message.parameter")
                            is GdsConstantDeclarator -> GdsBundle.message("inspection.unused.symbol.message.constant")
                            else -> GdsBundle.message("inspection.unused.symbol.message.variable")
                        }
                    val fixes = arrayOf(GdsRemoveUnusedSymbolFix(element.name))
                    holder.registerProblem(
                        element,
                        "$kind ${GdsBundle.message("inspection.unused.symbol.message", element.name)}",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                        *fixes,
                    )
                }
            }

            override fun visitFunctionNameDecl(element: GdsFunctionNameDecl) {
                val parent = element.parent
                if (parent !is GdsFunctionDeclaration) return

                if (isProcessingFunction(element)) return

                val scope = GlobalSearchScope.projectScope(element.project)
                val hasReferences = ReferencesSearch.search(element, scope, false).findFirst() != null

                if (!hasReferences) {
                    val kind = GdsBundle.message("inspection.unused.symbol.message.function")
                    holder.registerProblem(
                        element,
                        "$kind ${GdsBundle.message("inspection.unused.symbol.message", element.name)}",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                        GdsRemoveUnusedSymbolFix(element.name),
                    )
                }
            }
        }
    }

    private fun isProcessingFunction(element: GdsFunctionNameDecl): Boolean {
        val functionContext = FunctionContext.fromText(element.name)
        val file = element.containingFile as? GdsFile ?: return false
        val shaderType = GdsPsiImplUtil.getShaderType(file) ?: return false
        val validFunctions = Builtins.PROCESSING_FUNCTIONS[shaderType] ?: return false
        return functionContext in validFunctions
    }
}

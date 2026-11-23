package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.RecursionManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.psi.GdsConstantEvaluator
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsExpression
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement

object GdsMacroExpansion {
    private val EXPANSION_KEY = Key.create<CachedValue<GdsExpression?>>("GDS_MACRO_EXPANSION")

    // A macro is not re-expanded inside its own expansion, matching C preprocessor semantics.
    private val typeGuard = RecursionManager.createGuard<GdsMacroElement>("gds.macro.type")
    private val evaluationGuard = RecursionManager.createGuard<GdsMacroElement>("gds.macro.evaluation")

    fun expansionSite(element: PsiElement): PsiElement {
        var current = element
        while (true) {
            val file = current.containingFile as? GdsFile ?: return current
            current = file.expansionContext ?: return current
        }
    }

    fun inferType(site: PsiElement): DataType? {
        val macro = resolveMacro(site) ?: return null
        return typeGuard.doPreventingRecursion(macro, false) {
            expand(site)?.let { GdsExpressionTypeInference.inferType(it) }
        }
    }

    fun evaluate(site: PsiElement): Any? {
        val macro = resolveMacro(site) ?: return null
        return evaluationGuard.doPreventingRecursion(macro, false) {
            expand(site)?.let { GdsConstantEvaluator.evaluate(it) }
        }
    }

    fun expand(site: PsiElement): GdsExpression? =
        CachedValuesManager.getCachedValue(site, EXPANSION_KEY) {
            CachedValueProvider.Result.create(computeExpansion(site), PsiModificationTracker.MODIFICATION_COUNT)
        }

    private fun resolveMacro(site: PsiElement): GdsMacroElement? =
        when (site) {
            is GdsVariableNameRef -> site.reference.resolve() as? GdsMacroElement
            is GdsFunctionCall -> site.functionNameRef?.reference?.resolve() as? GdsMacroElement
            else -> null
        }

    private fun computeExpansion(site: PsiElement): GdsExpression? {
        val macro = resolveMacro(site)?.macro ?: return null
        val text =
            when (site) {
                is GdsVariableNameRef -> macro.bodyText.takeIf { !macro.isFunctionLike }
                is GdsFunctionCall -> {
                    val arguments = site.argumentList?.initializerList?.map { it.text } ?: emptyList()
                    if (macro.isFunctionLike) {
                        macro.substitute(arguments)
                    } else {
                        "${macro.bodyText}(${arguments.joinToString(", ")})"
                    }
                }
                else -> null
            }
        if (text.isNullOrBlank()) return null

        val expression = GdsElementFactory.createExpression(site.project, text) ?: return null
        val file = expression.containingFile as? GdsFile ?: return null
        if (PsiTreeUtil.hasErrorElements(file)) return null
        file.expansionContext = site
        return expression
    }
}

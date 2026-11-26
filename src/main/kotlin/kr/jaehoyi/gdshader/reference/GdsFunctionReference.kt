package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.psi.GdsConstantReducer
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction
import kr.jaehoyi.gdshader.resolve.GdsArrayMethodResolver
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver
import kr.jaehoyi.gdshader.resolve.GdsPreprocessorDefinitions
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsFunctionReference(
    element: PsiElement,
    textRange: TextRange,
) : GdsReference<PsiElement>(element, textRange) {
    private val key: String = element.text

    override fun resolve(): PsiElement? {
        val call = findFunctionCall()
        if (call != null && GdsArrayMethodResolver.isMemberCall(call)) {
            val spec = GdsArrayMethodResolver.resolve(call) ?: return null
            return GdsLightFunction(PsiManager.getInstance(element.project), spec)
        }
        val candidates = mutableListOf<GdsFunction>()

        GdsResolver.processFunctionDeclaration(element) { func ->
            if (func.name == key) {
                candidates.add(func)
            }
            true
        }

        if (candidates.isEmpty()) {
            return GdsPreprocessorDefinitions.resolveCallableMacro(element, key)
                ?: GdsResolver.findStructDeclaration(element, key)
        }
        if (candidates.size == 1) return candidates.first() as? PsiElement

        val functionCall = findFunctionCall() ?: return candidates.first() as? PsiElement
        val argTypes = collectArgumentTypes(functionCall)

        val specsWithElements =
            candidates.mapNotNull { candidate ->
                candidate.functionSpec?.let { it to candidate }
            }

        if (specsWithElements.isEmpty()) return candidates.first() as? PsiElement

        val specs = specsWithElements.map { it.first }
        val resolved =
            if (argTypes != null) {
                GdsOverloadResolver.resolveFunctionOverload(specs, argTypes, GdsConstantReducer.reduceArguments(functionCall))
            } else {
                val argCount = functionCall.argumentList?.initializerList?.size ?: 0
                GdsOverloadResolver.filterByArgumentCount(specs, argCount).singleOrNull()
            } ?: return candidates.first() as? PsiElement

        return specsWithElements.firstOrNull { it.first === resolved }?.second as? PsiElement
            ?: candidates.first() as? PsiElement
    }

    private fun findFunctionCall(): GdsFunctionCall? {
        var current: PsiElement? = element
        while (current != null) {
            if (current is GdsFunctionCall) return current
            current = current.parent
        }
        return null
    }

    private fun collectArgumentTypes(functionCall: GdsFunctionCall): List<DataType>? {
        val argList = functionCall.argumentList ?: return emptyList()
        return argList.initializerList.map { initializer ->
            initializer.expression?.let { GdsExpressionTypeInference.inferType(it) } ?: return null
        }
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        val identifier = GdsElementFactory.createIdentifier(element.project, newElementName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
}

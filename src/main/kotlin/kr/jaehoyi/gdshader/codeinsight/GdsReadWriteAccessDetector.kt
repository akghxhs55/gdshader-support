package kr.jaehoyi.gdshader.codeinsight

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver

class GdsReadWriteAccessDetector : ReadWriteAccessDetector() {

    override fun isReadWriteAccessible(element: PsiElement): Boolean {
        return element is GdsVariableNameDecl
    }

    override fun isDeclarationWriteAccess(element: PsiElement): Boolean {
        return false
    }

    override fun getReferenceAccess(element: PsiElement, reference: PsiReference): Access {
        return getExpressionAccess(reference.element)
    }

    override fun getExpressionAccess(expression: PsiElement): Access {
        if (expression !is GdsVariableNameRef) return Access.Read

        getAssignmentAccess(expression)?.let { return it }
        getPostfixIncrementAccess(expression)?.let { return it }
        getUnaryIncrementAccess(expression)?.let { return it }
        getParameterQualifierAccess(expression)?.let { return it }

        return Access.Read
    }

    private fun getAssignmentAccess(ref: GdsVariableNameRef): Access? {
        val assignExpr = PsiTreeUtil.getParentOfType(ref, GdsAssignExpr::class.java) ?: return null
        val assignmentOperator = assignExpr.assignmentOperator ?: return null
        val logicOrExpr = assignExpr.logicOrExpr

        if (!PsiTreeUtil.isAncestor(logicOrExpr, ref, false)) return null

        return if (assignmentOperator.text == "=") Access.Write else Access.ReadWrite
    }

    private fun getPostfixIncrementAccess(ref: GdsVariableNameRef): Access? {
        val postfixExpr = PsiTreeUtil.getParentOfType(ref, GdsPostfixExpr::class.java) ?: return null
        val node = postfixExpr.node
        if (node.findChildByType(GdsTypes.OP_INCREMENT) != null ||
            node.findChildByType(GdsTypes.OP_DECREMENT) != null) {
            return Access.ReadWrite
        }
        return null
    }

    private fun getUnaryIncrementAccess(ref: GdsVariableNameRef): Access? {
        val unaryExpr = PsiTreeUtil.getParentOfType(ref, GdsUnaryExpr::class.java) ?: return null
        val node = unaryExpr.node
        if (node.findChildByType(GdsTypes.OP_INCREMENT) != null ||
            node.findChildByType(GdsTypes.OP_DECREMENT) != null) {
            return Access.ReadWrite
        }
        return null
    }

    private fun getParameterQualifierAccess(ref: GdsVariableNameRef): Access? {
        val initializer = PsiTreeUtil.getParentOfType(ref, GdsInitializer::class.java) ?: return null
        val argumentList = initializer.parent as? GdsArgumentList ?: return null
        val functionCall = argumentList.parent as? GdsFunctionCall ?: return null

        val argIndex = argumentList.initializerList.indexOf(initializer)
        if (argIndex < 0) return null

        val qualifier = resolveParameterQualifier(functionCall, argIndex) ?: return null

        return when (qualifier) {
            ParameterQualifier.OUT -> Access.Write
            ParameterQualifier.INOUT -> Access.ReadWrite
            else -> null
        }
    }

    private fun resolveParameterQualifier(functionCall: GdsFunctionCall, argIndex: Int): ParameterQualifier? {
        functionCall.functionNameRef?.let { nameRef ->
            val resolved = nameRef.reference.resolve()
            if (resolved is GdsFunctionNameDecl) {
                val functionDecl = resolved.parent as? GdsFunctionDeclaration ?: return null
                val param = functionDecl.parameterList?.parameterList?.getOrNull(argIndex) ?: return null
                val qualifierText = param.parameterQualifier?.text
                return ParameterQualifier.fromText(qualifierText)
            }

            return resolveBuiltinParameterQualifier(functionCall, nameRef.text, argIndex)
        }

        return null
    }

    private fun resolveBuiltinParameterQualifier(
        functionCall: GdsFunctionCall,
        functionName: String,
        argIndex: Int
    ): ParameterQualifier? {
        val shaderType = GdsPsiImplUtil.getShaderType(functionCall) ?: return null
        val functionContext = GdsPsiImplUtil.getFunctionContext(functionCall) ?: return null

        val candidates = Builtins.getFunctions(shaderType, functionContext, functionName) ?: return null

        val argTypes = collectArgumentTypes(functionCall)
        val resolvedSpec = GdsOverloadResolver.resolveOverload(candidates, argTypes) ?: return null

        return resolvedSpec.parameters.getOrNull(argIndex)?.qualifier
    }

    private fun collectArgumentTypes(functionCall: GdsFunctionCall): List<DataType> {
        val argList = functionCall.argumentList ?: return emptyList()
        return argList.initializerList.mapNotNull { initializer ->
            initializer.expression?.let { GdsExpressionTypeInference.inferType(it) }
        }
    }
}

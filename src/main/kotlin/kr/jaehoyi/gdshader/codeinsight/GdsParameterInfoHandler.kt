package kr.jaehoyi.gdshader.codeinsight

import com.intellij.lang.parameterInfo.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsArrayMethodResolver

class GdsParameterInfoHandler : ParameterInfoHandler<GdsArgumentList, FunctionSpec> {
    override fun findElementForParameterInfo(context: CreateParameterInfoContext): GdsArgumentList? {
        val argumentList = findArgumentList(context.file, context.offset) ?: return null

        val candidates = resolveCandidates(argumentList)

        if (candidates.isNotEmpty()) {
            context.itemsToShow = candidates.toTypedArray()
            return argumentList
        }
        return null
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): GdsArgumentList? = findArgumentList(context.file, context.offset)

    private fun findArgumentList(
        file: PsiFile,
        offset: Int,
    ): GdsArgumentList? {
        val element = file.findElementAt(offset) ?: return null

        var functionCall = PsiTreeUtil.getParentOfType(element, GdsFunctionCall::class.java, false)
        while (functionCall != null) {
            if (isInsideParentheses(functionCall, offset)) return functionCall.argumentList
            functionCall = PsiTreeUtil.getParentOfType(functionCall, GdsFunctionCall::class.java)
        }

        var prev: PsiElement? = element
        while (prev != null && prev !is GdsFile) {
            val elementType = prev.node.elementType
            if (elementType == GdsTypes.PARENTHESIS_OPEN) {
                val call = prev.parent as? GdsFunctionCall
                if (call != null) return call.argumentList
            }
            if (elementType == GdsTypes.SEMICOLON ||
                elementType == GdsTypes.CURLY_BRACKET_OPEN ||
                elementType == GdsTypes.CURLY_BRACKET_CLOSE ||
                elementType == GdsTypes.PARENTHESIS_CLOSE
            ) {
                break
            }
            prev = PsiTreeUtil.prevLeaf(prev)
        }
        return null
    }

    private fun isInsideParentheses(
        functionCall: GdsFunctionCall,
        offset: Int,
    ): Boolean {
        val open = functionCall.node.findChildByType(GdsTypes.PARENTHESIS_OPEN) ?: return false
        if (offset < open.textRange.endOffset) return false
        val close = functionCall.node.findChildByType(GdsTypes.PARENTHESIS_CLOSE) ?: return true
        return offset <= close.startOffset
    }

    override fun showParameterInfo(
        element: GdsArgumentList,
        context: CreateParameterInfoContext,
    ) {
        context.showHint(element, element.textRange.startOffset, this)
    }

    override fun updateParameterInfo(
        argumentList: GdsArgumentList,
        context: UpdateParameterInfoContext,
    ) {
        val offset = context.offset
        val index = ParameterInfoUtils.getCurrentParameterIndex(argumentList.node, offset, GdsTypes.COMMA)
        context.setCurrentParameter(index)
    }

    override fun updateUI(
        p: FunctionSpec,
        context: ParameterInfoUIContext,
    ) {
        val buffer = StringBuilder()

        var highlightStart = -1
        var highlightEnd = -1

        val currentParamIndex = context.currentParameterIndex

        for ((i, param) in p.parameters.withIndex()) {
            if (i > 0) buffer.append(", ")

            val start = buffer.length
            buffer.append(param.type.presentationText).append(" ").append(param.name)

            if (i == currentParamIndex) {
                highlightStart = start
                highlightEnd = buffer.length
            }
        }

        if (p.parameters.isEmpty()) {
            buffer.append("<no parameters>")
        }

        context.setupUIComponentPresentation(
            buffer.toString(),
            highlightStart,
            highlightEnd,
            !context.isUIComponentEnabled,
            false,
            false,
            context.defaultParameterColor,
        )
    }

    private fun resolveCandidates(argumentList: GdsArgumentList): List<FunctionSpec> {
        val functionCall = argumentList.parent as? GdsFunctionCall ?: return emptyList()
        if (GdsArrayMethodResolver.isMemberCall(functionCall)) {
            return listOfNotNull(GdsArrayMethodResolver.resolve(functionCall))
        }

        functionCall.type?.let { typeNode ->
            val baseType = GdsDataTypeFactory.createFromType(typeNode) ?: return emptyList()

            if (baseType is Instantiable) {
                return baseType.getConstructors()
            }
        }

        functionCall.functionNameRef?.let { nameRef ->
            val functionName = nameRef.text

            val resolved = nameRef.reference.resolve()
            if (resolved is GdsFunctionNameDecl) {
                val functionDecl = resolved.parent as? GdsFunctionDeclaration
                createFunctionSpec(functionDecl)?.let { return listOf(it) }
            }

            if (functionCall.arraySize == null) {
                GdsDataTypeFactory.createFromStructName(nameRef, functionName)?.let { return it.getConstructors() }
            }

            val shaderType = GdsPsiImplUtil.getShaderType(functionCall)
            val functionContext = GdsPsiImplUtil.getFunctionContext(functionCall) ?: FunctionContext.COMMON

            return Builtins.getFunctions(shaderType, functionContext, functionName) ?: emptyList()
        }

        return emptyList()
    }

    private fun createFunctionSpec(declaration: GdsFunctionDeclaration?): FunctionSpec? {
        if (declaration == null) return null

        val functionName = declaration.functionNameDecl.text
        val returnType = GdsDataTypeFactory.createFromFunctionDeclaration(declaration) ?: VoidType

        val parameters =
            declaration.parameterList?.parameterList?.mapNotNull { param ->
                val paramName = param.variableNameDecl.text
                val paramType = GdsDataTypeFactory.createFromParameter(param) ?: return@mapNotNull null
                ParameterSpec(paramName, paramType)
            } ?: emptyList()

        return FunctionSpec(functionName, returnType, parameters)
    }
}

package kr.jaehoyi.gdshader.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.impl.GdsBuiltinDefine
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsArrayMethodResolver
import kr.jaehoyi.gdshader.resolve.GdsMacroExpansion
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver

object GdsExpressionTypeInference {
    fun inferType(element: PsiElement): DataType? =
        when (element) {
            is GdsLiteral -> inferLiteralType(element)
            is GdsVariableNameRef -> inferVariableRefType(element)
            is GdsPrimary -> inferPrimaryType(element)
            is GdsFunctionCall -> inferFunctionCallType(element)
            is GdsPostfixExpr -> inferPostfixExprType(element)
            is GdsUnaryExpr -> inferUnaryExprType(element)
            is GdsMultiplicativeExpr ->
                inferBinaryChainType(element, element.unaryExprList, GdsTokenSets.MULTIPLICATIVE_OPERATORS)
            is GdsAdditiveExpr ->
                inferBinaryChainType(element, element.multiplicativeExprList, GdsTokenSets.ADDITIVE_OPERATORS)
            is GdsShiftExpr -> inferBinaryChainType(element, element.additiveExprList, GdsTokenSets.SHIFT_OPERATORS)
            is GdsRelationalExpr -> inferRelationalExprType(element)
            is GdsEqualityExpr -> inferEqualityExprType(element)
            is GdsBitwiseAndExpr ->
                inferBinaryChainType(element, element.equalityExprList, GdsTokenSets.BITWISE_OPERATORS)
            is GdsBitwiseXorExpr ->
                inferBinaryChainType(element, element.bitwiseAndExprList, GdsTokenSets.BITWISE_OPERATORS)
            is GdsBitwiseOrExpr ->
                inferBinaryChainType(element, element.bitwiseXorExprList, GdsTokenSets.BITWISE_OPERATORS)
            is GdsLogicAndExpr -> inferLogicAndExprType(element)
            is GdsLogicOrExpr -> inferLogicOrExprType(element)
            is GdsAssignExpr -> inferAssignExprType(element)
            is GdsConditionalExpr -> inferConditionalExprType(element)
            is GdsExpression -> inferExpressionType(element)
            is GdsInitializerList -> inferInitializerListType(element)
            else -> null
        }

    fun inferTypeBefore(
        postfixExpr: GdsPostfixExpr,
        targetChild: PsiElement,
    ): DataType? {
        var currentType = inferPrimaryType(postfixExpr.primary) ?: return null

        var memberIdx = 0
        var functionIdx = 0
        var indexIdx = 0

        for (child in postfixExpr.children) {
            if (child == postfixExpr.primary) continue
            // Leaf tokens such as the completion dot are not included in PSI children.
            if (child.textOffset >= targetChild.textOffset) return currentType

            when (child) {
                is GdsStructMemberNameRef -> {
                    val memberRefs = postfixExpr.structMemberNameRefList
                    if (memberIdx < memberRefs.size && memberRefs[memberIdx] == child) {
                        currentType = inferMemberAccessType(currentType, child.text) ?: return null
                        memberIdx++
                    }
                }
                is GdsFunctionCall -> {
                    val functionCalls = postfixExpr.functionCallList
                    if (functionIdx < functionCalls.size && functionCalls[functionIdx] == child) {
                        currentType = inferFunctionCallType(child) ?: return null
                        functionIdx++
                    }
                }
                is GdsExpression -> {
                    val indexExpressions = postfixExpr.expressionList
                    if (indexIdx < indexExpressions.size && indexExpressions[indexIdx] == child) {
                        currentType = inferIndexingType(currentType) ?: return null
                        indexIdx++
                    }
                }
            }
        }

        return currentType
    }

    private fun inferLiteralType(literal: GdsLiteral): DataType? =
        when {
            literal.node.findChildByType(GdsTypes.TRUE) != null -> BoolType
            literal.node.findChildByType(GdsTypes.FALSE) != null -> BoolType
            literal.node.findChildByType(GdsTypes.FLOAT_CONSTANT) != null -> FloatType.DEFAULT
            literal.node.findChildByType(GdsTypes.INT_CONSTANT) != null -> IntType
            literal.node.findChildByType(GdsTypes.UINT_CONSTANT) != null -> UIntType
            else -> null
        }

    private fun inferVariableRefType(varRef: GdsVariableNameRef): DataType? {
        val target = varRef.reference.resolve()
        if (target is GdsMacroElement) return GdsMacroExpansion.inferType(varRef)
        if (target is GdsBuiltinDefine) return IntType
        val resolved = target as? GdsVariable ?: return null
        if (resolved is GdsLightVariable && resolved.isTypeAmbiguous) return null
        return resolved.variableSpec?.type
    }

    private fun inferPrimaryType(primary: GdsPrimary): DataType? {
        primary.literal?.let { return inferLiteralType(it) }

        primary.variableNameRef?.let { return inferVariableRefType(it) }

        primary.functionCall?.let { return inferFunctionCallType(it) }

        primary.expression?.let { return inferType(it) }

        primary.initializerList?.let { return inferInitializerListType(it) }

        return null
    }

    private fun inferFunctionCallType(functionCall: GdsFunctionCall): DataType? {
        if (GdsArrayMethodResolver.isMemberCall(functionCall)) {
            return GdsArrayMethodResolver.resolve(functionCall)?.returnType
        }
        functionCall.type?.let { typeNode ->
            val baseType = GdsDataTypeFactory.createFromType(typeNode) ?: return null
            return inferConstructorType(functionCall, baseType)
        }

        functionCall.functionNameRef?.let { nameRef ->
            return inferFunctionReturnType(functionCall, nameRef)
        }

        return null
    }

    private fun inferConstructorType(
        functionCall: GdsFunctionCall,
        baseType: DataType,
    ): DataType? {
        val arraySize = functionCall.arraySize
        if (arraySize != null) {
            val size = GdsConstantEvaluator.evaluateArraySize(arraySize)
            return ArrayType(baseType, size)
        }

        if (baseType is StructType && baseType.hasUnresolvedMembers) return baseType

        if (baseType is Instantiable) {
            val argTypes = collectArgumentTypes(functionCall) ?: return null
            val constructors = baseType.getConstructors()

            if (constructors.isNotEmpty()) {
                val resolvedConstructor = GdsOverloadResolver.resolveConstructorOverload(constructors, argTypes)
                if (resolvedConstructor != null) {
                    return resolvedConstructor.returnType
                }
                return null
            }
        }

        return null
    }

    private fun inferFunctionReturnType(
        functionCall: GdsFunctionCall,
        nameRef: GdsFunctionNameRef,
    ): DataType? {
        val functionName = nameRef.text

        val resolved = nameRef.reference.resolve()
        if (resolved is GdsFunctionNameDecl) {
            val functionDecl = resolved.parent as? GdsFunctionDeclaration
            return functionDecl?.let { GdsDataTypeFactory.createFromFunctionDeclaration(it) }
        }
        if (resolved is GdsMacroElement) return GdsMacroExpansion.inferType(functionCall)

        GdsDataTypeFactory.createFromStructName(nameRef, functionName)?.let { structType ->
            return inferConstructorType(functionCall, structType)
        }

        return inferBuiltinFunctionReturnType(functionCall, functionName)
    }

    private fun inferBuiltinFunctionReturnType(
        functionCall: GdsFunctionCall,
        functionName: String,
    ): DataType? {
        val shaderType = GdsPsiImplUtil.getShaderType(functionCall)
        val functionContext = GdsPsiImplUtil.getFunctionContext(functionCall) ?: FunctionContext.COMMON

        val candidates = Builtins.getFunctions(shaderType, functionContext, functionName) ?: return null

        val argTypes = collectArgumentTypes(functionCall) ?: return null

        return GdsOverloadResolver
            .resolveFunctionCall(candidates, argTypes, GdsConstantReducer.reduceArguments(functionCall))
            ?.returnType
    }

    private fun collectArgumentTypes(functionCall: GdsFunctionCall): List<DataType>? {
        val argList = functionCall.argumentList ?: return emptyList()
        return argList.initializerList.map { initializer ->
            initializer.expression?.let { inferType(it) }
                ?: initializer.initializerList?.let { inferInitializerListType(it) }
                ?: return null
        }
    }

    private fun inferInitializerListType(initializerList: GdsInitializerList): DataType? {
        val expressions = initializerList.expressionList
        if (expressions.isEmpty()) return null

        val elementType = inferType(expressions.first()) ?: return null
        return ArrayType(elementType, expressions.size)
    }

    private fun inferPostfixExprType(postfixExpr: GdsPostfixExpr): DataType? {
        var currentType = inferPrimaryType(postfixExpr.primary) ?: return null

        val memberRefs = postfixExpr.structMemberNameRefList
        val functionCalls = postfixExpr.functionCallList
        val indexExpressions = postfixExpr.expressionList

        var memberIdx = 0
        var functionIdx = 0
        var indexIdx = 0

        for (child in postfixExpr.children) {
            when (child) {
                is GdsStructMemberNameRef -> {
                    if (memberIdx < memberRefs.size) {
                        currentType = inferMemberAccessType(currentType, memberRefs[memberIdx].text) ?: return null
                        memberIdx++
                    }
                }
                is GdsFunctionCall -> {
                    if (functionIdx < functionCalls.size) {
                        currentType = inferFunctionCallType(functionCalls[functionIdx]) ?: return null
                        functionIdx++
                    }
                }
                is GdsExpression -> {
                    if (indexIdx < indexExpressions.size) {
                        currentType = inferIndexingType(currentType) ?: return null
                        indexIdx++
                    }
                }
            }
        }

        return currentType
    }

    private fun inferMemberAccessType(
        baseType: DataType,
        memberName: String,
    ): DataType? =
        when (baseType) {
            is MemberAccessible -> baseType.resolveMember(memberName)
            else -> null
        }

    private fun inferIndexingType(baseType: DataType): DataType? =
        when (baseType) {
            is Indexable -> baseType.elementType
            else -> null
        }

    private fun inferUnaryExprType(unaryExpr: GdsUnaryExpr): DataType? {
        val hasNot = unaryExpr.node.findChildByType(GdsTypes.OP_NOT) != null
        val hasBitInvert = unaryExpr.node.findChildByType(GdsTypes.OP_BIT_INVERT) != null

        if (hasNot) return BoolType

        val operandType =
            unaryExpr.unaryExpr?.let { inferType(it) }
                ?: unaryExpr.postfixExpr?.let { inferPostfixExprType(it) }
                ?: return null

        if (hasBitInvert) {
            return if (GdsOperatorTypeRules.isInteger(operandType)) operandType else null
        }

        return operandType
    }

    private fun inferBinaryChainType(
        chain: PsiElement,
        operands: List<PsiElement>,
        operators: TokenSet,
    ): DataType? {
        var resultType = operands.firstOrNull()?.let { inferType(it) } ?: return null
        val operatorTokens = chain.node.getChildren(operators)

        for (index in 1 until operands.size) {
            val operator = operatorTokens.getOrNull(index - 1)?.elementType ?: return null
            val operandType = inferType(operands[index]) ?: return null
            resultType = GdsOperatorTypeRules.binaryResultType(operator, resultType, operandType) ?: return null
        }

        return resultType
    }

    private fun inferRelationalExprType(relationalExpr: GdsRelationalExpr): DataType? {
        val operands = relationalExpr.shiftExprList
        return if (operands.size >= 2) BoolType else operands.firstOrNull()?.let { inferType(it) }
    }

    private fun inferEqualityExprType(equalityExpr: GdsEqualityExpr): DataType? {
        val operands = equalityExpr.relationalExprList
        return if (operands.size >= 2) BoolType else operands.firstOrNull()?.let { inferType(it) }
    }

    private fun inferLogicAndExprType(logicAndExpr: GdsLogicAndExpr): DataType? {
        val operands = logicAndExpr.bitwiseOrExprList
        return if (operands.size >= 2) BoolType else operands.firstOrNull()?.let { inferType(it) }
    }

    private fun inferLogicOrExprType(logicOrExpr: GdsLogicOrExpr): DataType? {
        val operands = logicOrExpr.logicAndExprList
        return if (operands.size >= 2) BoolType else operands.firstOrNull()?.let { inferType(it) }
    }

    private fun inferAssignExprType(assignExpr: GdsAssignExpr): DataType? = assignExpr.conditionalExprList.firstOrNull()?.let { inferType(it) }

    private fun inferConditionalExprType(conditionalExpr: GdsConditionalExpr): DataType? {
        val expressions = conditionalExpr.expressionList

        if (expressions.size < 2) {
            return inferType(conditionalExpr.logicOrExpr)
        }

        val trueType = inferType(expressions[0])
        val falseType = expressions.getOrNull(1)?.let { inferType(it) }

        return if (trueType == falseType) trueType else trueType
    }

    private fun inferExpressionType(expression: GdsExpression): DataType? = inferType(expression.assignExpr)
}

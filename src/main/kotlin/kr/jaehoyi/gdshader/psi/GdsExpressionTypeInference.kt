package kr.jaehoyi.gdshader.psi

import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver

object GdsExpressionTypeInference {

    fun inferType(element: PsiElement): DataType? {
        return when (element) {
            is GdsLiteral -> inferLiteralType(element)
            is GdsVariableNameRef -> inferVariableRefType(element)
            is GdsPrimary -> inferPrimaryType(element)
            is GdsFunctionCall -> inferFunctionCallType(element)
            is GdsPostfixExpr -> inferPostfixExprType(element)
            is GdsUnaryExpr -> inferUnaryExprType(element)
            is GdsMultiplicativeExpr -> inferBinaryExprType(element)
            is GdsAdditiveExpr -> inferBinaryExprType(element)
            is GdsShiftExpr -> inferShiftExprType(element)
            is GdsRelationalExpr -> inferRelationalExprType(element)
            is GdsEqualityExpr -> inferEqualityExprType(element)
            is GdsBitwiseAndExpr -> inferBitwiseExprType(element)
            is GdsBitwiseXorExpr -> inferBitwiseExprType(element)
            is GdsBitwiseOrExpr -> inferBitwiseExprType(element)
            is GdsLogicAndExpr -> inferLogicAndExprType(element)
            is GdsLogicOrExpr -> inferLogicOrExprType(element)
            is GdsAssignExpr -> inferAssignExprType(element)
            is GdsConditionalExpr -> inferConditionalExprType(element)
            is GdsExpression -> inferExpressionType(element)
            else -> null
        }
    }

    private fun inferLiteralType(literal: GdsLiteral): DataType? {
        return when {
            literal.node.findChildByType(GdsTypes.TRUE) != null -> BoolType
            literal.node.findChildByType(GdsTypes.FALSE) != null -> BoolType
            literal.node.findChildByType(GdsTypes.FLOAT_CONSTANT) != null -> FloatType.DEFAULT
            literal.node.findChildByType(GdsTypes.INT_CONSTANT) != null -> IntType
            literal.node.findChildByType(GdsTypes.UINT_CONSTANT) != null -> UIntType
            else -> null
        }
    }

    private fun inferVariableRefType(varRef: GdsVariableNameRef): DataType? {
        val resolved = varRef.reference.resolve() as? GdsVariable ?: return null
        return resolved.variableSpec?.type
    }

    private fun inferPrimaryType(primary: GdsPrimary): DataType? {
        primary.literal?.let { return inferLiteralType(it) }

        primary.variableNameRef?.let { return inferVariableRefType(it) }

        primary.functionCall?.let { return inferFunctionCallType(it) }

        primary.expression?.let { return inferType(it) }

        return null
    }

    private fun inferFunctionCallType(functionCall: GdsFunctionCall): DataType? {
        functionCall.type?.let { typeNode ->
            return inferConstructorType(functionCall, typeNode)
        }

        functionCall.functionNameRef?.let { nameRef ->
            return inferFunctionReturnType(functionCall, nameRef)
        }

        return null
    }

    private fun inferConstructorType(functionCall: GdsFunctionCall, typeNode: GdsType): DataType? {
        val typeText = typeNode.text
        val baseType = Builtins.getType(typeText)
            ?: resolveStructType(typeNode)
            ?: return null

        val arraySize = functionCall.arraySize
        if (arraySize != null) {
            val size = parseArraySize(arraySize)
            return ArrayType(baseType, size)
        }

        return baseType
    }

    private fun resolveStructType(typeNode: GdsType): DataType? {
        val structRef = typeNode.structNameRef ?: return null
        val structDecl = structRef.reference.resolve() as? GdsStructNameDecl ?: return null
        val structDeclaration = structDecl.parent as? GdsStructDeclaration ?: return null

        val members = mutableMapOf<String, DataType>()
        structDeclaration.structBlock?.structMemberList?.structMemberList?.forEach { member ->
            val memberName = member.structMemberNameDecl?.text ?: return@forEach
            val memberType = resolveMemberType(member)
            if (memberType != null) {
                members[memberName] = memberType
            }
        }

        return StructType(structDecl.text, members)
    }

    private fun resolveMemberType(member: GdsStructMember): DataType? {
        val typeText = member.type.text
        val baseType = Builtins.getType(typeText) ?: return null

        val arraySizeList = member.arraySizeList
        if (arraySizeList.isNotEmpty()) {
            val size = parseArraySize(arraySizeList.first())
            return ArrayType(baseType, size)
        }

        return baseType
    }

    private fun inferFunctionReturnType(functionCall: GdsFunctionCall, nameRef: GdsFunctionNameRef): DataType? {
        val functionName = nameRef.text

        val resolved = nameRef.reference.resolve()
        if (resolved is GdsFunctionNameDecl) {
            val functionDecl = resolved.parent as? GdsFunctionDeclaration
            return functionDecl?.let { GdsDataTypeFactory.createFromFunctionDeclaration(it) }
        }

        return inferBuiltinFunctionReturnType(functionCall, functionName)
    }

    private fun inferBuiltinFunctionReturnType(functionCall: GdsFunctionCall, functionName: String): DataType? {
        val shaderType = GdsPsiImplUtil.getShaderType(functionCall) ?: return null
        val functionContext = GdsPsiImplUtil.getFunctionContext(functionCall) ?: return null

        val candidates = Builtins.getFunctions(shaderType, functionContext, functionName) ?: return null

        val argTypes = collectArgumentTypes(functionCall)

        val resolvedSpec = GdsOverloadResolver.resolveOverload(candidates, argTypes) ?: return null

        val returnType = resolvedSpec.returnType
        if (returnType is AliasType) {
            val paramTypes = resolvedSpec.parameters.map { it.type }
            return GdsOverloadResolver.resolveAliasType(returnType, argTypes, paramTypes) ?: returnType
        }

        return returnType
    }

    private fun collectArgumentTypes(functionCall: GdsFunctionCall): List<DataType> {
        val argList = functionCall.argumentList ?: return emptyList()
        return argList.initializerList.mapNotNull { initializer ->
            initializer.expression?.let { inferType(it) }
                ?: initializer.initializerList?.let { inferInitializerListType(it) }
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

    private fun inferMemberAccessType(baseType: DataType, memberName: String): DataType? {
        return when (baseType) {
            is MemberAccessible -> baseType.resolveMember(memberName)
            else -> null
        }
    }

    private fun inferIndexingType(baseType: DataType): DataType? {
        return when (baseType) {
            is Indexable -> baseType.elementType
            else -> null
        }
    }

    private fun inferUnaryExprType(unaryExpr: GdsUnaryExpr): DataType? {
        val hasNot = unaryExpr.node.findChildByType(GdsTypes.OP_NOT) != null
        val hasBitInvert = unaryExpr.node.findChildByType(GdsTypes.OP_BIT_INVERT) != null

        if (hasNot) return BoolType

        val operandType = unaryExpr.unaryExpr?.let { inferType(it) }
            ?: unaryExpr.postfixExpr?.let { inferPostfixExprType(it) }
            ?: return null

        if (hasBitInvert) {
            return if (isIntegerType(operandType)) operandType else null
        }

        return operandType
    }

    private fun inferBinaryExprType(binaryExpr: PsiElement): DataType? {
        return when (binaryExpr) {
            is GdsMultiplicativeExpr -> {
                val operands = binaryExpr.unaryExprList
                if (operands.size < 2) {
                    operands.firstOrNull()?.let { inferType(it) }
                } else {
                    val leftType = inferType(operands[0]) ?: return null
                    val rightType = inferType(operands[1]) ?: return null
                    inferArithmeticResultType(leftType, rightType)
                }
            }
            is GdsAdditiveExpr -> {
                val operands = binaryExpr.multiplicativeExprList
                if (operands.size < 2) {
                    operands.firstOrNull()?.let { inferType(it) }
                } else {
                    val leftType = inferType(operands[0]) ?: return null
                    val rightType = inferType(operands[1]) ?: return null
                    inferArithmeticResultType(leftType, rightType)
                }
            }
            else -> null
        }
    }

    private fun inferArithmeticResultType(left: DataType, right: DataType): DataType? {
        if (left == right) return left

        if (left is VectorType && right is Scalar) {
            if (areCompatibleScalarAndVector(right, left)) return left
        }
        if (right is VectorType && left is Scalar) {
            if (areCompatibleScalarAndVector(left, right)) return right
        }

        if (left is MatrixType && right is VectorType) {
            if (left.containerSize == right.containerSize) return right
        }

        if (left is VectorType && right is MatrixType) {
            if (left.containerSize == right.containerSize) return left
        }

        if (left is MatrixType && right is MatrixType) {
            if (left.containerSize == right.containerSize) return left
        }

        if (left is MatrixType && right is FloatType) return left
        if (right is MatrixType && left is FloatType) return right

        return null
    }

    private fun areCompatibleScalarAndVector(scalar: Scalar, vector: VectorType): Boolean {
        return when (scalar) {
            is FloatType -> vector.elementType is FloatType
            is IntType -> vector.elementType is IntType
            is UIntType -> vector.elementType is UIntType
            is BoolType -> vector.elementType is BoolType
        }
    }

    private fun inferShiftExprType(shiftExpr: GdsShiftExpr): DataType? {
        val operands = shiftExpr.additiveExprList
        if (operands.isEmpty()) return null
        return inferType(operands[0])
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

    private fun inferBitwiseExprType(bitwiseExpr: PsiElement): DataType? {
        val operands = when (bitwiseExpr) {
            is GdsBitwiseAndExpr -> bitwiseExpr.equalityExprList
            is GdsBitwiseXorExpr -> bitwiseExpr.bitwiseAndExprList
            is GdsBitwiseOrExpr -> bitwiseExpr.bitwiseXorExprList
            else -> return null
        }

        if (operands.isEmpty()) return null

        val leftType = inferType(operands[0]) ?: return null

        if (operands.size < 2) return leftType

        return if (isIntegerType(leftType)) leftType else null
    }

    private fun inferAssignExprType(assignExpr: GdsAssignExpr): DataType? {
        return inferType(assignExpr.logicOrExpr)
    }

    private fun inferConditionalExprType(conditionalExpr: GdsConditionalExpr): DataType? {
        val expressions = conditionalExpr.expressionList

        if (expressions.size < 2) {
            return inferType(conditionalExpr.assignExpr)
        }

        val trueType = inferType(expressions[0])
        val falseType = expressions.getOrNull(1)?.let { inferType(it) }

        return if (trueType == falseType) trueType else trueType
    }

    private fun inferExpressionType(expression: GdsExpression): DataType? {
        return inferConditionalExprType(expression.conditionalExpr)
    }

    private fun isIntegerType(type: DataType): Boolean {
        return when (type) {
            is IntType, is UIntType -> true
            is VectorType -> type.elementType is IntType || type.elementType is UIntType
            else -> false
        }
    }

    private fun parseArraySize(arraySize: GdsArraySize): Int? {
        return arraySize.text.filter { it.isDigit() }.toIntOrNull()
    }
}

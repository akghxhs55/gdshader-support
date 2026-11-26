package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable

class GdsExpressionAnnotator : Annotator {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.EXPRESSION_VALIDATION)) return

        when (element) {
            is GdsPostfixExpr -> checkPostfixExpr(element, holder)
            is GdsUnaryExpr -> checkUnaryExpr(element, holder)
            is GdsMultiplicativeExpr -> checkBinaryChain(element, element.unaryExprList, holder)
            is GdsAdditiveExpr -> checkBinaryChain(element, element.multiplicativeExprList, holder)
            is GdsShiftExpr -> checkBinaryChain(element, element.additiveExprList, holder)
            is GdsBitwiseAndExpr -> checkBinaryChain(element, element.equalityExprList, holder)
            is GdsBitwiseXorExpr -> checkBinaryChain(element, element.bitwiseAndExprList, holder)
            is GdsBitwiseOrExpr -> checkBinaryChain(element, element.bitwiseXorExprList, holder)
            is GdsRelationalExpr -> checkBinaryChain(element, element.shiftExprList, holder)
            is GdsEqualityExpr -> checkBinaryChain(element, element.relationalExprList, holder)
            is GdsLogicAndExpr -> checkBinaryChain(element, element.bitwiseOrExprList, holder)
            is GdsLogicOrExpr -> checkBinaryChain(element, element.logicAndExprList, holder)
            is GdsAssignExpr -> checkAssignExpr(element, holder)
            is GdsConditionalExpr -> checkConditionalExpr(element, holder)
            is GdsInitializerList -> if (element.parent is GdsPrimary) checkInitializerListElements(element, holder)
        }
    }

    // Inside an expression, Godot takes the element type from the first element.
    private fun checkInitializerListElements(
        element: GdsInitializerList,
        holder: AnnotationHolder,
    ) {
        val expressions = element.expressionList
        val elementType = expressions.firstOrNull()?.let(GdsExpressionTypeInference::inferType) ?: return
        for (expression in expressions.drop(1)) {
            val type = GdsExpressionTypeInference.inferType(expression) ?: continue
            if (type.name == elementType.name) continue
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Invalid assignment of '${type.name}' to '${elementType.name}'.")
                .range(expression)
                .create()
            return
        }
    }

    private fun checkUnaryExpr(
        element: GdsUnaryExpr,
        holder: AnnotationHolder,
    ) {
        val operand = element.unaryExpr ?: element.postfixExpr ?: return
        val operator = element.node.findChildByType(UNARY_OPERATOR_TOKENS) ?: return
        val operandType = GdsExpressionTypeInference.inferType(operand)

        if (operandType != null) {
            val isValid =
                when (operator.elementType) {
                    GdsTypes.OP_NOT -> operandType is BoolType
                    GdsTypes.OP_BIT_INVERT -> GdsOperatorTypeRules.isInteger(operandType)
                    else -> GdsOperatorTypeRules.isNegatable(operandType)
                }
            if (!isValid) {
                annotateInvalidUnaryOperator(element, operator.text, operandType, holder)
                return
            }
        }

        if (operator.elementType in INCREMENT_DECREMENT_TOKENS) {
            checkModifiable(element, operand, holder)
        }
    }

    private fun checkConditionalExpr(
        element: GdsConditionalExpr,
        holder: AnnotationHolder,
    ) {
        val expressions = element.expressionList
        if (expressions.size < 2) return

        val conditionType = GdsExpressionTypeInference.inferType(element.logicOrExpr) ?: return
        val trueType = GdsExpressionTypeInference.inferType(expressions[0]) ?: return
        val falseType = GdsExpressionTypeInference.inferType(expressions[1]) ?: return

        if (conditionType !is BoolType || trueType.name != falseType.name) {
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Invalid argument to ternary operator: '${conditionType.name}, ${trueType.name}, ${falseType.name}'",
                ).range(element)
                .create()
        }
    }

    private fun checkPostfixExpr(
        element: GdsPostfixExpr,
        holder: AnnotationHolder,
    ) {
        for (indexExpr in element.expressionList) {
            val indexType = GdsExpressionTypeInference.inferType(indexExpr) ?: continue
            if (!GdsOperatorTypeRules.isInteger(indexType)) {
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "Only integer expressions are allowed for indexing",
                    ).range(indexExpr)
                    .create()
            }
        }

        val operator = element.node.findChildByType(INCREMENT_DECREMENT_TOKENS) ?: return
        val operandType = GdsExpressionTypeInference.inferType(element)
        if (operandType != null && !GdsOperatorTypeRules.isNegatable(operandType)) {
            annotateInvalidUnaryOperator(element, operator.text, operandType, holder)
            return
        }

        val message = accessTargetError(element) ?: return
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private fun checkBinaryChain(
        element: PsiElement,
        operands: List<PsiElement>,
        holder: AnnotationHolder,
    ) {
        if (operands.size < 2) return
        val operators = element.node.getChildren(BINARY_OPERATOR_TOKENS)
        var leftType = GdsExpressionTypeInference.inferType(operands[0]) ?: return

        for (index in 1 until operands.size) {
            val operator = operators.getOrNull(index - 1) ?: return
            val rightType = GdsExpressionTypeInference.inferType(operands[index]) ?: return
            val result = GdsOperatorTypeRules.binaryResultType(operator.elementType, leftType, rightType)
            if (result == null) {
                annotateInvalidOperator(element, operator.text, leftType, rightType, holder)
                return
            }
            leftType = result
        }
    }

    private fun checkAssignExpr(
        element: GdsAssignExpr,
        holder: AnnotationHolder,
    ) {
        val operands = element.conditionalExprList
        val operators = element.assignmentOperatorList
        var accumulatedType = operands.firstOrNull()?.let(GdsExpressionTypeInference::inferType) ?: return

        for (index in operators.indices) {
            val target = operands.getOrNull(index) ?: continue
            val rhs = operands.getOrNull(index + 1) ?: continue
            val rhsType = GdsExpressionTypeInference.inferType(rhs) ?: continue
            accumulatedType =
                checkAssignment(
                    element,
                    target,
                    accumulatedType,
                    rhsType,
                    operators[index],
                    holder,
                )
        }
    }

    private fun checkAssignment(
        element: GdsAssignExpr,
        target: GdsConditionalExpr,
        lhsType: DataType,
        rhsType: DataType,
        operator: GdsAssignmentOperator,
        holder: AnnotationHolder,
    ): DataType {
        checkModifiable(element, target, holder)

        val operatorType = operator.node.firstChildNode.elementType
        if (operatorType == GdsTypes.OP_ASSIGN) {
            if (!lhsType.acceptsValueOf(rhsType)) {
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "Cannot assign a value of type '${rhsType.name}' to type '${lhsType.name}'",
                    ).range(element)
                    .create()
            }
            return lhsType
        }

        val resultType = GdsOperatorTypeRules.compoundAssignmentResultType(operatorType, lhsType, rhsType)
        if (resultType == null) {
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Invalid arguments to operator '${operator.text}': '${lhsType.name}, ${rhsType.name}'",
                ).range(element)
                .create()
        }
        return resultType ?: lhsType
    }

    private fun checkModifiable(
        element: PsiElement,
        target: PsiElement,
        holder: AnnotationHolder,
    ) {
        val message = assignmentTargetError(target) ?: return
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    // Returns null when the target is assignable or cannot be analyzed, e.g. macros.
    private fun assignmentTargetError(target: PsiElement?): String? =
        when (target) {
            is GdsExpression ->
                target.assignExpr.conditionalExprList
                    .singleOrNull()
                    ?.let(::assignmentTargetError)
            is GdsConditionalExpr ->
                if (target.expressionList.isNotEmpty()) CONSTANT_EXPRESSION else assignmentTargetError(target.logicOrExpr)
            is GdsLogicOrExpr -> singleOperandError(target.logicAndExprList)
            is GdsLogicAndExpr -> singleOperandError(target.bitwiseOrExprList)
            is GdsBitwiseOrExpr -> singleOperandError(target.bitwiseXorExprList)
            is GdsBitwiseXorExpr -> singleOperandError(target.bitwiseAndExprList)
            is GdsBitwiseAndExpr -> singleOperandError(target.equalityExprList)
            is GdsEqualityExpr -> singleOperandError(target.relationalExprList)
            is GdsRelationalExpr -> singleOperandError(target.shiftExprList)
            is GdsShiftExpr -> singleOperandError(target.additiveExprList)
            is GdsAdditiveExpr -> singleOperandError(target.multiplicativeExprList)
            is GdsMultiplicativeExpr -> singleOperandError(target.unaryExprList)
            is GdsUnaryExpr ->
                if (target.node.findChildByType(UNARY_OPERATOR_TOKENS) != null) {
                    CONSTANT_EXPRESSION
                } else {
                    assignmentTargetError(target.postfixExpr)
                }
            is GdsPostfixExpr ->
                if (target.node.findChildByType(INCREMENT_DECREMENT_TOKENS) != null) {
                    CONSTANT_EXPRESSION
                } else {
                    accessTargetError(target)
                }
            else -> null
        }

    private fun singleOperandError(operands: List<PsiElement>): String? =
        when (operands.size) {
            0 -> null
            1 -> assignmentTargetError(operands[0])
            else -> CONSTANT_EXPRESSION
        }

    private fun accessTargetError(postfixExpr: GdsPostfixExpr): String? {
        val accessors = postfixExpr.children.filter { it != postfixExpr.primary }
        for (accessor in accessors.asReversed()) {
            when (accessor) {
                is GdsFunctionCall -> return "Assignment to function"
                is GdsStructMemberNameRef ->
                    if (hasSwizzleDuplicates(postfixExpr, accessor)) {
                        return "Swizzling assignment contains duplicates"
                    }
            }
        }
        return primaryTargetError(postfixExpr.primary)
    }

    private fun hasSwizzleDuplicates(
        postfixExpr: GdsPostfixExpr,
        member: GdsStructMemberNameRef,
    ): Boolean {
        val baseType = GdsExpressionTypeInference.inferTypeBefore(postfixExpr, member)
        val name = member.text
        return baseType is VectorType && name.toSet().size != name.length
    }

    private fun primaryTargetError(primary: GdsPrimary): String? {
        primary.literal?.let { return CONSTANT_EXPRESSION }
        primary.functionCall?.let {
            return if (it.functionNameRef != null) "Assignment to function" else CONSTANT_EXPRESSION
        }
        primary.expression?.let { return assignmentTargetError(it) }

        return when (val resolved = primary.variableNameRef?.reference?.resolve()) {
            is GdsVariableNameDecl -> {
                val spec = resolved.variableSpec
                when {
                    spec == null || spec.isMutable -> null
                    spec is ConstantSpec -> "Constants cannot be modified"
                    spec is UniformSpec -> "Assignment to uniform"
                    else -> "Cannot assign to read-only variable"
                }
            }
            is GdsLightVariable ->
                when {
                    resolved.isAssignable -> null
                    resolved.variableSpec is ConstantSpec -> "Constants cannot be modified"
                    else -> "Cannot assign to read-only built-in '${resolved.name}'"
                }
            else -> null
        }
    }

    private fun annotateInvalidUnaryOperator(
        element: PsiElement,
        op: String,
        operandType: DataType,
        holder: AnnotationHolder,
    ) {
        holder
            .newAnnotation(
                HighlightSeverity.ERROR,
                "Invalid arguments to unary operator '$op': ${operandType.name}",
            ).range(element)
            .create()
    }

    private fun annotateInvalidOperator(
        element: PsiElement,
        op: String,
        leftType: DataType,
        rightType: DataType,
        holder: AnnotationHolder,
    ) {
        holder
            .newAnnotation(
                HighlightSeverity.ERROR,
                "Invalid arguments to operator '$op': '${leftType.name}, ${rightType.name}'",
            ).range(element)
            .create()
    }

    companion object {
        private const val CONSTANT_EXPRESSION = "Assignment to constant expression"

        private val INCREMENT_DECREMENT_TOKENS = TokenSet.create(GdsTypes.OP_INCREMENT, GdsTypes.OP_DECREMENT)

        private val UNARY_OPERATOR_TOKENS =
            TokenSet.create(
                GdsTypes.OP_NOT,
                GdsTypes.OP_BIT_INVERT,
                GdsTypes.OP_ADD,
                GdsTypes.OP_SUB,
                GdsTypes.OP_INCREMENT,
                GdsTypes.OP_DECREMENT,
            )

        private val BINARY_OPERATOR_TOKENS =
            TokenSet.create(
                GdsTypes.OP_MUL,
                GdsTypes.OP_DIV,
                GdsTypes.OP_MOD,
                GdsTypes.OP_ADD,
                GdsTypes.OP_SUB,
                GdsTypes.OP_SHIFT_LEFT,
                GdsTypes.OP_SHIFT_RIGHT,
                GdsTypes.OP_LESS,
                GdsTypes.OP_LESS_EQUAL,
                GdsTypes.OP_GREATER,
                GdsTypes.OP_GREATER_EQUAL,
                GdsTypes.OP_EQUAL,
                GdsTypes.OP_NOT_EQUAL,
                GdsTypes.OP_BIT_AND,
                GdsTypes.OP_BIT_XOR,
                GdsTypes.OP_BIT_OR,
                GdsTypes.OP_AND,
                GdsTypes.OP_OR,
            )
    }
}

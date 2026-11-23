package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
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
            is GdsMultiplicativeExpr -> checkBinaryChain(element, element.unaryExprList, holder, ::arithmeticResult)
            is GdsAdditiveExpr -> checkBinaryChain(element, element.multiplicativeExprList, holder, ::arithmeticResult)
            is GdsShiftExpr -> checkBinaryChain(element, element.additiveExprList, holder, ::integerResult)
            is GdsBitwiseAndExpr -> checkBinaryChain(element, element.equalityExprList, holder, ::integerResult)
            is GdsBitwiseXorExpr -> checkBinaryChain(element, element.bitwiseAndExprList, holder, ::integerResult)
            is GdsBitwiseOrExpr -> checkBinaryChain(element, element.bitwiseXorExprList, holder, ::integerResult)
            is GdsRelationalExpr -> checkBinaryChain(element, element.shiftExprList, holder, ::relationalResult)
            is GdsEqualityExpr -> checkBinaryChain(element, element.relationalExprList, holder, ::equalityResult)
            is GdsLogicAndExpr -> checkBinaryChain(element, element.bitwiseOrExprList, holder, ::logicResult)
            is GdsLogicOrExpr -> checkBinaryChain(element, element.logicAndExprList, holder, ::logicResult)
            is GdsAssignExpr -> checkAssignExpr(element, holder)
            is GdsConditionalExpr -> checkConditionalExpr(element, holder)
        }
    }

    private fun checkUnaryExpr(
        element: GdsUnaryExpr,
        holder: AnnotationHolder,
    ) {
        val operand = element.unaryExpr ?: element.postfixExpr ?: return
        val operandType = GdsExpressionTypeInference.inferType(operand) ?: return

        val hasNot = element.node.findChildByType(GdsTypes.OP_NOT) != null
        val hasBitInvert = element.node.findChildByType(GdsTypes.OP_BIT_INVERT) != null
        val hasMinus = element.node.findChildByType(GdsTypes.OP_SUB) != null
        val hasPlus = element.node.findChildByType(GdsTypes.OP_ADD) != null

        val op =
            when {
                hasNot && operandType !is BoolType -> "!"
                hasBitInvert && !isIntegerType(operandType) -> "~"
                hasMinus && !isNumericType(operandType) -> "-"
                hasPlus && !isNumericType(operandType) -> "+"
                else -> return
            }
        holder
            .newAnnotation(
                HighlightSeverity.ERROR,
                "Invalid arguments to unary operator '$op': ${operandType.name}",
            ).range(element)
            .create()
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
            if (!isIntegerType(indexType)) {
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "Only integer expressions are allowed for indexing",
                    ).range(indexExpr)
                    .create()
            }
        }
    }

    private fun checkBinaryChain(
        element: PsiElement,
        operands: List<PsiElement>,
        holder: AnnotationHolder,
        resultType: (DataType, DataType) -> DataType?,
    ) {
        if (operands.size < 2) return
        val operators = element.node.getChildren(OPERATOR_TOKENS)
        var leftType = GdsExpressionTypeInference.inferType(operands[0]) ?: return

        for (index in 1 until operands.size) {
            val rightType = GdsExpressionTypeInference.inferType(operands[index]) ?: return
            val result = resultType(leftType, rightType)
            if (result == null) {
                val op = operators.getOrNull(index - 1)?.text ?: "?"
                annotateInvalidOperator(element, op, leftType, rightType, holder)
                return
            }
            leftType = result
        }
    }

    private fun arithmeticResult(
        left: DataType,
        right: DataType,
    ): DataType? = GdsExpressionTypeInference.inferArithmeticResultType(left, right)

    private fun integerResult(
        left: DataType,
        right: DataType,
    ): DataType? = left.takeIf { isIntegerType(left) && isIntegerType(right) }

    private fun relationalResult(
        left: DataType,
        right: DataType,
    ): DataType? = BoolType.takeIf { left is Scalar && right is Scalar && left == right }

    private fun equalityResult(
        left: DataType,
        right: DataType,
    ): DataType? = BoolType.takeIf { left == right }

    private fun logicResult(
        left: DataType,
        right: DataType,
    ): DataType? = BoolType.takeIf { left is BoolType && right is BoolType }

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
        val varRef = PsiTreeUtil.findChildOfType(target, GdsVariableNameRef::class.java)
        val resolved = varRef?.reference?.resolve()
        if (resolved is GdsVariableNameDecl) {
            val spec = resolved.variableSpec
            if (spec != null && !spec.isMutable) {
                val message =
                    when (spec) {
                        is ConstantSpec -> "Constants cannot be modified"
                        is UniformSpec -> "Assignment to uniform"
                        else -> "Cannot assign to read-only variable"
                    }
                holder
                    .newAnnotation(HighlightSeverity.ERROR, message)
                    .range(element)
                    .create()
            }
        } else if (resolved is GdsLightVariable && !resolved.isAssignable) {
            val message =
                if (resolved.variableSpec is ConstantSpec) {
                    "Constants cannot be modified"
                } else {
                    "Cannot assign to read-only built-in '${resolved.name}'"
                }
            holder
                .newAnnotation(HighlightSeverity.ERROR, message)
                .range(element)
                .create()
        }

        val opText = operator.text

        if (opText == "=") {
            if (!isAssignable(lhsType, rhsType)) {
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "Cannot assign a value of type '${rhsType.name}' to type '${lhsType.name}'",
                    ).range(element)
                    .create()
            }
            return lhsType
        } else {
            val resultType =
                when (opText) {
                    "+=", "-=", "*=", "/=", "%=" -> GdsExpressionTypeInference.inferArithmeticResultType(lhsType, rhsType)
                    "<<=", ">>=", "&=", "^=", "|=" ->
                        lhsType.takeIf { isIntegerType(lhsType) && isIntegerType(rhsType) }
                    else -> lhsType
                }
            val isValid = resultType != null && isAssignable(lhsType, resultType)
            if (!isValid) {
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "Invalid arguments to operator '$opText': '${lhsType.name}, ${rhsType.name}'",
                    ).range(element)
                    .create()
            }
            return resultType ?: lhsType
        }
    }

    private fun isAssignable(
        target: DataType,
        source: DataType,
    ): Boolean {
        if (target == source) return true
        if (target.name == source.name) return true
        return false
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

    private fun isNumericType(type: DataType): Boolean =
        when (type) {
            is FloatType, is IntType, is UIntType -> true
            is VectorType -> type.elementType !is BoolType
            is MatrixType -> true
            else -> false
        }

    private fun isIntegerType(type: DataType): Boolean =
        when (type) {
            is IntType, is UIntType -> true
            is VectorType -> type.elementType is IntType || type.elementType is UIntType
            else -> false
        }

    companion object {
        private val OPERATOR_TOKENS =
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

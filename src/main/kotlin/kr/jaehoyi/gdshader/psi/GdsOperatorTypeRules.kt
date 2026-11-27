package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.model.*

object GdsOperatorTypeRules {
    fun binaryResultType(
        operator: IElementType,
        left: DataType,
        right: DataType,
    ): DataType? =
        when (operator) {
            GdsTypes.OP_ADD, GdsTypes.OP_SUB, GdsTypes.OP_DIV -> arithmeticResultType(left, right, isMultiplication = false)
            GdsTypes.OP_MUL -> arithmeticResultType(left, right, isMultiplication = true)
            GdsTypes.OP_MOD, GdsTypes.OP_SHIFT_LEFT, GdsTypes.OP_SHIFT_RIGHT -> integerResultType(left, right)
            GdsTypes.OP_BIT_AND, GdsTypes.OP_BIT_XOR, GdsTypes.OP_BIT_OR -> left.takeIf { isInteger(it) && it == right }
            GdsTypes.OP_LESS, GdsTypes.OP_LESS_EQUAL, GdsTypes.OP_GREATER, GdsTypes.OP_GREATER_EQUAL ->
                BoolType.takeIf { left == right && isNumericScalar(left) }
            GdsTypes.OP_EQUAL, GdsTypes.OP_NOT_EQUAL -> BoolType.takeIf { left == right && left !is ArrayType }
            GdsTypes.OP_AND, GdsTypes.OP_OR -> BoolType.takeIf { left is BoolType && right is BoolType }
            else -> null
        }

    fun compoundAssignmentResultType(
        operator: IElementType,
        target: DataType,
        value: DataType,
    ): DataType? {
        val result =
            if (operator in COMPOUND_BITWISE) {
                integerResultType(target, value)
            } else {
                binaryResultType(COMPOUND_TO_BINARY[operator] ?: return null, target, value)
            }
        return result?.takeIf { it == target }
    }

    fun isNegatable(type: DataType): Boolean =
        when (type) {
            is VectorType -> isNumericScalar(type.elementType)
            else -> isNumericScalar(type)
        }

    fun isInteger(type: DataType): Boolean =
        when (type) {
            is VectorType -> isIntegerScalar(type.elementType)
            else -> isIntegerScalar(type)
        }

    private fun arithmeticResultType(
        left: DataType,
        right: DataType,
        isMultiplication: Boolean,
    ): DataType? {
        if (left == right) return left.takeIf { isNegatable(it) || it is MatrixType }

        scalarBroadcastType(left, right)?.let { return it }
        scalarBroadcastType(right, left)?.let { return it }

        if (isMultiplication) {
            if (left is MatrixType && right is VectorType && right == left.colType) return right
            if (left is VectorType && right is MatrixType && left == right.colType) return left
        }

        return null
    }

    private fun scalarBroadcastType(
        scalar: DataType,
        container: DataType,
    ): DataType? =
        when {
            scalar !is Scalar || !isNumericScalar(scalar) -> null
            container is VectorType && container.elementType == scalar -> container
            container is MatrixType && scalar is FloatType -> container
            else -> null
        }

    private fun integerResultType(
        left: DataType,
        right: DataType,
    ): DataType? {
        if (!isInteger(left) || !isInteger(right)) return null
        if (left == right) return left
        if (left is VectorType && left.elementType == right) return left
        return null
    }

    private fun isNumericScalar(type: DataType): Boolean = type is FloatType || isIntegerScalar(type)

    private fun isIntegerScalar(type: DataType): Boolean = type is IntType || type is UIntType

    private val COMPOUND_BITWISE = setOf(GdsTypes.OP_ASSIGN_BIT_AND, GdsTypes.OP_ASSIGN_BIT_XOR, GdsTypes.OP_ASSIGN_BIT_OR)

    private val COMPOUND_TO_BINARY =
        mapOf(
            GdsTypes.OP_ASSIGN_ADD to GdsTypes.OP_ADD,
            GdsTypes.OP_ASSIGN_SUB to GdsTypes.OP_SUB,
            GdsTypes.OP_ASSIGN_MUL to GdsTypes.OP_MUL,
            GdsTypes.OP_ASSIGN_DIV to GdsTypes.OP_DIV,
            GdsTypes.OP_ASSIGN_MOD to GdsTypes.OP_MOD,
            GdsTypes.OP_ASSIGN_SHIFT_LEFT to GdsTypes.OP_SHIFT_LEFT,
            GdsTypes.OP_ASSIGN_SHIFT_RIGHT to GdsTypes.OP_SHIFT_RIGHT,
            GdsTypes.OP_ASSIGN_BIT_AND to GdsTypes.OP_BIT_AND,
            GdsTypes.OP_ASSIGN_BIT_XOR to GdsTypes.OP_BIT_XOR,
            GdsTypes.OP_ASSIGN_BIT_OR to GdsTypes.OP_BIT_OR,
        )
}

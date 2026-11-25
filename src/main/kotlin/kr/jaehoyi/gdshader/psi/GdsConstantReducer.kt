package kr.jaehoyi.gdshader.psi

import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.model.BoolType
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FloatType
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.MatrixType
import kr.jaehoyi.gdshader.model.Scalar
import kr.jaehoyi.gdshader.model.UIntType
import kr.jaehoyi.gdshader.model.VectorType
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement

// Mirrors ShaderLanguage::_reduce_expression and convert_constant in Godot 4.7.2.
// Scalars are Float (float), Int (int), Long in 0..0xFFFFFFFF (uint) and Boolean (bool).
object GdsConstantReducer {
    sealed interface Result

    data class Constant(
        val type: DataType,
        val values: List<Any>,
    ) : Result

    data object NotConstant : Result

    // Macros, or code Godot rejects for another reason.
    data object Unknown : Result

    fun reduce(element: PsiElement): Result =
        when (element) {
            is GdsExpression -> reduce(element.assignExpr)
            is GdsAssignExpr ->
                if (element.assignmentOperatorList.isNotEmpty()) {
                    Unknown
                } else {
                    element.conditionalExprList.firstOrNull()?.let { reduce(it) } ?: Unknown
                }
            is GdsConditionalExpr ->
                if (element.expressionList.isNotEmpty()) NotConstant else reduce(element.logicOrExpr)
            is GdsLogicOrExpr -> reduceChain(element.logicAndExprList)
            is GdsLogicAndExpr -> reduceChain(element.bitwiseOrExprList)
            is GdsBitwiseOrExpr -> reduceChain(element.bitwiseXorExprList)
            is GdsBitwiseXorExpr -> reduceChain(element.bitwiseAndExprList)
            is GdsBitwiseAndExpr -> reduceChain(element.equalityExprList)
            is GdsEqualityExpr -> reduceChain(element.relationalExprList)
            is GdsRelationalExpr -> reduceChain(element.shiftExprList)
            is GdsShiftExpr -> reduceChain(element.additiveExprList)
            is GdsAdditiveExpr -> reduceChain(element.multiplicativeExprList)
            is GdsMultiplicativeExpr -> reduceChain(element.unaryExprList)
            is GdsUnaryExpr -> reduceUnary(element)
            is GdsPostfixExpr -> reducePostfix(element)
            is GdsPrimary -> reducePrimary(element)
            else -> Unknown
        }

    fun convertConstant(
        constant: Constant,
        to: DataType,
    ): Boolean {
        if (constant.type.name == to.name) return true
        val from = constant.type as? Scalar ?: return false
        val toScalar = to as? Scalar ?: return false
        return convertScalar(constant.values.first(), from, toScalar) != null
    }

    // Godot evaluates binary operators but keeps them as operator nodes.
    private fun reduceChain(operands: List<PsiElement>): Result =
        when (operands.size) {
            0 -> Unknown
            1 -> reduce(operands[0])
            else -> nonConstant(operands.map { reduce(it) })
        }

    private fun reduceUnary(unary: GdsUnaryExpr): Result {
        val nested = unary.unaryExpr
        if (nested == null) {
            val postfix = unary.postfixExpr ?: return Unknown
            if (unary.node.findChildByType(GdsTypes.OP_INCREMENT) != null ||
                unary.node.findChildByType(GdsTypes.OP_DECREMENT) != null
            ) {
                return Unknown
            }
            return reduce(postfix)
        }

        val inner = reduce(nested)
        return when {
            unary.node.findChildByType(GdsTypes.OP_ADD) != null -> inner
            unary.node.findChildByType(GdsTypes.OP_SUB) != null -> negate(inner)
            else -> nonConstant(listOf(inner))
        }
    }

    private fun negate(result: Result): Result {
        if (result !is Constant) return result
        // Godot rejects negating bool and matrix types before reduction.
        if (result.type is MatrixType || scalarOf(result.type) is BoolType) return Unknown
        val values =
            result.values.map { value ->
                when (value) {
                    is Int -> -value
                    is Long -> (0L - value) and 0xFFFFFFFFL
                    is Float -> -value
                    else -> return Unknown
                }
            }
        return Constant(result.type, values)
    }

    private fun reducePostfix(postfix: GdsPostfixExpr): Result {
        val primary = reduce(postfix.primary)
        if (postfix.node.findChildByType(GdsTypes.OP_INCREMENT) != null ||
            postfix.node.findChildByType(GdsTypes.OP_DECREMENT) != null
        ) {
            return Unknown
        }
        val hasAccess =
            postfix.structMemberNameRefList.isNotEmpty() ||
                postfix.expressionList.isNotEmpty() ||
                postfix.functionCallList.isNotEmpty()
        return if (hasAccess) nonConstant(listOf(primary)) else primary
    }

    private fun reducePrimary(primary: GdsPrimary): Result {
        primary.literal?.let { return reduceLiteral(it) }
        primary.functionCall?.let { return reduceFunctionCall(it) }
        primary.variableNameRef?.let { ref ->
            return if (ref.reference.resolve() is GdsMacroElement) Unknown else NotConstant
        }
        primary.expression?.let { return reduce(it) }
        return Unknown
    }

    private fun reduceLiteral(literal: GdsLiteral): Result {
        val type = GdsExpressionTypeInference.inferType(literal) as? Scalar ?: return Unknown
        val value = GdsConstantEvaluator.evaluate(literal) ?: return Unknown
        val valid =
            when (type) {
                is FloatType -> value is Float
                is IntType -> value is Int
                is UIntType -> value is Long
                is BoolType -> value is Boolean
            }
        return if (valid) Constant(type, listOf(value)) else Unknown
    }

    private fun reduceFunctionCall(call: GdsFunctionCall): Result {
        val nameRef = call.functionNameRef
        if (nameRef != null) {
            return if (nameRef.reference.resolve() is GdsMacroElement) Unknown else NotConstant
        }
        if (call.arraySize != null) return Unknown

        val typeNode = call.type ?: return Unknown
        val type = GdsDataTypeFactory.createFromType(typeNode) ?: return Unknown
        val base = scalarOf(type) ?: return Unknown
        val cardinality = cardinalityOf(type) ?: return Unknown

        val initializers = call.argumentList?.initializerList ?: return Unknown
        val arguments = initializers.map { initializer -> initializer.expression?.let { reduce(it) } ?: Unknown }
        if (arguments.isEmpty() || arguments.any { it !is Constant }) return nonConstant(arguments)

        val values = mutableListOf<Any>()
        for (argument in arguments) {
            argument as Constant
            val argumentBase = scalarOf(argument.type) ?: return Unknown
            when {
                argumentBase.name == base.name -> values.addAll(argument.values)
                argument.type is Scalar -> values += convertScalar(argument.values.first(), argumentBase, base) ?: return NotConstant
                else -> return NotConstant
            }
        }

        if (values.size == 1) {
            val value = values.first()
            val result =
                if (type is MatrixType) {
                    val size = type.containerSize
                    List(size * size) { index -> if (index / size == index % size) value else 0f }
                } else {
                    List(cardinality) { value }
                }
            return Constant(type, result)
        }
        if (values.size != cardinality) {
            // mat3(mat2) and similar are valid but not reduced; other mismatches are invalid constructors.
            val fromMatrix = type is MatrixType && arguments.size == 1 && (arguments[0] as Constant).type is MatrixType
            return if (fromMatrix) NotConstant else Unknown
        }
        return Constant(type, values)
    }

    private fun nonConstant(results: List<Result>): Result = if (results.any { it is Unknown }) Unknown else NotConstant

    private fun convertScalar(
        value: Any,
        from: Scalar,
        to: Scalar,
    ): Any? =
        when {
            from.name == to.name -> value
            from is IntType && to is FloatType -> (value as Int).toFloat()
            from is UIntType && to is FloatType -> (value as Long).toFloat()
            from is IntType && to is UIntType -> (value as Int).takeIf { it >= 0 }?.toLong()
            from is UIntType && to is IntType -> (value as Long).takeIf { it <= 0x7FFFFFFFL }?.toInt()
            else -> null
        }

    private fun scalarOf(type: DataType): Scalar? =
        when (type) {
            is Scalar -> type
            is VectorType -> type.elementType
            is MatrixType -> FloatType.DEFAULT
            else -> null
        }

    private fun cardinalityOf(type: DataType): Int? =
        when (type) {
            is Scalar -> 1
            is VectorType -> type.containerSize
            is MatrixType -> type.containerSize * type.containerSize
            else -> null
        }
}

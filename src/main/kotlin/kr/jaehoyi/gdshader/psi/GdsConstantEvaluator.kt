package kr.jaehoyi.gdshader.psi

import com.intellij.psi.PsiElement

object GdsConstantEvaluator {

    fun evaluate(element: PsiElement): Any? {
        return evaluate(element, mutableSetOf())
    }

    fun evaluateAsInt(element: PsiElement): Int? {
        return when (val value = evaluate(element)) {
            is Int -> value
            is Long -> value.toInt()
            is Float -> value.toInt()
            is Double -> value.toInt()
            is Boolean -> if (value) 1 else 0
            else -> null
        }
    }

    fun evaluateAsFloat(element: PsiElement): Float? {
        return when (val value = evaluate(element)) {
            is Int -> value.toFloat()
            is Long -> value.toFloat()
            is Float -> value
            is Double -> value.toFloat()
            else -> null
        }
    }

    fun evaluateAsBoolean(element: PsiElement): Boolean? {
        return when (val value = evaluate(element)) {
            is Boolean -> value
            is Int -> value != 0
            is Long -> value != 0L
            is Float -> value != 0f
            is Double -> value != 0.0
            else -> null
        }
    }

    private fun evaluate(element: PsiElement, visited: MutableSet<PsiElement>): Any? {
        if (element in visited) return null
        visited.add(element)

        try {
            return when (element) {
                is GdsLiteral -> evaluateLiteral(element)
                is GdsVariableNameRef -> evaluateVariableRef(element, visited)
                is GdsPrimary -> evaluatePrimary(element, visited)
                is GdsFunctionCall -> evaluateFunctionCall(element, visited)
                is GdsPostfixExpr -> evaluatePostfixExpr(element, visited)
                is GdsUnaryExpr -> evaluateUnaryExpr(element, visited)
                is GdsMultiplicativeExpr -> evaluateMultiplicativeExpr(element, visited)
                is GdsAdditiveExpr -> evaluateAdditiveExpr(element, visited)
                is GdsShiftExpr -> evaluateShiftExpr(element, visited)
                is GdsRelationalExpr -> evaluateRelationalExpr(element, visited)
                is GdsEqualityExpr -> evaluateEqualityExpr(element, visited)
                is GdsBitwiseAndExpr -> evaluateBitwiseAndExpr(element, visited)
                is GdsBitwiseXorExpr -> evaluateBitwiseXorExpr(element, visited)
                is GdsBitwiseOrExpr -> evaluateBitwiseOrExpr(element, visited)
                is GdsLogicAndExpr -> evaluateLogicAndExpr(element, visited)
                is GdsLogicOrExpr -> evaluateLogicOrExpr(element, visited)
                is GdsConditionalExpr -> evaluateConditionalExpr(element, visited)
                is GdsAssignExpr -> evaluateAssignExpr(element, visited)
                is GdsExpression -> evaluateExpression(element, visited)
                is GdsInitializer -> evaluateInitializer(element, visited)
                else -> null
            }
        } finally {
            visited.remove(element)
        }
    }

    // ===== Literal Evaluation =====

    private fun evaluateLiteral(literal: GdsLiteral): Any? {
        val text = literal.text

        return when {
            literal.node.findChildByType(GdsTypes.TRUE) != null -> true
            literal.node.findChildByType(GdsTypes.FALSE) != null -> false
            literal.node.findChildByType(GdsTypes.FLOAT_CONSTANT) != null -> parseFloat(text)
            literal.node.findChildByType(GdsTypes.INT_CONSTANT) != null -> parseInt(text)
            literal.node.findChildByType(GdsTypes.UINT_CONSTANT) != null -> parseUInt(text)
            else -> null
        }
    }

    private fun parseFloat(text: String): Float? {
        val cleaned = text.trimEnd('f', 'F')
        return cleaned.toFloatOrNull()
    }

    private fun parseInt(text: String): Int? {
        return when {
            text.startsWith("0x") || text.startsWith("0X") ->
                text.substring(2).toIntOrNull(16)
            text.startsWith("0") && text.length > 1 && text.all { it.isDigit() } ->
                text.toIntOrNull(8)
            else -> text.toIntOrNull()
        }
    }

    private fun parseUInt(text: String): Long? {
        val cleaned = text.trimEnd('u', 'U')
        return when {
            cleaned.startsWith("0x") || cleaned.startsWith("0X") ->
                cleaned.substring(2).toLongOrNull(16)
            cleaned.startsWith("0") && cleaned.length > 1 && cleaned.all { it.isDigit() } ->
                cleaned.toLongOrNull(8)
            else -> cleaned.toLongOrNull()
        }
    }

    // ===== Variable Reference Evaluation =====

    private fun evaluateVariableRef(varRef: GdsVariableNameRef, visited: MutableSet<PsiElement>): Any? {
        val resolved = varRef.reference.resolve() ?: return null

        return when (resolved) {
            is GdsVariableNameDecl -> {
                when (val declarator = resolved.parent) {
                    is GdsConstantDeclarator -> {
                        val initializer = declarator.initializer ?: return null
                        evaluate(initializer, visited)
                    }
                    else -> null
                }
            }
            else -> null
        }
    }

    // ===== Primary Expression Evaluation =====

    private fun evaluatePrimary(primary: GdsPrimary, visited: MutableSet<PsiElement>): Any? {
        primary.literal?.let { return evaluateLiteral(it) }
        primary.variableNameRef?.let { return evaluateVariableRef(it, visited) }
        primary.functionCall?.let { return evaluateFunctionCall(it, visited) }
        primary.expression?.let { return evaluate(it, visited) }
        return null
    }

    // ===== Function Call Evaluation (Scalar Constructors) =====

    private fun evaluateFunctionCall(functionCall: GdsFunctionCall, visited: MutableSet<PsiElement>): Any? {
        val typeNode = functionCall.type ?: return null
        val typeName = typeNode.text

        val args = functionCall.argumentList?.initializerList ?: return null
        if (args.size != 1) return null

        val argValue = args.firstOrNull()?.let { evaluate(it, visited) } ?: return null

        return when (typeName) {
            "float" -> toFloat(argValue)
            "int" -> toInt(argValue)
            "uint" -> toUInt(argValue)
            "bool" -> toBoolean(argValue)
            else -> null
        }
    }

    // ===== Postfix Expression Evaluation =====

    private fun evaluatePostfixExpr(postfixExpr: GdsPostfixExpr, visited: MutableSet<PsiElement>): Any? {
        if (postfixExpr.structMemberNameRefList.isNotEmpty() ||
            postfixExpr.expressionList.isNotEmpty() ||
            postfixExpr.functionCallList.isNotEmpty()) {
            return null
        }
        return evaluatePrimary(postfixExpr.primary, visited)
    }

    // ===== Unary Expression Evaluation =====

    private fun evaluateUnaryExpr(unaryExpr: GdsUnaryExpr, visited: MutableSet<PsiElement>): Any? {
        unaryExpr.unaryExpr?.let { nested ->
            val value = evaluate(nested, visited) ?: return null

            val hasNot = unaryExpr.node.findChildByType(GdsTypes.OP_NOT) != null
            val hasBitInvert = unaryExpr.node.findChildByType(GdsTypes.OP_BIT_INVERT) != null
            val hasPlus = unaryExpr.node.findChildByType(GdsTypes.OP_ADD) != null
            val hasMinus = unaryExpr.node.findChildByType(GdsTypes.OP_SUB) != null

            return when {
                hasNot -> !(toBoolean(value) ?: return null)
                hasBitInvert -> when (value) {
                    is Int -> value.inv()
                    is Long -> value.inv()
                    else -> null
                }
                hasPlus -> value
                hasMinus -> when (value) {
                    is Int -> -value
                    is Long -> -value
                    is Float -> -value
                    is Double -> -value
                    else -> null
                }
                else -> value
            }
        }

        return unaryExpr.postfixExpr?.let { evaluatePostfixExpr(it, visited) }
    }

    // ===== Binary Expression Evaluation =====

    private fun evaluateMultiplicativeExpr(expr: GdsMultiplicativeExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.unaryExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        var result = evaluate(operands[0], visited) ?: return null

        var operandIndex = 1
        var node = expr.node.firstChildNode
        while (node != null && operandIndex < operands.size) {
            val elementType = node.elementType
            if (elementType == GdsTypes.OP_MUL || elementType == GdsTypes.OP_DIV || elementType == GdsTypes.OP_MOD) {
                val rightValue = evaluate(operands[operandIndex], visited) ?: return null
                operandIndex++

                result = when (elementType) {
                    GdsTypes.OP_MUL -> multiply(result, rightValue) ?: return null
                    GdsTypes.OP_DIV -> divide(result, rightValue) ?: return null
                    GdsTypes.OP_MOD -> modulo(result, rightValue) ?: return null
                    else -> return null
                }
            }
            node = node.treeNext
        }

        return result
    }

    private fun evaluateAdditiveExpr(expr: GdsAdditiveExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.multiplicativeExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        var result = evaluate(operands[0], visited) ?: return null

        var operandIndex = 1
        var node = expr.node.firstChildNode
        while (node != null && operandIndex < operands.size) {
            val elementType = node.elementType
            if (elementType == GdsTypes.OP_ADD || elementType == GdsTypes.OP_SUB) {
                val rightValue = evaluate(operands[operandIndex], visited) ?: return null
                operandIndex++

                result = when (elementType) {
                    GdsTypes.OP_ADD -> add(result, rightValue) ?: return null
                    GdsTypes.OP_SUB -> subtract(result, rightValue) ?: return null
                    else -> return null
                }
            }
            node = node.treeNext
        }

        return result
    }

    private fun evaluateShiftExpr(expr: GdsShiftExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.additiveExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        var result = evaluate(operands[0], visited) ?: return null

        var operandIndex = 1
        var node = expr.node.firstChildNode
        while (node != null && operandIndex < operands.size) {
            val elementType = node.elementType
            if (elementType == GdsTypes.OP_SHIFT_LEFT || elementType == GdsTypes.OP_SHIFT_RIGHT) {
                val rightValue = evaluate(operands[operandIndex], visited) ?: return null
                operandIndex++

                val shift = toInt(rightValue) ?: return null
                result = when (elementType) {
                    GdsTypes.OP_SHIFT_LEFT -> when (result) {
                        is Int -> result shl shift
                        is Long -> result shl shift
                        else -> return null
                    }
                    GdsTypes.OP_SHIFT_RIGHT -> when (result) {
                        is Int -> result shr shift
                        is Long -> result shr shift
                        else -> return null
                    }
                    else -> return null
                }
            }
            node = node.treeNext
        }

        return result
    }

    private fun evaluateRelationalExpr(expr: GdsRelationalExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.shiftExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        val left = evaluate(operands[0], visited) ?: return null
        val right = evaluate(operands[1], visited) ?: return null

        var node = expr.node.firstChildNode
        while (node != null) {
            val elementType = node.elementType
            val result = when (elementType) {
                GdsTypes.OP_LESS -> compare(left, right)?.let { it < 0 }
                GdsTypes.OP_LESS_EQUAL -> compare(left, right)?.let { it <= 0 }
                GdsTypes.OP_GREATER -> compare(left, right)?.let { it > 0 }
                GdsTypes.OP_GREATER_EQUAL -> compare(left, right)?.let { it >= 0 }
                else -> null
            }
            if (result != null) return result
            node = node.treeNext
        }

        return null
    }

    private fun evaluateEqualityExpr(expr: GdsEqualityExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.relationalExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        val left = evaluate(operands[0], visited) ?: return null
        val right = evaluate(operands[1], visited) ?: return null

        var node = expr.node.firstChildNode
        while (node != null) {
            val elementType = node.elementType
            val result = when (elementType) {
                GdsTypes.OP_EQUAL -> areEqual(left, right)
                GdsTypes.OP_NOT_EQUAL -> areEqual(left, right)?.let { !it }
                else -> null
            }
            if (result != null) return result
            node = node.treeNext
        }

        return null
    }

    private fun evaluateBitwiseAndExpr(expr: GdsBitwiseAndExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.equalityExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        var result = evaluate(operands[0], visited) ?: return null
        for (i in 1 until operands.size) {
            val right = evaluate(operands[i], visited) ?: return null
            result = bitwiseAnd(result, right) ?: return null
        }
        return result
    }

    private fun evaluateBitwiseXorExpr(expr: GdsBitwiseXorExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.bitwiseAndExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        var result = evaluate(operands[0], visited) ?: return null
        for (i in 1 until operands.size) {
            val right = evaluate(operands[i], visited) ?: return null
            result = bitwiseXor(result, right) ?: return null
        }
        return result
    }

    private fun evaluateBitwiseOrExpr(expr: GdsBitwiseOrExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.bitwiseXorExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        var result = evaluate(operands[0], visited) ?: return null
        for (i in 1 until operands.size) {
            val right = evaluate(operands[i], visited) ?: return null
            result = bitwiseOr(result, right) ?: return null
        }
        return result
    }

    private fun evaluateLogicAndExpr(expr: GdsLogicAndExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.bitwiseOrExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        for (operand in operands) {
            val value = toBoolean(evaluate(operand, visited)) ?: return null
            if (!value) return false
        }
        return true
    }

    private fun evaluateLogicOrExpr(expr: GdsLogicOrExpr, visited: MutableSet<PsiElement>): Any? {
        val operands = expr.logicAndExprList
        if (operands.isEmpty()) return null
        if (operands.size == 1) return evaluate(operands[0], visited)

        for (operand in operands) {
            val value = toBoolean(evaluate(operand, visited)) ?: return null
            if (value) return true
        }
        return false
    }

    // ===== Conditional Expression Evaluation =====

    private fun evaluateConditionalExpr(expr: GdsConditionalExpr, visited: MutableSet<PsiElement>): Any? {
        val expressions = expr.expressionList

        if (expressions.size < 2) {
            return evaluate(expr.assignExpr, visited)
        }

        val condition = toBoolean(evaluate(expr.assignExpr, visited)) ?: return null
        return if (condition) {
            evaluate(expressions[0], visited)
        } else {
            expressions.getOrNull(1)?.let { evaluate(it, visited) }
        }
    }

    private fun evaluateAssignExpr(expr: GdsAssignExpr, visited: MutableSet<PsiElement>): Any? {
        if (expr.assignmentOperator != null) return null
        return evaluate(expr.logicOrExpr, visited)
    }

    private fun evaluateExpression(expr: GdsExpression, visited: MutableSet<PsiElement>): Any? {
        return evaluate(expr.conditionalExpr, visited)
    }

    private fun evaluateInitializer(initializer: GdsInitializer, visited: MutableSet<PsiElement>): Any? {
        return initializer.expression?.let { evaluate(it, visited) }
    }

    // ===== Type Conversion Helpers =====

    private fun toInt(value: Any?): Int? = when (value) {
        is Int -> value
        is Long -> value.toInt()
        is Float -> value.toInt()
        is Double -> value.toInt()
        is Boolean -> if (value) 1 else 0
        else -> null
    }

    private fun toUInt(value: Any?): Long? = when (value) {
        is Int -> value.toLong() and 0xFFFFFFFFL
        is Long -> value and 0xFFFFFFFFL
        is Float -> value.toLong() and 0xFFFFFFFFL
        is Double -> value.toLong() and 0xFFFFFFFFL
        is Boolean -> if (value) 1L else 0L
        else -> null
    }

    private fun toFloat(value: Any?): Float? = when (value) {
        is Int -> value.toFloat()
        is Long -> value.toFloat()
        is Float -> value
        is Double -> value.toFloat()
        is Boolean -> if (value) 1f else 0f
        else -> null
    }

    private fun toBoolean(value: Any?): Boolean? = when (value) {
        is Boolean -> value
        is Int -> value != 0
        is Long -> value != 0L
        is Float -> value != 0f
        is Double -> value != 0.0
        else -> null
    }

    // ===== Arithmetic Operation Helpers =====

    private fun add(left: Any, right: Any): Any? {
        return when {
            left is Float || right is Float || left is Double || right is Double -> {
                val l = toFloat(left) ?: return null
                val r = toFloat(right) ?: return null
                l + r
            }
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                l + r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l + r
            }
        }
    }

    private fun subtract(left: Any, right: Any): Any? {
        return when {
            left is Float || right is Float || left is Double || right is Double -> {
                val l = toFloat(left) ?: return null
                val r = toFloat(right) ?: return null
                l - r
            }
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                l - r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l - r
            }
        }
    }

    private fun multiply(left: Any, right: Any): Any? {
        return when {
            left is Float || right is Float || left is Double || right is Double -> {
                val l = toFloat(left) ?: return null
                val r = toFloat(right) ?: return null
                l * r
            }
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                l * r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l * r
            }
        }
    }

    private fun divide(left: Any, right: Any): Any? {
        return when {
            left is Float || right is Float || left is Double || right is Double -> {
                val l = toFloat(left) ?: return null
                val r = toFloat(right) ?: return null
                if (r == 0f) return null
                l / r
            }
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                if (r == 0L) return null
                l / r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                if (r == 0) return null
                l / r
            }
        }
    }

    private fun modulo(left: Any, right: Any): Any? {
        return when {
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                if (r == 0L) return null
                l % r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                if (r == 0) return null
                l % r
            }
        }
    }

    private fun compare(left: Any, right: Any): Int? {
        return when {
            left is Float || right is Float || left is Double || right is Double -> {
                val l = toFloat(left) ?: return null
                val r = toFloat(right) ?: return null
                l.compareTo(r)
            }
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                l.compareTo(r)
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l.compareTo(r)
            }
        }
    }

    private fun areEqual(left: Any, right: Any): Boolean? {
        return when {
            left is Boolean && right is Boolean -> left == right
            left is Float || right is Float || left is Double || right is Double -> {
                val l = toFloat(left) ?: return null
                val r = toFloat(right) ?: return null
                l == r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l == r
            }
        }
    }

    private fun bitwiseAnd(left: Any, right: Any): Any? {
        return when {
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                l and r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l and r
            }
        }
    }

    private fun bitwiseXor(left: Any, right: Any): Any? {
        return when {
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                l xor r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l xor r
            }
        }
    }

    private fun bitwiseOr(left: Any, right: Any): Any? {
        return when {
            left is Long || right is Long -> {
                val l = toUInt(left) ?: return null
                val r = toUInt(right) ?: return null
                l or r
            }
            else -> {
                val l = toInt(left) ?: return null
                val r = toInt(right) ?: return null
                l or r
            }
        }
    }
}

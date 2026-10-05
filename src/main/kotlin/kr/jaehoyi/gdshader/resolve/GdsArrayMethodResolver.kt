package kr.jaehoyi.gdshader.resolve

import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsPostfixExpr

object GdsArrayMethodResolver {
    private val LENGTH = FunctionSpec("length", IntType, emptyList())

    fun isMemberCall(call: GdsFunctionCall): Boolean = call.parent is GdsPostfixExpr

    fun receiverType(call: GdsFunctionCall): DataType? {
        val postfix = call.parent as? GdsPostfixExpr ?: return null
        return GdsExpressionTypeInference.inferTypeBefore(postfix, call)
    }

    fun resolve(call: GdsFunctionCall): FunctionSpec? = if (call.functionNameRef?.text == "length" && receiverType(call) is ArrayType) LENGTH else null
}

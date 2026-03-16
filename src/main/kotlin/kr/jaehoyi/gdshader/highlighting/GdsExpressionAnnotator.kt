package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*

class GdsExpressionAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdsMultiplicativeExpr -> checkArithmeticExpr(element, element.unaryExprList, holder)
            is GdsAdditiveExpr -> checkArithmeticExpr(element, element.multiplicativeExprList, holder)
            is GdsShiftExpr -> checkShiftExpr(element, holder)
            is GdsBitwiseAndExpr -> checkBitwiseExpr(element, element.equalityExprList, holder)
            is GdsBitwiseXorExpr -> checkBitwiseExpr(element, element.bitwiseAndExprList, holder)
            is GdsBitwiseOrExpr -> checkBitwiseExpr(element, element.bitwiseXorExprList, holder)
            is GdsLogicAndExpr -> checkLogicExpr(element, element.bitwiseOrExprList, holder)
            is GdsLogicOrExpr -> checkLogicExpr(element, element.logicAndExprList, holder)
        }
    }

    private fun checkArithmeticExpr(element: PsiElement, operands: List<PsiElement>, holder: AnnotationHolder) {
        if (operands.size < 2) return
        val leftType = GdsExpressionTypeInference.inferType(operands[0]) ?: return
        val rightType = GdsExpressionTypeInference.inferType(operands[1]) ?: return

        if (GdsExpressionTypeInference.inferType(element) == null) {
            annotateInvalidOperator(element, leftType, rightType, holder)
        }
    }

    private fun checkShiftExpr(element: GdsShiftExpr, holder: AnnotationHolder) {
        val operands = element.additiveExprList
        if (operands.size < 2) return
        val leftType = GdsExpressionTypeInference.inferType(operands[0]) ?: return
        val rightType = GdsExpressionTypeInference.inferType(operands[1]) ?: return

        if (!isIntegerType(leftType) || !isIntegerType(rightType)) {
            annotateInvalidOperator(element, leftType, rightType, holder)
        }
    }

    private fun checkBitwiseExpr(element: PsiElement, operands: List<PsiElement>, holder: AnnotationHolder) {
        if (operands.size < 2) return
        val leftType = GdsExpressionTypeInference.inferType(operands[0]) ?: return
        val rightType = GdsExpressionTypeInference.inferType(operands[1]) ?: return

        if (!isIntegerType(leftType) || !isIntegerType(rightType)) {
            annotateInvalidOperator(element, leftType, rightType, holder)
        }
    }

    private fun checkLogicExpr(element: PsiElement, operands: List<PsiElement>, holder: AnnotationHolder) {
        if (operands.size < 2) return
        val leftType = GdsExpressionTypeInference.inferType(operands[0]) ?: return
        val rightType = GdsExpressionTypeInference.inferType(operands[1]) ?: return

        if (leftType !is BoolType || rightType !is BoolType) {
            annotateInvalidOperator(element, leftType, rightType, holder)
        }
    }

    private fun annotateInvalidOperator(element: PsiElement, leftType: DataType, rightType: DataType, holder: AnnotationHolder) {
        val op = findOperatorText(element)
        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Invalid arguments to operator '$op': '${leftType.name}, ${rightType.name}'"
        ).range(element).create()
    }

    private fun findOperatorText(element: PsiElement): String {
        val operatorTokens = TokenSet.create(
            GdsTypes.OP_MUL, GdsTypes.OP_DIV, GdsTypes.OP_MOD,
            GdsTypes.OP_ADD, GdsTypes.OP_SUB,
            GdsTypes.OP_SHIFT_LEFT, GdsTypes.OP_SHIFT_RIGHT,
            GdsTypes.OP_BIT_AND, GdsTypes.OP_BIT_XOR, GdsTypes.OP_BIT_OR,
            GdsTypes.OP_AND, GdsTypes.OP_OR
        )
        return element.node.getChildren(operatorTokens).firstOrNull()?.text ?: "?"
    }

    private fun isIntegerType(type: DataType): Boolean {
        return when (type) {
            is IntType, is UIntType -> true
            is VectorType -> type.elementType is IntType || type.elementType is UIntType
            else -> false
        }
    }
}

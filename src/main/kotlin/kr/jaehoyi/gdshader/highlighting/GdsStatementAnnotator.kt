package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.BoolType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.UIntType
import kr.jaehoyi.gdshader.model.VoidType
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsDoWhileStatement
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsForStatement
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsIfStatement
import kr.jaehoyi.gdshader.psi.GdsReturnStatement
import kr.jaehoyi.gdshader.psi.GdsSimpleStatement
import kr.jaehoyi.gdshader.psi.GdsSwitchStatement
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsWhileStatement

class GdsStatementAnnotator : Annotator {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.STATEMENT_VALIDATION)) return

        when (element) {
            is GdsSimpleStatement -> {
                checkDiscardStatement(element, holder)
                checkBreakContinueStatement(element, holder)
            }
            is GdsReturnStatement -> checkReturnStatement(element, holder)
            is GdsSwitchStatement -> checkSwitchStatement(element, holder)
            is GdsIfStatement -> checkIfStatement(element, holder)
            is GdsForStatement -> checkForStatement(element, holder)
            is GdsWhileStatement -> checkWhileStatement(element, holder)
            is GdsDoWhileStatement -> checkDoWhileStatement(element, holder)
        }
    }

    private fun checkDiscardStatement(
        element: GdsSimpleStatement,
        holder: AnnotationHolder,
    ) {
        val firstChild = element.firstChild ?: return
        if (firstChild.node.elementType != GdsTypes.CF_DISCARD) return

        val functionDecl = element.parentOfType<GdsFunctionDeclaration>() ?: return
        val context = FunctionContext.fromText(functionDecl.functionNameDecl.text)

        if (context != FunctionContext.COMMON && context != FunctionContext.FRAGMENT && context != FunctionContext.LIGHT) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "'discard' can only be used in 'fragment' or 'light' functions")
                .range(element)
                .create()
        }
    }

    private fun checkBreakContinueStatement(
        element: GdsSimpleStatement,
        holder: AnnotationHolder,
    ) {
        val firstChild = element.firstChild ?: return
        val type = firstChild.node.elementType

        if (type == GdsTypes.CF_BREAK) {
            val inLoop =
                element.parentOfType<GdsForStatement>() != null ||
                    element.parentOfType<GdsWhileStatement>() != null ||
                    element.parentOfType<GdsDoWhileStatement>() != null
            val inSwitch = element.parentOfType<GdsSwitchStatement>() != null
            if (!inLoop && !inSwitch) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "'break' is not allowed outside of a loop or 'switch' statement")
                    .range(element)
                    .create()
            }
        } else if (type == GdsTypes.CF_CONTINUE) {
            val inLoop =
                element.parentOfType<GdsForStatement>() != null ||
                    element.parentOfType<GdsWhileStatement>() != null ||
                    element.parentOfType<GdsDoWhileStatement>() != null
            if (!inLoop) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "'continue' is not allowed outside of a loop")
                    .range(element)
                    .create()
            }
        }
    }

    private fun checkReturnStatement(
        element: GdsReturnStatement,
        holder: AnnotationHolder,
    ) {
        val functionDecl = element.parentOfType<GdsFunctionDeclaration>() ?: return
        val returnType = GdsDataTypeFactory.createFromFunctionDeclaration(functionDecl)
        if (returnType is ArrayType && returnType.containerSize == null) return
        val hasExpression = element.expression != null

        if (returnType is VoidType && hasExpression) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "'void' function cannot return a value")
                .range(element)
                .create()
        } else if (returnType != null && returnType !is VoidType && !hasExpression) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Expected return with an expression of type '${returnType.name}'")
                .range(element)
                .create()
        } else if (returnType != null && returnType !is VoidType && hasExpression) {
            val exprType = GdsExpressionTypeInference.inferType(element.expression ?: return) ?: return
            if (returnType.name != exprType.name) {
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "Expected return with an expression of type '${returnType.name}'",
                    ).range(element)
                    .create()
            }
        }
    }

    private fun checkSwitchStatement(
        element: GdsSwitchStatement,
        holder: AnnotationHolder,
    ) {
        checkSwitchExpression(element, holder)
        checkEmptySwitchBody(element, holder)
    }

    private fun checkSwitchExpression(
        element: GdsSwitchStatement,
        holder: AnnotationHolder,
    ) {
        val expression = element.expression ?: return
        val type = GdsExpressionTypeInference.inferType(expression) ?: return

        if (type !is IntType && type !is UIntType) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Expected an integer or unsigned integer expression")
                .range(expression)
                .create()
        }
    }

    // Godot 4.7.2 (ShaderLanguage::_parse_block, TK_CF_SWITCH) requires the first token after '{'
    // to be 'case' or 'default'. A body with other tokens already gets a parser error, so only
    // the empty body is reported here, on the closing brace like Godot.
    private fun checkEmptySwitchBody(
        element: GdsSwitchStatement,
        holder: AnnotationHolder,
    ) {
        val switchBlock = element.switchBlock ?: return
        val closingBrace = switchBlock.node.findChildByType(GdsTypes.CURLY_BRACKET_CLOSE)?.psi ?: return
        val switchBody = switchBlock.switchBody
        if (switchBody != null) {
            if (switchBody.caseClauseList.isNotEmpty()) return
            if (PsiTreeUtil.hasErrorElements(switchBody)) return
        }

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Expected a 'case' or 'default'")
            .range(closingBrace)
            .create()
    }

    private fun checkIfStatement(
        element: GdsIfStatement,
        holder: AnnotationHolder,
    ) {
        val expression = element.expression ?: return
        val type = GdsExpressionTypeInference.inferType(expression) ?: return

        if (type !is BoolType) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Expected a boolean expression")
                .range(expression)
                .create()
        }
    }

    private fun checkForStatement(
        element: GdsForStatement,
        holder: AnnotationHolder,
    ) {
        for (expression in element.forCondition?.expressionList.orEmpty()) {
            val type = GdsExpressionTypeInference.inferType(expression) ?: continue
            if (type !is BoolType) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Expected a boolean expression")
                    .range(expression)
                    .create()
            }
        }
    }

    private fun checkWhileStatement(
        element: GdsWhileStatement,
        holder: AnnotationHolder,
    ) {
        val expression = element.expression ?: return
        val type = GdsExpressionTypeInference.inferType(expression) ?: return

        if (type !is BoolType) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Expected a boolean expression")
                .range(expression)
                .create()
        }
    }

    private fun checkDoWhileStatement(
        element: GdsDoWhileStatement,
        holder: AnnotationHolder,
    ) {
        val expression = element.expression ?: return
        val type = GdsExpressionTypeInference.inferType(expression) ?: return

        if (type !is BoolType) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Expected a boolean expression")
                .range(expression)
                .create()
        }
    }
}

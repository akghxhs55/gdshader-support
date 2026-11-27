package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.BoolType
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.UIntType
import kr.jaehoyi.gdshader.model.VoidType
import kr.jaehoyi.gdshader.psi.GdsConstantEvaluator
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsDoWhileStatement
import kr.jaehoyi.gdshader.psi.GdsExpression
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsForStatement
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsIfStatement
import kr.jaehoyi.gdshader.psi.GdsReturnStatement
import kr.jaehoyi.gdshader.psi.GdsSimpleStatement
import kr.jaehoyi.gdshader.psi.GdsSwitchStatement
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.GdsWhileStatement
import kr.jaehoyi.gdshader.psi.impl.GdsBuiltinDefine
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

class GdsStatementAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.STATEMENT_VALIDATION)) return

        when (element) {
            is GdsSimpleStatement -> checkBreakContinueStatement(element, holder)
            is GdsReturnStatement -> checkReturnStatement(element, holder)
            is GdsSwitchStatement -> checkSwitchStatement(element, holder)
            is GdsIfStatement -> checkIfStatement(element, holder)
            is GdsForStatement -> checkForStatement(element, holder)
            is GdsFunctionDeclaration -> checkMissingReturn(element, holder)
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
        val functionName = functionDecl.functionNameDecl.name
        if (Builtins.isProcessingFunction(functionName, GdsPsiImplUtil.getShaderType(element))) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Using 'return' in the '$functionName' processor function is incorrect.")
                .range(element)
                .create()
            return
        }
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

    private fun checkMissingReturn(
        element: GdsFunctionDeclaration,
        holder: AnnotationHolder,
    ) {
        val block = element.block ?: return
        val closingBrace = block.node.findChildByType(GdsTypes.CURLY_BRACKET_CLOSE)?.psi ?: return
        if (Builtins.isProcessingFunction(element.functionNameDecl.name, GdsPsiImplUtil.getShaderType(element))) return
        val returnType = GdsDataTypeFactory.createFromFunctionDeclaration(element) ?: return
        if (returnType is VoidType) return
        if (PsiTreeUtil.findChildOfType(block, GdsReturnStatement::class.java) != null) return
        if (PsiTreeUtil.hasErrorElements(block)) return

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Expected at least one 'return' statement in a non-void function.")
            .range(closingBrace)
            .create()
    }

    private fun checkSwitchStatement(
        element: GdsSwitchStatement,
        holder: AnnotationHolder,
    ) {
        checkSwitchExpression(element, holder)
        checkEmptySwitchBody(element, holder)
        checkCaseClauses(element, holder)
    }

    private fun checkCaseClauses(
        element: GdsSwitchStatement,
        holder: AnnotationHolder,
    ) {
        val clauses = element.switchBlock?.switchBody?.caseClauseList ?: return
        val expectedType =
            element.expression
                ?.let { GdsExpressionTypeInference.inferType(it) }
                ?.takeIf { it is IntType || it is UIntType }
        val labels = mutableSetOf<Long>()
        var hasDefault = false

        for (clause in clauses) {
            val keyword = clause.firstChild ?: continue
            if (keyword.node.elementType == GdsTypes.CF_DEFAULT) {
                if (hasDefault) {
                    holder
                        .newAnnotation(HighlightSeverity.ERROR, "Default case must be defined only once.")
                        .range(keyword)
                        .create()
                }
                hasDefault = true
                continue
            }

            val label = clause.expression ?: continue
            val value = checkCaseLabel(label, expectedType, holder) ?: continue
            if (!labels.add(value)) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Duplicated case label: $value.")
                    .range(label)
                    .create()
            }
        }
    }

    private fun checkCaseLabel(
        label: GdsExpression,
        expectedType: DataType?,
        holder: AnnotationHolder,
    ): Long? {
        if (PsiTreeUtil.hasErrorElements(label)) return null
        val tokens =
            PsiTreeUtil
                .collectElements(label) { it.firstChild == null && it !is PsiWhiteSpace && it !is PsiComment }
                .toList()
        val valueIndex = if (tokens.firstOrNull()?.node?.elementType == GdsTypes.OP_SUB) 1 else 0
        val valueToken = tokens.getOrNull(valueIndex) ?: return null

        if (!isValidCaseLabelToken(valueToken, expectedType)) {
            val message = if (expectedType == UIntType) "Expected an unsigned integer constant." else "Expected an integer constant."
            holder
                .newAnnotation(HighlightSeverity.ERROR, message)
                .range(label)
                .create()
            return null
        }
        tokens.getOrNull(valueIndex + 1)?.let { extra ->
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Expected a ':'.")
                .range(extra)
                .create()
            return null
        }

        return when (val value = GdsConstantEvaluator.evaluate(label)) {
            is Int -> value.toLong()
            is Long -> value
            else -> null
        }
    }

    private fun isValidCaseLabelToken(
        token: PsiElement,
        expectedType: DataType?,
    ): Boolean =
        when (token.node.elementType) {
            GdsTypes.INT_CONSTANT -> expectedType != UIntType
            GdsTypes.UINT_CONSTANT -> expectedType != IntType
            GdsTypes.IDENTIFIER -> {
                val ref = token.parent as? GdsVariableNameRef
                when (val resolved = ref?.reference?.resolve()) {
                    null -> ref != null
                    is GdsMacroElement, is GdsBuiltinDefine -> true
                    is GdsVariableNameDecl -> {
                        val type = (resolved.variableSpec as? ConstantSpec)?.type
                        (type is IntType || type is UIntType) && (expectedType == null || type == expectedType)
                    }
                    else -> false
                }
            }
            else -> false
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
}

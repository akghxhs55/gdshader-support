package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.MatrixType
import kr.jaehoyi.gdshader.model.ParameterSpec
import kr.jaehoyi.gdshader.model.UIntType
import kr.jaehoyi.gdshader.model.VectorType
import kr.jaehoyi.gdshader.psi.GdsArraySize
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsConstantEvaluator
import kr.jaehoyi.gdshader.psi.GdsConstantReducer
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsForInit
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsPostfixExpr
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement
import kr.jaehoyi.gdshader.psi.isConstant

class GdsConstantAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.EXPRESSION_VALIDATION)) return

        when (element.node?.elementType) {
            GdsTypes.FLOAT_CONSTANT -> if (DIGITS_WITH_FLOAT_SUFFIX.matches(element.text)) error(holder, element, "Invalid (float) numeric constant")
            GdsTypes.INT_CONSTANT, GdsTypes.UINT_CONSTANT -> checkHexConstant(element, holder)
            GdsTypes.STRING_CONSTANT -> checkStringEscapes(element, holder)
        }
        when (element) {
            is GdsConstantDeclarator -> checkConstantInitializer(element, holder)
            is GdsLocalVariableDeclarator -> checkForInitConstant(element, holder)
            is GdsArraySize -> checkArraySize(element, holder)
            is GdsPostfixExpr -> checkConstantIndices(element, holder)
        }
    }

    private fun checkHexConstant(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        val text = element.text.trimEnd('u', 'U')
        if (!text.startsWith("0x") && !text.startsWith("0X")) return
        if (text.length > 10) error(holder, element, "Invalid (hexadecimal) numeric constant")
    }

    private fun checkStringEscapes(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        val content = element.text.removePrefix("\"").removeSuffix("\"")
        var index = 0
        while (index < content.length) {
            if (content[index] == '\\') {
                if (content.getOrNull(index + 1) != '"' && content.getOrNull(index + 1) != '\\') {
                    error(holder, element, "Only \\\" and \\\\ escape characters supported.")
                    return
                }
                index++
            }
            index++
        }
    }

    // In a constant declaration Godot rejects every identifier that is not constant and every call
    // to a function declared in the shader. A sampler constant already fails on its type.
    private fun checkConstantInitializer(
        element: GdsConstantDeclarator,
        holder: AnnotationHolder,
    ) {
        val initializer = element.initializer ?: return
        if (GdsDataTypeFactory.createFromConstantDeclaration(element)?.isOpaque == true) return
        firstNonConstant(initializer)?.let { error(holder, it, "Expected constant expression.") }
    }

    private fun checkForInitConstant(
        element: GdsLocalVariableDeclarator,
        holder: AnnotationHolder,
    ) {
        val forInit = element.parent?.parent as? GdsForInit ?: return
        if (!forInit.isConstant) return
        val initializer = element.initializer
        if (initializer == null) {
            error(holder, element, "Expected initialization of constant.")
            return
        }
        if (GdsDataTypeFactory.createFromForInit(element)?.isOpaque == true) return
        firstNonConstant(initializer)?.let { error(holder, it, "Expected constant expression.") }
    }

    private fun checkArraySize(
        element: GdsArraySize,
        holder: AnnotationHolder,
    ) {
        val expression = element.expression ?: return
        val invalid =
            firstNonConstant(expression) != null ||
                when (val value = GdsConstantEvaluator.evaluate(expression)) {
                    null -> false
                    is Int -> value <= 0
                    is Long -> value <= 0 || value > Int.MAX_VALUE
                    else -> true
                }
        if (invalid) error(holder, expression, "Expected a positive integer constant.")
    }

    private fun firstNonConstant(root: PsiElement): PsiElement? {
        for (ref in PsiTreeUtil.findChildrenOfAnyType(root, false, GdsVariableNameRef::class.java, GdsFunctionNameRef::class.java)) {
            val resolved = (ref as? GdsVariableNameRef)?.reference?.resolve() ?: (ref as? GdsFunctionNameRef)?.reference?.resolve()
            val constant =
                when (resolved) {
                    null, is GdsMacroElement -> true
                    is GdsFunctionNameDecl -> false
                    is GdsVariableNameDecl ->
                        when (val spec = resolved.variableSpec) {
                            is ConstantSpec -> true
                            is ParameterSpec -> !spec.isMutable
                            null -> true
                            else -> false
                        }
                    is GdsLightVariable -> resolved.isTypeAmbiguous || !resolved.isAssignable
                    else -> true
                }
            if (!constant) return ref
        }
        return null
    }

    private fun checkConstantIndices(
        element: GdsPostfixExpr,
        holder: AnnotationHolder,
    ) {
        for (index in element.expressionList) {
            val constant = GdsConstantReducer.reduce(index) as? GdsConstantReducer.Constant ?: continue
            if (constant.type !is IntType && constant.type !is UIntType) continue
            val value = (constant.values.singleOrNull() as? Number)?.toLong() ?: continue
            val size =
                when (val type = GdsExpressionTypeInference.inferTypeBefore(element, index)) {
                    is ArrayType -> type.containerSize
                    is VectorType -> type.containerSize
                    is MatrixType -> type.containerSize
                    else -> null
                } ?: continue
            if (value < 0 || value >= size) error(holder, index, "Index [$value] out of range [0..${size - 1}].")
        }
    }

    private fun error(
        holder: AnnotationHolder,
        element: PsiElement,
        message: String,
    ) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private companion object {
        val DIGITS_WITH_FLOAT_SUFFIX = Regex("""[0-9]+[fF]""")
    }
}

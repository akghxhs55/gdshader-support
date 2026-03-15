package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.UIntType
import kr.jaehoyi.gdshader.model.VoidType
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsReturnStatement
import kr.jaehoyi.gdshader.psi.GdsSimpleStatement
import kr.jaehoyi.gdshader.psi.GdsSwitchStatement
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsStatementAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdsSimpleStatement -> checkDiscard(element, holder)
            is GdsReturnStatement -> checkReturn(element, holder)
            is GdsSwitchStatement -> checkSwitch(element, holder)
        }
    }

    private fun checkDiscard(element: GdsSimpleStatement, holder: AnnotationHolder) {
        val firstChild = element.firstChild ?: return
        if (firstChild.node.elementType != GdsTypes.CF_DISCARD) return

        val functionDecl = element.parentOfType<GdsFunctionDeclaration>() ?: return
        val context = FunctionContext.fromText(functionDecl.functionNameDecl.text)

        if (context != FunctionContext.COMMON && context != FunctionContext.FRAGMENT && context != FunctionContext.LIGHT) {
            holder.newAnnotation(HighlightSeverity.ERROR, "'discard' can only be used in 'fragment' or 'light' functions")
                .range(element)
                .create()
        }
    }

    private fun checkReturn(element: GdsReturnStatement, holder: AnnotationHolder) {
        val functionDecl = element.parentOfType<GdsFunctionDeclaration>() ?: return
        val returnType = GdsDataTypeFactory.createFromFunctionDeclaration(functionDecl)
        val hasExpression = element.expression != null

        if (returnType is VoidType && hasExpression) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Void function must not return a value")
                .range(element)
                .create()
        } else if (returnType != null && returnType !is VoidType && !hasExpression) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Expected return value of type '${returnType.name}'")
                .range(element)
                .create()
        }
    }

    private fun checkSwitch(element: GdsSwitchStatement, holder: AnnotationHolder) {
        val expression = element.expression ?: return
        val type = GdsExpressionTypeInference.inferType(expression) ?: return
        
        if (type !is IntType && type !is UIntType) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Expected an integer or unsigned integer expression")
                .range(expression)
                .create()
        }
    }
}

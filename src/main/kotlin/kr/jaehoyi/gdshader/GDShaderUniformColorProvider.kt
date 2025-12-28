package kr.jaehoyi.gdshader

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParserFacade
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.psi.GDShaderElementFactory
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameDecl
import kr.jaehoyi.gdshader.util.GDShaderColorUtil.convertColorToVecString
import kr.jaehoyi.gdshader.util.GDShaderColorUtil.extractColorFromText
import java.awt.Color

class GDShaderUniformColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        if (element.node.elementType != GDShaderTypes.IDENTIFIER) {
            return null
        }

        val variableNameDecl = element.parent as? GDShaderVariableNameDecl ?: return null

        val uniformDeclaration = variableNameDecl.parent as? GDShaderUniformDeclaration ?: return null

        val hints = uniformDeclaration.hintSection?.hintList?.hintList ?: return null
        
        if (hints.none { it.text == "source_color" }) {
            return null
        }
        
        val typeText = uniformDeclaration.type?.text ?: return null
        
        if (typeText != DataType.VEC3.text && typeText != DataType.VEC4.text) {
            return null
        }
        
        return extractUniformColor(uniformDeclaration)
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val project = element.project
        val uniformDeclaration = element.parent?.parent as? GDShaderUniformDeclaration ?: return

        val typeText = uniformDeclaration.type?.text ?: return
        val isVec4 = typeText == DataType.VEC4.text
        
        val oldExpression = uniformDeclaration.expression

        WriteCommandAction.runWriteCommandAction(project) {
            val newText = convertColorToVecString(color, isVec4)

            if (oldExpression != null) {
                val newExpression = GDShaderElementFactory.createExpression(project, newText) ?: return@runWriteCommandAction
                oldExpression.replace(newExpression)
            }
            else {
                val dummyText = "uniform vec4 dummy = $newText;"
                val dummyDecl = GDShaderElementFactory.createUniformDeclaration(project, dummyText) ?: return@runWriteCommandAction

                val eqTokenFromDummy = dummyDecl.node.findChildByType(GDShaderTypes.OP_ASSIGN)?.psi
                val expressionFromDummy = dummyDecl.expression

                val semicolon = uniformDeclaration.node.findChildByType(GDShaderTypes.SEMICOLON)?.psi ?: return@runWriteCommandAction

                if (eqTokenFromDummy != null && expressionFromDummy != null) {
                    val parserFacade = PsiParserFacade.getInstance(project)
                    val space = parserFacade.createWhiteSpaceFromText(" ")

                    uniformDeclaration.addBefore(space, semicolon)
                    uniformDeclaration.addBefore(eqTokenFromDummy, semicolon)
                    uniformDeclaration.addBefore(space, semicolon)
                    uniformDeclaration.addBefore(expressionFromDummy, semicolon)
                }
            }
        }
    }

    private fun extractUniformColor(uniformDeclaration: GDShaderUniformDeclaration): Color {
        val initializer = uniformDeclaration.expression ?: return Color.BLACK
        return extractColorFromText(initializer.text) ?: Color.BLACK
    }
    
}
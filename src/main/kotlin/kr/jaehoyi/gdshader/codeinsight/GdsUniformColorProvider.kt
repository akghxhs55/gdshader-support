package kr.jaehoyi.gdshader.codeinsight

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParserFacade
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.convertColorToVecString
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.extractColorFromText
import kr.jaehoyi.gdshader.model.Builtins
import java.awt.Color

class GdsUniformColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        if (element.node.elementType != GdsTypes.IDENTIFIER) {
            return null
        }

        val variableNameDecl = element.parent as? GdsVariableNameDecl ?: return null

        val uniformDeclaration = variableNameDecl.parent as? GdsUniformDeclaration ?: return null

        val hints = uniformDeclaration.hintSection?.hintList?.hintList ?: return null
        
        if (hints.none { it.text == "source_color" }) {
            return null
        }
        
        val typeText = uniformDeclaration.type?.text ?: return null
        
        if (typeText != Builtins.VEC3.name && typeText != Builtins.VEC4.name) {
            return null
        }
        
        return extractUniformColor(uniformDeclaration)
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val project = element.project
        val uniformDeclaration = element.parent?.parent as? GdsUniformDeclaration ?: return

        val typeText = uniformDeclaration.type?.text ?: return
        val isVec4 = typeText == Builtins.VEC4.name
        
        val oldExpression = uniformDeclaration.expression

        WriteCommandAction.runWriteCommandAction(project) {
            val newText = convertColorToVecString(color, isVec4)

            if (oldExpression != null) {
                val newExpression = GdsElementFactory.createExpression(project, newText) ?: return@runWriteCommandAction
                oldExpression.replace(newExpression)
            }
            else {
                val dummyText = "uniform vec4 dummy = $newText;"
                val dummyDecl = GdsElementFactory.createUniformDeclaration(project, dummyText) ?: return@runWriteCommandAction

                val eqTokenFromDummy = dummyDecl.node.findChildByType(GdsTypes.OP_ASSIGN)?.psi
                val expressionFromDummy = dummyDecl.expression

                val semicolon = uniformDeclaration.node.findChildByType(GdsTypes.SEMICOLON)?.psi ?: return@runWriteCommandAction

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

    private fun extractUniformColor(uniformDeclaration: GdsUniformDeclaration): Color {
        val initializer = uniformDeclaration.expression ?: return Color.BLACK
        return extractColorFromText(initializer.text) ?: Color.BLACK
    }
    
}

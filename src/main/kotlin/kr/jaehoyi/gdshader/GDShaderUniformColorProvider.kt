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
import java.awt.Color
import java.util.Locale

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

    private fun extractUniformColor(uniformDeclaration: GDShaderUniformDeclaration): Color? {
        val initializer = uniformDeclaration.expression ?: return Color.BLACK
        val text = initializer.text

        val regex = Regex("""vec([34])\s*\(\s*([0-9.,\s]+)\s*\)""")
        val match = regex.find(text) ?: return Color.BLACK
        
        val type = match.groupValues[1]
        val content = match.groupValues[2]
        
        val args = content.split(",").mapNotNull { it.trim().toFloatOrNull() }

        when (args.size) {
            1 -> {
                val s = (args[0] * 255).toInt().coerceIn(0, 255)
                return Color(s, s, s, 255)
            }
            3 -> {
                val r = (args[0] * 255).toInt().coerceIn(0, 255)
                val g = (args[1] * 255).toInt().coerceIn(0, 255)
                val b = (args[2] * 255).toInt().coerceIn(0, 255)
                return Color(r, g, b, 255)
            }
            4 -> if (type == "4") {
                val r = (args[0] * 255).toInt().coerceIn(0, 255)
                val g = (args[1] * 255).toInt().coerceIn(0, 255)
                val b = (args[2] * 255).toInt().coerceIn(0, 255)
                val a = (args[3] * 255).toInt().coerceIn(0, 255)
                return Color(r, g, b, a)
            }
        }

        return Color.BLACK
    }
    
    private fun convertColorToVecString(color: Color, isVec4: Boolean): String {
        val r = formatFloat(color.red / 255.0f)
        val g = formatFloat(color.green / 255.0f)
        val b = formatFloat(color.blue / 255.0f)
        
        return if (isVec4) {
            val a = formatFloat(color.alpha / 255.0f)
            "vec4($r, $g, $b, $a)"
        } else {
            "vec3($r, $g, $b)"
        }
    }
    
    private fun formatFloat(value: Float): String {
        val formatted = String.format(Locale.US, "%.3f", value)
        val trimmed = formatted.trimEnd('0')
        
        return if (trimmed.endsWith(".")) "${trimmed}0" else trimmed
    }
    
}
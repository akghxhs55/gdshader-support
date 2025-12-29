package kr.jaehoyi.gdshader.codeinsight

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclarator
import kr.jaehoyi.gdshader.psi.GDShaderElementFactory
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameDecl
import kr.jaehoyi.gdshader.codeinsight.GDShaderColorUtil.convertColorToVecString
import kr.jaehoyi.gdshader.codeinsight.GDShaderColorUtil.extractColorFromText
import kr.jaehoyi.gdshader.codeinsight.GDShaderColorUtil.isColorVariableName
import java.awt.Color

class GDShaderConstantColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        if (element.node.elementType != GDShaderTypes.IDENTIFIER) {
            return null
        }
        if (!isColorVariableName(element.text)) {
            return null
        }

        val variableNameDecl = element.parent as? GDShaderVariableNameDecl ?: return null

        val constantDeclarator = variableNameDecl.parent as? GDShaderConstantDeclarator ?: return null
        if (constantDeclarator.arraySize != null) {
            return null
        }
        
        val constantDeclaration = constantDeclarator.parent.parent as? GDShaderConstantDeclaration ?: return null
        if (constantDeclaration.arraySize != null) {
            return null
        }
        
        val typeText = constantDeclaration.type?.text ?: return null

        if (typeText != DataType.VEC3.text && typeText != DataType.VEC4.text) {
            return null
        }
        

        return extractConstantColor(constantDeclarator)
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val project = element.project
        val constantDeclarator = element.parent?.parent as? GDShaderConstantDeclarator ?: return
        val constantDeclaration = constantDeclarator.parent.parent as? GDShaderConstantDeclaration ?: return
        val typeText = constantDeclaration.type?.text ?: return
        val isVec4 = typeText == DataType.VEC4.text

        val oldInitializer = constantDeclarator.initializer ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            val newText = convertColorToVecString(color, isVec4)

            val newInitializer = GDShaderElementFactory.createInitializer(project, newText) ?: return@runWriteCommandAction
            oldInitializer.replace(newInitializer)
        }
    }

    private fun extractConstantColor(constantDeclarator: GDShaderConstantDeclarator): Color {
        val initializer = constantDeclarator.initializer ?: return Color.BLACK
        return extractColorFromText(initializer.text) ?: Color.BLACK
    }

}
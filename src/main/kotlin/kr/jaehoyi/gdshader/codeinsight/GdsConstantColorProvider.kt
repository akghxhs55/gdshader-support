package kr.jaehoyi.gdshader.codeinsight

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.convertColorToVecString
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.extractColorFromText
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.isColorVariableName
import kr.jaehoyi.gdshader.model.Builtins
import java.awt.Color

class GdsConstantColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        if (element.node.elementType != GdsTypes.IDENTIFIER) {
            return null
        }
        if (!isColorVariableName(element.text)) {
            return null
        }

        val variableNameDecl = element.parent as? GdsVariableNameDecl ?: return null

        val constantDeclarator = variableNameDecl.parent as? GdsConstantDeclarator ?: return null
        if (constantDeclarator.arraySize != null) {
            return null
        }
        
        val constantDeclaration = constantDeclarator.parent.parent as? GdsConstantDeclaration ?: return null
        if (constantDeclaration.arraySize != null) {
            return null
        }
        
        val typeText = constantDeclaration.type?.text ?: return null

        if (typeText != Builtins.VEC3.name && typeText != Builtins.VEC4.name) {
            return null
        }
        

        return extractConstantColor(constantDeclarator)
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val project = element.project
        val constantDeclarator = element.parent?.parent as? GdsConstantDeclarator ?: return
        val constantDeclaration = constantDeclarator.parent.parent as? GdsConstantDeclaration ?: return
        val typeText = constantDeclaration.type?.text ?: return
        val isVec4 = typeText == Builtins.VEC4.name

        val oldInitializer = constantDeclarator.initializer ?: return

        WriteCommandAction.runWriteCommandAction(project) {
            val newText = convertColorToVecString(color, isVec4)

            val newInitializer = GdsElementFactory.createInitializer(project, newText) ?: return@runWriteCommandAction
            oldInitializer.replace(newInitializer)
        }
    }

    private fun extractConstantColor(constantDeclarator: GdsConstantDeclarator): Color {
        val initializer = constantDeclarator.initializer ?: return Color.BLACK
        return extractColorFromText(initializer.text) ?: Color.BLACK
    }

}

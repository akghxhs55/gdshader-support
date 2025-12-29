package kr.jaehoyi.gdshader.codeinsight

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParserFacade
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.psi.GDShaderElementFactory
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameDecl
import kr.jaehoyi.gdshader.codeinsight.GDShaderColorUtil.convertColorToVecString
import kr.jaehoyi.gdshader.codeinsight.GDShaderColorUtil.extractColorFromText
import kr.jaehoyi.gdshader.codeinsight.GDShaderColorUtil.isColorVariableName
import kr.jaehoyi.gdshader.psi.GDShaderLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderLocalVariableDeclarator
import java.awt.Color

class GDShaderLocalVariableColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        if (element.node.elementType != GDShaderTypes.IDENTIFIER) {
            return null
        }
        if (!isColorVariableName(element.text)) {
            return null
        }

        val variableNameDecl = element.parent as? GDShaderVariableNameDecl ?: return null

        val localVariableDeclarator = variableNameDecl.parent as? GDShaderLocalVariableDeclarator ?: return null
        if (localVariableDeclarator.arraySize != null) {
            return null
        }
        
        val localVariableDeclaration = localVariableDeclarator.parent.parent as? GDShaderLocalVariableDeclaration ?: return null
        if (localVariableDeclaration.arraySize != null) {
            return null
        }
        
        val typeText = localVariableDeclaration.type.text ?: return null

        if (typeText != DataType.VEC3.text && typeText != DataType.VEC4.text) {
            return null
        }
        

        return extractLocalVariableColor(localVariableDeclarator)
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val project = element.project
        val localVariableDeclarator = element.parent?.parent as? GDShaderLocalVariableDeclarator ?: return
        val localVariableDeclaration = localVariableDeclarator.parent.parent as? GDShaderLocalVariableDeclaration ?: return
        val typeText = localVariableDeclaration.type.text ?: return
        val isVec4 = typeText == DataType.VEC4.text

        val oldInitializer = localVariableDeclarator.initializer

        WriteCommandAction.runWriteCommandAction(project) {
            val newText = convertColorToVecString(color, isVec4)

            if (oldInitializer != null) {
                val newInitializer = GDShaderElementFactory.createInitializer(project, newText) ?: return@runWriteCommandAction
                oldInitializer.replace(newInitializer)
            }
            else {
                val dummyText = "vec4 dummy = $newText;"
                val dummyDeclarator = GDShaderElementFactory.createLocalVariableDeclarator(project, dummyText)
                    ?: return@runWriteCommandAction

                val eqTokenFromDummy = dummyDeclarator.node.findChildByType(GDShaderTypes.OP_ASSIGN)?.psi
                val initializerFromDummy = dummyDeclarator.initializer

                val variableNameDecl = localVariableDeclarator.variableNameDecl

                if (eqTokenFromDummy != null && initializerFromDummy != null) {
                    val addedEq = localVariableDeclarator.addAfter(eqTokenFromDummy, variableNameDecl)
                    val addedInitializer = localVariableDeclarator.addAfter(initializerFromDummy, addedEq)

                    val parserFacade = PsiParserFacade.getInstance(project)

                    localVariableDeclarator.addBefore(parserFacade.createWhiteSpaceFromText(" "), addedEq)

                    localVariableDeclarator.addBefore(parserFacade.createWhiteSpaceFromText(" "), addedInitializer)
                }
            }
        }
    }

    private fun extractLocalVariableColor(localVariableDeclarator: GDShaderLocalVariableDeclarator): Color {
        val initializer = localVariableDeclarator.initializer ?: return Color.BLACK
        return extractColorFromText(initializer.text) ?: Color.BLACK
    }

}
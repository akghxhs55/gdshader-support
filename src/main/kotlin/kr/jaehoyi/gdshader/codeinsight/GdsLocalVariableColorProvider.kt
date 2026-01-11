package kr.jaehoyi.gdshader.codeinsight

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParserFacade
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.convertColorToVecString
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.extractColorFromText
import kr.jaehoyi.gdshader.codeinsight.GdsColorUtil.isColorVariableName
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import java.awt.Color

class GdsLocalVariableColorProvider : ElementColorProvider {

    override fun getColorFrom(element: PsiElement): Color? {
        if (element.node.elementType != GdsTypes.IDENTIFIER) {
            return null
        }
        if (!isColorVariableName(element.text)) {
            return null
        }

        val variableNameDecl = element.parent as? GdsVariableNameDecl ?: return null

        val localVariableDeclarator = variableNameDecl.parent as? GdsLocalVariableDeclarator ?: return null
        if (localVariableDeclarator.arraySize != null) {
            return null
        }
        
        val localVariableDeclaration = localVariableDeclarator.parent.parent as? GdsLocalVariableDeclaration ?: return null
        if (localVariableDeclaration.arraySize != null) {
            return null
        }
        
        val typeText = localVariableDeclaration.type.text ?: return null

        if (typeText != Builtins.VEC3.name && typeText != Builtins.VEC4.name) {
            return null
        }
        

        return extractLocalVariableColor(localVariableDeclarator)
    }

    override fun setColorTo(element: PsiElement, color: Color) {
        val project = element.project
        val localVariableDeclarator = element.parent?.parent as? GdsLocalVariableDeclarator ?: return
        val localVariableDeclaration = localVariableDeclarator.parent.parent as? GdsLocalVariableDeclaration ?: return
        val typeText = localVariableDeclaration.type.text ?: return
        val isVec4 = typeText == Builtins.VEC4.name

        val oldInitializer = localVariableDeclarator.initializer

        WriteCommandAction.runWriteCommandAction(project) {
            val newText = convertColorToVecString(color, isVec4)

            if (oldInitializer != null) {
                val newInitializer = GdsElementFactory.createInitializer(project, newText) ?: return@runWriteCommandAction
                oldInitializer.replace(newInitializer)
            }
            else {
                val dummyText = "vec4 dummy = $newText;"
                val dummyDeclarator = GdsElementFactory.createLocalVariableDeclarator(project, dummyText)
                    ?: return@runWriteCommandAction

                val eqTokenFromDummy = dummyDeclarator.node.findChildByType(GdsTypes.OP_ASSIGN)?.psi
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

    private fun extractLocalVariableColor(localVariableDeclarator: GdsLocalVariableDeclarator): Color {
        val initializer = localVariableDeclarator.initializer ?: return Color.BLACK
        return extractColorFromText(initializer.text) ?: Color.BLACK
    }

}

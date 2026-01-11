package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.psi.GdsForInit
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsStatementBody
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

object GdsResolver {

    fun processDeclarations(startElement: PsiElement, processor: (element: PsiNamedElement) -> Boolean) {
        val start = if (startElement.node.elementType == GdsTypes.IDENTIFIER) startElement else startElement.context
        for (currentScope in generateSequence(start) { it.context }) {
            val params = (currentScope as? GdsFunctionDeclaration)?.parameterList?.parameterList
            if (params != null) {
                for (param in params) {
                    val nameDecl = param.variableNameDecl
                    if (!processor(nameDecl)) return
                }
            }
            
            for (sibling in generateSequence(currentScope.prevSibling) { it.prevSibling }) {
                when (sibling) {
                    is GdsStatementBody -> {
                        val statement = sibling.statement ?: continue

                        val localVariableDeclarators = statement.localVariableDeclaration
                            ?.localVariableDeclaratorList
                            ?.localVariableDeclaratorList
                            .orEmpty()
                        for (declarator in localVariableDeclarators) {
                            if (!processor(declarator.variableNameDecl)) return
                        }
                        
                        val constantDeclarators = statement.constantDeclaration
                            ?.constantDeclaratorList
                            ?.constantDeclaratorList
                            .orEmpty()
                        for (declarator in constantDeclarators) {
                            if (!processor(declarator.variableNameDecl)) return
                        }
                    }
                    
                    is GdsForInit -> {
                        val declarators = sibling.localVariableDeclaratorList
                            .localVariableDeclaratorList
                        for (declarator in declarators) {
                            if (!processor(declarator.variableNameDecl)) return
                        }
                    }
                    
                    is GdsItem -> {
                        val uniformDeclaration = sibling.topLevelDeclaration.uniformDeclaration
                        if (uniformDeclaration != null) {
                            val nameDecl = uniformDeclaration.variableNameDecl
                            if (nameDecl != null && !processor(nameDecl)) return
                        }
                        
                        val constantDeclarators = sibling.topLevelDeclaration.constantDeclaration
                            ?.constantDeclaratorList
                            ?.constantDeclaratorList
                            .orEmpty()
                        for (declarator in constantDeclarators) {
                            if (!processor(declarator.variableNameDecl)) return
                        }
                        
                        val varyingDeclaration = sibling.topLevelDeclaration.varyingDeclaration
                        if (varyingDeclaration != null) {
                            val nameDecl = varyingDeclaration.variableNameDecl
                            if (nameDecl != null && !processor(nameDecl)) return
                        }
                        
                        val functionDeclaration = sibling.topLevelDeclaration.functionDeclaration
                        if (functionDeclaration != null) {
                            val nameDecl = functionDeclaration.functionNameDecl
                            if (!processor(nameDecl)) return
                        }

//                        val structDeclaration = sibling.topLevelDeclaration.structDeclaration
//                        if (structDeclaration != null) {
//                            val nameDecl = structDeclaration.structNameDecl
//                            if (!processor(nameDecl)) return
//                        }
                    }
                }
            }
        }
        
        val shaderType = GdsPsiImplUtil.getShaderType(startElement) ?: return
        val functionContext = GdsPsiImplUtil.getFunctionContext(startElement) ?: return
        val variableSpec = Builtins.getVariable(shaderType, functionContext, startElement.text) ?: return
        val lightVariable = GdsLightVariable(PsiManager.getInstance(startElement.project), variableSpec)
        if (!processor(lightVariable)) return
    }
    
}
package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import kr.jaehoyi.gdshader.model.GDShaderBuiltins
import kr.jaehoyi.gdshader.psi.GDShaderForInit
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderItem
import kr.jaehoyi.gdshader.psi.GDShaderStatementBody
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.impl.GDShaderLightVariable
import kr.jaehoyi.gdshader.psi.impl.GDShaderPsiImplUtil

object GDShaderResolver {

    fun processDeclarations(startElement: PsiElement, processor: (element: PsiNamedElement) -> Boolean) {
        val start = if (startElement.node.elementType == GDShaderTypes.IDENTIFIER) startElement else startElement.context
        for (currentScope in generateSequence(start) { it.context }) {
            val params = (currentScope as? GDShaderFunctionDeclaration)?.parameterList?.parameterList
            if (params != null) {
                for (param in params) {
                    val nameDecl = param.variableNameDecl
                    if (!processor(nameDecl)) return
                }
            }
            
            for (sibling in generateSequence(currentScope.prevSibling) { it.prevSibling }) {
                when (sibling) {
                    is GDShaderStatementBody -> {
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
                    
                    is GDShaderForInit -> {
                        val declarators = sibling.localVariableDeclaratorList
                            .localVariableDeclaratorList
                        for (declarator in declarators) {
                            if (!processor(declarator.variableNameDecl)) return
                        }
                    }
                    
                    is GDShaderItem -> {
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
                        
//                        val functionDeclaration = sibling.topLevelDeclaration.functionDeclaration
//                        if (functionDeclaration != null) {
//                            val nameDecl = functionDeclaration.functionNameDecl
//                            if (!processor(nameDecl)) return
//                        }
//                        
//                        val structDeclaration = sibling.topLevelDeclaration.structDeclaration
//                        if (structDeclaration != null) {
//                            val nameDecl = structDeclaration.structNameDecl
//                            if (!processor(nameDecl)) return
//                        }
                    }
                }
            }
        }
        
        val shaderType = GDShaderPsiImplUtil.getShaderType(startElement) ?: return
        val functionContext = GDShaderPsiImplUtil.getFunctionContext(startElement) ?: return
        val variableSpec = GDShaderBuiltins.getVariable(shaderType, functionContext, startElement.text) ?: return
        val lightVariable = GDShaderLightVariable(PsiManager.getInstance(startElement.project), variableSpec)
        if (!processor(lightVariable)) return
    }
    
}
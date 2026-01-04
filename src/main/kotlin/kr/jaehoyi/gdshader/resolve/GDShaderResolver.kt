package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderItem
import kr.jaehoyi.gdshader.psi.GDShaderStatementBody

object GDShaderResolver {

    fun processDeclarations(startElement: PsiElement, processor: (nameDecl: PsiNamedElement) -> Boolean) {
        for (currentScope in generateSequence(startElement.context) { it.context }) {
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
    }
    
}
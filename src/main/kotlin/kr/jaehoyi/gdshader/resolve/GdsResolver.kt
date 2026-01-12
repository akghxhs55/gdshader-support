package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsForInit
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsStatementBody
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

object GdsResolver {

    fun processVariableDeclaration(startElement: PsiElement, processor: (element: GdsVariable) -> Boolean): Boolean {
        return walkUpScope(startElement) { scope ->
            when (scope) {
                is GdsStatementBody -> {
                    scope.statement?.localVariableDeclaration?.localVariableDeclaratorList?.localVariableDeclaratorList?.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpScope false
                    }
                    scope.statement?.constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpScope false
                    }
                }

                is GdsForInit -> {
                    scope.localVariableDeclaratorList.localVariableDeclaratorList.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpScope false
                    }
                }

                is GdsFunctionDeclaration -> {
                    scope.parameterList?.parameterList?.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpScope false
                    }
                }

                is GdsItem -> {
                    scope.topLevelDeclaration.uniformDeclaration?.variableNameDecl?.let {
                        if (!processor(it)) return@walkUpScope false
                    }
                    scope.topLevelDeclaration.constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpScope false
                    }
                    scope.topLevelDeclaration.varyingDeclaration?.variableNameDecl?.let {
                        if (!processor(it)) return@walkUpScope false
                    }
                }
            }
            true
        } && processBuiltinVariables(startElement, processor)
    }
    
    fun processFunctionDeclaration(startElement: PsiElement, processor: (element: GdsFunction) -> Boolean): Boolean {
        return walkUpScope(startElement) { scope ->
            when (scope) {
                is GdsItem -> {
                    scope.topLevelDeclaration.functionDeclaration?.functionNameDecl?.let {
                        if (!processor(it)) return@walkUpScope false
                    }
                }
            }
            true
        } && processBuiltinFunctions(startElement, processor)
    }
    
    fun processStructDeclaration(startElement: PsiElement, processor: (element: GdsStructNameDecl) -> Boolean): Boolean {
        val file = startElement.containingFile as? GdsFile ?: return true
        
        for (item in file.children) {
            if (item !is GdsItem) continue
            val structDeclaration = item.topLevelDeclaration.structDeclaration ?: continue
            val nameDecl = structDeclaration.structNameDecl ?: continue
            if (!processor(nameDecl)) return false
        }
        
        return true
    }

    private inline fun walkUpScope(startElement: PsiElement, action: (PsiElement) -> Boolean): Boolean {
        var current = startElement.context
        while (current != null) {
            if (!action(current)) return false

            var sibling = current.prevSibling
            while (sibling != null) {
                if (!action(sibling)) return false
                sibling = sibling.prevSibling
            }

            current = current.context
        }
        return true
    }
    
    private fun processBuiltinVariables(element: PsiElement, processor: (element: GdsVariable) -> Boolean): Boolean {
        val shaderType = GdsPsiImplUtil.getShaderType(element) ?: return true
        val functionContext = GdsPsiImplUtil.getFunctionContext(element) ?: return true
        
        val project = element.project
        val manager = PsiManager.getInstance(project)
        
        val spec = Builtins.getVariable(shaderType, functionContext, element.text)
        if (spec != null) {
            return !processor(GdsLightVariable(manager, spec))
        }
        return true
    }
    
    private fun processBuiltinFunctions(element: PsiElement, processor: (element: GdsFunction) -> Boolean): Boolean {
        val shaderType = GdsPsiImplUtil.getShaderType(element) ?: return true
        val functionContext = GdsPsiImplUtil.getFunctionContext(element) ?: return true
        
        val project = element.project
        val manager = PsiManager.getInstance(project)
        
        val spec = Builtins.getFunctions(shaderType, functionContext, element.text) ?: return true
        spec.forEach {
            if (!processor(GdsLightFunction(manager, it))) return false
        }
        return true
    }
    
}
package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveState
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.psi.GdsForInit
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsStatementBody
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

object GdsResolver {

    fun processVariableDeclaration(startElement: PsiElement, processor: (element: GdsVariable) -> Boolean): Boolean {
        val keepGoing = walkUpLocalScope(startElement) { scope ->
            when (scope) {
                is GdsStatementBody -> {
                    scope.statement?.localVariableDeclaration?.localVariableDeclaratorList?.localVariableDeclaratorList?.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                    }
                    scope.statement?.constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                    }
                }

                is GdsForInit -> {
                    scope.localVariableDeclaratorList.localVariableDeclaratorList.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                    }
                }

                is GdsFunctionDeclaration -> {
                    scope.parameterList?.parameterList?.forEach {
                        if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                    }
                }
            }
            true
        }
        
        if (!keepGoing) return false
        
        val file = startElement.containingFile
        if (!file.processDeclarations(
                GdsScopeProcessor(GdsVariable::class.java, processor),
                ResolveState.initial(),
                null,
                startElement
            )
        ) {
            return false
        }
        
        return processBuiltinVariables(startElement, processor)
    }
    
    fun processFunctionDeclaration(startElement: PsiElement, processor: (element: GdsFunction) -> Boolean): Boolean {
        val file = startElement.containingFile
        if (!file.processDeclarations(
                GdsScopeProcessor(GdsFunction::class.java, processor),
                ResolveState.initial(),
                null,
                startElement
            )) {
            return false
        }

        return processBuiltinFunctions(startElement, processor)
    }
    
    fun processStructDeclaration(startElement: PsiElement, processor: (element: GdsStructNameDecl) -> Boolean): Boolean {
        val file = startElement.containingFile
        return file.processDeclarations(
            GdsScopeProcessor(GdsStructNameDecl::class.java, processor),
            ResolveState.initial(),
            null,
            startElement
        )
    }

    private inline fun walkUpLocalScope(startElement: PsiElement, action: (PsiElement) -> Boolean): Boolean {
        var current: PsiElement? = startElement
        while (current != null) {
            var sibling = current.prevSibling
            while (sibling != null) {
                if (!action(sibling)) return false
                sibling = sibling.prevSibling
            }
            
            val parent = current.context

            if (parent is GdsFunctionDeclaration) {
                if (!action(parent)) return false
            }
            
            current = parent
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
package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.ResolveState
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.isWritableBuiltin
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsForInit
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsStatementBody
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

object GdsResolver {
    fun processVariableDeclaration(
        startElement: PsiElement,
        processor: (element: GdsVariable) -> Boolean,
    ): Boolean {
        val startOffset = startElement.textOffset
        val keepGoing =
            walkUpLocalScope(startElement) { scope ->
                when (scope) {
                    is GdsLocalVariableDeclarator -> {
                        if (!processor(scope.variableNameDecl)) return@walkUpLocalScope false
                    }

                    is GdsConstantDeclarator -> {
                        if (!processor(scope.variableNameDecl)) return@walkUpLocalScope false
                    }

                    is GdsForInit -> {
                        scope.localVariableDeclaratorList.localVariableDeclaratorList.forEach {
                            if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                        }
                    }

                    is GdsStatementBody -> {
                        scope.statement?.localVariableDeclaration?.localVariableDeclaratorList?.localVariableDeclaratorList?.forEach {
                            if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                        }
                        scope.statement?.constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.forEach {
                            if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                        }
                    }

                    is GdsFunctionDeclaration -> {
                        scope.parameterList?.parameterList?.forEach {
                            val decl = it.variableNameDecl
                            if (decl.textOffset >= startOffset) return@forEach
                            if (!processor(it.variableNameDecl)) return@walkUpLocalScope false
                        }
                    }
                }
                true
            }

        if (!keepGoing) return false

        val site = GdsMacroExpansion.expansionSite(startElement)
        val file = site.containingFile
        val originFile = file.originalFile
        if (!file.processDeclarations(
                GdsScopeProcessor(GdsVariable::class.java, site.textOffset, originFile, processor),
                ResolveState.initial(),
                null,
                site,
            )
        ) {
            return false
        }

        return processBuiltinVariables(startElement, processor)
    }

    fun processFunctionDeclaration(
        startElement: PsiElement,
        processor: (element: GdsFunction) -> Boolean,
    ): Boolean {
        val site = GdsMacroExpansion.expansionSite(startElement)
        val file = site.containingFile
        val originFile = file.originalFile
        if (!file.processDeclarations(
                GdsScopeProcessor(GdsFunction::class.java, site.textOffset, originFile, processor),
                ResolveState.initial(),
                null,
                site,
            )
        ) {
            return false
        }

        return processBuiltinFunctions(startElement, processor)
    }

    fun findStructDeclaration(
        startElement: PsiElement,
        name: String,
    ): GdsStructNameDecl? {
        var result: GdsStructNameDecl? = null
        processStructDeclaration(startElement) { decl ->
            if (decl.name != name) return@processStructDeclaration true
            result = decl
            false
        }
        return result
    }

    fun processStructDeclaration(
        startElement: PsiElement,
        processor: (element: GdsStructNameDecl) -> Boolean,
    ): Boolean {
        val site = GdsMacroExpansion.expansionSite(startElement)
        val file = site.containingFile
        val originFile = file.originalFile
        // Godot registers a struct only after its closing ';', so its own body cannot refer to it.
        val startOffset = site.parentOfType<GdsStructDeclaration>()?.textRange?.startOffset ?: site.textOffset
        return file.processDeclarations(
            GdsScopeProcessor(GdsStructNameDecl::class.java, startOffset, originFile, processor),
            ResolveState.initial(),
            null,
            site,
        )
    }

    private inline fun walkUpLocalScope(
        startElement: PsiElement,
        action: (PsiElement) -> Boolean,
    ): Boolean {
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

    private fun processBuiltinVariables(
        element: PsiElement,
        processor: (element: GdsVariable) -> Boolean,
    ): Boolean {
        val shaderType = GdsPsiImplUtil.getShaderType(element)
        val functionContext = GdsPsiImplUtil.getFunctionContext(element) ?: FunctionContext.COMMON

        val project = element.project
        val manager = PsiManager.getInstance(project)

        val specs = Builtins.getVariables(shaderType, functionContext, element.text)
        if (specs.isNotEmpty()) {
            val isTypeAmbiguous = specs.distinctBy { it.type }.size > 1
            val isAssignable = specs.any { it.isWritableBuiltin }
            return !processor(GdsLightVariable(manager, specs.first(), isTypeAmbiguous, isAssignable))
        }
        return true
    }

    private fun processBuiltinFunctions(
        element: PsiElement,
        processor: (element: GdsFunction) -> Boolean,
    ): Boolean {
        val shaderType = GdsPsiImplUtil.getShaderType(element)
        val functionContext = GdsPsiImplUtil.getFunctionContext(element) ?: FunctionContext.COMMON

        val project = element.project
        val manager = PsiManager.getInstance(project)

        val spec = Builtins.getFunctions(shaderType, functionContext, element.text) ?: return true
        spec.forEach {
            if (!processor(GdsLightFunction(manager, it))) return false
        }
        return true
    }
}

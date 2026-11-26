package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.BoolType
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.GeneratedBuiltins
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.SamplerType
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.model.UIntType
import kr.jaehoyi.gdshader.model.VectorType
import kr.jaehoyi.gdshader.psi.GdsAssignExpr
import kr.jaehoyi.gdshader.psi.GdsConstantEvaluator
import kr.jaehoyi.gdshader.psi.GdsConstantReducer
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsPostfixExpr
import kr.jaehoyi.gdshader.psi.GdsSimpleStatement
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsArrayMethodResolver
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsFunctionContextAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.STATEMENT_VALIDATION)) return

        when (element) {
            is GdsSimpleStatement -> if (element.firstChild?.node?.elementType == GdsTypes.CF_DISCARD) checkDiscard(element, holder)
            is GdsFunctionCall -> {
                checkRecursion(element, holder)
                checkFragmentOnlyFunction(element, holder)
                checkTextureGather(element, holder)
            }
            is GdsVaryingDeclaration -> checkVaryingDeclaration(element, holder)
            is GdsVariableNameRef -> checkVaryingAssignment(element, holder)
        }
    }

    private fun checkDiscard(
        element: GdsSimpleStatement,
        holder: AnnotationHolder,
    ) {
        val shaderType = GdsPsiImplUtil.getShaderType(element)
        if (shaderType != null && shaderType != ShaderType.SPATIAL && shaderType != ShaderType.CANVAS_ITEM) {
            error(holder, element, "Use of 'discard' is not supported for the '${shaderType.text}' shader type.")
            return
        }
        checkRestrictedItem(element, "discard", holder)
    }

    private fun checkRecursion(
        call: GdsFunctionCall,
        holder: AnnotationHolder,
    ) {
        val nameRef = call.functionNameRef ?: return
        if (GdsArrayMethodResolver.isMemberCall(call)) return
        val function = call.parentOfType<GdsFunctionDeclaration>() ?: return
        if (nameRef.text != function.functionNameDecl.name) return
        if (resolveCallTarget(call, function) === function) error(holder, nameRef, "Recursion is not allowed.")
    }

    // Mirrors how Godot binds a call before its recursion check: an exact match among the overloads declared so far,
    // else the one with the most leading matching arguments, else the first one declared, which keeps the plain name.
    private fun resolveCallTarget(
        call: GdsFunctionCall,
        function: GdsFunctionDeclaration,
    ): GdsFunctionDeclaration? {
        val nameDecl = function.functionNameDecl
        val candidates = mutableListOf<Pair<GdsFunctionDeclaration, FunctionSpec>>()
        GdsResolver.processFunctionDeclaration(call) { candidate ->
            if (candidate is GdsFunctionNameDecl &&
                candidate.name == nameDecl.name &&
                !GdsConditionalDirectives.areExclusive(candidate, nameDecl)
            ) {
                val declaration = candidate.parent as? GdsFunctionDeclaration
                val spec = candidate.functionSpec
                if (declaration != null && spec != null) candidates += declaration to spec
            }
            true
        }
        if (candidates.size <= 1) return candidates.firstOrNull()?.first

        val argTypes =
            call.argumentList?.initializerList.orEmpty().map { initializer ->
                initializer.expression?.let { GdsExpressionTypeInference.inferType(it) } ?: return null
            }
        val exact = GdsOverloadResolver.resolveFunctionOverload(candidates.map { it.second }, argTypes, GdsConstantReducer.reduceArguments(call))
        if (exact != null) return candidates.first { it.second === exact }.first

        var best = candidates.first().first
        var bestCount = 0
        for ((declaration, spec) in candidates) {
            if (spec.parameters.size != argTypes.size) continue
            val count =
                spec.parameters
                    .zip(argTypes)
                    .takeWhile { (parameter, argType) -> parameter.type == argType }
                    .size
            if (count > bestCount) {
                best = declaration
                bestCount = count
            }
        }
        return best
    }

    private fun checkFragmentOnlyFunction(
        call: GdsFunctionCall,
        holder: AnnotationHolder,
    ) {
        val nameRef = call.functionNameRef ?: return
        val name = nameRef.text
        if (name !in FRAGMENT_ONLY_FUNCTIONS) return
        if (nameRef.reference.resolve() is GdsFunctionNameDecl) return

        if (isUnsupportedFragmentOnlyFunction(nameRef)) {
            val shaderType = GdsPsiImplUtil.getShaderType(call) ?: return
            error(holder, nameRef, "Built-in function '$name' is not supported for the '${shaderType.text}' shader type.")
            return
        }
        checkRestrictedItem(nameRef, name, holder)
    }

    private fun checkRestrictedItem(
        element: PsiElement,
        item: String,
        holder: AnnotationHolder,
    ) {
        val function = element.parentOfType<GdsFunctionDeclaration>() ?: return
        val functionName = function.functionNameDecl.name
        val shaderType = GdsPsiImplUtil.getShaderType(element)
        if (shaderType != ShaderType.SPATIAL && shaderType != ShaderType.CANVAS_ITEM) return

        if (FunctionContext.fromText(functionName) == FunctionContext.VERTEX) {
            error(holder, element, "'$item' cannot be used within the 'vertex' processor function.")
        } else if (functionName in calledFromVertex(element.containingFile)) {
            error(holder, element, "'$item' cannot be used here, because '$functionName' is called by the 'vertex' processor function (which is not allowed).")
        }
    }

    private fun calledFromVertex(file: PsiFile): Set<String> =
        CachedValuesManager.getCachedValue(file, CALLED_FROM_VERTEX_KEY) {
            val calls = mutableMapOf<String, MutableSet<String>>()
            for (declaration in PsiTreeUtil.findChildrenOfType(file, GdsFunctionDeclaration::class.java)) {
                val callees = calls.getOrPut(declaration.functionNameDecl.name) { mutableSetOf() }
                PsiTreeUtil.findChildrenOfType(declaration, GdsFunctionCall::class.java).mapNotNullTo(callees) { it.functionNameRef?.text }
            }
            val reached = mutableSetOf<String>()
            val pending = ArrayDeque(calls["vertex"].orEmpty())
            while (pending.isNotEmpty()) {
                val name = pending.removeFirst()
                if (name == "vertex" || !calls.containsKey(name) || !reached.add(name)) continue
                pending.addAll(calls[name].orEmpty())
            }
            CachedValueProvider.Result.create(reached.toSet(), PsiModificationTracker.MODIFICATION_COUNT)
        }

    private fun checkTextureGather(
        call: GdsFunctionCall,
        holder: AnnotationHolder,
    ) {
        val nameRef = call.functionNameRef ?: return
        if (nameRef.text != "textureGather" || nameRef.reference.resolve() is GdsFunctionNameDecl) return
        val argument =
            call.argumentList
                ?.initializerList
                ?.getOrNull(2)
                ?.expression ?: return
        val value = GdsConstantEvaluator.evaluate(argument)
        val isInt = GdsExpressionTypeInference.inferType(argument) is IntType
        if (isInt && value is Int && value in 0..3) return
        error(holder, argument, "Expected integer constant within [0..3] range.")
    }

    private fun checkVaryingDeclaration(
        element: GdsVaryingDeclaration,
        holder: AnnotationHolder,
    ) {
        if (element.parentOfType<GdsFunctionDeclaration>() != null) return
        val shaderType = GdsPsiImplUtil.getShaderType(element)
        if (shaderType == ShaderType.PARTICLES || shaderType == ShaderType.SKY || shaderType == ShaderType.FOG) {
            error(holder, element, "Varyings cannot be used in '${shaderType.text}' shaders.")
            return
        }

        val type = element.type?.let { GdsDataTypeFactory.createFromType(it) } ?: return
        val interpolation = element.interpolationQualifier?.text
        when {
            type is SamplerType -> error(holder, element.type ?: element, "Invalid data type for varying.")
            interpolation != null && isBoolean(type) ->
                error(holder, element.interpolationQualifier ?: element, "Interpolation modifier '$interpolation' cannot be used with boolean types.")
            isInteger(type) && interpolation != "flat" && firstAssignmentContext(element) != FunctionContext.FRAGMENT ->
                error(holder, element.variableNameDecl ?: element, "Varying with integer data type must be declared with `flat` interpolation qualifier.")
        }
    }

    private fun checkVaryingAssignment(
        ref: GdsVariableNameRef,
        holder: AnnotationHolder,
    ) {
        if (!isAssignmentTarget(ref)) return
        val declaration = (ref.reference.resolve() as? GdsVariableNameDecl)?.parent as? GdsVaryingDeclaration ?: return
        if (declaration.parentOfType<GdsFunctionDeclaration>() != null) return
        val function = ref.parentOfType<GdsFunctionDeclaration>() ?: return
        val functionName = function.functionNameDecl.name

        val context = FunctionContext.fromText(functionName)
        if (context != FunctionContext.VERTEX && context != FunctionContext.FRAGMENT) {
            error(holder, ref, "Varying may not be assigned in the '$functionName' function.")
            return
        }

        val type = declaration.type?.let { GdsDataTypeFactory.createFromType(it) } ?: return
        if (context == FunctionContext.VERTEX && isBoolean(type) && firstAssignment(declaration) == ref) {
            error(holder, ref, "Varying with '${type.name}' data type may only be assigned in the 'fragment' function.")
        }
    }

    private fun isAssignmentTarget(ref: GdsVariableNameRef): Boolean {
        val postfix = ref.parentOfType<GdsPostfixExpr>() ?: return false
        if (postfix.primary.variableNameRef != ref) return false
        if (postfix.functionCallList.isNotEmpty()) return false
        val assign = PsiTreeUtil.getParentOfType(postfix, GdsAssignExpr::class.java) ?: return false
        val target = assign.conditionalExprList.firstOrNull() ?: return false
        return assign.assignmentOperatorList.isNotEmpty() && target.textRange == postfix.textRange
    }

    private fun firstAssignment(declaration: GdsVaryingDeclaration): GdsVariableNameRef? {
        val nameDecl = declaration.variableNameDecl ?: return null
        return PsiTreeUtil
            .findChildrenOfType(declaration.containingFile, GdsVariableNameRef::class.java)
            .firstOrNull { it.text == nameDecl.name && isAssignmentTarget(it) && it.reference.resolve() == nameDecl }
    }

    private fun firstAssignmentContext(declaration: GdsVaryingDeclaration): FunctionContext? = firstAssignment(declaration)?.parentOfType<GdsFunctionDeclaration>()?.let { FunctionContext.fromText(it.functionNameDecl.name) }

    private fun isBoolean(type: DataType): Boolean = type is BoolType || (type is VectorType && type.elementType is BoolType)

    private fun isInteger(type: DataType): Boolean = type is IntType || type is UIntType || (type is VectorType && (type.elementType is IntType || type.elementType is UIntType))

    private fun error(
        holder: AnnotationHolder,
        element: PsiElement,
        message: String,
    ) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    companion object {
        private val CALLED_FROM_VERTEX_KEY = Key.create<CachedValue<Set<String>>>("GDS_CALLED_FROM_VERTEX")
        private val FRAGMENT_ONLY_FUNCTIONS: Set<String> by lazy { GeneratedBuiltins.fragmentOnlyFunctions.map { it.name }.toSet() }
        private val FRAGMENT_ONLY_SHADER_TYPES = setOf(ShaderType.SPATIAL, ShaderType.CANVAS_ITEM, ShaderType.SKY)

        fun isUnsupportedFragmentOnlyFunction(nameRef: PsiElement): Boolean {
            if (nameRef.text !in FRAGMENT_ONLY_FUNCTIONS) return false
            val shaderType = GdsPsiImplUtil.getShaderType(nameRef) ?: return false
            return shaderType !in FRAGMENT_ONLY_SHADER_TYPES
        }
    }
}

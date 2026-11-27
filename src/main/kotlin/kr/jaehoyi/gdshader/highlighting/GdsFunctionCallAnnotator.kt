package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.Instantiable
import kr.jaehoyi.gdshader.model.ParameterQualifier
import kr.jaehoyi.gdshader.model.ParameterSpec
import kr.jaehoyi.gdshader.model.UniformSpec
import kr.jaehoyi.gdshader.model.VaryingSpec
import kr.jaehoyi.gdshader.psi.GdsArgumentList
import kr.jaehoyi.gdshader.psi.GdsAssignExpr
import kr.jaehoyi.gdshader.psi.GdsBlock
import kr.jaehoyi.gdshader.psi.GdsConditionalExpr
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsConstantReducer
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpression
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsInitializer
import kr.jaehoyi.gdshader.psi.GdsPostfixExpr
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsArrayMethodResolver
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsFunctionCallAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (element !is GdsFunctionCall) return
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.FUNCTION_CALL_VALIDATION)) return

        if (GdsArrayMethodResolver.isMemberCall(element)) {
            val spec = GdsArrayMethodResolver.resolve(element) ?: return
            checkArguments(listOf(spec), getArgumentCount(element), spec.name, element, holder) { specs, types ->
                GdsOverloadResolver.resolveFunctionOverload(specs, types)
            }
            return
        }

        element.type?.let { typeNode ->
            checkConstructorCall(element, typeNode.text, holder)
            return
        }

        if (element.arraySize != null) {
            checkStructArrayConstructor(element, holder)
            return
        }

        element.functionNameRef?.let { nameRef ->
            checkFunctionCall(element, nameRef.text, holder)
        }
    }

    private fun checkConstructorCall(
        functionCall: GdsFunctionCall,
        typeName: String,
        holder: AnnotationHolder,
    ) {
        val baseType = Builtins.getType(typeName) ?: return
        if (functionCall.arraySize != null) return
        if (baseType !is Instantiable) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Type '$typeName' has no constructor")
                .range(functionCall)
                .create()
            return
        }

        val constructors = baseType.getConstructors()
        if (constructors.isEmpty()) return

        val argCount = getArgumentCount(functionCall)
        checkArguments(constructors, argCount, typeName, functionCall, holder, GdsOverloadResolver::resolveConstructorOverload)
    }

    // Godot parses `Struct[N](...)` only where it expects an array value of a known type:
    // after `=` to an array variable or member, as an array member argument of a struct constructor,
    // and in a global constant initializer. Elsewhere, including local declarations, it reports
    // "Unknown identifier in expression".
    private fun checkStructArrayConstructor(
        functionCall: GdsFunctionCall,
        holder: AnnotationHolder,
    ) {
        val nameRef = functionCall.functionNameRef ?: return
        GdsDataTypeFactory.createFromStructName(nameRef, nameRef.text) ?: return
        if (isStructArrayConstructorAllowed(functionCall)) return

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Struct array constructor is not allowed here")
            .range(nameRef)
            .create()
    }

    private fun isStructArrayConstructorAllowed(functionCall: GdsFunctionCall): Boolean {
        var top: PsiElement = functionCall
        while (true) {
            val parent = top.parent ?: break
            if (parent is GdsArgumentList || parent.textRange != functionCall.textRange) break
            top = parent
        }

        val parent = top.parent
        return when {
            top is GdsConditionalExpr && parent is GdsAssignExpr -> isArrayAssignmentValue(parent, top)
            top is GdsInitializer && parent is GdsConstantDeclarator -> PsiTreeUtil.getParentOfType(parent, GdsBlock::class.java) == null
            top is GdsInitializer && parent is GdsArgumentList -> isStructArrayMemberArgument(parent, top)
            else -> false
        }
    }

    private fun isArrayAssignmentValue(
        assignExpr: GdsAssignExpr,
        value: GdsConditionalExpr,
    ): Boolean {
        val index = assignExpr.conditionalExprList.indexOf(value)
        if (index < 1) return false
        val operator = assignExpr.assignmentOperatorList.getOrNull(index - 1) ?: return false
        if (operator.node.firstChildNode.elementType != GdsTypes.OP_ASSIGN) return false
        val targetType = GdsExpressionTypeInference.inferType(assignExpr.conditionalExprList[index - 1]) ?: return true
        return targetType is ArrayType
    }

    private fun isStructArrayMemberArgument(
        argumentList: GdsArgumentList,
        argument: GdsInitializer,
    ): Boolean {
        val constructorCall = argumentList.parent as? GdsFunctionCall ?: return false
        if (constructorCall.arraySize != null) return false
        val nameRef = constructorCall.functionNameRef ?: return false
        val structType = GdsDataTypeFactory.createFromStructName(nameRef, nameRef.text) ?: return false
        if (structType.hasUnresolvedMembers) return true
        val index = argumentList.initializerList.indexOf(argument)
        return structType.members.values.elementAtOrNull(index) is ArrayType
    }

    private fun checkFunctionCall(
        functionCall: GdsFunctionCall,
        functionName: String,
        holder: AnnotationHolder,
    ) {
        val nameRef = functionCall.functionNameRef ?: return
        if (callsProcessingFunction(nameRef, functionName)) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Function '$functionName' can't be called from source code.")
                .range(nameRef)
                .create()
            return
        }
        val functions = collectFunctionCandidates(functionCall, functionName) ?: return
        val argCount = getArgumentCount(functionCall)

        if (functions.isNotEmpty()) {
            val resolved =
                checkArguments(functions, argCount, functionName, functionCall, holder) { specs, argTypes ->
                    GdsOverloadResolver.resolveFunctionOverload(specs, argTypes, GdsConstantReducer.reduceArguments(functionCall))
                }
            resolved?.let { checkOutArguments(functionCall, it, holder) }
            return
        }

        val structConstructors = GdsDataTypeFactory.createFromStructName(nameRef, functionName)?.getConstructors().orEmpty()
        if (structConstructors.isEmpty()) return

        checkArguments(structConstructors, argCount, functionName, functionCall, holder, GdsOverloadResolver::resolveStructConstructorOverload)
    }

    private fun callsProcessingFunction(
        nameRef: PsiElement,
        functionName: String,
    ): Boolean {
        if (!Builtins.isProcessingFunction(functionName, GdsPsiImplUtil.getShaderType(nameRef))) return false
        var declared = false
        GdsResolver.processFunctionDeclaration(nameRef) { func ->
            declared = func is GdsFunctionNameDecl && func.name == functionName
            !declared
        }
        return declared
    }

    private fun collectFunctionCandidates(
        functionCall: GdsFunctionCall,
        functionName: String,
    ): List<FunctionSpec>? {
        val nameRef = functionCall.functionNameRef ?: return null
        val candidates = mutableListOf<FunctionSpec>()
        var hasUnresolvedDeclaration = false

        GdsResolver.processFunctionDeclaration(nameRef) { func ->
            if (func.name == functionName) {
                val spec = func.functionSpec
                if (spec != null) {
                    candidates.add(spec)
                } else {
                    hasUnresolvedDeclaration = true
                }
            }
            true
        }

        // A declaration whose types cannot be resolved may be the intended overload, so checking only the rest would report a false mismatch.
        if (hasUnresolvedDeclaration) return null
        return candidates
    }

    private fun getArgumentCount(functionCall: GdsFunctionCall): Int {
        val argList = functionCall.argumentList ?: return 0
        return argList.initializerList.size
    }

    private fun collectArgumentTypes(functionCall: GdsFunctionCall): List<DataType>? {
        val argList = functionCall.argumentList ?: return emptyList()
        return argList.initializerList.map { initializer ->
            (initializer.expression ?: initializer.initializerList)?.let { GdsExpressionTypeInference.inferType(it) } ?: return null
        }
    }

    private fun checkArguments(
        candidates: List<FunctionSpec>,
        argCount: Int,
        name: String,
        functionCall: GdsFunctionCall,
        holder: AnnotationHolder,
        resolveOverload: (List<FunctionSpec>, List<DataType>) -> FunctionSpec?,
    ): FunctionSpec? {
        val countMatches =
            candidates.filter { spec ->
                val requiredCount = spec.parameters.count { !it.isOptional }
                val totalCount = spec.parameters.size
                argCount in requiredCount..totalCount
            }

        if (countMatches.isEmpty()) {
            annotateArgumentCount(candidates, argCount, name, functionCall, holder)
            return null
        }

        val argTypes = collectArgumentTypes(functionCall) ?: return null

        val resolved = resolveOverload(countMatches, argTypes)
        if (resolved != null) return resolved

        if (countMatches.any { Builtins.isEngineFunction(it) }) {
            val argListElement = functionCall.argumentList ?: return null
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Invalid arguments for the built-in function: '$name(${argTypes.joinToString(",") { it.name }})'",
                ).range(argListElement)
                .create()
            return null
        }

        val bestCandidate = countMatches.firstOrNull() ?: return null
        for (i in argTypes.indices) {
            if (i >= bestCandidate.parameters.size) break
            val paramType = bestCandidate.parameters[i].type
            val argType = argTypes[i]
            if (paramType != argType) {
                val signature = formatSignature(name, bestCandidate)
                val argListElement = functionCall.argumentList ?: return null
                holder
                    .newAnnotation(
                        HighlightSeverity.ERROR,
                        "No matching function for '$signature' call: argument ${i + 1} should be ${paramType.name} but is ${argType.name}",
                    ).range(argListElement)
                    .create()
                return null
            }
        }
        return null
    }

    private fun checkOutArguments(
        functionCall: GdsFunctionCall,
        spec: FunctionSpec,
        holder: AnnotationHolder,
    ) {
        val arguments = functionCall.argumentList?.initializerList ?: return
        spec.parameters.forEachIndexed { index, parameter ->
            if (parameter.qualifier != ParameterQualifier.OUT && parameter.qualifier != ParameterQualifier.INOUT) return@forEachIndexed
            val argument = arguments.getOrNull(index) ?: return
            if (!isConstantArgument(argument)) return@forEachIndexed
            holder
                .newAnnotation(HighlightSeverity.ERROR, "A constant value cannot be passed for the '${parameter.qualifier.text}' parameter.")
                .range(argument)
                .create()
            return
        }
    }

    // Mirrors Godot's node kinds rather than assignability: `v[0]` on a vector is rejected,
    // while a member of a uniform (`u.x`) or a swizzle with duplicates (`v.xx`) is accepted.
    private fun isConstantArgument(argument: GdsInitializer): Boolean {
        if (argument.initializerList != null) return true
        val expression = argument.expression ?: return false
        return isConstantExpression(expression)
    }

    private fun isConstantExpression(expression: GdsExpression): Boolean {
        var current: PsiElement = expression
        while (current !is GdsPostfixExpr) {
            current = current.children.singleOrNull()?.takeIf { it.textRange == expression.textRange } ?: return true
        }
        return isConstantPostfix(current)
    }

    private fun isConstantPostfix(postfix: GdsPostfixExpr): Boolean {
        if (postfix.node.findChildByType(INCREMENT_DECREMENT_TOKENS) != null) return true
        val primary = postfix.primary
        val accessors = postfix.children.filter { it != primary }
        val root = primary.variableNameRef

        if (accessors.isEmpty()) {
            primary.expression?.let { return isConstantExpression(it) }
            if (root == null) return true
            return rootKind(root).let { it == RootKind.CONST || it == RootKind.UNIFORM }
        }
        if (accessors.any { it is GdsFunctionCall }) return true

        val last = accessors.last()
        if (last is GdsExpression) {
            if (GdsExpressionTypeInference.inferTypeBefore(postfix, last) !is ArrayType) return true
            if (accessors.size == 1) {
                if (root == null) return true
                return rootKind(root).let { it == RootKind.CONST || it == RootKind.UNIFORM }
            }
        }
        return root != null && rootKind(root) == RootKind.CONST
    }

    private enum class RootKind { CONST, UNIFORM, MUTABLE, UNKNOWN }

    private fun rootKind(ref: GdsVariableNameRef): RootKind =
        when (val resolved = ref.reference.resolve()) {
            is GdsVariableNameDecl ->
                when (val spec = resolved.variableSpec) {
                    is ConstantSpec -> RootKind.CONST
                    is UniformSpec -> RootKind.UNIFORM
                    is ParameterSpec -> if (spec.isMutable) RootKind.MUTABLE else RootKind.CONST
                    // Godot checks varyings against the shader stage with a different error.
                    is VaryingSpec, null -> RootKind.UNKNOWN
                    else -> RootKind.MUTABLE
                }
            is GdsLightVariable ->
                when {
                    resolved.isTypeAmbiguous -> RootKind.UNKNOWN
                    resolved.isAssignable -> RootKind.MUTABLE
                    else -> RootKind.CONST
                }
            else -> RootKind.UNKNOWN
        }

    private companion object {
        val INCREMENT_DECREMENT_TOKENS = TokenSet.create(GdsTypes.OP_INCREMENT, GdsTypes.OP_DECREMENT)
    }

    private fun annotateArgumentCount(
        candidates: List<FunctionSpec>,
        argCount: Int,
        name: String,
        functionCall: GdsFunctionCall,
        holder: AnnotationHolder,
    ) {
        val minRequired = candidates.minOf { it.parameters.count { p -> !p.isOptional } }
        val maxTotal = candidates.maxOf { it.parameters.size }
        val tooFew = argCount < minRequired

        val bestCandidate =
            if (tooFew) {
                candidates.minByOrNull { it.parameters.count { p -> !p.isOptional } } ?: return
            } else {
                candidates.maxByOrNull { it.parameters.size } ?: return
            }

        val signature = formatSignature(name, bestCandidate)
        val bound = if (tooFew) "at least $minRequired" else "at most $maxTotal"

        val argListElement = functionCall.argumentList ?: return
        holder
            .newAnnotation(
                HighlightSeverity.ERROR,
                "Too ${if (tooFew) "few" else "many"} arguments for '$signature' call. Expected $bound but received $argCount.",
            ).range(argListElement)
            .create()
    }

    private fun formatSignature(
        name: String,
        spec: FunctionSpec,
    ): String {
        val params = spec.parameters.joinToString(", ") { it.type.name }
        return "$name($params)"
    }
}

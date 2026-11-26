package kr.jaehoyi.gdshader.resolve

import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.GdsConstantReducer

object GdsOverloadResolver {
    data class FunctionCallResolution(
        val spec: FunctionSpec,
        val returnType: DataType,
    )

    fun resolveFunctionOverload(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
        constantArguments: List<GdsConstantReducer.Result> = emptyList(),
    ): FunctionSpec? = resolveFunctionCall(candidates, argumentTypes, constantArguments)?.spec

    // Godot converts scalar constant arguments for every built-in function, never for stage functions,
    // and for a user function only when its name has no overloads.
    fun resolveFunctionCall(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
        constantArguments: List<GdsConstantReducer.Result> = emptyList(),
    ): FunctionCallResolution? {
        val countMatches = filterByArgumentCount(candidates, argumentTypes.size)
        if (countMatches.isEmpty()) return null

        val userFunctionCount = candidates.count { !Builtins.isEngineFunction(it) && !Builtins.isStageFunction(it) }
        val noConversions = emptyList<GdsConstantReducer.Result>()

        for (allowConversions in listOf(false, true)) {
            for (spec in countMatches) {
                val convertible =
                    when {
                        !allowConversions -> false
                        Builtins.isEngineFunction(spec) -> true
                        Builtins.isStageFunction(spec) -> false
                        else -> userFunctionCount == 1
                    }
                if (allowConversions && !convertible) continue

                val bindings = bindArguments(spec.parameters, argumentTypes, if (convertible) constantArguments else noConversions)
                if (bindings != null) return FunctionCallResolution(spec, substituteReturnType(spec.returnType, bindings))
            }
        }
        return null
    }

    private fun bindArguments(
        params: List<ParameterSpec>,
        argTypes: List<DataType>,
        constantArguments: List<GdsConstantReducer.Result>,
    ): Map<AliasType, DataType>? {
        val bindings = mutableMapOf<AliasType, DataType>()
        for (i in argTypes.indices) {
            if (i >= params.size) return null
            val paramType = params[i].type
            val argType = argTypes[i]
            val constant = constantArguments.getOrNull(i)?.takeIf { it != GdsConstantReducer.NotConstant }

            val boundType =
                when {
                    paramType == argType -> continue
                    paramType is AliasType ->
                        when {
                            matchesAliasType(paramType, argType) -> argType
                            constant != null && argType is Scalar -> {
                                val scalar = aliasScalar(paramType) ?: return null
                                if (!isConstantConvertible(constant, scalar)) return null
                                scalar
                            }
                            else -> return null
                        }
                    constant != null && paramType is Scalar && argType is Scalar ->
                        if (isConstantConvertible(constant, paramType)) continue else return null
                    else -> return null
                }

            val existing = bindings[paramType]
            if (existing != null && existing != boundType) return null
            bindings[paramType as AliasType] = boundType
        }
        return bindings
    }

    private fun substituteReturnType(
        returnType: DataType,
        bindings: Map<AliasType, DataType>,
    ): DataType {
        if (returnType !is AliasType) return returnType
        bindings[returnType]?.let { return it }
        if (returnType == AliasType.GVEC4_TYPE) {
            bindings.entries
                .firstOrNull { isGenericSampler(it.key) }
                ?.let { resolveGvec4FromSampler(it.value) }
                ?.let { return it }
        }
        return returnType
    }

    private fun aliasScalar(aliasType: AliasType): Scalar? =
        when (aliasType) {
            AliasType.VEC_TYPE -> FloatType.DEFAULT
            AliasType.VEC_INT_TYPE -> IntType
            AliasType.VEC_UINT_TYPE -> UIntType
            AliasType.VEC_BOOL_TYPE -> BoolType
            else -> null
        }

    // A macro argument may expand to a literal, so it is given the benefit of the doubt.
    private fun isConstantConvertible(
        constant: GdsConstantReducer.Result,
        to: Scalar,
    ): Boolean =
        when (constant) {
            is GdsConstantReducer.Constant -> GdsConstantReducer.convertConstant(constant, to)
            else -> true
        }

    fun resolveConstructorOverload(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
    ): FunctionSpec? =
        resolveOverload(candidates, argumentTypes) { paramType, argType, _, aliasBindings ->
            isConstructorTypeCompatible(paramType, argType, aliasBindings)
        }

    fun resolveStructConstructorOverload(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
    ): FunctionSpec? =
        resolveOverload(candidates, argumentTypes) { paramType, argType, _, _ ->
            paramType == argType
        }

    private fun resolveOverload(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
        typeCompatible: (DataType, DataType, Int, MutableMap<AliasType, DataType>) -> Boolean,
    ): FunctionSpec? {
        val countMatches = filterByArgumentCount(candidates, argumentTypes.size)
        if (countMatches.isEmpty()) return null

        val exactMatches =
            countMatches.filter { spec ->
                matchesExactly(spec.parameters, argumentTypes)
            }
        if (exactMatches.size == 1) return exactMatches.first()

        val compatibleMatches =
            countMatches.filter { spec ->
                matchesWithCompatibleTypes(spec.parameters, argumentTypes, typeCompatible)
            }

        return compatibleMatches.firstOrNull()
    }

    fun filterByArgumentCount(
        candidates: List<FunctionSpec>,
        argumentCount: Int,
    ): List<FunctionSpec> =
        candidates.filter { spec ->
            val requiredCount = spec.parameters.count { !it.isOptional }
            argumentCount in requiredCount..spec.parameters.size
        }

    fun matchesAliasType(
        aliasType: AliasType,
        concreteType: DataType,
    ): Boolean =
        when (aliasType) {
            AliasType.VEC_TYPE -> concreteType is FloatType || isFloatVector(concreteType)
            AliasType.VEC_INT_TYPE -> concreteType is IntType || isIntVector(concreteType)
            AliasType.VEC_UINT_TYPE -> concreteType is UIntType || isUIntVector(concreteType)
            AliasType.VEC_BOOL_TYPE -> concreteType is BoolType || isBoolVector(concreteType)
            AliasType.MAT_TYPE -> concreteType is MatrixType
            AliasType.GVEC4_TYPE ->
                concreteType == VectorType.VEC4 ||
                    concreteType == VectorType.IVEC4 ||
                    concreteType == VectorType.UVEC4
            AliasType.GSAMPLER2D ->
                concreteType == SamplerType.SAMPLER2D ||
                    concreteType == SamplerType.ISAMPLER2D ||
                    concreteType == SamplerType.USAMPLER2D
            AliasType.GSAMPLER2DARRAY ->
                concreteType == SamplerType.SAMPLER2DARRAY ||
                    concreteType == SamplerType.ISAMPLER2DARRAY ||
                    concreteType == SamplerType.USAMPLER2DARRAY
            AliasType.GSAMPLER3D ->
                concreteType == SamplerType.SAMPLER3D ||
                    concreteType == SamplerType.ISAMPLER3D ||
                    concreteType == SamplerType.USAMPLER3D
            else -> false
        }

    private fun matchesExactly(
        params: List<ParameterSpec>,
        argTypes: List<DataType>,
    ): Boolean {
        for (i in argTypes.indices) {
            if (i >= params.size) return false
            val paramType = params[i].type
            val argType = argTypes[i]

            if (paramType != argType) return false
        }
        return true
    }

    private fun matchesWithCompatibleTypes(
        params: List<ParameterSpec>,
        argTypes: List<DataType>,
        typeCompatible: (DataType, DataType, Int, MutableMap<AliasType, DataType>) -> Boolean,
    ): Boolean {
        val aliasBindings = mutableMapOf<AliasType, DataType>()

        for (i in argTypes.indices) {
            if (i >= params.size) return false
            val paramType = params[i].type
            val argType = argTypes[i]

            if (!typeCompatible(paramType, argType, i, aliasBindings)) return false
        }
        return true
    }

    private fun isAliasCompatible(
        paramType: DataType,
        argType: DataType,
        aliasBindings: MutableMap<AliasType, DataType>,
    ): Boolean {
        if (paramType == argType) return true
        if (paramType !is AliasType || !matchesAliasType(paramType, argType)) return false

        val existingBinding = aliasBindings[paramType]
        if (existingBinding != null) {
            return existingBinding == argType
        }
        aliasBindings[paramType] = argType
        return true
    }

    private fun isConstructorTypeCompatible(
        paramType: DataType,
        argType: DataType,
        aliasBindings: MutableMap<AliasType, DataType>,
    ): Boolean {
        if (isAliasCompatible(paramType, argType, aliasBindings)) return true

        return when {
            paramType is Scalar && argType is Scalar -> isConstructorScalarConvertible(argType, paramType)
            else -> false
        }
    }

    private fun isConstructorScalarConvertible(
        from: Scalar,
        to: Scalar,
    ): Boolean =
        when (from) {
            is BoolType -> to is BoolType || to is IntType || to is UIntType || to is FloatType
            is IntType -> to is IntType || to is UIntType || to is FloatType
            is UIntType -> to is IntType || to is UIntType || to is FloatType
            is FloatType -> to is FloatType
        }

    private fun isGenericSampler(type: DataType): Boolean =
        type == AliasType.GSAMPLER2D ||
            type == AliasType.GSAMPLER2DARRAY ||
            type == AliasType.GSAMPLER3D

    private fun resolveGvec4FromSampler(samplerType: DataType): DataType? =
        when (samplerType) {
            SamplerType.SAMPLER2D, SamplerType.SAMPLER2DARRAY, SamplerType.SAMPLER3D,
            SamplerType.SAMPLERCUBE, SamplerType.SAMPLERCUBEARRAY, SamplerType.SAMPLEREXTERNALOES,
            -> VectorType.VEC4
            SamplerType.ISAMPLER2D, SamplerType.ISAMPLER2DARRAY, SamplerType.ISAMPLER3D -> VectorType.IVEC4
            SamplerType.USAMPLER2D, SamplerType.USAMPLER2DARRAY, SamplerType.USAMPLER3D -> VectorType.UVEC4
            else -> null
        }

    private fun isFloatVector(type: DataType): Boolean = type is VectorType && type.elementType is FloatType

    private fun isIntVector(type: DataType): Boolean = type is VectorType && type.elementType is IntType

    private fun isUIntVector(type: DataType): Boolean = type is VectorType && type.elementType is UIntType

    private fun isBoolVector(type: DataType): Boolean = type is VectorType && type.elementType is BoolType
}

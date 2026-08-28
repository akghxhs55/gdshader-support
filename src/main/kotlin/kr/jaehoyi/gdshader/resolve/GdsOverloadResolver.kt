package kr.jaehoyi.gdshader.resolve

import kr.jaehoyi.gdshader.model.*

object GdsOverloadResolver {
    fun resolveFunctionOverload(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
        constantArguments: List<Boolean> = emptyList(),
    ): FunctionSpec? {
        val allowConstantConversions = candidates.size == 1
        return resolveOverload(candidates, argumentTypes) { paramType, argType, index, aliasBindings ->
            isFunctionTypeCompatible(
                paramType,
                argType,
                index,
                constantArguments,
                allowConstantConversions,
                aliasBindings,
            )
        }
    }

    fun resolveConstructorOverload(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
    ): FunctionSpec? =
        resolveOverload(candidates, argumentTypes) { paramType, argType, _, aliasBindings ->
            isConstructorTypeCompatible(paramType, argType, aliasBindings)
        }

    private fun resolveOverload(
        candidates: List<FunctionSpec>,
        argumentTypes: List<DataType>,
        typeCompatible: (DataType, DataType, Int, MutableMap<AliasType, DataType>) -> Boolean,
    ): FunctionSpec? {
        val countMatches =
            candidates.filter { spec ->
                val requiredCount = spec.parameters.count { !it.isOptional }
                val totalCount = spec.parameters.size
                argumentTypes.size in requiredCount..totalCount
            }

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

    fun resolveAliasType(
        aliasType: AliasType,
        argTypes: List<DataType>,
        paramTypes: List<DataType>,
    ): DataType? {
        for (i in paramTypes.indices) {
            if (i >= argTypes.size) break
            val paramType = paramTypes[i]
            val argType = argTypes[i]

            if (paramType == aliasType) {
                return argType
            }

            if (aliasType == AliasType.GVEC4_TYPE && isGenericSampler(paramType)) {
                return resolveGvec4FromSampler(argType)
            }
        }

        return null
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

    private fun isFunctionTypeCompatible(
        paramType: DataType,
        argType: DataType,
        argumentIndex: Int = 0,
        constantArguments: List<Boolean> = emptyList(),
        allowConstantConversions: Boolean = false,
        aliasBindings: MutableMap<AliasType, DataType>,
    ): Boolean {
        if (paramType == argType) return true

        if (allowConstantConversions && constantArguments.getOrElse(argumentIndex) { false }) {
            if (paramType is Scalar && argType is Scalar && isNumericScalarConvertible(argType, paramType)) return true
        }

        if (paramType is AliasType) {
            if (!matchesAliasType(paramType, argType)) return false

            val existingBinding = aliasBindings[paramType]
            if (existingBinding != null) {
                return existingBinding == argType
            }
            aliasBindings[paramType] = argType
            return true
        }

        return false
    }

    private fun isNumericScalarConvertible(
        from: Scalar,
        to: Scalar,
    ): Boolean =
        when (from) {
            is IntType -> to is IntType || to is UIntType || to is FloatType
            is UIntType -> to is IntType || to is UIntType || to is FloatType
            else -> false
        }

    private fun isConstructorTypeCompatible(
        paramType: DataType,
        argType: DataType,
        aliasBindings: MutableMap<AliasType, DataType>,
    ): Boolean {
        if (isFunctionTypeCompatible(paramType, argType, aliasBindings = aliasBindings)) return true

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

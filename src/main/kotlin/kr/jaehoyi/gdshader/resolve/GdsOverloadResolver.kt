package kr.jaehoyi.gdshader.resolve

import kr.jaehoyi.gdshader.model.*

object GdsOverloadResolver {

    fun resolveOverload(candidates: List<FunctionSpec>, argumentTypes: List<DataType>): FunctionSpec? {
        val countMatches = candidates.filter { spec ->
            val requiredCount = spec.parameters.count { !it.isOptional }
            val totalCount = spec.parameters.size
            argumentTypes.size in requiredCount..totalCount
        }

        if (countMatches.isEmpty()) return null

        val exactMatches = countMatches.filter { spec ->
            matchesExactly(spec.parameters, argumentTypes)
        }
        if (exactMatches.size == 1) return exactMatches.first()

        val compatibleMatches = countMatches.filter { spec ->
            matchesWithAliasTypes(spec.parameters, argumentTypes)
        }

        return compatibleMatches.firstOrNull()
    }

    fun resolveAliasType(aliasType: AliasType, argTypes: List<DataType>, paramTypes: List<DataType>): DataType? {
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

    fun matchesAliasType(aliasType: AliasType, concreteType: DataType): Boolean {
        return when (aliasType) {
            AliasType.VEC_TYPE -> concreteType is FloatType || isFloatVector(concreteType)
            AliasType.VEC_INT_TYPE -> concreteType is IntType || isIntVector(concreteType)
            AliasType.VEC_UINT_TYPE -> concreteType is UIntType || isUIntVector(concreteType)
            AliasType.VEC_BOOL_TYPE -> concreteType is BoolType || isBoolVector(concreteType)
            AliasType.MAT_TYPE -> concreteType is MatrixType
            AliasType.GVEC4_TYPE -> concreteType == VectorType.VEC4 ||
                                   concreteType == VectorType.IVEC4 ||
                                   concreteType == VectorType.UVEC4
            AliasType.GSAMPLER2D -> concreteType == SamplerType.SAMPLER2D ||
                                    concreteType == SamplerType.ISAMPLER2D ||
                                    concreteType == SamplerType.USAMPLER2D
            AliasType.GSAMPLER2DARRAY -> concreteType == SamplerType.SAMPLER2DARRAY ||
                                         concreteType == SamplerType.ISAMPLER2DARRAY ||
                                         concreteType == SamplerType.USAMPLER2DARRAY
            AliasType.GSAMPLER3D -> concreteType == SamplerType.SAMPLER3D ||
                                    concreteType == SamplerType.ISAMPLER3D ||
                                    concreteType == SamplerType.USAMPLER3D
            else -> false
        }
    }

    private fun matchesExactly(params: List<ParameterSpec>, argTypes: List<DataType>): Boolean {
        for (i in argTypes.indices) {
            if (i >= params.size) return false
            val paramType = params[i].type
            val argType = argTypes[i]

            if (paramType != argType) return false
        }
        return true
    }

    private fun matchesWithAliasTypes(params: List<ParameterSpec>, argTypes: List<DataType>): Boolean {
        val aliasBindings = mutableMapOf<AliasType, DataType>()

        for (i in argTypes.indices) {
            if (i >= params.size) return false
            val paramType = params[i].type
            val argType = argTypes[i]

            if (!isTypeCompatible(paramType, argType, aliasBindings)) return false
        }
        return true
    }

    private fun isTypeCompatible(
        paramType: DataType,
        argType: DataType,
        aliasBindings: MutableMap<AliasType, DataType>
    ): Boolean {
        if (paramType == argType) return true

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

    private fun isGenericSampler(type: DataType): Boolean {
        return type == AliasType.GSAMPLER2D ||
               type == AliasType.GSAMPLER2DARRAY ||
               type == AliasType.GSAMPLER3D
    }

    private fun resolveGvec4FromSampler(samplerType: DataType): DataType? {
        return when (samplerType) {
            SamplerType.SAMPLER2D, SamplerType.SAMPLER2DARRAY, SamplerType.SAMPLER3D,
            SamplerType.SAMPLERCUBE, SamplerType.SAMPLERCUBEARRAY, SamplerType.SAMPLEREXTERNALOES -> VectorType.VEC4
            SamplerType.ISAMPLER2D, SamplerType.ISAMPLER2DARRAY, SamplerType.ISAMPLER3D -> VectorType.IVEC4
            SamplerType.USAMPLER2D, SamplerType.USAMPLER2DARRAY, SamplerType.USAMPLER3D -> VectorType.UVEC4
            else -> null
        }
    }

    private fun isFloatVector(type: DataType): Boolean {
        return type is VectorType && type.elementType is FloatType
    }

    private fun isIntVector(type: DataType): Boolean {
        return type is VectorType && type.elementType is IntType
    }

    private fun isUIntVector(type: DataType): Boolean {
        return type is VectorType && type.elementType is UIntType
    }

    private fun isBoolVector(type: DataType): Boolean {
        return type is VectorType && type.elementType is BoolType
    }
}

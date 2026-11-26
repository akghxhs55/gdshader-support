package kr.jaehoyi.gdshader.model

import java.util.EnumMap

object Builtins {
    // Data types
    val VOID = VoidType
    val BOOL = BoolType
    val BVEC2 = VectorType.BVEC2
    val BVEC3 = VectorType.BVEC3
    val BVEC4 = VectorType.BVEC4
    val INT = IntType
    val IVEC2 = VectorType.IVEC2
    val IVEC3 = VectorType.IVEC3
    val IVEC4 = VectorType.IVEC4
    val UINT = UIntType
    val UVEC2 = VectorType.UVEC2
    val UVEC3 = VectorType.UVEC3
    val UVEC4 = VectorType.UVEC4
    val FLOAT = FloatType.DEFAULT
    val VEC2 = VectorType.VEC2
    val VEC3 = VectorType.VEC3
    val VEC4 = VectorType.VEC4
    val MAT2 = MatrixType.MAT2
    val MAT3 = MatrixType.MAT3
    val MAT4 = MatrixType.MAT4
    val SAMPLER2D = SamplerType.SAMPLER2D
    val ISAMPLER2D = SamplerType.ISAMPLER2D
    val USAMPLER2D = SamplerType.USAMPLER2D
    val SAMPLER2DARRAY = SamplerType.SAMPLER2DARRAY
    val ISAMPLER2DARRAY = SamplerType.ISAMPLER2DARRAY
    val USAMPLER2DARRAY = SamplerType.USAMPLER2DARRAY
    val SAMPLER3D = SamplerType.SAMPLER3D
    val ISAMPLER3D = SamplerType.ISAMPLER3D
    val USAMPLER3D = SamplerType.USAMPLER3D
    val SAMPLERCUBE = SamplerType.SAMPLERCUBE
    val SAMPLERCUBEARRAY = SamplerType.SAMPLERCUBEARRAY
    val SAMPLEREXTERNALOES = SamplerType.SAMPLEREXTERNALOES

    val VEC_TYPE = AliasType.VEC_TYPE
    val VEC_INT_TYPE = AliasType.VEC_INT_TYPE
    val VEC_UINT_TYPE = AliasType.VEC_UINT_TYPE
    val VEC_BOOL_TYPE = AliasType.VEC_BOOL_TYPE
    val MAT_TYPE = AliasType.MAT_TYPE
    val GVEC4_TYPE = AliasType.GVEC4_TYPE
    val GSAMPLER2D = AliasType.GSAMPLER2D
    val GSAMPLER2DARRAY = AliasType.GSAMPLER2DARRAY
    val GSAMPLER3D = AliasType.GSAMPLER3D

    val ALL_DATA_TYPE_LIST: List<DataType> by lazy {
        listOf(
            VOID,
            BOOL,
            BVEC2,
            BVEC3,
            BVEC4,
            INT,
            IVEC2,
            IVEC3,
            IVEC4,
            UINT,
            UVEC2,
            UVEC3,
            UVEC4,
            FLOAT,
            VEC2,
            VEC3,
            VEC4,
            MAT2,
            MAT3,
            MAT4,
            SAMPLER2D,
            ISAMPLER2D,
            USAMPLER2D,
            SAMPLER2DARRAY,
            ISAMPLER2DARRAY,
            USAMPLER2DARRAY,
            SAMPLER3D,
            ISAMPLER3D,
            USAMPLER3D,
            SAMPLERCUBE,
            SAMPLERCUBEARRAY,
            SAMPLEREXTERNALOES,
            VEC_TYPE,
            VEC_INT_TYPE,
            VEC_UINT_TYPE,
            VEC_BOOL_TYPE,
            MAT_TYPE,
            GVEC4_TYPE,
            GSAMPLER2D,
            GSAMPLER2DARRAY,
            GSAMPLER3D,
        )
    }

    val DATA_TYPES: Map<String, DataType> by lazy {
        ALL_DATA_TYPE_LIST.associateBy { it.name }
    }

    fun getType(name: String): DataType? = DATA_TYPES[name]

    // Functions
    val FUNCTIONS: Map<Pair<ShaderType, FunctionContext>, List<FunctionSpec>> by lazy {
        val globalFunctions = GeneratedBuiltins.globalFunctions
        val fragmentShaderOnlyFunctions = GeneratedBuiltins.fragmentOnlyFunctions
        mapOf(
            ShaderType.SPATIAL to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,
            ShaderType.CANVAS_ITEM to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,
            ShaderType.CANVAS_ITEM to FunctionContext.FRAGMENT to sdfFunctions,
            ShaderType.CANVAS_ITEM to FunctionContext.LIGHT to sdfFunctions,
            ShaderType.PARTICLES to FunctionContext.COMMON to globalFunctions + emitSubparticleFunction,
            ShaderType.SKY to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,
            ShaderType.FOG to FunctionContext.COMMON to globalFunctions,
        )
    }

    private val ENGINE_FUNCTIONS: Set<FunctionSpec> by lazy {
        (GeneratedBuiltins.globalFunctions + GeneratedBuiltins.fragmentOnlyFunctions).toHashSet()
    }

    private val STAGE_FUNCTIONS: Set<FunctionSpec> by lazy {
        (sdfFunctions + emitSubparticleFunction).toHashSet()
    }

    fun isEngineFunction(spec: FunctionSpec): Boolean = spec in ENGINE_FUNCTIONS

    fun isStageFunction(spec: FunctionSpec): Boolean = spec in STAGE_FUNCTIONS

    private val FUNCTION_LOOKUP_CACHE: Map<Pair<ShaderType, FunctionContext>, Map<String, List<FunctionSpec>>> by lazy {
        FUNCTIONS.mapValues { entry ->
            entry.value.groupBy { it.name }
        }
    }

    fun getFunctions(
        shaderType: ShaderType?,
        functionContext: FunctionContext,
        name: String,
    ): List<FunctionSpec>? {
        if (shaderType != null) {
            return FUNCTION_LOOKUP_CACHE[shaderType to functionContext]?.get(name)
                ?: FUNCTION_LOOKUP_CACHE[shaderType to FunctionContext.COMMON]?.get(name)
        }

        return ShaderType.entries
            .flatMap { getFunctions(it, functionContext, name).orEmpty() }
            .distinctBy { it.signatureKey() }
            .ifEmpty { null }
    }

    fun getVariables(
        shaderType: ShaderType?,
        functionContext: FunctionContext,
        name: String,
    ): List<VariableSpec> {
        if (shaderType != null) {
            return listOfNotNull(getVariable(shaderType, functionContext, name))
        }

        return ShaderType.entries
            .mapNotNull { getVariable(it, functionContext, name) }
            .distinct()
    }

    fun getAllShaderTypeFunctions(functionContext: FunctionContext): List<FunctionSpec> =
        ShaderType.entries
            .flatMap { shaderType ->
                FUNCTIONS[shaderType to FunctionContext.COMMON].orEmpty() + FUNCTIONS[shaderType to functionContext].orEmpty()
            }.distinctBy { it.signatureKey() }

    fun getAllShaderTypeVariables(functionContext: FunctionContext): List<VariableSpec> =
        ShaderType.entries
            .flatMap { shaderType ->
                VARIABLES[shaderType to FunctionContext.COMMON].orEmpty() + VARIABLES[shaderType to functionContext].orEmpty()
            }.distinctBy { it.name to it.presentationTypeText }

    private fun FunctionSpec.signatureKey() = Triple(name, returnType, parameters.map { Triple(it.name, it.type, it.qualifier) })

    // Variables
    val VARIABLES: Map<Pair<ShaderType, FunctionContext>, List<VariableSpec>> by lazy {
        GeneratedBuiltins.variables
    }

    private val VARIABLE_LOOKUP_CACHE: Map<Pair<ShaderType, FunctionContext>, Map<String, VariableSpec>> by lazy {
        VARIABLES.mapValues { (_, list) ->
            list.associateBy { it.name }
        }
    }

    fun getVariable(
        shaderType: ShaderType,
        functionContext: FunctionContext,
        name: String,
    ): VariableSpec? =
        VARIABLE_LOOKUP_CACHE[shaderType to functionContext]?.get(name)
            ?: VARIABLE_LOOKUP_CACHE[shaderType to FunctionContext.COMMON]?.get(name)

    fun getProcessingFunctions(shaderType: ShaderType?): List<FunctionContext> =
        if (shaderType != null) {
            PROCESSING_FUNCTIONS[shaderType].orEmpty()
        } else {
            PROCESSING_FUNCTIONS.values.flatten().distinct()
        }

    // Processing functions
    val PROCESSING_FUNCTIONS: EnumMap<ShaderType, List<FunctionContext>> =
        EnumMap(
            mapOf(
                ShaderType.SPATIAL to
                    listOf(
                        FunctionContext.VERTEX,
                        FunctionContext.FRAGMENT,
                        FunctionContext.LIGHT,
                    ),
                ShaderType.CANVAS_ITEM to
                    listOf(
                        FunctionContext.VERTEX,
                        FunctionContext.FRAGMENT,
                        FunctionContext.LIGHT,
                    ),
                ShaderType.PARTICLES to
                    listOf(
                        FunctionContext.START,
                        FunctionContext.PROCESS,
                    ),
                ShaderType.SKY to
                    listOf(
                        FunctionContext.SKY,
                    ),
                ShaderType.FOG to
                    listOf(
                        FunctionContext.FOG,
                    ),
            ),
        )

    private val emitSubparticleFunction =
        FunctionSpec(
            name = "emit_subparticle",
            returnType = BOOL,
            parameters =
                listOf(
                    ParameterSpec("xform", MAT4),
                    ParameterSpec("velocity", VEC3),
                    ParameterSpec("color", VEC4),
                    ParameterSpec("custom", VEC4),
                    ParameterSpec("flags", UINT),
                ),
            description = "Emits a particle from a sub-emitter.",
        )

    private val sdfFunctions =
        listOf(
            FunctionSpec(
                name = "texture_sdf",
                returnType = FLOAT,
                parameters =
                    listOf(
                        ParameterSpec("sdf_pos", VEC2),
                    ),
                description = "Performs an SDF texture lookup.",
            ),
            FunctionSpec(
                name = "texture_sdf_normal",
                returnType = VEC2,
                parameters =
                    listOf(
                        ParameterSpec("sdf_pos", VEC2),
                    ),
                description = "Calculates a normal from the SDF texture.",
            ),
            FunctionSpec(
                name = "sdf_to_screen_uv",
                returnType = VEC2,
                parameters =
                    listOf(
                        ParameterSpec("sdf_pos", VEC2),
                    ),
                description = "Converts an SDF to screen UV.",
            ),
            FunctionSpec(
                name = "screen_uv_to_sdf",
                returnType = VEC2,
                parameters =
                    listOf(
                        ParameterSpec("uv", VEC2),
                    ),
                description = "Converts screen UV to an SDF.",
            ),
        )
}

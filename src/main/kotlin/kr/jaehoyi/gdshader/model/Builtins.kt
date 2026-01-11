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
            BOOL, BVEC2, BVEC3, BVEC4,
            INT, IVEC2, IVEC3, IVEC4,
            UINT, UVEC2, UVEC3, UVEC4,
            FLOAT, VEC2, VEC3, VEC4,
            MAT2, MAT3, MAT4,
            SAMPLER2D, ISAMPLER2D, USAMPLER2D,
            SAMPLER2DARRAY, ISAMPLER2DARRAY, USAMPLER2DARRAY,
            SAMPLER3D, ISAMPLER3D, USAMPLER3D,
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
    val FUNCTIONS: Map<Pair<ShaderType, FunctionContext>, List<FunctionSpec>> by lazy { mapOf(
        ShaderType.SPATIAL to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,

        ShaderType.CANVAS_ITEM to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,

        ShaderType.CANVAS_ITEM to FunctionContext.FRAGMENT to sdfFunctions,
        
        ShaderType.CANVAS_ITEM to FunctionContext.LIGHT to sdfFunctions,

        ShaderType.PARTICLES to FunctionContext.COMMON to globalFunctions + listOf(FunctionSpec(
                name = "emit_subparticle",
                returnType = BOOL,
                parameters = listOf(
                    ParameterSpec("xform", MAT4),
                    ParameterSpec("velocity", VEC3),
                    ParameterSpec("color", VEC4),
                    ParameterSpec("custom", VEC4),
                    ParameterSpec("flags", UINT),
                ),
            )),

        ShaderType.SKY to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,

        ShaderType.FOG to FunctionContext.COMMON to globalFunctions,
    ) }
    
    private val FUNCTION_LOOKUP_CACHE: Map<Pair<ShaderType, FunctionContext>, Map<String, List<FunctionSpec>>> by lazy {
        FUNCTIONS.mapValues { entry ->
            entry.value.groupBy { it.name }
        }
    }
    
    fun getFunctions(shaderType: ShaderType, functionContext: FunctionContext, name: String): List<FunctionSpec>? = 
        FUNCTION_LOOKUP_CACHE[shaderType to functionContext]?.get(name) 
            ?: FUNCTION_LOOKUP_CACHE[shaderType to FunctionContext.COMMON]?.get(name)

    // Variables
    val VARIABLES: Map<Pair<ShaderType, FunctionContext>, List<VariableSpec>> by lazy { mapOf(
        ShaderType.SPATIAL to FunctionContext.COMMON to globalVariables + listOf(
            ParameterSpec(
                name = "OUTPUT_IS_SRGB",
                type = BOOL,
            ),

            ParameterSpec(
                name = "CLIP_SPACE_FAR",
                type = FLOAT,
            ),
        ),
        
        ShaderType.SPATIAL to FunctionContext.VERTEX to listOf(
            ParameterSpec(
                name = "VIEWPORT_SIZE",
                type = VEC2,
            ),

            ParameterSpec(
                name = "VIEW_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "INV_VIEW_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "MAIN_CAM_INV_VIEW_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "INV_PROJECTION_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "NODE_POSITION_WORLD",
                type = VEC3,
            ),

            ParameterSpec(
                name = "NODE_POSITION_VIEW",
                type = VEC3,
            ),

            ParameterSpec(
                name = "CAMERA_POSITION_WORLD",
                type = VEC3,
            ),

            ParameterSpec(
                name = "CAMERA_DIRECTION_WORLD",
                type = VEC3,
            ),

            ParameterSpec(
                name = "CAMERA_VISIBLE_LAYERS",
                type = UINT,
            ),

            ParameterSpec(
                name = "INSTANCE_ID",
                type = INT,
            ),

            ParameterSpec(
                name = "INSTANCE_CUSTOM",
                type = VEC4,
            ),

            ParameterSpec(
                name = "VIEW_INDEX",
                type = INT,
            ),

            ParameterSpec(
                name = "VIEW_MONO_LEFT",
                type = INT,
            ),

            ParameterSpec(
                name = "VIEW_RIGHT",
                type = INT,
            ),

            ParameterSpec(
                name = "EYE_OFFSET",
                type = VEC3,
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "VERTEX_ID",
                type = INT,
            ),

            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "TANGENT",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "BINORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "POSITION",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "UV2",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "ROUGHNESS",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "POINT_SIZE",
                type = FLOAT,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "MODELVIEW_MATRIX",
                type = MAT4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "MODELVIEW_NORMAL_MATRIX",
                type = MAT3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
                
            ),

            ParameterSpec(
                name = "MODEL_NORMAL_MATRIX",
                type = MAT3,
                
            ),

            ParameterSpec(
                name = "PROJECTION_MATRIX",
                type = MAT4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "BONE_INDICES",
                type = UVEC4,
            ),

            ParameterSpec(
                name = "BONE_WEIGHTS",
                type = VEC4,
            ),

            ParameterSpec(
                name = "CUSTOM0",
                type = VEC4,
            ),

            ParameterSpec(
                name = "CUSTOM1",
                type = VEC4,
            ),

            ParameterSpec(
                name = "CUSTOM2",
                type = VEC4,
            ),

            ParameterSpec(
                name = "CUSTOM3",
                type = VEC4,
            ),
        ),

        ShaderType.SPATIAL to FunctionContext.FRAGMENT to listOf(
            ParameterSpec(
                name = "VIEWPORT_SIZE",
                type = VEC2,
            ),

            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
            ),

            ParameterSpec(
                name = "FRONT_FACING",
                type = BOOL,
            ),

            ParameterSpec(
                name = "VIEW",
                type = VEC3,
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "UV2",
                type = VEC2,
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
            ),

            ParameterSpec(
                name = "POINT_COORD",
                type = VEC2,
            ),

            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "MODEL_NORMAL_MATRIX",
                type = MAT3,
            ),

            ParameterSpec(
                name = "VIEW_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "INV_VIEW_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "PROJECTION_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "INV_PROJECTION_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "NODE_POSITION_WORLD",
                type = VEC3,
            ),

            ParameterSpec(
                name = "NODE_POSITION_VIEW",
                type = VEC3,
            ),

            ParameterSpec(
                name = "CAMERA_POSITION_WORLD",
                type = VEC3,
            ),

            ParameterSpec(
                name = "CAMERA_DIRECTION_WORLD",
                type = VEC3,
            ),

            ParameterSpec(
                name = "CAMERA_VISIBLE_LAYERS",
                type = UINT,
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHT_VERTEX",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "VIEW_INDEX",
                type = INT,
            ),

            ParameterSpec(
                name = "VIEW_MONO_LEFT",
                type = INT,
            ),

            ParameterSpec(
                name = "VIEW_RIGHT",
                type = INT,
                
            ),

            ParameterSpec(
                name = "EYE_OFFSET",
                type = VEC3,
            ),

            ParameterSpec(
                name = "SCREEN_TEXTURE",
                type = SAMPLER2D,
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "DEPTH_TEXTURE",
                type = SAMPLER2D,
            ),

            ParameterSpec(
                name = "DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "TANGENT",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "BINORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "NORMAL_MAP",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "NORMAL_MAP_DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ALBEDO",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ALPHA",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ALPHA_SCISSOR_THRESHOLD",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ALPHA_TEXTURE_COORDINATE",
                type = VEC2,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "PREMUL_ALPHA_FACTOR",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "METALLIC",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "SPECULAR",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ROUGHNESS",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "RIM",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "RIM_TINT",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "CLEARCOAT",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "CLEARCOAT_GLOSS",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ANISOTROPY",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ANISOTROPY_FLOW",
                type = VEC2,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "SSS_STRENGTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "SSS_TRANSMITTANCE_COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "SSS_TRANSMITTANCE_DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "SSS_TRANSMISSION_BOOST",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "BACKLIGHT",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "AO",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "AO_LIGHT_AFFECT",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "EMISSION",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "FOG",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "RADIANCE",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "IRRADIANCE",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
            ),
        ),

        ShaderType.SPATIAL to FunctionContext.LIGHT to listOf(
            ParameterSpec(
                name = "VIEWPORT_SIZE",
                type = VEC2,
            ),

            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
            ),

            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "INV_VIEW_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "VIEW_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "PROJECTION_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "INV_PROJECTION_MATRIX",
                type = MAT4,
            ),
            
            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "UV2",
                type = VEC2,
            ),

            ParameterSpec(
                name = "VIEW",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHT",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHT_COLOR",
                type = VEC3,
            ),

            ParameterSpec(
                name = "SPECULAR_AMOUNT",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "LIGHT_IS_DIRECTIONAL",
                type = BOOL,
            ),

            ParameterSpec(
                name = "ATTENUATION",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "ALBEDO",
                type = VEC3,
            ),

            ParameterSpec(
                name = "BACKLIGHT",
                type = VEC3,
            ),

            ParameterSpec(
                name = "METALLIC",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "ROUGHNESS",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "DIFFUSE_LIGHT",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "SPECULAR_LIGHT",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ALPHA",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),
        ),
        
        ShaderType.CANVAS_ITEM to FunctionContext.COMMON to globalVariables,
        
        ShaderType.CANVAS_ITEM to FunctionContext.VERTEX to listOf(
            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "CANVAS_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "SCREEN_MATRIX",
                type = MAT4,
            ),

            ParameterSpec(
                name = "INSTANCE_ID",
                type = INT,
            ),

            ParameterSpec(
                name = "INSTANCE_CUSTOM",
                type = VEC4,
            ),

            ParameterSpec(
                name = "AT_LIGHT_PASS",
                type = BOOL,
            ),

            ParameterSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = VEC2,
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "VERTEX_ID",
                type = INT,
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "POINT_SIZE",
                type = FLOAT,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "CUSTOM0",
                type = VEC4,
            ),

            ParameterSpec(
                name = "CUSTOM1",
                type = VEC4,
            ),
        ),

        ShaderType.CANVAS_ITEM to FunctionContext.FRAGMENT to listOf(
            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
            ),

            ParameterSpec(
                name = "SCREEN_PIXEL_SIZE",
                type = VEC2,
            ),

            ParameterSpec(
                name = "REGION_RECT",
                type = VEC4,
            ),

            ParameterSpec(
                name = "POINT_COORD",
                type = VEC2,
            ),

            ParameterSpec(
                name = "TEXTURE",
                type = SAMPLER2D,
            ),

            ParameterSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = VEC2,
            ),

            ParameterSpec(
                name = "AT_LIGHT_PASS",
                type = BOOL,
            ),

            ParameterSpec(
                name = "SPECULAR_SHININESS_TEXTURE",
                type = SAMPLER2D,
            ),

            ParameterSpec(
                name = "SPECULAR_SHININESS",
                type = VEC4,
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "SCREEN_TEXTURE",
                type = SAMPLER2D,
            ),

            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "NORMAL_TEXTURE",
                type = SAMPLER2D,
            ),

            ParameterSpec(
                name = "NORMAL_MAP",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "NORMAL_MAP_DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "SHADOW_VERTEX",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "LIGHT_VERTEX",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),
        ),

        ShaderType.CANVAS_ITEM to FunctionContext.LIGHT to listOf(
            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
            ),
            
            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
            ),
            
            ParameterSpec(
                name = "COLOR",
                type = VEC4,
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "TEXTURE",
                type = SAMPLER2D,
            ),

            ParameterSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = VEC2,
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "POINT_COORD",
                type = VEC2,
            ),

            ParameterSpec(
                name = "LIGHT_COLOR",
                type = VEC4,
            ),

            ParameterSpec(
                name = "LIGHT_ENERGY",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "LIGHT_POSITION",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHT_DIRECTION",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHT_IS_DIRECTIONAL",
                type = BOOL,
            ),

            ParameterSpec(
                name = "LIGHT_VERTEX",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHT",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "SPECULAR_SHININESS",
                type = VEC4,
            ),

            ParameterSpec(
                name = "SHADOW_MODULATE",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
            ),
        ),

        ShaderType.PARTICLES to FunctionContext.COMMON to globalVariables,
        
        ShaderType.PARTICLES to FunctionContext.START to listOf(
            ParameterSpec(
                name = "LIFETIME",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "DELTA",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "NUMBER",
                type = UINT,
            ),

            ParameterSpec(
                name = "INDEX",
                type = UINT,
            ),

            ParameterSpec(
                name = "EMISSION_TRANSFORM",
                type = MAT4,
            ),

            ParameterSpec(
                name = "RANDOM_SEED",
                type = UINT,
            ),

            ParameterSpec(
                name = "ACTIVE",
                type = BOOL,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "VELOCITY",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "TRANSFORM",
                type = MAT4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "CUSTOM",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "MASS",
                type = FLOAT,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "USERDATAX",
                type = VEC4,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_POSITION",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_ROT_SCALE",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_VELOCITY",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_COLOR",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_CUSTOM",
                type = UINT,
            ),

            ParameterSpec(
                name = "EMITTER_VELOCITY",
                type = VEC3,
            ),

            ParameterSpec(
                name = "INTERPOLATE_TO_END",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "AMOUNT_RATIO",
                type = UINT,
            ),

            ParameterSpec(
                name = "RESTART_POSITION",
                type = BOOL,
            ),

            ParameterSpec(
                name = "RESTART_ROT_SCALE",
                type = BOOL,
            ),

            ParameterSpec(
                name = "RESTART_VELOCITY",
                type = BOOL,
            ),

            ParameterSpec(
                name = "RESTART_COLOR",
                type = BOOL,
            ),

            ParameterSpec(
                name = "RESTART_CUSTOM",
                type = BOOL,
            ),
        ),

        ShaderType.PARTICLES to FunctionContext.PROCESS to listOf(
            ParameterSpec(
                name = "LIFETIME",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "DELTA",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "NUMBER",
                type = UINT,
            ),

            ParameterSpec(
                name = "INDEX",
                type = UINT,
            ),

            ParameterSpec(
                name = "EMISSION_TRANSFORM",
                type = MAT4,
            ),

            ParameterSpec(
                name = "RANDOM_SEED",
                type = UINT,
            ),

            ParameterSpec(
                name = "ACTIVE",
                type = BOOL,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "VELOCITY",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "TRANSFORM",
                type = MAT4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "CUSTOM",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "MASS",
                type = FLOAT,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "USERDATAX",
                type = VEC4,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_POSITION",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_ROT_SCALE",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_VELOCITY",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_COLOR",
                type = UINT,
            ),

            ParameterSpec(
                name = "FLAG_EMIT_CUSTOM",
                type = UINT,
            ),

            ParameterSpec(
                name = "EMITTER_VELOCITY",
                type = VEC3,
            ),

            ParameterSpec(
                name = "INTERPOLATE_TO_END",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "AMOUNT_RATIO",
                type = UINT,
            ),

            ParameterSpec(
                name = "RESTART",
                type = BOOL,
            ),

            ParameterSpec(
                name = "COLLIDED",
                type = BOOL,
            ),

            ParameterSpec(
                name = "COLLISION_NORMAL",
                type = BOOL,
            ),

            ParameterSpec(
                name = "COLLISION_DEPTH",
                type = BOOL,
            ),

            ParameterSpec(
                name = "ATTRACTOR_FORCE",
                type = VEC3,
            ),
        ),

        ShaderType.SKY to FunctionContext.COMMON to globalVariables + listOf(
            ParameterSpec(
                name = "POSITION",
                type = VEC3,
            ),

            ParameterSpec(
                name = "RADIANCE",
                type = SAMPLERCUBE,
            ),

            ParameterSpec(
                name = "AT_HALF_RES_PASS",
                type = BOOL,
            ),

            ParameterSpec(
                name = "AT_QUARTER_RES_PASS",
                type = BOOL,
            ),

            ParameterSpec(
                name = "AT_CUBEMAP_PASS",
                type = BOOL,
            ),

            ParameterSpec(
                name = "LIGHTX_ENABLED",
                type = BOOL,
            ),

            ParameterSpec(
                name = "LIGHTX_ENERGY",
                type = FLOAT,
            ),

            ParameterSpec(
                name = "LIGHTX_DIRECTION",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHTX_COLOR",
                type = VEC3,
            ),

            ParameterSpec(
                name = "LIGHTX_SIZE",
                type = FLOAT,
            ),
        ),
        
        ShaderType.SKY to FunctionContext.SKY to listOf(
            ParameterSpec(
                name = "EYEDIR",
                type = VEC3,
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
            ),

            ParameterSpec(
                name = "SKY_COORDS",
                type = VEC2,
            ),

            ParameterSpec(
                name = "HALF_RES_COLOR",
                type = VEC4,
            ),

            ParameterSpec(
                name = "QUARTER_RES_COLOR",
                type = VEC4,
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "ALPHA",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "FOG",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
            ),
        ),

        ShaderType.FOG to FunctionContext.COMMON to globalVariables,
        
        ShaderType.FOG to FunctionContext.FOG to listOf(
            ParameterSpec(
                name = "WORLD_POSITION",
                type = VEC3,
            ),

            ParameterSpec(
                name = "OBJECT_POSITION",
                type = VEC3,
            ),

            ParameterSpec(
                name = "UVW",
                type = VEC3,
            ),

            ParameterSpec(
                name = "SIZE",
                type = VEC3,
            ),

            ParameterSpec(
                name = "SDF",
                type = VEC3,
            ),

            ParameterSpec(
                name = "ALBEDO",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "DENSITY",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
            ),

            ParameterSpec(
                name = "EMISSION",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
            ),
        ),
    ) }
    
    private val VARIABLE_LOOKUP_CACHE: Map<Pair<ShaderType, FunctionContext>, Map<String, VariableSpec>> by lazy {
        VARIABLES.mapValues { (_, list) ->
            list.associateBy { it.name }
        }
    }
    
    fun getVariable(shaderType: ShaderType, functionContext: FunctionContext, name: String): VariableSpec? =
        VARIABLE_LOOKUP_CACHE[shaderType to functionContext]?.get(name)
            ?: VARIABLE_LOOKUP_CACHE[shaderType to FunctionContext.COMMON]?.get(name)
    
    // Processing functions
    val PROCESSING_FUNCTIONS: EnumMap<ShaderType, List<FunctionContext>> = EnumMap(mapOf(
        ShaderType.SPATIAL to listOf(
            FunctionContext.VERTEX,
            FunctionContext.FRAGMENT,
            FunctionContext.LIGHT,
        ),
        ShaderType.CANVAS_ITEM to listOf(
            FunctionContext.VERTEX,
            FunctionContext.FRAGMENT,
            FunctionContext.LIGHT,
        ),
        ShaderType.PARTICLES to listOf(
            FunctionContext.START,
            FunctionContext.PROCESS,
        ),
        ShaderType.SKY to listOf(
            FunctionContext.SKY,
        ),
        ShaderType.FOG to listOf(
            FunctionContext.FOG,
        ),
    ))

    private val globalFunctions = listOf(
        FunctionSpec(
            name = "radians",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("degrees", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "degrees",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("radians", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "sin",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "cos",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "tan",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "asin",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "acos",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "atan",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("y_over_x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "atan",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("y", VEC_TYPE),
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "sinh",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "cosh",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "tanh",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "asinh",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "acosh",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "atanh",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "pow",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "exp",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "exp2",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "log",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "log2",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "sqrt",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "inversesqrt",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "abs",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "abs",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "sign",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "sign",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "floor",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "round",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "roundEven",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "trunc",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "ceil",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "fract",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "mod",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "mod",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "modf",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("i", VEC_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),

        FunctionSpec(
            name = "min",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "min",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "min",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_INT_TYPE),
                ParameterSpec("b", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "min",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_INT_TYPE),
                ParameterSpec("b", INT)
            ),
        ),

        FunctionSpec(
            name = "min",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_UINT_TYPE),
                ParameterSpec("b", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "min",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_UINT_TYPE),
                ParameterSpec("b", UINT)
            ),
        ),

        FunctionSpec(
            name = "max",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "max",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "max",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_INT_TYPE),
                ParameterSpec("b", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "max",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_INT_TYPE),
                ParameterSpec("b", INT)
            ),
        ),

        FunctionSpec(
            name = "max",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_UINT_TYPE),
                ParameterSpec("b", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "max",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_UINT_TYPE),
                ParameterSpec("b", UINT)
            ),
        ),

        FunctionSpec(
            name = "clamp",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("min", VEC_TYPE),
                ParameterSpec("max", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "clamp",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("min", FLOAT),
                ParameterSpec("max", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "clamp",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_INT_TYPE),
                ParameterSpec("min", VEC_INT_TYPE),
                ParameterSpec("max", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "clamp",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_INT_TYPE),
                ParameterSpec("min", INT),
                ParameterSpec("max", INT)
            ),
        ),

        FunctionSpec(
            name = "clamp",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_UINT_TYPE),
                ParameterSpec("min", VEC_UINT_TYPE),
                ParameterSpec("max", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "clamp",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_UINT_TYPE),
                ParameterSpec("min", UINT),
                ParameterSpec("max", UINT)
            ),
        ),

        FunctionSpec(
            name = "mix",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE),
                ParameterSpec("c", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "mix",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE),
                ParameterSpec("c", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "mix",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE),
                ParameterSpec("c", VEC_BOOL_TYPE)
            ),
        ),

        FunctionSpec(
            name = "fma",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE),
                ParameterSpec("c", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "step",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "step",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", FLOAT),
                ParameterSpec("b", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "smoothstep",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE),
                ParameterSpec("c", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "smoothstep",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", FLOAT),
                ParameterSpec("b", FLOAT),
                ParameterSpec("c", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "isnan",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "isinf",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "floatBitsToInt",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "floatBitsToUint",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "intBitsToFloat",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "uintBitsToFloat",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "length",
            returnType = FLOAT,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "distance",
            returnType = FLOAT,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "dot",
            returnType = FLOAT,
            parameters = listOf(
                ParameterSpec("a", VEC_TYPE),
                ParameterSpec("b", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "cross",
            returnType = VEC3,
            parameters = listOf(
                ParameterSpec("a", VEC3),
                ParameterSpec("b", VEC3)
            ),
        ),

        FunctionSpec(
            name = "normalize",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "reflect",
            returnType = VEC3,
            parameters = listOf(
                ParameterSpec("I", VEC3),
                ParameterSpec("N", VEC3)
            ),
        ),

        FunctionSpec(
            name = "refract",
            returnType = VEC3,
            parameters = listOf(
                ParameterSpec("I", VEC3),
                ParameterSpec("N", VEC3),
                ParameterSpec("eta", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "faceforward",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("N", VEC_TYPE),
                ParameterSpec("I", VEC_TYPE),
                ParameterSpec("Nref", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "matrixCompMult",
            returnType = MAT_TYPE,
            parameters = listOf(
                ParameterSpec("x", MAT_TYPE),
                ParameterSpec("y", MAT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "outerProduct",
            returnType = MAT_TYPE,
            parameters = listOf(
                ParameterSpec("column", VEC_TYPE),
                ParameterSpec("row", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "transpose",
            returnType = MAT_TYPE,
            parameters = listOf(
                ParameterSpec("m", MAT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "determinant",
            returnType = FLOAT,
            parameters = listOf(
                ParameterSpec("m", MAT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "inverse",
            returnType = MAT_TYPE,
            parameters = listOf(
                ParameterSpec("m", MAT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "lessThan",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE),
            ),
        ),

        FunctionSpec(
            name = "greaterThan",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE),
            ),
        ),

        FunctionSpec(
            name = "lessThanEqual",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE),
            ),
        ),

        FunctionSpec(
            name = "greaterThanEqual",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE),
            ),
        ),

        FunctionSpec(
            name = "equal",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE),
            ),
        ),

        FunctionSpec(
            name = "notEqual",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec("y", VEC_TYPE),
            ),
        ),

        FunctionSpec(
            name = "any",
            returnType = BOOL,
            parameters = listOf(
                ParameterSpec("x", VEC_BOOL_TYPE)
            ),
        ),

        FunctionSpec(
            name = "all",
            returnType = BOOL,
            parameters = listOf(
                ParameterSpec("x", VEC_BOOL_TYPE)
            ),
        ),

        FunctionSpec(
            name = "not",
            returnType = VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_BOOL_TYPE)
            ),
        ),

        FunctionSpec(
            name = "textureSize",
            returnType = IVEC2,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "textureSize",
            returnType = IVEC2,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "textureSize",
            returnType = IVEC2,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBEARRAY),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "textureSize",
            returnType = IVEC3,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "textureSize",
            returnType = IVEC3,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "textureQueryLevels",
            returnType = INT,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D)
            ),
        ),

        FunctionSpec(
            name = "textureQueryLevels",
            returnType = INT,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY)
            ),
        ),

        FunctionSpec(
            name = "textureQueryLevels",
            returnType = INT,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D)
            ),
        ),

        FunctionSpec(
            name = "textureQueryLevels",
            returnType = INT,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE)
            ),
        ),

        FunctionSpec(
            name = "texture",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC2),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "texture",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("p", VEC3),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "texture",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC3),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "texture",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE),
                ParameterSpec("p", VEC3),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "texture",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBEARRAY),
                ParameterSpec("p", VEC4),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "texture",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLEREXTERNALOES),
                ParameterSpec("p", VEC2),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "textureProj",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC3),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "textureProj",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC4),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "textureProj",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC4),
                ParameterSpec("bias", FLOAT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "textureLod",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC2),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureLod",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("p", VEC3),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureLod",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC3),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureLod",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE),
                ParameterSpec("p", VEC3),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureLod",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBEARRAY),
                ParameterSpec("p", VEC4),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureProjLod",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC3),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureProjLod",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC4),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureProjLod",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC4),
                ParameterSpec("lod", FLOAT)
            ),
        ),

        FunctionSpec(
            name = "textureGrad",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC2),
                ParameterSpec("dPdx", VEC2),
                ParameterSpec("dPdy", VEC2),
            ),
        ),

        FunctionSpec(
            name = "textureGrad",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("p", VEC3),
                ParameterSpec("dPdx", VEC2),
                ParameterSpec("dPdy", VEC2),
            ),
        ),

        FunctionSpec(
            name = "textureGrad",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC3),
                ParameterSpec("dPdx", VEC2),
                ParameterSpec("dPdy", VEC2),
            ),
        ),

        FunctionSpec(
            name = "textureGrad",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE),
                ParameterSpec("p", VEC3),
                ParameterSpec("dPdx", VEC3),
                ParameterSpec("dPdy", VEC3),
            ),
        ),

        FunctionSpec(
            name = "textureGrad",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBEARRAY),
                ParameterSpec("p", VEC3),
                ParameterSpec("dPdx", VEC3),
                ParameterSpec("dPdy", VEC3),
            ),
        ),

        FunctionSpec(
            name = "textureProjGrad",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC3),
                ParameterSpec("dPdx", VEC2),
                ParameterSpec("dPdy", VEC2),
            ),
        ),

        FunctionSpec(
            name = "textureProjGrad",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC4),
                ParameterSpec("dPdx", VEC2),
                ParameterSpec("dPdy", VEC2),
            ),
        ),

        FunctionSpec(
            name = "textureProjGrad",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC4),
                ParameterSpec("dPdx", VEC3),
                ParameterSpec("dPdy", VEC3),
            ),
        ),

        FunctionSpec(
            name = "texelFetch",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", IVEC2),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "texelFetch",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("p", IVEC3),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "texelFetch",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", IVEC3),
                ParameterSpec("lod", INT)
            ),
        ),

        FunctionSpec(
            name = "textureGather",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC2),
                ParameterSpec("comps", INT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "textureGather",
            returnType = GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("p", VEC3),
                ParameterSpec("comps", INT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "textureGather",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE),
                ParameterSpec("p", VEC3),
                ParameterSpec("comps", INT, isOptional = true)
            ),
        ),

        FunctionSpec(
            name = "fwidth",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "packHalf2x16",
            returnType = UINT,
            parameters = listOf(
                ParameterSpec("v", VEC2)
            ),
        ),

        FunctionSpec(
            name = "unpackHalf2x16",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("v", UINT)
            ),
        ),

        FunctionSpec(
            name = "packUnorm2x16",
            returnType = UINT,
            parameters = listOf(
                ParameterSpec("v", VEC2)
            ),
        ),

        FunctionSpec(
            name = "unpackUnorm2x16",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("v", UINT)
            ),
        ),

        FunctionSpec(
            name = "packSnorm2x16",
            returnType = UINT,
            parameters = listOf(
                ParameterSpec("v", VEC2)
            ),
        ),

        FunctionSpec(
            name = "unpackSnorm2x16",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("v", UINT)
            ),
        ),

        FunctionSpec(
            name = "packUnorm4x8",
            returnType = UINT,
            parameters = listOf(
                ParameterSpec("v", VEC4)
            ),
        ),

        FunctionSpec(
            name = "unpackUnorm4x8",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("v", UINT)
            ),
        ),

        FunctionSpec(
            name = "packSnorm4x8",
            returnType = UINT,
            parameters = listOf(
                ParameterSpec("v", VEC4)
            ),
        ),

        FunctionSpec(
            name = "unpackSnorm4x8",
            returnType = VEC4,
            parameters = listOf(
                ParameterSpec("v", UINT)
            ),
        ),

        FunctionSpec(
            name = "bitfieldExtract",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_INT_TYPE),
                ParameterSpec("offset", INT),
                ParameterSpec("bits", INT)
            ),
        ),

        FunctionSpec(
            name = "bitfieldExtract",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_UINT_TYPE),
                ParameterSpec("offset", INT),
                ParameterSpec("bits", INT)
            ),
        ),

        FunctionSpec(
            name = "bitfieldInsert",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("base", VEC_INT_TYPE),
                ParameterSpec("insert", VEC_INT_TYPE),
                ParameterSpec("offset", INT),
                ParameterSpec("bits", INT)
            ),
        ),

        FunctionSpec(
            name = "bitfieldInsert",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("base", VEC_UINT_TYPE),
                ParameterSpec("insert", VEC_UINT_TYPE),
                ParameterSpec("offset", INT),
                ParameterSpec("bits", INT)
            ),
        ),

        FunctionSpec(
            name = "bitfieldReverse",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "bitfieldReverse",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "bitCount",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "bitCount",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "findLSB",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "findLSB",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "findMSB",
            returnType = VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_INT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "findMSB",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", VEC_UINT_TYPE)
            ),
        ),

        FunctionSpec(
            name = "imulExtended",
            returnType = VOID,
            parameters = listOf(
                ParameterSpec("x", VEC_INT_TYPE),
                ParameterSpec("y", VEC_INT_TYPE),
                ParameterSpec(
                    "msb",
                    VEC_INT_TYPE,
                    qualifier = ParameterQualifier.OUT
                ),
                ParameterSpec(
                    "lsb",
                    VEC_INT_TYPE,
                    qualifier = ParameterQualifier.OUT
                )
            ),
        ),

        FunctionSpec(
            name = "umulExtended",
            returnType = VOID,
            parameters = listOf(
                ParameterSpec("x", VEC_UINT_TYPE),
                ParameterSpec("y", VEC_UINT_TYPE),
                ParameterSpec(
                    "msb",
                    VEC_UINT_TYPE,
                    qualifier = ParameterQualifier.OUT
                ),
                ParameterSpec(
                    "lsb",
                    VEC_UINT_TYPE,
                    qualifier = ParameterQualifier.OUT
                )
            ),
        ),

        FunctionSpec(
            name = "uaddCarry",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_UINT_TYPE),
                ParameterSpec("y", VEC_UINT_TYPE),
                ParameterSpec(
                    "carry",
                    VEC_UINT_TYPE,
                    qualifier = ParameterQualifier.OUT
                )
            ),
        ),

        FunctionSpec(
            name = "usubBorrow",
            returnType = VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_UINT_TYPE),
                ParameterSpec("y", VEC_UINT_TYPE),
                ParameterSpec(
                    "borrow",
                    VEC_UINT_TYPE,
                    qualifier = ParameterQualifier.OUT
                )
            ),
        ),

        FunctionSpec(
            name = "ldexp",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec(
                    "exp",
                    VEC_INT_TYPE,
                    qualifier = ParameterQualifier.OUT
                )
            ),
        ),

        FunctionSpec(
            name = "frexp",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", VEC_TYPE),
                ParameterSpec(
                    "exp",
                    VEC_INT_TYPE,
                    qualifier = ParameterQualifier.OUT
                )
            ),
        ),
    )

    private val fragmentShaderOnlyFunctions = listOf(
        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC2)
            ),
        ),

        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC3,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("p", VEC2)
            ),
        ),

        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC2)
            ),
        ),

        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE),
                ParameterSpec("p", VEC2)
            ),
        ),

        FunctionSpec(
            name = "dFdx",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "dFdxCoarse",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "dFdxFine",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "dFdy",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "dFdyCoarse",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "dFdyFine",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "fwidthCoarse",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),

        FunctionSpec(
            name = "fwidthFine",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
        ),
    )

    private val sdfFunctions = listOf(
        FunctionSpec(
            name = "texture_sdf",
            returnType = FLOAT,
            parameters = listOf(
                ParameterSpec("sdf_pos", VEC2)
            ),
        ),

        FunctionSpec(
            name = "texture_sdf_normal",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("sdf_pos", VEC2)
            ),
        ),

        FunctionSpec(
            name = "sdf_to_screen_uv",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("sdf_pos", VEC2)
            ),
        ),

        FunctionSpec(
            name = "screen_uv_to_sdf",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("uv", VEC2)
            ),
        ),
    )

    private val globalVariables = listOf(
        ParameterSpec(
            name = "TIME",
            type = FLOAT,
        ),

        ParameterSpec(
            name = "PI",
            type = FLOAT,
        ),

        ParameterSpec(
            name = "TAU",
            type = FLOAT,
        ),

        ParameterSpec(
            name = "E",
            type = FLOAT,
        ),
    )
    
}

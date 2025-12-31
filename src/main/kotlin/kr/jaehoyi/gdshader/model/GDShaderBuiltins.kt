package kr.jaehoyi.gdshader.model

import java.util.EnumMap

object GDShaderBuiltins {
    
    val FUNCTIONS: Map<Pair<ShaderType, FunctionContext>, List<FunctionSpec>> = mapOf(
        ShaderType.SPATIAL to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,

        ShaderType.CANVAS_ITEM to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,

        ShaderType.CANVAS_ITEM to FunctionContext.FRAGMENT to sdfFunctions,
        
        ShaderType.CANVAS_ITEM to FunctionContext.LIGHT to sdfFunctions,

        ShaderType.PARTICLES to FunctionContext.COMMON to globalFunctions,
        
        ShaderType.PARTICLES to FunctionContext.PROCESS to listOf(
            FunctionSpec(
                name = "emit_subparticle",
                returnType = DataType.BOOL,
                parameters = listOf(
                    ParameterSpec("xform", DataType.MAT4),
                    ParameterSpec("velocity", DataType.VEC3),
                    ParameterSpec("color", DataType.VEC4),
                    ParameterSpec("custom", DataType.VEC4),
                    ParameterSpec("flags", DataType.UINT),
                ),
            ),
        ),

        ShaderType.SKY to FunctionContext.COMMON to globalFunctions + fragmentShaderOnlyFunctions,

        ShaderType.FOG to FunctionContext.COMMON to globalFunctions,
    )

    val VARIABLES: Map<Pair<ShaderType, FunctionContext>, List<VariableSpec>> = mapOf(
        ShaderType.SPATIAL to FunctionContext.COMMON to globalVariables + listOf(
            VariableSpec(
                name = "OUTPUT_IS_SRGB",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CLIP_SPACE_FAR",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),
        ),
        
        ShaderType.SPATIAL to FunctionContext.VERTEX to listOf(
            VariableSpec(
                name = "VIEWPORT_SIZE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INV_VIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "MAIN_CAM_INV_VIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INV_PROJECTION_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "NODE_POSITION_WORLD",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "NODE_POSITION_VIEW",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CAMERA_POSITION_WORLD",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CAMERA_DIRECTION_WORLD",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CAMERA_VISIBLE_LAYERS",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INSTANCE_ID",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INSTANCE_CUSTOM",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW_INDEX",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW_MONO_LEFT",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW_RIGHT",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "EYE_OFFSET",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VERTEX",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "VERTEX_ID",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "NORMAL",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "TANGENT",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "BINORMAL",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "POSITION",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "UV2",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "ROUGHNESS",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "POINT_SIZE",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "MODELVIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "MODELVIEW_NORMAL_MATRIX",
                type = DataType.MAT3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "MODEL_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "MODEL_NORMAL_MATRIX",
                type = DataType.MAT3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "PROJECTION_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "BONE_INDICES",
                type = DataType.UVEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "BONE_WEIGHTS",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CUSTOM0",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CUSTOM1",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CUSTOM2",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CUSTOM3",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),
        ),

        ShaderType.SPATIAL to FunctionContext.FRAGMENT to listOf(
            VariableSpec(
                name = "VIEWPORT_SIZE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FRAGCOORD",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FRONT_FACING",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UV2",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "POINT_COORD",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "MODEL_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "MODEL_NORMAL_MATRIX",
                type = DataType.MAT3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INV_VIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "PROJECTION_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "INV_PROJECTION_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "NODE_POSITION_WORLD",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "NODE_POSITION_VIEW",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CAMERA_POSITION_WORLD",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "CAMERA_DIRECTION_WORLD",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "CAMERA_VISIBLE_LAYERS",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "VERTEX",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_VERTEX",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "VIEW_INDEX",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "VIEW_MONO_LEFT",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "VIEW_RIGHT",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "EYE_OFFSET",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_TEXTURE",
                type = DataType.SAMPLER2D,
            ),

            VariableSpec(
                name = "SCREEN_UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "DEPTH_TEXTURE",
                type = DataType.SAMPLER2D,
            ),
            
            VariableSpec(
                name = "DEPTH",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "NORMAL",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "TANGENT",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),
            
            VariableSpec(
                name = "BINORMAL",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),
            
            VariableSpec(
                name = "NORMAL_MAP",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "NORMAL_MAP_DEPTH",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "ALBEDO",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "ALPHA",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "ALPHA_SCISSOR_THRESHOLD",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "ALPHA_TEXTURE_COORDINATE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "PREMUL_ALPHA_FACTOR",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "METALLIC",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "SPECULAR",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "ROUGHNESS",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "RIM",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "RIM_TINT",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "CLEARCOAT",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "CLEARCOAT_GLOSS",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "ANISOTROPY",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "ANISOTROPY_FLOW",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "SSS_STRENGTH",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "SSS_TRANSMITTANCE_COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "SSS_TRANSMITTANCE_DEPTH",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "SSS_TRANSMISSION_BOOST",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "BACKLIGHT",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "AO",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "AO_LIGHT_AFFECT",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "EMISSION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),
            
            VariableSpec(
                name = "FOG",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "RADIANCE",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "IRRADIANCE",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.OUT,
            ),
        ),

        ShaderType.SPATIAL to FunctionContext.LIGHT to listOf(
            VariableSpec(
                name = "VIEWPORT_SIZE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FRAGCOORD",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "MODEL_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INV_VIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "PROJECTION_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INV_PROJECTION_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "NORMAL",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UV2",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VIEW",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_COLOR",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SPECULAR_AMOUNT",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_IS_DIRECTIONAL",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "ATTENUATION",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "ALBEDO",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "BACKLIGHT",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "METALLIC",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "ROUGHNESS",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "DIFFUSE_LIGHT",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "SPECULAR_LIGHT",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "ALPHA",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),
        ),
        
        ShaderType.CANVAS_ITEM to FunctionContext.COMMON to globalVariables,
        
        ShaderType.CANVAS_ITEM to FunctionContext.VERTEX to listOf(
            VariableSpec(
                name = "MODEL_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CANVAS_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_MATRIX",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INSTANCE_ID",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INSTANCE_CUSTOM",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "AT_LIGHT_PASS",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "VERTEX",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "VERTEX_ID",
                type = DataType.INT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "POINT_SIZE",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "CUSTOM0",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "CUSTOM1",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),
        ),

        ShaderType.CANVAS_ITEM to FunctionContext.FRAGMENT to listOf(
            VariableSpec(
                name = "FRAGCOORD",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_PIXEL_SIZE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "REGION_RECT",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "POINT_COORD",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "TEXTURE",
                type = DataType.SAMPLER2D,
            ),

            VariableSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "AT_LIGHT_PASS",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SPECULAR_SHININESS_TEXTURE",
                type = DataType.SAMPLER2D,
            ),

            VariableSpec(
                name = "SPECULAR_SHININESS",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_TEXTURE",
                type = DataType.SAMPLER2D,
            ),

            VariableSpec(
                name = "NORMAL",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "NORMAL_TEXTURE",
                type = DataType.SAMPLER2D,
            ),

            VariableSpec(
                name = "NORMAL_MAP",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "NORMAL_MAP_DEPTH",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "VERTEX",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "SHADOW_VERTEX",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "LIGHT_VERTEX",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),
        ),

        ShaderType.CANVAS_ITEM to FunctionContext.LIGHT to listOf(
            VariableSpec(
                name = "FRAGCOORD",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "NORMAL",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),
            
            VariableSpec(
                name = "COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "TEXTURE",
                type = DataType.SAMPLER2D,
            ),

            VariableSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "POINT_COORD",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_ENERGY",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_POSITION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_DIRECTION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_IS_DIRECTIONAL",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT_VERTEX",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHT",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "SPECULAR_SHININESS",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SHADOW_MODULATE",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.OUT,
            ),
        ),

        ShaderType.PARTICLES to FunctionContext.COMMON to globalVariables,
        
        ShaderType.PARTICLES to FunctionContext.START to listOf(
            VariableSpec(
                name = "LIFETIME",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "DELTA",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "NUMBER",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INDEX",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "EMISSION_TRANSFORM",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RANDOM_SEED",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "ACTIVE",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "VELOCITY",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "TRANSFORM",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "CUSTOM",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "MASS",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "USERDATAX",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_POSITION",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_ROT_SCALE",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_VELOCITY",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_COLOR",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_CUSTOM",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "EMITTER_VELOCITY",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INTERPOLATE_TO_END",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "AMOUNT_RATIO",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RESTART_POSITION",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RESTART_ROT_SCALE",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RESTART_VELOCITY",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RESTART_COLOR",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RESTART_CUSTOM",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),
        ),

        ShaderType.PARTICLES to FunctionContext.PROCESS to listOf(
            VariableSpec(
                name = "LIFETIME",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "DELTA",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "NUMBER",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INDEX",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "EMISSION_TRANSFORM",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RANDOM_SEED",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "ACTIVE",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "VELOCITY",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "TRANSFORM",
                type = DataType.MAT4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "CUSTOM",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "MASS",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.INOUT,
            ),

            VariableSpec(
                name = "USERDATAX",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_POSITION",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_ROT_SCALE",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_VELOCITY",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_COLOR",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "FLAG_EMIT_CUSTOM",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "EMITTER_VELOCITY",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "INTERPOLATE_TO_END",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "AMOUNT_RATIO",
                type = DataType.UINT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RESTART",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "COLLIDED",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "COLLISION_NORMAL",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "COLLISION_DEPTH",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "ATTRACTOR_FORCE",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),
        ),

        ShaderType.SKY to FunctionContext.COMMON to globalVariables + listOf(
            VariableSpec(
                name = "POSITION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "RADIANCE",
                type = DataType.SAMPLERCUBE,
            ),

            VariableSpec(
                name = "AT_HALF_RES_PASS",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "AT_QUARTER_RES_PASS",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "AT_CUBEMAP_PASS",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHTX_ENABLED",
                type = DataType.BOOL,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHTX_ENERGY",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHTX_DIRECTION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHTX_COLOR",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "LIGHTX_SIZE",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.IN,
            ),
        ),
        
        ShaderType.SKY to FunctionContext.SKY to listOf(
            VariableSpec(
                name = "EYEDIR",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SCREEN_UV",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SKY_COORDS",
                type = DataType.VEC2,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "HALF_RES_COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "QUARTER_RES_COLOR",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "COLOR",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "ALPHA",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "FOG",
                type = DataType.VEC4,
                parameterQualifier = ParameterQualifier.OUT,
            ),
        ),

        ShaderType.FOG to FunctionContext.COMMON to globalVariables,
        
        ShaderType.FOG to FunctionContext.FOG to listOf(
            VariableSpec(
                name = "WORLD_POSITION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "OBJECT_POSITION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "UVW",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SIZE",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "SDF",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.IN,
            ),

            VariableSpec(
                name = "ALBEDO",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "DENSITY",
                type = DataType.FLOAT,
                parameterQualifier = ParameterQualifier.OUT,
            ),

            VariableSpec(
                name = "EMISSION",
                type = DataType.VEC3,
                parameterQualifier = ParameterQualifier.OUT,
            ),
        ),
    )
    
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
    
}

private val globalFunctions = listOf(
    FunctionSpec(
        name = "radians",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("degrees", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "degrees",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("radians", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "sin",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "cos",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "tan",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "asin",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "acos",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "atan",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("y_over_x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "atan",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("y", DataType.VEC_TYPE),
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "sinh",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "cosh",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "tanh",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "asinh",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "acosh",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "atanh",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "pow",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "exp",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "exp2",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "log",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "log2",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "sqrt",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "inversesqrt",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "abs",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "abs",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "sign",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "sign",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "floor",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "round",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "roundEven",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "trunc",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "ceil",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "fract",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "mod",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "mod",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "modf",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("i", DataType.VEC_TYPE, qualifier = ParameterQualifier.OUT)
        ),
    ),

    FunctionSpec(
        name = "min",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "min",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "min",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_INT_TYPE),
            ParameterSpec("b", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "min",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_INT_TYPE),
            ParameterSpec("b", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "min",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_UINT_TYPE),
            ParameterSpec("b", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "min",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_UINT_TYPE),
            ParameterSpec("b", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "max",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "max",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "max",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_INT_TYPE),
            ParameterSpec("b", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "max",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_INT_TYPE),
            ParameterSpec("b", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "max",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_UINT_TYPE),
            ParameterSpec("b", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "max",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_UINT_TYPE),
            ParameterSpec("b", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "clamp",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("min", DataType.VEC_TYPE),
            ParameterSpec("max", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "clamp",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("min", DataType.FLOAT),
            ParameterSpec("max", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "clamp",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_INT_TYPE),
            ParameterSpec("min", DataType.VEC_INT_TYPE),
            ParameterSpec("max", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "clamp",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_INT_TYPE),
            ParameterSpec("min", DataType.INT),
            ParameterSpec("max", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "clamp",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_UINT_TYPE),
            ParameterSpec("min", DataType.VEC_UINT_TYPE),
            ParameterSpec("max", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "clamp",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_UINT_TYPE),
            ParameterSpec("min", DataType.UINT),
            ParameterSpec("max", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "mix",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE),
            ParameterSpec("c", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "mix",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE),
            ParameterSpec("c", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "mix",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE),
            ParameterSpec("c", DataType.VEC_BOOL_TYPE)
        ),
    ),

    FunctionSpec(
        name = "fma",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE),
            ParameterSpec("c", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "step",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "step",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.FLOAT),
            ParameterSpec("b", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "smoothstep",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE),
            ParameterSpec("c", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "smoothstep",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("a", DataType.FLOAT),
            ParameterSpec("b", DataType.FLOAT),
            ParameterSpec("c", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "isnan",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "isinf",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "floatBitsToInt",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "floatBitsToUint",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "intBitsToFloat",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "uintBitsToFloat",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "length",
        returnType = DataType.FLOAT,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "distance",
        returnType = DataType.FLOAT,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "dot",
        returnType = DataType.FLOAT,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC_TYPE),
            ParameterSpec("b", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "cross",
        returnType = DataType.VEC3,
        parameters = listOf(
            ParameterSpec("a", DataType.VEC3),
            ParameterSpec("b", DataType.VEC3)
        ),
    ),

    FunctionSpec(
        name = "normalize",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "reflect",
        returnType = DataType.VEC3,
        parameters = listOf(
            ParameterSpec("I", DataType.VEC3),
            ParameterSpec("N", DataType.VEC3)
        ),
    ),

    FunctionSpec(
        name = "refract",
        returnType = DataType.VEC3,
        parameters = listOf(
            ParameterSpec("I", DataType.VEC3),
            ParameterSpec("N", DataType.VEC3),
            ParameterSpec("eta", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "faceforward",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("N", DataType.VEC_TYPE),
            ParameterSpec("I", DataType.VEC_TYPE),
            ParameterSpec("Nref", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "matrixCompMult",
        returnType = DataType.MAT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.MAT_TYPE),
            ParameterSpec("y", DataType.MAT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "outerProduct",
        returnType = DataType.MAT_TYPE,
        parameters = listOf(
            ParameterSpec("column", DataType.VEC_TYPE),
            ParameterSpec("row", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "transpose",
        returnType = DataType.MAT_TYPE,
        parameters = listOf(
            ParameterSpec("m", DataType.MAT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "determinant",
        returnType = DataType.FLOAT,
        parameters = listOf(
            ParameterSpec("m", DataType.MAT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "inverse",
        returnType = DataType.MAT_TYPE,
        parameters = listOf(
            ParameterSpec("m", DataType.MAT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "lessThan",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE),
        ),
    ),

    FunctionSpec(
        name = "greaterThan",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE),
        ),
    ),

    FunctionSpec(
        name = "lessThanEqual",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE),
        ),
    ),

    FunctionSpec(
        name = "greaterThanEqual",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE),
        ),
    ),

    FunctionSpec(
        name = "equal",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE),
        ),
    ),

    FunctionSpec(
        name = "notEqual",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec("y", DataType.VEC_TYPE),
        ),
    ),

    FunctionSpec(
        name = "any",
        returnType = DataType.BOOL,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_BOOL_TYPE)
        ),
    ),

    FunctionSpec(
        name = "all",
        returnType = DataType.BOOL,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_BOOL_TYPE)
        ),
    ),

    FunctionSpec(
        name = "not",
        returnType = DataType.VEC_BOOL_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_BOOL_TYPE)
        ),
    ),

    FunctionSpec(
        name = "textureSize",
        returnType = DataType.IVEC2,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "textureSize",
        returnType = DataType.IVEC2,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBE),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "textureSize",
        returnType = DataType.IVEC2,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "textureSize",
        returnType = DataType.IVEC3,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "textureSize",
        returnType = DataType.IVEC3,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "textureQueryLevels",
        returnType = DataType.INT,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D)
        ),
    ),

    FunctionSpec(
        name = "textureQueryLevels",
        returnType = DataType.INT,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY)
        ),
    ),

    FunctionSpec(
        name = "textureQueryLevels",
        returnType = DataType.INT,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D)
        ),
    ),

    FunctionSpec(
        name = "textureQueryLevels",
        returnType = DataType.INT,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBE)
        ),
    ),

    FunctionSpec(
        name = "texture",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC2),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "texture",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "texture",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "texture",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBE),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "texture",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "texture",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLEREXTERNALOES),
            ParameterSpec("p", DataType.VEC2),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "textureProj",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "textureProj",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "textureProj",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("bias", DataType.FLOAT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "textureLod",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC2),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureLod",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureLod",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureLod",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBE),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureLod",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureProjLod",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureProjLod",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureProjLod",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("lod", DataType.FLOAT)
        ),
    ),

    FunctionSpec(
        name = "textureGrad",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC2),
            ParameterSpec("dPdx", DataType.VEC2),
            ParameterSpec("dPdy", DataType.VEC2),
        ),
    ),

    FunctionSpec(
        name = "textureGrad",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("dPdx", DataType.VEC2),
            ParameterSpec("dPdy", DataType.VEC2),
        ),
    ),

    FunctionSpec(
        name = "textureGrad",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("dPdx", DataType.VEC2),
            ParameterSpec("dPdy", DataType.VEC2),
        ),
    ),

    FunctionSpec(
        name = "textureGrad",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBE),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("dPdx", DataType.VEC3),
            ParameterSpec("dPdy", DataType.VEC3),
        ),
    ),

    FunctionSpec(
        name = "textureGrad",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("dPdx", DataType.VEC3),
            ParameterSpec("dPdy", DataType.VEC3),
        ),
    ),

    FunctionSpec(
        name = "textureProjGrad",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("dPdx", DataType.VEC2),
            ParameterSpec("dPdy", DataType.VEC2),
        ),
    ),

    FunctionSpec(
        name = "textureProjGrad",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("dPdx", DataType.VEC2),
            ParameterSpec("dPdy", DataType.VEC2),
        ),
    ),

    FunctionSpec(
        name = "textureProjGrad",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.VEC4),
            ParameterSpec("dPdx", DataType.VEC3),
            ParameterSpec("dPdy", DataType.VEC3),
        ),
    ),

    FunctionSpec(
        name = "texelFetch",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.IVEC2),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "texelFetch",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY),
            ParameterSpec("p", DataType.IVEC3),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "texelFetch",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.IVEC3),
            ParameterSpec("lod", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "textureGather",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC2),
            ParameterSpec("comps", DataType.INT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "textureGather",
        returnType = DataType.GVEC4_TYPE,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("comps", DataType.INT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "textureGather",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBE),
            ParameterSpec("p", DataType.VEC3),
            ParameterSpec("comps", DataType.INT, isOptional = true)
        ),
    ),

    FunctionSpec(
        name = "fwidth",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "packHalf2x16",
        returnType = DataType.UINT,
        parameters = listOf(
            ParameterSpec("v", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "unpackHalf2x16",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("v", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "packUnorm2x16",
        returnType = DataType.UINT,
        parameters = listOf(
            ParameterSpec("v", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "unpackUnorm2x16",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("v", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "packSnorm2x16",
        returnType = DataType.UINT,
        parameters = listOf(
            ParameterSpec("v", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "unpackSnorm2x16",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("v", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "packUnorm4x8",
        returnType = DataType.UINT,
        parameters = listOf(
            ParameterSpec("v", DataType.VEC4)
        ),
    ),

    FunctionSpec(
        name = "unpackUnorm4x8",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("v", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "packSnorm4x8",
        returnType = DataType.UINT,
        parameters = listOf(
            ParameterSpec("v", DataType.VEC4)
        ),
    ),

    FunctionSpec(
        name = "unpackSnorm4x8",
        returnType = DataType.VEC4,
        parameters = listOf(
            ParameterSpec("v", DataType.UINT)
        ),
    ),

    FunctionSpec(
        name = "bitfieldExtract",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_INT_TYPE),
            ParameterSpec("offset", DataType.INT),
            ParameterSpec("bits", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "bitfieldExtract",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_UINT_TYPE),
            ParameterSpec("offset", DataType.INT),
            ParameterSpec("bits", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "bitfieldInsert",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("base", DataType.VEC_INT_TYPE),
            ParameterSpec("insert", DataType.VEC_INT_TYPE),
            ParameterSpec("offset", DataType.INT),
            ParameterSpec("bits", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "bitfieldInsert",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("base", DataType.VEC_UINT_TYPE),
            ParameterSpec("insert", DataType.VEC_UINT_TYPE),
            ParameterSpec("offset", DataType.INT),
            ParameterSpec("bits", DataType.INT)
        ),
    ),

    FunctionSpec(
        name = "bitfieldReverse",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "bitfieldReverse",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "bitCount",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "bitCount",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "findLSB",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "findLSB",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "findMSB",
        returnType = DataType.VEC_INT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_INT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "findMSB",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("value", DataType.VEC_UINT_TYPE)
        ),
    ),

    FunctionSpec(
        name = "imulExtended",
        returnType = DataType.VOID,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_INT_TYPE),
            ParameterSpec("y", DataType.VEC_INT_TYPE),
            ParameterSpec(
                "msb",
                DataType.VEC_INT_TYPE,
                qualifier = ParameterQualifier.OUT
            ),
            ParameterSpec(
                "lsb",
                DataType.VEC_INT_TYPE,
                qualifier = ParameterQualifier.OUT
            )
        ),
    ),

    FunctionSpec(
        name = "umulExtended",
        returnType = DataType.VOID,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_UINT_TYPE),
            ParameterSpec("y", DataType.VEC_UINT_TYPE),
            ParameterSpec(
                "msb",
                DataType.VEC_UINT_TYPE,
                qualifier = ParameterQualifier.OUT
            ),
            ParameterSpec(
                "lsb",
                DataType.VEC_UINT_TYPE,
                qualifier = ParameterQualifier.OUT
            )
        ),
    ),

    FunctionSpec(
        name = "uaddCarry",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_UINT_TYPE),
            ParameterSpec("y", DataType.VEC_UINT_TYPE),
            ParameterSpec(
                "carry",
                DataType.VEC_UINT_TYPE,
                qualifier = ParameterQualifier.OUT
            )
        ),
    ),

    FunctionSpec(
        name = "usubBorrow",
        returnType = DataType.VEC_UINT_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_UINT_TYPE),
            ParameterSpec("y", DataType.VEC_UINT_TYPE),
            ParameterSpec(
                "borrow",
                DataType.VEC_UINT_TYPE,
                qualifier = ParameterQualifier.OUT
            )
        ),
    ),

    FunctionSpec(
        name = "ldexp",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec(
                "exp",
                DataType.VEC_INT_TYPE,
                qualifier = ParameterQualifier.OUT
            )
        ),
    ),

    FunctionSpec(
        name = "frexp",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("x", DataType.VEC_TYPE),
            ParameterSpec(
                "exp",
                DataType.VEC_INT_TYPE,
                qualifier = ParameterQualifier.OUT
            )
        ),
    ),
)

private val fragmentShaderOnlyFunctions = listOf(
    FunctionSpec(
        name = "textureQueryLod",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2D),
            ParameterSpec("p", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "textureQueryLod",
        returnType = DataType.VEC3,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER2DARRAY),
            ParameterSpec("p", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "textureQueryLod",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("s", DataType.GSAMPLER3D),
            ParameterSpec("p", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "textureQueryLod",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("s", DataType.SAMPLERCUBE),
            ParameterSpec("p", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "dFdx",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "dFdxCoarse",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "dFdxFine",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "dFdy",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "dFdyCoarse",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "dFdyFine",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "fwidthCoarse",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),

    FunctionSpec(
        name = "fwidthFine",
        returnType = DataType.VEC_TYPE,
        parameters = listOf(
            ParameterSpec("p", DataType.VEC_TYPE)
        ),
    ),
)

private val sdfFunctions = listOf(
    FunctionSpec(
        name = "texture_sdf",
        returnType = DataType.FLOAT,
        parameters = listOf(
            ParameterSpec("sdf_pos", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "texture_sdf_normal",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("sdf_pos", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "sdf_to_screen_uv",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("sdf_pos", DataType.VEC2)
        ),
    ),

    FunctionSpec(
        name = "screen_uv_to_sdf",
        returnType = DataType.VEC2,
        parameters = listOf(
            ParameterSpec("uv", DataType.VEC2)
        ),
    ),
)

private val globalVariables = listOf(
    VariableSpec(
        name = "TIME",
        type = DataType.FLOAT,
        parameterQualifier = ParameterQualifier.IN,
    ),

    VariableSpec(
        name = "PI",
        type = DataType.FLOAT,
        parameterQualifier = ParameterQualifier.IN,
    ),

    VariableSpec(
        name = "TAU",
        type = DataType.FLOAT,
        parameterQualifier = ParameterQualifier.IN,
    ),

    VariableSpec(
        name = "E",
        type = DataType.FLOAT,
        parameterQualifier = ParameterQualifier.IN,
    ),
)

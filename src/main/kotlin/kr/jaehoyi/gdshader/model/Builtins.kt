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
                description = "Emits a particle from a sub-emitter.",
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
                description = "<code>true</code> when output is in sRGB color space (this is <code>true</code> in the Compatibility renderer, <code>false</code> in Forward+ and Mobile)."
            ),

            ParameterSpec(
                name = "CLIP_SPACE_FAR",
                type = FLOAT,
                description = "Clip space far <code>z</code> value. In the Forward+ or Mobile renderers, it's <code>0.0</code>. In the Compatibility renderer, it's <code>-1.0.</code>"
            ),
        ),
        
        ShaderType.SPATIAL to FunctionContext.VERTEX to listOf(
            ParameterSpec(
                name = "VIEWPORT_SIZE",
                type = VEC2,
                description = "Size of viewport (in pixels)."
            ),

            ParameterSpec(
                name = "VIEW_MATRIX",
                type = MAT4,
                description = "World space to view space transform."
            ),

            ParameterSpec(
                name = "INV_VIEW_MATRIX",
                type = MAT4,
                description = "View space to world space transform."
            ),

            ParameterSpec(
                name = "MAIN_CAM_INV_VIEW_MATRIX",
                type = MAT4,
                description = "View space to world space transform of the camera used to draw the current viewport."
            ),

            ParameterSpec(
                name = "INV_PROJECTION_MATRIX",
                type = MAT4,
                description = "Clip space to view space transform."
            ),

            ParameterSpec(
                name = "NODE_POSITION_WORLD",
                type = VEC3,
                description = "Node position, in world space."
            ),

            ParameterSpec(
                name = "NODE_POSITION_VIEW",
                type = VEC3,
                description = "Node position, in view space."
            ),

            ParameterSpec(
                name = "CAMERA_POSITION_WORLD",
                type = VEC3,
                description = "Camera position, in world space. Represents the midpoint of the two eyes when in multiview/stereo rendering."
            ),

            ParameterSpec(
                name = "CAMERA_DIRECTION_WORLD",
                type = VEC3,
                description = "Camera direction, in world space."
            ),

            ParameterSpec(
                name = "CAMERA_VISIBLE_LAYERS",
                type = UINT,
                description = "Cull layers of the camera rendering the current pass."
            ),

            ParameterSpec(
                name = "INSTANCE_ID",
                type = INT,
                description = "Instance ID for instancing."
            ),

            ParameterSpec(
                name = "INSTANCE_CUSTOM",
                type = VEC4,
                description = "Instance custom data (for particles, mostly)."
            ),

            ParameterSpec(
                name = "VIEW_INDEX",
                type = INT,
                description = "The view that we are rendering. <code>VIEW_MONO_LEFT</code> (<code>0</code>) for Mono (not multiview) or left eye, <code>VIEW_RIGHT</code> (<code>1</code>) for right eye."
            ),

            ParameterSpec(
                name = "VIEW_MONO_LEFT",
                type = INT,
                description = "Constant for Mono or left eye, always <code>0</code>."
            ),

            ParameterSpec(
                name = "VIEW_RIGHT",
                type = INT,
                description = "Constant for right eye, always <code>1</code>."
            ),

            ParameterSpec(
                name = "EYE_OFFSET",
                type = VEC3,
                description = "Position offset for the eye being rendered, in view space. Only applicable for multiview rendering."
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Position of the vertex, in model space. In world space if <code>world_vertex_coords</code> is used."
            ),

            ParameterSpec(
                name = "VERTEX_ID",
                type = INT,
                description = "The index of the current vertex in the vertex buffer."
            ),

            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Normal in model space. In world space if <code>world_vertex_coords</code> is used."
            ),

            ParameterSpec(
                name = "TANGENT",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Tangent in model space. In world space if <code>world_vertex_coords</code> is used."
            ),

            ParameterSpec(
                name = "BINORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description =  "Binormal in model space. In world space if <code>world_vertex_coords</code> is used."
            ),

            ParameterSpec(
                name = "POSITION",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
                description = "If written to, overrides final vertex position in clip space."
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
                description = "UV main channel."
            ),

            ParameterSpec(
                name = "UV2",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
                description = "UV secondary channel."
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
                description = "Color from vertices. Limited to values between <code>0.0</code> and <code>1.0</code> for each channel and 8 bits per channel precision (256 possible levels). Alpha channel is supported. Values outside the allowed range are clamped, and values may be rounded due to precision limitations. Use <code>CUSTOM0</code>-<code>CUSTOM3</code> to pass data with more precision if needed."
            ),

            ParameterSpec(
                name = "ROUGHNESS",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Roughness for vertex lighting."
            ),

            ParameterSpec(
                name = "POINT_SIZE",
                type = FLOAT,
                qualifier = ParameterQualifier.INOUT,
                description = "Point size for point rendering."
            ),

            ParameterSpec(
                name = "MODELVIEW_MATRIX",
                type = MAT4,
                qualifier = ParameterQualifier.INOUT,
                description = "Model/local space to view space transform (use if possible)."
            ),

            ParameterSpec(
                name = "MODELVIEW_NORMAL_MATRIX",
                type = MAT3,
                qualifier = ParameterQualifier.INOUT,
            ),

            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
                description = "Model/local space to world space transform."
            ),

            ParameterSpec(
                name = "MODEL_NORMAL_MATRIX",
                type = MAT3,
            ),

            ParameterSpec(
                name = "PROJECTION_MATRIX",
                type = MAT4,
                qualifier = ParameterQualifier.INOUT,
                description =  "View space to clip space transform."
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
                description =  "Custom value from vertex primitive. When using extra UVs, <code>xy</code> is UV3 and <code>zw</code> is UV4."
            ),

            ParameterSpec(
                name = "CUSTOM1",
                type = VEC4,
                description = "Custom value from vertex primitive. When using extra UVs, <code>xy</code> is UV5 and <code>zw</code> is UV6."
            ),

            ParameterSpec(
                name = "CUSTOM2",
                type = VEC4,
                description = "Custom value from vertex primitive. When using extra UVs, <code>xy</code> is UV7 and <code>zw</code> is UV8."
            ),

            ParameterSpec(
                name = "CUSTOM3",
                type = VEC4,
                description =  "Custom value from vertex primitive."
            ),

            ParameterSpec(
                name = "Z_CLIP_SCALE",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "If written to, scales the vertex towards the camera to avoid clipping into things like walls. Lighting and shadows will continue to work correctly when this is written to, but screen-space effects like SSAO and SSR may break with lower scales. Try to keep this value as close to <code>1.0</code> as possible."
            )
        ),

        ShaderType.SPATIAL to FunctionContext.FRAGMENT to listOf(
            ParameterSpec(
                name = "VIEWPORT_SIZE",
                type = VEC2,
                description = "Size of viewport (in pixels)."
            ),

            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
                description = "Coordinate of pixel center in screen space. <code>xy</code> specifies position in window. Origin is upper left. <code>z</code> specifies fragment depth. It is also used as the output value for the fragment depth unless <code>DEPTH</code> is written to."
            ),

            ParameterSpec(
                name = "FRONT_FACING",
                type = BOOL,
                description = "<code>true</code> if current face is front facing, <code>false</code> otherwise."
            ),

            ParameterSpec(
                name = "VIEW",
                type = VEC3,
                description =  "Normalized vector from fragment position to camera (in view space). This is the same for both perspective and orthogonal cameras."
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                description = "UV that comes from the <code>vertex()</code> function."
            ),

            ParameterSpec(
                name = "UV2",
                type = VEC2,
                description = "UV2 that comes from the <code>vertex()</code> function."
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                description = "COLOR that comes from the <code>vertex()</code> function."
            ),

            ParameterSpec(
                name = "POINT_COORD",
                type = VEC2,
                description = "Point coordinate for drawing points with <code>POINT_SIZE</code>."
            ),

            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
                description = "Model/local space to world space transform."
            ),

            ParameterSpec(
                name = "MODEL_NORMAL_MATRIX",
                type = MAT3,
                description = "Model/local space to world space transform for normals. This is the same as <code>MODEL_MATRIX</code> by default unless the object is scaled non-uniformly, in which case this is set to <code>transpose(inverse(mat3(MODEL_MATRIX)))</code>."
            ),

            ParameterSpec(
                name = "VIEW_MATRIX",
                type = MAT4,
                description = "World space to view space transform."
            ),

            ParameterSpec(
                name = "INV_VIEW_MATRIX",
                type = MAT4,
                description = "View space to world space transform."
            ),

            ParameterSpec(
                name = "PROJECTION_MATRIX",
                type = MAT4,
                description = "View space to clip space transform."
            ),

            ParameterSpec(
                name = "INV_PROJECTION_MATRIX",
                type = MAT4,
                description = "Clip space to view space transform."
            ),

            ParameterSpec(
                name = "NODE_POSITION_WORLD",
                type = VEC3,
                description = "Node position, in world space."
            ),

            ParameterSpec(
                name = "NODE_POSITION_VIEW",
                type = VEC3,
                description = "Node position, in view space."
            ),

            ParameterSpec(
                name = "CAMERA_POSITION_WORLD",
                type = VEC3,
                description = "Camera position, in world space. Represents the midpoint of the two eyes when in multiview/stereo rendering."
            ),

            ParameterSpec(
                name = "CAMERA_DIRECTION_WORLD",
                type = VEC3,
                description = "Camera direction, in world space."
            ),

            ParameterSpec(
                name = "CAMERA_VISIBLE_LAYERS",
                type = UINT,
                description = "Cull layers of the camera rendering the current pass."
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC3,
                description = "Position of the fragment (pixel), in view space. It is the <code>VERTEX</code> value from <code>vertex()</code> interpolated between the face's vertices and transformed into view space. If <code>skip_vertex_transform</code> is enabled, it may not be in view space."
            ),

            ParameterSpec(
                name = "LIGHT_VERTEX",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "A writable version of <code>VERTEX</code> that can be used to alter light and shadows. Writing to this will not change the position of the fragment."
            ),

            ParameterSpec(
                name = "VIEW_INDEX",
                type = INT,
                description = "The view that we are rendering. Used to distinguish between views in multiview/stereo rendering. <code>VIEW_MONO_LEFT</code> (<code>0</code>) for Mono (not multiview) or left eye, <code>VIEW_RIGHT</code> (<code>1</code>) for right eye."
            ),

            ParameterSpec(
                name = "VIEW_MONO_LEFT",
                type = INT,
                description = "Constant for Mono or left eye, always <code>0</code>."
            ),

            ParameterSpec(
                name = "VIEW_RIGHT",
                type = INT,
                description = "Constant for right eye, always <code>1</code>."
                
            ),

            ParameterSpec(
                name = "EYE_OFFSET",
                type = VEC3,
                description = "Position offset for the eye being rendered, in view space. Only applicable for multiview rendering."
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
                description = "Screen UV coordinate for the current pixel."
            ),

            ParameterSpec(
                name = "DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Custom depth value (range <code>[0.0, 1.0]</code>). If <code>DEPTH</code> is written to in any shader branch, then you are responsible for setting <code>DEPTH</code> for all other branches. Otherwise, the graphics API will leave them uninitialized."
            ),

            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Normal that comes from the <code>vertex()</code> function, in view space. If <code>skip_vertex_transform</code> is enabled, it may not be in view space."
            ),

            ParameterSpec(
                name = "TANGENT",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Tangent that comes from the <code>vertex()</code> function, in view space. If <code>skip_vertex_transform</code> is enabled, it may not be in view space."
            ),

            ParameterSpec(
                name = "BINORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Binormal that comes from the <code>vertex()</code> function, in view space. If <code>skip_vertex_transform</code> is enabled, it may not be in view space."
            ),

            ParameterSpec(
                name = "NORMAL_MAP",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Set normal here if reading normal from a texture instead of <code>NORMAL</code>."
            ),

            ParameterSpec(
                name = "NORMAL_MAP_DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Depth from <code>NORMAL_MAP</code>. Defaults to <code>1.0</code>."
            ),

            ParameterSpec(
                name = "ALBEDO",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Albedo (default white). Base color."
            ),

            ParameterSpec(
                name = "ALPHA",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Alpha (range <code>[0.0, 1.0]</code>). If read from or written to, the material will go to the transparent pipeline."
            ),

            ParameterSpec(
                name = "ALPHA_SCISSOR_THRESHOLD",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "If written to, values below a certain amount of alpha are discarded."
            ),

            ParameterSpec(
                name = "ALPHA_HASH_SCALE",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Alpha hash scale when using the alpha hash transparency mode. Defaults to <code>1.0</code>. Higher values result in more visible pixels in the dithering pattern."
            ),

            ParameterSpec(
                name = "ALPHA_ANTIALIASING_EDGE",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "The threshold below which alpha to coverage antialiasing should be used. Defaults to <code>0.0</code>. Requires the <code>alpha_to_coverage</code> render mode. Should be set to a value lower than <code>ALPHA_SCISSOR_THRESHOLD</code> to be effective."
            ),

            ParameterSpec(
                name = "ALPHA_TEXTURE_COORDINATE",
                type = VEC2,
                qualifier = ParameterQualifier.OUT,
                description = "The texture coordinate to use for alpha-to-coverge antialiasing. Requires the <code>alpha_to_coverage</code> render mode. Typically set to <code>UV * vec2(albedo_texture_size)</code> where <code>albedo_texture_size</code> is the size of the albedo texture in pixels."
            ),

            ParameterSpec(
                name = "PREMUL_ALPHA_FACTOR",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Premultiplied alpha factor. Only effective if <code>render_mode blend_premul_alpha;</code> is used. This should be written to when using a shaded material with premultiplied alpha blending for interaction with lighting. This is not required for unshaded materials."
            ),

            ParameterSpec(
                name = "METALLIC",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Metallic (range <code>[0.0, 1.0]</code>)."
            ),

            ParameterSpec(
                name = "SPECULAR",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Specular (not physically accurate to change). Defaults to <code>0.5</code>. <code>0.0</code> disables reflections."
            ),

            ParameterSpec(
                name = "ROUGHNESS",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Roughness (range <code>[0.0, 1.0]</code>)."
            ),

            ParameterSpec(
                name = "RIM",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Rim (range <code>[0.0, 1.0]</code>). If used, Godot calculates rim lighting. Rim size depends on <code>ROUGHNESS</code>."
            ),

            ParameterSpec(
                name = "RIM_TINT",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Rim Tint, range from <code>0.0</code> (white) to <code>1.0</code> (albedo). If used, Godot calculates rim lighting."
            ),

            ParameterSpec(
                name = "CLEARCOAT",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Small specular blob added on top of the existing one. If used, Godot calculates clearcoat."
            ),

            ParameterSpec(
                name = "CLEARCOAT_GLOSS",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Gloss of clearcoat. If used, Godot calculates clearcoat."
            ),

            ParameterSpec(
                name = "ANISOTROPY",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "For distorting the specular blob according to tangent space."
            ),

            ParameterSpec(
                name = "ANISOTROPY_FLOW",
                type = VEC2,
                qualifier = ParameterQualifier.OUT,
                description = "Distortion direction, use with flowmaps."
            ),

            ParameterSpec(
                name = "SSS_STRENGTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Strength of subsurface scattering. If used, subsurface scattering will be applied to the object."
            ),

            ParameterSpec(
                name = "SSS_TRANSMITTANCE_COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
                description = "Color of subsurface scattering transmittance. If used, subsurface scattering transmittance will be applied to the object."
            ),

            ParameterSpec(
                name = "SSS_TRANSMITTANCE_DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Depth of subsurface scattering transmittance. Higher values allow the effect to reach deeper into the object."
            ),

            ParameterSpec(
                name = "SSS_TRANSMISSION_BOOST",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Boosts the subsurface scattering transmittance if set above <code>0.0</code>. This makes the effect show up even on directly lit surfaces"
            ),

            ParameterSpec(
                name = "BACKLIGHT",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Color of backlighting (works like direct light, but it's received even if the normal is slightly facing away from the light). If used, backlighting will be applied to the object. Can be used as a cheaper approximation of subsurface scattering."
            ),

            ParameterSpec(
                name = "AO",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Strength of ambient occlusion. For use with pre-baked AO."
            ),

            ParameterSpec(
                name = "AO_LIGHT_AFFECT",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "How much ambient occlusion affects direct light (range <code>[0.0, 1.0]</code>, default <code>0.0</code>)."
            ),

            ParameterSpec(
                name = "EMISSION",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Emission color (can go over <code>(1.0, 1.0, 1.0)</code> for HDR)."
            ),

            ParameterSpec(
                name = "FOG",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
                description = "If written to, blends final pixel color with <code>FOG.rgb</code> based on <code>FOG.a</code>."
            ),

            ParameterSpec(
                name = "RADIANCE",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
                description = "If written to, blends environment map radiance with <code>RADIANCE.rgb</code> based on <code>RADIANCE.a</code>."
            ),

            ParameterSpec(
                name = "IRRADIANCE",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
                description = "If written to, blends environment map irradiance with <code>IRRADIANCE.rgb</code> based on <code>IRRADIANCE.a</code>."
            ),
        ),

        ShaderType.SPATIAL to FunctionContext.LIGHT to listOf(
            ParameterSpec(
                name = "VIEWPORT_SIZE",
                type = VEC2,
                description = "Size of viewport (in pixels)."
            ),

            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
                description = "Coordinate of pixel center in screen space. <code>xy</code> specifies position in window, <code>z</code> specifies fragment depth if <code>DEPTH</code> is not used. Origin is lower-left."
            ),

            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
                description = "Model/local space to world space transform."
            ),

            ParameterSpec(
                name = "INV_VIEW_MATRIX",
                type = MAT4,
                description = "View space to world space transform."
            ),

            ParameterSpec(
                name = "VIEW_MATRIX",
                type = MAT4,
                description = "World space to view space transform."
            ),

            ParameterSpec(
                name = "PROJECTION_MATRIX",
                type = MAT4,
                description = "View space to clip space transform."
            ),

            ParameterSpec(
                name = "INV_PROJECTION_MATRIX",
                type = MAT4,
                description = "Clip space to view space transform."
            ),
            
            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                description = "Normal vector, in view space."
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
                description = "Screen UV coordinate for the current pixel."
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                description = "UV that comes from the <code>vertex()</code> function."
            ),

            ParameterSpec(
                name = "UV2",
                type = VEC2,
                description = "UV2 that comes from the <code>vertex()</code> function."
            ),

            ParameterSpec(
                name = "VIEW",
                type = VEC3,
                description = "View vector, in view space."
            ),

            ParameterSpec(
                name = "LIGHT",
                type = VEC3,
                description = "Light vector, in view space."
            ),

            ParameterSpec(
                name = "LIGHT_COLOR",
                type = VEC3,
                description = "<a href=\"https://docs.godotengine.org/en/stable/classes/class_light3d.html#class-light3d-property-light-color\">Light</a> color multiplied by <a href=\"https://docs.godotengine.org/en/stable/classes/class_light3d.html#class-light3d-property-light-energy\">light energy</a> multiplied by <code>PI</code>. The <code>PI</code> multiplication is present because physically-based lighting models include a division by <code>PI</code>."
            ),

            ParameterSpec(
                name = "SPECULAR_AMOUNT",
                type = FLOAT,
                description = "For <a href=\"https://docs.godotengine.org/en/stable/classes/class_omnilight3d.html#class-omnilight3d\">OmniLight3D</a> and <a href=\"https://docs.godotengine.org/en/stable/classes/class_spotlight3d.html#class-spotlight3d\">SpotLight3D</a>, <code>2.0</code> multiplied by <a href=\"https://docs.godotengine.org/en/stable/classes/class_light3d.html#class-light3d-property-light-specular\">light_specular</a>. For <a href=\"https://docs.godotengine.org/en/stable/classes/class_directionallight3d.html#class-directionallight3d\">DirectionalLight3D</a>, <code>1.0</code>."
            ),

            ParameterSpec(
                name = "LIGHT_IS_DIRECTIONAL",
                type = BOOL,
                description = "<code>true</code> if this pass is a <a href=\"https://docs.godotengine.org/en/stable/classes/class_directionallight3d.html#class-directionallight3d\">DirectionalLight3D</a>."
            ),

            ParameterSpec(
                name = "ATTENUATION",
                type = FLOAT,
                description = "Attenuation based on distance or shadow."
            ),

            ParameterSpec(
                name = "ALBEDO",
                type = VEC3,
                description = "Base albedo."
            ),

            ParameterSpec(
                name = "BACKLIGHT",
                type = VEC3,
            ),

            ParameterSpec(
                name = "METALLIC",
                type = FLOAT,
                description = "Metallic."
            ),

            ParameterSpec(
                name = "ROUGHNESS",
                type = FLOAT,
                description = "Roughness."
            ),

            ParameterSpec(
                name = "DIFFUSE_LIGHT",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Diffuse light result."
            ),

            ParameterSpec(
                name = "SPECULAR_LIGHT",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Specular light result."
            ),

            ParameterSpec(
                name = "ALPHA",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Alpha (range <code>[0.0, 1.0]</code>). If written to, the material will go to the transparent pipeline."
            ),
        ),
        
        ShaderType.CANVAS_ITEM to FunctionContext.COMMON to globalVariables,
        
        ShaderType.CANVAS_ITEM to FunctionContext.VERTEX to listOf(
            ParameterSpec(
                name = "MODEL_MATRIX",
                type = MAT4,
                description = "Local space to world space transform. World space is the coordinates you normally use in the editor."
            ),

            ParameterSpec(
                name = "CANVAS_MATRIX",
                type = MAT4,
                description = "World space to canvas space transform. In canvas space the origin is the upper-left corner of the screen and coordinates range from <code>(0.0, 0.0)</code> to viewport size."
            ),

            ParameterSpec(
                name = "SCREEN_MATRIX",
                type = MAT4,
                description = "Canvas space to clip space transform. In clip space coordinates range from <code>(-1.0, -1.0)</code> to <code>(1.0, 1.0)</code>."
            ),

            ParameterSpec(
                name = "INSTANCE_ID",
                type = INT,
                description = "Instance ID for instancing."
            ),

            ParameterSpec(
                name = "INSTANCE_CUSTOM",
                type = VEC4,
                description = "Instance custom data."
            ),

            ParameterSpec(
                name = "AT_LIGHT_PASS",
                type = BOOL,
                description = "Always <code>false</code>."
            ),

            ParameterSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = VEC2,
                description = "Normalized pixel size of the default 2D texture. For a Sprite2D with a texture of size 64x32px, <code>TEXTURE_PIXEL_SIZE</code> = <code>vec2(1.0/64.0, 1.0/32.0)</code>"
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
                description = "Vertex position, in local space."
            ),

            ParameterSpec(
                name = "VERTEX_ID",
                type = INT,
                description = "The index of the current vertex in the vertex buffer."
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
                description = "Normalized texture coordinates. Range from <code>0.0</code> to <code>1.0</code>."
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
                description = "Color from vertex primitive multiplied by the CanvasItem's <a href=\"https://docs.godotengine.org/en/stable/classes/class_canvasitem.html#class-canvasitem-property-modulate\">modulate</a> multiplied by CanvasItem's <a href=\"https://docs.godotengine.org/en/stable/classes/class_canvasitem.html#class-canvasitem-property-self-modulate\">self_modulate</a>."
            ),

            ParameterSpec(
                name = "POINT_SIZE",
                type = FLOAT,
                qualifier = ParameterQualifier.INOUT,
                description = "Point size for point drawing."
            ),

            ParameterSpec(
                name = "CUSTOM0",
                type = VEC4,
                description = "Custom value from vertex primitive."
            ),

            ParameterSpec(
                name = "CUSTOM1",
                type = VEC4,
                description = "Custom value from vertex primitive."
            ),
        ),

        ShaderType.CANVAS_ITEM to FunctionContext.FRAGMENT to listOf(
            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
                description = "Coordinate of pixel center. In screen space. <code>xy</code> specifies position in viewport. Upper-left of the viewport is the origin, <code>(0.0, 0.0)</code>."
            ),

            ParameterSpec(
                name = "SCREEN_PIXEL_SIZE",
                type = VEC2,
                description = "Size of individual pixels. Equal to the inverse of resolution."
            ),

            ParameterSpec(
                name = "REGION_RECT",
                type = VEC4,
                description = "Visible area of the sprite region in format <code>(x, y, width, height)</code>. Varies according to Sprite2D's <code>region_enabled</code> property."
            ),

            ParameterSpec(
                name = "POINT_COORD",
                type = VEC2,
                description = "Coordinate for drawing points."
            ),

            ParameterSpec(
                name = "TEXTURE",
                type = SAMPLER2D,
                description = "Default 2D texture."
            ),

            ParameterSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = VEC2,
                description = "Normalized pixel size of the default 2D texture. For a Sprite2D with a texture of size 64x32px, <code>TEXTURE_PIXEL_SIZE</code> = <code>vec2(1/64, 1/32)</code>"
            ),

            ParameterSpec(
                name = "AT_LIGHT_PASS",
                type = BOOL,
                description = "Always <code>false</code>."
            ),

            ParameterSpec(
                name = "SPECULAR_SHININESS_TEXTURE",
                type = SAMPLER2D,
                description = "Specular shininess texture of this object."
            ),

            ParameterSpec(
                name = "SPECULAR_SHININESS",
                type = VEC4,
                description = "Specular shininess color, as sampled from the texture."
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                description = "UV from the <code>vertex()</code> function. For a Sprite2D with region enabled, this will sample the entire texture. Use <code>REGION_RECT</code> instead to sample only the region defined in the Sprite2D's properties."
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
                description = "Screen UV coordinate for the current pixel."
            ),

            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Normal read from <code>NORMAL_TEXTURE</code>. Writable."
            ),

            ParameterSpec(
                name = "NORMAL_TEXTURE",
                type = SAMPLER2D,
                description = "Default 2D normal texture."
            ),

            ParameterSpec(
                name = "NORMAL_MAP",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Configures normal maps meant for 3D for use in 2D. If used, overrides <code>NORMAL</code>."
            ),

            ParameterSpec(
                name = "NORMAL_MAP_DEPTH",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Normal map depth for scaling."
            ),

            ParameterSpec(
                name = "VERTEX",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
                description = "Pixel position in screen space."
            ),

            ParameterSpec(
                name = "SHADOW_VERTEX",
                type = VEC2,
                qualifier = ParameterQualifier.INOUT,
                description = "Same as <code>VERTEX</code> but can be written to alter shadows."
            ),

            ParameterSpec(
                name = "LIGHT_VERTEX",
                type = VEC3,
                qualifier = ParameterQualifier.INOUT,
                description = "Same as <code>VERTEX</code> but can be written to alter lighting. Z component represents height."
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
                description = "<code>COLOR</code> from the <code>vertex()</code> function multiplied by the <code>TEXTURE</code> color. Also output color value."
            ),
        ),

        ShaderType.CANVAS_ITEM to FunctionContext.LIGHT to listOf(
            ParameterSpec(
                name = "FRAGCOORD",
                type = VEC4,
                description = "Coordinate of pixel center. In screen space. <code>xy</code> specifies position in viewport. Upper-left of the viewport is the origin, <code>(0.0, 0.0)</code>."
            ),
            
            ParameterSpec(
                name = "NORMAL",
                type = VEC3,
                description = "Input normal."
            ),
            
            ParameterSpec(
                name = "COLOR",
                type = VEC4,
                description = "Input color. This is the output of the <code>fragment()</code> function."
            ),

            ParameterSpec(
                name = "UV",
                type = VEC2,
                description = "UV from the <code>vertex()</code> function, equivalent to the UV in the <code>fragment()</code> function."
            ),

            ParameterSpec(
                name = "TEXTURE",
                type = SAMPLER2D,
                description = "Current texture in use for the CanvasItem."
            ),

            ParameterSpec(
                name = "TEXTURE_PIXEL_SIZE",
                type = VEC2,
                description = "Normalized pixel size of <code>TEXTURE</code>. For a Sprite2D with a <code>TEXTURE</code> of size <code>64x32</code> pixels, <code>TEXTURE_PIXEL_SIZE</code> = <code>vec2(1/64, 1/32)</code>"
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
                description = "Screen UV coordinate for the current pixel."
            ),

            ParameterSpec(
                name = "POINT_COORD",
                type = VEC2,
                description = "UV for Point Sprite."
            ),

            ParameterSpec(
                name = "LIGHT_COLOR",
                type = VEC4,
                description = "<a href=\"https://docs.godotengine.org/en/stable/classes/class_light2d.html#class-light2d-property-color\">Color</a> of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_light2d.html#class-light2d\">Light2D</a>. If the light is a <a href=\"https://docs.godotengine.org/en/stable/classes/class_pointlight2d.html#class-pointlight2d\">PointLight2D</a>, multiplied by the light's <a href=\"https://docs.godotengine.org/en/stable/classes/class_pointlight2d.html#class-pointlight2d-property-texture\">texture</a>."
            ),

            ParameterSpec(
                name = "LIGHT_ENERGY",
                type = FLOAT,
                description = "<a href=\"https://docs.godotengine.org/en/stable/classes/class_light2d.html#class-light2d-property-energy\">Energy multiplier</a> of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_light2d.html#class-light2d\">Light2D</a>."
            ),

            ParameterSpec(
                name = "LIGHT_POSITION",
                type = VEC3,
                description = "Position of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_light2d.html#class-light2d\">Light2D</a> in screen space. If using a <a href=\"https://docs.godotengine.org/en/stable/classes/class_directionallight2d.html#class-directionallight2d\">DirectionalLight2D</a> this is always <code>(0.0, 0.0, 0.0)</code>."
            ),

            ParameterSpec(
                name = "LIGHT_DIRECTION",
                type = VEC3,
                description = "Direction of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_light2d.html#class-light2d\">Light2D</a> in screen space."
            ),

            ParameterSpec(
                name = "LIGHT_IS_DIRECTIONAL",
                type = BOOL,
                description = "<code>true</code> if this pass is a <a href=\"https://docs.godotengine.org/en/stable/classes/class_directionallight2d.html#class-directionallight2d\">DirectionalLight2D</a>."
            ),

            ParameterSpec(
                name = "LIGHT_VERTEX",
                type = VEC3,
                description = "Pixel position, in screen space as modified in the <code>fragment()</code> function."
            ),

            ParameterSpec(
                name = "LIGHT",
                type = VEC4,
                qualifier = ParameterQualifier.INOUT,
                description = "Output color for this <a href=\"https://docs.godotengine.org/en/stable/classes/class_light2d.html#class-light2d\">Light2D</a>."
            ),

            ParameterSpec(
                name = "SPECULAR_SHININESS",
                type = VEC4,
                description = "Specular shininess, as set in the object's texture."
            ),

            ParameterSpec(
                name = "SHADOW_MODULATE",
                type = VEC4,
                qualifier = ParameterQualifier.OUT,
                description = "Multiply shadows cast at this point by this color."
            ),
        ),

        ShaderType.PARTICLES to FunctionContext.COMMON to globalVariables,
        
        ShaderType.PARTICLES to FunctionContext.START to particlesStartAndProcessVariables + listOf(
            ParameterSpec(
                name = "RESTART_POSITION",
                type = BOOL,
                description = "<code>true</code> if particle is restarted, or emitted without a custom position (i.e. this particle was created by <code>emit_subparticle()</code> without the <code>FLAG_EMIT_POSITION</code> flag)."
            ),

            ParameterSpec(
                name = "RESTART_ROT_SCALE",
                type = BOOL,
                description = "<code>true</code> if particle is restarted, or emitted without a custom rotation or scale (i.e. this particle was created by <code>emit_subparticle()</code> without the <code>FLAG_EMIT_ROT_SCALE</code> flag)."
            ),

            ParameterSpec(
                name = "RESTART_VELOCITY",
                type = BOOL,
                description = "<code>true</code> if particle is restarted, or emitted without a custom velocity (i.e. this particle was created by <code>emit_subparticle()</code> without the <code>FLAG_EMIT_VELOCITY</code> flag)."
            ),

            ParameterSpec(
                name = "RESTART_COLOR",
                type = BOOL,
                description = "<code>true</code> if particle is restarted, or emitted without a custom color (i.e. this particle was created by <code>emit_subparticle()</code> without the <code>FLAG_EMIT_COLOR</code> flag)."
            ),

            ParameterSpec(
                name = "RESTART_CUSTOM",
                type = BOOL,
                description = "<code>true</code> if particle is restarted, or emitted without a custom property (i.e. this particle was created by <code>emit_subparticle()</code> without the <code>FLAG_EMIT_CUSTOM</code> flag)."
            ),
        ),

        ShaderType.PARTICLES to FunctionContext.PROCESS to particlesStartAndProcessVariables + listOf(
            ParameterSpec(
                name = "RESTART",
                type = BOOL,
                description = "<code>true</code> if the current process frame is the first for the particle."
            ),

            ParameterSpec(
                name = "COLLIDED",
                type = BOOL,
                description = "<code>true</code> when the particle has collided with a particle collider."
            ),

            ParameterSpec(
                name = "COLLISION_NORMAL",
                type = BOOL,
                description = "A normal of the last collision. If there is no collision detected it is equal to <code>(0.0, 0.0, 0.0)</code>."
            ),

            ParameterSpec(
                name = "COLLISION_DEPTH",
                type = BOOL,
                description = "A length of the normal of the last collision. If there is no collision detected it is equal to <code>0.0</code>."
            ),

            ParameterSpec(
                name = "ATTRACTOR_FORCE",
                type = VEC3,
                description = "A combined force of the attractors at the moment on that particle."
            ),
        ),

        ShaderType.SKY to FunctionContext.COMMON to globalVariables + listOf(
            ParameterSpec(
                name = "POSITION",
                type = VEC3,
                description = "Camera position, in world space."
            ),

            ParameterSpec(
                name = "RADIANCE",
                type = SAMPLERCUBE,
                description = "Radiance cubemap. Can only be read from during the background pass. Check <code>!AT_CUBEMAP_PASS</code> before using."
            ),

            ParameterSpec(
                name = "AT_HALF_RES_PASS",
                type = BOOL,
                description = "<code>true</code> when rendering to the half resolution pass."
            ),

            ParameterSpec(
                name = "AT_QUARTER_RES_PASS",
                type = BOOL,
                description = "<code>true</code> when rendering to the quarter resolution pass."
            ),

            ParameterSpec(
                name = "AT_CUBEMAP_PASS",
                type = BOOL,
                description = "<code>true</code> when rendering to the radiance cubemap."
            ),

            ParameterSpec(
                name = "LIGHTX_ENABLED",
                type = BOOL,
                description = "<code>true</code> if <code>LIGHTX</code> is visible and in the scene. If <code>false</code>, other light properties may be garbage."
            ),

            ParameterSpec(
                name = "LIGHTX_ENERGY",
                type = FLOAT,
                description = "Energy multiplier for <code>LIGHTX</code>."
            ),

            ParameterSpec(
                name = "LIGHTX_DIRECTION",
                type = VEC3,
                description = "Direction that <code>LIGHTX</code> is facing."
            ),

            ParameterSpec(
                name = "LIGHTX_COLOR",
                type = VEC3,
                description = "Color of <code>LIGHTX</code>."
            ),

            ParameterSpec(
                name = "LIGHTX_SIZE",
                type = FLOAT,
                description = "Angular diameter of <code>LIGHTX</code> in the sky. Expressed in radians. For reference, the sun from earth is about .0087 radians (0.5 degrees)."
            ),
        ),
        
        ShaderType.SKY to FunctionContext.SKY to listOf(
            ParameterSpec(
                name = "EYEDIR",
                type = VEC3,
                description = "Normalized direction of the current pixel. Use this as your basic direction for procedural effects."
            ),

            ParameterSpec(
                name = "SCREEN_UV",
                type = VEC2,
                description = "Screen UV coordinate for the current pixel. Used to map a texture to the full screen."
            ),

            ParameterSpec(
                name = "SKY_COORDS",
                type = VEC2,
                description = "Sphere UV. Used to map a panorama texture to the sky."
            ),

            ParameterSpec(
                name = "HALF_RES_COLOR",
                type = VEC4,
                description = "Color value of the corresponding pixel from the half resolution pass. Uses linear filter."
            ),

            ParameterSpec(
                name = "QUARTER_RES_COLOR",
                type = VEC4,
                description = "Color value of the corresponding pixel from the quarter resolution pass. Uses linear filter."
            ),

            ParameterSpec(
                name = "COLOR",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Output color."
            ),

            ParameterSpec(
                name = "ALPHA",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Output alpha value, can only be used in subpasses."
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
                description = "Position of current froxel cell in world space."
            ),

            ParameterSpec(
                name = "OBJECT_POSITION",
                type = VEC3,
                description = "Position of the center of the current <a href=\"https://docs.godotengine.org/en/stable/classes/class_fogvolume.html#class-fogvolume\">FogVolume</a> in world space."
            ),

            ParameterSpec(
                name = "UVW",
                type = VEC3,
                description = "3-dimensional UV, used to map a 3D texture to the current <a href=\"https://docs.godotengine.org/en/stable/classes/class_fogvolume.html#class-fogvolume\">FogVolume</a>."
            ),

            ParameterSpec(
                name = "SIZE",
                type = VEC3,
                description = "Size of the current <a href=\"https://docs.godotengine.org/en/stable/classes/class_fogvolume.html#class-fogvolume\">FogVolume</a> when its <a href=\"https://docs.godotengine.org/en/stable/classes/class_fogvolume.html#class-fogvolume-property-shape\">shape</a> has a size."
            ),

            ParameterSpec(
                name = "SDF",
                type = VEC3,
                description = "Signed distance field to the surface of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_fogvolume.html#class-fogvolume\">FogVolume</a>. Negative if inside volume, positive otherwise."
            ),

            ParameterSpec(
                name = "ALBEDO",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Output base color value, interacts with light to produce final color. Only written to fog volume if used."
            ),

            ParameterSpec(
                name = "DENSITY",
                type = FLOAT,
                qualifier = ParameterQualifier.OUT,
                description = "Output density value. Can be negative to allow subtracting one volume from another. Density must be used for fog shader to write anything at all."
            ),

            ParameterSpec(
                name = "EMISSION",
                type = VEC3,
                qualifier = ParameterQualifier.OUT,
                description = "Output emission color value, added to color during light pass to produce final color. Only written to fog volume if used."
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
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Converts a quantity specified in degrees into radians, with the formula <code>degrees * (PI / 180)</code>.""",
                    params = mapOf(
                        "degrees" to "The quantity, in degrees, to be converted to radians."
                    ),
                    returns = "The input <code>degrees</code> converted to radians.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/radians.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "degrees",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("radians", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Converts a quantity specified in radians into degrees, with the formula <code>radians * (180 / PI)</code>""",
                    params = mapOf(
                        "radians" to "The quantity, in radians, to be converted to degrees."
                    ),
                    returns = "The input <code>radians</code> converted to degrees.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/degrees.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "sin",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("angle", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the trigonometric sine of <code>angle</code>.""",
                    params = mapOf(
                        "angle" to "The quantity, in radians, of which to return the sine."
                    ),
                    returns = "The sine of <code>angle</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/sin.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "cos",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("angle", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the trigonometric cosine of <code>angle</code>.""",
                    params = mapOf(
                        "angle" to "The quantity, in radians, of which to return the cosine."
                    ),
                    returns = "The cosine of <code>angle</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/cos.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "tan",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("angle", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the trigonometric tangent of <code>angle</code>.""",
                    params = mapOf(
                        "angle" to "The quantity, in radians, of which to return the tangent."
                    ),
                    returns = "The tangent of <code>angle</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/tan.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "asin",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Arc sine, or inverse sine. Calculates the angle whose sine is <code>x</code> and is in the range <code>[-PI/2, PI/2]</code>. The result is undefined if <code>x < -1</code> or <code>x > 1</code>.""",
                    params = mapOf(
                        "x" to "The value whose arc sine to return."
                    ),
                    returns = "The angle whose trigonometric sine is <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/asin.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "acos",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Arc cosine, or inverse cosine. Calculates the angle whose cosine is <code>x</code> and is in the range <code>[0, PI]</code>.<br>The result is undefined if <code>x < -1</code> or <code>x > 1</code>.""",
                    params = mapOf(
                        "x" to "The value whose arc cosine to return."
                    ),
                    returns = "The angle whose trigonometric cosine is <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/acos.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "atan",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("y_over_x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the arc tangent given a tangent value of <code>y/x</code>.<br><br><b>Note:</b> Because of the sign ambiguity, the function cannot determine with certainty in which quadrant the angle falls only by its tangent value. If you need to know the quadrant, use :ref:<i>atan(vec_type y, vec_type x)</i>.""",
                    params = mapOf(
                        "y_over_x" to "The fraction whose arc tangent to return."
                    ),
                    returns = "The trigonometric arc-tangent of <code>y_over_x</code> and is in the range <code>[-PI/2, PI/2]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/atan.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "atan",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("y", VEC_TYPE),
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the arc tangent given a numerator and denominator. The signs of <code>y</code> and <code>x</code> are used to determine the quadrant that the angle lies in. The result is undefined if <code>x == 0</code>.<br>Equivalent to :ref:<i>atan2()</i> in GDScript.""",
                    params = mapOf(
                        "y" to "The numerator of the fraction whose arc tangent to return.",
                        "x" to "The denominator of the fraction whose arc tangent to return."
                    ),
                    returns = "The trigonometric arc tangent of <code>y/x</code> and is in the range <code>[-PI, PI]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/atan.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "sinh",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the hyperbolic sine using <code>(e^x - e^-x)/2</code>.""",
                    params = mapOf(
                        "x" to "The value whose hyperbolic sine to return."
                    ),
                    returns = "The hyperbolic sine of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/sinh.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "cosh",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the hyperbolic cosine using <code>(e^x + e^-x)/2</code>.""",
                    params = mapOf(
                        "x" to "The value whose hyperbolic cosine to return."
                    ),
                    returns = "The hyperbolic cosine of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/cosh.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "tanh",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the hyperbolic tangent using <code>sinh(x)/cosh(x)</code>.""",
                    params = mapOf(
                        "x" to "The value whose hyperbolic tangent to return."
                    ),
                    returns = "The hyperbolic tangent of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/tanh.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "asinh",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the arc hyperbolic sine of <code>x</code>, or the inverse of <code>sinh</code>.""",
                    params = mapOf(
                        "x" to "The value whose arc hyperbolic sine to return."
                    ),
                    returns = "The arc hyperbolic sine of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/asinh.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "acosh",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the arc hyperbolic cosine of <code>x</code>, or the non-negative inverse of <code>cosh</code>. The result is undefined if <code>x < 1</code>.""",
                    params = mapOf(
                        "x" to "The value whose arc hyperbolic cosine to return."
                    ),
                    returns = "The arc hyperbolic cosine of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/acosh.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "atanh",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Calculates the arc hyperbolic tangent of <code>x</code>, or the inverse of <code>tanh</code>. The result is undefined if <code>abs(x) > 1</code>.""",
                    params = mapOf(
                        "x" to "The value whose arc hyperbolic tangent to return."
                    ),
                    returns = "The arc hyperbolic tangent of <code>x</code>.<br><br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/atanh.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "pow",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Raises <code>x</code> to the power of <code>y</code>.<br>The result is undefined if <code>x < 0</code> or  if <code>x == 0</code> and <code>y <= 0</code>.""",
                    params = mapOf(
                        "x" to "The value to be raised to the power <code>y</code>.",
                        "y" to "The power to which <code>x</code> will be raised."
                    ),
                    returns = "The value of <code>x</code> raised to the <code>y</code> power.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/pow.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "exp",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Raises <code>e</code> to the power of <code>x</code>, or the the natural exponentiation.<br>Equivalent to <code>pow(e, x)</code>.""",
                    params = mapOf(
                        "x" to "The value to exponentiate."
                    ),
                    returns = "The natural exponentiation of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/exp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "exp2",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Raises <code>2</code> to the power of <code>x</code>.<br>Equivalent to <code>pow(2.0, x)</code>.<br>""",
                    params = mapOf(
                        "x" to "The value of the power to which <code>2</code> will be raised."
                    ),
                    returns = "<code>2</code> raised to the power of x.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/exp2.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "log",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the natural logarithm of <code>x</code>, i.e. the value <code>y</code> which satisfies <code>x == pow(e, y)</code>. The result is undefined if <code>x <= 0</code>.""",
                    params = mapOf(
                        "x" to "The value of which to take the natural logarithm."
                    ),
                    returns = "The natural logarithm of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/log.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "log2",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the base-2 logarithm of <code>x</code>, i.e. the value <code>y</code> which satisfies <code>x == pow(2, y)</code>. The result is undefined if <code>x <= 0</code>.""",
                    params = mapOf(
                        "x" to "The value of which to take the base-2 logarithm."
                    ),
                    returns = "The base-2 logarithm of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/log2.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "sqrt",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the square root of <code>x</code>. The result is undefined if <code>x < 0</code>.""",
                    params = mapOf(
                        "x" to "The value of which to take the square root."
                    ),
                    returns = "The square root of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/sqrt.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "inversesqrt",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the inverse of the square root of <code>x</code>, or <code>1.0 / sqrt(x)</code>. The result is undefined if <code>x <= 0</code>.""",
                    params = mapOf(
                        "x" to "The value of which to take the inverse of the square root."
                    ),
                    returns = "The inverse of the square root of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/inversesqrt.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "abs",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the absolute value of <code>x</code>. Returns <code>x</code> if <code>x</code> is positive, otherwise returns <code>-1 * x</code>.""",
                    params = mapOf(
                        "x" to "The value of which to return the absolute."
                    ),
                    returns = "The absolute value of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/abs.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "abs",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the absolute value of <code>x</code>. Returns <code>x</code> if <code>x</code> is positive, otherwise returns <code>-1 * x</code>.""",
                    params = mapOf(
                        "x" to "The value of which to return the absolute."
                    ),
                    returns = "The absolute value of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/abs.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "sign",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns <code>-1</code> if <code>x < 0</code>, <code>0</code> if <code>x == 0</code>, and <code>1</code> if <code>x > 0</code>.""",
                    params = mapOf(
                        "x" to "The value from which to extract the sign."
                    ),
                    returns = "The sign of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/sign.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "sign",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns <code>-1</code> if <code>x < 0</code>, <code>0</code> if <code>x == 0</code>, and <code>1</code> if <code>x > 0</code>.""",
                    params = mapOf(
                        "x" to "The value from which to extract the sign."
                    ),
                    returns = "The sign of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/sign.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "floor",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns a value equal to the nearest integer that is less than or equal to <code>x</code>.""",
                    params = mapOf(
                        "x" to "The value to floor."
                    ),
                    returns = "The nearest integer that is less than or equal to <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/floor.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "round",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Rounds <code>x</code> to the nearest integer.<br><br><b>note:</b> Rounding of values with a fractional part of <code>0.5</code> is implementation-dependent. This includes the possibility that <code>round(x)</code> returns the same value as <code>roundEven(x)</code>for all values of <code>x</code>.""",
                    params = mapOf(
                        "x" to "The value to round."
                    ),
                    returns = "The rounded value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/round.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "roundEven",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Rounds <code>x</code> to the nearest integer. A value with a fractional part of <code>0.5</code> will always round toward the nearest even integer. For example, both <code>3.5</code> and <code>4.5</code> will round to <code>4.0</code>.""",
                    params = mapOf(
                        "x" to "The value to round."
                    ),
                    returns = "The rounded value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/roundEven.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "trunc",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Truncates <code>x</code>. Returns a value equal to the nearest integer to <code>x</code> whose absolute value is not larger than the absolute value of <code>x</code>.""",
                    params = mapOf(
                        "x" to "The value to evaluate."
                    ),
                    returns = "The truncated value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/trunc.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "ceil",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns a value equal to the nearest integer that is greater than or equal to <code>x</code>.""",
                    params = mapOf(
                        "x" to "The value to evaluate."
                    ),
                    returns = "The ceiling-ed value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/ceil.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "fract",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the fractional part of <code>x</code>.<br>This is calculated as <code>x - floor(x)</code>.""",
                    params = mapOf(
                        "x" to "The value to evaluate."
                    ),
                    returns = "The fractional part of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/fract.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "mod",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x modulo y</code>. This is also sometimes called the remainder.<br>This is computed as <code>x - y * floor(x/y)</code>.""",
                    params = mapOf(
                        "x" to "The value to evaluate.",
                        "y" to ""
                    ),
                    returns = "The value of <code>x modulo y</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/mod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "mod",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", FLOAT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x modulo y</code>. This is also sometimes called the remainder.<br>This is computed as <code>x - y * floor(x/y)</code>.""",
                    params = mapOf(
                        "x" to "The value to evaluate.",
                        "y" to ""
                    ),
                    returns = "The value of <code>x modulo y</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/mod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "modf",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("i", /* UNKNOWN: out vec_type */ VOID)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Separates a floating-point value <code>x</code> into its integer and fractional parts.<br>The fractional part of the number is returned from the function. The integer part (as a floating-point quantity) is returned in the output parameter <code>i</code>.""",
                    params = mapOf(
                        "x" to "The value to separate.<br>:param out i: A variable that receives the integer part of <code>x</code>.",
                        "i" to ""
                    ),
                    returns = "The fractional part of the number.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/modf.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "min",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the minimum of two values <code>a</code> and <code>b</code>.<br>Returns <code>b</code> if <code>b < a</code>, otherwise returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The minimum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/min.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "min",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", FLOAT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the minimum of two values <code>a</code> and <code>b</code>.<br>Returns <code>b</code> if <code>b < a</code>, otherwise returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The minimum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/min.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "min",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_INT_TYPE),
                    ParameterSpec("b", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the minimum of two values <code>a</code> and <code>b</code>.<br>Returns <code>b</code> if <code>b < a</code>, otherwise returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The minimum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/min.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "min",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_INT_TYPE),
                    ParameterSpec("b", INT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the minimum of two values <code>a</code> and <code>b</code>.<br>Returns <code>b</code> if <code>b < a</code>, otherwise returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The minimum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/min.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "min",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_UINT_TYPE),
                    ParameterSpec("b", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the minimum of two values <code>a</code> and <code>b</code>.<br>Returns <code>b</code> if <code>b < a</code>, otherwise returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The minimum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/min.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "min",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_UINT_TYPE),
                    ParameterSpec("b", UINT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the minimum of two values <code>a</code> and <code>b</code>.<br>Returns <code>b</code> if <code>b < a</code>, otherwise returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The minimum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/min.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "max",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the maximum of two values <code>a</code> and <code>b</code>.<br>It returns <code>b</code> if <code>b > a</code>, otherwise it returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The maximum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/max.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "max",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", FLOAT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the maximum of two values <code>a</code> and <code>b</code>.<br>It returns <code>b</code> if <code>b > a</code>, otherwise it returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The maximum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/max.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "max",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_UINT_TYPE),
                    ParameterSpec("b", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the maximum of two values <code>a</code> and <code>b</code>.<br>It returns <code>b</code> if <code>b > a</code>, otherwise it returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The maximum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/max.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "max",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_UINT_TYPE),
                    ParameterSpec("b", UINT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the maximum of two values <code>a</code> and <code>b</code>.<br>It returns <code>b</code> if <code>b > a</code>, otherwise it returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The maximum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/max.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "max",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_INT_TYPE),
                    ParameterSpec("b", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the maximum of two values <code>a</code> and <code>b</code>.<br>It returns <code>b</code> if <code>b > a</code>, otherwise it returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The maximum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/max.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "max",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_INT_TYPE),
                    ParameterSpec("b", INT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the maximum of two values <code>a</code> and <code>b</code>.<br>It returns <code>b</code> if <code>b > a</code>, otherwise it returns <code>a</code>.""",
                    params = mapOf(
                        "a" to "The first value to compare.",
                        "b" to "The second value to compare."
                    ),
                    returns = "The maximum value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/max.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "clamp",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("minVal", VEC_TYPE),
                    ParameterSpec("maxVal", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x</code> constrained to the range <code>minVal</code> to <code>maxVal</code>.<br>The returned value is computed as <code>min(max(x, minVal), maxVal)</code>.""",
                    params = mapOf(
                        "x" to "The value to constrain.",
                        "minVal" to "The lower end of the range into which to constrain <code>x</code>.",
                        "maxVal" to "The upper end of the range into which to constrain <code>x</code>."
                    ),
                    returns = "The clamped value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/clamp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "clamp",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("minVal", FLOAT),
                    ParameterSpec("maxVal", FLOAT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x</code> constrained to the range <code>minVal</code> to <code>maxVal</code>.<br>The returned value is computed as <code>min(max(x, minVal), maxVal)</code>.""",
                    params = mapOf(
                        "x" to "The value to constrain.",
                        "minVal" to "The lower end of the range into which to constrain <code>x</code>.",
                        "maxVal" to "The upper end of the range into which to constrain <code>x</code>."
                    ),
                    returns = "The clamped value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/clamp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "clamp",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_INT_TYPE),
                    ParameterSpec("minVal", VEC_INT_TYPE),
                    ParameterSpec("maxVal", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x</code> constrained to the range <code>minVal</code> to <code>maxVal</code>.<br>The returned value is computed as <code>min(max(x, minVal), maxVal)</code>.""",
                    params = mapOf(
                        "x" to "The value to constrain.",
                        "minVal" to "The lower end of the range into which to constrain <code>x</code>.",
                        "maxVal" to "The upper end of the range into which to constrain <code>x</code>."
                    ),
                    returns = "The clamped value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/clamp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "clamp",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_INT_TYPE),
                    ParameterSpec("minVal", INT),
                    ParameterSpec("maxVal", INT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x</code> constrained to the range <code>minVal</code> to <code>maxVal</code>.<br>The returned value is computed as <code>min(max(x, minVal), maxVal)</code>.""",
                    params = mapOf(
                        "x" to "The value to constrain.",
                        "minVal" to "The lower end of the range into which to constrain <code>x</code>.",
                        "maxVal" to "The upper end of the range into which to constrain <code>x</code>."
                    ),
                    returns = "The clamped value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/clamp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "clamp",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_UINT_TYPE),
                    ParameterSpec("minVal", VEC_UINT_TYPE),
                    ParameterSpec("maxVal", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x</code> constrained to the range <code>minVal</code> to <code>maxVal</code>.<br>The returned value is computed as <code>min(max(x, minVal), maxVal)</code>.""",
                    params = mapOf(
                        "x" to "The value to constrain.",
                        "minVal" to "The lower end of the range into which to constrain <code>x</code>.",
                        "maxVal" to "The upper end of the range into which to constrain <code>x</code>."
                    ),
                    returns = "The clamped value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/clamp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "clamp",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_UINT_TYPE),
                    ParameterSpec("minVal", UINT),
                    ParameterSpec("maxVal", UINT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the value of <code>x</code> constrained to the range <code>minVal</code> to <code>maxVal</code>.<br>The returned value is computed as <code>min(max(x, minVal), maxVal)</code>.""",
                    params = mapOf(
                        "x" to "The value to constrain.",
                        "minVal" to "The lower end of the range into which to constrain <code>x</code>.",
                        "maxVal" to "The upper end of the range into which to constrain <code>x</code>."
                    ),
                    returns = "The clamped value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/clamp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "mix",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE),
                    ParameterSpec("c", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Performs a linear interpolation between <code>a</code> and <code>b</code> using <code>c</code> to weight between them.<br>Computed as <code>a * (1 - c) + b * c</code>.<br>Equivalent to :ref:<i>lerp()</i> in GDScript.""",
                    params = mapOf(
                        "a" to "The start of the range in which to interpolate.",
                        "b" to "The end of the range in which to interpolate.",
                        "c" to "The value to use to interpolate between <code>a</code> and <code>b</code>."
                    ),
                    returns = "The interpolated value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/mix.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "mix",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE),
                    ParameterSpec("c", FLOAT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Performs a linear interpolation between <code>a</code> and <code>b</code> using <code>c</code> to weight between them.<br>Computed as <code>a * (1 - c) + b * c</code>.<br>Equivalent to :ref:<i>lerp()</i> in GDScript.""",
                    params = mapOf(
                        "a" to "The start of the range in which to interpolate.",
                        "b" to "The end of the range in which to interpolate.",
                        "c" to "The value to use to interpolate between <code>a</code> and <code>b</code>."
                    ),
                    returns = "The interpolated value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/mix.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "mix",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE),
                    ParameterSpec("c", VEC_BOOL_TYPE)
                ),
                description = functionDoc(
                    description = """Selects either value <code>a</code> or value <code>b</code> based on the value of <code>c</code>. For a component of <code>c</code> that is false, the corresponding component of <code>a</code> is returned. For a component of <code>c</code> that is true, the corresponding component of <code>b</code> is returned. Components of <code>a</code> and <code>b</code> that are not selected are allowed to be invalid floating-point values and will have no effect on the results.<br>If <code>a</code>, <code>b</code>, and <code>c</code> are vector types the operation is performed :ref:<i>component-wise</i>. ie. <code>mix(vec2(42, 314), vec2(9.8, 6e23), bvec2(true, false)))</code> will return <code>vec2(9.8, 314)</code>.""",
                    params = mapOf(
                        "a" to "Value returned when <code>c</code> is false.",
                        "b" to "Value returned when <code>c</code> is true.",
                        "c" to "The value used to select between <code>a</code> and <code>b</code>."
                    ),
                    returns = "The interpolated value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/mix.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "fma",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE),
                    ParameterSpec("c", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Performs, where possible, a fused multiply-add operation, returning <code>a * b + c</code>. In use cases where the return value is eventually consumed by a variable declared as precise:<br>- <code>fma()</code> is considered a single operation, whereas the expression <code>a * b + c</code> consumed by a variable declared as precise is considered two operations.<br>- The precision of <code>fma()</code> can differ from the precision of the expression <code>a * b + c</code>.<br>- <code>fma()</code> will be computed with the same precision as any other <code>fma()</code> consumed by a precise variable, giving invariant results for the same input values of a, b and c.<br>Otherwise, in the absence of precise consumption, there are no special constraints on the number of operations or difference in precision between <code>fma()</code> and the expression <code>a * b + c</code>.""",
                    params = mapOf(
                        "a" to "The first value to be multiplied.",
                        "b" to "The second value to be multiplied.",
                        "c" to "The value to be added to the result."
                    ),
                    returns = "The value of <code>a * b + c</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/fma.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "step",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Generates a step function by comparing b to a.<br>Equivalent to <code>if (b < a) { return 0.0; } else { return 1.0; }</code>. For element i of the return value, 0.0 is returned if b[i] < a[i], and 1.0 is returned otherwise.""",
                    params = mapOf(
                        "a" to "The location of the edge of the step function.",
                        "b" to "The value to be used to generate the step function."
                    ),
                    returns = "<code>0.0</code> or <code>1.0</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/step.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "step",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", FLOAT),
                    ParameterSpec("b", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Generates a step function by comparing b to a.<br>Equivalent to <code>if (b < a) { return 0.0; } else { return 1.0; }</code>. For element i of the return value, 0.0 is returned if b[i] < a[i], and 1.0 is returned otherwise.""",
                    params = mapOf(
                        "a" to "The location of the edge of the step function.",
                        "b" to "The value to be used to generate the step function."
                    ),
                    returns = "<code>0.0</code> or <code>1.0</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/step.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "smoothstep",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE),
                    ParameterSpec("c", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Performs smooth Hermite interpolation between <code>0</code> and <code>1</code> when a < c < b. This is useful in cases where a threshold function with a smooth transition is desired.<br>Smoothstep is equivalent to:<br><pre><code>
    vec_type t;
    t = clamp((c - a) / (b - a), 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
    </code></pre> Results are undefined if <code>a >= b</code>.""",
                    params = mapOf(
                        "a" to "The value of the lower edge of the Hermite function.",
                        "b" to "The value of the upper edge of the Hermite function.",
                        "c" to "The source value for interpolation."
                    ),
                    returns = "The interpolated value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/smoothstep.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "smoothstep",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("a", FLOAT),
                    ParameterSpec("b", FLOAT),
                    ParameterSpec("c", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Performs smooth Hermite interpolation between <code>0</code> and <code>1</code> when a < c < b. This is useful in cases where a threshold function with a smooth transition is desired.<br>Smoothstep is equivalent to:<br><pre><code>
    vec_type t;
    t = clamp((c - a) / (b - a), 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
    </code></pre> Results are undefined if <code>a >= b</code>.""",
                    params = mapOf(
                        "a" to "The value of the lower edge of the Hermite function.",
                        "b" to "The value of the upper edge of the Hermite function.",
                        "c" to "The source value for interpolation."
                    ),
                    returns = "The interpolated value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/smoothstep.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "isnan",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>For each element i of the result, returns <code>true</code> if x[i] is positive or negative floating-point NaN (Not a Number) and false otherwise.""",
                    params = mapOf(
                        "x" to "The value to test for NaN."
                    ),
                    returns = "<code>true</code> or <code>false</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/isnan.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "isinf",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>For each element i of the result, returns <code>true</code> if x[i] is positive or negative floating-point infinity and false otherwise.""",
                    params = mapOf(
                        "x" to "The value to test for infinity."
                    ),
                    returns = "<code>true</code> or <code>false</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/isinf.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "floatBitsToInt",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the encoding of the floating-point parameters as <code>int</code>.<br>The floating-point bit-level representation is preserved.""",
                    params = mapOf(
                        "x" to "The value whose floating-point encoding to return."
                    ),
                    returns = "The floating-point encoding of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/floatBitsToInt.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "floatBitsToUint",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Returns the encoding of the floating-point parameters as <code>uint</code>.<br>The floating-point bit-level representation is preserved.""",
                    params = mapOf(
                        "x" to "The value whose floating-point encoding to return."
                    ),
                    returns = "The floating-point encoding of <code>x</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/floatBitsToInt.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "intBitsToFloat",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Converts a bit encoding to a floating-point value. Opposite of <i>floatBitsToInt</i><br>If the encoding of a <code>NaN</code> is passed in <code>x</code>, it will not signal and the resulting value will be undefined.<br>If the encoding of a floating-point infinity is passed in parameter <code>x</code>, the resulting floating-point value is the corresponding (positive or negative) floating-point infinity.""",
                    params = mapOf(
                        "x" to "The bit encoding to return as a floating-point value."
                    ),
                    returns = "A floating-point value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/intBitsToFloat.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "uintBitsToFloat",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Converts a bit encoding to a floating-point value. Opposite of <i>floatBitsToUint</i><br>If the encoding of a <code>NaN</code> is passed in <code>x</code>, it will not signal and the resulting value will be undefined.<br>If the encoding of a floating-point infinity is passed in parameter <code>x</code>, the resulting floating-point value is the corresponding (positive or negative) floating-point infinity.""",
                    params = mapOf(
                        "x" to "The bit encoding to return as a floating-point value."
                    ),
                    returns = "A floating-point value.<br><br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/intBitsToFloat.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "length",
                returnType = FLOAT,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Returns the length of the vector. ie. <code>sqrt(x[0] * x[0] + x[1] * x[1] + ... + x[n] * x[n])</code>""",
                    params = mapOf(
                        "x" to "The vector"
                    ),
                    returns = "The length of the vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/length.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "distance",
                returnType = FLOAT,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Returns the distance between the two points a and b.<br>i.e., <code>length(b - a);</code>""",
                    params = mapOf(
                        "a" to "The first point.",
                        "b" to "The second point."
                    ),
                    returns = "The scalar distance between the points<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/distance.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "dot",
                returnType = FLOAT,
                parameters = listOf(
                    ParameterSpec("a", VEC_TYPE),
                    ParameterSpec("b", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Returns the dot product of two vectors, <code>a</code> and <code>b</code>. i.e., <code>a.x * b.x + a.y * b.y + ...</code>""",
                    params = mapOf(
                        "a" to "The first vector.",
                        "b" to "The second vector."
                    ),
                    returns = "The dot product.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/dot.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "cross",
                returnType = VEC3,
                parameters = listOf(
                    ParameterSpec("a", VEC3),
                    ParameterSpec("b", VEC3)
                ),
                description = functionDoc(
                    description = """Returns the cross product of two vectors. i.e.:<br><pre><code>
    vec2( a.y * b.z - b.y * a.z,
    a.z * b.x - b.z * a.x,
    a.x * b.z - b.x * a.y)
    </code></pre>""",
                    params = mapOf(
                        "a" to "The first vector.",
                        "b" to "The second vector."
                    ),
                    returns = "The cross product of <code>a</code> and <code>b</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/cross.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "normalize",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Returns a vector with the same direction as <code>x</code> but with length <code>1.0</code>.""",
                    params = mapOf(
                        "x" to "The vector to normalize."
                    ),
                    returns = "The normalized vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/normalize.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "reflect",
                returnType = VEC3,
                parameters = listOf(
                    ParameterSpec("I", VEC3),
                    ParameterSpec("N", VEC3)
                ),
                description = functionDoc(
                    description = """Calculate the reflection direction for an incident vector.<br>For a given incident vector <code>I</code> and surface normal <code>N</code> reflect returns the reflection direction calculated as <code>I - 2.0 * dot(N, I) * N</code>.<br><br><b>Note:</b> <code>N</code> should be normalized in order to achieve the desired result.""",
                    params = mapOf(
                        "I" to "The incident vector.",
                        "N" to "The normal vector."
                    ),
                    returns = "The reflection vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/reflect.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "refract",
                returnType = VEC3,
                parameters = listOf(
                    ParameterSpec("I", VEC3),
                    ParameterSpec("N", VEC3),
                    ParameterSpec("eta", FLOAT)
                ),
                description = functionDoc(
                    description = """Calculate the refraction direction for an incident vector.<br>For a given incident vector <code>I</code>, surface normal <code>N</code> and ratio of indices of refraction, <code>eta</code>, refract returns the refraction vector, <code>R</code>.<br><code>R</code> is calculated as:<br><pre><code>
    k = 1.0 - eta * eta * (1.0 - dot(N, I) * dot(N, I));
    if (k < 0.0)
    R = genType(0.0);       // or genDType(0.0)
    else
    R = eta * I - (eta * dot(N, I) + sqrt(k)) * N;
    </code></pre> <br><b>Note:</b> The input parameters I and N should be normalized in order to achieve the desired result.""",
                    params = mapOf(
                        "I" to "The incident vector.",
                        "N" to "The normal vector.",
                        "eta" to "The ratio of indices of refraction."
                    ),
                    returns = "The refraction vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/refract.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "faceforward",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("N", VEC_TYPE),
                    ParameterSpec("I", VEC_TYPE),
                    ParameterSpec("Nref", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Returns a vector pointing in the same direction as another.<br>Orients a vector to point away from a surface as defined by its normal. If <code>dot(Nref, I) < 0</code> faceforward returns <code>N</code>, otherwise it returns <code>-N</code>.""",
                    params = mapOf(
                        "N" to "The vector to orient.",
                        "I" to "The incident vector.",
                        "Nref" to "The reference vector."
                    ),
                    returns = "The oriented vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/faceforward.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "matrixCompMult",
                returnType = MAT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", MAT_TYPE),
                    ParameterSpec("y", MAT_TYPE)
                ),
                description = functionDoc(
                    description = """Perform a :ref:<i>component-wise</i> multiplication of two matrices.<br>Performs a component-wise multiplication of two matrices, yielding a result matrix where each component, <code>result[i][j]</code> is computed as the scalar product of <code>x[i][j]</code> and <code>y[i][j]</code>.""",
                    params = mapOf(
                        "x" to "The first matrix multiplicand.",
                        "y" to "The second matrix multiplicand."
                    ),
                    returns = "The resultant matrix.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/matrixCompMult.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "outerProduct",
                returnType = MAT_TYPE,
                parameters = listOf(
                    ParameterSpec("column", VEC_TYPE),
                    ParameterSpec("row", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Calculate the outer product of a pair of vectors.<br>Does a linear algebraic matrix multiply <code>column * row</code>, yielding a matrix whose number of rows is the number of components in <code>column</code> and whose number of columns is the number of components in <code>row</code>.""",
                    params = mapOf(
                        "column" to "The column vector for multiplication.",
                        "row" to "The row vector for multiplication."
                    ),
                    returns = "The outer product matrix.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/outerProduct.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "transpose",
                returnType = MAT_TYPE,
                parameters = listOf(
                    ParameterSpec("m", MAT_TYPE)
                ),
                description = functionDoc(
                    description = """Calculate the transpose of a matrix.""",
                    params = mapOf(
                        "m" to "The matrix to transpose."
                    ),
                    returns = "A new matrix that is the transpose of the input matrix <code>m</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/transpose.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "determinant",
                returnType = FLOAT,
                parameters = listOf(
                    ParameterSpec("m", MAT_TYPE)
                ),
                description = functionDoc(
                    description = """Calculate the determinant of a matrix.""",
                    params = mapOf(
                        "m" to "The matrix."
                    ),
                    returns = "The determinant of the input matrix <code>m</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/determinant.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "inverse",
                returnType = MAT_TYPE,
                parameters = listOf(
                    ParameterSpec("m", MAT_TYPE)
                ),
                description = functionDoc(
                    description = """Calculate the inverse of a matrix.<br>The values in the returned matrix are undefined if <code>m</code> is singular or poorly-conditioned (nearly singular).""",
                    params = mapOf(
                        "m" to "The matrix of which to take the inverse."
                    ),
                    returns = "A new matrix which is the inverse of the input matrix <code>m</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/inverse.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "lessThan",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Performs a :ref:<i>component-wise</i> less-than comparison of two vectors.""",
                    params = mapOf(
                        "x" to "The first vector to compare.",
                        "y" to "The second vector to compare."
                    ),
                    returns = "A boolean vector in which each element <code>i</code> is computed as <code>x[i] < y[i]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/lessThan.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "greaterThan",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Performs a :ref:<i>component-wise</i> greater-than comparison of two vectors.""",
                    params = mapOf(
                        "x" to "The first vector to compare.",
                        "y" to "The second vector to compare."
                    ),
                    returns = "A boolean vector in which each element <code>i</code> is computed as <code>x[i] > y[i]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/greaterThan.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "lessThanEqual",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Performs a :ref:<i>component-wise</i> less-than-or-equal comparison of two vectors.""",
                    params = mapOf(
                        "x" to "The first vector to compare.",
                        "y" to "The second vector to compare."
                    ),
                    returns = "A boolean vector in which each element <code>i</code> is computed as <code>x[i] <= y[i]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/lessThanEqual.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "greaterThanEqual",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Performs a :ref:<i>component-wise</i> greater-than-or-equal comparison of two vectors.""",
                    params = mapOf(
                        "x" to "The first vector to compare.",
                        "y" to "The second vector to compare."
                    ),
                    returns = "A boolean vector in which each element <code>i</code> is computed as <code>x[i] >= y[i]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/greaterThanEqual.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "equal",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Performs a :ref:<i>component-wise</i> equal-to comparison of two vectors.""",
                    params = mapOf(
                        "x" to "The first vector to compare.",
                        "y" to "The second vector to compare."
                    ),
                    returns = "A boolean vector in which each element <code>i</code> is computed as <code>x[i] == y[i]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/equal.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "notEqual",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("y", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Performs a :ref:<i>component-wise</i> not-equal-to comparison of two vectors.""",
                    params = mapOf(
                        "x" to "The first vector for comparison.",
                        "y" to "The second vector for comparison."
                    ),
                    returns = "A boolean vector in which each element <code>i</code> is computed as <code>x[i] != y[i]</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/notEqual.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "any",
                returnType = BOOL,
                parameters = listOf(
                    ParameterSpec("x", VEC_BOOL_TYPE)
                ),
                description = functionDoc(
                    description = """Returns <code>true</code> if any element of a boolean vector is <code>true</code>, <code>false</code> otherwise.<br>Functionally equivalent to:<br><pre><code>
    bool any(bvec x) {     // bvec can be bvec2, bvec3 or bvec4
    bool result = false;
    int i;
    for (i = 0; i < x.length(); ++i) {
    result |= x[i];
    }
    return result;
    }
    </code></pre>""",
                    params = mapOf(
                        "x" to "The vector to be tested for truth."
                    ),
                    returns = "True if any element of x is true and false otherwise.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/any.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "all",
                returnType = BOOL,
                parameters = listOf(
                    ParameterSpec("x", VEC_BOOL_TYPE)
                ),
                description = functionDoc(
                    description = """Returns <code>true</code> if all elements of a boolean vector are <code>true</code>, <code>false</code> otherwise.<br>Functionally equivalent to:<br><pre><code>
    bool all(bvec x)       // bvec can be bvec2, bvec3 or bvec4
    {
    bool result = true;
    int i;
    for (i = 0; i < x.length(); ++i)
    {
    result &= x[i];
    }
    return result;
    }
    </code></pre>""",
                    params = mapOf(
                        "x" to "The vector to be tested for truth."
                    ),
                    returns = "<code>true</code> if all elements of <code>x</code> are <code>true</code> and <code>false</code> otherwise.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/all.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "not",
                returnType = VEC_BOOL_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_BOOL_TYPE)
                ),
                description = functionDoc(
                    description = """Logically invert a boolean vector.""",
                    params = mapOf(
                        "x" to "The vector to be inverted."
                    ),
                    returns = "A new boolean vector for which each element i is computed as !x[i].<br><br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/not.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureSize",
                returnType = IVEC2,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Retrieves the dimensions of a level of a texture.<br>Returns the dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>The components in the return value are filled in, in order, with the width, height and depth of the texture. For the array forms, the last component of the return value is the number of layers in the texture array.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose dimensions to retrieve is bound.",
                        "lod" to "The level of the texture for which to retrieve the dimensions."
                    ),
                    returns = "The dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureSize.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureSize",
                returnType = IVEC2,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBE),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Retrieves the dimensions of a level of a texture.<br>Returns the dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>The components in the return value are filled in, in order, with the width, height and depth of the texture. For the array forms, the last component of the return value is the number of layers in the texture array.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose dimensions to retrieve is bound.",
                        "lod" to "The level of the texture for which to retrieve the dimensions."
                    ),
                    returns = "The dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureSize.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureSize",
                returnType = IVEC2,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBEARRAY),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Retrieves the dimensions of a level of a texture.<br>Returns the dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>The components in the return value are filled in, in order, with the width, height and depth of the texture. For the array forms, the last component of the return value is the number of layers in the texture array.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose dimensions to retrieve is bound.",
                        "lod" to "The level of the texture for which to retrieve the dimensions."
                    ),
                    returns = "The dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureSize.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureSize",
                returnType = IVEC3,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2DARRAY),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Retrieves the dimensions of a level of a texture.<br>Returns the dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>The components in the return value are filled in, in order, with the width, height and depth of the texture. For the array forms, the last component of the return value is the number of layers in the texture array.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose dimensions to retrieve is bound.",
                        "lod" to "The level of the texture for which to retrieve the dimensions."
                    ),
                    returns = "The dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureSize.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureSize",
                returnType = IVEC3,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Retrieves the dimensions of a level of a texture.<br>Returns the dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>The components in the return value are filled in, in order, with the width, height and depth of the texture. For the array forms, the last component of the return value is the number of layers in the texture array.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose dimensions to retrieve is bound.",
                        "lod" to "The level of the texture for which to retrieve the dimensions."
                    ),
                    returns = "The dimensions of level <code>lod</code> (if present) of the texture bound to sampler.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureSize.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureQueryLevels",
                returnType = INT,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D)
                ),
                description = functionDoc(
                    description = """Compute the number of accessible mipmap levels of a texture.<br>If called on an incomplete texture, or if no texture is associated with sampler, <code>0</code> is returned.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose mipmap level count will be queried is bound."
                    ),
                    returns = "The number of accessible mipmap levels in the texture, or <code>0</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLevels.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureQueryLevels",
                returnType = INT,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2DARRAY)
                ),
                description = functionDoc(
                    description = """Compute the number of accessible mipmap levels of a texture.<br>If called on an incomplete texture, or if no texture is associated with sampler, <code>0</code> is returned.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose mipmap level count will be queried is bound."
                    ),
                    returns = "The number of accessible mipmap levels in the texture, or <code>0</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLevels.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureQueryLevels",
                returnType = INT,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D)
                ),
                description = functionDoc(
                    description = """Compute the number of accessible mipmap levels of a texture.<br>If called on an incomplete texture, or if no texture is associated with sampler, <code>0</code> is returned.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose mipmap level count will be queried is bound."
                    ),
                    returns = "The number of accessible mipmap levels in the texture, or <code>0</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLevels.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureQueryLevels",
                returnType = INT,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBE)
                ),
                description = functionDoc(
                    description = """Compute the number of accessible mipmap levels of a texture.<br>If called on an incomplete texture, or if no texture is associated with sampler, <code>0</code> is returned.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture whose mipmap level count will be queried is bound."
                    ),
                    returns = "The number of accessible mipmap levels in the texture, or <code>0</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLevels.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texture",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC2),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Retrieves texels from a texture.<br>Samples texels from the texture bound to <code>s</code> at texture coordinate <code>p</code>. An optional bias, specified in <code>bias</code> is included in the level-of-detail computation that is used to choose mipmap(s) from which to sample.<br>For shadow forms, the last component of <code>p</code> is used as Dsub and the array layer is specified in the second to last component of <code>p</code>. (The second component of <code>p</code> is unused for 1D shadow lookups.)<br>For non-shadow variants, the array layer comes from the last component of P.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "An optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texture.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texture",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2DARRAY),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Retrieves texels from a texture.<br>Samples texels from the texture bound to <code>s</code> at texture coordinate <code>p</code>. An optional bias, specified in <code>bias</code> is included in the level-of-detail computation that is used to choose mipmap(s) from which to sample.<br>For shadow forms, the last component of <code>p</code> is used as Dsub and the array layer is specified in the second to last component of <code>p</code>. (The second component of <code>p</code> is unused for 1D shadow lookups.)<br>For non-shadow variants, the array layer comes from the last component of P.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "An optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texture.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texture",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Retrieves texels from a texture.<br>Samples texels from the texture bound to <code>s</code> at texture coordinate <code>p</code>. An optional bias, specified in <code>bias</code> is included in the level-of-detail computation that is used to choose mipmap(s) from which to sample.<br>For shadow forms, the last component of <code>p</code> is used as Dsub and the array layer is specified in the second to last component of <code>p</code>. (The second component of <code>p</code> is unused for 1D shadow lookups.)<br>For non-shadow variants, the array layer comes from the last component of P.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "An optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texture.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texture",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBE),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Retrieves texels from a texture.<br>Samples texels from the texture bound to <code>s</code> at texture coordinate <code>p</code>. An optional bias, specified in <code>bias</code> is included in the level-of-detail computation that is used to choose mipmap(s) from which to sample.<br>For shadow forms, the last component of <code>p</code> is used as Dsub and the array layer is specified in the second to last component of <code>p</code>. (The second component of <code>p</code> is unused for 1D shadow lookups.)<br>For non-shadow variants, the array layer comes from the last component of P.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "An optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texture.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texture",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBEARRAY),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Retrieves texels from a texture.<br>Samples texels from the texture bound to <code>s</code> at texture coordinate <code>p</code>. An optional bias, specified in <code>bias</code> is included in the level-of-detail computation that is used to choose mipmap(s) from which to sample.<br>For shadow forms, the last component of <code>p</code> is used as Dsub and the array layer is specified in the second to last component of <code>p</code>. (The second component of <code>p</code> is unused for 1D shadow lookups.)<br>For non-shadow variants, the array layer comes from the last component of P.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "An optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texture.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texture",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLEREXTERNALOES),
                    ParameterSpec("p", VEC2),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Retrieves texels from a texture.<br>Samples texels from the texture bound to <code>s</code> at texture coordinate <code>p</code>. An optional bias, specified in <code>bias</code> is included in the level-of-detail computation that is used to choose mipmap(s) from which to sample.<br>For shadow forms, the last component of <code>p</code> is used as Dsub and the array layer is specified in the second to last component of <code>p</code>. (The second component of <code>p</code> is unused for 1D shadow lookups.)<br>For non-shadow variants, the array layer comes from the last component of P.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "An optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texture.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProj",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Perform a texture lookup with projection.<br>The texture coordinates consumed from <code>p</code>, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. The resulting 3rd component of <code>p</code> in the shadow forms is used as Dref. After these values are computed, the texture lookup proceeds as in texture.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "Optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProj.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProj",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Perform a texture lookup with projection.<br>The texture coordinates consumed from <code>p</code>, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. The resulting 3rd component of <code>p</code> in the shadow forms is used as Dref. After these values are computed, the texture lookup proceeds as in texture.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "Optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProj.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProj",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("bias", FLOAT)
                ),
                description = functionDoc(
                    description = """Perform a texture lookup with projection.<br>The texture coordinates consumed from <code>p</code>, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. The resulting 3rd component of <code>p</code> in the shadow forms is used as Dref. After these values are computed, the texture lookup proceeds as in texture.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "bias" to "Optional bias to be applied during level-of-detail computation."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProj.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureLod",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC2),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with an explicit level-of-detail as specified in <code>lod</code>. <code>lod</code> specifies λbase and sets the partial derivatives as follows:<br><pre><code>
    δu/δx=0, δv/δx=0, δw/δx=0
    δu/δy=0, δv/δy=0, δw/δy=0
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureLod",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2DARRAY),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with an explicit level-of-detail as specified in <code>lod</code>. <code>lod</code> specifies λbase and sets the partial derivatives as follows:<br><pre><code>
    δu/δx=0, δv/δx=0, δw/δx=0
    δu/δy=0, δv/δy=0, δw/δy=0
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureLod",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with an explicit level-of-detail as specified in <code>lod</code>. <code>lod</code> specifies λbase and sets the partial derivatives as follows:<br><pre><code>
    δu/δx=0, δv/δx=0, δw/δx=0
    δu/δy=0, δv/δy=0, δw/δy=0
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureLod",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBE),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with an explicit level-of-detail as specified in <code>lod</code>. <code>lod</code> specifies λbase and sets the partial derivatives as follows:<br><pre><code>
    δu/δx=0, δv/δx=0, δw/δx=0
    δu/δy=0, δv/δy=0, δw/δy=0
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureLod",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBEARRAY),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with an explicit level-of-detail as specified in <code>lod</code>. <code>lod</code> specifies λbase and sets the partial derivatives as follows:<br><pre><code>
    δu/δx=0, δv/δx=0, δw/δx=0
    δu/δy=0, δv/δy=0, δw/δy=0
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProjLod",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup with projection from an explicitly specified level-of-detail.<br>The texture coordinates consumed from P, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. The resulting 3rd component of <code>p</code> in the shadow forms is used as Dref. After these values are computed, the texture lookup proceeds as in <i>textureLod</i>, with <code>lod</code> used to specify the level-of-detail from which the texture will be sampled.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail from which to fetch texels."
                    ),
                    returns = "a texel<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProjLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProjLod",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup with projection from an explicitly specified level-of-detail.<br>The texture coordinates consumed from P, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. The resulting 3rd component of <code>p</code> in the shadow forms is used as Dref. After these values are computed, the texture lookup proceeds as in <i>textureLod</i>, with <code>lod</code> used to specify the level-of-detail from which the texture will be sampled.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail from which to fetch texels."
                    ),
                    returns = "a texel<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProjLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProjLod",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("lod", FLOAT)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup with projection from an explicitly specified level-of-detail.<br>The texture coordinates consumed from P, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. The resulting 3rd component of <code>p</code> in the shadow forms is used as Dref. After these values are computed, the texture lookup proceeds as in <i>textureLod</i>, with <code>lod</code> used to specify the level-of-detail from which the texture will be sampled.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "The explicit level-of-detail from which to fetch texels."
                    ),
                    returns = "a texel<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProjLod.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGrad",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC2),
                    ParameterSpec("dPdx", VEC2),
                    ParameterSpec("dPdy", VEC2)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with explicit texture coordinate gradiends as specified in <code>dPdx</code> and <code>dPdy</code>. Set: - <code>δs/δx=δp/δx</code> for a 1D texture, <code>δp.s/δx</code> otherwise - <code>δs/δy=δp/δy</code> for a 1D texture, <code>δp.s/δy</code> otherwise - <code>δt/δx=0.0</code> for a 1D texture, <code>δp.t/δx</code> otherwise - <code>δt/δy=0.0</code> for a 1D texture, <code>δp.t/δy</code> otherwise - <code>δr/δx=0.0</code> for a 1D or 2D texture, <code>δp.p/δx</code> otherwise - <code>δr/δy=0.0</code>  for a 1D or 2D texture, <code>δp.p/δy</code> otherwise<br>For the cube version, the partial derivatives of <code>p</code> are assumed to be in the coordinate system used before texture coordinates are projected onto the appropriate cube face.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of P with respect to window x.",
                        "dPdy" to "The partial derivative of P with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGrad",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2DARRAY),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("dPdx", VEC2),
                    ParameterSpec("dPdy", VEC2)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with explicit texture coordinate gradiends as specified in <code>dPdx</code> and <code>dPdy</code>. Set: - <code>δs/δx=δp/δx</code> for a 1D texture, <code>δp.s/δx</code> otherwise - <code>δs/δy=δp/δy</code> for a 1D texture, <code>δp.s/δy</code> otherwise - <code>δt/δx=0.0</code> for a 1D texture, <code>δp.t/δx</code> otherwise - <code>δt/δy=0.0</code> for a 1D texture, <code>δp.t/δy</code> otherwise - <code>δr/δx=0.0</code> for a 1D or 2D texture, <code>δp.p/δx</code> otherwise - <code>δr/δy=0.0</code>  for a 1D or 2D texture, <code>δp.p/δy</code> otherwise<br>For the cube version, the partial derivatives of <code>p</code> are assumed to be in the coordinate system used before texture coordinates are projected onto the appropriate cube face.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of P with respect to window x.",
                        "dPdy" to "The partial derivative of P with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGrad",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("dPdx", VEC2),
                    ParameterSpec("dPdy", VEC2)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with explicit texture coordinate gradiends as specified in <code>dPdx</code> and <code>dPdy</code>. Set: - <code>δs/δx=δp/δx</code> for a 1D texture, <code>δp.s/δx</code> otherwise - <code>δs/δy=δp/δy</code> for a 1D texture, <code>δp.s/δy</code> otherwise - <code>δt/δx=0.0</code> for a 1D texture, <code>δp.t/δx</code> otherwise - <code>δt/δy=0.0</code> for a 1D texture, <code>δp.t/δy</code> otherwise - <code>δr/δx=0.0</code> for a 1D or 2D texture, <code>δp.p/δx</code> otherwise - <code>δr/δy=0.0</code>  for a 1D or 2D texture, <code>δp.p/δy</code> otherwise<br>For the cube version, the partial derivatives of <code>p</code> are assumed to be in the coordinate system used before texture coordinates are projected onto the appropriate cube face.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of P with respect to window x.",
                        "dPdy" to "The partial derivative of P with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGrad",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBE),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("dPdx", VEC3),
                    ParameterSpec("dPdy", VEC3)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with explicit texture coordinate gradiends as specified in <code>dPdx</code> and <code>dPdy</code>. Set: - <code>δs/δx=δp/δx</code> for a 1D texture, <code>δp.s/δx</code> otherwise - <code>δs/δy=δp/δy</code> for a 1D texture, <code>δp.s/δy</code> otherwise - <code>δt/δx=0.0</code> for a 1D texture, <code>δp.t/δx</code> otherwise - <code>δt/δy=0.0</code> for a 1D texture, <code>δp.t/δy</code> otherwise - <code>δr/δx=0.0</code> for a 1D or 2D texture, <code>δp.p/δx</code> otherwise - <code>δr/δy=0.0</code>  for a 1D or 2D texture, <code>δp.p/δy</code> otherwise<br>For the cube version, the partial derivatives of <code>p</code> are assumed to be in the coordinate system used before texture coordinates are projected onto the appropriate cube face.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of P with respect to window x.",
                        "dPdy" to "The partial derivative of P with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGrad",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBEARRAY),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("dPdx", VEC3),
                    ParameterSpec("dPdy", VEC3)
                ),
                description = functionDoc(
                    description = """Performs a texture lookup at coordinate <code>p</code> from the texture bound to sampler with explicit texture coordinate gradiends as specified in <code>dPdx</code> and <code>dPdy</code>. Set: - <code>δs/δx=δp/δx</code> for a 1D texture, <code>δp.s/δx</code> otherwise - <code>δs/δy=δp/δy</code> for a 1D texture, <code>δp.s/δy</code> otherwise - <code>δt/δx=0.0</code> for a 1D texture, <code>δp.t/δx</code> otherwise - <code>δt/δy=0.0</code> for a 1D texture, <code>δp.t/δy</code> otherwise - <code>δr/δx=0.0</code> for a 1D or 2D texture, <code>δp.p/δx</code> otherwise - <code>δr/δy=0.0</code>  for a 1D or 2D texture, <code>δp.p/δy</code> otherwise<br>For the cube version, the partial derivatives of <code>p</code> are assumed to be in the coordinate system used before texture coordinates are projected onto the appropriate cube face.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of P with respect to window x.",
                        "dPdy" to "The partial derivative of P with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProjGrad",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("dPdx", VEC2),
                    ParameterSpec("dPdy", VEC2)
                ),
                description = functionDoc(
                    description = """Perform a texture lookup with projection and explicit gradients.<br>The texture coordinates consumed from <code>p</code>, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. After these values are computed, the texture lookup proceeds as in <i>textureGrad</i>, passing <code>dPdx</code> and <code>dPdy</code> as gradients.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of <code>p</code> with respect to window x.",
                        "dPdy" to "The partial derivative of <code>p</code> with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProjGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProjGrad",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("dPdx", VEC2),
                    ParameterSpec("dPdy", VEC2)
                ),
                description = functionDoc(
                    description = """Perform a texture lookup with projection and explicit gradients.<br>The texture coordinates consumed from <code>p</code>, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. After these values are computed, the texture lookup proceeds as in <i>textureGrad</i>, passing <code>dPdx</code> and <code>dPdy</code> as gradients.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of <code>p</code> with respect to window x.",
                        "dPdy" to "The partial derivative of <code>p</code> with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProjGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureProjGrad",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("p", VEC4),
                    ParameterSpec("dPdx", VEC3),
                    ParameterSpec("dPdy", VEC3)
                ),
                description = functionDoc(
                    description = """Perform a texture lookup with projection and explicit gradients.<br>The texture coordinates consumed from <code>p</code>, not including the last component of <code>p</code>, are divided by the last component of <code>p</code>. After these values are computed, the texture lookup proceeds as in <i>textureGrad</i>, passing <code>dPdx</code> and <code>dPdy</code> as gradients.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "dPdx" to "The partial derivative of <code>p</code> with respect to window x.",
                        "dPdy" to "The partial derivative of <code>p</code> with respect to window y."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureProjGrad.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texelFetch",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", IVEC2),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Performs a lookup of a single texel from texture coordinate <code>p</code> in the texture bound to sampler.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "Specifies the level-of-detail within the texture from which the texel will be fetched."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texelFetch.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texelFetch",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2DARRAY),
                    ParameterSpec("p", IVEC3),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Performs a lookup of a single texel from texture coordinate <code>p</code> in the texture bound to sampler.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "Specifies the level-of-detail within the texture from which the texel will be fetched."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texelFetch.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "texelFetch",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER3D),
                    ParameterSpec("p", IVEC3),
                    ParameterSpec("lod", INT)
                ),
                description = functionDoc(
                    description = """Performs a lookup of a single texel from texture coordinate <code>p</code> in the texture bound to sampler.""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "lod" to "Specifies the level-of-detail within the texture from which the texel will be fetched."
                    ),
                    returns = "A texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/texelFetch.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGather",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2D),
                    ParameterSpec("p", VEC2),
                    ParameterSpec("comps", INT)
                ),
                description = functionDoc(
                    description = """Gathers four texels from a texture.<br>Returns the value:<br><pre><code>
    vec4(Sample_i0_j1(p, base).comps,
    Sample_i1_j1(p, base).comps,
    Sample_i1_j0(p, base).comps,
    Sample_i0_j0(p, base).comps);
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "comps" to "*optional* the component of the source texture (0 -> x, 1 -> y, 2 -> z, 3 -> w) that will be used to generate the resulting vector. Zero if not specified."
                    ),
                    returns = "The gathered texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGather.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGather",
                returnType = GVEC4_TYPE,
                parameters = listOf(
                    ParameterSpec("s", GSAMPLER2DARRAY),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("comps", INT)
                ),
                description = functionDoc(
                    description = """Gathers four texels from a texture.<br>Returns the value:<br><pre><code>
    vec4(Sample_i0_j1(p, base).comps,
    Sample_i1_j1(p, base).comps,
    Sample_i1_j0(p, base).comps,
    Sample_i0_j0(p, base).comps);
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "comps" to "*optional* the component of the source texture (0 -> x, 1 -> y, 2 -> z, 3 -> w) that will be used to generate the resulting vector. Zero if not specified."
                    ),
                    returns = "The gathered texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGather.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "textureGather",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("s", SAMPLERCUBE),
                    ParameterSpec("p", VEC3),
                    ParameterSpec("comps", INT)
                ),
                description = functionDoc(
                    description = """Gathers four texels from a texture.<br>Returns the value:<br><pre><code>
    vec4(Sample_i0_j1(p, base).comps,
    Sample_i1_j1(p, base).comps,
    Sample_i1_j0(p, base).comps,
    Sample_i0_j0(p, base).comps);
    </code></pre>""",
                    params = mapOf(
                        "s" to "The sampler to which the texture from which texels will be retrieved is bound.",
                        "p" to "The texture coordinates at which texture will be sampled.",
                        "comps" to "*optional* the component of the source texture (0 -> x, 1 -> y, 2 -> z, 3 -> w) that will be used to generate the resulting vector. Zero if not specified."
                    ),
                    returns = "The gathered texel.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureGather.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "fwidth",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("p", VEC_TYPE)
                ),
                description = functionDoc(
                    description = """Returns the sum of the absolute value of derivatives in x and y.<br>Uses local differencing for the input argument <code>p</code>.<br>Equivalent to <code>abs(dFdx(p)) + abs(dFdy(p))</code>.""",
                    params = mapOf(
                        "p" to "The expression of which to take the partial derivative."
                    ),
                    returns = "The partial derivative.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/fwidth.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "packHalf2x16",
                returnType = UINT,
                parameters = listOf(
                    ParameterSpec("v", VEC2)
                ),
                description = functionDoc(
                    description = """Converts two 32-bit floating-point quantities to 16-bit floating-point quantities and packs them into a single 32-bit integer.<br>Returns an unsigned integer obtained by converting the components of a two-component floating-point vector to the 16-bit floating-point representation found in the OpenGL Specification, and then packing these two 16-bit integers into a 32-bit unsigned integer. The first vector component specifies the 16 least-significant bits of the result; the second component specifies the 16 most-significant bits.""",
                    params = mapOf(
                        "v" to "A vector of two 32-bit floating-point values that are to be converted to 16-bit representation and packed into the result."
                    ),
                    returns = "The packed value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/packHalf2x16.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "unpackHalf2x16",
                returnType = VEC2,
                parameters = listOf(
                    ParameterSpec("v", UINT)
                ),
                description = functionDoc(
                    description = """Inverse of :ref:<i>packHalf2x16</i>.<br>Unpacks a 32-bit integer into two 16-bit floating-point values, converts them to 32-bit floating-point values, and puts them into a vector. The first component of the vector is obtained from the 16 least-significant bits of <code>v</code>; the second component is obtained from the 16 most-significant bits of <code>v</code>.""",
                    params = mapOf(
                        "v" to "A single 32-bit unsigned integer containing 2 packed 16-bit floating-point values."
                    ),
                    returns = "Two unpacked floating-point values.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/unpackHalf2x16.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "packUnorm2x16",
                returnType = UINT,
                parameters = listOf(
                    ParameterSpec("v", VEC2)
                ),
                description = functionDoc(
                    description = """Pack floating-point values into an unsigned integer.<br>Converts each component of the normalized floating-point value v into 16-bit integer values and then packs the results into a 32-bit unsigned integer.<br>The conversion for component c of <code>v</code> to fixed-point is performed as follows:<br><pre><code>
    round(clamp(c, 0.0, 1.0) * 65535.0)
    </code></pre> The first component of the vector will be written to the least significant bits of the output; the last component will be written to the most significant bits.<br>""",
                    params = mapOf(
                        "v" to "A vector of values to be packed into an unsigned integer."
                    ),
                    returns = "Unsigned 32 bit integer containing the packed encoding of the vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/packUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "unpackUnorm2x16",
                returnType = VEC2,
                parameters = listOf(
                    ParameterSpec("v", UINT)
                ),
                description = functionDoc(
                    description = """Unpack floating-point values from an unsigned integer.<br>Unpack single 32-bit unsigned integers into a pair of 16-bit unsigned integers. Then, each component is converted to a normalized floating-point value to generate the returned two-component vector.<br>The conversion for unpacked fixed point value f to floating-point is performed as follows:<br>f / 65535.0<br>The first component of the returned vector will be extracted from the least significant bits of the input; the last component will be extracted from the most significant bits.""",
                    params = mapOf(
                        "v" to "An unsigned integer containing packed floating-point values.<br>"
                    ),
                    returns = "",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/unpackUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "packSnorm2x16",
                returnType = UINT,
                parameters = listOf(
                    ParameterSpec("v", VEC2)
                ),
                description = functionDoc(
                    description = """Packs floating-point values into an unsigned integer.<br>Convert each component of the normalized floating-point value <code>v</code> into 16-bit integer values and then packs the results into a 32-bit unsigned integer.<br>The conversion for component c of <code>v</code> to fixed-point is performed as follows:<br><pre><code>
    round(clamp(c, -1.0, 1.0) * 32767.0)
    </code></pre> The first component of the vector will be written to the least significant bits of the output; the last component will be written to the most significant bits.""",
                    params = mapOf(
                        "v" to "A vector of values to be packed into an unsigned integer."
                    ),
                    returns = "Unsigned 32 bit integer containing the packed encoding of the vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/packUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "unpackSnorm2x16",
                returnType = VEC2,
                parameters = listOf(
                    ParameterSpec("v", UINT)
                ),
                description = functionDoc(
                    description = """Unpacks floating-point values from an unsigned integer.<br>Unpacks single 32-bit unsigned integers into a pair of 16-bit signed integers. Then, each component is converted to a normalized floating-point value to generate the returned two-component vector.<br>The conversion for unpacked fixed point value f to floating-point is performed as follows:<br>clamp(f / 32727.0, -1.0, 1.0)<br>The first component of the returned vector will be extracted from the least significant bits of the input; the last component will be extracted from the most significant bits.""",
                    params = mapOf(
                        "v" to "An unsigned integer containing packed floating-point values.<br>"
                    ),
                    returns = "",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/unpackUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "packUnorm4x8",
                returnType = UINT,
                parameters = listOf(
                    ParameterSpec("v", VEC4)
                ),
                description = functionDoc(
                    description = """Packs floating-point values into an unsigned integer.<br>Converts each component of the normalized floating-point value <code>v</code> into 16-bit integer values and then packs the results into a 32-bit unsigned integer.<br>The conversion for component c of <code>v</code> to fixed-point is performed as follows:<br><pre><code>
    round(clamp(c, 0.0, 1.0) * 255.0)
    </code></pre> The first component of the vector will be written to the least significant bits of the output; the last component will be written to the most significant bits.<br>""",
                    params = mapOf(
                        "v" to "A vector of values to be packed into an unsigned integer."
                    ),
                    returns = "Unsigned 32 bit integer containing the packed encoding of the vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/packUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "unpackUnorm4x8",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("v", UINT)
                ),
                description = functionDoc(
                    description = """Unpacks floating-point values from an unsigned integer.<br>Unpacks single 32-bit unsigned integers into four 8-bit unsigned integers. Then, each component is converted to a normalized floating-point value to generate the returned four-component vector.<br>The conversion for unpacked fixed point value f to floating-point is performed as follows:<br>f / 255.0<br>The first component of the returned vector will be extracted from the least significant bits of the input; the last component will be extracted from the most significant bits.""",
                    params = mapOf(
                        "v" to "An unsigned integer containing packed floating-point values.<br>"
                    ),
                    returns = "",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/unpackUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "packSnorm4x8",
                returnType = UINT,
                parameters = listOf(
                    ParameterSpec("v", VEC4)
                ),
                description = functionDoc(
                    description = """Packs floating-point values into an unsigned integer.<br>Convert each component of the normalized floating-point value <code>v</code> into 16-bit integer values and then packs the results into a 32-bit unsigned integer.<br>The conversion for component c of <code>v</code> to fixed-point is performed as follows:<br><pre><code>
    round(clamp(c, -1.0, 1.0) * 127.0)
    </code></pre> The first component of the vector will be written to the least significant bits of the output; the last component will be written to the most significant bits.<br>""",
                    params = mapOf(
                        "v" to "A vector of values to be packed into an unsigned integer."
                    ),
                    returns = "Unsigned 32 bit integer containing the packed encoding of the vector.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/packUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "unpackSnorm4x8",
                returnType = VEC4,
                parameters = listOf(
                    ParameterSpec("v", UINT)
                ),
                description = functionDoc(
                    description = """Unpack floating-point values from an unsigned integer.<br>Unpack single 32-bit unsigned integers into four 8-bit signed integers. Then, each component is converted to a normalized floating-point value to generate the returned four-component vector.<br>The conversion for unpacked fixed point value f to floating-point is performed as follows:<br>clamp(f / 127.0, -1.0, 1.0)<br>The first component of the returned vector will be extracted from the least significant bits of the input; the last component will be extracted from the most significant bits.""",
                    params = mapOf(
                        "v" to "An unsigned integer containing packed floating-point values.<br><br>"
                    ),
                    returns = "",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/unpackUnorm.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "bitfieldExtract",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_INT_TYPE),
                    ParameterSpec("offset", INT),
                    ParameterSpec("bits", INT)
                ),
                description = functionDoc(
                    description = """Extracts a subset of the bits of <code>value</code> and returns it in the least significant bits of the result. The range of bits extracted is <code>[offset, offset + bits - 1]</code>.<br>The most significant bits of the result will be set to zero.<br><br><b>note:</b> If bits is zero, the result will be zero.<br><br><b>warning:</b> The result will be undefined if:<br>- offset or bits is negative. - if the sum of offset and bits is greater than the number of bits used to store the operand.""",
                    params = mapOf(
                        "value" to "The integer from which to extract bits.",
                        "offset" to "The index of the first bit to extract.",
                        "bits" to "The number of bits to extract."
                    ),
                    returns = "Integer with the requested bits.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitfieldExtract.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "bitfieldExtract",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_UINT_TYPE),
                    ParameterSpec("offset", INT),
                    ParameterSpec("bits", INT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Extracts a subset of the bits of <code>value</code> and returns it in the least significant bits of the result. The range of bits extracted is <code>[offset, offset + bits - 1]</code>.<br>The most significant bits will be set to the value of <code>offset + base - 1</code> (i.e., it is sign extended to the width of the return type).<br><br><b>note:</b> If bits is zero, the result will be zero.<br><br><b>warning:</b> The result will be undefined if:<br>- offset or bits is negative. - if the sum of offset and bits is greater than the number of bits used to store the operand.""",
                    params = mapOf(
                        "value" to "The integer from which to extract bits.",
                        "offset" to "The index of the first bit to extract.",
                        "bits" to "The number of bits to extract."
                    ),
                    returns = "Integer with the requested bits.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitfieldExtract.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "bitfieldExtract",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_UINT_TYPE),
                    ParameterSpec("offset", INT),
                    ParameterSpec("bits", INT)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Inserts the <code>bits</code> least significant bits of <code>insert</code> into <code>base</code> at offset <code>offset</code>.<br>The returned value will have bits [offset, offset + bits + 1] taken from [0, bits - 1] of <code>insert</code> and all other bits taken directly from the corresponding bits of base.<br><br><b>note:</b> If bits is zero, the result will be the original value of base.<br><br><b>warning:</b> The result will be undefined if:<br>- offset or bits is negative. - if the sum of offset and bits is greater than the number of bits used to store the operand.""",
                    params = mapOf(
                        "value" to "",
                        "offset" to "The index of the first bit to insert.",
                        "bits" to "The number of bits to insert."
                    ),
                    returns = "<code>base</code> with inserted bits.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitfieldInsert.xhtml"
                )
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
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Inserts the <code>bits</code> least significant bits of <code>insert</code> into <code>base</code> at offset <code>offset</code>.<br>The returned value will have bits [offset, offset + bits + 1] taken from [0, bits - 1] of <code>insert</code> and all other bits taken directly from the corresponding bits of base.<br><br><b>note:</b> If bits is zero, the result will be the original value of base.<br><br><b>warning:</b> The result will be undefined if:<br>- offset or bits is negative. - if the sum of offset and bits is greater than the number of bits used to store the operand.""",
                    params = mapOf(
                        "base" to "The integer into which to insert <code>insert</code>.",
                        "insert" to "The value of the bits to insert.",
                        "offset" to "The index of the first bit to insert.",
                        "bits" to "The number of bits to insert."
                    ),
                    returns = "<code>base</code> with inserted bits.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitfieldInsert.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "bitfieldReverse",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Reverse the order of bits in an integer.<br>The bit numbered <code>n</code> will be taken from bit <code>(bits - 1) - n</code> of <code>value</code>, where bits is the total number of bits used to represent <code>value</code>.""",
                    params = mapOf(
                        "value" to "The value whose bits to reverse."
                    ),
                    returns = "<code>value</code> but with its bits reversed.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitfieldReverse.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "bitfieldReverse",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Reverse the order of bits in an integer.<br>The bit numbered <code>n</code> will be taken from bit <code>(bits - 1) - n</code> of <code>value</code>, where bits is the total number of bits used to represent <code>value</code>.""",
                    params = mapOf(
                        "value" to "The value whose bits to reverse."
                    ),
                    returns = "<code>value</code> but with its bits reversed.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitfieldReverse.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "bitCount",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Counts the number of 1 bits in an integer.""",
                    params = mapOf(
                        "value" to "The value whose bits to count."
                    ),
                    returns = "The number of bits that are set to 1 in the binary representation of <code>value</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitCount.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "bitCount",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Counts the number of 1 bits in an integer.""",
                    params = mapOf(
                        "value" to "The value whose bits to count."
                    ),
                    returns = "The number of bits that are set to 1 in the binary representation of <code>value</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/bitCount.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "findLSB",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Find the index of the least significant bit set to <code>1</code>.<br><br><b>note:</b> If <code>value</code> is zero, <code>-1</code> will be returned.""",
                    params = mapOf(
                        "value" to "The value whose bits to scan."
                    ),
                    returns = "The bit number of the least significant bit that is set to 1 in the binary representation of value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/findLSB.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "findLSB",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Find the index of the least significant bit set to <code>1</code>.<br><br><b>note:</b> If <code>value</code> is zero, <code>-1</code> will be returned.""",
                    params = mapOf(
                        "value" to "The value whose bits to scan."
                    ),
                    returns = "The bit number of the least significant bit that is set to 1 in the binary representation of value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/findLSB.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "findMSB",
                returnType = VEC_INT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_INT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Find the index of the most significant bit set to 1.<br><br><b>note:</b> For signed integer types, the sign bit is checked first and then: - For positive integers, the result will be the bit number of the most significant bit that is set to 1. - For negative integers, the result will be the bit number of the most significant bit set to 0.<br><br><b>note:</b> For a value of zero or negative 1, -1 will be returned.""",
                    params = mapOf(
                        "value" to "The value whose bits to scan."
                    ),
                    returns = "The bit number of the most significant bit that is set to 1 in the binary representation of value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/findMSB.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "findMSB",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("value", VEC_UINT_TYPE)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Find the index of the most significant bit set to 1.<br><br><b>note:</b> For signed integer types, the sign bit is checked first and then: - For positive integers, the result will be the bit number of the most significant bit that is set to 1. - For negative integers, the result will be the bit number of the most significant bit set to 0.<br><br><b>note:</b> For a value of zero or negative 1, -1 will be returned.""",
                    params = mapOf(
                        "value" to "The value whose bits to scan."
                    ),
                    returns = "The bit number of the most significant bit that is set to 1 in the binary representation of value.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/findMSB.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "imulExtended",
                returnType = VOID,
                parameters = listOf(
                    ParameterSpec("x", VEC_INT_TYPE),
                    ParameterSpec("y", VEC_INT_TYPE),
                    ParameterSpec("msb", /* UNKNOWN: out vec_int_type */ VOID),
                    ParameterSpec("lsb", /* UNKNOWN: out vec_int_type */ VOID)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Perform 32-bit by 32-bit signed multiplication to produce a 64-bit result.<br>The 32 least significant bits of this product are returned in <code>lsb</code> and the 32 most significant bits are returned in <code>msb</code>.""",
                    params = mapOf(
                        "x" to "The first multiplicand.",
                        "y" to "The second multiplicand.",
                        "msb" to "The variable to receive the most significant word of the product.",
                        "lsb" to "The variable to receive the least significant word of the product.<br>"
                    ),
                    returns = "",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/umulExtended.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "umulExtended",
                returnType = VOID,
                parameters = listOf(
                    ParameterSpec("x", VEC_UINT_TYPE),
                    ParameterSpec("y", VEC_UINT_TYPE),
                    ParameterSpec("msb", /* UNKNOWN: out vec_uint_type */ VOID),
                    ParameterSpec("lsb", /* UNKNOWN: out vec_uint_type */ VOID)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Perform 32-bit by 32-bit unsigned multiplication to produce a 64-bit result.<br>The 32 least significant bits of this product are returned in <code>lsb</code> and the 32 most significant bits are returned in <code>msb</code>.""",
                    params = mapOf(
                        "x" to "The first multiplicand.",
                        "y" to "The second multiplicand.",
                        "msb" to "The variable to receive the most significant word of the product.",
                        "lsb" to "The variable to receive the least significant word of the product.<br>"
                    ),
                    returns = "",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/umulExtended.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "uaddCarry",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_UINT_TYPE),
                    ParameterSpec("y", VEC_UINT_TYPE),
                    ParameterSpec("carry", /* UNKNOWN: out vec_uint_type */ VOID)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Add unsigned integers and generate carry.<br>adds two 32-bit unsigned integer variables (scalars or vectors) and generates a 32-bit unsigned integer result, along with a carry output. The value carry is .""",
                    params = mapOf(
                        "x" to "The first operand.",
                        "y" to "The second operand.",
                        "carry" to "0 if the sum is less than 2\\ :sup:<i>32</i>, otherwise 1."
                    ),
                    returns = "<code>(x + y) % 2^32</code>.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/uaddCarry.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "usubBorrow",
                returnType = VEC_UINT_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_UINT_TYPE),
                    ParameterSpec("y", VEC_UINT_TYPE),
                    ParameterSpec("borrow", /* UNKNOWN: out vec_uint_type */ VOID)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Subtract unsigned integers and generate borrow.""",
                    params = mapOf(
                        "x" to "The first operand.",
                        "y" to "The second operand.",
                        "borrow" to "<code>0</code> if <code>x >= y</code>, otherwise <code>1</code>."
                    ),
                    returns = "The difference of <code>x</code> and <code>y</code> if non-negative, or 2\\ :sup:<i>32</i> plus that difference otherwise.<br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/usubBorrow.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "ldexp",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("exp", /* UNKNOWN: out vec_int_type */ VOID)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Assembles a floating-point number from a value and exponent.<br><br><b>warning:</b> If this product is too large to be represented in the floating-point type, the result is undefined.""",
                    params = mapOf(
                        "x" to "The value to be used as a source of significand.",
                        "exp" to "The value to be used as a source of exponent."
                    ),
                    returns = "<code>x * 2^exp</code><br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/ldexp.xhtml"
                )
            ),
    
            FunctionSpec(
                name = "frexp",
                returnType = VEC_TYPE,
                parameters = listOf(
                    ParameterSpec("x", VEC_TYPE),
                    ParameterSpec("exp", /* UNKNOWN: out vec_int_type */ VOID)
                ),
                description = functionDoc(
                    description = """<a href="https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shader_functions.html#shading-componentwise">Component-wise Function</a>.<br>Extracts <code>x</code> into a floating-point significand in the range <code>[0.5, 1.0)</code> and in integral exponent of two, such that:<br><pre><code>
    x = significand * 2 ^ exponent
    </code></pre> For a floating-point value of zero, the significand and exponent are both zero.<br><br><b>warning:</b> For a floating-point value that is an infinity or a floating-point NaN, the results are undefined.""",
                    params = mapOf(
                        "x" to "The value from which significand and exponent are to be extracted.",
                        "exp" to "The variable into which to place the exponent of <code>x</code>."
                    ),
                    returns = "The significand of <code>x</code>.<br><br>",
                    link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/frexp.xhtml"
                )
            )
        )
    
    private val fragmentShaderOnlyFunctions = listOf(
        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2D),
                ParameterSpec("p", VEC2)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader.<br>Compute the level-of-detail that would be used to sample from a texture.<br>The mipmap array(s) that would be accessed is returned in the x component of the return value. The computed level-of-detail relative to the base level is returned in the y component of the return value.<br>If called on an incomplete texture, the result of the operation is undefined.""",
                params = mapOf(
                    "s" to "The sampler to which the texture whose level-of-detail will be queried is bound.",
                    "p" to "The texture coordinates at which the level-of-detail will be queried."
                ),
                returns = "See description.<br>",
                link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLod.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER2DARRAY),
                ParameterSpec("p", VEC2)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader.<br>Compute the level-of-detail that would be used to sample from a texture.<br>The mipmap array(s) that would be accessed is returned in the x component of the return value. The computed level-of-detail relative to the base level is returned in the y component of the return value.<br>If called on an incomplete texture, the result of the operation is undefined.""",
                params = mapOf(
                    "s" to "The sampler to which the texture whose level-of-detail will be queried is bound.",
                    "p" to "The texture coordinates at which the level-of-detail will be queried."
                ),
                returns = "See description.<br>",
                link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLod.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("s", GSAMPLER3D),
                ParameterSpec("p", VEC3)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader.<br>Compute the level-of-detail that would be used to sample from a texture.<br>The mipmap array(s) that would be accessed is returned in the x component of the return value. The computed level-of-detail relative to the base level is returned in the y component of the return value.<br>If called on an incomplete texture, the result of the operation is undefined.""",
                params = mapOf(
                    "s" to "The sampler to which the texture whose level-of-detail will be queried is bound.",
                    "p" to "The texture coordinates at which the level-of-detail will be queried."
                ),
                returns = "See description.<br>",
                link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLod.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "textureQueryLod",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("s", SAMPLERCUBE),
                ParameterSpec("p", VEC3)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader.<br>Compute the level-of-detail that would be used to sample from a texture.<br>The mipmap array(s) that would be accessed is returned in the x component of the return value. The computed level-of-detail relative to the base level is returned in the y component of the return value.<br>If called on an incomplete texture, the result of the operation is undefined.""",
                params = mapOf(
                    "s" to "The sampler to which the texture whose level-of-detail will be queried is bound.",
                    "p" to "The texture coordinates at which the level-of-detail will be queried."
                ),
                returns = "See description.<br>",
                link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/textureQueryLod.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "dFdx",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader.<br>Returns the partial derivative of <code>p</code> with respect to the window x coordinate using local differencing.<br>Returns either :ref:<i>dFdxCoarse</i> or :ref:<i>dFdxFine</i>. The implementation may choose which calculation to perform based upon factors such as performance or the value of the API <code>GL_FRAGMENT_SHADER_DERIVATIVE_HINT</code> hint.<br><br><br><b>warning:</b> Expressions that imply higher order derivatives such as <code>dFdx(dFdx(n))</code> have undefined results, as do mixed-order derivatives such as <code>dFdx(dFdy(n))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative.<br><br><b>note:</b> It is assumed that the expression <code>p</code> is continuous and therefore expressions evaluated via non-uniform control flow may be undefined."
                ),
                returns = "The partial derivative of <code>p</code>.<br>",
                link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/dFdx.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "dFdxCoarse",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader. Not available when using the Compatibility renderer.<br>Returns the partial derivative of <code>p</code> with respect to the window x coordinate.<br>Calculates derivatives using local differencing based on the value of <code>p</code> for the current fragment's neighbors, and will possibly, but not necessarily, include the value for the current fragment. That is, over a given area, the implementation can compute derivatives in fewer unique locations than would be allowed for the corresponding :ref:<i>dFdxFine</i> function.<br><br><b>warning:</b> Expressions that imply higher order derivatives such as <code>dFdx(dFdx(n))</code> have undefined results, as do mixed-order derivatives such as <code>dFdx(dFdy(n))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative.<br><br><b>note:</b> It is assumed that the expression <code>p</code> is continuous and therefore expressions evaluated via non-uniform control flow may be undefined."
                ),
                returns = "The partial derivative of <code>p</code>.<br>",
                link = "https://registry.khronos.org/OpenGL-Refpages/gl4/html/dFdx.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "dFdxFine",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader. Not available when using the Compatibility renderer.<br>Returns the partial derivative of <code>p</code> with respect to the window x coordinate.<br>Calculates derivatives using local differencing based on the value of <code>p</code> for the current fragment and its immediate neighbor(s).<br><br><b>warning:</b> Expressions that imply higher order derivatives such as <code>dFdx(dFdx(n))</code> have undefined results, as do mixed-order derivatives such as <code>dFdx(dFdy(n))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative.<br><br><b>note:</b> It is assumed that the expression <code>p</code> is continuous and therefore expressions evaluated via non-uniform control flow may be undefined."
                ),
                returns = "The partial derivative of <code>p</code>.<br>",
                link = "https://registry.khronos.org/OpenGL-Refpages/gl4/html/dFdx.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "dFdy",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader.<br>Returns the partial derivative of <code>p</code> with respect to the window y coordinate using local differencing.<br>Returns either :ref:<i>dFdyCoarse</i> or :ref:<i>dFdyFine</i>. The implementation may choose which calculation to perform based upon factors such as performance or the value of the API <code>GL_FRAGMENT_SHADER_DERIVATIVE_HINT</code> hint.<br><br><b>warning:</b> Expressions that imply higher order derivatives such as <code>dFdx(dFdx(n))</code> have undefined results, as do mixed-order derivatives such as <code>dFdx(dFdy(n))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative.<br><br><b>note:</b> It is assumed that the expression <code>p</code> is continuous and therefore expressions evaluated via non-uniform control flow may be undefined."
                ),
                returns = "The partial derivative of <code>p</code>.<br>",
                link = "https://registry.khronos.org/OpenGL-Refpages/gl4/html/dFdx.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "dFdyCoarse",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader. Not available when using the Compatibility renderer.<br>Returns the partial derivative of <code>p</code> with respect to the window y coordinate.<br>Calculates derivatives using local differencing based on the value of <code>p</code> for the current fragment's neighbors, and will possibly, but not necessarily, include the value for the current fragment. That is, over a given area, the implementation can compute derivatives in fewer unique locations than would be allowed for the corresponding dFdyFine and dFdyFine functions.<br><br><b>warning:</b> Expressions that imply higher order derivatives such as <code>dFdx(dFdx(n))</code> have undefined results, as do mixed-order derivatives such as <code>dFdx(dFdy(n))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative.<br><br><b>note:</b> It is assumed that the expression <code>p</code> is continuous and therefore expressions evaluated via non-uniform control flow may be undefined."
                ),
                returns = "The partial derivative of <code>p</code>.<br>",
                link = "https://registry.khronos.org/OpenGL-Refpages/gl4/html/dFdx.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "dFdyFine",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader. Not available when using the Compatibility renderer.<br>Returns the partial derivative of <code>p</code> with respect to the window y coordinate.<br>Calculates derivatives using local differencing based on the value of <code>p</code> for the current fragment and its immediate neighbor(s).<br><br><b>warning:</b> Expressions that imply higher order derivatives such as <code>dFdx(dFdx(n))</code> have undefined results, as do mixed-order derivatives such as <code>dFdx(dFdy(n))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative.<br><br><b>note:</b> It is assumed that the expression <code>p</code> is continuous and therefore expressions evaluated via non-uniform control flow may be undefined."
                ),
                returns = "The partial derivative of <code>p</code>.<br>",
                link = "https://registry.khronos.org/OpenGL-Refpages/gl4/html/dFdx.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "fwidthCoarse",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader. Not available when using the Compatibility renderer.<br>Returns the sum of the absolute value of derivatives in x and y.<br>Uses local differencing for the input argument p.<br>Equivalent  to <code>abs(dFdxCoarse(p)) + abs(dFdyCoarse(p))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative."
                ),
                returns = "The partial derivative.<br>",
                link = "https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/fwidth.xhtml"
            )
        ),
    
        FunctionSpec(
            name = "fwidthFine",
            returnType = VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", VEC_TYPE)
            ),
            description = functionDoc(
                description = """<br><b>note:</b> Available only in the fragment shader. Not available when using the Compatibility renderer.<br>Returns the sum of the absolute value of derivatives in x and y.<br>Uses local differencing for the input argument p.<br>Equivalent to <code>abs(dFdxFine(p)) + abs(dFdyFine(p))</code>.""",
                params = mapOf(
                    "p" to "The expression of which to take the partial derivative."
                ),
                returns = "The partial derivative.<br><br>",
                link = "https://registry.khronos.org/OpenGL-Refpages/gl4/html/fwidth.xhtml"
            )
        ),
    )
    
    private val sdfFunctions = listOf(
        FunctionSpec(
            name = "texture_sdf",
            returnType = FLOAT,
            parameters = listOf(
                ParameterSpec("sdf_pos", VEC2)
            ),
            description = "Performs an SDF texture lookup."
        ),
    
        FunctionSpec(
            name = "texture_sdf_normal",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("sdf_pos", VEC2)
            ),
            description = "Calculates a normal from the SDF texture."
        ),
    
        FunctionSpec(
            name = "sdf_to_screen_uv",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("sdf_pos", VEC2)
            ),
            description = "Converts an SDF to screen UV."
        ),
    
        FunctionSpec(
            name = "screen_uv_to_sdf",
            returnType = VEC2,
            parameters = listOf(
                ParameterSpec("uv", VEC2)
            ),
            description = "Converts screen UV to an SDF."
        ),
    )
    
    private val globalVariables = listOf(
        ParameterSpec(
            name = "TIME",
            type = FLOAT,
            description = "Global time since the engine has started, in seconds. It repeats after every <code>3,600</code> seconds (which can be changed with the <a href=\"https://docs.godotengine.org/en/stable/classes/class_projectsettings.html#class-projectsettings-property-rendering-limits-time-time-rollover-secs\">rollover</a> setting). It's affected by <a href=\"https://docs.godotengine.org/en/stable/classes/class_engine.html#class-engine-property-time-scale\">time_scale</a> but not by pausing. If you need a <code>TIME</code> variable that is not affected by time scale, add your own <a href=\"https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference/shading_language.html#doc-shading-language-global-uniforms\">global shader uniform</a> and update it each frame."
        ),
    
        ParameterSpec(
            name = "PI",
            type = FLOAT,
            description = "A <code>PI</code> constant (<code>3.141592</code>). The ratio of a circle's circumference to its diameter and the number of radians in a half turn."
        ),
    
        ParameterSpec(
            name = "TAU",
            type = FLOAT,
            description = "A <code>TAU</code> constant (<code>6.283185</code>). Equivalent to PI * 2 and the number of radians in a full turn."
        ),
    
        ParameterSpec(
            name = "E",
            type = FLOAT,
            description = "An <code>E</code> constant (<code>2.718281</code>). Euler's number, the base of the natural logarithm."
        ),
    )
    
    private val particlesStartAndProcessVariables = listOf(
        ParameterSpec(
            name = "LIFETIME",
            type = FLOAT,
            description = "Particle lifetime."
        ),

        ParameterSpec(
            name = "DELTA",
            type = FLOAT,
            description = "Delta process time."
        ),

        ParameterSpec(
            name = "NUMBER",
            type = UINT,
            description = "Unique number since emission start."
        ),

        ParameterSpec(
            name = "INDEX",
            type = UINT,
            description = "Particle index (from total particles)."
        ),

        ParameterSpec(
            name = "EMISSION_TRANSFORM",
            type = MAT4,
            description = "Emitter transform (used for non-local systems)."
        ),

        ParameterSpec(
            name = "RANDOM_SEED",
            type = UINT,
            description = "Random seed used as base for random."
        ),

        ParameterSpec(
            name = "ACTIVE",
            type = BOOL,
            qualifier = ParameterQualifier.INOUT,
            description = "<code>true</code> when the particle is active, can be set to <code>false</code>."
        ),

        ParameterSpec(
            name = "COLOR",
            type = VEC4,
            qualifier = ParameterQualifier.INOUT,
            description = "Particle color, can be written to and accessed in the mesh's vertex function."
        ),

        ParameterSpec(
            name = "VELOCITY",
            type = VEC3,
            qualifier = ParameterQualifier.INOUT,
            description = "Particle velocity, can be modified."
        ),

        ParameterSpec(
            name = "TRANSFORM",
            type = MAT4,
            qualifier = ParameterQualifier.INOUT,
            description = "Particle transform."
        ),

        ParameterSpec(
            name = "CUSTOM",
            type = VEC4,
            qualifier = ParameterQualifier.INOUT,
            description = "Custom particle data. Accessible from the mesh's shader as <code>INSTANCE_CUSTOM</code>."
        ),

        ParameterSpec(
            name = "MASS",
            type = FLOAT,
            qualifier = ParameterQualifier.INOUT,
            description = "Particle mass, intended to be used with attractors. <code>1.0</code> by default."
        ),

        ParameterSpec(
            name = "USERDATAX",
            type = VEC4,
            description = "Vector that enables the integration of supplementary user-defined data into the particle process shader. <code>USERDATAX</code> are six built-ins identified by number, <code>X</code> can be numbers between 1 and 6, for example <code>USERDATA3</code>."
        ),

        ParameterSpec(
            name = "FLAG_EMIT_POSITION",
            type = UINT,
            description = "A flag for the last argument of the <code>emit_subparticle()</code> function to assign a position to a new particle's transform."
        ),

        ParameterSpec(
            name = "FLAG_EMIT_ROT_SCALE",
            type = UINT,
            description = "A flag for the last argument of the <code>emit_subparticle()</code> function to assign a rotation and scale to a new particle's transform."
        ),

        ParameterSpec(
            name = "FLAG_EMIT_VELOCITY",
            type = UINT,
            description = "A flag for the last argument of the <code>emit_subparticle()</code> function to assign a velocity to a new particle."
        ),

        ParameterSpec(
            name = "FLAG_EMIT_COLOR",
            type = UINT,
            description = "A flag for the last argument of the <code>emit_subparticle()</code> function to assign a color to a new particle."
        ),

        ParameterSpec(
            name = "FLAG_EMIT_CUSTOM",
            type = UINT,
            description = "A flag for the last argument of the <code>emit_subparticle()</code> function to assign a custom data vector to a new particle."
        ),

        ParameterSpec(
            name = "EMITTER_VELOCITY",
            type = VEC3,
            description = "Velocity of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_gpuparticles2d.html#class-gpuparticles2d\">Particles2D</a> (<a href=\"https://docs.godotengine.org/en/stable/classes/class_gpuparticles3d.html#class-gpuparticles3d\">3D</a>) node."
        ),

        ParameterSpec(
            name = "INTERPOLATE_TO_END",
            type = FLOAT,
            description = "Value of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_gpuparticles2d.html#class-gpuparticles2d-property-interp-to-end\">interp_to_end</a> (<a href=\"https://docs.godotengine.org/en/stable/classes/class_gpuparticles3d.html#class-gpuparticles3d-property-interp-to-end\">3D</a>) property of the Particles node."
        ),

        ParameterSpec(
            name = "AMOUNT_RATIO",
            type = UINT,
            description = "Value of the <a href=\"https://docs.godotengine.org/en/stable/classes/class_gpuparticles2d.html#class-gpuparticles2d-property-amount-ratio\">amount_ratio</a> (<a href=\"https://docs.godotengine.org/en/stable/classes/class_gpuparticles3d.html#class-gpuparticles3d-property-amount-ratio\">3D</a>) property of the Particles node."
        ),
    )

    private fun functionDoc(
        description: String,
        params: Map<String, String> = emptyMap(),
        returns: String? = null,
        link: String? = null
    ): String {
        val sb = StringBuilder()
        sb.append(description)

        if (params.isNotEmpty()) {
            sb.append("<br><br><b>Parameters:</b>")
            sb.append("<table cellspacing='0' cellpadding='0' border='0'>")
            params.forEach { (name, desc) ->
                sb.append("<tr><td valign='top' style='white-space:nowrap'><code>$name</code>&nbsp;&nbsp;</td><td valign='top'>$desc</td></tr>")
            }
            sb.append("</table>")
        }

        if (returns != null) {
            sb.append("<br><b>Returns:</b><br>")
            sb.append(returns)
        }

        if (link != null) {
            sb.append("<br><br><a href=\"$link\">Online Reference</a>")
        }

        return sb.toString()
    }
}

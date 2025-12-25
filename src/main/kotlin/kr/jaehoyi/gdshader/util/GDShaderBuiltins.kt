package kr.jaehoyi.gdshader.util

object GDShaderBuiltins {
    
    val BUILTIN_FUNCTIONS = listOf(
        BuiltinFunctionSpec(
            name = "radians",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("degrees", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "degrees",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("radians", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "sin",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "cos",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "tan",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "asin",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "acos",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "atan",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("y_over_x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "atan",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("y", "vec_type"),
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "sinh",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "cosh",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "tanh",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "asinh",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "acosh",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "atanh",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "pow",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "exp",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "exp2",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "log",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "log2",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "sqrt",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "inversesqrt",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "abs",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "abs",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("x", "vec_int_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "sign",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "sign",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("x", "vec_int_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "floor",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "round",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "roundEven",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "trunc",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "ceil",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "fract",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "mod",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "mod",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "modf",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("i", "vec_type", qualifier = ParameterQualifier.OUT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("a", "vec_int_type"),
                ParameterSpec("b", "vec_int_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("a", "vec_int_type"),
                ParameterSpec("b", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("a", "vec_uint_type"),
                ParameterSpec("b", "vec_uint_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("a", "vec_uint_type"),
                ParameterSpec("b", "uint")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "max",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("a", "vec_int_type"),
                ParameterSpec("b", "vec_int_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("a", "vec_int_type"),
                ParameterSpec("b", "int")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("a", "vec_uint_type"),
                ParameterSpec("b", "vec_uint_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("a", "vec_uint_type"),
                ParameterSpec("b", "uint")
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("min", "vec_type"),
                ParameterSpec("max", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("min", "float"),
                ParameterSpec("max", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("x", "vec_int_type"),
                ParameterSpec("min", "vec_int_type"),
                ParameterSpec("max", "vec_int_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("x", "vec_int_type"),
                ParameterSpec("min", "int"),
                ParameterSpec("max", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("x", "vec_uint_type"),
                ParameterSpec("min", "vec_uint_type"),
                ParameterSpec("max", "vec_uint_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("x", "vec_uint_type"),
                ParameterSpec("min", "uint"),
                ParameterSpec("max", "uint")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "mix",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type"),
                ParameterSpec("c", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "mix",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type"),
                ParameterSpec("c", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "mix",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type"),
                ParameterSpec("c", "vec_bool_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "fma",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type"),
                ParameterSpec("c", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "step",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "step",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "float"),
                ParameterSpec("b", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "smoothstep",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type"),
                ParameterSpec("c", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "smoothstep",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("a", "float"),
                ParameterSpec("b", "float"),
                ParameterSpec("c", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "isnan",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "isinf",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "floatBitsToInt",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "floatBitsToUint",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "intBitsToFloat",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_int_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "uintBitsToFloat",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_uint_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "length",
            returnType = "float",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "distance",
            returnType = "float",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "dot",
            returnType = "float",
            parameters = listOf(
                ParameterSpec("a", "vec_type"),
                ParameterSpec("b", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "cross",
            returnType = "vec3",
            parameters = listOf(
                ParameterSpec("a", "vec3"),
                ParameterSpec("b", "vec3")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "normalize",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "reflect",
            returnType = "vec3",
            parameters = listOf(
                ParameterSpec("I", "vec3"),
                ParameterSpec("N", "vec3")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "refract",
            returnType = "vec3",
            parameters = listOf(
                ParameterSpec("I", "vec3"),
                ParameterSpec("N", "vec3"),
                ParameterSpec("eta", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "faceforward",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("N", "vec_type"),
                ParameterSpec("I", "vec_type"),
                ParameterSpec("Nref", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "matrixCompMult",
            returnType = "mat_type",
            parameters = listOf(
                ParameterSpec("x", "mat_type"),
                ParameterSpec("y", "mat_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "outerProduct",
            returnType = "mat_type",
            parameters = listOf(
                ParameterSpec("column", "vec_type"),
                ParameterSpec("row", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "transpose",
            returnType = "mat_type",
            parameters = listOf(
                ParameterSpec("m", "mat_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "determinant",
            returnType = "float",
            parameters = listOf(
                ParameterSpec("m", "mat_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "inverse",
            returnType = "mat_type",
            parameters = listOf(
                ParameterSpec("m", "mat_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "lessThan",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "greaterThan",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "lessThanEqual",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "greaterThanEqual",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "equal",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "notEqual",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("y", "vec_type"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "any",
            returnType = "bool",
            parameters = listOf(
                ParameterSpec("x", "vec_bool_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "all",
            returnType = "bool",
            parameters = listOf(
                ParameterSpec("x", "vec_bool_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "not",
            returnType = "vec_bool_type",
            parameters = listOf(
                ParameterSpec("x", "vec_bool_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = "ivec2",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = "ivec2",
            parameters = listOf(
                ParameterSpec("s", "samplerCube"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = "ivec2",
            parameters = listOf(
                ParameterSpec("s", "samplerCubeArray"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = "ivec3",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = "ivec3",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = "vec2",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec2")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = "vec3",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray"),
                ParameterSpec("p", "vec2")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = "vec2",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "vec2")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = "vec2",
            parameters = listOf(
                ParameterSpec("s", "samplerCube"),
                ParameterSpec("p", "vec2")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = "int",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = "int",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = "int",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = "int",
            parameters = listOf(
                ParameterSpec("s", "samplerCube")
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec2"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerCube"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerCubeArray"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerExternalOES"),
                ParameterSpec("p", "vec2"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("bias", "float", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec2"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerCube"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerCubeArray"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("lod", "float")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec2"),
                ParameterSpec("dPdx", "vec2"),
                ParameterSpec("dPdy", "vec2"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("dPdx", "vec2"),
                ParameterSpec("dPdy", "vec2"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("dPdx", "vec2"),
                ParameterSpec("dPdy", "vec2"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerCube"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("dPdx", "vec3"),
                ParameterSpec("dPdy", "vec3"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerCubeArray"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("dPdx", "vec3"),
                ParameterSpec("dPdy", "vec3"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("dPdx", "vec2"),
                ParameterSpec("dPdy", "vec2"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("dPdx", "vec2"),
                ParameterSpec("dPdy", "vec2"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "vec4"),
                ParameterSpec("dPdx", "vec3"),
                ParameterSpec("dPdy", "vec3"),
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "ivec2"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray"),
                ParameterSpec("p", "ivec3"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler3D"),
                ParameterSpec("p", "ivec3"),
                ParameterSpec("lod", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2D"),
                ParameterSpec("p", "vec2"),
                ParameterSpec("comps", "int", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = "gvec4_type",
            parameters = listOf(
                ParameterSpec("s", "gsampler2DArray"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("comps", "int", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("s", "samplerCube"),
                ParameterSpec("p", "vec3"),
                ParameterSpec("comps", "int", isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdx",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdxCoarse",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdxFine",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdy",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdyCoarse",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdyFine",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidth",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidthCoarse",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidthFine",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("p", "vec_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "packHalf2x16",
            returnType = "uint",
            parameters = listOf(
                ParameterSpec("v", "vec2")
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackHalf2x16",
            returnType = "vec2",
            parameters = listOf(
                ParameterSpec("v", "uint")
            ),
        ),

        BuiltinFunctionSpec(
            name = "packUnorm2x16",
            returnType = "uint",
            parameters = listOf(
                ParameterSpec("v", "vec2")
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackUnorm2x16",
            returnType = "vec2",
            parameters = listOf(
                ParameterSpec("v", "uint")
            ),
        ),

        BuiltinFunctionSpec(
            name = "packSnorm2x16",
            returnType = "uint",
            parameters = listOf(
                ParameterSpec("v", "vec2")
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackSnorm2x16",
            returnType = "vec2",
            parameters = listOf(
                ParameterSpec("v", "uint")
            ),
        ),

        BuiltinFunctionSpec(
            name = "packUnorm4x8",
            returnType = "uint",
            parameters = listOf(
                ParameterSpec("v", "vec4")
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackUnorm4x8",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("v", "uint")
            ),
        ),

        BuiltinFunctionSpec(
            name = "packSnorm4x8",
            returnType = "uint",
            parameters = listOf(
                ParameterSpec("v", "vec4")
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackSnorm4x8",
            returnType = "vec4",
            parameters = listOf(
                ParameterSpec("v", "uint")
            ),
        ),

        BuiltinFunctionSpec(
            name = "bitfieldExtract",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("value", "vec_int_type"),
                ParameterSpec("offset", "int"),
                ParameterSpec("bits", "int")
            ),
        ),

        BuiltinFunctionSpec(
            name = "bitfieldExtract",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("value", "vec_uint_type"),
                ParameterSpec("offset", "int"),
                ParameterSpec("bits", "int")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldInsert",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("base", "vec_int_type"),
                ParameterSpec("insert", "vec_int_type"),
                ParameterSpec("offset", "int"),
                ParameterSpec("bits", "int")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldInsert",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("base", "vec_uint_type"),
                ParameterSpec("insert", "vec_uint_type"),
                ParameterSpec("offset", "int"),
                ParameterSpec("bits", "int")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldReverse",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("value", "vec_int_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldReverse",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("value", "vec_uint_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitCount",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("value", "vec_int_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitCount",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("value", "vec_uint_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findLSB",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("value", "vec_int_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findLSB",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("value", "vec_uint_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findMSB",
            returnType = "vec_int_type",
            parameters = listOf(
                ParameterSpec("value", "vec_int_type")
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findMSB",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("value", "vec_uint_type")
            ),
        ),

        BuiltinFunctionSpec(
            name = "imulExtended",
            returnType = "void",
            parameters = listOf(
                ParameterSpec("x", "vec_int_type"),
                ParameterSpec("y", "vec_int_type"),
                ParameterSpec("msb", "vec_int_type", qualifier = ParameterQualifier.OUT),
                ParameterSpec("lsb", "vec_int_type", qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "umulExtended",
            returnType = "void",
            parameters = listOf(
                ParameterSpec("x", "vec_uint_type"),
                ParameterSpec("y", "vec_uint_type"),
                ParameterSpec("msb", "vec_uint_type", qualifier = ParameterQualifier.OUT),
                ParameterSpec("lsb", "vec_uint_type", qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "uaddCarry",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("x", "vec_uint_type"),
                ParameterSpec("y", "vec_uint_type"),
                ParameterSpec("carry", "vec_uint_type", qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "usubBorrow",
            returnType = "vec_uint_type",
            parameters = listOf(
                ParameterSpec("x", "vec_uint_type"),
                ParameterSpec("y", "vec_uint_type"),
                ParameterSpec("borrow", "vec_uint_type", qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "ldexp",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("exp", "vec_int_type", qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "frexp",
            returnType = "vec_type",
            parameters = listOf(
                ParameterSpec("x", "vec_type"),
                ParameterSpec("exp", "vec_int_type", qualifier = ParameterQualifier.OUT)
            ),
        ),
    )
    
}
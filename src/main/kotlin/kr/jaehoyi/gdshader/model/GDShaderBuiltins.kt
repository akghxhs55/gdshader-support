package kr.jaehoyi.gdshader.model

object GDShaderBuiltins {
    
    val GLOBAL_FUNCTIONS = listOf(
        BuiltinFunctionSpec(
            name = "radians",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("degrees", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "degrees",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("radians", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "sin",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "cos",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "tan",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "asin",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "acos",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "atan",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("y_over_x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "atan",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("y", DataType.VEC_TYPE),
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "sinh",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "cosh",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "tanh",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "asinh",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "acosh",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "atanh",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "pow",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "exp",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "exp2",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "log",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "log2",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "sqrt",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "inversesqrt",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "abs",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "abs",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "sign",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "sign",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "floor",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "round",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "roundEven",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "trunc",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "ceil",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fract",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mod",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mod",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "modf",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("i", DataType.VEC_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_INT_TYPE),
                ParameterSpec("b", DataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_INT_TYPE),
                ParameterSpec("b", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_UINT_TYPE),
                ParameterSpec("b", DataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_UINT_TYPE),
                ParameterSpec("b", DataType.UINT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "max",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_INT_TYPE),
                ParameterSpec("b", DataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_INT_TYPE),
                ParameterSpec("b", DataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_UINT_TYPE),
                ParameterSpec("b", DataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_UINT_TYPE),
                ParameterSpec("b", DataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("min", DataType.VEC_TYPE),
                ParameterSpec("max", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("min", DataType.FLOAT),
                ParameterSpec("max", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_INT_TYPE),
                ParameterSpec("min", DataType.VEC_INT_TYPE),
                ParameterSpec("max", DataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_INT_TYPE),
                ParameterSpec("min", DataType.INT),
                ParameterSpec("max", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_UINT_TYPE),
                ParameterSpec("min", DataType.VEC_UINT_TYPE),
                ParameterSpec("max", DataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_UINT_TYPE),
                ParameterSpec("min", DataType.UINT),
                ParameterSpec("max", DataType.UINT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "mix",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE),
                ParameterSpec("c", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mix",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE),
                ParameterSpec("c", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mix",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE),
                ParameterSpec("c", DataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fma",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE),
                ParameterSpec("c", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "step",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "step",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.FLOAT),
                ParameterSpec("b", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "smoothstep",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE),
                ParameterSpec("c", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "smoothstep",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", DataType.FLOAT),
                ParameterSpec("b", DataType.FLOAT),
                ParameterSpec("c", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "isnan",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "isinf",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "floatBitsToInt",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "floatBitsToUint",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "intBitsToFloat",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "uintBitsToFloat",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "length",
            returnType = DataType.FLOAT,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "distance",
            returnType = DataType.FLOAT,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dot",
            returnType = DataType.FLOAT,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC_TYPE),
                ParameterSpec("b", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "cross",
            returnType = DataType.VEC3,
            parameters = listOf(
                ParameterSpec("a", DataType.VEC3),
                ParameterSpec("b", DataType.VEC3)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "normalize",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "reflect",
            returnType = DataType.VEC3,
            parameters = listOf(
                ParameterSpec("I", DataType.VEC3),
                ParameterSpec("N", DataType.VEC3)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "refract",
            returnType = DataType.VEC3,
            parameters = listOf(
                ParameterSpec("I", DataType.VEC3),
                ParameterSpec("N", DataType.VEC3),
                ParameterSpec("eta", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "faceforward",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("N", DataType.VEC_TYPE),
                ParameterSpec("I", DataType.VEC_TYPE),
                ParameterSpec("Nref", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "matrixCompMult",
            returnType = DataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.MAT_TYPE),
                ParameterSpec("y", DataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "outerProduct",
            returnType = DataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("column", DataType.VEC_TYPE),
                ParameterSpec("row", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "transpose",
            returnType = DataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("m", DataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "determinant",
            returnType = DataType.FLOAT,
            parameters = listOf(
                ParameterSpec("m", DataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "inverse",
            returnType = DataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("m", DataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "lessThan",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "greaterThan",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "lessThanEqual",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "greaterThanEqual",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "equal",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "notEqual",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_TYPE),
                ParameterSpec("y", DataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "any",
            returnType = DataType.BOOL,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "all",
            returnType = DataType.BOOL,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "not",
            returnType = DataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", DataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = DataType.IVEC2,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = DataType.IVEC2,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBE),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = DataType.IVEC2,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = DataType.IVEC3,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = DataType.IVEC3,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = DataType.VEC2,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = DataType.VEC3,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY),
                ParameterSpec("p", DataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = DataType.VEC2,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = DataType.VEC2,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBE),
                ParameterSpec("p", DataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = DataType.INT,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = DataType.INT,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = DataType.INT,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = DataType.INT,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC2),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBE),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLEREXTERNALOES),
                ParameterSpec("p", DataType.VEC2),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("bias", DataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC2),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBE),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("lod", DataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC2),
                ParameterSpec("dPdx", DataType.VEC2),
                ParameterSpec("dPdy", DataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("dPdx", DataType.VEC2),
                ParameterSpec("dPdy", DataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("dPdx", DataType.VEC2),
                ParameterSpec("dPdy", DataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBE),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("dPdx", DataType.VEC3),
                ParameterSpec("dPdy", DataType.VEC3),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBEARRAY),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("dPdx", DataType.VEC3),
                ParameterSpec("dPdy", DataType.VEC3),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("dPdx", DataType.VEC2),
                ParameterSpec("dPdy", DataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("dPdx", DataType.VEC2),
                ParameterSpec("dPdy", DataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.VEC4),
                ParameterSpec("dPdx", DataType.VEC3),
                ParameterSpec("dPdy", DataType.VEC3),
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.IVEC2),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY),
                ParameterSpec("p", DataType.IVEC3),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER3D),
                ParameterSpec("p", DataType.IVEC3),
                ParameterSpec("lod", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2D),
                ParameterSpec("p", DataType.VEC2),
                ParameterSpec("comps", DataType.INT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = DataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", DataType.GSAMPLER2DARRAY),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("comps", DataType.INT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", DataType.SAMPLERCUBE),
                ParameterSpec("p", DataType.VEC3),
                ParameterSpec("comps", DataType.INT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdx",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdxCoarse",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdxFine",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdy",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdyCoarse",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdyFine",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidth",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidthCoarse",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidthFine",
            returnType = DataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", DataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packHalf2x16",
            returnType = DataType.UINT,
            parameters = listOf(
                ParameterSpec("v", DataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackHalf2x16",
            returnType = DataType.VEC2,
            parameters = listOf(
                ParameterSpec("v", DataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packUnorm2x16",
            returnType = DataType.UINT,
            parameters = listOf(
                ParameterSpec("v", DataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackUnorm2x16",
            returnType = DataType.VEC2,
            parameters = listOf(
                ParameterSpec("v", DataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packSnorm2x16",
            returnType = DataType.UINT,
            parameters = listOf(
                ParameterSpec("v", DataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackSnorm2x16",
            returnType = DataType.VEC2,
            parameters = listOf(
                ParameterSpec("v", DataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packUnorm4x8",
            returnType = DataType.UINT,
            parameters = listOf(
                ParameterSpec("v", DataType.VEC4)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackUnorm4x8",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("v", DataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packSnorm4x8",
            returnType = DataType.UINT,
            parameters = listOf(
                ParameterSpec("v", DataType.VEC4)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackSnorm4x8",
            returnType = DataType.VEC4,
            parameters = listOf(
                ParameterSpec("v", DataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "bitfieldExtract",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_INT_TYPE),
                ParameterSpec("offset", DataType.INT),
                ParameterSpec("bits", DataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "bitfieldExtract",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_UINT_TYPE),
                ParameterSpec("offset", DataType.INT),
                ParameterSpec("bits", DataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldInsert",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("base", DataType.VEC_INT_TYPE),
                ParameterSpec("insert", DataType.VEC_INT_TYPE),
                ParameterSpec("offset", DataType.INT),
                ParameterSpec("bits", DataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldInsert",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("base", DataType.VEC_UINT_TYPE),
                ParameterSpec("insert", DataType.VEC_UINT_TYPE),
                ParameterSpec("offset", DataType.INT),
                ParameterSpec("bits", DataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldReverse",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldReverse",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitCount",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitCount",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findLSB",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findLSB",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findMSB",
            returnType = DataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findMSB",
            returnType = DataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", DataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
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
        
        BuiltinFunctionSpec(
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
        
        BuiltinFunctionSpec(
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
        
        BuiltinFunctionSpec(
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
        
        BuiltinFunctionSpec(
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
        
        BuiltinFunctionSpec(
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
    
}
package kr.jaehoyi.gdshader.util

object GDShaderBuiltins {
    
    val BUILTIN_FUNCTIONS = listOf(
        BuiltinFunctionSpec(
            name = "radians",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("degrees", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "degrees",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("radians", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "sin",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "cos",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "tan",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "asin",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "acos",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "atan",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("y_over_x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "atan",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("y", GDShaderDataType.VEC_TYPE),
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "sinh",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "cosh",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "tanh",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "asinh",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "acosh",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "atanh",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "pow",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "exp",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "exp2",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "log",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "log2",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "sqrt",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "inversesqrt",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "abs",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "abs",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "sign",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "sign",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "floor",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "round",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "roundEven",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "trunc",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "ceil",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fract",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mod",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mod",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "modf",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("i", GDShaderDataType.VEC_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("b", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "min",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("b", GDShaderDataType.UINT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "max",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("b", GDShaderDataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "max",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("b", GDShaderDataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("min", GDShaderDataType.VEC_TYPE),
                ParameterSpec("max", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("min", GDShaderDataType.FLOAT),
                ParameterSpec("max", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("min", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("max", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("min", GDShaderDataType.INT),
                ParameterSpec("max", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("min", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("max", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "clamp",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("min", GDShaderDataType.UINT),
                ParameterSpec("max", GDShaderDataType.UINT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "mix",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE),
                ParameterSpec("c", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mix",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE),
                ParameterSpec("c", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "mix",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE),
                ParameterSpec("c", GDShaderDataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fma",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE),
                ParameterSpec("c", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "step",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "step",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.FLOAT),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "smoothstep",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE),
                ParameterSpec("c", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "smoothstep",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.FLOAT),
                ParameterSpec("b", GDShaderDataType.FLOAT),
                ParameterSpec("c", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "isnan",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "isinf",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "floatBitsToInt",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "floatBitsToUint",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "intBitsToFloat",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "uintBitsToFloat",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "length",
            returnType = GDShaderDataType.FLOAT,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "distance",
            returnType = GDShaderDataType.FLOAT,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dot",
            returnType = GDShaderDataType.FLOAT,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC_TYPE),
                ParameterSpec("b", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "cross",
            returnType = GDShaderDataType.VEC3,
            parameters = listOf(
                ParameterSpec("a", GDShaderDataType.VEC3),
                ParameterSpec("b", GDShaderDataType.VEC3)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "normalize",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "reflect",
            returnType = GDShaderDataType.VEC3,
            parameters = listOf(
                ParameterSpec("I", GDShaderDataType.VEC3),
                ParameterSpec("N", GDShaderDataType.VEC3)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "refract",
            returnType = GDShaderDataType.VEC3,
            parameters = listOf(
                ParameterSpec("I", GDShaderDataType.VEC3),
                ParameterSpec("N", GDShaderDataType.VEC3),
                ParameterSpec("eta", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "faceforward",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("N", GDShaderDataType.VEC_TYPE),
                ParameterSpec("I", GDShaderDataType.VEC_TYPE),
                ParameterSpec("Nref", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "matrixCompMult",
            returnType = GDShaderDataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.MAT_TYPE),
                ParameterSpec("y", GDShaderDataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "outerProduct",
            returnType = GDShaderDataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("column", GDShaderDataType.VEC_TYPE),
                ParameterSpec("row", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "transpose",
            returnType = GDShaderDataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("m", GDShaderDataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "determinant",
            returnType = GDShaderDataType.FLOAT,
            parameters = listOf(
                ParameterSpec("m", GDShaderDataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "inverse",
            returnType = GDShaderDataType.MAT_TYPE,
            parameters = listOf(
                ParameterSpec("m", GDShaderDataType.MAT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "lessThan",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "greaterThan",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "lessThanEqual",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "greaterThanEqual",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "equal",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "notEqual",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_TYPE),
            ),
        ),

        BuiltinFunctionSpec(
            name = "any",
            returnType = GDShaderDataType.BOOL,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "all",
            returnType = GDShaderDataType.BOOL,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "not",
            returnType = GDShaderDataType.VEC_BOOL_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_BOOL_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = GDShaderDataType.IVEC2,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = GDShaderDataType.IVEC2,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBE),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = GDShaderDataType.IVEC2,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBEARRAY),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = GDShaderDataType.IVEC3,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureSize",
            returnType = GDShaderDataType.IVEC3,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = GDShaderDataType.VEC2,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = GDShaderDataType.VEC3,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY),
                ParameterSpec("p", GDShaderDataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = GDShaderDataType.VEC2,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLod",
            returnType = GDShaderDataType.VEC2,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBE),
                ParameterSpec("p", GDShaderDataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = GDShaderDataType.INT,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = GDShaderDataType.INT,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = GDShaderDataType.INT,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureQueryLevels",
            returnType = GDShaderDataType.INT,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC2),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBE),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBEARRAY),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texture",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLEREXTERNALOES),
                ParameterSpec("p", GDShaderDataType.VEC2),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProj",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("bias", GDShaderDataType.FLOAT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC2),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBE),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureLod",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBEARRAY),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjLod",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("lod", GDShaderDataType.FLOAT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC2),
                ParameterSpec("dPdx", GDShaderDataType.VEC2),
                ParameterSpec("dPdy", GDShaderDataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("dPdx", GDShaderDataType.VEC2),
                ParameterSpec("dPdy", GDShaderDataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("dPdx", GDShaderDataType.VEC2),
                ParameterSpec("dPdy", GDShaderDataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBE),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("dPdx", GDShaderDataType.VEC3),
                ParameterSpec("dPdy", GDShaderDataType.VEC3),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGrad",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBEARRAY),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("dPdx", GDShaderDataType.VEC3),
                ParameterSpec("dPdy", GDShaderDataType.VEC3),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("dPdx", GDShaderDataType.VEC2),
                ParameterSpec("dPdy", GDShaderDataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("dPdx", GDShaderDataType.VEC2),
                ParameterSpec("dPdy", GDShaderDataType.VEC2),
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureProjGrad",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.VEC4),
                ParameterSpec("dPdx", GDShaderDataType.VEC3),
                ParameterSpec("dPdy", GDShaderDataType.VEC3),
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.IVEC2),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY),
                ParameterSpec("p", GDShaderDataType.IVEC3),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "texelFetch",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER3D),
                ParameterSpec("p", GDShaderDataType.IVEC3),
                ParameterSpec("lod", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2D),
                ParameterSpec("p", GDShaderDataType.VEC2),
                ParameterSpec("comps", GDShaderDataType.INT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = GDShaderDataType.GVEC4_TYPE,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.GSAMPLER2DARRAY),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("comps", GDShaderDataType.INT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "textureGather",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("s", GDShaderDataType.SAMPLERCUBE),
                ParameterSpec("p", GDShaderDataType.VEC3),
                ParameterSpec("comps", GDShaderDataType.INT, isOptional = true)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdx",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdxCoarse",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdxFine",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdy",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdyCoarse",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "dFdyFine",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidth",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidthCoarse",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "fwidthFine",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("p", GDShaderDataType.VEC_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packHalf2x16",
            returnType = GDShaderDataType.UINT,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackHalf2x16",
            returnType = GDShaderDataType.VEC2,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packUnorm2x16",
            returnType = GDShaderDataType.UINT,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackUnorm2x16",
            returnType = GDShaderDataType.VEC2,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packSnorm2x16",
            returnType = GDShaderDataType.UINT,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.VEC2)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackSnorm2x16",
            returnType = GDShaderDataType.VEC2,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packUnorm4x8",
            returnType = GDShaderDataType.UINT,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.VEC4)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackUnorm4x8",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "packSnorm4x8",
            returnType = GDShaderDataType.UINT,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.VEC4)
            ),
        ),

        BuiltinFunctionSpec(
            name = "unpackSnorm4x8",
            returnType = GDShaderDataType.VEC4,
            parameters = listOf(
                ParameterSpec("v", GDShaderDataType.UINT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "bitfieldExtract",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("offset", GDShaderDataType.INT),
                ParameterSpec("bits", GDShaderDataType.INT)
            ),
        ),

        BuiltinFunctionSpec(
            name = "bitfieldExtract",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("offset", GDShaderDataType.INT),
                ParameterSpec("bits", GDShaderDataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldInsert",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("base", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("insert", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("offset", GDShaderDataType.INT),
                ParameterSpec("bits", GDShaderDataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldInsert",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("base", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("insert", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("offset", GDShaderDataType.INT),
                ParameterSpec("bits", GDShaderDataType.INT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldReverse",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitfieldReverse",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitCount",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "bitCount",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findLSB",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findLSB",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findMSB",
            returnType = GDShaderDataType.VEC_INT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_INT_TYPE)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "findMSB",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("value", GDShaderDataType.VEC_UINT_TYPE)
            ),
        ),

        BuiltinFunctionSpec(
            name = "imulExtended",
            returnType = GDShaderDataType.VOID,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_INT_TYPE),
                ParameterSpec("msb", GDShaderDataType.VEC_INT_TYPE, qualifier = ParameterQualifier.OUT),
                ParameterSpec("lsb", GDShaderDataType.VEC_INT_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "umulExtended",
            returnType = GDShaderDataType.VOID,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("msb", GDShaderDataType.VEC_UINT_TYPE, qualifier = ParameterQualifier.OUT),
                ParameterSpec("lsb", GDShaderDataType.VEC_UINT_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "uaddCarry",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("carry", GDShaderDataType.VEC_UINT_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "usubBorrow",
            returnType = GDShaderDataType.VEC_UINT_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("y", GDShaderDataType.VEC_UINT_TYPE),
                ParameterSpec("borrow", GDShaderDataType.VEC_UINT_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "ldexp",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("exp", GDShaderDataType.VEC_INT_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),
        
        BuiltinFunctionSpec(
            name = "frexp",
            returnType = GDShaderDataType.VEC_TYPE,
            parameters = listOf(
                ParameterSpec("x", GDShaderDataType.VEC_TYPE),
                ParameterSpec("exp", GDShaderDataType.VEC_INT_TYPE, qualifier = ParameterQualifier.OUT)
            ),
        ),
    )
    
}
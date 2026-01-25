package kr.jaehoyi.gdshader.model

object BuiltinConstructors {

    private val FLOAT = FloatType.DEFAULT
    private val INT = IntType
    private val UINT = UIntType
    private val BOOL = BoolType

    private val VEC2 = VectorType.VEC2
    private val VEC3 = VectorType.VEC3
    private val VEC4 = VectorType.VEC4

    private val IVEC2 = VectorType.IVEC2
    private val IVEC3 = VectorType.IVEC3
    private val IVEC4 = VectorType.IVEC4

    private val UVEC2 = VectorType.UVEC2
    private val UVEC3 = VectorType.UVEC3
    private val UVEC4 = VectorType.UVEC4

    private val BVEC2 = VectorType.BVEC2
    private val BVEC3 = VectorType.BVEC3
    private val BVEC4 = VectorType.BVEC4

    private val MAT2 = MatrixType.MAT2
    private val MAT3 = MatrixType.MAT3
    private val MAT4 = MatrixType.MAT4

    val FLOAT_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(FLOAT, INT, UINT, BOOL).map {
            FunctionSpec("float", FLOAT, listOf(ParameterSpec("x", it)))
        }
    }

    val INT_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(FLOAT, INT, UINT, BOOL).map {
            FunctionSpec("int", INT, listOf(ParameterSpec("x", it)))
        }
    }

    val UINT_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(FLOAT, INT, UINT, BOOL).map {
            FunctionSpec("uint", UINT, listOf(ParameterSpec("x", it)))
        }
    }

    val BOOL_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(FLOAT, INT, UINT, BOOL).map {
            FunctionSpec("bool", BOOL, listOf(ParameterSpec("x", it)))
        }
    }

    val VEC2_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("vec2", VEC2, listOf(ParameterSpec("x", FLOAT))),
            FunctionSpec("vec2", VEC2, listOf(ParameterSpec("x", FLOAT), ParameterSpec("y", FLOAT)))
        )
    }

    val VEC3_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("vec3", VEC3, listOf(ParameterSpec("x", FLOAT))),
            FunctionSpec("vec3", VEC3, listOf(ParameterSpec("x", FLOAT), ParameterSpec("y", FLOAT), ParameterSpec("z", FLOAT))),
            FunctionSpec("vec3", VEC3, listOf(ParameterSpec("xy", VEC2), ParameterSpec("z", FLOAT))),
            FunctionSpec("vec3", VEC3, listOf(ParameterSpec("x", FLOAT), ParameterSpec("yz", VEC2)))
        )
    }

    val VEC4_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("x", FLOAT))),
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("x", FLOAT), ParameterSpec("y", FLOAT), ParameterSpec("z", FLOAT), ParameterSpec("w", FLOAT))),
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("xy", VEC2), ParameterSpec("z", FLOAT), ParameterSpec("w", FLOAT))),
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("x", FLOAT), ParameterSpec("yz", VEC2), ParameterSpec("w", FLOAT))),
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("x", FLOAT), ParameterSpec("y", FLOAT), ParameterSpec("zw", VEC2))),
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("xy", VEC2), ParameterSpec("zw", VEC2))),
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("xyz", VEC3), ParameterSpec("w", FLOAT))),
            FunctionSpec("vec4", VEC4, listOf(ParameterSpec("x", FLOAT), ParameterSpec("yzw", VEC3)))
        )
    }

    val IVEC2_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("ivec2", IVEC2, listOf(ParameterSpec("x", INT))),
            FunctionSpec("ivec2", IVEC2, listOf(ParameterSpec("x", INT), ParameterSpec("y", INT)))
        )
    }

    val IVEC3_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("ivec3", IVEC3, listOf(ParameterSpec("x", INT))),
            FunctionSpec("ivec3", IVEC3, listOf(ParameterSpec("x", INT), ParameterSpec("y", INT), ParameterSpec("z", INT))),
            FunctionSpec("ivec3", IVEC3, listOf(ParameterSpec("xy", IVEC2), ParameterSpec("z", INT))),
            FunctionSpec("ivec3", IVEC3, listOf(ParameterSpec("x", INT), ParameterSpec("yz", IVEC2)))
        )
    }

    val IVEC4_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("x", INT))),
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("x", INT), ParameterSpec("y", INT), ParameterSpec("z", INT), ParameterSpec("w", INT))),
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("xy", IVEC2), ParameterSpec("z", INT), ParameterSpec("w", INT))),
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("x", INT), ParameterSpec("yz", IVEC2), ParameterSpec("w", INT))),
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("x", INT), ParameterSpec("y", INT), ParameterSpec("zw", IVEC2))),
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("xy", IVEC2), ParameterSpec("zw", IVEC2))),
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("xyz", IVEC3), ParameterSpec("w", INT))),
            FunctionSpec("ivec4", IVEC4, listOf(ParameterSpec("x", INT), ParameterSpec("yzw", IVEC3)))
        )
    }

    val UVEC2_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("uvec2", UVEC2, listOf(ParameterSpec("x", UINT))),
            FunctionSpec("uvec2", UVEC2, listOf(ParameterSpec("x", UINT), ParameterSpec("y", UINT)))
        )
    }

    val UVEC3_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("uvec3", UVEC3, listOf(ParameterSpec("x", UINT))),
            FunctionSpec("uvec3", UVEC3, listOf(ParameterSpec("x", UINT), ParameterSpec("y", UINT), ParameterSpec("z", UINT))),
            FunctionSpec("uvec3", UVEC3, listOf(ParameterSpec("xy", UVEC2), ParameterSpec("z", UINT))),
            FunctionSpec("uvec3", UVEC3, listOf(ParameterSpec("x", UINT), ParameterSpec("yz", UVEC2)))
        )
    }

    val UVEC4_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("x", UINT))),
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("x", UINT), ParameterSpec("y", UINT), ParameterSpec("z", UINT), ParameterSpec("w", UINT))),
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("xy", UVEC2), ParameterSpec("z", UINT), ParameterSpec("w", UINT))),
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("x", UINT), ParameterSpec("yz", UVEC2), ParameterSpec("w", UINT))),
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("x", UINT), ParameterSpec("y", UINT), ParameterSpec("zw", UVEC2))),
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("xy", UVEC2), ParameterSpec("zw", UVEC2))),
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("xyz", UVEC3), ParameterSpec("w", UINT))),
            FunctionSpec("uvec4", UVEC4, listOf(ParameterSpec("x", UINT), ParameterSpec("yzw", UVEC3)))
        )
    }

    val BVEC2_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("bvec2", BVEC2, listOf(ParameterSpec("x", BOOL))),
            FunctionSpec("bvec2", BVEC2, listOf(ParameterSpec("x", BOOL), ParameterSpec("y", BOOL)))
        )
    }

    val BVEC3_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("bvec3", BVEC3, listOf(ParameterSpec("x", BOOL))),
            FunctionSpec("bvec3", BVEC3, listOf(ParameterSpec("x", BOOL), ParameterSpec("y", BOOL), ParameterSpec("z", BOOL))),
            FunctionSpec("bvec3", BVEC3, listOf(ParameterSpec("xy", BVEC2), ParameterSpec("z", BOOL))),
            FunctionSpec("bvec3", BVEC3, listOf(ParameterSpec("x", BOOL), ParameterSpec("yz", BVEC2)))
        )
    }

    val BVEC4_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("x", BOOL))),
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("x", BOOL), ParameterSpec("y", BOOL), ParameterSpec("z", BOOL), ParameterSpec("w", BOOL))),
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("xy", BVEC2), ParameterSpec("z", BOOL), ParameterSpec("w", BOOL))),
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("x", BOOL), ParameterSpec("yz", BVEC2), ParameterSpec("w", BOOL))),
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("x", BOOL), ParameterSpec("y", BOOL), ParameterSpec("zw", BVEC2))),
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("xy", BVEC2), ParameterSpec("zw", BVEC2))),
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("xyz", BVEC3), ParameterSpec("w", BOOL))),
            FunctionSpec("bvec4", BVEC4, listOf(ParameterSpec("x", BOOL), ParameterSpec("yzw", BVEC3)))
        )
    }

    val MAT2_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("mat2", MAT2, listOf(ParameterSpec("scalar", FLOAT))),
            FunctionSpec("mat2", MAT2, listOf(
                ParameterSpec("e00", FLOAT), ParameterSpec("e01", FLOAT),
                ParameterSpec("e10", FLOAT), ParameterSpec("e11", FLOAT)
            )),
            FunctionSpec("mat2", MAT2, listOf(ParameterSpec("col0", VEC2), ParameterSpec("col1", VEC2)))
        )
    }

    val MAT3_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("mat3", MAT3, listOf(ParameterSpec("scalar", FLOAT))),
            FunctionSpec("mat3", MAT3, listOf(
                ParameterSpec("e00", FLOAT), ParameterSpec("e01", FLOAT), ParameterSpec("e02", FLOAT),
                ParameterSpec("e10", FLOAT), ParameterSpec("e11", FLOAT), ParameterSpec("e12", FLOAT),
                ParameterSpec("e20", FLOAT), ParameterSpec("e21", FLOAT), ParameterSpec("e22", FLOAT)
            )),
            FunctionSpec("mat3", MAT3, listOf(ParameterSpec("col0", VEC3), ParameterSpec("col1", VEC3), ParameterSpec("col2", VEC3)))
        )
    }

    val MAT4_CONSTRUCTORS: List<FunctionSpec> by lazy {
        listOf(
            FunctionSpec("mat4", MAT4, listOf(ParameterSpec("scalar", FLOAT))),
            FunctionSpec("mat4", MAT4, listOf(
                ParameterSpec("e00", FLOAT), ParameterSpec("e01", FLOAT), ParameterSpec("e02", FLOAT), ParameterSpec("e03", FLOAT),
                ParameterSpec("e10", FLOAT), ParameterSpec("e11", FLOAT), ParameterSpec("e12", FLOAT), ParameterSpec("e13", FLOAT),
                ParameterSpec("e20", FLOAT), ParameterSpec("e21", FLOAT), ParameterSpec("e22", FLOAT), ParameterSpec("e23", FLOAT),
                ParameterSpec("e30", FLOAT), ParameterSpec("e31", FLOAT), ParameterSpec("e32", FLOAT), ParameterSpec("e33", FLOAT)
            )),
            FunctionSpec("mat4", MAT4, listOf(ParameterSpec("col0", VEC4), ParameterSpec("col1", VEC4), ParameterSpec("col2", VEC4), ParameterSpec("col3", VEC4)))
        )
    }

    fun getConstructors(typeName: String): List<FunctionSpec>? {
        return when (typeName) {
            "float" -> FLOAT_CONSTRUCTORS
            "int" -> INT_CONSTRUCTORS
            "uint" -> UINT_CONSTRUCTORS
            "bool" -> BOOL_CONSTRUCTORS
            "vec2" -> VEC2_CONSTRUCTORS
            "vec3" -> VEC3_CONSTRUCTORS
            "vec4" -> VEC4_CONSTRUCTORS
            "ivec2" -> IVEC2_CONSTRUCTORS
            "ivec3" -> IVEC3_CONSTRUCTORS
            "ivec4" -> IVEC4_CONSTRUCTORS
            "uvec2" -> UVEC2_CONSTRUCTORS
            "uvec3" -> UVEC3_CONSTRUCTORS
            "uvec4" -> UVEC4_CONSTRUCTORS
            "bvec2" -> BVEC2_CONSTRUCTORS
            "bvec3" -> BVEC3_CONSTRUCTORS
            "bvec4" -> BVEC4_CONSTRUCTORS
            "mat2" -> MAT2_CONSTRUCTORS
            "mat3" -> MAT3_CONSTRUCTORS
            "mat4" -> MAT4_CONSTRUCTORS
            else -> null
        }
    }

    fun getConstructors(type: DataType): List<FunctionSpec> {
        return when (type) {
            is FloatType -> FLOAT_CONSTRUCTORS
            is IntType -> INT_CONSTRUCTORS
            is UIntType -> UINT_CONSTRUCTORS
            is BoolType -> BOOL_CONSTRUCTORS
            is VectorType -> getVectorConstructors(type)
            is MatrixType -> getMatrixConstructors(type)
            is StructType -> getStructConstructors(type)
            is ArrayType -> getArrayConstructors(type)
            else -> emptyList()
        }
    }

    private fun getVectorConstructors(vectorType: VectorType): List<FunctionSpec> {
        return when (vectorType) {
            VectorType.VEC2 -> VEC2_CONSTRUCTORS
            VectorType.VEC3 -> VEC3_CONSTRUCTORS
            VectorType.VEC4 -> VEC4_CONSTRUCTORS
            VectorType.IVEC2 -> IVEC2_CONSTRUCTORS
            VectorType.IVEC3 -> IVEC3_CONSTRUCTORS
            VectorType.IVEC4 -> IVEC4_CONSTRUCTORS
            VectorType.UVEC2 -> UVEC2_CONSTRUCTORS
            VectorType.UVEC3 -> UVEC3_CONSTRUCTORS
            VectorType.UVEC4 -> UVEC4_CONSTRUCTORS
            VectorType.BVEC2 -> BVEC2_CONSTRUCTORS
            VectorType.BVEC3 -> BVEC3_CONSTRUCTORS
            VectorType.BVEC4 -> BVEC4_CONSTRUCTORS
            else -> emptyList()
        }
    }

    private fun getMatrixConstructors(matrixType: MatrixType): List<FunctionSpec> {
        return when (matrixType) {
            MatrixType.MAT2 -> MAT2_CONSTRUCTORS
            MatrixType.MAT3 -> MAT3_CONSTRUCTORS
            MatrixType.MAT4 -> MAT4_CONSTRUCTORS
            else -> emptyList()
        }
    }

    private fun getStructConstructors(structType: StructType): List<FunctionSpec> {
        val parameters = structType.members.map { (name, type) ->
            ParameterSpec(name, type)
        }
        return listOf(
            FunctionSpec(
                name = structType.name,
                returnType = structType,
                parameters = parameters
            )
        )
    }

    private fun getArrayConstructors(arrayType: ArrayType): List<FunctionSpec> {
        val size = arrayType.containerSize ?: return emptyList()
        val parameters = (0 until size).map { index ->
            ParameterSpec("element$index", arrayType.elementType)
        }
        return listOf(
            FunctionSpec(
                name = arrayType.name,
                returnType = arrayType,
                parameters = parameters
            )
        )
    }
}

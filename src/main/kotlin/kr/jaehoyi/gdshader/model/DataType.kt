package kr.jaehoyi.gdshader.model

import com.intellij.openapi.diagnostic.Logger

interface Instantiable {
    // fun getConstructors(): List<FunctionSpec>
}

interface MemberAccessible {
    fun resolveMember(name: String): DataType?
}

interface Indexable {
    val elementType: DataType
    val containerSize: Int?
}

sealed class DataType(
    val name: String
) {
    
    open val presentationText: String
        get() = name
    
    val isInstantiable: Boolean 
        get() = this is Instantiable
    
    open val isDeclarable: Boolean = true
    
    open val isOpaque: Boolean = false
    
    open val isValidReturnType: Boolean
        get() = (this is VoidType) || (isDeclarable && !isOpaque)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataType) return false
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
    
}

sealed class Scalar(name: String) : DataType(name), Instantiable

class FloatType private constructor(
    val precision: Precision = Precision.DEFAULT
) : Scalar("float") {
    
    override val presentationText: String
        get() = if (precision == Precision.DEFAULT) name else "${precision.text} $name"
    
    companion object {
        val DEFAULT = FloatType(Precision.DEFAULT)
        val HIGH = FloatType(Precision.HIGH)
        val MEDIUM = FloatType(Precision.MEDIUM)
        val LOW = FloatType(Precision.LOW)

        fun of(precision: Precision): FloatType = when (precision) {
            Precision.DEFAULT -> DEFAULT
            Precision.HIGH -> HIGH
            Precision.MEDIUM -> MEDIUM
            Precision.LOW -> LOW
        }
    }
    
}

object IntType : Scalar("int")
object UIntType : Scalar("uint")
object BoolType : Scalar("bool")
object VoidType : DataType("void") {
    override val isDeclarable: Boolean = false
}

class SamplerType private constructor(
    name: String
) : DataType(name) {

    override val isDeclarable: Boolean = false
    override val isOpaque: Boolean = true
    
    companion object {
        val SAMPLER2D = SamplerType("sampler2D")
        val ISAMPLER2D = SamplerType("isampler2D")
        val USAMPLER2D = SamplerType("usampler2D")
        val SAMPLER2DARRAY = SamplerType("sampler2DArray")
        val ISAMPLER2DARRAY = SamplerType("isampler2DArray")
        val USAMPLER2DARRAY = SamplerType("usampler2DArray")
        val SAMPLER3D = SamplerType("sampler3D")
        val ISAMPLER3D = SamplerType("isampler3D")
        val USAMPLER3D = SamplerType("usampler3D")
        val SAMPLERCUBE = SamplerType("samplerCube")
        val SAMPLERCUBEARRAY = SamplerType("samplerCubeArray")
        val SAMPLEREXTERNALOES = SamplerType("samplerExternalOES")
        
        private val map = listOf(
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
            SAMPLEREXTERNALOES
        ).associateBy { it.name }
        
        fun fromName(name: String): SamplerType? = map[name]
    }
    
}

class AliasType private constructor(
    name: String
) : DataType(name) {

    override val isDeclarable: Boolean = false
    
    companion object {
        val VEC_TYPE = AliasType("vec_type")
        val VEC_INT_TYPE = AliasType("vec_int_type")
        val VEC_UINT_TYPE = AliasType("vec_uint_type")
        val VEC_BOOL_TYPE = AliasType("vec_bool_type")
        val MAT_TYPE = AliasType("mat_type")
        val GVEC4_TYPE = AliasType("gvec4_type")
        val GSAMPLER2D = AliasType("gsampler2D")
        val GSAMPLER2DARRAY = AliasType("gsampler2DArray")
        val GSAMPLER3D = AliasType("gsampler3D")
    }
    
}

class VectorType private constructor(
    override val elementType: Scalar,
    override val containerSize: Int
) : DataType(
    name = when (elementType) {
        is FloatType -> "vec$containerSize"
        is IntType -> "ivec$containerSize"
        is UIntType -> "uvec$containerSize"
        is BoolType -> "bvec$containerSize"
    }
), Instantiable, MemberAccessible, Indexable {

    override val presentationText: String
        get() = if (elementType is FloatType && elementType.precision != Precision.DEFAULT) {
            "${elementType.precision.text} $name"
        } else {
            name
        }
    
    companion object {
        val VEC2 = VectorType(FloatType.DEFAULT, 2)
        val VEC3 = VectorType(FloatType.DEFAULT, 3)
        val VEC4 = VectorType(FloatType.DEFAULT, 4)
        val IVEC2 = VectorType(IntType, 2)
        val IVEC3 = VectorType(IntType, 3)
        val IVEC4 = VectorType(IntType, 4)
        val UVEC2 = VectorType(UIntType, 2)
        val UVEC3 = VectorType(UIntType, 3)
        val UVEC4 = VectorType(UIntType, 4)
        val BVEC2 = VectorType(BoolType, 2)
        val BVEC3 = VectorType(BoolType, 3)
        val BVEC4 = VectorType(BoolType, 4)
        
        fun of(elementType: Scalar, size: Int): VectorType? = when (elementType) {
            is FloatType ->
                if (elementType.precision == Precision.DEFAULT) {
                    when (size) {
                        2 -> VEC2
                        3 -> VEC3
                        4 -> VEC4
                        else -> null
                    }
                } else {
                    VectorType(elementType, size)
                }
            is IntType -> when (size) {
                2 -> IVEC2
                3 -> IVEC3
                4 -> IVEC4
                else -> null
            }
            is UIntType -> when (size) {
                2 -> UVEC2
                3 -> UVEC3
                4 -> UVEC4
                else -> null
            }
            is BoolType -> when (size) {
                2 -> BVEC2
                3 -> BVEC3
                4 -> BVEC4
                else -> null
            }
        }
        
        private val map = listOf(
            VEC2, VEC3, VEC4,
            IVEC2, IVEC3, IVEC4,
            UVEC2, UVEC3, UVEC4,
            BVEC2, BVEC3, BVEC4
        ).associateBy { it.name }
        
        fun fromName(name: String): VectorType? = map[name]
    }
    
    override fun resolveMember(name: String): DataType? {
        if (name.length !in 1..4) {
            return null
        }
        
        for (char in name) {
            val valid = when (char) {
                'x', 'r', 's' -> 0 < containerSize
                'y', 'g', 't' -> 1 < containerSize
                'z', 'b', 'p' -> 2 < containerSize
                'w', 'a', 'q' -> 3 < containerSize
                else -> false
            }
            if (!valid) {
                return null
            }
        }
        
        return if (name.length == 1) {
            elementType
        } else {
            of(elementType, name.length)
        }
    }
    
}

data class StructType(
    private val structName: String,
    val members: Map<String, DataType>
) : DataType(structName), MemberAccessible, Instantiable {

    override fun resolveMember(name: String): DataType? = members[name]
    
}

open class ArrayType(
    override val elementType: DataType,
    override val containerSize: Int? = null,
) : DataType(
    name = if (containerSize != null) "${elementType.name}[$containerSize]" else "${elementType.name}[]"
), Instantiable, Indexable {

    override val isOpaque: Boolean
        get() = elementType.isOpaque
    
}

class MatrixType private constructor(
    val colType: VectorType
) : DataType(
    "mat${colType.containerSize}"
), Instantiable, Indexable {

    override val elementType: DataType
        get() = colType
    override val containerSize: Int
        get() = colType.containerSize

    override val presentationText: String
        get() {
            val floatType = colType.elementType as? FloatType
            if (floatType != null && floatType.precision != Precision.DEFAULT) {
                return "${floatType.precision.text} $name"
            }
            return name
        }
    
    companion object {
        val MAT2 = MatrixType(VectorType.VEC2)
        val MAT3 = MatrixType(VectorType.VEC3)
        val MAT4 = MatrixType(VectorType.VEC4)
        
        fun of(colType: VectorType): MatrixType? {
            if (colType.elementType !is FloatType) {
                return null
            }
            if (colType.elementType.precision != Precision.DEFAULT) {
                return MatrixType(colType)
            }
            return when (colType.containerSize) {
                2 -> MAT2
                3 -> MAT3
                4 -> MAT4
                else -> null
            }
        }
        
        private val map = listOf(
            MAT2,
            MAT3,
            MAT4
        ).associateBy { it.name }
        
        fun fromName(name: String): MatrixType? = map[name]
    }
    
}

enum class Precision(val text: String) {

    DEFAULT(""),
    HIGH("highp"),
    MEDIUM("mediump"),
    LOW("lowp");

    companion object {
        private val LOG = Logger.getInstance(Precision::class.java)
        
        fun fromText(text: String?): Precision = when (text) {
            "highp" -> HIGH
            "mediump" -> MEDIUM
            "lowp" -> LOW
            null, "" -> DEFAULT
            else -> {
                LOG.warn("Unknown precision text: $text")
                DEFAULT
            }
        }
    }

}


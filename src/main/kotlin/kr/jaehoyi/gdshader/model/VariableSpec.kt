package kr.jaehoyi.gdshader.model

import com.intellij.openapi.diagnostic.Logger

sealed interface VariableSpec {
    val name: String
    val type: DataType
    val description: String?
    val presentationTypeText: String
    val isMutable: Boolean
}

data class UniformSpec(
    override val name: String,
    override val type: DataType,
    val qualifier: UniformQualifier,
    override val description: String? = null,
) : VariableSpec {

    override val presentationTypeText: String
        get() {
            val prefix = if (qualifier == UniformQualifier.NORMAL) "" else "${qualifier.text} "
            return "${prefix}uniform ${type.presentationText}"
        }

    override val isMutable: Boolean
        get() = false
    
}

data class VaryingSpec(
    override val name: String,
    override val type: DataType,
    val qualifier: InterpolationQualifier = InterpolationQualifier.SMOOTH,
    override val description: String? = null,
) : VariableSpec {

    override val presentationTypeText: String
        get() {
            val prefix = if (qualifier == InterpolationQualifier.SMOOTH) "" else "${qualifier.text} "
            return "${prefix}varying ${type.presentationText}"
        }

    override val isMutable: Boolean
        get() = true
    
}

data class ConstantSpec(
    override val name: String,
    override val type: DataType,
    override val description: String? = null,
) : VariableSpec {

    override val presentationTypeText: String
        get() = "const ${type.presentationText}"

    override val isMutable: Boolean
        get() = false
    
}

data class ParameterSpec(
    override val name: String,
    override val type: DataType,
    val qualifier: ParameterQualifier = ParameterQualifier.IN,
    val isOptional: Boolean = false,
    override val description: String? = null,
) : VariableSpec {

    override val presentationTypeText: String
        get() {
            val prefix = if (qualifier == ParameterQualifier.IN) "" else "${qualifier.text} "
            return "${prefix}${type.presentationText}"
        } 

    override val isMutable: Boolean
        get() = qualifier == ParameterQualifier.OUT || qualifier == ParameterQualifier.INOUT
    
}

data class LocalVariableSpec(
    override val name: String,
    override val type: DataType,
    override val description: String? = null,
) : VariableSpec {

    override val presentationTypeText: String
        get() = type.presentationText

    override val isMutable: Boolean
        get() = true
    
}

enum class UniformQualifier(val text: String) {
    NORMAL(""),
    GLOBAL("global"),
    LOCAL("local");
    
    companion object {
        val LOG = Logger.getInstance(UniformQualifier::class.java)
        
        fun fromText(text: String?): UniformQualifier = when (text) {
            "global" -> GLOBAL
            "local" -> LOCAL
            null, "" -> NORMAL
            else -> {
                LOG.warn("Unknown UniformQualifier text: $text")
                NORMAL
            }
        }
    }
}

enum class InterpolationQualifier(val text: String) {
    FLAT("flat"),
    SMOOTH("smooth");
    
    companion object {
        val LOG = Logger.getInstance(InterpolationQualifier::class.java)
        
        fun fromText(text: String?): InterpolationQualifier = when (text) {
            "flat" -> FLAT
            "smooth" -> SMOOTH
            null, "" -> SMOOTH
            else -> {
                LOG.warn("Unknown InterpolationQualifier text: $text")
                SMOOTH
            }
        }
    }
}

enum class ParameterQualifier(val text: String) {
    IN("in"),
    OUT("out"),
    INOUT("inout"),
    CONST("const"),
    CONST_IN("const in");
    
    companion object {
        val LOG = Logger.getInstance(ParameterQualifier::class.java)
        
        fun fromText(text: String?): ParameterQualifier = when (text) {
            "in" -> IN
            "out" -> OUT
            "inout" -> INOUT
            "const" -> CONST
            "const in" -> CONST_IN
            null, "" -> IN
            else -> {
                LOG.warn("Unknown ParameterQualifier text: $text")
                IN
            }
        }
    }
}

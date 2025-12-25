package kr.jaehoyi.gdshader.model

data class ParameterSpec(
    val name: String,
    val type: DataType,
    val qualifier: ParameterQualifier = ParameterQualifier.NONE,
    val isOptional: Boolean = false
)

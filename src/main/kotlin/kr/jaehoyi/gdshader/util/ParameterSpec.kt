package kr.jaehoyi.gdshader.util

data class ParameterSpec(
    val name: String,
    val type: String,
    val qualifier: ParameterQualifier = ParameterQualifier.NONE,
    val isOptional: Boolean = false
)

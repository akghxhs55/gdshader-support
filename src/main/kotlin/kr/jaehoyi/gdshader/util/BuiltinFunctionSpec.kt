package kr.jaehoyi.gdshader.util

data class BuiltinFunctionSpec(
    val name: String,
    val returnType: String,
    val parameters: List<ParameterSpec>,
    val description: String? = null
)

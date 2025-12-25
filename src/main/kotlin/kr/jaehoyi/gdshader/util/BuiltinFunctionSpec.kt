package kr.jaehoyi.gdshader.util

data class BuiltinFunctionSpec(
    val name: String,
    val returnType: GDShaderDataType,
    val parameters: List<ParameterSpec>,
    val description: String? = null
)

package kr.jaehoyi.gdshader.model

data class FunctionSpec(
    val name: String,
    val returnType: DataType,
    val parameters: List<ParameterSpec>,
    val description: String? = null
)

package kr.jaehoyi.gdshader.model

data class VariableSpec(
    val name: String,
    val type: DataType,
    val precision: Precision = Precision.DEFAULT,
    val arraySize: Int? = null,
    val storageQualifier: StorageQualifier = StorageQualifier.LOCAL,
    val parameterQualifier: ParameterQualifier = ParameterQualifier.NONE,
    val interpolationQualifier: InterpolationQualifier = InterpolationQualifier.DEFAULT,
    val constKeyword: Boolean = false,
    val description: String? = null
) {
    val isReadOnly: Boolean
        get() = constKeyword || parameterQualifier == ParameterQualifier.IN
}

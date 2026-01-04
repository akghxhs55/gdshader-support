package kr.jaehoyi.gdshader.model

data class VariableSpec(
    val name: String,
    val type: DataType,
    val precision: Precision = Precision.DEFAULT,
    val array: Boolean = false,
    val storageQualifier: StorageQualifier = StorageQualifier.LOCAL,
    val parameterQualifier: ParameterQualifier = ParameterQualifier.NONE,
    val interpolationQualifier: InterpolationQualifier = InterpolationQualifier.DEFAULT,
    val description: String? = null
) {
    val isReadOnly: Boolean
        get() = 
            storageQualifier == StorageQualifier.CONSTANT ||
            storageQualifier == StorageQualifier.UNIFORM ||
            storageQualifier == StorageQualifier.GLOBAL_UNIFORM ||
            storageQualifier == StorageQualifier.INSTANCE_UNIFORM ||
            parameterQualifier == ParameterQualifier.IN ||
            parameterQualifier == ParameterQualifier.CONST ||
            parameterQualifier == ParameterQualifier.CONST_IN
}

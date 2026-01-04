package kr.jaehoyi.gdshader.model

enum class StorageQualifier(val text: String) {
    LOCAL(""),
    PARAMETER(""),
    CONSTANT("const"),
    UNIFORM("uniform"),
    GLOBAL_UNIFORM("global uniform"),
    INSTANCE_UNIFORM("instance uniform"),
    VARYING("varying"),
}
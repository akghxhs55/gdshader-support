package kr.jaehoyi.gdshader.util

enum class ParameterQualifier(val text: String) {
    NONE(""),
    IN("in"),
    OUT("out"),
    INOUT("inout"),
    CONST("const"),
    CONST_IN("const in");
}
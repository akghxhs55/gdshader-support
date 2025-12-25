package kr.jaehoyi.gdshader.model

enum class FunctionContext(val text: String) {
    
    COMMON("common"),
    VERTEX("vertex"),
    FRAGMENT("fragment"),
    LIGHT("light"),
    START("start"),
    PROCESS("process"),
    SKY("sky"),
    FOG("fog");
    
}
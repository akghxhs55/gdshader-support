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
    
    companion object {
        fun fromText(text: String): FunctionContext = when (text) {
            "vertex" -> VERTEX
            "fragment" -> FRAGMENT
            "light" -> LIGHT
            "start" -> START
            "process" -> PROCESS
            "sky" -> SKY
            "fog" -> FOG
            else -> COMMON
        }
    }
    
}

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
        private val textToFunctionContextMap = FunctionContext.entries.associateBy { it.text }
        
        fun fromText(text: String): FunctionContext? 
            = textToFunctionContextMap[text]
    }
    
}
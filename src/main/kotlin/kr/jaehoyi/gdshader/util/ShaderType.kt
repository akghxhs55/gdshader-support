package kr.jaehoyi.gdshader.util

enum class ShaderType(val text: String) {
    
    SPATIAL("spatial"),
    CANVAS_ITEM("canvas_item"),
    PARTICLES("particles"),
    SKY("sky"),
    FOG("fog");
    
    companion object {
        private val textToTypeMap = entries.associateBy { it.text }
        
        fun fromText(text: String): ShaderType? {
            return textToTypeMap[text]
        }
    }
    
}
package kr.jaehoyi.gdshader.model

enum class ShaderType(val text: String) {
    
    SPATIAL("spatial"),
    CANVAS_ITEM("canvas_item"),
    PARTICLES("particles"),
    SKY("sky"),
    FOG("fog");
    
    companion object {
        private val textToShaderTypeMap = entries.associateBy { it.text }
        
        fun fromText(text: String): ShaderType? =
            textToShaderTypeMap[text]
    }
    
}
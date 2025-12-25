package kr.jaehoyi.gdshader.model

enum class Precision(val text: String) {
    DEFAULT(""),
    HIGH("highp"),
    MEDIUM("mediump"),
    LOW("lowp");
    
    companion object {
        private val textToPrecisionMap = entries.associateBy { it.text }
        
        fun fromText(text: String): Precision? {
            return textToPrecisionMap[text]
        }
    }
}
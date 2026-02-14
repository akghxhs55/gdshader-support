package kr.jaehoyi.gdshader.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class GdsCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings("GdsCodeStyleSettings", container) {

    @JvmField var SPACE_BEFORE_FOR_SEMICOLON = false
    @JvmField var SPACE_AFTER_FOR_SEMICOLON = true
    
    @JvmField var SPACE_BEFORE_HINT_COLON: Boolean = true
    @JvmField var SPACE_AFTER_HINT_COLON: Boolean = true
    
    @JvmField var SPACE_BEFORE_TERNARY_COLON: Boolean = true
    @JvmField var SPACE_AFTER_TERNARY_COLON: Boolean = true
    
    @JvmField var SPACE_BEFORE_CASE_COLON: Boolean = false
}
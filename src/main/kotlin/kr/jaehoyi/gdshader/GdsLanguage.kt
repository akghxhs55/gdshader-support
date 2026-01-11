package kr.jaehoyi.gdshader

import com.intellij.lang.Language

object GdsLanguage : Language("GDShader") {
    
    private fun readResolve(): Any = GdsLanguage
    
}
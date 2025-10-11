package kr.jaehoyi.gdshader

import com.intellij.lang.Language

object GDShaderLanguage : Language("GDShader") {
    
    private fun readResolve(): Any = GDShaderLanguage
    
}
package kr.jaehoyi.gdshader

import com.intellij.lang.Language

object GdsLanguage : Language("kr.jaehoyi.gdshader") {
    override fun getDisplayName(): String = "GDShader"

    private fun readResolve(): Any = GdsLanguage
}

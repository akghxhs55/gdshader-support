package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.GDShaderLanguage

class GDShaderTokenType(debugName: String) : IElementType(debugName, GDShaderLanguage) {
    override fun toString(): String = "GDShaderTokenType." + super.toString()
}
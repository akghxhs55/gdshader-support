package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.GdsLanguage

class GdsTokenType(debugName: String) : IElementType(debugName, GdsLanguage) {
    
    override fun toString(): String = "GDShaderTokenType." + super.toString()
    
}
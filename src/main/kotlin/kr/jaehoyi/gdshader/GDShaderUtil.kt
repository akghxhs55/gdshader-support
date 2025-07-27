package kr.jaehoyi.gdshader

import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration

object GDShaderUtil {
    private val shaderTypes = setOf("spatial", "canvas_item", "particles", "sky", "fog")
    
    fun getAvailableShaderTypes(): Set<String> = shaderTypes
    fun validateShaderName(element: GDShaderShaderTypeDeclaration): Boolean = element.shaderType in shaderTypes
}
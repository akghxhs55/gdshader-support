package kr.jaehoyi.gdshader.psi.impl

import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration

object GDShaderPsiImplUtil {
    // Shader Type Declaration
    @JvmStatic
    fun getShaderType(element: GDShaderShaderTypeDeclaration): String = element.shaderTypeName.text
}
package kr.jaehoyi.gdshader.psi.impl

import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration

object GDShaderPsiImplUtil {
    @JvmStatic
    fun getShaderType(element: GDShaderShaderTypeDeclaration): String = element.shaderTypeName.text
}
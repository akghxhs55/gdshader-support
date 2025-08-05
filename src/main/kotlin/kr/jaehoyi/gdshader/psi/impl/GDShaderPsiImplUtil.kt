package kr.jaehoyi.gdshader.psi.impl

import kr.jaehoyi.gdshader.psi.GDShaderPrimary
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration

object GDShaderPsiImplUtil {
    // Shader Type Declaration
    @JvmStatic fun getShaderType(element: GDShaderShaderTypeDeclaration): String = element.shaderTypeName?.text ?: ""
    
    // Primary
    @JvmStatic fun isLiteral(element: GDShaderPrimary): Boolean = element.literal != null
    @JvmStatic fun isFunctionCall(element: GDShaderPrimary): Boolean = element.functionCall != null
    @JvmStatic fun isExpression(element: GDShaderPrimary): Boolean = element.expression != null
    @JvmStatic fun isVariableName(element: GDShaderPrimary): Boolean = element.variableName != null
}
package kr.jaehoyi.gdshader.psi.impl

import kr.jaehoyi.gdshader.psi.GDShaderPrimary
import kr.jaehoyi.gdshader.psi.GDShaderRenderModeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration

object GDShaderPsiImplUtil {
    
    // Shader Type Declaration
    @JvmStatic fun getShaderType(element: GDShaderShaderTypeDeclaration): String = element.shaderTypeName?.text ?: ""
    
    // Render Mode Dec
    @JvmStatic fun getRenderModes(element: GDShaderRenderModeDeclaration): List<String> 
        = element.renderModeDeclaratorList?.renderModeNameList?.map { it.text } ?: emptyList()
    
    // Primary
    @JvmStatic fun isLiteral(element: GDShaderPrimary): Boolean = element.literal != null
    @JvmStatic fun isFunctionCall(element: GDShaderPrimary): Boolean = element.functionCall != null
    @JvmStatic fun isExpression(element: GDShaderPrimary): Boolean = element.expression != null
    @JvmStatic fun isVariableName(element: GDShaderPrimary): Boolean = element.variableName != null
    
}
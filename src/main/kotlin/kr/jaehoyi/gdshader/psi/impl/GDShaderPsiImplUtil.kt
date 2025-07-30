package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.psi.GDShaderConstVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderGlobalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderUniformVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVariableName
import kr.jaehoyi.gdshader.psi.GDShaderVaryingVariableDeclaration

object GDShaderPsiImplUtil {
    // Shader Type Declaration
    @JvmStatic
    fun getShaderType(element: GDShaderShaderTypeDeclaration): String = element.shaderTypeName.text
    
    // Global Variable Declaration
    @JvmStatic
    fun getVariableNameElements(element: GDShaderGlobalVariableDeclaration): List<GDShaderVariableName> {
        val uniform = PsiTreeUtil.getChildOfType(element, GDShaderUniformVariableDeclaration::class.java)
        if (uniform != null) return listOf(uniform.variableName)
        
        val const = PsiTreeUtil.getChildOfType(element, GDShaderConstVariableDeclaration::class.java)
        if (const != null) return const.variableDeclaratorList.variableDeclaratorList.map { it.variableName }
        
        val varying = PsiTreeUtil.getChildOfType(element, GDShaderVaryingVariableDeclaration::class.java)
        if (varying != null) return listOf(varying.variableName)
        
        return emptyList()
    }
}
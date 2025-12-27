package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderItem

object GDShaderPsiImplUtil {
    
    fun getShaderType(file: GDShaderFile): ShaderType? {
        for (child in file.children) {
            if (child !is GDShaderItem) continue
            val shaderTypeDeclaration = child.topLevelDeclaration.shaderTypeDeclaration ?: continue
            
            val typeText = shaderTypeDeclaration.shaderTypeName?.text ?: return null
            return ShaderType.fromText(typeText)
        }
        
        return null
    }
    
    fun getFunctionContext(element: PsiElement): FunctionContext? {
        val functionDeclaration = element.parentOfType<GDShaderFunctionDeclaration>() ?: return null
        
        return FunctionContext.fromText(functionDeclaration.functionNameDecl.text)
    }
    
}
package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.psi.GDShaderElementFactory
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderItem
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameRef
import kr.jaehoyi.gdshader.reference.GDShaderReference

object GDShaderPsiImplUtil {

    @JvmStatic
    fun getName(element: GDShaderVariableNameDecl): String =
        element.text

    @JvmStatic
    fun setName(element: GDShaderVariableNameDecl, newName: String): PsiElement? {
        val identifier = GDShaderElementFactory.createIdentifier(element.project, newName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
    
    @JvmStatic
    fun getNameIdentifier(element: GDShaderVariableNameDecl): PsiElement? =
        element.node.findChildByType(GDShaderTypes.IDENTIFIER)?.psi
    
    @JvmStatic
    fun getReference(element: GDShaderVariableNameRef): PsiReference =
        GDShaderReference(element, element.textRangeInParent)
    
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
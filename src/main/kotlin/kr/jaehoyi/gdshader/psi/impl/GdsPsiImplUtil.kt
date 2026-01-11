package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.reference.GdsReference

object GdsPsiImplUtil {

    @JvmStatic
    fun getName(element: GdsVariableNameDecl): String =
        element.text

    @JvmStatic
    fun setName(element: GdsVariableNameDecl, newName: String): PsiElement? {
        val identifier = GdsElementFactory.createIdentifier(element.project, newName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
    
    @JvmStatic
    fun getNameIdentifier(element: GdsVariableNameDecl): PsiElement? =
        element.node.findChildByType(GdsTypes.IDENTIFIER)?.psi
    
    @JvmStatic
    fun getReference(element: GdsVariableNameRef): PsiReference =
        GdsReference(element, element.textRangeInParent)
    
    @JvmStatic
    fun getName(element: GdsFunctionNameDecl): String =
        element.text
    
    @JvmStatic
    fun setName(element: GdsFunctionNameDecl, newName: String): PsiElement? {
        val identifier = GdsElementFactory.createIdentifier(element.project, newName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
    
    @JvmStatic
    fun getNameIdentifier(element: GdsFunctionNameDecl): PsiElement? =
        element.node.findChildByType(GdsTypes.IDENTIFIER)?.psi
    
    @JvmStatic
    fun getReference(element: GdsFunctionNameRef): PsiReference =
        GdsReference(element, element.textRangeInParent)
    
    fun getShaderType(file: GdsFile): ShaderType? {
        for (child in file.children) {
            if (child !is GdsItem) continue
            val shaderTypeDeclaration = child.topLevelDeclaration.shaderTypeDeclaration ?: continue
            
            val typeText = shaderTypeDeclaration.shaderTypeName?.text ?: return null
            return ShaderType.fromText(typeText)
        }
        
        return null
    }
    
    fun getShaderType(element: PsiElement): ShaderType? {
        val gdsFile = element.containingFile as? GdsFile ?: return null
        return getShaderType(gdsFile)
    }
    
    fun getFunctionContext(element: PsiElement): FunctionContext? {
        val functionDeclaration = element.parentOfType<GdsFunctionDeclaration>() ?: return null
        
        return FunctionContext.fromText(functionDeclaration.functionNameDecl.text)
    }
    
}
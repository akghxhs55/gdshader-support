package kr.jaehoyi.gdshader.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.InterpolationQualifier
import kr.jaehoyi.gdshader.model.LocalVariableSpec
import kr.jaehoyi.gdshader.model.ParameterQualifier
import kr.jaehoyi.gdshader.model.ParameterSpec
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.model.UniformQualifier
import kr.jaehoyi.gdshader.model.UniformSpec
import kr.jaehoyi.gdshader.model.VariableSpec
import kr.jaehoyi.gdshader.model.VaryingSpec
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsElementFactory
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsForInit
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaratorList
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructNameRef
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.reference.GdsFunctionReference
import kr.jaehoyi.gdshader.reference.GdsStructReference
import kr.jaehoyi.gdshader.reference.GdsVariableReference

import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef
import kr.jaehoyi.gdshader.reference.GdsStructMemberReference

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
    fun getVariableSpec(element: GdsVariableNameDecl): VariableSpec? {
            val declarator = element.parent ?: return null
            return when (declarator) {
                is GdsUniformDeclaration -> {
                    val type = GdsDataTypeFactory.createFromUniformDeclaration(declarator) ?: return null
                    val qualifierText = declarator.uniformHeader.uniformQualifier?.text
                    val qualifier = UniformQualifier.fromText(qualifierText)

                    UniformSpec(
                        name = element.name,
                        type = type,
                        qualifier = qualifier,
                    )
                }

                is GdsConstantDeclarator -> {
                    val type = GdsDataTypeFactory.createFromConstantDeclaration(declarator) ?: return null

                    ConstantSpec(
                        name = element.name,
                        type = type,
                    )
                }

                is GdsVaryingDeclaration -> {
                    val type = GdsDataTypeFactory.createFromVaryingDeclaration(declarator) ?: return null
                    val qualifierText = declarator.interpolationQualifier?.text
                    val qualifier = InterpolationQualifier.fromText(qualifierText)

                    VaryingSpec(
                        name = element.name,
                        type = type,
                        qualifier = qualifier,
                    )
                }

                is GdsLocalVariableDeclarator -> {
                    val declaratorList = declarator.parent as? GdsLocalVariableDeclaratorList ?: return null
                    val parent = declaratorList.parent ?: return null
                    when (parent) {
                        is GdsLocalVariableDeclaration -> {
                            val type = GdsDataTypeFactory.createFromLocalVariableDeclaration(declarator) ?: return null

                            LocalVariableSpec(
                                name = element.name,
                                type = type,
                            )
                        }

                        is GdsForInit -> {
                            val type = GdsDataTypeFactory.createFromForInit(declarator) ?: return null

                            LocalVariableSpec(
                                name = element.name,
                                type = type,
                            )
                        }

                        else -> null
                    }
                }

                is GdsParameter -> {
                    val type = GdsDataTypeFactory.createFromParameter(declarator) ?: return null
                    val qualifierText = declarator.parameterQualifier?.text
                    val qualifier = ParameterQualifier.fromText(qualifierText)

                    ParameterSpec(
                        name = element.name,
                        type = type,
                        qualifier = qualifier,
                    )
                }

                else -> null
            }
        }
    
    @JvmStatic
    fun getReference(element: GdsVariableNameRef): PsiReference =
        GdsVariableReference(element, TextRange(0, element.textLength))
    
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
    fun getFunctionSpec(element: GdsFunctionNameDecl): FunctionSpec? {
        val declaration = element.parent as? GdsFunctionDeclaration ?: return null
        val returnType = GdsDataTypeFactory.createFromFunctionDeclaration(declaration) ?: return null
        val parameterList = declaration.parameterList?.parameterList ?: emptyList()
        val parameters = parameterList.mapNotNull { param ->
            val nameDecl = param.variableNameDecl
            nameDecl.variableSpec as ParameterSpec
        }

        return FunctionSpec(
            name = element.name,
            returnType = returnType,
            parameters = parameters,
        )
    }
    
    @JvmStatic
    fun getReference(element: GdsFunctionNameRef): PsiReference =
        GdsFunctionReference(element, TextRange(0, element.textLength))
    
    @JvmStatic
    fun getName(element: GdsStructNameDecl): String =
        element.text
    
    @JvmStatic
    fun setName(element: GdsStructNameDecl, newName: String): PsiElement? {
        val identifier = GdsElementFactory.createIdentifier(element.project, newName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }
    
    @JvmStatic
    fun getNameIdentifier(element: GdsStructNameDecl): PsiElement? =
        element.node.findChildByType(GdsTypes.IDENTIFIER)?.psi
    
    @JvmStatic
    fun getReference(element: GdsStructNameRef): PsiReference =
        GdsStructReference(element, TextRange(0, element.textLength))

    @JvmStatic
    fun getName(element: GdsStructMemberNameDecl): String =
        element.text

    @JvmStatic
    fun setName(element: GdsStructMemberNameDecl, newName: String): PsiElement? {
        val identifier = GdsElementFactory.createIdentifier(element.project, newName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: GdsStructMemberNameDecl): PsiElement? =
        element.node.findChildByType(GdsTypes.IDENTIFIER)?.psi

    @JvmStatic
    fun getReference(element: GdsStructMemberNameRef): PsiReference =
        GdsStructMemberReference(element, TextRange(0, element.textLength))
    
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
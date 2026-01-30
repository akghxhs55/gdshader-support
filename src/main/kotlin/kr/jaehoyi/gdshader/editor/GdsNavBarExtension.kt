package kr.jaehoyi.gdshader.editor

import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.intellij.lang.Language
import kr.jaehoyi.gdshader.GdsIcons
import kr.jaehoyi.gdshader.GdsLanguage
import kr.jaehoyi.gdshader.psi.*
import javax.swing.Icon

class GdsNavBarExtension : StructureAwareNavBarModelExtension() {

    override val language: Language = GdsLanguage

    override fun getPresentableText(obj: Any?): String? {
        return when (obj) {
            is GdsFile -> obj.name
            is GdsFunctionDeclaration -> obj.functionNameDecl.name
            is GdsStructDeclaration -> obj.structNameDecl?.name ?: "struct"
            is GdsUniformGroupDeclaration -> buildString {
                append("group_uniforms ")
                append(obj.uniformGroupNameList.joinToString(".") { it.text })
            }
            is GdsShaderTypeDeclaration -> buildString {
                append("shader_type")
                obj.shaderTypeName?.text?.let { append(" $it") }
            }
            is GdsRenderModeDeclaration -> "render_mode"
            is GdsStencilModeDeclaration -> "stencil_mode"
            is GdsUniformDeclaration -> obj.variableNameDecl?.name
            is GdsConstantDeclarator -> obj.variableNameDecl.name
            is GdsVaryingDeclaration -> obj.variableNameDecl?.name
            is GdsStructMember -> obj.structMemberNameDecl?.name
            is GdsParameter -> obj.variableNameDecl.name
            else -> null
        }
    }

    override fun getIcon(obj: Any?): Icon? {
        return when (obj) {
            is GdsFile -> GdsIcons.GDSHADER
            is GdsFunctionDeclaration -> GdsIcons.FUNCTION
            is GdsStructDeclaration -> GdsIcons.STRUCT
            is GdsUniformGroupDeclaration -> GdsIcons.UNIFORM_GROUP
            is GdsShaderTypeDeclaration -> GdsIcons.SHADER_TYPE
            is GdsRenderModeDeclaration -> GdsIcons.RENDER_MODE
            is GdsStencilModeDeclaration -> GdsIcons.STENCIL_MODE
            is GdsUniformDeclaration -> GdsIcons.UNIFORM
            is GdsConstantDeclarator -> GdsIcons.CONSTANT
            is GdsVaryingDeclaration -> GdsIcons.VARYING
            is GdsStructMember -> GdsIcons.STRUCT_MEMBER
            is GdsParameter -> GdsIcons.PARAMETER
            else -> null
        }
    }
}

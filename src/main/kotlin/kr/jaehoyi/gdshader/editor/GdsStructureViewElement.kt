package kr.jaehoyi.gdshader.editor

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.GdsIcons
import kr.jaehoyi.gdshader.psi.*

class GdsStructureViewElement(private val element: NavigatablePsiElement) :
    StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getAlphaSortKey(): String = getPresentation().presentableText ?: ""

    override fun getPresentation(): ItemPresentation = when (element) {
        is GdsFile -> PresentationData(element.name, null, GdsIcons.GDSHADER, null)
        is GdsShaderTypeDeclaration -> PresentationData(
            "shader_type",
            element.shaderTypeName?.text,
            GdsIcons.SHADER_TYPE,
            null,
        )
        is GdsRenderModeDeclaration -> PresentationData(
            "render_mode",
            element.renderModeDeclaratorList?.renderModeNameList?.joinToString(", ") { it.text },
            GdsIcons.RENDER_MODE,
            null,
        )
        is GdsStencilModeDeclaration -> PresentationData(
            "stencil_mode",
            element.stencilModeDeclaratorList?.stencilModeNameList?.joinToString(", ") { it.text },
            GdsIcons.STENCIL_MODE,
            null,
        )
        is GdsUniformGroupDeclaration -> PresentationData(
            "group_uniform",
            element.uniformGroupNameList.joinToString(".") { it.text },
            GdsIcons.UNIFORM_GROUP,
            null,
        )
        is GdsUniformDeclaration -> PresentationData(
            element.variableNameDecl?.name ?: "",
            buildString {
                element.uniformHeader.uniformQualifier?.let { append(it.text).append(' ') }
                append("uniform ")
                element.type?.text?.let { append(it) }
            },
            GdsIcons.UNIFORM,
            null,
        )
        is GdsConstantDeclarator -> {
            val constDecl = element.parent?.parent as? GdsConstantDeclaration
            PresentationData(
                element.variableNameDecl.name,
                buildString {
                    append("const ")
                    constDecl?.type?.text?.let { append(it) }
                },
                GdsIcons.CONSTANT,
                null,
            )
        }
        is GdsVaryingDeclaration -> PresentationData(
            element.variableNameDecl?.name ?: "",
            buildString {
                append("varying ")
                element.type?.text?.let { append(it) }
            },
            GdsIcons.VARYING,
            null,
        )
        is GdsFunctionDeclaration -> PresentationData(
            buildString {
                append(element.functionNameDecl.name)
                append('(')
                element.parameterList?.parameterList?.joinToString(", ") { param ->
                    buildString {
                        append(param.type.text)
                        append(' ')
                        append(param.variableNameDecl.name)
                    }
                }?.let { append(it) }
                append(')')
            },
            element.type.text,
            GdsIcons.FUNCTION,
            null,
        )
        is GdsStructDeclaration -> PresentationData(
            element.structNameDecl?.name ?: "",
            "struct",
            GdsIcons.STRUCT,
            null,
        )
        is GdsStructMember -> PresentationData(
            element.structMemberNameDecl?.name ?: "",
            element.type.text,
            GdsIcons.STRUCT_MEMBER,
            null,
        )
        is GdsParameter -> PresentationData(
            element.variableNameDecl.name,
            buildString {
                element.parameterQualifier?.let { append(it.text).append(' ') }
                append(element.type.text)
            },
            GdsIcons.PARAMETER,
            null,
        )
        else -> PresentationData(element.text, null, null, null)
    }

    override fun getChildren(): Array<TreeElement> = when (element) {
        is GdsFile -> {
            val items = PsiTreeUtil.getChildrenOfType(element, GdsItem::class.java)
            items?.flatMap { item ->
                val topLevel = item.topLevelDeclaration
                val decl = topLevel.shaderTypeDeclaration
                    ?: topLevel.renderModeDeclaration
                    ?: topLevel.stencilModeDeclaration
                    ?: topLevel.uniformGroupDeclaration
                    ?: topLevel.uniformDeclaration
                    ?: topLevel.varyingDeclaration
                    ?: topLevel.functionDeclaration
                    ?: topLevel.structDeclaration

                if (decl == null) {
                    val constDecl = topLevel.constantDeclaration
                    constDecl?.constantDeclaratorList?.constantDeclaratorList
                        ?.map { GdsStructureViewElement(it as NavigatablePsiElement) }
                        ?: emptyList()
                } else {
                    listOf(GdsStructureViewElement(decl as NavigatablePsiElement))
                }
            }?.toTypedArray() ?: emptyArray()
        }
        is GdsFunctionDeclaration -> {
            element.parameterList?.parameterList
                ?.map { GdsStructureViewElement(it as NavigatablePsiElement) }
                ?.toTypedArray()
                ?: emptyArray()
        }
        is GdsStructDeclaration -> {
            element.structBlock?.structMemberList?.structMemberList
                ?.map { GdsStructureViewElement(it as NavigatablePsiElement) }
                ?.toTypedArray()
                ?: emptyArray()
        }
        else -> emptyArray()
    }
}

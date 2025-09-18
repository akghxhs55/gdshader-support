package kr.jaehoyi.gdshader.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import kr.jaehoyi.gdshader.GDShaderIncludeFileType
import org.jetbrains.annotations.NonNls

class GDShaderIncludeNewFileAction : CreateFileFromTemplateAction(), DumbAware {
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("GDShader Include")
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GDShaderIncludeFileType.defaultExtension) {
                    builder.addKind(it.name, GDShaderIncludeFileType.icon, it.name)
                }
            }
        
        builder
            .addKind("Shader Include", GDShaderIncludeFileType.icon, "Shader Include")
    }

    override fun getActionName(directory: PsiDirectory?, newName: @NonNls String, templateName: @NonNls String?): String
        = "GDShader Include File"
}
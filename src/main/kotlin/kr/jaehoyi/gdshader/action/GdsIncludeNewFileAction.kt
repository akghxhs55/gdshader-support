package kr.jaehoyi.gdshader.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import kr.jaehoyi.gdshader.GdsIncludeFileType

class GdsIncludeNewFileAction : CreateFileFromTemplateAction(), DumbAware {
    
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("GDShader Include")
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GdsIncludeFileType.defaultExtension) {
                    builder.addKind(it.name, GdsIncludeFileType.icon, it.name)
                }
            }
        
        builder
            .addKind("Shader Include", GdsIncludeFileType.icon, "Shader Include")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String
        = "Create GDShader Include File $newName"
    
}
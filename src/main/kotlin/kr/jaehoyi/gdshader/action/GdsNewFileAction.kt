package kr.jaehoyi.gdshader.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import kr.jaehoyi.gdshader.GdsFileType

class GdsNewFileAction : CreateFileFromTemplateAction(), DumbAware {
    
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("GDShader")
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GdsFileType.defaultExtension) {
                    builder.addKind(it.name, GdsFileType.icon, it.name)
                }
            }
        
        builder
            .addKind("Spatial", GdsFileType.icon, "Spatial")
            .addKind("Canvas Item", GdsFileType.icon, "Canvas Item")
            .addKind("Particles", GdsFileType.icon, "Particles")
            .addKind("Sky", GdsFileType.icon, "Sky")
            .addKind("Fog", GdsFileType.icon, "Fog")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String
        = "Create GDShader File $newName"
    
}
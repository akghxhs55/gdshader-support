package kr.jaehoyi.gdshader.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import kr.jaehoyi.gdshader.GDShaderFileType

class GDShaderNewFileAction : CreateFileFromTemplateAction(), DumbAware {
    
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("GDShader")
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GDShaderFileType.defaultExtension) {
                    builder.addKind(it.name, GDShaderFileType.icon, it.name)
                }
            }
        
        builder
            .addKind("Spatial", GDShaderFileType.icon, "Spatial")
            .addKind("Canvas Item", GDShaderFileType.icon, "Canvas Item")
            .addKind("Particles", GDShaderFileType.icon, "Particles")
            .addKind("Sky", GDShaderFileType.icon, "Sky")
            .addKind("Fog", GDShaderFileType.icon, "Fog")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String
        = "Create GDShader File $newName"
    
}
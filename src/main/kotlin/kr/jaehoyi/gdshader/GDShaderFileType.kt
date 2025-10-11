package kr.jaehoyi.gdshader

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object GDShaderFileType : LanguageFileType(GDShaderLanguage) {
    
    override fun getName(): String = "GDShader File"

    override fun getDescription(): String = "GDShader file"

    override fun getDefaultExtension(): String = "gdshader"

    override fun getIcon(): Icon = GDShaderIcons.GDSHADER

    override fun getDisplayName(): String = "GDShader"
    
}

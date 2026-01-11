package kr.jaehoyi.gdshader

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object GdsFileType : LanguageFileType(GdsLanguage) {
    
    override fun getName(): String = "GDShader File"

    override fun getDescription(): String = "GDShader file"

    override fun getDefaultExtension(): String = "gdshader"

    override fun getIcon(): Icon = GdsIcons.GDSHADER

    override fun getDisplayName(): String = "GDShader"
    
}

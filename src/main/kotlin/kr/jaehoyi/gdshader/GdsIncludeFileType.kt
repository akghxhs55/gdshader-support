package kr.jaehoyi.gdshader

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object GdsIncludeFileType : LanguageFileType(GdsLanguage) {
    
    override fun getName(): String = "GDShader Include File"

    override fun getDescription(): String = "GDShader include file"

    override fun getDefaultExtension(): String = "gdshaderinc"

    override fun getIcon(): Icon = GdsIcons.GDSHADERINC

    override fun getDisplayName(): String = "GDShader Include"
    
}
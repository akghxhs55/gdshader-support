package kr.jaehoyi.gdshader.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import kr.jaehoyi.gdshader.GdsFileType
import kr.jaehoyi.gdshader.GdsLanguage

class GdsFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GdsLanguage) {
    
    override fun getFileType(): FileType = GdsFileType
    
    override fun toString(): String = "GDShader File"
    
}
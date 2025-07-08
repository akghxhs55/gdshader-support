package kr.jaehoyi.gdshader.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import kr.jaehoyi.gdshader.GDShaderFileType
import kr.jaehoyi.gdshader.GDShaderLanguage

class GDShaderFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GDShaderLanguage) {
    override fun getFileType(): FileType = GDShaderFileType
    
    override fun toString(): String = "GDShader File"
}
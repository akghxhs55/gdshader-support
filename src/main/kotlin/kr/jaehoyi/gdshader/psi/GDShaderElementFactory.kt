package kr.jaehoyi.gdshader.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.GDShaderFileType

object GDShaderElementFactory {
    
    fun createExpression(project: Project, text: String): GDShaderExpression? {
        val fileName = "dummy.gdshader"
        val dummyText = "uniform int dummy = $text;"
        
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, GDShaderFileType, dummyText)
        
        val uniformDeclaration = PsiTreeUtil.findChildOfType(file, GDShaderUniformDeclaration::class.java)
        
        return uniformDeclaration?.expression
    }
    
    fun createUniformDeclaration(project: Project, text: String): GDShaderUniformDeclaration? {
        val fileName = "dummy.gdshader"
        
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, GDShaderFileType, text)
        
        return PsiTreeUtil.findChildOfType(file, GDShaderUniformDeclaration::class.java)
    }
    
    fun createInitializer(project: Project, text: String): GDShaderInitializer? {
        val fileName = "dummy.gdshader"
        val dummyText = "const int dummy = $text;"
        
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, GDShaderFileType, dummyText)
        
        val constantDeclaration = PsiTreeUtil.findChildOfType(file, GDShaderConstantDeclaration::class.java)
        val constantDeclarator = constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.firstOrNull()
        
        return constantDeclarator?.initializer
    }
    
}
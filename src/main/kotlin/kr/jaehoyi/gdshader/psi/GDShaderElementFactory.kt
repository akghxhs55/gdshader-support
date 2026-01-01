package kr.jaehoyi.gdshader.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.GDShaderFileType

object GDShaderElementFactory {
    
    fun createExpression(project: Project, text: String): GDShaderExpression? {
        val dummyText = "uniform int dummy = $text;"
        val file = createFile(project, dummyText)
        
        val uniformDeclaration = PsiTreeUtil.findChildOfType(file, GDShaderUniformDeclaration::class.java)
        
        return uniformDeclaration?.expression
    }
    
    fun createUniformDeclaration(project: Project, text: String): GDShaderUniformDeclaration? {
        val file = createFile(project, text)
        
        return PsiTreeUtil.findChildOfType(file, GDShaderUniformDeclaration::class.java)
    }
    
    fun createInitializer(project: Project, text: String): GDShaderInitializer? {
        val dummyText = "const int dummy = $text;"
        val file = createFile(project, dummyText)
        
        val constantDeclaration = PsiTreeUtil.findChildOfType(file, GDShaderConstantDeclaration::class.java)
        val constantDeclarator = constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.firstOrNull()
        
        return constantDeclarator?.initializer
    }
    
    fun createLocalVariableDeclarator(project: Project, text: String): GDShaderLocalVariableDeclarator? {
        val dummyText = "void dummy() { $text }"
        val file = createFile(project, dummyText)
        
        return PsiTreeUtil.findChildOfType(file, GDShaderLocalVariableDeclarator::class.java)
    }
    
    fun createIdentifier(project: Project, text: String): PsiElement? {
        val dummyText = "void dummy() { var $text = 0; }"
        val file = createFile(project, dummyText)
        
        return file.findChildByClass(GDShaderItem::class.java)
            ?.topLevelDeclaration
            ?.functionDeclaration
            ?.block
            ?.blockBody
            ?.statementBodyList?.firstOrNull()
            ?.statement
            ?.localVariableDeclaration
            ?.localVariableDeclaratorList
            ?.localVariableDeclaratorList?.firstOrNull()
            ?.variableNameDecl
            ?.firstChild
    }
    
    fun createFile(project: Project, text: String): GDShaderFile {
        val fileName = "dummy.gdshader"
        
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, GDShaderFileType, text)
        
        return file as GDShaderFile
    }
    
}
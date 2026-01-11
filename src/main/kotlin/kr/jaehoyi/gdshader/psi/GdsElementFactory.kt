package kr.jaehoyi.gdshader.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.GdsFileType

object GdsElementFactory {
    
    fun createExpression(project: Project, text: String): GdsExpression? {
        val dummyText = "uniform int dummy = $text;"
        val file = createFile(project, dummyText)
        
        val uniformDeclaration = PsiTreeUtil.findChildOfType(file, GdsUniformDeclaration::class.java)
        
        return uniformDeclaration?.expression
    }
    
    fun createUniformDeclaration(project: Project, text: String): GdsUniformDeclaration? {
        val file = createFile(project, text)
        
        return PsiTreeUtil.findChildOfType(file, GdsUniformDeclaration::class.java)
    }
    
    fun createInitializer(project: Project, text: String): GdsInitializer? {
        val dummyText = "const int dummy = $text;"
        val file = createFile(project, dummyText)
        
        val constantDeclaration = PsiTreeUtil.findChildOfType(file, GdsConstantDeclaration::class.java)
        val constantDeclarator = constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.firstOrNull()
        
        return constantDeclarator?.initializer
    }
    
    fun createLocalVariableDeclarator(project: Project, text: String): GdsLocalVariableDeclarator? {
        val dummyText = "void dummy() { $text }"
        val file = createFile(project, dummyText)
        
        return PsiTreeUtil.findChildOfType(file, GdsLocalVariableDeclarator::class.java)
    }
    
    fun createIdentifier(project: Project, text: String): PsiElement? {
        val dummyText = "void dummy() { var $text = 0; }"
        val file = createFile(project, dummyText)
        
        return file.findChildByClass(GdsItem::class.java)
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
    
    fun createFile(project: Project, text: String): GdsFile {
        val fileName = "dummy.gdshader"
        
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText(fileName, GdsFileType, text)
        
        return file as GdsFile
    }
    
}
package kr.jaehoyi.gdshader

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.parser.GDShaderParser
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderTokenSets
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderParserDefinition : ParserDefinition {

    override fun createLexer(project: Project): Lexer = GDShaderLexerAdapter()

    override fun getCommentTokens(): TokenSet = GDShaderTokenSets.COMMENTS
    
    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun getWhitespaceTokens(): TokenSet = TokenSet.WHITE_SPACE
    
    override fun createParser(project: Project): PsiParser = GDShaderParser()
    
    override fun getFileNodeType(): IFileElementType = FILE

    override fun createFile(viewProvider: FileViewProvider): PsiFile = GDShaderFile(viewProvider)

    override fun createElement(node: ASTNode): PsiElement = GDShaderTypes.Factory.createElement(node) 
    
}

private val FILE: IFileElementType = IFileElementType(GDShaderLanguage)

package kr.jaehoyi.gdshader

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilderFactory
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.parser.GdsParser
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsTokenSets
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives

class GdsParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer = GdsConditionalLexer()

    override fun getCommentTokens(): TokenSet = GdsTokenSets.COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun getWhitespaceTokens(): TokenSet = TokenSet.WHITE_SPACE

    override fun createParser(project: Project): PsiParser = GdsParser()

    override fun getFileNodeType(): IFileElementType = GdsFileElementType

    override fun createFile(viewProvider: FileViewProvider): PsiFile = GdsFile(viewProvider)

    override fun createElement(node: ASTNode): PsiElement = GdsTypes.Factory.createElement(node)
}

// .gdshader and .gdshaderinc share this definition and createLexer cannot see the file, so the file kind is passed here.
private object GdsFileElementType : IFileElementType(GdsLanguage) {
    override fun doParseContents(
        chameleon: ASTNode,
        psi: PsiElement,
    ): ASTNode? {
        val file = psi.containingFile ?: return super.doParseContents(chameleon, psi)
        val lexer = GdsConditionalLexer(GdsConditionalDirectives.undefinedByDefault(file))
        val builder = PsiBuilderFactory.getInstance().createBuilder(psi.project, chameleon, lexer, language, chameleon.chars)
        return GdsParser().parse(this, builder).firstChildNode
    }
}

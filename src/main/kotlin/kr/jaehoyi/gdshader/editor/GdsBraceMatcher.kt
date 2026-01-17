package kr.jaehoyi.gdshader.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsBraceMatcher : PairedBraceMatcher {
    
    private val pairs = arrayOf(
        BracePair(GdsTypes.PARENTHESIS_OPEN, GdsTypes.PARENTHESIS_CLOSE, false),
        BracePair(GdsTypes.CURLY_BRACKET_OPEN, GdsTypes.CURLY_BRACKET_CLOSE, true),
        BracePair(GdsTypes.BRACKET_OPEN, GdsTypes.BRACKET_CLOSE, false),
        BracePair(GdsTypes.BLOCK_COMMENT_START, GdsTypes.BLOCK_COMMENT_END, false)
    )
    
    private val pairingAllowedTokenSet = TokenSet.create(
        TokenType.WHITE_SPACE, GdsTypes.LINE_COMMENT, GdsTypes.BLOCK_COMMENT_START, GdsTypes.BLOCK_COMMENT_END,
        GdsTypes.BLOCK_COMMENT_CONTENT, GdsTypes.PP_DEFINE_LINE, GdsTypes.PP_UNDEF_LINE, GdsTypes.PP_ELSE_LINE, 
        GdsTypes.PP_ELIF_LINE, GdsTypes.PP_ENDIF_LINE, GdsTypes.PP_IFDEF_LINE, GdsTypes.PP_IFNDEF_LINE, 
        GdsTypes.PP_IF_LINE, GdsTypes.PP_ERROR_LINE, GdsTypes.PP_INCLUDE_LINE, GdsTypes.PP_PRAGMA_LINE, GdsTypes.COMMA, 
        GdsTypes.COLON, GdsTypes.SEMICOLON, GdsTypes.PERIOD, GdsTypes.PARENTHESIS_CLOSE, GdsTypes.BRACKET_CLOSE, 
        GdsTypes.CURLY_BRACKET_CLOSE
    )

    override fun getPairs(): Array<BracePair> = pairs

    override fun isPairedBracesAllowedBeforeType(leftBraceType: IElementType, contextType: IElementType?): Boolean =
        contextType == null || pairingAllowedTokenSet.contains(contextType)

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int = openingBraceOffset
    
}
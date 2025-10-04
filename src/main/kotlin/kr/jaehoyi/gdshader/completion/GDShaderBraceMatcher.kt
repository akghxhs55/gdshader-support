package kr.jaehoyi.gdshader.completion

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderBraceMatcher : PairedBraceMatcher {
    private val pairs = arrayOf(
        BracePair(GDShaderTypes.PARENTHESIS_OPEN, GDShaderTypes.PARENTHESIS_CLOSE, false),
        BracePair(GDShaderTypes.CURLY_BRACKET_OPEN, GDShaderTypes.CURLY_BRACKET_CLOSE, true),
        BracePair(GDShaderTypes.BRACKET_OPEN, GDShaderTypes.BRACKET_CLOSE, false)
    )
    
    private val pairingAllowedTokenSet = TokenSet.create(
        TokenType.WHITE_SPACE, GDShaderTypes.LINE_COMMENT, GDShaderTypes.BLOCK_COMMENT, GDShaderTypes.PP_DEFINE_LINE, 
        GDShaderTypes.PP_UNDEF_LINE, GDShaderTypes.PP_ELSE_LINE, GDShaderTypes.PP_ELIF_LINE, 
        GDShaderTypes.PP_ENDIF_LINE, GDShaderTypes.PP_IFDEF_LINE, GDShaderTypes.PP_IFNDEF_LINE, 
        GDShaderTypes.PP_IF_LINE, GDShaderTypes.PP_ERROR_LINE, GDShaderTypes.PP_INCLUDE_LINE,
        GDShaderTypes.PP_PRAGMA_LINE, GDShaderTypes.COMMA, GDShaderTypes.COLON, GDShaderTypes.SEMICOLON,
        GDShaderTypes.PERIOD, GDShaderTypes.PARENTHESIS_CLOSE, GDShaderTypes.BRACKET_CLOSE,
        GDShaderTypes.CURLY_BRACKET_CLOSE
    )

    override fun getPairs(): Array<BracePair> = pairs

    override fun isPairedBracesAllowedBeforeType(leftBraceType: IElementType, contextType: IElementType?): Boolean =
        contextType == null || pairingAllowedTokenSet.contains(contextType)

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int = openingBraceOffset
}

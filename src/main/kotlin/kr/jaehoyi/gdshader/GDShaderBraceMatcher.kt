package kr.jaehoyi.gdshader

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderBraceMatcher : PairedBraceMatcher {
    private val pairs = arrayOf(
        BracePair(GDShaderTypes.PARENTHESIS_OPEN, GDShaderTypes.PARENTHESIS_CLOSE, false),
        BracePair(GDShaderTypes.CURLY_BRACKET_OPEN, GDShaderTypes.CURLY_BRACKET_CLOSE, true),
        BracePair(GDShaderTypes.BRACKET_OPEN, GDShaderTypes.BRACKET_CLOSE, false)
    )

    override fun getPairs(): Array<BracePair> = pairs

    override fun isPairedBracesAllowedBeforeType(leftBraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int = openingBraceOffset
}
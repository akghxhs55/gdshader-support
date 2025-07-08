package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.TokenSet

object GDShaderTokenSets {
    val COMMENTS: TokenSet
        get() = TokenSet.create(GDShaderTypes.LINE_COMMENT, GDShaderTypes.BLOCK_COMMENT)
}
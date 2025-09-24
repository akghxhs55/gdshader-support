package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.TokenSet

object GDShaderTokenSets {
    val COMMENTS: TokenSet
        get() = TokenSet.create(GDShaderTypes.LINE_COMMENT, GDShaderTypes.BLOCK_COMMENT, GDShaderTypes.PP_DEFINE_LINE,
            GDShaderTypes.PP_UNDEF_LINE, GDShaderTypes.PP_ELSE_LINE, GDShaderTypes.PP_ELIF_LINE,
            GDShaderTypes.PP_ENDIF_LINE, GDShaderTypes.PP_IFDEF_LINE, GDShaderTypes.PP_IFNDEF_LINE,
            GDShaderTypes.PP_IF_LINE, GDShaderTypes.PP_ERROR_LINE, GDShaderTypes.PP_INCLUDE_LINE,
            GDShaderTypes.PP_PRAGMA_LINE)
}
package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderQuoteHandler : SimpleTokenSetQuoteHandler(
    GDShaderTypes.STRING_CONSTANT,
    GDShaderTypes.UNTERMINATED_STRING_CONSTANT
)

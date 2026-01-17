package kr.jaehoyi.gdshader.editor

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsQuoteHandler : SimpleTokenSetQuoteHandler(
    GdsTypes.STRING_CONSTANT,
    GdsTypes.UNTERMINATED_STRING_CONSTANT
)
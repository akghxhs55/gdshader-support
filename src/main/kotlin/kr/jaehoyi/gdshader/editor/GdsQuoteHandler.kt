package kr.jaehoyi.gdshader.editor

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsQuoteHandler : SimpleTokenSetQuoteHandler(
    GdsTypes.STRING_CONSTANT,
    GdsTypes.UNTERMINATED_STRING_CONSTANT
) {

    override fun isOpeningQuote(iterator: HighlighterIterator?, offset: Int): Boolean {
        if (super.isOpeningQuote(iterator, offset)) return true
        if (iterator == null) return false

        if (iterator.tokenType == GdsTypes.PP_INCLUDE_LINE) {
            val doc = iterator.document ?: return false
            val start = iterator.start

            if (offset > start) {
                val textBefore = doc.charsSequence.subSequence(start, offset).toString().trim()
                if (textBefore.endsWith("#include")) {
                    return true
                }
            }
        }
        return false
    }

    override fun hasNonClosedLiteral(editor: Editor?, iterator: HighlighterIterator?, offset: Int): Boolean {
        if (iterator != null && iterator.tokenType == GdsTypes.PP_INCLUDE_LINE) {
            return true
        }

        return super.hasNonClosedLiteral(editor, iterator, offset)
    }

    override fun isClosingQuote(iterator: HighlighterIterator?, offset: Int): Boolean {
        if (super.isClosingQuote(iterator, offset)) return true
        if (iterator == null) return false

        if (iterator.tokenType == GdsTypes.PP_INCLUDE_LINE) {
            val doc = iterator.document ?: return false
            if (offset < doc.textLength && doc.charsSequence[offset] == '"') {
                return true
            }
        }
        return false
    }
    
}

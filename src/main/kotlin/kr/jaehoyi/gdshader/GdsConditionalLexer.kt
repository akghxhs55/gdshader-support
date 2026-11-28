package kr.jaehoyi.gdshader

import com.intellij.lexer.LexerBase
import com.intellij.openapi.util.TextRange
import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives

// Needs the whole file to evaluate directives, so the editor highlighter keeps using GdsLexerAdapter.
class GdsConditionalLexer(
    private val undefinedByDefault: Boolean = false,
) : LexerBase() {
    private class Token(
        val type: IElementType,
        val start: Int,
        val end: Int,
    )

    private var buffer: CharSequence = ""
    private var bufferEnd = 0
    private var tokens: List<Token> = emptyList()
    private var index = 0

    override fun start(
        buffer: CharSequence,
        startOffset: Int,
        endOffset: Int,
        initialState: Int,
    ) {
        this.buffer = buffer
        bufferEnd = endOffset
        index = 0

        val base = GdsLexerAdapter()
        base.start(buffer, startOffset, endOffset, initialState)
        val baseTokens = mutableListOf<Token>()
        while (true) {
            val type = base.tokenType ?: break
            baseTokens += Token(type, base.tokenStart, base.tokenEnd)
            base.advance()
        }

        val directives =
            baseTokens
                .filter { it.type in GdsConditionalDirectives.DIRECTIVES }
                .map { GdsConditionalDirectives.DirectiveToken(it.type, TextRange(it.start, it.end), buffer.substring(it.start, it.end)) }
        val inactiveRanges = GdsConditionalDirectives.analyze(directives, endOffset, undefinedByDefault).inactiveRanges
        tokens = merge(baseTokens, inactiveRanges)
    }

    private fun merge(
        baseTokens: List<Token>,
        inactiveRanges: List<TextRange>,
    ): List<Token> {
        if (inactiveRanges.isEmpty()) return baseTokens
        val result = ArrayList<Token>(baseTokens.size)
        var rangeIndex = 0
        for (token in baseTokens) {
            while (rangeIndex < inactiveRanges.size && inactiveRanges[rangeIndex].endOffset <= token.start) rangeIndex++
            val range = inactiveRanges.getOrNull(rangeIndex)
            if (range == null || token.end <= range.startOffset) {
                result += token
                continue
            }
            val end = minOf(range.endOffset, bufferEnd)
            if (result.lastOrNull()?.let { it.type == GdsTypes.PP_INACTIVE_CODE && it.end >= token.end } == true) continue
            result += Token(GdsTypes.PP_INACTIVE_CODE, token.start, maxOf(end, token.end))
        }
        return result
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = tokens.getOrNull(index)?.type

    override fun getTokenStart(): Int = tokens.getOrNull(index)?.start ?: bufferEnd

    override fun getTokenEnd(): Int = tokens.getOrNull(index)?.end ?: bufferEnd

    override fun advance() {
        index++
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = bufferEnd
}

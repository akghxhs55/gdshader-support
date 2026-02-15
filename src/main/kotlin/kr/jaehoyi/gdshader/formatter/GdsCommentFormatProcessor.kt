package kr.jaehoyi.gdshader.formatter

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsCommentFormatProcessor : PostFormatProcessor {

    override fun processElement(source: PsiElement, settings: CodeStyleSettings): PsiElement = source

    override fun processText(source: PsiFile, rangeToReformat: TextRange, settings: CodeStyleSettings): TextRange {
        if (source !is GdsFile) return rangeToReformat

        val document = source.viewProvider.document ?: return rangeToReformat
        val insertions = mutableListOf<Int>()

        var element = source.findElementAt(rangeToReformat.startOffset)
        while (element != null && element.textRange.startOffset < rangeToReformat.endOffset) {
            when (element.node.elementType) {
                GdsTypes.LINE_COMMENT -> {
                    val text = element.text
                    if (text.length > 2 && text.startsWith("//") && !text.startsWith("// ")) {
                        insertions.add(element.textRange.startOffset + 2)
                    }
                }

                GdsTypes.BLOCK_COMMENT_START -> {
                    val next = PsiTreeUtil.nextLeaf(element)
                    if (next != null && next.node.elementType == GdsTypes.BLOCK_COMMENT_CONTENT) {
                        val text = next.text
                        if (text.isNotEmpty() && !text[0].isWhitespace()) {
                            insertions.add(next.textRange.startOffset)
                        }
                    }
                }

                GdsTypes.BLOCK_COMMENT_END -> {
                    val prev = PsiTreeUtil.prevLeaf(element)
                    if (prev != null && prev.node.elementType == GdsTypes.BLOCK_COMMENT_CONTENT) {
                        val text = prev.text
                        if (text.isNotEmpty() && !text.last().isWhitespace()) {
                            insertions.add(prev.textRange.endOffset)
                        }
                    }
                }
            }
            element = PsiTreeUtil.nextLeaf(element)
        }

        for (offset in insertions.reversed()) {
            document.insertString(offset, " ")
        }

        if (insertions.isNotEmpty()) {
            PsiDocumentManager.getInstance(source.project).commitDocument(document)
        }

        return TextRange(rangeToReformat.startOffset, rangeToReformat.endOffset + insertions.size)
    }
}

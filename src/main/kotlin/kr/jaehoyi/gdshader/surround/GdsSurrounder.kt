package kr.jaehoyi.gdshader.surround

import com.intellij.lang.surroundWith.Surrounder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager

abstract class GdsSurrounder(
    private val templateDescription: String,
    private val prefix: String,
    private val suffix: String,
    private val caretInSuffix: Boolean = false,
    private val caretLocalOffset: Int,
) : Surrounder {

    override fun getTemplateDescription(): String = templateDescription

    override fun isApplicable(elements: Array<out PsiElement>): Boolean = elements.isNotEmpty()

    override fun surroundElements(
        project: Project,
        editor: Editor,
        elements: Array<out PsiElement>,
    ): TextRange? {
        val document = editor.document
        val startOffset = elements.first().textRange.startOffset
        val endOffset = elements.last().textRange.endOffset
        val selectedText = document.getText(TextRange(startOffset, endOffset))

        val newText = "$prefix\n$selectedText\n$suffix"
        val caretPos = if (caretInSuffix) {
            startOffset + prefix.length + 1 + selectedText.length + 1 + caretLocalOffset
        } else {
            startOffset + caretLocalOffset
        }

        document.replaceString(startOffset, endOffset, newText)
        val marker = document.createRangeMarker(caretPos, caretPos)

        val manager = PsiDocumentManager.getInstance(project)
        manager.commitDocument(document)
        val psiFile = manager.getPsiFile(document) ?: return null
        CodeStyleManager.getInstance(project).reformatText(psiFile, startOffset, startOffset + newText.length)

        val result = TextRange.from(marker.startOffset, 0)
        marker.dispose()
        return result
    }
}

class GdsIfSurrounder : GdsSurrounder("if", "if () {", "}", caretLocalOffset = 4)
class GdsIfElseSurrounder : GdsSurrounder("if / else", "if () {", "} else {\n}", caretLocalOffset = 4)
class GdsForSurrounder : GdsSurrounder("for", "for () {", "}", caretLocalOffset = 5)
class GdsWhileSurrounder : GdsSurrounder("while", "while () {", "}", caretLocalOffset = 7)
class GdsDoWhileSurrounder : GdsSurrounder("do / while", "do {", "} while ();", caretInSuffix = true, caretLocalOffset = 9)

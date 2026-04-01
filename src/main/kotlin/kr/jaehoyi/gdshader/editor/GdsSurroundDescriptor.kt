package kr.jaehoyi.gdshader.editor

import com.intellij.lang.surroundWith.SurroundDescriptor
import com.intellij.lang.surroundWith.Surrounder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.psi.GdsBlockBody
import kr.jaehoyi.gdshader.psi.GdsCaseBody
import kr.jaehoyi.gdshader.psi.GdsStatementBody

class GdsSurroundDescriptor : SurroundDescriptor {
    private val surrounders =
        arrayOf<Surrounder>(
            GdsIfSurrounder(),
            GdsIfElseSurrounder(),
            GdsForSurrounder(),
            GdsWhileSurrounder(),
            GdsDoWhileSurrounder(),
        )

    override fun getElementsToSurround(
        file: PsiFile,
        startOffset: Int,
        endOffset: Int,
    ): Array<PsiElement> {
        if (startOffset >= endOffset) return PsiElement.EMPTY_ARRAY

        val startElement = file.findElementAt(startOffset) ?: return PsiElement.EMPTY_ARRAY
        val endElement = file.findElementAt(endOffset - 1) ?: return PsiElement.EMPTY_ARRAY

        val commonParent =
            PsiTreeUtil.findCommonParent(startElement, endElement)
                ?: return PsiElement.EMPTY_ARRAY

        val container =
            generateSequence(commonParent) { it.parent }
                .firstOrNull { it is GdsBlockBody || it is GdsCaseBody }
                ?: return PsiElement.EMPTY_ARRAY

        val statements =
            container.children
                .filterIsInstance<GdsStatementBody>()
                .filter { it.textRange.startOffset < endOffset && it.textRange.endOffset > startOffset }

        if (statements.isEmpty()) return PsiElement.EMPTY_ARRAY
        return statements.toTypedArray()
    }

    override fun getSurrounders(): Array<Surrounder> = surrounders

    override fun isExclusive(): Boolean = false
}

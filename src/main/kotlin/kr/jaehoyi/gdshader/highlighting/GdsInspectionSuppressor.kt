package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInsight.daemon.ChangeLocalityDetector
import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.codeInspection.SuppressionUtil
import com.intellij.codeInspection.SuppressionUtilCore
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfTypes
import kr.jaehoyi.gdshader.GdsBundle
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStatement
import kr.jaehoyi.gdshader.psi.GdsTypes
import java.util.regex.Pattern

class GdsInspectionSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(
        element: PsiElement,
        toolId: String,
    ): Boolean {
        if (toolId != GdsInspectionUtil.UNUSED_SYMBOL) return false
        val container = GdsSuppression.findContainer(element) ?: return false
        return listOfNotNull(GdsSuppression.findCommentAbove(container), GdsSuppression.findTrailingComment(container))
            .any { GdsSuppression.mentions(it, toolId) }
    }

    override fun getSuppressActions(
        element: PsiElement?,
        toolId: String,
    ): Array<SuppressQuickFix> {
        if (toolId != GdsInspectionUtil.UNUSED_SYMBOL || element == null) return SuppressQuickFix.EMPTY_ARRAY
        if (!GdsSuppressByCommentFix.canSuppress(element)) return SuppressQuickFix.EMPTY_ARRAY
        return arrayOf(GdsSuppressByCommentFix(toolId))
    }
}

// The platform re-highlights only the edited comment, which would leave the declaration next to it stale.
class GdsSuppressionChangeLocalityDetector : ChangeLocalityDetector {
    override fun getChangeHighlightingDirtyScopeFor(changedElement: PsiElement): PsiElement? = if (changedElement.elementType == GdsTypes.LINE_COMMENT) changedElement.parent else null
}

class GdsSuppressByCommentFix(
    private val toolId: String,
) : SuppressQuickFix {
    override fun getFamilyName(): String = GdsBundle.message("inspection.suppress.for.declaration")

    override fun isAvailable(
        project: Project,
        context: PsiElement,
    ): Boolean = canSuppress(context)

    override fun isSuppressAll(): Boolean = false

    override fun applyFix(
        project: Project,
        descriptor: ProblemDescriptor,
    ) {
        val element = descriptor.psiElement ?: return
        val container = GdsSuppression.findContainer(element) ?: return
        val document = element.containingFile.viewProvider.document ?: return
        val manager = PsiDocumentManager.getInstance(project)
        manager.doPostponedOperationsAndUnblockDocument(document)

        // Inserting through PSI would reformat the new line with the code style indentation.
        val above = GdsSuppression.findCommentAbove(container)
        val idsEnd = above?.let { GdsSuppression.suppressedIdsEnd(it, toolId) }
        if (above != null && idsEnd != null) {
            document.insertString(above.textRange.startOffset + idsEnd, ",$toolId")
        } else {
            val first = GdsSuppression.firstCodeLeaf(container) ?: return
            val offset = first.textRange.startOffset
            val lineStart = document.getLineStartOffset(document.getLineNumber(offset))
            val indent = document.charsSequence.subSequence(lineStart, offset).takeWhile { it == ' ' || it == '\t' }
            val comment = "// ${SuppressionUtilCore.SUPPRESS_INSPECTIONS_TAG_NAME} $toolId"
            val previous = PsiTreeUtil.prevLeaf(first)
            if (previous == null || (previous is PsiWhiteSpace && previous.textContains('\n'))) {
                document.insertString(offset, "$comment\n$indent")
            } else {
                val start = if (previous is PsiWhiteSpace) previous.textRange.startOffset else offset
                document.replaceString(start, offset, "\n$indent$comment\n$indent")
            }
        }
        manager.commitDocument(document)
    }

    companion object {
        // A line comment cannot be put before a parameter without breaking the signature across lines.
        fun canSuppress(element: PsiElement): Boolean {
            val container = GdsSuppression.findContainer(element)
            return container != null && container !is GdsParameter
        }
    }
}

private object GdsSuppression {
    private val PATTERN = Pattern.compile("//" + SuppressionUtil.COMMON_SUPPRESS_REGEXP + ".*")

    fun findContainer(element: PsiElement): PsiElement? = element.parentOfTypes(GdsParameter::class, GdsStatement::class, GdsItem::class, withSelf = true)

    fun firstCodeLeaf(container: PsiElement): PsiElement? =
        generateSequence(PsiTreeUtil.firstChild(container)) { PsiTreeUtil.nextLeaf(it) }
            .takeWhile { PsiTreeUtil.isAncestor(container, it, false) }
            .firstOrNull { it !is PsiWhiteSpace && it !is PsiComment }

    private fun lastCodeLeaf(container: PsiElement): PsiElement? =
        generateSequence(PsiTreeUtil.lastChild(container)) { PsiTreeUtil.prevLeaf(it) }
            .takeWhile { PsiTreeUtil.isAncestor(container, it, false) }
            .firstOrNull { it !is PsiWhiteSpace && it !is PsiComment }

    fun findCommentAbove(container: PsiElement): PsiElement? {
        val first = firstCodeLeaf(container) ?: return null
        val space = PsiTreeUtil.prevLeaf(first) as? PsiWhiteSpace ?: return null
        val comment = PsiTreeUtil.prevLeaf(space) ?: return null
        if (comment.elementType != GdsTypes.LINE_COMMENT) return null
        val before = PsiTreeUtil.prevLeaf(comment)
        if (before != null && !(before is PsiWhiteSpace && before.textContains('\n'))) return null
        return comment
    }

    fun findTrailingComment(container: PsiElement): PsiElement? {
        val last = lastCodeLeaf(container) ?: return null
        var next = PsiTreeUtil.nextLeaf(last)
        if (next is PsiWhiteSpace) {
            if (next.textContains('\n')) return null
            next = PsiTreeUtil.nextLeaf(next)
        }
        return next?.takeIf { it.elementType == GdsTypes.LINE_COMMENT }
    }

    fun mentions(
        comment: PsiElement,
        toolId: String,
    ): Boolean {
        val matcher = PATTERN.matcher(comment.text)
        return matcher.matches() && SuppressionUtil.isInspectionToolIdMentioned(matcher.group(1), toolId)
    }

    fun suppressedIdsEnd(
        comment: PsiElement,
        toolId: String,
    ): Int? {
        val matcher = PATTERN.matcher(comment.text)
        if (!matcher.matches() || SuppressionUtil.isInspectionToolIdMentioned(matcher.group(1), toolId)) return null
        return matcher.end(1)
    }
}

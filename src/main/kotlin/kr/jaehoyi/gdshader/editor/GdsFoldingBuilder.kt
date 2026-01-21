package kr.jaehoyi.gdshader.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.psi.GdsBlock
import kr.jaehoyi.gdshader.psi.GdsInitializerList
import kr.jaehoyi.gdshader.psi.GdsStructBlock
import kr.jaehoyi.gdshader.psi.GdsSwitchBlock
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsFoldingBuilder : CustomFoldingBuilder(), DumbAware {

    private val foldingTargets = listOf(
        GdsBlock::class.java,
        GdsStructBlock::class.java,
        GdsSwitchBlock::class.java,
        GdsInitializerList::class.java
    )
    
    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean
    ) {

        for (type in foldingTargets) {
            val elements = PsiTreeUtil.findChildrenOfType(root, type)
            for (element in elements) {
                if (element.textRange.length < 2) continue
                descriptors.add(FoldingDescriptor(element.node, element.textRange))
            }
        }

        val commentStarts = PsiTreeUtil.collectElements(root) {
            it.node.elementType == GdsTypes.BLOCK_COMMENT_START
        }

        for (startNode in commentStarts) {
            var endNode = startNode.nextSibling

            while (endNode != null) {
                val type = endNode.node.elementType

                if (type == GdsTypes.BLOCK_COMMENT_END) {
                    val range = TextRange(startNode.textRange.startOffset, endNode.textRange.endOffset)
                    descriptors.add(FoldingDescriptor(startNode.node, range))
                    break
                }

                if (type != GdsTypes.BLOCK_COMMENT_CONTENT) {
                    break
                }

                endNode = endNode.nextSibling
            }
        }
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String {
        val elementType = node.elementType
        return when (elementType) {
            GdsTypes.BLOCK_COMMENT_START -> "/*...*/"
            else -> "{...}"
        }
    }

    override fun isRegionCollapsedByDefault(node: ASTNode): Boolean = false
}
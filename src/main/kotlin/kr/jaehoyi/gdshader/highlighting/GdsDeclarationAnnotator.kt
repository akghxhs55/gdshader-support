package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

class GdsDeclarationAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when {
            element is GdsStructDeclaration -> checkEmptyStruct(element, holder)
            element.node.elementType == GdsTypes.PP_INCLUDE_LINE -> checkIncludeLine(element, holder)
        }
    }

    private fun checkEmptyStruct(element: GdsStructDeclaration, holder: AnnotationHolder) {
        val structBlock = element.structBlock ?: return
        if (structBlock.structMemberList.structMemberList.isEmpty()) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Empty structs are not allowed")
                .range(element)
                .create()
        }
    }

    private fun checkIncludeLine(element: PsiElement, holder: AnnotationHolder) {
        val text = element.text
        val regex = Regex("[\"']([^\"']*)[\"']")
        val matchResult = regex.find(text) ?: return
        val path = matchResult.groups[1]?.value ?: return

        if (!path.endsWith(".gdshaderinc")) {
            val pathRange = matchResult.groups[1]!!.range
            val startOffset = element.textRange.startOffset + pathRange.first
            val endOffset = element.textRange.startOffset + pathRange.last + 1
            holder.newAnnotation(HighlightSeverity.ERROR, "Only '.gdshaderinc' files can be included")
                .range(com.intellij.openapi.util.TextRange(startOffset, endOffset))
                .create()
            return
        }

        val resolved = GdsPathUtil.resolvePath(element.containingFile, path)
        if (resolved == null) {
            val pathRange = matchResult.groups[1]!!.range
            val startOffset = element.textRange.startOffset + pathRange.first
            val endOffset = element.textRange.startOffset + pathRange.last + 1
            holder.newAnnotation(HighlightSeverity.ERROR, "Cannot resolve file '$path'")
                .range(com.intellij.openapi.util.TextRange(startOffset, endOffset))
                .create()
        }
    }
}
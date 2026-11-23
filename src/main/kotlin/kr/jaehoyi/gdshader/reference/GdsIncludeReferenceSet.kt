package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.util.IncorrectOperationException
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

class GdsIncludeReferenceSet private constructor(
    private val includePath: GdsPathUtil.IncludePath,
    private val isResPath: Boolean,
    element: PsiElement,
    prefixLength: Int,
) : FileReferenceSet(
        includePath.path.substring(prefixLength),
        element,
        includePath.rangeInElement.startOffset + prefixLength,
        null,
        SystemInfo.isFileSystemCaseSensitive,
        true,
    ) {
    override fun createFileReference(
        range: TextRange,
        index: Int,
        text: String,
    ): FileReference = GdsIncludeFileReference(this, range, index, text)

    override fun isAbsolutePathReference(): Boolean = false

    override fun computeDefaultContexts(): Collection<PsiFileSystemItem> {
        val file = containingFile ?: return emptyList()
        val root =
            if (isResPath) {
                GdsPathUtil.findResRoot(file)
            } else {
                file.originalFile.virtualFile?.parent
            }

        return toFileSystemItems(listOfNotNull(root))
    }

    private fun createPathTo(target: VirtualFile): String? {
        val file = containingFile ?: return null

        if (isResPath) {
            val resRoot = GdsPathUtil.findResRoot(file) ?: return null
            val relativePath = VfsUtilCore.getRelativePath(target, resRoot)
            return if (relativePath.isNullOrEmpty()) null else GdsPathUtil.RES_PREFIX + relativePath
        }

        val currentDir = file.originalFile.virtualFile?.parent ?: return null
        val relativePath = VfsUtilCore.findRelativePath(currentDir, target, '/')
        if (relativePath.isNullOrEmpty()) return null

        return if (pathString.startsWith("./") && !relativePath.startsWith("..")) "./$relativePath" else relativePath
    }

    private class GdsIncludeFileReference(
        private val referenceSet: GdsIncludeReferenceSet,
        range: TextRange,
        index: Int,
        text: String,
    ) : FileReference(referenceSet, range, index, text) {
        override fun innerResolve(
            caseSensitive: Boolean,
            containingFile: PsiFile,
        ): Array<ResolveResult> {
            if (!isLast) {
                return super.innerResolve(caseSensitive, containingFile)
            }

            // The included file must also satisfy Godot's rules, such as ignored directories.
            val file =
                GdsPathUtil.resolvePath(containingFile, referenceSet.includePath.path)
                    ?: return ResolveResult.EMPTY_ARRAY
            return arrayOf(PsiElementResolveResult(file))
        }

        override fun bindToElement(element: PsiElement): PsiElement = bindToElement(element, false)

        // Rebuilds the path up to this segment so that the original style (res:// or relative) is kept.
        override fun bindToElement(
            element: PsiElement,
            absolute: Boolean,
        ): PsiElement {
            val target =
                (element as? PsiFileSystemItem)?.virtualFile
                    ?: throw IncorrectOperationException("Cannot bind include path to $element")

            // Leave the path as is when the target is not reachable from this file, e.g. outside the Godot project.
            val newPath = referenceSet.createPathTo(target) ?: return getElement()

            val range = TextRange(referenceSet.includePath.rangeInElement.startOffset, rangeInElement.endOffset)
            return ElementManipulators.handleContentChange(getElement(), range, newPath) ?: getElement()
        }
    }

    companion object {
        fun create(element: PsiElement): GdsIncludeReferenceSet? {
            val includePath = GdsPathUtil.findIncludePath(element) ?: return null
            val isResPath = includePath.path.startsWith(GdsPathUtil.RES_PREFIX)
            val prefixLength = if (isResPath) GdsPathUtil.RES_PREFIX.length else 0

            return GdsIncludeReferenceSet(includePath, isResPath, element, prefixLength)
        }
    }
}

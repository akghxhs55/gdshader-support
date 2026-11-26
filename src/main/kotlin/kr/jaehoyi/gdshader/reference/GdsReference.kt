package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

abstract class GdsReference<T : PsiElement>(
    element: T,
    textRange: TextRange,
) : PsiReferenceBase<T>(element, textRange) {
    override fun isReferenceTo(element: PsiElement): Boolean {
        val resolved = resolve() ?: return false
        return element.manager.areElementsEquivalent(resolved, element) || isSameDeclarationInAnotherPath(resolved, element)
    }

    // A file reached through a symlinked directory is a separate VirtualFile from its target, so includes through
    // each path resolve to separate copies of the same declaration.
    private fun isSameDeclarationInAnotherPath(
        a: PsiElement,
        b: PsiElement,
    ): Boolean {
        if (a.javaClass != b.javaClass || a.textRange != b.textRange) return false
        val fileA = a.containingFile?.virtualFile ?: return false
        val fileB = b.containingFile?.virtualFile ?: return false
        if (fileA == fileB) return false
        val path = fileA.canonicalPath ?: return false
        return path == fileB.canonicalPath
    }
}

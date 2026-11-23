package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

class GdsFileReference(
    element: PsiElement,
    textRange: TextRange,
) : PsiReferenceBase<PsiElement>(element, textRange) {
    override fun resolve(): PsiElement? {
        val pathString = rangeInElement.substring(element.text)

        return GdsPathUtil.resolvePath(element.containingFile, pathString)
    }
}

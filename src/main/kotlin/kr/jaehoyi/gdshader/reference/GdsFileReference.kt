package kr.jaehoyi.gdshader.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import kr.jaehoyi.gdshader.completion.GdsLookupElements
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

class GdsFileReference(
    element: PsiElement,
    textRange: TextRange,
) : PsiReferenceBase<PsiElement>(element, textRange) {
    override fun resolve(): PsiElement? {
        val pathString = rangeInElement.substring(element.text)

        return GdsPathUtil.resolvePath(element.containingFile, pathString)
    }

    override fun getVariants(): Array<out Any> {
        val project = element.project
        val contextFile = element.containingFile
        val resRoot = GdsPathUtil.findResRoot(contextFile) ?: return emptyArray()
        val currentVirtualFile = contextFile.originalFile.virtualFile

        val result = mutableListOf<LookupElement>()

        val virtualFiles = FilenameIndex.getAllFilesByExt(project, "gdshaderinc", GlobalSearchScope.projectScope(project))
        for (file in virtualFiles) {
            if (file == currentVirtualFile) continue
            if (!GdsPathUtil.isIncludable(file, resRoot, currentVirtualFile)) continue

            val relativeFilePath = VfsUtilCore.getRelativePath(file, resRoot) ?: continue
            val resPath = GdsPathUtil.RES_PREFIX + relativeFilePath

            result.add(GdsLookupElements.createFromIncludeFilePath(resPath))
        }

        return result.toTypedArray()
    }
}

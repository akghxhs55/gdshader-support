package kr.jaehoyi.gdshader.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import kr.jaehoyi.gdshader.GdsIcons
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

class GdsFileReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange) {

    override fun resolve(): PsiElement? {
        val pathString = rangeInElement.substring(element.text)
        
        return GdsPathUtil.resolvePath(element.containingFile, pathString)
    }

    override fun getVariants(): Array<out Any> {
        val project = element.project
        
        val contentRoots = ProjectRootManager.getInstance(project).contentRoots
        if (contentRoots.isEmpty()) return emptyArray()
        
        val projectBaseDir = contentRoots[0]
        
        val result = mutableListOf<LookupElement>()
        
        val virtualFiles = FilenameIndex.getAllFilesByExt(project, "gdshaderinc", GlobalSearchScope.projectScope(project))
        for (file in virtualFiles) {
            val parentDir = file.parent
            val folderPath = VfsUtilCore.getRelativePath(parentDir, projectBaseDir) ?: ""
            
            val relativeFilePath = VfsUtilCore.getRelativePath(file, projectBaseDir) ?: continue
            val resPath = "res://$relativeFilePath"
            
            result.add(
                LookupElementBuilder.create(resPath)
                    .withPresentableText(file.name)
                    .withTypeText(folderPath)
                    .withIcon(GdsIcons.GDSHADERINC)
                    .withCaseSensitivity(false)
            )
        }
        
        return result.toTypedArray()
    }
    
}
package kr.jaehoyi.gdshader.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import kr.jaehoyi.gdshader.GdsIcons

class GdsFileReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement>(element, textRange) {

    override fun resolve(): PsiElement? {
        val pathString = rangeInElement.substring(element.text)
        
        val targetVirtualFile =
            if (pathString.startsWith("res://")) {
                val clearPath = pathString.removePrefix("res://")
                val projectBaseDir = element.project.guessProjectDir()
                projectBaseDir?.findFileByRelativePath(clearPath)
            } else {
                val currentFile = element.containingFile.originalFile.virtualFile
                currentFile.parent.findFileByRelativePath(pathString)
            }
        
        if (targetVirtualFile != null) {
            return PsiManager.getInstance(element.project).findFile(targetVirtualFile)
        }
        
        return null
    }

    override fun getVariants(): Array<out Any> {
        val project = element.project
        val projectBaseDir = element.project.guessProjectDir() ?: return emptyArray()
        
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
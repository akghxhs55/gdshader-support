package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase

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
    
}
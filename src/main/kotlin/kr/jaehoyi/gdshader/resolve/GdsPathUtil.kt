package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

object GdsPathUtil {
    
    fun resolvePath(contextFile: PsiFile, rawPath: String): PsiFile? {
        val project = contextFile.project
        val cleanPath = rawPath.trim().removeSurrounding("\"")
        
        if (cleanPath.isEmpty()) {
            return null
        }

        val targetVirtualFile =
            if (cleanPath.startsWith("res://")) {
                val relativePath = cleanPath.removePrefix("res://")
                val projectRoot = getProjectRoot(project) ?: return null

                projectRoot.findFileByRelativePath(relativePath)
            } else {
                val currentFile = contextFile.originalFile.virtualFile ?: return null
                val parentDir = currentFile.parent ?: return null
                
                parentDir.findFileByRelativePath(cleanPath)
            }

        return targetVirtualFile?.let {
            PsiManager.getInstance(project).findFile(it)
        }
    }

    private fun getProjectRoot(project: Project): VirtualFile? {
        val contentRoots = ProjectRootManager.getInstance(project).contentRoots
        if (contentRoots.isNotEmpty()) {
            return contentRoots[0]
        }

        val basePath = project.basePath ?: return null
        return LocalFileSystem.getInstance().findFileByPath(basePath)
    }
    
}
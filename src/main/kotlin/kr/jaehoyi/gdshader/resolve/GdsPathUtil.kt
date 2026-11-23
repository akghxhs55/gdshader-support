package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

object GdsPathUtil {
    const val RES_PREFIX = "res://"
    private const val GODOT_PROJECT_FILE = "project.godot"
    private const val GDIGNORE_FILE = ".gdignore"
    private const val PROJECT_DATA_DIR = ".godot"

    private val INCLUDE_PATH_REGEX = Regex("\"([^\"]*)\"")

    data class IncludePath(
        val path: String,
        val rangeInElement: TextRange,
    )

    fun findIncludePath(includeLine: PsiElement): IncludePath? = findIncludePath(includeLine.text)

    fun findIncludePath(includeLineText: String): IncludePath? {
        val group = INCLUDE_PATH_REGEX.find(includeLineText)?.groups?.get(1) ?: return null
        return IncludePath(group.value, TextRange(group.range.first, group.range.last + 1))
    }

    fun resolvePath(
        contextFile: PsiFile,
        rawPath: String,
    ): PsiFile? {
        val project = contextFile.project
        val cleanPath = rawPath.trim().removeSurrounding("\"")

        if (cleanPath.isEmpty()) {
            return null
        }

        val currentFile = contextFile.originalFile.virtualFile
        val resRoot = findResRoot(contextFile)

        val targetVirtualFile =
            if (cleanPath.startsWith(RES_PREFIX)) {
                val relativePath = cleanPath.removePrefix(RES_PREFIX)
                resRoot?.findFileByRelativePath(relativePath)
            } else {
                currentFile?.parent?.findFileByRelativePath(cleanPath)
            } ?: return null

        if (resRoot != null && !isIncludable(targetVirtualFile, resRoot, currentFile)) {
            return null
        }

        return PsiManager.getInstance(project).findFile(targetVirtualFile)
    }

    fun isIncludable(
        target: VirtualFile,
        resRoot: VirtualFile,
        contextFile: VirtualFile?,
    ): Boolean {
        if (!VfsUtilCore.isAncestor(resRoot, target, true)) {
            return false
        }

        var dir = target.parent
        while (dir != null && dir != resRoot) {
            // A file inside an ignored directory may still include its neighbors.
            val containsContext = contextFile != null && VfsUtilCore.isAncestor(dir, contextFile, true)
            if (!containsContext && isIgnoredDirectory(dir, resRoot)) {
                return false
            }
            dir = dir.parent
        }

        return true
    }

    // Mirrors EditorFileSystem::_should_skip_directory in Godot.
    private fun isIgnoredDirectory(
        dir: VirtualFile,
        resRoot: VirtualFile,
    ): Boolean =
        (dir.parent == resRoot && dir.name == PROJECT_DATA_DIR) ||
            dir.findChild(GODOT_PROJECT_FILE) != null ||
            dir.findChild(GDIGNORE_FILE) != null

    fun findResRoot(contextFile: PsiFile): VirtualFile? {
        val project = contextFile.project
        val virtualFile = contextFile.originalFile.virtualFile

        if (virtualFile == null) {
            return ProjectRootManager.getInstance(project).contentRoots.firstOrNull()
                ?: findProjectBaseDir(project)
        }

        return findResRoot(virtualFile, project)
    }

    private fun findResRoot(
        file: VirtualFile,
        project: Project,
    ): VirtualFile? =
        findGodotProjectDir(file)
            ?: ProjectFileIndex.getInstance(project).getContentRootForFile(file)
            ?: findProjectBaseDir(project)

    private fun findGodotProjectDir(file: VirtualFile): VirtualFile? {
        var dir = if (file.isDirectory) file else file.parent
        while (dir != null) {
            if (dir.findChild(GODOT_PROJECT_FILE) != null) {
                return dir
            }
            dir = dir.parent
        }
        return null
    }

    private fun findProjectBaseDir(project: Project): VirtualFile? {
        val basePath = project.basePath ?: return null
        return LocalFileSystem.getInstance().findFileByPath(basePath)
    }
}

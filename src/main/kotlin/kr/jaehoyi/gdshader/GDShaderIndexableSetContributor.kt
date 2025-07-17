package kr.jaehoyi.gdshader

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.IndexableSetContributor

class GDShaderIndexableSetContributor : IndexableSetContributor() {
    override fun getAdditionalRootsToIndex(): Set<VirtualFile?> = emptySet()

    override fun getAdditionalProjectRootsToIndex(project: Project): Set<VirtualFile?> {
        val result = mutableSetOf<VirtualFile?>()
        val baseDir = project.guessProjectDir() ?: return result

        VfsUtilCore.iterateChildrenRecursively(
            baseDir,
            { true },
            { fileOrDir ->
                if (!fileOrDir.isDirectory && fileOrDir.extension.equals("gdshader", ignoreCase = true)) {
                    result.add(fileOrDir)
                }
                true
            }
        )
        
        return result
    }
}
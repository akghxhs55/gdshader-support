package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.DelegatingGlobalSearchScope
import com.intellij.psi.search.GlobalSearchScope

object GdsSearchScopes {
    // Rider's project scope only covers files in the solution model, which leaves out Godot files.
    fun godotFiles(project: Project): GlobalSearchScope = GodotFilesScope(project)

    private class GodotFilesScope(
        project: Project,
    ) : DelegatingGlobalSearchScope(allScope(project)) {
        private val fileIndex = ProjectFileIndex.getInstance(project)

        override fun contains(file: VirtualFile): Boolean =
            super.contains(file) &&
                (fileIndex.getContentRootForFile(file, false) != null || GdsPathUtil.findGodotProjectDir(file) != null)
    }
}

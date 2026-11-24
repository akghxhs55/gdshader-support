package kr.jaehoyi.gdshader

import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.testFramework.PsiTestUtil
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

// An excluded directory registered as a library source root is indexed but left out of the project scope,
// like Godot files in Rider's solution scope.
fun withDirectoryOutsideProjectScope(
    module: Module,
    directory: VirtualFile,
    action: () -> Unit,
) {
    val project = module.project
    PsiTestUtil.addExcludedRoot(module, directory)
    val library = PsiTestUtil.addProjectLibrary(module, "outside-project-scope", emptyList(), listOf(directory))
    try {
        VfsUtilCore.iterateChildrenRecursively(directory, null) { file ->
            if (!file.isDirectory) {
                assertFalse(GlobalSearchScope.projectScope(project).contains(file))
                assertTrue(GlobalSearchScope.allScope(project).contains(file))
            }
            true
        }
        action()
    } finally {
        PsiTestUtil.removeLibrary(module, library)
        WriteAction.runAndWait<Throwable> {
            LibraryTablesRegistrar.getInstance().getLibraryTable(project).removeLibrary(library)
        }
        PsiTestUtil.removeExcludedRoot(module, directory)
    }
}

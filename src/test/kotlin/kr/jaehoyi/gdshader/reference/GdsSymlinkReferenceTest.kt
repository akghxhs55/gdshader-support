package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.IndexingTestUtil
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.highlighting.GdsUnusedSymbolInspection
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.resolve.GdsSearchScopes
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

// Mirrors a library folder shared between Godot projects through a symlink: Game/Lib.Shaders -> ../Lib.
class GdsSymlinkReferenceTest : BasePlatformTestCase() {
    private lateinit var baseDir: Path
    private lateinit var root: VirtualFile

    override fun setUp() {
        super.setUp()
        baseDir = Files.createTempDirectory("gds-symlink").toRealPath()
        Files.createDirectories(baseDir.resolve("Lib"))
        Files.writeString(
            baseDir.resolve("Lib/Effect.gdshaderinc"),
            """
            float effect(float x) {
                return x * 2.0;
            }
            float unused_effect(float x) {
                return x;
            }
            """.trimIndent(),
        )
        Files.createDirectories(baseDir.resolve("Game"))
        Files.writeString(baseDir.resolve("Game/project.godot"), "")
        Files.writeString(
            baseDir.resolve("Game/main.gdshader"),
            """
            shader_type canvas_item;
            #include "res://Lib.Shaders/Effect.gdshaderinc"
            void fragment() {
                COLOR = vec4(effect(UV.x));
            }
            """.trimIndent(),
        )
        createDirectoryLink(baseDir.resolve("Game/Lib.Shaders"), baseDir.resolve("Lib"))

        root = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(baseDir)!!
        VfsUtil.markDirtyAndRefresh(false, true, true, root)
        PsiTestUtil.addContentRoot(module, root)
        IndexingTestUtil.waitUntilIndexesAreReady(project)
        myFixture.enableInspections(GdsUnusedSymbolInspection().apply { reportIncludeTopLevelSymbols = true })
    }

    override fun tearDown() {
        try {
            PsiTestUtil.removeContentEntry(module, root)
            FileUtil.delete(baseDir)
        } catch (e: Throwable) {
            addSuppressedException(e)
        } finally {
            super.tearDown()
        }
    }

    fun `test usages through symlink are found from target path`() {
        val usages = findUsages("Lib/Effect.gdshaderinc", "effect")
        assertEquals(listOf("main.gdshader"), usages)
    }

    fun `test usages through symlink are found from symlink path`() {
        val usages = findUsages("Game/Lib.Shaders/Effect.gdshaderinc", "effect")
        assertEquals(listOf("main.gdshader"), usages)
    }

    fun `test function used through symlink is not reported as unused`() {
        myFixture.configureFromExistingVirtualFile(root.findFileByRelativePath("Lib/Effect.gdshaderinc")!!)
        val unused =
            myFixture
                .doHighlighting()
                .mapNotNull { it.description }
                .filter { it.endsWith("is never used") }
        assertEquals(listOf("Function 'unused_effect' is never used"), unused)
    }

    private fun findUsages(
        path: String,
        name: String,
    ): List<String> {
        val file = PsiManager.getInstance(project).findFile(root.findFileByRelativePath(path)!!)!!
        val declaration = PsiTreeUtil.findChildrenOfType(file, GdsFunctionNameDecl::class.java).first { it.name == name }
        return ReferencesSearch
            .search(declaration, GdsSearchScopes.godotFiles(project), false)
            .findAll()
            .map { it.element.containingFile.name }
    }

    private fun createDirectoryLink(
        link: Path,
        target: Path,
    ) {
        try {
            Files.createSymbolicLink(link, target)
        } catch (e: IOException) {
            if (!SystemInfo.isWindows) throw e
            // Symlinks on Windows need Developer Mode or administrator rights, but junctions do not.
            val process = ProcessBuilder("cmd", "/c", "mklink", "/J", link.toString(), target.toString()).redirectErrorStream(true).start()
            val output = process.inputStream.bufferedReader().readText()
            check(process.waitFor() == 0) { output }
        }
    }
}

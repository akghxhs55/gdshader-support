package kr.jaehoyi.gdshader.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsFile

class GdsIncludeReferenceTest : BasePlatformTestCase() {
    fun `test absolute path reference`() {
        val targetFile = myFixture.configureByText("utils.gdshaderinc", "// utility functions")

        myFixture.configureByText(
            "main.gdshader",
            """
            )
            #include "res://<caret>utils.gdshaderinc"
            """.trimIndent(),
        )

        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())

        val resolvedElement = reference.resolve()

        assertInstanceOf(resolvedElement, GdsFile::class.java)
        assertEquals(targetFile, resolvedElement)
        assertEquals("utils.gdshaderinc", (resolvedElement as PsiFile).name)
    }

    fun `test relative path reference`() {
        val targetFile = myFixture.addFileToProject("shaders/common.gdshaderinc", "// common shader functions")

        val mainFile =
            myFixture.addFileToProject(
                "shaders/main.gdshader",
                """
                )
                #include "common.gdshaderinc"
                """.trimIndent(),
            )

        myFixture.configureFromExistingVirtualFile(mainFile.virtualFile)

        myFixture.editor.caretModel.moveToOffset(mainFile.text.indexOf("common.gdshaderinc") + 1)

        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())

        val resolvedElement = reference.resolve()

        assertInstanceOf(resolvedElement, GdsFile::class.java)
        assertEquals(targetFile, resolvedElement)
        assertEquals("common.gdshaderinc", (resolvedElement as PsiFile).name)
    }

    fun `test parent directory relative path reference`() {
        val targetFile = myFixture.addFileToProject("shaders/includes/math.gdshaderinc", "// math functions")

        val mainFile =
            myFixture.addFileToProject(
                "shaders/main.gdshader",
                """
                )
                #include "includes/math.gdshaderinc"
                """.trimIndent(),
            )

        myFixture.configureFromExistingVirtualFile(mainFile.virtualFile)

        myFixture.editor.caretModel.moveToOffset(mainFile.text.indexOf("includes/math.gdshaderinc") + 1)

        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())

        val resolvedElement = reference.resolve()

        assertInstanceOf(resolvedElement, GdsFile::class.java)
        assertEquals(targetFile, resolvedElement)
        assertEquals("math.gdshaderinc", (resolvedElement as PsiFile).name)
    }

    fun `test res path is resolved from godot project directory`() {
        myFixture.addFileToProject("game/project.godot", "")
        val targetFile = myFixture.addFileToProject("game/lib/foo.gdshaderinc", "// foo")

        val resolvedElement = resolveIncludeIn("game/shaders/main.gdshader", "res://lib/foo.gdshaderinc")

        assertEquals(targetFile, resolvedElement)
    }

    fun `test res path is not resolved from ide root when godot project is nested`() {
        myFixture.addFileToProject("game/project.godot", "")
        myFixture.addFileToProject("game/lib/foo.gdshaderinc", "// foo")

        val resolvedElement = resolveIncludeIn("game/shaders/main.gdshader", "res://game/lib/foo.gdshaderinc")

        assertNull(resolvedElement)
    }

    fun `test res path uses nearest godot project directory`() {
        myFixture.addFileToProject("project.godot", "")
        myFixture.addFileToProject("sub/project.godot", "")
        myFixture.addFileToProject("foo.gdshaderinc", "// outer")
        val innerFile = myFixture.addFileToProject("sub/foo.gdshaderinc", "// inner")

        val resolvedElement = resolveIncludeIn("sub/main.gdshader", "res://foo.gdshaderinc")

        assertEquals(innerFile, resolvedElement)
    }

    fun `test res path into nested godot project is not resolved`() {
        myFixture.addFileToProject("project.godot", "")
        myFixture.addFileToProject("sub/project.godot", "")
        myFixture.addFileToProject("sub/foo.gdshaderinc", "// inner")

        assertNull(resolveIncludeIn("main.gdshader", "res://sub/foo.gdshaderinc"))
    }

    fun `test relative path into nested godot project is not resolved`() {
        myFixture.addFileToProject("project.godot", "")
        myFixture.addFileToProject("sub/project.godot", "")
        myFixture.addFileToProject("sub/foo.gdshaderinc", "// inner")

        assertNull(resolveIncludeIn("main.gdshader", "sub/foo.gdshaderinc"))
    }

    fun `test relative path outside godot project is not resolved`() {
        myFixture.addFileToProject("game/project.godot", "")
        myFixture.addFileToProject("outside.gdshaderinc", "// outside")

        assertNull(resolveIncludeIn("game/main.gdshader", "../outside.gdshaderinc"))
    }

    fun `test path into gdignore directory is not resolved`() {
        myFixture.addFileToProject("project.godot", "")
        myFixture.addFileToProject("ignored/.gdignore", "")
        myFixture.addFileToProject("ignored/deep/foo.gdshaderinc", "// ignored")

        assertNull(resolveIncludeIn("main.gdshader", "res://ignored/deep/foo.gdshaderinc"))
    }

    fun `test path into project data directory is not resolved`() {
        myFixture.addFileToProject("project.godot", "")
        myFixture.addFileToProject(".godot/foo.gdshaderinc", "// data")

        assertNull(resolveIncludeIn("main.gdshader", "res://.godot/foo.gdshaderinc"))
    }

    fun `test file inside gdignore directory can include its neighbors`() {
        myFixture.addFileToProject("project.godot", "")
        myFixture.addFileToProject("ignored/.gdignore", "")
        val targetFile = myFixture.addFileToProject("ignored/foo.gdshaderinc", "// neighbor")

        val resolvedElement = resolveIncludeIn("ignored/main.gdshader", "foo.gdshaderinc")

        assertEquals(targetFile, resolvedElement)
    }

    fun `test single quoted path has no reference`() {
        myFixture.addFileToProject("utils.gdshaderinc", "// utility functions")

        myFixture.configureByText(
            "main.gdshader",
            """
            #include 'res://<caret>utils.gdshaderinc'
            """.trimIndent(),
        )

        assertNull(myFixture.getReferenceAtCaretPosition())
    }

    fun `test non-existing file reference`() {
        myFixture.configureByText(
            "main.gdshader",
            """
            #include "non<caret>_existing_file.gdshaderinc"
            """.trimIndent(),
        )

        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())

        val resolvedElement = reference.resolve()

        assertNull(resolvedElement)
    }

    private fun resolveIncludeIn(
        filePath: String,
        includePath: String,
    ): PsiElement? {
        val mainFile = myFixture.addFileToProject(filePath, "#include \"$includePath\"")

        myFixture.configureFromExistingVirtualFile(mainFile.virtualFile)
        myFixture.editor.caretModel.moveToOffset(mainFile.text.indexOf(includePath) + 1)

        return requireNotNull(myFixture.getReferenceAtCaretPosition()).resolve()
    }
}

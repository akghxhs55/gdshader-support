package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.VfsTestUtil

class IncludeCompletionTest : GdsCompletionTestBase() {
    fun `test include shows file list inside quotes`() {
        myFixture.addFileToProject("utils.gdshaderinc", "void util() {}")
        myFixture.addFileToProject("lib/math.gdshaderinc", "float pi = 3.14;")
        myFixture.addFileToProject("ignored.gdshader", "text file")

        myFixture.configureByText(
            "main.gdshader",
            """
            #include "<caret>"
            void main() {}
            """.trimIndent(),
        )

        val suggestedStrings = completeAndGetStrings()

        assertContainsElements(suggestedStrings, "res://utils.gdshaderinc", "res://lib/math.gdshaderinc")
        assertDoesntContain(suggestedStrings, "ignored.gdshader")
    }

    fun `test include completion handles subdirectories`() {
        myFixture.addFileToProject("common/core.gdshaderinc", "")
        myFixture.addFileToProject("utils.gdshaderinc", "")

        val filePath = "common/shader.gdshader"
        myFixture.addFileToProject(
            filePath,
            """
            #include "<caret>"
            """.trimIndent(),
        )

        myFixture.configureFromTempProjectFile(filePath)

        val suggestedStrings = completeAndGetStrings()

        assertContainsElements(suggestedStrings, "res://common/core.gdshaderinc", "res://utils.gdshaderinc")
    }

    fun `test include completion is relative to godot project directory`() {
        myFixture.addFileToProject("game/project.godot", "")
        myFixture.addFileToProject("game/lib/foo.gdshaderinc", "")
        myFixture.addFileToProject("game/bar.gdshaderinc", "")

        val suggestedStrings = completeIncludeIn("game/shaders/main.gdshader")

        assertContainsElements(suggestedStrings, "res://lib/foo.gdshaderinc", "res://bar.gdshaderinc")
        assertDoesntContain(suggestedStrings, "res://game/lib/foo.gdshaderinc", "res://game/bar.gdshaderinc")
    }

    fun `test include completion excludes files from other godot projects`() {
        myFixture.addFileToProject("game_a/project.godot", "")
        myFixture.addFileToProject("game_a/a.gdshaderinc", "")
        myFixture.addFileToProject("game_a/common.gdshaderinc", "")
        myFixture.addFileToProject("game_b/project.godot", "")
        myFixture.addFileToProject("game_b/b.gdshaderinc", "")
        myFixture.addFileToProject("outside.gdshaderinc", "")

        val suggestedStrings = completeIncludeIn("game_a/main.gdshader")

        assertContainsElements(suggestedStrings, "res://a.gdshaderinc", "res://common.gdshaderinc")
        assertDoesntContain(suggestedStrings, "res://b.gdshaderinc", "res://outside.gdshaderinc", "res://game_b/b.gdshaderinc")
    }

    fun `test include completion excludes nested godot project and ignored directories`() {
        myFixture.addFileToProject("project.godot", "")
        myFixture.addFileToProject("visible.gdshaderinc", "")
        myFixture.addFileToProject("also_visible.gdshaderinc", "")
        myFixture.addFileToProject("sub/project.godot", "")
        myFixture.addFileToProject("sub/nested.gdshaderinc", "")
        myFixture.addFileToProject("ignored/.gdignore", "")
        myFixture.addFileToProject("ignored/hidden.gdshaderinc", "")
        myFixture.addFileToProject(".godot/data.gdshaderinc", "")

        val suggestedStrings = completeIncludeIn("main.gdshader")

        assertContainsElements(suggestedStrings, "res://visible.gdshaderinc", "res://also_visible.gdshaderinc")
        assertDoesntContain(
            suggestedStrings,
            "res://sub/nested.gdshaderinc",
            "res://ignored/hidden.gdshaderinc",
            "res://.godot/data.gdshaderinc",
        )
    }

    fun `test include completion excludes current file`() {
        myFixture.addFileToProject("lib/other.gdshaderinc", "")
        myFixture.addFileToProject("lib/another.gdshaderinc", "")

        val suggestedStrings = completeIncludeIn("lib/self.gdshaderinc")

        assertContainsElements(suggestedStrings, "res://lib/other.gdshaderinc", "res://lib/another.gdshaderinc")
        assertDoesntContain(suggestedStrings, "res://lib/self.gdshaderinc")
    }

    fun `test res prefix is suggested`() {
        assertContainsElements(completeIncludeLine("a.gdshader", "#include \"<caret>\""), "res://")
        assertContainsElements(completeIncludeLine("b.gdshader", "#include \"r<caret>\""), "res://")
        assertContainsElements(completeIncludeLine("c.gdshader", "#include \"res:/<caret>\""), "res://")
    }

    fun `test res prefix is not suggested after it is typed`() {
        myFixture.addFileToProject("utils.gdshaderinc", "")
        myFixture.addFileToProject("other.gdshaderinc", "")

        val suggestedStrings = completeIncludeLine("main.gdshader", "#include \"res://<caret>\"")

        assertDoesntContain(suggestedStrings, "res://")
    }

    fun `test selecting res prefix inserts it`() {
        myFixture.addFileToProject("utils.gdshaderinc", "")
        completeIncludeLine("main.gdshader", "#include \"<caret>\"")

        selectLookupItem("res://", Lookup.NORMAL_SELECT_CHAR)

        myFixture.checkResult("#include \"res://<caret>\"")
    }

    fun `test include completion works without closing quote`() {
        myFixture.addFileToProject("lib/math.gdshaderinc", "")
        myFixture.addFileToProject("utils.gdshaderinc", "")

        val suggestedStrings = completeIncludeLine("main.gdshader", "#include \"res://l<caret>\nvoid f() {}")

        assertContainsElements(suggestedStrings, "res://lib/math.gdshaderinc")
        assertDoesntContain(suggestedStrings, "include", "res://utils.gdshaderinc")
    }

    fun `test include completion matches file name`() {
        myFixture.addFileToProject("lib/math.gdshaderinc", "")
        myFixture.addFileToProject("utils.gdshaderinc", "")

        val suggestedStrings = completeIncludeLine("main.gdshader", "#include \"ma<caret>\"")

        assertContainsElements(suggestedStrings, "res://lib/math.gdshaderinc")
        assertDoesntContain(suggestedStrings, "res://utils.gdshaderinc")
    }

    fun `test include completion suggests parent relative paths`() {
        myFixture.addFileToProject("lib/math.gdshaderinc", "")
        myFixture.addFileToProject("utils.gdshaderinc", "")

        val suggestedStrings = completeIncludeLine("shaders/main.gdshader", "#include \"../<caret>\"")

        assertContainsElements(suggestedStrings, "../lib/math.gdshaderinc", "../utils.gdshaderinc")
    }

    fun `test include completion suggests current directory relative paths`() {
        myFixture.addFileToProject("lib/math.gdshaderinc", "")
        myFixture.addFileToProject("lib/vector.gdshaderinc", "")

        assertContainsElements(
            completeIncludeLine("main.gdshader", "#include \"lib/<caret>\""),
            "lib/math.gdshaderinc",
            "lib/vector.gdshaderinc",
        )
        assertContainsElements(
            completeIncludeLine("other.gdshader", "#include \"./<caret>\""),
            "./lib/math.gdshaderinc",
            "./lib/vector.gdshaderinc",
        )
    }

    fun `test include completion replaces whole path on tab`() {
        myFixture.addFileToProject("utils.gdshaderinc", "")
        myFixture.addFileToProject("other.gdshaderinc", "")
        completeIncludeLine("main.gdshader", "#include \"res://<caret>old/path.gdshaderinc\"")

        selectLookupItem("res://utils.gdshaderinc", Lookup.REPLACE_SELECT_CHAR)

        myFixture.checkResult("#include \"res://utils.gdshaderinc<caret>\"")
    }

    fun `test include completion works outside project content`() {
        val outsideRoot = createOutsideContentDir("outside_content")
        try {
            createFile(outsideRoot, "project.godot", "")
            createFile(outsideRoot, "lib/math.gdshaderinc", "")
            createFile(outsideRoot, "utils.gdshaderinc", "")
            createFile(outsideRoot, "ignored/.gdignore", "")
            createFile(outsideRoot, "ignored/hidden.gdshaderinc", "")
            createFile(outsideRoot, "sub/project.godot", "")
            createFile(outsideRoot, "sub/nested.gdshaderinc", "")
            val mainFile = createFile(outsideRoot, "main.gdshader", "#include \"\"")
            assertFalse(ProjectFileIndex.getInstance(project).isInContent(mainFile))

            myFixture.configureFromExistingVirtualFile(mainFile)
            myFixture.editor.caretModel.moveToOffset("#include \"".length)

            val suggestedStrings = completeAndGetStrings()

            assertContainsElements(suggestedStrings, "res://lib/math.gdshaderinc", "res://utils.gdshaderinc")
            assertDoesntContain(suggestedStrings, "res://ignored/hidden.gdshaderinc", "res://sub/nested.gdshaderinc")
        } finally {
            WriteAction.runAndWait<Throwable> { outsideRoot.delete(this) }
        }
    }

    private fun createOutsideContentDir(name: String): VirtualFile {
        val contentRoot = ProjectRootManager.getInstance(project).contentRoots.first()
        return WriteAction.computeAndWait<VirtualFile, Throwable> {
            contentRoot.parent.createChildDirectory(this, name)
        }
    }

    private fun createFile(
        root: VirtualFile,
        relativePath: String,
        text: String,
    ): VirtualFile = VfsTestUtil.createFile(root, relativePath, text)

    private fun completeIncludeIn(filePath: String): List<String> = completeIncludeLine(filePath, "#include \"<caret>\"")

    private fun completeIncludeLine(
        filePath: String,
        text: String,
    ): List<String> {
        myFixture.addFileToProject(filePath, text)
        myFixture.configureFromTempProjectFile(filePath)

        return completeAndGetStrings()
    }

    private fun selectLookupItem(
        lookupString: String,
        completionChar: Char,
    ) {
        val item = myFixture.lookupElements!!.first { it.lookupString == lookupString }
        myFixture.lookup.currentItem = item
        myFixture.finishLookup(completionChar)
    }
}

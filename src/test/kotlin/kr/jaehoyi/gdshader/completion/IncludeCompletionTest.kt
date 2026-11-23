package kr.jaehoyi.gdshader.completion

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

    private fun completeIncludeIn(filePath: String): List<String> {
        myFixture.addFileToProject(filePath, "#include \"<caret>\"")
        myFixture.configureFromTempProjectFile(filePath)

        return completeAndGetStrings()
    }
}

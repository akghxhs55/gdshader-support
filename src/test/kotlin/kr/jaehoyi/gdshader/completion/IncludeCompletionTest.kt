package kr.jaehoyi.gdshader.completion

class IncludeCompletionTest : GdsCompletionTestBase() {

    fun `test include shows file list inside quotes`() {
        myFixture.addFileToProject("utils.gdshaderinc", "void util() {}")
        myFixture.addFileToProject("lib/math.gdshaderinc", "float pi = 3.14;")
        myFixture.addFileToProject("ignored.gdshader", "text file")

        myFixture.configureByText("main.gdshader", """
            #include "<caret>"
            void main() {}
        """.trimIndent())

        val suggestedStrings = completeAndGetStrings()

        assertContainsElements(suggestedStrings, "res://utils.gdshaderinc", "res://lib/math.gdshaderinc")
        assertDoesntContain(suggestedStrings, "ignored.gdshader")
    }

    fun `test include completion handles subdirectories`() {
        myFixture.addFileToProject("common/core.gdshaderinc", "")
        myFixture.addFileToProject("utils.gdshaderinc", "")

        val filePath = "common/shader.gdshader"
        myFixture.addFileToProject(filePath, """
            #include "<caret>"
        """.trimIndent())

        myFixture.configureFromTempProjectFile(filePath)

        val suggestedStrings = completeAndGetStrings()

        assertContainsElements(suggestedStrings, "res://common/core.gdshaderinc", "res://utils.gdshaderinc")
    }

}

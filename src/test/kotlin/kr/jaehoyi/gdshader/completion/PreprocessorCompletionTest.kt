package kr.jaehoyi.gdshader.completion

class PreprocessorCompletionTest : GdsCompletionTestBase() {

    fun `test preprocessor directives completion after hash`() {
        myFixture.configureByText("test.gdshader", """
            #<caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        val expected = listOf(
            "define", "undef", "if", "ifdef", "ifndef", "elif", "else", "endif", "error", "pragma", "include"
        )
        assertContainsElements(completions, expected)
    }

    fun `test preprocessor directives completion with prefix`() {
        myFixture.configureByText("test.gdshader", """
            #def<caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "define")
        assertDoesntContain(completions, "if", "include", "pragma")
    }
}

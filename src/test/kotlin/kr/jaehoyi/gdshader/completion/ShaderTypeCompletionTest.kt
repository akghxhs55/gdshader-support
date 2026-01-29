package kr.jaehoyi.gdshader.completion

class ShaderTypeCompletionTest : GdsCompletionTestBase() {

    fun `test shader type keyword`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "shader_type")
    }

    fun `test shader type values`() {
        myFixture.configureByText("test.gdshader", """
            shader_type <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, GdsKeywords.SHADER_TYPES)
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }

}

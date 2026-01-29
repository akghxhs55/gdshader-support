package kr.jaehoyi.gdshader.completion

class RenderModeCompletionTest : GdsCompletionTestBase() {

    fun `test render mode keyword`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "render_mode")
    }

    fun `test render mode values`() {
        myFixture.configureByText("test.gdshader", """
            render_mode <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, GdsKeywords.RENDER_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }

    fun `test second render mode values`() {
        myFixture.configureByText("test.gdshader", """
            render_mode blend_add, <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, GdsKeywords.RENDER_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }

}

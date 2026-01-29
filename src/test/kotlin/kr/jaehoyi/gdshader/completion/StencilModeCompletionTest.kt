package kr.jaehoyi.gdshader.completion

class StencilModeCompletionTest : GdsCompletionTestBase() {

    fun `test stencil mode keyword`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "stencil_mode")
    }

    fun `test stencil mode values`() {
        myFixture.configureByText("test.gdshader", """
            stencil_mode <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, GdsKeywords.STENCIL_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }

    fun `test second stencil mode values`() {
        myFixture.configureByText("test.gdshader", """
            stencil_mode write, <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, GdsKeywords.STENCIL_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }

}

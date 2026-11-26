package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.model.ShaderType

class RenderModeCompletionTest : GdsCompletionTestBase() {
    fun `test render mode values`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            render_mode <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, GdsKeywords.RENDER_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }

    fun `test second render mode values`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            render_mode blend_add, <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, GdsKeywords.RENDER_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }

    fun `test render modes are limited to the shader type`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type sky;
            render_mode <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertSameElements(completions, GdsKeywords.RENDER_MODES.getValue(ShaderType.SKY))
        assertContainsElements(completions, "use_debanding")
    }

    fun `test render modes shared by shader types are listed once`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            render_mode <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertEquals(completions.distinct(), completions)
    }
}

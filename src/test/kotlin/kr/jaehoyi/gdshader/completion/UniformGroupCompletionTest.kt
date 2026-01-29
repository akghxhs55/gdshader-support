package kr.jaehoyi.gdshader.completion

class UniformGroupCompletionTest : GdsCompletionTestBase() {

    fun `test uniform group keyword`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "group_uniforms")
    }

    fun `test uniform group values`() {
        myFixture.configureByText("test.gdshader", """
            group_uniforms <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "shader_type", "group_uniforms", "void", "uniform")
    }

}

package kr.jaehoyi.gdshader.completion

class ToplevelCompletionTest : GdsCompletionTestBase() {

    fun `test in empty file`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertTopLevelKeywords(completions)
    }

    fun `test before toplevel declaration`() {
        myFixture.configureByText("test.gdshader", """
            <caret>

            shader_type spatial;
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertTopLevelKeywords(completions)
    }

    fun `test after toplevel declaration`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertTopLevelKeywords(completions)
    }

    fun `test between toplevel declaration`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            <caret>

            void fragment() {
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertTopLevelKeywords(completions)
    }

    fun `test in function body`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "shader_type", "render_mode", "stencil_mode", "group_uniforms")
    }

    private fun assertTopLevelKeywords(completions: List<String>) {
        assertContainsElements(completions,
            "shader_type", "render_mode", "stencil_mode", "group_uniforms", "uniform", "const", "varying",
            "struct", "highp", "mediump", "lowp", "void", "bool", "int", "float", "vec2", "vec3", "vec4")
    }

}

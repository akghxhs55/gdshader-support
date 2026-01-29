package kr.jaehoyi.gdshader.completion

class StructCompletionTest : GdsCompletionTestBase() {

    fun `test struct keyword`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "struct")
    }

    fun `test after struct`() {
        myFixture.configureByText("test.gdshader", """
            struct <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "struct", "shader_type", "void", "uniform")
    }

    fun `test first member`() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec2", "vec3", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if")
    }

    fun `test member after precision`() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                highp <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec2", "vec3")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if", "highp", "mediump", "lowp")
    }

    fun `test member after type`() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                float <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if", "highp", "mediump", "lowp", "int", "float", "vec2", "vec3")
    }

    fun `test after member`() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                int a;
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec2", "vec3", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if")
    }

}

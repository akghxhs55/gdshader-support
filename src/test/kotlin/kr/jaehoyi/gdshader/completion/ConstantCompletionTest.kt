package kr.jaehoyi.gdshader.completion

class ConstantCompletionTest : GdsCompletionTestBase() {

    fun `test constant keyword in toplevel`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "const")
    }

    fun `test after const in toplevel`() {
        myFixture.configureByText("test.gdshader", """
            const <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }

    fun `test after precision in toplevel`() {
        myFixture.configureByText("test.gdshader", """
            const highp <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }

    fun `test after type in toplevel`() {
        myFixture.configureByText("test.gdshader", """
            const float <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }

    fun `test const keyword in function`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
	            <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "const")
    }

    fun `test after const in function`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                const <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }

    fun `test after precision in function`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                const highp <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }

    fun `test after type in function`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                const float <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }

}

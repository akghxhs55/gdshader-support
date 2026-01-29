package kr.jaehoyi.gdshader.completion

class FunctionDeclarationCompletionTest : GdsCompletionTestBase() {

    fun `test toplevel`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "void", "int", "float", "highp", "mediump")
    }

    fun `test return type after precision`() {
        myFixture.configureByText("test.gdshader", """
            highp <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "void", "int", "float")
        assertDoesntContain(completions, "highp", "mediump", "lowp", "shader_type", "uniform")
    }

    fun `test after return type`() {
        myFixture.configureByText("test.gdshader", """
            float <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "void", "int", "float", "highp", "mediump", "lowp", "shader_type", "uniform")
    }

    fun `test parameter header`() {
        myFixture.configureByText("test.gdshader", """
            void f(<caret>)
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "in", "out", "inout", "highp", "mediump", "lowp", "const")
        assertDoesntContain(completions, "shader_type", "uniform")
    }

    fun `test parameter header after const`() {
        myFixture.configureByText("test.gdshader", """
            void f(const <caret>)
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "in", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "out", "inout", "shader_type", "uniform", "const", "void")
    }

    fun `test parameter header after qualifier`() {
        myFixture.configureByText("test.gdshader", """
            void f(in <caret>)
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "in", "out", "inout", "shader_type", "uniform", "void")
    }

    fun `test parameter header after precision`() {
        myFixture.configureByText("test.gdshader", """
            void f(highp <caret>)
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float")
        assertDoesntContain(completions, "highp", "mediump", "lowp", "in", "out", "inout", "shader_type", "uniform", "void")
    }

    fun `test after parameter header`() {
        myFixture.configureByText("test.gdshader", """
            void f(float <caret>)
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "int", "float", "highp", "mediump", "lowp", "in", "out", "inout", "shader_type", "uniform", "const", "void")
    }

    fun `test multiple parameters`() {
        myFixture.configureByText("test.gdshader", """
            void f(float a, <caret>)
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "in", "out", "inout", "highp", "mediump", "lowp", "const")
        assertDoesntContain(completions, "shader_type", "uniform", "void")
    }

    fun `test no toplevel completion in function body`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "shader_type", "uniform", "varying", "struct")
    }

}

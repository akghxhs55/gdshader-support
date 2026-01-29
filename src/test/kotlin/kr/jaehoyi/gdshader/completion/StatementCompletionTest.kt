package kr.jaehoyi.gdshader.completion

class StatementCompletionTest : GdsCompletionTestBase() {

    fun `test in function body`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "if", "for", "while", "do", "switch")
        assertDoesntContain(completions, "else")
    }

    fun `test inside if condition`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (<caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "true", "false")
    }

    fun `test inside if body`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (true) {
                    <caret>
                }
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "if", "return")
    }

    fun `test else after if statement`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (true) {

                }
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "else")
    }

    fun `test if after else`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (true) {

                }
                else <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "if")
        assertDoesntContain(completions, "else")
    }

    fun `test inside for init`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (<caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "highp")
        assertDoesntContain(completions, "for", "if")
    }

    fun `test inside for condition`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (int i = 1; <caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "true", "false")
        assertDoesntContain(completions, "for", "if")
    }

    fun `test inside for iteration`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (int i = 1; i < 10; <caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "true", "false")
        assertDoesntContain(completions, "for", "if")
    }

    fun `test after statement`() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (;;) {
                }
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "if", "for", "return")
    }

}

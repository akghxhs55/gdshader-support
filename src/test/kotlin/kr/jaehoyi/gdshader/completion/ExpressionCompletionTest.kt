package kr.jaehoyi.gdshader.completion

class ExpressionCompletionTest : GdsCompletionTestBase() {

    fun `test in initializer`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            uniform bool t = <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in function body`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in function body after statement`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                ;
                <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in function body before statement`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                <caret>
                ;
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in function body between statements`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                ;
                <caret>
                test;
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in if statement condition`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                if (<caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test after if statement condition`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                if (true)
                    <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test if statement body`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                if (test) <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in for statement condition`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                for (; <caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in for statement iteration`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                for (;;<caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in switch statement expression`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                switch (<caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in switch body`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                switch (test) {
                    case 0:
                    <caret>
                }
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test after case keyword`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                switch (test) {
                    case <caret>
                }
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test after do keyword`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                do <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test after return keyword`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                return <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in function argument`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                test(<caret>)
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test after operator`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            uniform int t = 1 + <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test after primary`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                1 <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "radians")
    }

    fun `test uniform declaration initializer`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            uniform int test = <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test constant declaration initializer`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            const int test = <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test local variable declaration initializer`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                int test = <caret>
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in initializer list`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            const int t[1] = { <caret> }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in array size`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                test[<caret>]
            }
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

    fun `test in constant declaration array size`() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            const int test[<caret>]
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "radians")
    }

}

package kr.jaehoyi.gdshader.completion

class ConstantCompletionTest : GdsCompletionTestBase() {
    fun `test all global and included constants are completion candidates`() {
        myFixture.addFileToProject("constants.gdshaderinc", "const float INCLUDED_A = 1.0, INCLUDED_B = 2.0;")
        myFixture.addFileToProject(
            "main.gdshader",
            """
            shader_type spatial;
            #include "constants.gdshaderinc"
            const float LOCAL_A = 1.0, LOCAL_B = 2.0;
            void fragment() { ALBEDO = vec3(<caret>); }
            """.trimIndent(),
        )
        myFixture.configureFromTempProjectFile("main.gdshader")
        val completions = completeAndGetStrings()
        assertContainsElements(completions, "LOCAL_A", "LOCAL_B", "INCLUDED_A", "INCLUDED_B")
    }

    fun `test constant keyword in toplevel`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "const")
    }

    fun `test after const in toplevel`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            const <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }

    fun `test after precision in toplevel`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            const highp <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }

    fun `test after type in toplevel`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            const float <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }

    fun `test const keyword in function`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void f() {
                <caret>
            }
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "const")
    }

    fun `test after const in function`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void f() {
                const <caret>
            }
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }

    fun `test after precision in function`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void f() {
                const highp <caret>
            }
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }

    fun `test after type in function`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void f() {
                const float <caret>
            }
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }
}

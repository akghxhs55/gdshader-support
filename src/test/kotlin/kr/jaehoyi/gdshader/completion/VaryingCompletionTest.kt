package kr.jaehoyi.gdshader.completion

class VaryingCompletionTest : GdsCompletionTestBase() {

    fun `test varying keyword`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "varying")
    }

    fun `test after varying`() {
        myFixture.configureByText("test.gdshader", """
            varying <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec2", "flat", "smooth", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "varying", "shader_type", "uniform")
    }

    fun `test after interpolation modifier`() {
        myFixture.configureByText("test.gdshader", """
            varying flat <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec2", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "varying", "flat", "smooth", "shader_type", "uniform")
    }

    fun `test after precision modifier`() {
        myFixture.configureByText("test.gdshader", """
            varying highp <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "vec2")
        assertDoesntContain(completions, "varying", "flat", "smooth", "highp", "mediump", "lowp", "shader_type", "uniform")
    }

    fun `test after type`() {
        myFixture.configureByText("test.gdshader", """
            varying float <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "varying", "flat", "smooth", "highp", "mediump", "lowp", "int", "float", "vec2", "shader_type", "uniform")
    }

}

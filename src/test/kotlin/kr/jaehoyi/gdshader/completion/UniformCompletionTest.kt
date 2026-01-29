package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.model.Builtins

class UniformCompletionTest : GdsCompletionTestBase() {

    fun `test uniform keywords`() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "uniform", "global", "instance")
    }

    fun `test after modifier`() {
        myFixture.configureByText("test.gdshader", """
            global <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "uniform")
        assertDoesntContain(completions, "global", "instance", "shader_type", "void")
    }

    fun `test after uniform`() {
        myFixture.configureByText("test.gdshader", """
            uniform <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "bool", "highp")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "void")
    }

    fun `test after precision`() {
        myFixture.configureByText("test.gdshader", """
            uniform highp <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "bool")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "highp", "mediump", "lowp", "void")
    }

    fun `test after type`() {
        myFixture.configureByText("test.gdshader", """
            uniform vec4 <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test after variable name`() {
        myFixture.configureByText("test.gdshader", """
            uniform vec4 myVar <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test hint for vec3`() {
        myFixture.configureByText("test.gdshader", """
            uniform vec3 myVar : <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.VEC3])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test hint for vec4`() {
        myFixture.configureByText("test.gdshader", """
            uniform vec4 myVar : <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.VEC4])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test hint for int`() {
        myFixture.configureByText("test.gdshader", """
            uniform int myVar : <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.INT])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test hint for float`() {
        myFixture.configureByText("test.gdshader", """
            uniform float myVar : <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.FLOAT])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test hint for sampler2D`() {
        myFixture.configureByText("test.gdshader", """
            uniform sampler2D myVar : <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.SAMPLER2D])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test chained hint`() {
        myFixture.configureByText("test.gdshader", """
            uniform sampler2D myVar : source_color, <caret>
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "instance_index")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }

    fun `test inside range hint parameter`() {
        myFixture.configureByText("test.gdshader", """
            uniform float u_roundness : hint_range(0.0, <caret>);
        """.trimIndent())

        val completions = completeAndGetStrings()

        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp", "hint_range", "hint_enum", "hint_normal", "hint_default_white", "filter_nearest", "filter_linear", "instance_index")
    }

}

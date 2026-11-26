package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.model.Builtins

class UniformCompletionTest : GdsCompletionTestBase() {
    fun `test after modifier`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            global <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "uniform")
        assertDoesntContain(completions, "global", "instance", "shader_type", "void")
    }

    fun `test after uniform`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "bool", "highp")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "void")
    }

    fun `test after precision`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform highp <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "int", "float", "bool")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "highp", "mediump", "lowp", "void")
    }

    fun `test after type`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform vec4 <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test after variable name`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform vec4 myVar <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test hint for vec3`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform vec3 myVar : <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        val expectedHints = GdsKeywords.getUniformHints(Builtins.VEC3, isArray = false)
        assertContainsElements(completions, expectedHints)
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test hint for vec4`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform vec4 myVar : <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        val expectedHints = GdsKeywords.getUniformHints(Builtins.VEC4, isArray = false)
        assertContainsElements(completions, expectedHints)
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test hint for int`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform int myVar : <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        val expectedHints = GdsKeywords.getUniformHints(Builtins.INT, isArray = false)
        assertContainsElements(completions, expectedHints)
        assertDoesntContain(completions, "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test hint for float`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform float myVar : <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        val expectedHints = GdsKeywords.getUniformHints(Builtins.FLOAT, isArray = false)
        assertContainsElements(completions, expectedHints)
        assertDoesntContain(completions, "hint_enum", "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test hints follow godot completion for every sampler type`() {
        for (type in listOf("isampler2D", "usampler2D", "sampler3D", "samplerCube")) {
            myFixture.configureByText(
                "test.gdshader",
                """
                uniform $type myVar : <caret>
                """.trimIndent(),
            )

            val completions = completeAndGetStrings()

            assertContainsElements(completions, "filter_linear_mipmap_anisotropic", "filter_nearest_mipmap_anisotropic", "hint_default_black", "instance_index")
            assertDoesntContain(completions, "hint_linear_mipmap_anisotropic", "hint_nearest_mipmap_anisotropic", "hint_range")
        }
    }

    fun `test hints for uniform arrays`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform sampler2D myVar[2] : <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "filter_linear", "repeat_enable", "source_color")
        assertDoesntContain(completions, "hint_default_black", "instance_index")
    }

    fun `test hint for sampler2D`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform sampler2D myVar : <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        val expectedHints = GdsKeywords.getUniformHints(Builtins.SAMPLER2D, isArray = false)
        assertContainsElements(completions, expectedHints)
        assertDoesntContain(completions, "hint_enum", "hint_range")
        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test hint for 3D array and cube samplers`() {
        val samplers =
            mapOf(
                "sampler3D" to Builtins.SAMPLER3D,
                "isampler3D" to Builtins.ISAMPLER3D,
                "sampler2DArray" to Builtins.SAMPLER2DARRAY,
                "usampler2DArray" to Builtins.USAMPLER2DARRAY,
                "samplerCube" to Builtins.SAMPLERCUBE,
                "samplerCubeArray" to Builtins.SAMPLERCUBEARRAY,
                "samplerExternalOES" to Builtins.SAMPLEREXTERNALOES,
            )
        for ((typeName, type) in samplers) {
            myFixture.configureByText("test.gdshader", "uniform $typeName myVar : <caret>")

            val completions = completeAndGetStrings()

            val expectedHints = GdsKeywords.getUniformHints(type, isArray = false)
            assertContainsElements(completions, expectedHints)
            assertDoesntContain(completions, "hint_enum", "hint_range")
        }
    }

    fun `test chained hint`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform sampler2D myVar : source_color, <caret>
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "instance_index")
        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
        )
    }

    fun `test inside range hint parameter`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            uniform float u_roundness : hint_range(0.0, <caret>);
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertDoesntContain(
            completions,
            "uniform",
            "global",
            "instance",
            "shader_type",
            "int",
            "float",
            "bool",
            "void",
            "highp",
            "mediump",
            "lowp",
            "hint_range",
            "hint_enum",
            "hint_normal",
            "hint_default_white",
            "filter_nearest",
            "filter_linear",
            "instance_index",
        )
    }
}

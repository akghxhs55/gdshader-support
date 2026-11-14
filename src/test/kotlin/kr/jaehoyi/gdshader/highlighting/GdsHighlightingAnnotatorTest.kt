package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsHighlightingAnnotatorTest : BasePlatformTestCase() {
    fun `test half-typed identifier before assignment is not a struct type`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                void fragment() {
                    foo
                    COLOR = vec4(1.0);
                    COLOR.a = 0.5;
                }
                """,
            )

        assertEquals(emptyList<TextAttributesKey>(), colors.of("foo"))
        assertEquals(
            listOf(GdsSyntaxHighlighter.BUILTIN_VARIABLE, GdsSyntaxHighlighter.BUILTIN_VARIABLE),
            colors.of("COLOR"),
        )
    }

    fun `test half-typed identifier keeps following statements highlighted`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                void fragment() {
                    foo
                    float b = 1.0;
                    if (b > 0.5) {
                        b = 2.0;
                    }
                }
                """,
            )

        assertEquals(emptyList<TextAttributesKey>(), colors.of("foo"))
        assertEquals(List(3) { GdsSyntaxHighlighter.LOCAL_VARIABLE }, colors.of("b"))
    }

    fun `test half-typed type keeps following statements highlighted`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                void fragment() {
                    vec3
                    float b = 1.0;
                    b = 2.0;
                }
                """,
            )

        assertEquals(List(2) { GdsSyntaxHighlighter.LOCAL_VARIABLE }, colors.of("b"))
    }

    fun `test half-typed identifier in nested block keeps following function highlighted`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                void fragment() {
                    if (true) {
                        foo
                        float a = 1.0;
                    }
                }
                void vertex() {
                    float c = 1.0;
                }
                """,
            )

        assertEquals(listOf(GdsSyntaxHighlighter.FUNCTION), colors.of("vertex"))
        assertEquals(listOf(GdsSyntaxHighlighter.LOCAL_VARIABLE), colors.of("c"))
    }

    fun `test half-typed identifier at top level keeps following declarations highlighted`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                uniform float strength;
                foo
                void fragment() {
                    float b = strength;
                }
                """,
            )

        assertEquals(emptyList<TextAttributesKey>(), colors.of("foo"))
        assertEquals(listOf(GdsSyntaxHighlighter.FUNCTION), colors.of("fragment"))
        assertEquals(listOf(GdsSyntaxHighlighter.LOCAL_VARIABLE), colors.of("b"))
        assertEquals(List(2) { GdsSyntaxHighlighter.UNIFORM_VARIABLE }, colors.of("strength"))
    }

    fun `test half-typed function at top level keeps following declarations highlighted`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                void helper
                void fragment() {
                    float b = 1.0;
                }
                """,
            )

        assertEquals(listOf(GdsSyntaxHighlighter.FUNCTION), colors.of("fragment"))
        assertEquals(listOf(GdsSyntaxHighlighter.LOCAL_VARIABLE), colors.of("b"))
    }

    // === Structs ===

    fun `test unknown type name is not highlighted as struct`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                void fragment() {
                    Unknown value;
                }
                """,
            )

        assertEquals(emptyList<TextAttributesKey>(), colors.of("Unknown"))
        assertEquals(listOf(GdsSyntaxHighlighter.LOCAL_VARIABLE), colors.of("value"))
    }

    fun `test struct type and constructor are highlighted as struct`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                struct Light {
                    float energy;
                };
                void fragment() {
                    Light light = Light(1.0);
                }
                """,
            )

        assertEquals(List(3) { GdsSyntaxHighlighter.STRUCT }, colors.of("Light"))
        assertEquals(listOf(GdsSyntaxHighlighter.LOCAL_VARIABLE), colors.of("light"))
    }

    // === Functions ===

    fun `test function declaration and calls use separate colors`() {
        val colors =
            semanticColors(
                """
                shader_type canvas_item;
                float helper(float value) {
                    return abs(value);
                }
                void fragment() {
                    float b = helper(1.0);
                }
                """,
            )

        assertEquals(listOf(GdsSyntaxHighlighter.FUNCTION, GdsSyntaxHighlighter.FUNCTION_CALL), colors.of("helper"))
        assertEquals(listOf(GdsSyntaxHighlighter.FUNCTION_CALL), colors.of("abs"))
        assertEquals(listOf(GdsSyntaxHighlighter.FUNCTION), colors.of("fragment"))
    }

    private class SemanticColors(
        private val entries: List<Pair<String, TextAttributesKey>>,
    ) {
        fun of(text: String): List<TextAttributesKey> = entries.filter { it.first == text }.map { it.second }
    }

    private fun semanticColors(code: String): SemanticColors {
        myFixture.configureByText("test.gdshader", code.trimIndent())
        val entries =
            myFixture
                .doHighlighting()
                .filter { it.severity == HighlightSeverity.INFORMATION && it.forcedTextAttributesKey != null }
                .sortedBy { it.startOffset }
                .map { it.text to it.forcedTextAttributesKey }
        return SemanticColors(entries)
    }
}

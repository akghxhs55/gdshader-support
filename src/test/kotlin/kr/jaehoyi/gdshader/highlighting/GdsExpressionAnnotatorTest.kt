package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsExpressionAnnotatorTest : BasePlatformTestCase() {

    // === Arithmetic operators ===

    fun `test valid vec3 plus vec3`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                vec3 b = vec3(2.0);
                vec3 c = a + b;
            }
        """.trimIndent())
    }

    fun `test valid float times vec3`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float s = 2.0;
                vec3 v = vec3(1.0);
                vec3 r = s * v;
            }
        """.trimIndent())
    }

    fun `test valid mat4 times vec4`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                mat4 m = mat4(1.0);
                vec4 v = vec4(1.0);
                vec4 r = m * v;
            }
        """.trimIndent())
    }

    fun `test invalid vec2 plus vec3`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec2 a = vec2(1.0);
                vec3 b = vec3(2.0);
                vec3 c = <error descr="Invalid arguments to operator '+': 'vec2, vec3'">a + b</error>;
            }
        """.trimIndent())
    }

    fun `test invalid float plus bool`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                bool b = true;
                float c = <error descr="Invalid arguments to operator '+': 'float, bool'">a + b</error>;
            }
        """.trimIndent())
    }

    fun `test invalid int times float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 1;
                float b = 2.0;
                float c = <error descr="Invalid arguments to operator '*': 'int, float'">a * b</error>;
            }
        """.trimIndent())
    }

    // === Shift operators ===

    fun `test valid int shift`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 1;
                int b = a << 2;
            }
        """.trimIndent())
    }

    fun `test invalid float shift`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                int b = <error descr="Invalid arguments to operator '<<': 'float, int'">a << 2</error>;
            }
        """.trimIndent())
    }

    // === Bitwise operators ===

    fun `test valid int bitwise and`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 5;
                int b = 3;
                int c = a & b;
            }
        """.trimIndent())
    }

    fun `test invalid float bitwise or`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                float c = <error descr="Invalid arguments to operator '|': 'float, float'">a | b</error>;
            }
        """.trimIndent())
    }

    // === Logical operators ===

    fun `test valid bool logic and`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = false;
                bool c = a && b;
            }
        """.trimIndent())
    }

    fun `test invalid int logic and`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 1;
                int b = 0;
                bool c = <error descr="Invalid arguments to operator '&&': 'int, int'">a && b</error>;
            }
        """.trimIndent())
    }

    fun `test invalid float logic or`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 0.0;
                bool c = <error descr="Invalid arguments to operator '||': 'float, float'">a || b</error>;
            }
        """.trimIndent())
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

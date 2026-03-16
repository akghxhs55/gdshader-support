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
                float b = <error descr="Invalid arguments to operator '<<': 'float, int'">a << 2</error>;
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

    // === Simple assignment ===

    fun `test valid assignment`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                x = 2.0;
            }
        """.trimIndent())
    }

    fun `test invalid assignment int to float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                <error descr="Cannot assign a value of type 'int' to type 'float'">x = 1</error>;
            }
        """.trimIndent())
    }

    fun `test invalid assignment vec2 to vec3`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                <error descr="Cannot assign a value of type 'vec2' to type 'vec3'">v = vec2(1.0)</error>;
            }
        """.trimIndent())
    }

    // === Compound assignment ===

    fun `test valid compound assignment vec3 plus float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                v += 1.0;
            }
        """.trimIndent())
    }

    fun `test invalid compound assignment float plus bool`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                <error descr="Invalid arguments to operator '+=': 'float, bool'">x += true</error>;
            }
        """.trimIndent())
    }

    // === Relational operators ===

    fun `test valid float less than`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                bool c = a < b;
            }
        """.trimIndent())
    }

    fun `test valid int greater equal`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 1;
                int b = 2;
                bool c = a >= b;
            }
        """.trimIndent())
    }

    fun `test invalid int less than float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 1;
                float b = 2.0;
                bool c = <error descr="Invalid arguments to operator '<': 'int, float'">a < b</error>;
            }
        """.trimIndent())
    }

    fun `test invalid vec3 less than`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                vec3 b = vec3(2.0);
                bool c = <error descr="Invalid arguments to operator '<': 'vec3, vec3'">a < b</error>;
            }
        """.trimIndent())
    }

    // === Equality operators ===

    fun `test valid vec3 equality`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                vec3 b = vec3(2.0);
                bool c = a == b;
            }
        """.trimIndent())
    }

    fun `test valid bool not equal`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = false;
                bool c = a != b;
            }
        """.trimIndent())
    }

    fun `test invalid vec2 equal vec3`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec2 a = vec2(1.0);
                vec3 b = vec3(2.0);
                bool c = <error descr="Invalid arguments to operator '==': 'vec2, vec3'">a == b</error>;
            }
        """.trimIndent())
    }

    fun `test invalid int equal float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 1;
                float b = 1.0;
                bool c = <error descr="Invalid arguments to operator '==': 'int, float'">a == b</error>;
            }
        """.trimIndent())
    }

    // === Unary operators ===

    fun `test valid negation float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = -a;
            }
        """.trimIndent())
    }

    fun `test valid negation vec3`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                vec3 w = -v;
            }
        """.trimIndent())
    }

    fun `test valid logical not`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = !a;
            }
        """.trimIndent())
    }

    fun `test valid bitwise invert`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 5;
                int b = ~a;
            }
        """.trimIndent())
    }

    fun `test invalid logical not on int`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int a = 1;
                bool b = <error descr="Invalid arguments to unary operator '!': int">!a</error>;
            }
        """.trimIndent())
    }

    fun `test invalid bitwise invert on float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = <error descr="Invalid arguments to unary operator '~': float">~a</error>;
            }
        """.trimIndent())
    }

    fun `test invalid negation on bool`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = <error descr="Invalid arguments to unary operator '-': bool">-a</error>;
            }
        """.trimIndent())
    }

    // === Ternary operator ===

    fun `test valid ternary`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                bool c = true;
                float a = 1.0;
                float b = 2.0;
                float r = c ? a : b;
            }
        """.trimIndent())
    }

    fun `test invalid ternary condition`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int c = 1;
                float a = 1.0;
                float b = 2.0;
                float r = <error descr="Invalid argument to ternary operator: 'int, float, float'">c ? a : b</error>;
            }
        """.trimIndent())
    }

    fun `test invalid ternary type mismatch`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                bool c = true;
                float a = 1.0;
                int b = 1;
                <error descr="Invalid argument to ternary operator: 'bool, float, int'">c ? a : b</error>;
            }
        """.trimIndent())
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsExpressionAnnotatorTest : BasePlatformTestCase() {
    fun `test invalid vec2 plus vec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 a = vec2(1.0);
                vec3 b = vec3(2.0);
                vec3 c = <error descr="Invalid arguments to operator '+': 'vec2, vec3'">a + b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid float plus bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                bool b = true;
                float c = <error descr="Invalid arguments to operator '+': 'float, bool'">a + b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid arithmetic chain with macro operand`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define TINT vec3(1.0)
            void fragment() {
                vec3 base = vec3(1.0);
                ALBEDO = base * 0.5 * TINT;
            }
            """.trimIndent(),
        )
    }

    fun `test arithmetic chain with unknown operand type`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define UNKNOWN undefined_name
            void fragment() {
                vec3 a = vec3(1.0);
                vec3 c = a * 0.5 * UNKNOWN;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid third operand in arithmetic chain`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                vec2 b = vec2(1.0);
                vec3 c = <error descr="Invalid arguments to operator '*': 'vec3, vec2'">a * 0.5 * b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid operator reported at failing position`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                vec2 b = vec2(1.0);
                vec3 c = <error descr="Invalid arguments to operator '-': 'vec3, vec2'">a + a - b</error>;
                vec3 d = <error descr="Invalid arguments to operator '/': 'vec3, vec2'">a * 2.0 / b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid int times float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                float b = 2.0;
                float c = <error descr="Invalid arguments to operator '*': 'int, float'">a * b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid int shift`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                int b = a << 2;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid float shift`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = <error descr="Invalid arguments to operator '<<': 'float, int'">a << 2</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid int bitwise and`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 5;
                int b = 3;
                int c = a & b;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid float bitwise or`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                float c = <error descr="Invalid arguments to operator '|': 'float, float'">a | b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid bool logic and`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = false;
                bool c = a && b;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid int logic and`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                int b = 0;
                bool c = <error descr="Invalid arguments to operator '&&': 'int, int'">a && b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid float logic or`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 0.0;
                bool c = <error descr="Invalid arguments to operator '||': 'float, float'">a || b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid assignment`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                x = 2.0;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid assignment int to float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                <error descr="Cannot assign a value of type 'int' to type 'float'">x = 1</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid assignment vec2 to vec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                <error descr="Cannot assign a value of type 'vec2' to type 'vec3'">v = vec2(1.0)</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid type in chained assignment`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 0.0;
                float b = 1.0;
                int c = 2;
                <error descr="Cannot assign a value of type 'int' to type 'float'">a = b = c</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid compound assignment vec3 plus float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                v += 1.0;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid compound assignment float plus bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                <error descr="Invalid arguments to operator '+=': 'float, bool'">x += true</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test chained compound assignment uses accumulated type`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(0.0);
                float b = 1.0;
                vec3 c = vec3(2.0);
                a += b *= c;
            }
            """.trimIndent(),
        )
    }

    fun `test valid float less than`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                bool c = a < b;
            }
            """.trimIndent(),
        )
    }

    fun `test valid int greater equal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                int b = 2;
                bool c = a >= b;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid int less than float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                float b = 2.0;
                bool c = <error descr="Invalid arguments to operator '<': 'int, float'">a < b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid vec3 less than`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                vec3 b = vec3(2.0);
                bool c = <error descr="Invalid arguments to operator '<': 'vec3, vec3'">a < b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid vec3 equality`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                vec3 b = vec3(2.0);
                bool c = a == b;
            }
            """.trimIndent(),
        )
    }

    fun `test valid bool not equal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = false;
                bool c = a != b;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid vec2 equal vec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 a = vec2(1.0);
                vec3 b = vec3(2.0);
                bool c = <error descr="Invalid arguments to operator '==': 'vec2, vec3'">a == b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid int equal float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                float b = 1.0;
                bool c = <error descr="Invalid arguments to operator '==': 'int, float'">a == b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test array equality`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a[2];
                float b[2];
                bool c = <error descr="Invalid arguments to operator '==': 'float[2], float[2]'">a == b</error>;
                bool d = <error descr="Invalid arguments to operator '!=': 'float[2], float[2]'">a != b</error>;
                bool e = a[0] == b[1];
            }
            """.trimIndent(),
        )
    }

    fun `test vector and scalar bitwise operators`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uvec2 a = <error descr="Invalid arguments to operator '&': 'uvec2, uint'">uvec2(1u) & 1u</error>;
                uvec2 b = <error descr="Invalid arguments to operator '^': 'uint, uvec2'">1u ^ uvec2(1u)</error>;
                ivec2 c = <error descr="Invalid arguments to operator '|': 'ivec2, int'">ivec2(1) | 1</error>;
                ivec2 d = ivec2(1) & ivec2(2);
                uvec2 e = uvec2(1u) % 1u;
                uvec2 f = uvec2(1u) << 1u;
                d |= 1;
                e ^= 1u;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid third operand in shift chain`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                float f = 1.0;
                int b = <error descr="Invalid arguments to operator '>>': 'int, float'">a << 1 >> f</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid third operand in bitwise chain`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                int b = 2;
                float f = 1.0;
                int c = <error descr="Invalid arguments to operator '&': 'int, float'">a & b & f</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid third operand in logic chain`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = false;
                int i = 1;
                bool c = <error descr="Invalid arguments to operator '&&': 'bool, int'">a && b && i</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test logic chain with unknown operand type`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define UNKNOWN undefined_name
            void fragment() {
                bool a = true;
                bool b = false;
                bool c = a && b && UNKNOWN;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid chained relational`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                float c = 3.0;
                bool r = <error descr="Invalid arguments to operator '<': 'bool, float'">a < b < c</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid chained equality with bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                bool c = true;
                bool r = a == b == c;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid chained equality`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                float c = 3.0;
                bool r = <error descr="Invalid arguments to operator '!=': 'bool, float'">a == b != c</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid negation float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = -a;
            }
            """.trimIndent(),
        )
    }

    fun `test valid negation vec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                vec3 w = -v;
            }
            """.trimIndent(),
        )
    }

    fun `test valid logical not`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = !a;
            }
            """.trimIndent(),
        )
    }

    fun `test valid bitwise invert`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 5;
                int b = ~a;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid logical not on int`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 1;
                bool b = <error descr="Invalid arguments to unary operator '!': int">!a</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid bitwise invert on float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = <error descr="Invalid arguments to unary operator '~': float">~a</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid negation on bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = <error descr="Invalid arguments to unary operator '-': bool">-a</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid ternary`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool c = true;
                float a = 1.0;
                float b = 2.0;
                float r = c ? a : b;
            }
            """.trimIndent(),
        )
    }

    fun `test valid ternary with comparison condition`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            uniform int test_mode;
            void fragment() {
                vec3 color = vec3(0.0);
                if (test_mode == 0 || test_mode == 4) {
                    color = test_mode == 4 ? vec3(0.95, 0.36, 0.24) : vec3(0.24, 0.72, 0.98);
                }
            }
            """.trimIndent(),
        )
    }

    fun `test valid ternary with include function parameter`() {
        myFixture.addFileToProject(
            "pattern.gdshaderinc",
            """
            shader_type canvas_item;
            struct Result { float mask; };
            Result choose(bool draw_regions, float region_mask, float outline_mask) {
                Result result;
                result.mask = draw_regions ? region_mask : outline_mask;
                return result;
            }
            """.trimIndent(),
        )
        myFixture.configureFromTempProjectFile("pattern.gdshaderinc")
        myFixture.checkHighlighting(false, false, true)
    }

    fun `test invalid ternary condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int c = 1;
                float a = 1.0;
                float b = 2.0;
                float r = <error descr="Invalid argument to ternary operator: 'int, float, float'">c ? a : b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid ternary type mismatch`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool c = true;
                float a = 1.0;
                int b = 1;
                <error descr="Invalid argument to ternary operator: 'bool, float, int'">c ? a : b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to const variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                const float x = 1.0;
                <error descr="Constants cannot be modified">x = 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test compound assign to const variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                const int x = 1;
                <error descr="Constants cannot be modified">x += 2</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to uniform variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float speed;
            void fragment() {
                <error descr="Assignment to uniform">speed = 1.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to read-only builtin`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            void fragment() {
                <error descr="Cannot assign to read-only built-in 'TIME'">TIME = 1.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to builtin constant`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            void fragment() {
                <error descr="Constants cannot be modified">PI += 1.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to writable builtin`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            void fragment() {
                COLOR = vec4(1.0);
            }
            """.trimIndent(),
        )
    }

    fun `test assign to in function parameter`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            float f(in float x) {
                x = 2.0;
                return x;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to mutable variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                x = 2.0;
            }
            """.trimIndent(),
        )
    }

    fun `test valid int array index`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float arr[3] = { 1.0, 2.0, 3.0 };
                float x = arr[0];
            }
            """.trimIndent(),
        )
    }

    fun `test valid int variable array index`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float arr[3] = { 1.0, 2.0, 3.0 };
                int i = 1;
                float x = arr[i];
            }
            """.trimIndent(),
        )
    }

    fun `test invalid float array index`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float arr[3] = { 1.0, 2.0, 3.0 };
                float x = arr[<error descr="Only integer expressions are allowed for indexing">1.0</error>];
            }
            """.trimIndent(),
        )
    }

    fun `test valid vector int index`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                float x = v[0];
            }
            """.trimIndent(),
        )
    }

    fun `test invalid vector with float index`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                float x = v[<error descr="Only integer expressions are allowed for indexing">0.0</error>];
            }
            """.trimIndent(),
        )
    }

    fun `test invalid bool literal plus bool literal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool c = <error descr="Invalid arguments to operator '+': 'bool, bool'">true + false</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid mat3 plus mat3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat3 a = mat3(1.0);
                mat3 b = mat3(2.0);
                mat3 c = a + b;
            }
            """.trimIndent(),
        )
    }

    fun `test valid float times mat2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float s = 2.0;
                mat2 m = mat2(1.0);
                mat2 a = s * m;
                mat2 b = m - s;
            }
            """.trimIndent(),
        )
    }

    fun `test valid vec4 times mat4`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                mat4 m = mat4(1.0);
                vec4 r = v * m;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid vec3 divided by mat3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                mat3 m = mat3(1.0);
                vec3 r = <error descr="Invalid arguments to operator '/': 'vec3, mat3'">v / m</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid uint modulo uint`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uint a = 5u;
                uint b = 2u;
                uint c = a % b;
            }
            """.trimIndent(),
        )
    }

    fun `test valid uvec3 modulo uvec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uvec3 a = uvec3(5u);
                uvec3 b = uvec3(2u);
                uvec3 c = a % b;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid float literal modulo float literal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float c = <error descr="Invalid arguments to operator '%': 'float, float'">1.0 % 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid vec3 modulo float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 a = vec3(1.0);
                float b = 2.0;
                vec3 c = <error descr="Invalid arguments to operator '%': 'vec3, float'">a % b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid mat2 modulo mat2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat2 a = mat2(1.0);
                mat2 b = mat2(2.0);
                mat2 c = <error descr="Invalid arguments to operator '%': 'mat2, mat2'">a % b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid modulo in multiplicative chain`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = 2.0;
                float c = <error descr="Invalid arguments to operator '%': 'float, float'">a * b % a</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid compound modulo assignment float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                <error descr="Invalid arguments to operator '%=': 'float, float'">a %= 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid compound modulo assignment ivec2 int`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                ivec2 a = ivec2(5);
                a %= 2;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid compound add assignment bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                <error descr="Invalid arguments to operator '+=': 'bool, bool'">a += false</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid uint less than`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uint a = 1u;
                uint b = 2u;
                bool c = a < b;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid bool less than bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = false;
                bool c = <error descr="Invalid arguments to operator '<': 'bool, bool'">a < b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid bool literal less than bool literal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool c = <error descr="Invalid arguments to operator '<': 'bool, bool'">true < false</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid bool greater equal bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool a = true;
                bool b = false;
                bool c = <error descr="Invalid arguments to operator '>=': 'bool, bool'">a >= b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid bvec2 equality`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bvec2 a = bvec2(true);
                bvec2 b = bvec2(false);
                bool c = a == b;
            }
            """.trimIndent(),
        )
    }

    fun `test valid mat2 not equal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat2 a = mat2(1.0);
                mat2 b = mat2(2.0);
                bool c = a != b;
            }
            """.trimIndent(),
        )
    }

    fun `test valid negation uvec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uvec2 a = uvec2(1u);
                uvec2 b = -a;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid negation on mat4`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat4 a = mat4(1.0);
                mat4 b = <error descr="Invalid arguments to unary operator '-': mat4">-a</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid negation on mat2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat2 a = mat2(1.0);
                mat2 b = <error descr="Invalid arguments to unary operator '-': mat2">-a</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid uvec3 shift uvec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uvec3 a = uvec3(8u);
                uvec3 b = uvec3(1u);
                uvec3 c = a >> b;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid uvec2 shift int`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uvec2 a = uvec2(1u);
                int b = 2;
                uvec2 c = <error descr="Invalid arguments to operator '<<': 'uvec2, int'">a << b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid uint bitwise and ivec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uint a = 1u;
                ivec2 b = ivec2(2);
                ivec2 c = <error descr="Invalid arguments to operator '&': 'uint, ivec2'">a & b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid compound bitwise and assignment ivec2 int`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                ivec2 a = ivec2(5);
                a &= 3;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid compound bitwise and assignment int ivec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 5;
                <error descr="Invalid arguments to operator '&=': 'int, ivec2'">a &= ivec2(3)</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid compound shift assignment int uint`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = 5;
                <error descr="Invalid arguments to operator '<<=': 'int, uint'">a <<= 1u</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid compound xor assignment ivec3 ivec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                ivec3 a = ivec3(5);
                <error descr="Invalid arguments to operator '^=': 'ivec3, ivec2'">a ^= ivec2(1)</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test valid postfix increment int`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int i = 0;
                i++;
            }
            """.trimIndent(),
        )
    }

    fun `test valid prefix decrement float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float f = 1.0;
                --f;
            }
            """.trimIndent(),
        )
    }

    fun `test valid postfix decrement vec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                v--;
            }
            """.trimIndent(),
        )
    }

    fun `test valid prefix increment uvec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uvec2 u = uvec2(1u);
                ++u;
            }
            """.trimIndent(),
        )
    }

    fun `test valid increment in for loop`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                for (int i = 0; i < 10; ++i) {
                }
            }
            """.trimIndent(),
        )
    }

    fun `test invalid postfix increment on bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool b = true;
                <error descr="Invalid arguments to unary operator '++': bool">b++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid prefix decrement on bool`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool b = true;
                <error descr="Invalid arguments to unary operator '--': bool">--b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid prefix increment on bvec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bvec2 b = bvec2(true);
                <error descr="Invalid arguments to unary operator '++': bvec2">++b</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid postfix increment on mat4`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat4 m = mat4(1.0);
                <error descr="Invalid arguments to unary operator '++': mat4">m++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid prefix decrement on mat3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat3 m = mat3(1.0);
                <error descr="Invalid arguments to unary operator '--': mat3">--m</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid postfix decrement on bool in expression`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bool b = true;
                bool c = <error descr="Invalid arguments to unary operator '--': bool">b--</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on local const`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                const int c = 1;
                <error descr="Constants cannot be modified">c++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test prefix decrement on global const`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float C = 1.0;
            void fragment() {
                <error descr="Constants cannot be modified">--C</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on const array element`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                const int a[2] = {1, 2};
                <error descr="Constants cannot be modified">a[0]++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on uniform`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float speed;
            void fragment() {
                <error descr="Assignment to uniform">speed++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test prefix increment on uniform swizzle`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform vec2 offset;
            void fragment() {
                <error descr="Assignment to uniform">++offset.x</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test decrement on read-only builtin`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            void fragment() {
                <error descr="Cannot assign to read-only built-in 'TIME'">TIME--</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test prefix increment on builtin constant`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            void fragment() {
                <error descr="Constants cannot be modified">++PI</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on literal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a = <error descr="Assignment to constant expression">1++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test prefix decrement on literal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = <error descr="Assignment to constant expression">--1.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on function call`() {
        doHighlightTest(
            """
            shader_type spatial;
            int f() {
                return 1;
            }
            void fragment() {
                int a = <error descr="Assignment to function">f()++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on swizzle with duplicates`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(1.0);
                <error descr="Swizzling assignment contains duplicates">v.xx++</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on writable builtin component`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            void fragment() {
                COLOR.r++;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on local array element`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int a[2] = {1, 2};
                a[0]++;
                --a[1];
            }
            """.trimIndent(),
        )
    }

    fun `test increment on function parameter`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            float f(in float x, inout int y) {
                x++;
                --y;
                return x;
            }
            """.trimIndent(),
        )
    }

    fun `test increment on vector component`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                v.x++;
                --v.yz;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to function call`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f() {
                return 1.0;
            }
            void fragment() {
                <error descr="Assignment to function">f() = 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test compound assign to function call`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f() {
                return 1.0;
            }
            void fragment() {
                <error descr="Assignment to function">f() += 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test chained assign to function call with const argument`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float C = 1.0;
            float f(float x) {
                return x;
            }
            void fragment() {
                float x;
                <error descr="Assignment to function">x = f(C) = 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to swizzle with duplicates`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                <error descr="Swizzling assignment contains duplicates">v.xx = vec2(2.0)</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test compound assign to swizzle with duplicates`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                <error descr="Swizzling assignment contains duplicates">v.xyx *= 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to swizzle without duplicates`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                v.xz = vec2(2.0);
                v.wzyx += vec4(1.0);
            }
            """.trimIndent(),
        )
    }

    fun `test assign to literal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                <error descr="Assignment to constant expression">1 = 2</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test assign to builtin define`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                <error descr="Assignment to constant expression">CURRENT_RENDERER = 1</error>;
                float f = <error descr="Cannot assign a value of type 'int' to type 'float'">RENDERER_MOBILE</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test compound assign to literal`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                <error descr="Assignment to constant expression">1.0 -= 2.0</error>;
            }
            """.trimIndent(),
        )
    }

    // === initializer list in expressions ===

    fun `test initializer list in expressions`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            struct Wrap { MyData arr[2]; };
            void f(float a[2]) {}
            void fragment() {
                float a[2];
                a = { 1.0, 2.0 };
                a = ({ 1.0, 2.0 });
                a = true ? { 1.0, 2.0 } : { 3.0, 4.0 };
                MyData items[2];
                items = { MyData(1.0), MyData(2.0) };
                Wrap w;
                w.arr = { MyData(1.0), MyData(2.0) };
                float x = { 1.0, 2.0 }[0];
                int n = { 1.0, 2.0 }.length();
                f({ 1.0, 2.0 });
                a = float[2]({ 1.0, 2.0 }[0], 2.0);
            }
            """.trimIndent(),
        )
    }

    fun `test initializer list with mixed element types`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = { 1.0, <error descr="Invalid assignment of 'int' to 'float'.">2</error> }[0];
            }
            """.trimIndent(),
        )
    }

    fun `test initializer list assigned with wrong type`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a[2];
                <error descr="Cannot assign a value of type 'float[3]' to type 'float[2]'">a = { 1.0, 2.0, 3.0 }</error>;
                <error descr="Cannot assign a value of type 'int[2]' to type 'float[2]'">a = { 1, 2 }</error>;
            }
            """.trimIndent(),
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

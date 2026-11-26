package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsConstantAnnotatorTest : BasePlatformTestCase() {
    fun `test numeric literals`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = 1.0f + .5f + 1e2f + 1.f;
                float b = <error descr="Invalid (float) numeric constant">1f</error>;
                int c = 0xFFFFFFFF;
                uint d = 0x000000FFu;
                int e = <error descr="Invalid (hexadecimal) numeric constant">0x1FFFFFFFF</error>;
                uint f = <error descr="Invalid (hexadecimal) numeric constant">0x00000000FFu</error>;
            }
            """,
        )
    }

    fun `test string escapes`() {
        myFixture.configureByText(
            "test_shader.gdshader",
            "shader_type spatial;\nuniform int a : hint_enum(\"a\\\"b\", \"c\\\\d\");\nuniform int b : hint_enum(\"e\\nf\");\n",
        )
        val errors = myFixture.doHighlighting().filter { it.severity == HighlightSeverity.ERROR }
        assertEquals(listOf("\"e\\nf\"" to "Only \\\" and \\\\ escape characters supported."), errors.map { it.text to it.description })
    }

    fun `test constant initializers`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float u;
            const vec2 V = vec2(1.0);
            const float A[2] = {1.0, 2.0};
            const float G = <error descr="Expected constant expression.">u</error>;
            float f(float x) { return x; }
            void fragment() {
                float y = 1.0;
                int i = 1;
                const float a = TIME + sin(1.0) + V.x + A[1] + (true ? 1.0 : 2.0);
                const vec3 b = VERTEX;
                const float c[2] = {1.0, UV.x};
                const float d = 1.0 + <error descr="Expected constant expression.">y</error>;
                const float e = <error descr="Expected constant expression.">f</error>(1.0);
                const float g = A[<error descr="Expected constant expression.">i</error>];
            }
            """,
        )
    }

    fun `test array sizes`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int N = 2;
            void fragment() {
                int n = 2;
                float a[N + 1];
                float b[<error descr="Expected a positive integer constant.">n</error>];
            }
            """,
        )
    }

    fun `test constant index range`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int I = 5;
            void fragment() {
                float a[2];
                vec2 v;
                mat3 m;
                float ok = a[1] + v[1] + m[2].x + a[I];
                float x = a[<error descr="Index [2] out of range [0..1].">2</error>];
                float y = v[<error descr="Index [2] out of range [0..1].">2</error>];
                vec3 z = m[<error descr="Index [3] out of range [0..2].">3</error>];
                float w = a[<error descr="Index [-1] out of range [0..1].">-1</error>];
            }
            """,
        )
    }

    fun `test array initializer elements`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float A[2] = {<error descr="Cannot assign a value of type 'int' to type 'float'">1</error>, 2.0};
            void fragment() {
                float a[2] = {<error descr="Cannot assign a value of type 'int' to type 'float'">1</error>, 2.0};
                float b[2] = {1.0, 2.0};
            }
            """,
        )
    }

    fun `test array initializer size`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a[3] = <error descr="Array size mismatch. Expected 3 elements (found 2).">{1.0, 2.0}</error>;
                float b[3] = <error descr="Array size mismatch. Expected 3 elements (found 2).">float[](1.0, 2.0)</error>;
                float c[2] = float[](1.0, 2.0);
            }
            """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code.trimIndent())
        myFixture.checkHighlighting(false, false, true)
    }
}

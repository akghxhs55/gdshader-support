package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsFunctionContextAnnotatorTest : BasePlatformTestCase() {
    fun `test discard`() {
        doHighlightTest(
            """
            shader_type particles;
            void helper() { <error descr="Use of 'discard' is not supported for the 'particles' shader type.">discard;</error> }
            void process() {}
            """,
        )
        doHighlightTest(
            """
            shader_type spatial;
            void inner() { <error descr="'discard' cannot be used here, because 'inner' is called by the 'vertex' processor function (which is not allowed).">discard;</error> }
            void outer() { inner(); }
            void unused() { discard; }
            void vertex() { outer(); }
            void fragment() { unused(); discard; }
            """,
        )
    }

    fun `test fragment-only functions`() {
        doHighlightTest(
            """
            shader_type spatial;
            float deriv(float x) { return <error descr="'dFdx' cannot be used here, because 'deriv' is called by the 'vertex' processor function (which is not allowed).">dFdx</error>(x); }
            void vertex() {
                float a = deriv(1.0);
                float b = <error descr="'fwidth' cannot be used within the 'vertex' processor function.">fwidth</error>(1.0);
            }
            void fragment() { float c = dFdx(UV.x) + deriv(1.0); }
            """,
        )
        doHighlightTest(
            """
            shader_type particles;
            void process() { float d = <error descr="Built-in function 'dFdx' is not supported for the 'particles' shader type.">dFdx</error>(1.0); }
            """,
        )
        doHighlightTest(
            """
            shader_type sky;
            void sky() { float d = dFdx(SKY_COORDS.x); }
            """,
        )
    }

    fun `test textureGather component`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform sampler2D tex;
            const int C = 2;
            void fragment() {
                int n = 1;
                vec4 a = textureGather(tex, UV, C);
                vec4 b = textureGather(tex, UV);
                vec4 c = textureGather(tex, UV, <error descr="Expected integer constant within [0..3] range.">4</error>);
                vec4 d = textureGather(tex, UV, <error descr="Expected integer constant within [0..3] range.">n</error>);
            }
            """,
        )
    }

    fun `test varying declarations`() {
        doHighlightTest(
            """
            shader_type particles;
            <error descr="Varyings cannot be used in 'particles' shaders.">varying float v;</error>
            void process() {}
            """,
        )
        doHighlightTest(
            """
            shader_type spatial;
            varying <error descr="Invalid data type for varying.">sampler2D</error> s;
            varying <error descr="Interpolation modifier 'flat' cannot be used with boolean types.">flat</error> bool b;
            varying int <error descr="Varying with integer data type must be declared with `flat` interpolation qualifier.">i</error>;
            varying int j;
            varying flat int k;
            void vertex() { i = 1; k = 1; }
            void fragment() { j = 1; }
            """,
        )
    }

    fun `test varying assignments`() {
        doHighlightTest(
            """
            shader_type spatial;
            varying float v;
            varying vec2 w;
            varying bool b;
            void helper() { <error descr="Varying may not be assigned in the 'helper' function.">v</error> = 1.0; }
            void vertex() { v = 1.0; <error descr="Varying with 'bool' data type may only be assigned in the 'fragment' function.">b</error> = true; }
            void light() { <error descr="Varying may not be assigned in the 'light' function.">w</error>.x = 1.0; float r = v; }
            """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code.trimIndent())
        myFixture.checkHighlighting(false, false, true)
    }
}

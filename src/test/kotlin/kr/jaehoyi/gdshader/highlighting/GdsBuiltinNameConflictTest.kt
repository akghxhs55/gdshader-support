package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsBuiltinNameConflictTest : BasePlatformTestCase() {
    fun `test built-in names cannot be redefined`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float <error descr="Redefinition of 'ALBEDO'">ALBEDO</error>;
            uniform float sin;
            const float <error descr="Redefinition of 'NORMAL_MAP'">NORMAL_MAP</error> = 1.0;
            const float cos = 1.0;
            varying float <error descr="Redefinition of 'LIGHT'">LIGHT</error>;
            float <error descr="Redefinition of 'texture'">texture</error>(float x) { return x; }
            float <error descr="Redefinition of 'dFdx'">dFdx</error>(float x) { return x; }
            void <error descr="Redefinition of 'ROUGHNESS'">ROUGHNESS</error>() {}
            float emit_subparticle(float x) { return x; }
            """,
        )
    }

    fun `test built-in define names cannot be declared globally`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float <error descr="Expected an identifier or '['.">CURRENT_RENDERER</error>;
            varying float <error descr="Expected an identifier or '['.">RENDERER_MOBILE</error>;
            const float <error descr="Expected an identifier or '[' after type.">RENDERER_FORWARD_PLUS</error> = 1.0;
            struct <error descr="Expected a struct identifier.">RENDERER_COMPATIBILITY</error> { float a; };
            struct S { float <error descr="Expected an identifier or '['.">RENDERER_MOBILE</error>; };
            float <error descr="Expected a function name after type.">RENDERER_MOBILE</error>(float x) { return x; }
            """,
        )
    }

    fun `test built-in define names cannot be declared locally`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct S { float a; };
            float f(float <error descr="Expected an identifier for argument name.">RENDERER_MOBILE</error>) { return 1.0; }
            void fragment() {
                float <error descr="Expected an identifier or '[' after type.">CURRENT_RENDERER</error> = 1.0;
                const float <error descr="Expected an identifier or '[' after type.">RENDERER_FORWARD_PLUS</error> = 1.0;
                for (int <error descr="Expected an identifier or '[' after type.">RENDERER_COMPATIBILITY</error> = 0, i = 0; i < 2; i++) {}
                S s;
                float x = s.<error descr="Expected an identifier as a member.">RENDERER_MOBILE</error>;
            }
            """,
        )
    }

    fun `test recursion`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f(float x) { return <error descr="Recursion is not allowed.">f</error>(x); }
            float g(float x) { float values[2]; return float(values.length()) + f(x); }
            """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code.trimIndent())
        myFixture.checkHighlighting(false, false, true)
    }
}

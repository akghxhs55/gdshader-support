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

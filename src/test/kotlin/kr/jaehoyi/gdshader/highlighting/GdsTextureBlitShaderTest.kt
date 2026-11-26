package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsTextureBlitShaderTest : BasePlatformTestCase() {
    fun `test blit built-ins`() {
        doHighlightTest(
            """
            shader_type texture_blit;
            render_mode blend_add;
            uniform sampler2D source;
            void blit() {
                COLOR0 = texture(source, UV) * MODULATE;
                COLOR3 = vec4(FRAGCOORD.xy, sin(TIME), PI);
                <error descr="Cannot assign to read-only built-in 'UV'">UV = vec2(0.0)</error>;
            }
            """,
        )
    }

    fun `test blit built-ins are only available in blit`() {
        doHighlightTest(
            """
            shader_type texture_blit;
            vec4 helper() {
                return <warning descr="Unresolved reference 'COLOR0'">COLOR0</warning>;
            }
            void blit() {}
            """,
        )
    }

    fun `test blit function has a line marker`() {
        myFixture.configureByText("test.gdshader", "shader_type texture_blit;\nvoid blit() {}\n")
        val gutters = myFixture.findAllGutters()
        assertSize(1, gutters)
        assertTrue(requireNotNull(gutters[0].tooltipText).contains("blit"))
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code.trimIndent())
        myFixture.checkHighlighting(true, false, true)
    }
}

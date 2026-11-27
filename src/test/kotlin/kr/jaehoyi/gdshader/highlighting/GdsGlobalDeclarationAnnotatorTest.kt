package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsGlobalDeclarationAnnotatorTest : BasePlatformTestCase() {
    fun `test shader type`() {
        doHighlightTest(
            """
            shader_type <error descr="Invalid shader type. Valid types are: 'spatial', 'canvas_item', 'particles', 'sky', 'fog', 'texture_blit'">foo</error>;
            """,
        )
        doHighlightTest(
            """
            shader_type spatial;
            <error descr="Shader type is already defined.">shader_type spatial;</error>
            """,
        )
    }

    fun `test render modes`() {
        doHighlightTest(
            """
            shader_type spatial;
            render_mode blend_add, unshaded_foo, cull_back;
            render_mode <error descr="Invalid render mode: 'use_debanding'.">use_debanding</error>;
            render_mode <error descr="Duplicated render mode: 'cull_back'.">cull_back</error>;
            render_mode <error descr="Redefinition of render mode: 'blend_mix'. The 'blend' mode has already been set to 'blend_add'.">blend_mix</error>;
            """,
        )
    }

    fun `test stencil modes`() {
        doHighlightTest(
            """
            shader_type spatial;
            stencil_mode write, compare_less, 1, write_if_depth_fail;
            stencil_mode <error descr="Duplicated stencil mode reference value: '2'.">2</error>;
            """,
        )
        doHighlightTest(
            """
            shader_type spatial;
            stencil_mode <error descr="Stencil mode reference value cannot be greater than 255: '256'.">256</error>;
            """,
        )
        doHighlightTest(
            """
            shader_type canvas_item;
            stencil_mode <error descr="Invalid stencil mode: 'read'.">read</error>;
            """,
        )
    }

    fun `test uniform groups`() {
        doHighlightTest(
            """
            shader_type spatial;
            group_uniforms colors.base;
            uniform vec4 albedo;
            group_uniforms;
            <error descr="Group needs to be opened before.">group_uniforms;</error>
            <error descr="Expected an uniform group identifier.">group_uniforms .x;</error>
            """,
        )
    }

    fun `test uniform qualifiers`() {
        doHighlightTest(
            """
            shader_type spatial;
            instance uniform vec4 tint;
            <error descr="The 'SCOPE_INSTANCE' qualifier is not supported for sampler types.">instance</error> uniform sampler2D tex;
            <error descr="The 'SCOPE_INSTANCE' qualifier is not supported for matrix types.">instance</error> uniform mat4 m;
            <error descr="The 'SCOPE_INSTANCE' qualifier is not supported for uniform arrays.">instance</error> uniform float a[2];
            <error descr="The 'SCOPE_GLOBAL' qualifier is not supported for uniform arrays.">global</error> uniform float g[2];
            """,
        )
        doHighlightTest(
            """
            shader_type sky;
            <error descr="Uniform instances are not yet implemented for 'sky' shaders.">instance</error> uniform vec4 tint;
            void sky() {}
            """,
        )
    }

    fun `test uniform hints`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform vec4 c : source_color, color_conversion_disabled;
            uniform sampler2D t : source_color, hint_default_white, filter_linear, repeat_enable, instance_index(1);
            uniform sampler2D ta[2] : source_color, filter_nearest;
            uniform float f1 : <error descr="Source color hint is for 'vec3', 'vec4' or sampler types only.">source_color</error>;
            uniform float f2 : <error descr="This hint is only for sampler types.">filter_linear</error>;
            uniform float f3[2] : <error descr="This hint is not supported for uniform arrays.">hint_range(0.0, 1.0)</error>;
            uniform vec4 c2 : <error descr="Hint 'color_conversion_disabled' should be preceded by 'source_color'.">color_conversion_disabled</error>;
            uniform sampler2D t2 : hint_default_white, <error descr="Redefinition of hint: 'hint_default_black'. The hint has already been set to 'hint_default_white'.">hint_default_black</error>;
            uniform sampler2D t3 : filter_linear, <error descr="Duplicated filter mode: 'filter_linear'.">filter_linear</error>;
            uniform sampler2D t4 : source_color, <error descr="Duplicated hint: 'source_color'.">source_color</error>;
            uniform vec2 i : <error descr="Range hint is for 'float' and 'int' only.">hint_range(0, 1)</error>;
            uniform float e : <error descr="Enum hint is for 'int' only.">hint_enum("A", "B")</error>;
            """,
        )
        doHighlightTest(
            """
            shader_type canvas_item;
            uniform sampler2D screen : hint_screen_texture;
            uniform sampler2D depth : <error descr="'hint_depth_texture' is not supported in 'canvas_item' shaders.">hint_depth_texture</error>;
            """,
        )
        doHighlightTest(
            """
            shader_type particles;
            uniform sampler2D screen : <error descr="'hint_screen_texture' is not supported in 'particles' shaders.">hint_screen_texture</error>;
            void process() {}
            """,
        )
    }

    fun `test instance index range`() {
        doHighlightTest(
            """
            shader_type spatial;
            instance uniform float a : instance_index(15);
            instance uniform float b : instance_index(15u);
            instance uniform float c : instance_index(0xF);
            instance uniform float d : <error descr="Allowed instance uniform indices must be within [0..15] range.">instance_index(16)</error>;
            instance uniform float e : <error descr="Allowed instance uniform indices must be within [0..15] range.">instance_index(0x10)</error>;
            instance uniform float f : <error descr="Allowed instance uniform indices must be within [0..15] range.">instance_index(16u)</error>;
            instance uniform float g : instance_index(1), <error descr="Can only specify 'instance_index' once.">instance_index(2)</error>;
            """,
        )
    }

    fun `test too many instance uniforms`() {
        val autoIndexed = (0 until 16).joinToString("\n") { "instance uniform float a$it;" }
        doHighlightTest(
            """
            |shader_type spatial;
            |$autoIndexed
            |instance uniform float explicit : instance_index(3);
            |<error descr="Too many 'instance' uniforms in shader, maximum supported is 16.">instance</error> uniform float overflow;
            """.trimMargin(),
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code.trimIndent())
        myFixture.checkHighlighting(false, false, true)
    }
}

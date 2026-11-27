package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInsight.daemon.HighlightDisplayKey
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsUnresolvedReferenceAnnotatorTest : BasePlatformTestCase() {
    // === Variable references ===

    fun `test unresolved variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = <error descr="Unresolved reference 'unknown_var'">unknown_var</error>;
            }
        """,
        )
    }

    fun `test severity follows inspection profile`() {
        myFixture.enableInspections(GdsUnresolvedReferenceInspection())
        val key = HighlightDisplayKey.find(GdsInspectionUtil.UNRESOLVED_REFERENCE)!!
        InspectionProjectProfileManager
            .getInstance(project)
            .currentProfile
            .setErrorLevel(key, HighlightDisplayLevel.WARNING, project)

        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float x; };
            void fragment() {
                float x = <warning descr="Unresolved reference 'unknown_var'">unknown_var</warning>;
                <warning descr="Unresolved reference 'unknown_func'">unknown_func</warning>();
                <warning descr="Unresolved reference 'Unknown'">Unknown</warning> d;
                Data data;
                float y = data.<error descr="Invalid member for 'Data' expression: '.missing'">missing</error>;
            }
        """,
        )
    }

    fun `test resolved local variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float my_var = 1.0;
                float x = my_var;
            }
        """,
        )
    }

    fun `test variable in unbraced body is not visible outside it`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                if (true) int a = 1; <error descr="Unresolved reference 'a'">a</error> = 2;
                if (true) int b = 1; else <error descr="Unresolved reference 'b'">b</error> = 2;
                do int c = 1; while (<error descr="Unresolved reference 'c'">c</error> < 3);
                for (int i = 0; false;) int d = 1; <error descr="Unresolved reference 'd'">d</error> = 2;
                while (false) int e = 1; <error descr="Unresolved reference 'e'">e</error> = 2;
            }
        """,
        )
    }

    fun `test local variable is not visible in its own initializer`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int f = <error descr="Unresolved reference 'f'">f</error>;
                const int c = <error descr="Unresolved reference 'c'">c</error>;
                for (int i = <error descr="Unresolved reference 'i'">i</error>; false;) { }
                for (const int k = 0, j = <error descr="Unresolved reference 'j'">j</error>; false;) { }
                int p = 1, q = <error descr="Unresolved reference 'q'">q</error>;
                int r[2] = { 1, <error descr="Unresolved reference 'r'">r</error>[0] };
                int a = 1, b = a;
                int x = 1;
                { int x = x; }
            }
        """,
        )
    }

    fun `test global is not visible in its own initializer`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int G = <error descr="Unresolved reference 'G'">G</error>;
            const int H = 1, K = <error descr="Unresolved reference 'K'">K</error>;
            const int M = 1, N = M;
            uniform int U = <error descr="Unresolved reference 'U'">U</error>;
            const int A[2] = { 1, <error descr="Unresolved reference 'A'">A</error>[0] };
            void fragment() { }
        """,
        )
    }

    fun `test resolved uniform variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float u_time;
            void fragment() {
                float t = u_time;
            }
        """,
        )
    }

    fun `test resolved builtin variable`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            void fragment() {
                COLOR = vec4(1.0);
            }
        """,
        )
    }

    fun `test resolved Godot 4 7 spatial builtins`() {
        doHighlightTest(
            """
            shader_type spatial;
            void helper() {
                bool multiview = IS_MULTIVIEW;
                bool shadow_pass = IN_SHADOW_PASS;
            }
            void fragment() {
                BENT_NORMAL_MAP = vec3(0.0, 0.0, 1.0);
                CLEARCOAT_ROUGHNESS = 0.5;
            }
        """,
        )
    }

    // CLEARCOAT_GLOSS existed only in Godot 3.x; Godot 4.0+ rejects it as an unknown identifier.
    fun `test Godot 3 CLEARCOAT_GLOSS is unresolved`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                <error descr="Unresolved reference 'CLEARCOAT_GLOSS'">CLEARCOAT_GLOSS</error> = 1.0;
            }
        """,
        )
    }

    fun `test resolved parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func(float param) {
                float x = param;
            }
        """,
        )
    }

    fun `test resolved constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float MAX_SPEED = 10.0;
            void fragment() {
                float s = MAX_SPEED;
            }
        """,
        )
    }

    fun `test sampler2DArray is recognized as builtin type`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform sampler2DArray texture_array;
            uniform isampler2DArray integer_texture_array;
            uniform usampler2DArray unsigned_texture_array;
        """,
        )
    }

    fun `test resolved literal define`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define EPS 0.01
            void fragment() {
                float x = EPS;
            }
        """,
        )
    }

    fun `test resolved builtin defines`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int R = RENDERER_MOBILE;
            void fragment() {
                float a[RENDERER_FORWARD_PLUS];
                if (CURRENT_RENDERER == RENDERER_COMPATIBILITY) {}
            }
        """,
        )
    }

    fun `test resolved non-literal define`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define TINT vec3(1.0)
            void fragment() {
                vec3 c = TINT;
            }
        """,
        )
    }

    fun `test resolved function-like macro`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define SQ(x) ((x) * (x))
            void fragment() {
                float y = SQ(2.0);
            }
        """,
        )
    }

    fun `test function-like macro name without call is unresolved`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define SQ(x) ((x) * (x))
            void fragment() {
                float y = <error descr="Unresolved reference 'SQ'">SQ</error>;
            }
        """,
        )
    }

    fun `test undefined macro is unresolved`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define TINT vec3(1.0)
            #undef TINT
            void fragment() {
                vec3 c = <error descr="Unresolved reference 'TINT'">TINT</error>;
            }
        """,
        )
    }

    fun `test macro used before definition is unresolved`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 c = <error descr="Unresolved reference 'TINT'">TINT</error>;
            }
            #define TINT vec3(1.0)
        """,
        )
    }

    // === Function references ===

    fun `test unresolved function`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = <error descr="Unresolved reference 'unknown_func'">unknown_func</error>(1.0);
            }
        """,
        )
    }

    fun `test function from include before usage`() {
        myFixture.addFileToProject("math.gdshaderinc", "float twice(float x) { return x * 2.0; }")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://math.gdshaderinc"
            void fragment() {
                float x = twice(1.0);
            }
        """,
        )
    }

    fun `test function from include after usage`() {
        myFixture.addFileToProject("math.gdshaderinc", "float twice(float x) { return x * 2.0; }")
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = <error descr="Unresolved reference 'twice'">twice</error>(1.0);
            }
            #include "res://math.gdshaderinc"
        """,
        )
    }

    fun `test resolved user function`() {
        doHighlightTest(
            """
            shader_type spatial;
            float my_func(float x) { return x; }
            void fragment() {
                float y = my_func(1.0);
            }
        """,
        )
    }

    fun `test resolved builtin function`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float y = sin(1.0);
            }
        """,
        )
    }

    // === Struct references ===

    fun `test unresolved struct`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                <error descr="Unresolved reference 'UnknownStruct'">UnknownStruct</error> s;
            }
        """,
        )
    }

    fun `test resolved struct`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyStruct { float value; };
            void fragment() {
                MyStruct s;
            }
        """,
        )
    }

    fun `test struct used as its own member type`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Node { <error descr="Unresolved reference 'Node'">Node</error> next; float value; };
        """,
        )
    }

    fun `test struct used as member type before declaration`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct A { <error descr="Unresolved reference 'B'">B</error> b; };
            struct B { float x; };
        """,
        )
    }

    fun `test struct used as parameter type before declaration`() {
        doHighlightTest(
            """
            shader_type spatial;
            void f(<error descr="Unresolved reference 'B'">B</error> b) {}
            struct B { float x; };
        """,
        )
    }

    fun `test struct used as member type after declaration`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct B { float x; };
            struct A { B b; };
        """,
        )
    }

    // === Struct member references ===

    fun `test unresolved struct member`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyStruct { float value; };
            void fragment() {
                MyStruct s = MyStruct(1.0);
                float x = s.<error descr="Invalid member for 'MyStruct' expression: '.unknown_field'">unknown_field</error>;
            }
        """,
        )
    }

    fun `test resolved struct member`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyStruct { float value; };
            void fragment() {
                MyStruct s = MyStruct(1.0);
                float x = s.value;
            }
        """,
        )
    }

    // === Vector swizzle ===

    fun `test vector swizzle not flagged`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0, 2.0, 3.0);
                vec2 a = v.xy;
                float b = v.z;
            }
        """,
        )
    }

    // === Invalid swizzle ===

    fun `test invalid swizzle component for vec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(1.0, 2.0);
                float z = v.<error descr="Invalid member for 'vec2' expression: '.z'">z</error>;
            }
        """,
        )
    }

    fun `test invalid swizzle component w for vec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0, 2.0, 3.0);
                float w = v.<error descr="Invalid member for 'vec3' expression: '.w'">w</error>;
            }
        """,
        )
    }

    fun `test valid swizzle for vec4`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                float w = v.w;
                vec2 xy = v.xy;
                vec3 rgb = v.rgb;
            }
        """,
        )
    }

    fun `test invalid multi-component swizzle for vec2`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(1.0, 2.0);
                vec3 bad = v.<error descr="Invalid member for 'vec2' expression: '.xyz'">xyz</error>;
            }
        """,
        )
    }

    // === Mixed swizzle sets ===

    fun `test mixed position and color swizzle sets`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                vec2 a = v.<error descr="Cannot combine symbols from different sets in expression '.xg'">xg</error>;
            }
        """,
        )
    }

    fun `test mixed color and texture swizzle sets`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
                vec3 a = v.<error descr="Cannot combine symbols from different sets in expression '.rgp'">rgp</error>;
            }
        """,
        )
    }

    fun `test mixed position and texture swizzle sets`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                ivec4 v = ivec4(1);
                ivec4 a = v.<error descr="Cannot combine symbols from different sets in expression '.xysq'">xysq</error>;
            }
        """,
        )
    }

    fun `test mixed swizzle sets reported before invalid component`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(1.0);
                vec3 a = v.<error descr="Cannot combine symbols from different sets in expression '.xgz'">xgz</error>;
            }
        """,
        )
    }

    fun `test out of range component does not count as mixed set`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(1.0);
                vec2 a = v.<error descr="Invalid member for 'vec2' expression: '.xb'">xb</error>;
            }
        """,
        )
    }

    fun `test member access on mixed swizzle result is not reported again`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                float a = v.<error descr="Cannot combine symbols from different sets in expression '.xg'">xg</error>.x;
            }
        """,
        )
    }

    fun `test single swizzle set for each set`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                vec4 a = v.wzyx;
                vec4 b = v.abgr;
                vec4 c = v.qpts;
            }
        """,
        )
    }

    // === Member access on non-member types ===

    fun `test member access on float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                float y = x.<error descr="Invalid member for 'float' expression: '.value'">value</error>;
            }
        """,
        )
    }

    fun `test member access on int`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int x = 1;
                int y = x.<error descr="Invalid member for 'int' expression: '.z'">z</error>;
            }
        """,
        )
    }

    // === Global scope builtins ===

    fun `test resolved builtin constant in global constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float HALF_PI = PI * 0.5;
            const float FULL_TURN = TAU;
        """,
        )
    }

    fun `test resolved builtin function in global constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float S = sin(1.0);
        """,
        )
    }

    fun `test unresolved function in global constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float S = <error descr="Unresolved reference 'unknown_func'">unknown_func</error>(1.0);
        """,
        )
    }

    fun `test processing function builtin is unresolved in global constant`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            const vec4 C = <error descr="Unresolved reference 'COLOR'">COLOR</error>;
        """,
        )
    }

    // === Include files ===

    fun `test gdshaderinc files are skipped`() {
        myFixture.configureByText(
            "test.gdshaderinc",
            """
            void my_func() {
                float x = unknown_var;
            }
        """,
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun `test resolved variable from included file`() {
        myFixture.addFileToProject("utils.gdshaderinc", "uniform float u_global;")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://utils.gdshaderinc"
            void fragment() {
                float t = u_global;
            }
        """,
        )
    }

    // === Include files ===

    fun `test symbols declared outside include file`() {
        doIncludeHighlightTest(
            """
            float use(ExternalStruct s) {
                return external_function(external_value);
            }
        """,
        )
    }

    fun `test invalid swizzle in include file`() {
        doIncludeHighlightTest(
            """
            float use(vec2 v) {
                return v.<error descr="Invalid member for 'vec2' expression: '.z'">z</error>;
            }
        """,
        )
    }

    fun `test invalid struct member in include file`() {
        doIncludeHighlightTest(
            """
            struct Data { float value; };
            float use(Data d) {
                return d.<error descr="Invalid member for 'Data' expression: '.missing'">missing</error>;
            }
        """,
        )
    }

    fun `test member of type declared outside include file`() {
        doIncludeHighlightTest(
            """
            float use(ExternalStruct s) {
                return s.anything;
            }
        """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(true, false, true)
    }

    private fun doIncludeHighlightTest(code: String) {
        myFixture.configureByText("test_include.gdshaderinc", code)
        myFixture.checkHighlighting(true, false, true)
    }
}

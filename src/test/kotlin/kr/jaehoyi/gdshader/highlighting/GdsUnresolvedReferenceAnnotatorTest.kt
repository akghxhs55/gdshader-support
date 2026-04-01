package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsUnresolvedReferenceAnnotatorTest : BasePlatformTestCase() {
    // === Variable references ===

    fun `test unresolved variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = <warning descr="Unresolved reference 'unknown_var'">unknown_var</warning>;
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

    // === Function references ===

    fun `test unresolved function`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = <warning descr="Unresolved reference 'unknown_func'">unknown_func</warning>(1.0);
            }
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

    fun `test function in helper skipped`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_helper() {
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

    fun `test vector swizzle reassignment not flagged`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec4 c = vec4(1.0);
                vec3 d = c.rgb;
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
        myFixture.checkHighlighting(false, false, false)
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

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(true, false, true)
    }
}

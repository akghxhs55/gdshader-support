package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsFunctionCallAnnotatorTest : BasePlatformTestCase() {

    // === User-defined function argument count ===

    fun `test correct argument count`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            void fragment() {
                float x = add(1.0, 2.0);
            }
        """)
    }

    fun `test too few arguments`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            void fragment() {
                float x = add(<error descr="Too few arguments for 'add(float, float)' call. Expected at least 2 but received 1.">1.0</error>);
            }
        """)
    }

    fun `test too many arguments`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            void fragment() {
                float x = add(<error descr="Too many arguments for 'add(float, float)' call. Expected at most 2 but received 3.">1.0, 2.0, 3.0</error>);
            }
        """)
    }

    fun `test zero arguments when some expected`() {
        doHighlightTest("""
            shader_type spatial;
            float get_value(float x) { return x; }
            void fragment() {
                float x = get_value(<error descr="Too few arguments for 'get_value(float)' call. Expected at least 1 but received 0."></error>);
            }
        """)
    }

    // === User-defined function argument type ===

    fun `test correct argument types`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            void fragment() {
                float x = add(1.0, 2.0);
            }
        """)
    }

    fun `test wrong argument type`() {
        doHighlightTest("""
            shader_type spatial;
            float f(int x) { return 1.0; }
            void fragment() {
                float x = f(<error descr="No matching function for 'f(int)' call: argument 1 should be int but is float">1.0</error>);
            }
        """)
    }

    fun `test wrong second argument type`() {
        doHighlightTest("""
            shader_type spatial;
            float f(float a, int b) { return a; }
            void fragment() {
                float x = f(<error descr="No matching function for 'f(float, int)' call: argument 2 should be int but is float">1.0, 2.0</error>);
            }
        """)
    }

    // === Implicit conversion in arguments ===

    fun `test int literal to float parameter`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(0, 0);
            }
        """)
    }

    fun `test int literal to float parameter mixed`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(1.0, 0);
            }
        """)
    }

    fun `test uint literal to int parameter`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                ivec2 v = ivec2(1u);
            }
        """)
    }

    fun `test int literal to uint parameter`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                uvec2 v = uvec2(1);
            }
        """)
    }

    fun `test int literal to float function parameter`() {
        doHighlightTest("""
            shader_type spatial;
            float f(float a) { return a; }
            void fragment() {
                float x = f(1);
            }
        """)
    }

    // === Builtin function argument count ===

    fun `test builtin function correct args`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = sin(1.0);
            }
        """)
    }

    fun `test builtin function too many args`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = sin(<error descr="Too many arguments for 'sin(vec_type)' call. Expected at most 1 but received 2.">1.0, 2.0</error>);
            }
        """)
    }

    fun `test builtin function with multiple overloads`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = clamp(1.0, 0.0, 1.0);
            }
        """)
    }

    // === Constructor argument count ===

    fun `test vec3 correct constructor`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0, 2.0, 3.0);
            }
        """)
    }

    fun `test vec3 single arg constructor`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
            }
        """)
    }

    fun `test vec3 too many args`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(<error descr="Too many arguments for 'vec3(float, float, float)' call. Expected at most 3 but received 5.">1.0, 2.0, 3.0, 4.0, 5.0</error>);
            }
        """)
    }

    // === Struct constructor ===

    fun `test struct constructor correct`() {
        doHighlightTest("""
            shader_type spatial;
            struct MyData { float x; float y; };
            void fragment() {
                MyData d = MyData(1.0, 2.0);
            }
        """)
    }

    fun `test struct constructor too few args`() {
        doHighlightTest("""
            shader_type spatial;
            struct MyData { float x; float y; };
            void fragment() {
                MyData d = MyData(<error descr="Too few arguments for 'MyData(float, float)' call. Expected at least 2 but received 1.">1.0</error>);
            }
        """)
    }

    // === Optional parameters ===

    fun `test texture with bias omitted`() {
        doHighlightTest("""
            shader_type spatial;
            uniform sampler2D tex;
            void fragment() {
                vec4 c = texture(tex, UV);
            }
        """)
    }

    fun `test texture with bias provided`() {
        doHighlightTest("""
            shader_type spatial;
            uniform sampler2D tex;
            void fragment() {
                vec4 c = texture(tex, UV, 0.5);
            }
        """)
    }

    fun `test texture too many args`() {
        doHighlightTest("""
            shader_type spatial;
            uniform sampler2D tex;
            void fragment() {
                vec4 c = texture(<error descr="Too many arguments for 'texture(gsampler2D, vec2, float)' call. Expected at most 3 but received 4.">tex, UV, 0.5, 1.0</error>);
            }
        """)
    }

    // === Edge cases ===

    fun `test gdshaderinc files are skipped`() {
        myFixture.configureByText("test.gdshaderinc", """
            float add(float a, float b) { return a + b; }
            void my_func() {
                float x = add(1.0, 2.0, 3.0);
            }
        """)
        myFixture.checkHighlighting(false, false, false)
    }

    fun `test unresolved function is not checked`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = <error descr="Unresolved reference 'unknown_func'">unknown_func</error>(1.0, 2.0, 3.0);
            }
        """)
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

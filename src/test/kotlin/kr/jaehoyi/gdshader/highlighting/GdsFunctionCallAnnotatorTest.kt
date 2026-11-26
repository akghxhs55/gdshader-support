package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsFunctionCallAnnotatorTest : BasePlatformTestCase() {
    // === User-defined function argument count ===

    fun `test correct argument count`() {
        doHighlightTest(
            """
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            void fragment() {
                float x = add(1.0, 2.0);
            }
        """,
        )
    }

    fun `test too few arguments`() {
        doHighlightTest(
            """
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            void fragment() {
                float x = add(<error descr="Too few arguments for 'add(float, float)' call. Expected at least 2 but received 1.">1.0</error>);
            }
        """,
        )
    }

    fun `test too many arguments`() {
        doHighlightTest(
            """
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            void fragment() {
                float x = add(<error descr="Too many arguments for 'add(float, float)' call. Expected at most 2 but received 3.">1.0, 2.0, 3.0</error>);
            }
        """,
        )
    }

    fun `test zero arguments when some expected`() {
        doHighlightTest(
            """
            shader_type spatial;
            float get_value(float x) { return x; }
            void fragment() {
                float x = get_value(<error descr="Too few arguments for 'get_value(float)' call. Expected at least 1 but received 0."></error>);
            }
        """,
        )
    }

    fun `test unknown parameter type does not report argument count`() {
        doHighlightTest(
            """
            shader_type spatial;
            float foo(<error descr="Unresolved reference 'Unknown'">Unknown</error> a, float b) { return b; }
            void fragment() {
                float x = foo(1.0, 2.0);
            }
        """,
        )
    }

    fun `test overload with unknown parameter type does not report argument count`() {
        doHighlightTest(
            """
            shader_type spatial;
            float foo(float a) { return a; }
            float foo(<error descr="Unresolved reference 'Unknown'">Unknown</error> a, float b) { return b; }
            void fragment() {
                float x = foo(1.0, 2.0);
            }
        """,
        )
    }

    // === User-defined function argument type ===

    fun `test wrong argument type`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f(int x) { return 1.0; }
            void fragment() {
                float x = f(<error descr="No matching function for 'f(int)' call: argument 1 should be int but is float">1.0</error>);
            }
        """,
        )
    }

    fun `test wrong second argument type`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f(float a, int b) { return a; }
            void fragment() {
                float x = f(<error descr="No matching function for 'f(float, int)' call: argument 2 should be int but is float">1.0, 2.0</error>);
            }
        """,
        )
    }

    fun `test overloaded call with unknown argument type does not report mismatch`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f(float a) { return a; }
            vec2 f(int x, float a) { return vec2(a); }
            void fragment() {
                vec2 r = f(unknown, 1.0);
            }
            """.trimIndent(),
        )
    }

    // === Implicit conversion in arguments ===

    fun `test int literal to float parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(0, 0);
            }
        """,
        )
    }

    fun `test int literal to float parameter mixed`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec2 v = vec2(1.0, 0);
            }
        """,
        )
    }

    fun `test uint literal to int parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                ivec2 v = ivec2(1u);
            }
        """,
        )
    }

    fun `test int literal to uint parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                uvec2 v = uvec2(1);
            }
        """,
        )
    }

    fun `test int literal to float function parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f(float a) { return a; }
            void fragment() {
                float x = f(1);
            }
        """,
        )
    }

    fun `test boolean literal to float function parameter is invalid`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f(float a) { return a; }
            void fragment() {
                float x = f(<error descr="No matching function for 'f(float)' call: argument 1 should be float but is bool">true</error>);
            }
        """,
        )
    }

    // === Builtin function argument count ===

    fun `test builtin function correct args`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = sin(1.0);
            }
        """,
        )
    }

    fun `test builtin function too many args`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = sin(<error descr="Too many arguments for 'sin(vec_type)' call. Expected at most 1 but received 2.">1.0, 2.0</error>);
            }
        """,
        )
    }

    fun `test builtin function with multiple overloads`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = clamp(1.0, 0.0, 1.0);
            }
        """,
        )
    }

    fun `test mix accepts scalar and vector multiplication chain`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float v_specular = 1.0;
                float dielectric_f0_scale = 1.0;
                vec3 dielectric_f0 = vec3(1.0);
                vec3 Ctint = vec3(1.0);
                float specularTint = 0.5;
                vec3 Cdlin = vec3(1.0);
                float v_metallic = 0.5;

                vec3 Cspec0 = mix(v_specular * dielectric_f0_scale * dielectric_f0 * mix(vec3(1.0), Ctint, specularTint), Cdlin, v_metallic);
            }
        """,
        )
    }

    // === Constructor argument count ===

    fun `test vector conversion constructor`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                ivec2 size = ivec2(64, 32);
                vec2 converted = vec2(size);
            }
        """,
        )
    }

    fun `test texture size vector conversion constructor`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform sampler2D noise;
            void fragment() {
                vec2 size = vec2(textureSize(noise, 0));
            }
        """,
        )
    }

    fun `test vector conversion constructor with scalar`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 converted = vec3(vec2(64.0, 32.0), 1);
            }
        """,
        )
    }

    fun `test boolean vector constructor rejects numeric scalar`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                bvec2 values = bvec2(<error descr="No matching function for 'bvec2(bool)' call: argument 1 should be bool but is int">1</error>);
            }
        """,
        )
    }

    fun `test vec3 correct constructor`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0, 2.0, 3.0);
            }
        """,
        )
    }

    fun `test vec3 single arg constructor`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0);
            }
        """,
        )
    }

    fun `test vec3 too many args`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(<error descr="Too many arguments for 'vec3(float, float, float)' call. Expected at most 3 but received 5.">1.0, 2.0, 3.0, 4.0, 5.0</error>);
            }
        """,
        )
    }

    fun `test matrix scalar constructor is valid`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat4 m = mat4(1.0);
            }
        """,
        )
    }

    fun `test matrix conversion constructor is valid`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat2 a = mat2(1.0);
                mat3 b = mat3(a);
                mat4 c = mat4(b);
            }
        """,
        )
    }

    fun `test matrix component wise constructor is invalid`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat2 m = mat2(<error descr="Too many arguments for 'mat2(vec2, vec2)' call. Expected at most 2 but received 4.">1.0, 0.0, 0.0, 1.0</error>);
            }
        """,
        )
    }

    fun `test matrix column vector constructor is valid`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                mat4 m = mat4(vec4(1.0), vec4(0.0), vec4(0.0), vec4(1.0));
            }
        """,
        )
    }

    // === Struct constructor ===

    fun `test struct constructor correct`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; float y; };
            void fragment() {
                MyData d = MyData(1.0, 2.0);
            }
        """,
        )
    }

    fun `test struct constructor too few args`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; float y; };
            void fragment() {
                MyData d = MyData(<error descr="Too few arguments for 'MyData(float, float)' call. Expected at least 2 but received 1.">1.0</error>);
            }
        """,
        )
    }

    fun `test struct constructor with struct member too few args`() {
        doHighlightTest(
            """
            shader_type canvas_item;

            struct InnerStruct {
                float a;
                vec2 b;
            };

            struct OuterStruct {
                float x;
                InnerStruct y;
            };

            void fragment() {
                OuterStruct o = OuterStruct(
                    <error descr="Too few arguments for 'OuterStruct(float, InnerStruct)' call. Expected at least 2 but received 1.">1.0</error>
                );
            }
        """,
        )
    }

    fun `test struct constructor with struct member correct`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            struct InnerStruct { float a; vec2 b; };
            struct OuterStruct { float x; InnerStruct y; };
            void fragment() {
                OuterStruct o = OuterStruct(1.0, InnerStruct(1.0, vec2(0.0)));
            }
        """,
        )
    }

    fun `test struct constructor rejects wrong type for struct member`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            struct InnerStruct { float a; vec2 b; };
            struct OtherStruct { float a; };
            struct OuterStruct { float x; InnerStruct y; };
            void fragment() {
                OuterStruct o = OuterStruct(<error descr="No matching function for 'OuterStruct(float, InnerStruct)' call: argument 2 should be InnerStruct but is OtherStruct">1.0, OtherStruct(1.0)</error>);
            }
        """,
        )
    }

    fun `test struct constructor rejects int constant for float member`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; float y; };
            void fragment() {
                MyData d = MyData(<error descr="No matching function for 'MyData(float, float)' call: argument 1 should be float but is int">1, 2.0</error>);
            }
        """,
        )
    }

    fun `test struct constructor rejects uint constant for int member`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { int x; };
            void fragment() {
                MyData d = MyData(<error descr="No matching function for 'MyData(int)' call: argument 1 should be int but is uint">1u</error>);
            }
        """,
        )
    }

    fun `test user function accepts int constant for float parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            float half_of(float x) { return x * 0.5; }
            void fragment() {
                float h = half_of(1);
            }
        """,
        )
    }

    fun `test struct constructor accepts array member of same size`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float values[2]; };
            void fragment() {
                MyData d = MyData(float[2](1.0, 2.0));
            }
        """,
        )
    }

    fun `test struct constructor rejects array member of different size`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float values[3]; };
            void fragment() {
                MyData d = MyData(<error descr="No matching function for 'MyData(float[3])' call: argument 1 should be float[3] but is float[2]">float[2](1.0, 2.0)</error>);
            }
        """,
        )
    }

    fun `test function accepts struct constructor argument`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            struct InnerStruct { float a; vec2 b; };
            float sum(InnerStruct s) { return s.a + s.b.x; }
            void fragment() {
                float total = sum(InnerStruct(1.0, vec2(0.0)));
            }
        """,
        )
    }

    fun `test struct array constructor`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            void fragment() {
                MyData items[2] = <error descr="Struct array constructor is not allowed here">MyData</error>[2](MyData(1.0), MyData(2.0));
                MyData others[] = <error descr="Struct array constructor is not allowed here">MyData</error>[](MyData(1.0), MyData(2.0));
            }
        """,
        )
    }

    fun `test struct array constructor in local constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            void fragment() {
                const MyData items[2] = <error descr="Struct array constructor is not allowed here">MyData</error>[2](MyData(1.0), MyData(2.0));
            }
        """,
        )
    }

    fun `test struct array constructor as function argument`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            void f(MyData a[2]) {}
            void fragment() {
                f(<error descr="Struct array constructor is not allowed here">MyData</error>[2](MyData(1.0), MyData(2.0)));
            }
        """,
        )
    }

    fun `test struct array initializer list`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            void fragment() {
                MyData items[2] = { MyData(1.0), MyData(2.0) };
            }
        """,
        )
    }

    fun `test struct array constructor assigned to array`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            void fragment() {
                MyData items[2];
                items = MyData[2](MyData(1.0), MyData(2.0));
            }
        """,
        )
    }

    fun `test struct array constructor assigned to array member`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            struct Wrap { MyData arr[2]; };
            void fragment() {
                Wrap w;
                w.arr = MyData[2](MyData(1.0), MyData(2.0));
            }
        """,
        )
    }

    fun `test struct array constructor as array member of struct constructor`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            struct Wrap { MyData arr[2]; };
            void fragment() {
                Wrap w = Wrap(MyData[2](MyData(1.0), MyData(2.0)));
            }
        """,
        )
    }

    fun `test constant values passed to inout parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; float arr[2]; vec2 v; };
            uniform float u_val;
            uniform float u_arr[2];
            const float G = 1.0;
            float g() { return 1.0; }
            void f(inout float p) {}
            void fa(inout float p[2]) {}
            void fv(inout vec2 p) {}
            void h(const float p) { f(<error descr="A constant value cannot be passed for the 'inout' parameter.">p</error>); }
            void fragment() {
                float x;
                vec2 v;
                mat2 m;
                MyData d;
                const float c = 1.0;
                const vec2 cv[2] = { vec2(0.0), vec2(1.0) };
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">1.0</error>);
                fa(<error descr="A constant value cannot be passed for the 'inout' parameter.">{ 1.0, 2.0 }</error>);
                fa(<error descr="A constant value cannot be passed for the 'inout' parameter.">float[2](1.0, 2.0)</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">x + 1.0</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">-x</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">x++</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">++x</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">x = 1.0</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">true ? x : x</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">g()</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">float(1.0)</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">c</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">G</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">u_val</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">u_arr[0]</error>);
                fa(<error descr="A constant value cannot be passed for the 'inout' parameter.">u_arr</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">TIME</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">UV.x</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">v[0]</error>);
                fv(<error descr="A constant value cannot be passed for the 'inout' parameter.">m[0]</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">d.v[0]</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">cv[0].x</error>);
            }
        """,
        )
    }

    fun `test parenthesized arguments passed to inout parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform vec2 u_v;
            float[2] h() { float a[2]; return a; }
            void f(inout float p) {}
            void fv(inout vec2 p) {}
            void fragment() {
                float a[2];
                const float ca[2] = { 1.0, 2.0 };
                const float c = 1.0;
                const vec2 cv = vec2(0.0);
                vec2 v;
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">h()[0]</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">(a)[0]</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">(ca)[0]</error>);
                f(<error descr="A constant value cannot be passed for the 'inout' parameter.">(c)</error>);
                fv(<error descr="A constant value cannot be passed for the 'inout' parameter.">(u_v)</error>);
                f((cv).x);
                f((u_v).x);
                f((v).x);
            }
        """,
        )
    }

    fun `test variables passed to inout parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; float arr[2]; vec2 v; };
            uniform vec2 u_v;
            MyData g() { return MyData(1.0, float[2](1.0, 2.0), vec2(0.0)); }
            void f(inout float p) {}
            void fa(inout float p[2]) {}
            void fv(inout vec2 p) {}
            void h(in float p) { f(p); }
            void fragment() {
                float x;
                float a[2];
                vec2 v;
                vec4 v4;
                vec2 va[2];
                MyData d;
                f(x);
                f((x));
                fa(a);
                f(a[0]);
                f(v.x);
                fv(v4.xx);
                f(d.x);
                f(d.arr[0]);
                f(va[0].x);
                f(u_v.x);
                f(g().x);
                f(ROUGHNESS);
            }
        """,
        )
    }

    fun `test constant value passed to out parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            void f(out float p) {}
            void fragment() {
                f(<error descr="A constant value cannot be passed for the 'out' parameter.">1.0</error>);
            }
        """,
        )
    }

    fun `test struct array constructor in global constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            const MyData ITEMS[2] = MyData[2](MyData(1.0), MyData(2.0));
            void fragment() {}
        """,
        )
    }

    fun `test struct constructor with unresolved member type is not checked`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float x; <error descr="Unresolved reference 'Missing'">Missing</error> y; };
            void fragment() {
                Data d = Data(1.0, 2.0);
            }
        """,
        )
    }

    fun `test struct constructor with duplicate member is not checked`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float x; float <error descr="Redefinition of 'x'">x</error>; };
            void fragment() {
                Data d = Data(1.0, 2.0);
            }
        """,
        )
    }

    fun `test struct constructor with self referencing member is not checked`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Node { <error descr="Unresolved reference 'Node'">Node</error> next; float value; };
            void fragment() {
                Node first;
                Node n = Node(first, 1.0);
            }
        """,
        )
    }

    fun `test struct constructor with array member correct`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float values[3]; };
            void fragment() {
                Data d = Data(float[3](1.0, 2.0, 3.0));
            }
        """,
        )
    }

    fun `test struct constructor with array member rejects scalar`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float values[3]; };
            void fragment() {
                Data d = Data(<error descr="No matching function for 'Data(float[3])' call: argument 1 should be float[3] but is float">1.0</error>);
            }
        """,
        )
    }

    // === Optional parameters ===

    fun `test texture with bias omitted`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform sampler2D tex;
            void fragment() {
                vec4 c = texture(tex, UV);
            }
        """,
        )
    }

    fun `test texture with bias provided`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform sampler2D tex;
            void fragment() {
                vec4 c = texture(tex, UV, 0.5);
            }
        """,
        )
    }

    fun `test texture too many args`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform sampler2D tex;
            void fragment() {
                vec4 c = texture(<error descr="Too many arguments for 'texture(gsampler2D, vec2, float)' call. Expected at most 3 but received 4.">tex, UV, 0.5, 1.0</error>);
            }
        """,
        )
    }

    // === Edge cases ===

    fun `test unresolved function is not checked`() {
        myFixture.configureByText(
            "test_shader.gdshader",
            """
            shader_type spatial;
            void fragment() {
                float x = <warning descr="Unresolved reference 'unknown_func'">unknown_func</warning>(1.0, 2.0, 3.0);
            }
        """,
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun `test array length accepts no arguments`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float values[3];
                int count = values.length();
                ALBEDO = vec3(float(count));
            }
            """.trimIndent(),
        )
    }

    fun `test array length rejects arguments`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float values[3];
                int count = values.length(<error descr="Too many arguments for 'length()' call. Expected at most 0 but received 1.">1</error>);
            }
            """.trimIndent(),
        )
    }

    fun `test nested array length and global length coexist`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float values[3]; };
            void fragment() {
                Data data[2];
                int count = data[0].values.length();
                float distance = length(vec3(1.0));
            }
            """.trimIndent(),
        )
    }

    // === Include files ===

    fun `test struct constructor argument count in include file`() {
        doIncludeHighlightTest(
            """
            struct FooStruct {
                float screen_pixel_size;
                float bounds;
            };
            const FooStruct FOO = FooStruct(<error descr="Too few arguments for 'FooStruct(float, float)' call. Expected at least 2 but received 1.">1.0</error>);
        """,
        )
    }

    fun `test struct constructor argument type in include file`() {
        doIncludeHighlightTest(
            """
            struct FooStruct {
                float screen_pixel_size;
                float bounds;
            };
            const FooStruct FOO = FooStruct(<error descr="No matching function for 'FooStruct(float, float)' call: argument 2 should be float but is vec2">1.0, vec2(1.0)</error>);
        """,
        )
    }

    fun `test builtin constructor in include file`() {
        doIncludeHighlightTest(
            """
            vec3 make() {
                return vec3(<error descr="Too many arguments for 'vec3(float, float, float)' call. Expected at most 3 but received 4.">1.0, 2.0, 3.0, 4.0</error>);
            }
        """,
        )
    }

    fun `test call to function declared outside include file`() {
        doIncludeHighlightTest(
            """
            float use() {
                return external_function(1.0, 2.0, 3.0);
            }
        """,
        )
    }

    fun `test call with argument declared outside include file`() {
        doIncludeHighlightTest(
            """
            float use() {
                return sin(external_value);
            }
        """,
        )
    }

    // === Built-in function constant conversion ===

    fun `test built-in functions convert scalar constants across overloads`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 0.5;
                vec2 v = vec2(0.5);
                float a = pow(x, 2);
                float b = clamp(x, 0, 1);
                float c = sin(1);
                float d = max(x, 0);
                float e = step(0, x);
                float f = max(1, 2.0);
                vec2 g = max(v, 1);
                int h = max(1, 2);
                float i = max(1u, 2);
                float j = pow(2, x);
            }
        """,
        )
    }

    fun `test built-in function does not convert non-constant arguments`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 0.5;
                int n = 2;
                float a = pow(<error descr="Invalid arguments for the built-in function: 'pow(float,int)'">x, n</error>);
            }
        """,
        )
    }

    fun `test only reduced constants are converted`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define TWO 2
            const int C = 2;
            float half_of(float v) { return v * 0.5; }
            void fragment() {
                const int L = 2;
                float x = 0.5;
                float a = pow(x, -2);
                float b = pow(x, int(2));
                float c = pow(x, (2));
                float d = pow(x, TWO);
                float e = half_of(-1);
                float f = pow(<error descr="Invalid arguments for the built-in function: 'pow(float,int)'">x, 1 + 1</error>);
                float g = pow(<error descr="Invalid arguments for the built-in function: 'pow(float,int)'">x, C</error>);
                float h = pow(<error descr="Invalid arguments for the built-in function: 'pow(float,int)'">x, L</error>);
                float i = half_of(<error descr="No matching function for 'half_of(float)' call: argument 1 should be float but is int">C</error>);
                float j = half_of(<error descr="No matching function for 'half_of(float)' call: argument 1 should be float but is int">1 + 1</error>);
            }
        """,
        )
    }

    fun `test built-in function converts constants only when the value fits`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float a = uintBitsToFloat(1);
                float b = uintBitsToFloat(<error descr="Invalid arguments for the built-in function: 'uintBitsToFloat(int)'">-1</error>);
                float c = intBitsToFloat(2147483647u);
                float d = intBitsToFloat(<error descr="Invalid arguments for the built-in function: 'intBitsToFloat(uint)'">2147483648u</error>);
            }
        """,
        )
    }

    fun `test stage function does not convert constants`() {
        doHighlightTest(
            """
            shader_type particles;
            void process() {
                emit_subparticle(<error descr="No matching function for 'emit_subparticle(mat4, vec3, vec4, vec4, uint)' call: argument 5 should be uint but is int">mat4(1.0), vec3(0.0), vec4(1.0), vec4(0.0), 0</error>);
            }
        """,
        )
    }

    fun `test overloaded user function does not convert constants`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f(float a) { return a; }
            float f(vec2 a) { return a.x; }
            void fragment() {
                float x = f(<error descr="No matching function for 'f(float)' call: argument 1 should be float but is int">1</error>);
            }
        """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }

    private fun doIncludeHighlightTest(code: String) {
        myFixture.configureByText("test_include.gdshaderinc", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

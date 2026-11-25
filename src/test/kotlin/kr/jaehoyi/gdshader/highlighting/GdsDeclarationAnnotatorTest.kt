package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsDeclarationAnnotatorTest : BasePlatformTestCase() {
    fun `test shader_type first`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float x;
        """,
        )
    }

    fun `test missing shader_type`() {
        doHighlightTest(
            """
            <error descr="Expected 'shader_type' before the first declaration">uniform float x;</error>
        """,
        )
    }

    fun `test empty struct`() {
        doHighlightTest(
            """
            shader_type spatial;
            <error descr="Empty structs are not allowed">struct Empty {};</error>
        """,
        )
    }

    fun `test non-empty struct`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float x; };
        """,
        )
    }

    fun `test include gdshaderinc file`() {
        myFixture.addFileToProject("utils.gdshaderinc", "float add(float a, float b) { return a + b; }")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://utils.gdshaderinc"
        """,
        )
    }

    fun `test include non-gdshaderinc file`() {
        doHighlightTest(
            """
            shader_type spatial;
            #include "<error descr="Only '.gdshaderinc' files can be included">res://other.gdshader</error>"
        """,
        )
    }

    fun `test include nonexistent file`() {
        doHighlightTest(
            """
            shader_type spatial;
            #include "<error descr="Cannot resolve file 'res://nonexistent.gdshaderinc'">res://nonexistent.gdshaderinc</error>"
        """,
        )
    }

    fun `test uniform single array size`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float arr[3];
        """,
        )
    }

    fun `test uniform double array size`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float[3] arr<error descr="Array size is already defined">[2]</error>;
        """,
        )
    }

    fun `test parameter double array size`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func(float[3] arr<error descr="Array size is already defined">[2]</error>) {}
        """,
        )
    }

    fun `test struct member double array size`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float[3] x<error descr="Array size is already defined">[2]</error>; };
        """,
        )
    }

    fun `test local variable double array size`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float[3] arr<error descr="Array size is already defined">[2]</error>;
            }
        """,
        )
    }

    fun `test duplicate function`() {
        doHighlightTest(
            """
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            float <error descr="Redefinition of 'add'">add</error>(float x, float y) { return x + y; }
        """,
        )
    }

    fun `test no duplicate function`() {
        doHighlightTest(
            """
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            float sub(float a, float b) { return a - b; }
        """,
        )
    }

    fun `test function overload with different parameters`() {
        doHighlightTest(
            """
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            vec2 add(vec2 a, vec2 b) { return a + b; }
            float add(float a, float b, float c) { return a + b + c; }
        """,
        )
    }

    fun `test function overload differing only in return type`() {
        doHighlightTest(
            """
            shader_type spatial;
            float get(float a) { return a; }
            int <error descr="Redefinition of 'get'">get</error>(float a) { return 1; }
        """,
        )
    }

    fun `test function overload differing only in precision`() {
        doHighlightTest(
            """
            shader_type spatial;
            float get(highp float a) { return a; }
            float <error descr="Redefinition of 'get'">get</error>(lowp float a) { return a; }
        """,
        )
    }

    fun `test function overload differing only in qualifier`() {
        doHighlightTest(
            """
            shader_type spatial;
            void set(in float a) {}
            void <error descr="Redefinition of 'set'">set</error>(inout float a) {}
        """,
        )
    }

    fun `test function overload with different array sizes`() {
        doHighlightTest(
            """
            shader_type spatial;
            float sum(float a[2]) { return a[0]; }
            float sum(float a[3]) { return a[0]; }
            float sum(float a) { return a; }
        """,
        )
    }

    fun `test function overload with array size on type and name`() {
        doHighlightTest(
            """
            shader_type spatial;
            float sum(float[2] a) { return a[0]; }
            float <error descr="Redefinition of 'sum'">sum</error>(float a[2]) { return a[0]; }
        """,
        )
    }

    fun `test function overload with different struct parameters`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct A { float x; };
            struct B { float x; };
            float get(A a) { return a.x; }
            float get(B b) { return b.x; }
            float <error descr="Redefinition of 'get'">get</error>(A other) { return other.x; }
        """,
        )
    }

    fun `test duplicate function in included file`() {
        myFixture.addFileToProject("math.gdshaderinc", "float add(float a, float b) { return a + b; }")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://math.gdshaderinc"
            float <error descr="Redefinition of 'add'">add</error>(float x, float y) { return x + y; }
        """,
        )
    }

    fun `test function overload of included function`() {
        myFixture.addFileToProject("math.gdshaderinc", "float add(float a, float b) { return a + b; }")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://math.gdshaderinc"
            vec2 add(vec2 a, vec2 b) { return a + b; }
        """,
        )
    }

    fun `test duplicate function in include after declaration`() {
        myFixture.addFileToProject("math.gdshaderinc", "float add(float a, float b) { return a + b; }")
        doHighlightTest(
            """
            shader_type spatial;
            float add(float x, float y) { return x + y; }
            #include "<error descr="Redefinition of 'add' in included file">res://math.gdshaderinc</error>"
        """,
        )
    }

    fun `test function overload in include after declaration`() {
        myFixture.addFileToProject("math.gdshaderinc", "float add(float a, float b) { return a + b; }")
        doHighlightTest(
            """
            shader_type spatial;
            vec2 add(vec2 x, vec2 y) { return x + y; }
            #include "res://math.gdshaderinc"
        """,
        )
    }

    fun `test duplicate function in nested include`() {
        myFixture.addFileToProject("inner.gdshaderinc", "float add(float a, float b) { return a + b; }")
        myFixture.addFileToProject("outer.gdshaderinc", "#include \"res://inner.gdshaderinc\"")
        doHighlightTest(
            """
            shader_type spatial;
            float add(float x, float y) { return x + y; }
            #include "<error descr="Redefinition of 'add' in included file">res://outer.gdshaderinc</error>"
        """,
        )
    }

    fun `test duplicate function between includes`() {
        myFixture.addFileToProject("a.gdshaderinc", "float add(float a, float b) { return a + b; }")
        myFixture.addFileToProject("b.gdshaderinc", "float add(float x, float y) { return x + y; }")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://a.gdshaderinc"
            #include "<error descr="Redefinition of 'add' in included file">res://b.gdshaderinc</error>"
        """,
        )
    }

    fun `test duplicate processing function in include after declaration`() {
        myFixture.addFileToProject("stage.gdshaderinc", "void fragment() {}")
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {}
            #include "<error descr="Redefinition of 'fragment' in included file">res://stage.gdshaderinc</error>"
        """,
        )
    }

    fun `test duplicate struct in included file`() {
        myFixture.addFileToProject("types.gdshaderinc", "struct Data { float x; };")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://types.gdshaderinc"
            struct <error descr="Redefinition of 'Data'">Data</error> { float y; };
        """,
        )
    }

    fun `test duplicate struct in include after declaration`() {
        myFixture.addFileToProject("types.gdshaderinc", "struct Data { float x; };")
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float y; };
            #include "<error descr="Redefinition of 'Data' in included file">res://types.gdshaderinc</error>"
        """,
        )
    }

    fun `test duplicate uniform in included file`() {
        myFixture.addFileToProject("params.gdshaderinc", "uniform float speed;")
        doHighlightTest(
            """
            shader_type spatial;
            #include "res://params.gdshaderinc"
            uniform float <error descr="Redefinition of 'speed'">speed</error>;
        """,
        )
    }

    fun `test duplicate constant in include after declaration`() {
        myFixture.addFileToProject("params.gdshaderinc", "const float SPEED = 1.0;")
        doHighlightTest(
            """
            shader_type spatial;
            uniform float SPEED;
            #include "<error descr="Redefinition of 'SPEED' in included file">res://params.gdshaderinc</error>"
        """,
        )
    }

    fun `test duplicate processing function with different parameters`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {}
            void <error descr="Redefinition of 'fragment'">fragment</error>(float x) {}
        """,
        )
    }

    fun `test processing function name of other shader type can be overloaded`() {
        doHighlightTest(
            """
            shader_type canvas_item;
            float sky() { return 0.0; }
            float sky(float a) { return a; }
        """,
        )
    }

    fun `test duplicate struct`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float x; };
            struct <error descr="Redefinition of 'Data'">Data</error> { float y; };
        """,
        )
    }

    fun `test no duplicate struct`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float x; };
            struct Info { float y; };
        """,
        )
    }

    fun `test duplicate struct member`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Data { float x; vec2 <error descr="Redefinition of 'x'">x</error>; float <error descr="Redefinition of 'x'">x</error>[2]; };
        """,
        )
    }

    fun `test same member name in different structs and globals`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float x;
            struct Data { float x; };
            struct Info { float x; };
        """,
        )
    }

    fun `test duplicate uniform`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float speed;
            uniform float <error descr="Redefinition of 'speed'">speed</error>;
        """,
        )
    }

    fun `test duplicate uniform and varying`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float value;
            varying float <error descr="Redefinition of 'value'">value</error>;
        """,
        )
    }

    fun `test duplicate uniform and constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float speed;
            const float <error descr="Redefinition of 'speed'">speed</error> = 1.0;
        """,
        )
    }

    fun `test no duplicate variables`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float speed;
            uniform float strength;
            const float MAX = 10.0;
        """,
        )
    }

    fun `test parameter shadows uniform`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float t = 1.0;
            void my_func(float <error descr="Redefinition of 't'">t</error>) {
            }
            """.trimIndent(),
        )
    }

    fun `test local variable shadows uniform`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float t = 1.0;
            void my_func() {
                float <error descr="Redefinition of 't'">t</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test local constant shadows uniform`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float t = 1.0;
            void my_func() {
                const float <error descr="Redefinition of 't'">t</error> = 1.0;
            }
            """.trimIndent(),
        )
    }

    fun `test duplicate inside constant declaration`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int x = 1, <error descr="Redefinition of 'x'">x</error> = 1;
            """.trimIndent(),
        )
    }

    fun `test duplicate inside parameter declaration`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func(int p, int <error descr="Redefinition of 'p'">p</error>) {
            }
            """.trimIndent(),
        )
    }

    fun `test duplicate inside same scope`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                int x = 1;
                int <error descr="Redefinition of 'x'">x</error> = 1;
            }
            """.trimIndent(),
        )
    }

    fun `test duplicate inside local variable declaration`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                int x = 1, <error descr="Redefinition of 'x'">x</error> = 1;
            }
            """.trimIndent(),
        )
    }

    fun `test local variable shadows parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func(float x) {
                float <error descr="Redefinition of 'x'">x</error> = 1.0;
            }
            """.trimIndent(),
        )
    }

    fun `test local variable shadows another local variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                float x = 1.0;
                {
                    float x = 2.0;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test local constant shadows local variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                float x = 1.0;
                {
                    const float x = 2.0;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test valid local variable initializer`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                vec3 v = vec3(1.0);
            }
            """.trimIndent(),
        )
    }

    fun `test invalid local variable initializer int to float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = <error descr="Cannot assign a value of type 'int' to type 'float'">1</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid local variable initializer bool to float`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = <error descr="Cannot assign a value of type 'bool' to type 'float'">true</error>;
            }
            """.trimIndent(),
        )
    }

    fun `test invalid local variable initializer struct to vector`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; float y; };
            void fragment() {
                vec3 v = <error descr="Cannot assign a value of type 'MyData' to type 'vec3'">MyData(1.0, 2.0)</error>;
            }
        """,
        )
    }

    fun `test invalid constant initializer`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float X = <error descr="Cannot assign a value of type 'int' to type 'float'">3</error>;
            """.trimIndent(),
        )
    }

    fun `test uniform numeric constant conversions`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int ANGLE = 180;
            uniform float spread = 180;
            uniform float unsigned_spread = 180u;
            uniform float negative_spread = -180;
            uniform float constant_spread = <error descr="Expected constant expression after '='.">ANGLE</error>;
            uniform float computed_spread = <error descr="Expected constant expression after '='.">(90 + 90)</error>;
            uniform uint zero = 0;
            uniform uint positive = 180;
            uniform int signed_max = 2147483647u;
            instance uniform float instance_spread = 180;
            """.trimIndent(),
        )
    }

    fun `test invalid uniform constant conversions`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform uint negative = <error descr="Can't convert constant to 'uint'.">-1</error>;
            uniform int overflow = <error descr="Can't convert constant to 'int'.">2147483648u</error>;
            uniform int count = <error descr="Can't convert constant to 'int'.">1.0</error>;
            uniform uint unsigned_count = <error descr="Can't convert constant to 'uint'.">1.0</error>;
            uniform float speed = <error descr="Can't convert constant to 'float'.">true</error>;
            uniform bool enabled = <error descr="Can't convert constant to 'bool'.">1</error>;
            uniform vec3 direction = <error descr="Can't convert constant to 'vec3'.">1</error>;
            uniform vec4 color = <error descr="Can't convert constant to 'vec4'.">vec3(1.0)</error>;
            """.trimIndent(),
        )
    }

    fun `test uniform non-constant initializer`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform int source;
            uniform float reference = <error descr="Expected constant expression after '='.">source</error>;
            uniform float component = <error descr="Expected constant expression after '='.">ivec2(1).x</error>;
            uniform vec2 vector = <error descr="Can't convert constant to 'vec2'.">ivec2(1)</error>;
            uniform vec3 valid_vector = vec3(1.0);
            """.trimIndent(),
        )
    }

    fun `test uniform constructor and unary reduction`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float parenthesized = (180);
            uniform float plus = +180;
            uniform float double_negative = -(-180);
            uniform highp float precise = 180;
            uniform vec3 negative_vector = -vec3(1.0);
            uniform vec2 converted_arguments = vec2(1, 2u);
            uniform vec4 nested = vec4(vec2(1.0), 0.0, 1.0);
            uniform mat2 diagonal = mat2(1.0);
            uniform mat2 columns = mat2(vec2(1.0, 0.0), vec2(0.0, 1.0));
            uniform bvec2 flags = bvec2(true);
            uniform uint wrapped = -1u;
            uniform float scalar_constructor = float(180);
            uniform vec2 mixed_vector = <error descr="Expected constant expression after '='.">vec2(ivec2(1))</error>;
            uniform uint negative_constructor = <error descr="Expected constant expression after '='.">uint(-1)</error>;
            uniform int truncated = <error descr="Expected constant expression after '='.">int(1.5)</error>;
            uniform float from_bool = <error descr="Expected constant expression after '='.">float(true)</error>;
            uniform mat3 from_matrix = <error descr="Expected constant expression after '='.">mat3(mat2(1.0))</error>;
            uniform bool inverted = <error descr="Expected constant expression after '='.">!true</error>;
            uniform int inverted_bits = <error descr="Expected constant expression after '='.">~0</error>;
            uniform float selected = <error descr="Expected constant expression after '='.">true ? 1.0 : 0.0</error>;
            uniform float called = <error descr="Expected constant expression after '='.">sin(1.0)</error>;
            """.trimIndent(),
        )
    }

    fun `test unknown preprocessor directive`() {
        doHighlightTest(
            """
            shader_type spatial;
            <error descr="Unknown preprocessor directive '#foo'">#foo</error>
        """,
        )
    }

    fun `test define directive is known`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define FOO
        """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

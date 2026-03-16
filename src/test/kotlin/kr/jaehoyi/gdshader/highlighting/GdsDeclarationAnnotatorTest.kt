package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsDeclarationAnnotatorTest : BasePlatformTestCase() {

    // === shader_type must come first ===

    fun `test shader_type first`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float x;
        """)
    }

    fun `test missing shader_type`() {
        doHighlightTest("""
            <error descr="Expected 'shader_type' before the first declaration">uniform float x;</error>
        """)
    }
    
    // === Empty struct ===

    fun `test empty struct`() {
        doHighlightTest("""
            shader_type spatial;
            <error descr="Empty structs are not allowed">struct Empty {};</error>
        """)
    }

    fun `test non-empty struct`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float x; };
        """)
    }

    // === Include file extension ===

    fun `test include gdshaderinc file`() {
        myFixture.addFileToProject("utils.gdshaderinc", "float add(float a, float b) { return a + b; }")
        doHighlightTest("""
            shader_type spatial;
            #include "res://utils.gdshaderinc"
        """)
    }

    fun `test include non-gdshaderinc file`() {
        doHighlightTest("""
            shader_type spatial;
            #include "<error descr="Only '.gdshaderinc' files can be included">res://other.gdshader</error>"
        """)
    }

    // === Include file existence ===

    fun `test include nonexistent file`() {
        doHighlightTest("""
            shader_type spatial;
            #include "<error descr="Cannot resolve file 'res://nonexistent.gdshaderinc'">res://nonexistent.gdshaderinc</error>"
        """)
    }

    fun `test include existing file`() {
        myFixture.addFileToProject("helpers.gdshaderinc", "// helper")
        doHighlightTest("""
            shader_type spatial;
            #include "res://helpers.gdshaderinc"
        """)
    }

    // === Double array size ===

    fun `test uniform single array size`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float arr[3];
        """)
    }

    fun `test uniform double array size`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float[3] arr<error descr="Array size is already defined">[2]</error>;
        """)
    }

    fun `test parameter double array size`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func(float[3] arr<error descr="Array size is already defined">[2]</error>) {}
        """)
    }

    fun `test struct member double array size`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float[3] x<error descr="Array size is already defined">[2]</error>; };
        """)
    }

    fun `test local variable double array size`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float[3] arr<error descr="Array size is already defined">[2]</error>;
            }
        """)
    }

    // === Duplicate declarations ===

    fun `test duplicate function`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            float <error descr="Redefinition of 'add'">add</error>(float x, float y) { return x + y; }
        """)
    }

    fun `test no duplicate function`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            float sub(float a, float b) { return a - b; }
        """)
    }

    fun `test duplicate struct`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float x; };
            struct <error descr="Redefinition of 'Data'">Data</error> { float y; };
        """)
    }

    fun `test no duplicate struct`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float x; };
            struct Info { float y; };
        """)
    }

    fun `test duplicate uniform`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float speed;
            uniform float <error descr="Redefinition of 'speed'">speed</error>;
        """)
    }

    fun `test duplicate uniform and varying`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float value;
            varying float <error descr="Redefinition of 'value'">value</error>;
        """)
    }

    fun `test duplicate uniform and constant`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float speed;
            const float <error descr="Redefinition of 'speed'">speed</error> = 1.0;
        """)
    }

    fun `test no duplicate variables`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float speed;
            uniform float strength;
            const float MAX = 10.0;
        """)
    }
    
    fun `test duplicate parameters`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float t = 1.0;
            void my_func(float <error descr="Redefinition of 't'">t</error>) {
            }
        """.trimIndent())
    }
    
    fun `test duplicate local variable`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float t = 1.0;
            void my_func() {
                float <error descr="Redefinition of 't'">t</error>;
            }
        """.trimIndent())
    }

    fun `test duplicate local constant`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float t = 1.0;
            void my_func() {
                const float <error descr="Redefinition of 't'">t</error> = 1.0;
            }
        """.trimIndent())
    }
    
    fun `test duplicate inside constant declaration`() {
        doHighlightTest("""
            shader_type spatial;
            const int x = 1, <error descr="Redefinition of 'x'">x</error> = 1;
        """.trimIndent())
    }
    
    fun `test duplicate inside parameter declaration`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func(int p, int <error descr="Redefinition of 'p'">p</error>) {
            }
        """.trimIndent())
    }
    
    fun `test duplicate inside same scope`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func() {
                int x = 1;
                int <error descr="Redefinition of 'x'">x</error> = 1;
            }
        """.trimIndent())
    }
    
    fun `test duplicate inside local variable declaration`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func() {
                int x = 1, <error descr="Redefinition of 'x'">x</error> = 1;
            }
        """.trimIndent())
    }

    fun `test local variable shadows parameter`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func(float x) {
                float <error descr="Redefinition of 'x'">x</error> = 1.0;
            }
        """.trimIndent())
    }

    // === Local variable shadowing (allowed) ===

    fun `test local variable shadows another local variable`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func() {
                float x = 1.0;
                {
                    float x = 2.0;
                }
            }
        """.trimIndent())
    }

    fun `test local constant shadows local variable`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func() {
                float x = 1.0;
                {
                    const float x = 2.0;
                }
            }
        """.trimIndent())
    }

    // === Initializer type mismatch ===

    fun `test valid local variable initializer`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                vec3 v = vec3(1.0);
            }
        """.trimIndent())
    }

    fun `test invalid local variable initializer int to float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = <error descr="Cannot assign a value of type 'int' to type 'float'">1</error>;
            }
        """.trimIndent())
    }

    fun `test invalid local variable initializer bool to float`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float x = <error descr="Cannot assign a value of type 'bool' to type 'float'">true</error>;
            }
        """.trimIndent())
    }

    fun `test invalid constant initializer`() {
        doHighlightTest("""
            shader_type spatial;
            const float X = <error descr="Cannot assign a value of type 'int' to type 'float'">3</error>;
        """.trimIndent())
    }

    fun `test invalid uniform default value`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float speed = <error descr="Cannot assign a value of type 'int' to type 'float'">1</error>;
        """.trimIndent())
    }

    // === Unknown preprocessor directive ===

    fun `test unknown preprocessor directive`() {
        doHighlightTest("""
            shader_type spatial;
            <error descr="Unknown preprocessor directive '#foo'">#foo</error>
        """)
    }

    fun `test known preprocessor directives`() {
        doHighlightTest("""
            shader_type spatial;
            #define FOO
        """)
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}
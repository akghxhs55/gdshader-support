package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsStatementAnnotatorTest : BasePlatformTestCase() {

    // === discard statement ===

    fun `test discard in fragment`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                discard;
            }
        """)
    }

    fun `test discard in light`() {
        doHighlightTest("""
            shader_type spatial;
            void light() {
                discard;
            }
        """)
    }

    fun `test discard in vertex`() {
        doHighlightTest("""
            shader_type spatial;
            void vertex() {
                <error descr="'discard' can only be used in 'fragment' or 'light' functions">discard;</error>
            }
        """)
    }

    fun `test discard in helper function`() {
        doHighlightTest("""
            shader_type spatial;
            void my_helper() {
                discard;
            }
        """)
    }

    // === return statement ===

    fun `test void function with return value`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func() {
                <error descr="'void' function cannot return a value">return 1.0;</error>
            }
        """)
    }

    fun `test void function with empty return`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func() {
                return;
            }
        """)
    }

    fun `test non-void function with empty return`() {
        doHighlightTest("""
            shader_type spatial;
            float my_func() {
                <error descr="Expected return with an expression of type 'float'">return;</error>
            }
        """)
    }

    fun `test non-void function with return value`() {
        doHighlightTest("""
            shader_type spatial;
            float my_func() {
                return 1.0;
            }
        """)
    }
    
    // === switch statement ===
    
    fun `test int type in switch expression`() {
        doHighlightTest("""
            shader_type spatial;
            float my_func() {
                int x = 1;
                switch (x) {
                }
            }
        """.trimIndent())
    }
    
    fun `test uint type in switch expression`() {
        doHighlightTest("""
            shader_type spatial;
            float my_func() {
                uint x = 1u;
                switch (x) {
                }
            }
        """.trimIndent())
    }
    
    fun `test float type in switch expression`() {
        doHighlightTest("""
            shader_type spatial;
            float my_func() {
                float x = 1.0;
                switch (<error descr="Expected an integer or unsigned integer expression">x</error>) {
                }
            }
        """.trimIndent())
    }
    
    // === control statements condition ===
    
    fun `test bool type in if statement condition`() {
        doHighlightTest("""
            shader_type spatial;
            bool my_func() {
                if (true) {
                }
            }
        """.trimIndent())
    }
    
    fun `test bool type in for statement condition`() {
        doHighlightTest("""
            shader_type spatial;
            bool my_func() {
                for (;true;) {
                }
            }
        """.trimIndent())
    }

    fun `test bool type in while statement condition`() {
        doHighlightTest("""
            shader_type spatial;
            bool my_func() {
                while (true) {
                }
            }
        """.trimIndent())
    }
    
    fun `test bool type in do-while statement condition`() {
        doHighlightTest("""
            shader_type spatial;
            bool my_func() {
                do {
                } while (true);
            }
        """.trimIndent())
    }

    // === return type mismatch ===

    fun `test return type mismatch int to float`() {
        doHighlightTest("""
            shader_type spatial;
            float my_func() {
                <error descr="Expected return with an expression of type 'float'">return 1;</error>
            }
        """.trimIndent())
    }

    fun `test return type mismatch vec2 to vec3`() {
        doHighlightTest("""
            shader_type spatial;
            vec3 my_func() {
                <error descr="Expected return with an expression of type 'vec3'">return vec2(1.0);</error>
            }
        """.trimIndent())
    }

    fun `test return correct type`() {
        doHighlightTest("""
            shader_type spatial;
            float my_func() {
                return 1.0;
            }
        """.trimIndent())
    }

    fun `test float type in if statement condition`() {
        doHighlightTest("""
            shader_type spatial;
            bool my_func() {
                if (<error descr="Expected a boolean expression">1.0</error>) {
                }
            }
        """.trimIndent())
    }

    // === break/continue ===

    fun `test break in for loop`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                for (int i = 0; i < 10; i++) {
                    break;
                }
            }
        """.trimIndent())
    }

    fun `test break in while loop`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                while (true) {
                    break;
                }
            }
        """.trimIndent())
    }

    fun `test break in switch`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int x = 1;
                switch (x) {
                    case 0:
                        break;
                }
            }
        """.trimIndent())
    }

    fun `test break outside loop`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                <error descr="'break' is not allowed outside of a loop or 'switch' statement">break;</error>
            }
        """.trimIndent())
    }

    fun `test continue in for loop`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                for (int i = 0; i < 10; i++) {
                    continue;
                }
            }
        """.trimIndent())
    }

    fun `test continue outside loop`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                <error descr="'continue' is not allowed outside of a loop">continue;</error>
            }
        """.trimIndent())
    }

    fun `test continue in switch`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                int x = 1;
                switch (x) {
                    case 0:
                        <error descr="'continue' is not allowed outside of a loop">continue;</error>
                }
            }
        """.trimIndent())
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

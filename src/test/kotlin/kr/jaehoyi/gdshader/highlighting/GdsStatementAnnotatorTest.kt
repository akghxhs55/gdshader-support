package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsStatementAnnotatorTest : BasePlatformTestCase() {
    // === discard statement ===

    fun `test discard in fragment`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                discard;
            }
        """,
        )
    }

    fun `test discard in light`() {
        doHighlightTest(
            """
            shader_type spatial;
            void light() {
                discard;
            }
        """,
        )
    }

    fun `test discard in vertex`() {
        doHighlightTest(
            """
            shader_type spatial;
            void vertex() {
                <error descr="'discard' cannot be used within the 'vertex' processor function.">discard;</error>
            }
        """,
        )
    }

    fun `test discard in helper function`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_helper() {
                discard;
            }
        """,
        )
    }

    // === return statement ===

    fun `test void function with return value`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                <error descr="'void' function cannot return a value">return 1.0;</error>
            }
        """,
        )
    }

    fun `test void function with empty return`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                return;
            }
        """,
        )
    }

    fun `test non-void function with empty return`() {
        doHighlightTest(
            """
            shader_type spatial;
            float my_func() {
                <error descr="Expected return with an expression of type 'float'">return;</error>
            }
        """,
        )
    }

    fun `test non-void function with return value`() {
        doHighlightTest(
            """
            shader_type spatial;
            float my_func() {
                return 1.0;
            }
        """,
        )
    }

    // === switch statement ===

    fun `test int type in switch expression`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                int x = 1;
                switch (x) {
                    case 0: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test uint type in switch expression`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                uint x = 1u;
                switch (x) {
                    case 0u: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test float type in switch expression`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                float x = 1.0;
                switch (<error descr="Expected an integer or unsigned integer expression">x</error>) {
                    case 0: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test switch with only default`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                int x = 1;
                switch (x) {
                    default: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test empty switch`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                int x = 1;
                switch (x) {
                <error descr="Expected a 'case' or 'default'">}</error>
            }
            """.trimIndent(),
        )
    }

    // === control statements condition ===

    fun `test bool type in if statement condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                if (true) {
                }
            }
            """.trimIndent(),
        )
    }

    fun `test bool type in for statement condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                for (;true;) {
                }
            }
            """.trimIndent(),
        )
    }

    fun `test comma lists in for statement`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                for (int i = 0, j = 0; i < 3, j < 3; i++, j++) {
                }
                for (int i = 0, j = 0; i < 3, <error descr="Expected a boolean expression">j</error>; i++) {
                }
            }
            """.trimIndent(),
        )
    }

    fun `test bool type in while statement condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                while (true) {
                }
            }
            """.trimIndent(),
        )
    }

    fun `test bool type in do-while statement condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                do {
                } while (true);
            }
            """.trimIndent(),
        )
    }

    // === return type mismatch ===

    fun `test return type mismatch int to float`() {
        doHighlightTest(
            """
            shader_type spatial;
            float my_func() {
                <error descr="Expected return with an expression of type 'float'">return 1;</error>
            }
            """.trimIndent(),
        )
    }

    fun `test return type mismatch vec2 to vec3`() {
        doHighlightTest(
            """
            shader_type spatial;
            vec3 my_func() {
                <error descr="Expected return with an expression of type 'vec3'">return vec2(1.0);</error>
            }
            """.trimIndent(),
        )
    }

    fun `test float type in if statement condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                if (<error descr="Expected a boolean expression">1.0</error>) {
                }
            }
            """.trimIndent(),
        )
    }

    // === break/continue ===

    fun `test break in for loop`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                for (int i = 0; i < 10; i++) {
                    break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test break in while loop`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                while (true) {
                    break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test break in switch`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int x = 1;
                switch (x) {
                    case 0:
                        break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test break outside loop`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                <error descr="'break' is not allowed outside of a loop or 'switch' statement">break;</error>
            }
            """.trimIndent(),
        )
    }

    fun `test continue in for loop`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                for (int i = 0; i < 10; i++) {
                    continue;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test continue outside loop`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                <error descr="'continue' is not allowed outside of a loop">continue;</error>
            }
            """.trimIndent(),
        )
    }

    fun `test continue in switch`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                int x = 1;
                switch (x) {
                    case 0:
                        <error descr="'continue' is not allowed outside of a loop">continue;</error>
                }
            }
            """.trimIndent(),
        )
    }

    // === array return type ===

    fun `test array return type`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct MyData { float x; };
            float[2] g() { float a[2]; return a; }
            highp float[2] h() { return { 1.0, 2.0 }; }
            MyData[2] s() { MyData a[2]; return a; }
            void fragment() {
                float b[2] = g();
                b = h();
                float x = g()[0];
                int n = g().length();
                MyData c[2] = s();
            }
            """.trimIndent(),
        )
    }

    fun `test array return type size mismatch`() {
        doHighlightTest(
            """
            shader_type spatial;
            float[3] g() {
                float a[2];
                <error descr="Expected return with an expression of type 'float[3]'">return a;</error>
            }
            float[2] h() {
                <error descr="Expected return with an expression of type 'float[2]'">return { 1.0, 2.0, 3.0 };</error>
            }
            """.trimIndent(),
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

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

    fun `test valid case labels`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int A = 2;
            const uint B = 2u;
            void my_func() {
                int x = 1;
                switch (x) {
                    case -1: break;
                    case 1: break;
                    case A: break;
                    case -A: break;
                    default: break;
                }
                uint y = 1u;
                switch (y) {
                    case 1u: break;
                    case B: break;
                }
                switch (x) {
                    case 1: switch (x) { case 1: break; } break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test duplicated case labels`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int A = 1;
            const int C = A + 1;
            void my_func() {
                int x = 1;
                const int L = 3;
                switch (x) {
                    case 1: break;
                    case <error descr="Duplicated case label: 1.">1</error>: break;
                    case <error descr="Duplicated case label: 1.">A</error>: break;
                    case <error descr="Duplicated case label: 1.">0x1</error>: break;
                    case C: break;
                    case <error descr="Duplicated case label: 2.">2</error>: break;
                    case L: break;
                    case <error descr="Duplicated case label: 3.">3</error>: break;
                    case -1: break;
                    case <error descr="Duplicated case label: -1.">-1</error>: break;
                }
                uint y = 1u;
                switch (y) {
                    case 1u: break;
                    case <error descr="Duplicated case label: 1.">1u</error>: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test duplicated default`() {
        doHighlightTest(
            """
            shader_type spatial;
            void my_func() {
                int x = 1;
                switch (x) {
                    default: break;
                    case 1: break;
                    <error descr="Default case must be defined only once.">default</error>: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test case label type mismatch`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int A = 1;
            const uint B = 1u;
            void my_func() {
                int x = 1;
                switch (x) {
                    case <error descr="Expected an integer constant.">1u</error>: break;
                    case <error descr="Expected an integer constant.">B</error>: break;
                    case <error descr="Expected an integer constant.">1.0</error>: break;
                }
                uint y = 1u;
                switch (y) {
                    case <error descr="Expected an unsigned integer constant.">1</error>: break;
                    case <error descr="Expected an unsigned integer constant.">A</error>: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test case label must be a literal or constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            const int ARR[2] = { 1, 2 };
            uniform int U;
            void my_func(const in int p) {
                int x = 1;
                int y = 1;
                switch (x) {
                    case <error descr="Expected an integer constant.">(1)</error>: break;
                    case <error descr="Expected an integer constant.">-(1)</error>: break;
                    case <error descr="Expected an integer constant.">+1</error>: break;
                    case <error descr="Expected an integer constant.">y</error>: break;
                    case <error descr="Expected an integer constant.">U</error>: break;
                    case <error descr="Expected an integer constant.">p</error>: break;
                    case <error descr="Expected an integer constant.">ARR</error>: break;
                    case 1 <error descr="Expected a ':'.">+</error> 1: break;
                }
            }
            """.trimIndent(),
        )
    }

    fun `test case label with macro`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define Y 3
            void my_func() {
                int x = 1;
                switch (x) {
                    case Y: break;
                    case <error descr="Duplicated case label: 3.">3</error>: break;
                }
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

    fun `test non-bool while and do-while conditions are not flagged`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                while (1) { break; }
                while (1.0) { break; }
                while (vec2(1.0)) { break; }
                do { break; } while (1);
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

    // === return in processing functions ===

    fun `test return in processing function`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                if (true) { <error descr="Using 'return' in the 'fragment' processor function is incorrect.">return;</error> }
            }
            void light() {
                <error descr="Using 'return' in the 'light' processor function is incorrect.">return 1.0;</error>
            }
        """,
        )
    }

    fun `test return in function named after another shader type's processing function`() {
        doHighlightTest(
            """
            shader_type spatial;
            void start() { return; }
            void helper() { return; }
            void fragment() { helper(); }
        """,
        )
    }

    fun `test return in particles processing function`() {
        doHighlightTest(
            """
            shader_type particles;
            void process() {
                <error descr="Using 'return' in the 'process' processor function is incorrect.">return;</error>
            }
        """,
        )
    }

    // === missing return ===

    fun `test non-void function without return`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f() {
                float x = 1.0;
            <error descr="Expected at least one 'return' statement in a non-void function.">}</error>
        """,
        )
    }

    fun `test non-void function with empty body`() {
        doHighlightTest(
            """
            shader_type spatial;
            vec3 f() {<error descr="Expected at least one 'return' statement in a non-void function.">}</error>
        """,
        )
    }

    fun `test void function without return`() {
        doHighlightTest(
            """
            shader_type spatial;
            void f() {
                float x = 1.0;
            }
        """,
        )
    }

    fun `test return only in nested control flow`() {
        doHighlightTest(
            """
            shader_type spatial;
            float a(bool b) { if (b) return 1.0; }
            float c(bool b) { if (b) { } else { return 1.0; } }
            float d() { while (false) { return 1.0; } }
            float e() { for (int i = 0; i < 1; i++) { return 1.0; } }
            float g(int i) { switch (i) { case 0: return 1.0; } }
            float h() { { return 1.0; } }
        """,
        )
    }

    fun `test code after return`() {
        doHighlightTest(
            """
            shader_type spatial;
            float f() {
                return 1.0;
                float y = 2.0;
            }
        """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

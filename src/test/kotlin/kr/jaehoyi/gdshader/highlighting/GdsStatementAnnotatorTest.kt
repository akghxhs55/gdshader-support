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
                <error descr="Void function must not return a value">return 1.0;</error>
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
                <error descr="Expected return value of type 'float'">return;</error>
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
                uint x = 1;
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

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}

package kr.jaehoyi.gdshader.highlighting

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration

class GdsConditionalDirectiveTest : BasePlatformTestCase() {
    fun `test disabled branch is not parsed`() {
        doHighlightTest(
            """
            shader_type spatial;
            #if 0
            this is not shader code {
            #endif
            void fragment() {}
            """,
        )
    }

    fun `test branches known from the file are evaluated`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define USE_RED
            #define LEVEL 2
            #ifdef USE_RED
            void f() {}
            #else
            void f() { not valid
            #endif
            #if LEVEL > 1 && defined(USE_RED)
            void g() {}
            #elif RENDERER_MOBILE == 1
            void g() { not valid
            #else
            void g() { not valid
            #endif
            #undef USE_RED
            #ifndef USE_RED
            void h() {}
            #endif
            """,
        )
        val functions = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionDeclaration::class.java)
        assertEquals(listOf("f", "g", "h"), functions.map { it.functionNameDecl.name })
    }

    fun `test unknown branches are parsed without redefinition errors`() {
        doHighlightTest(
            """
            shader_type spatial;
            #if CURRENT_RENDERER == RENDERER_COMPATIBILITY
            uniform float value;
            struct Data { float x; };
            float f() { return 0.0; }
            #elif defined(FROM_INCLUDE)
            uniform float value;
            struct Data { float x; };
            float f() { return 1.0; }
            #else
            uniform float value;
            struct Data { float x; };
            float f() { return 2.0; }
            #endif
            void fragment() {
            #ifdef FROM_INCLUDE
                float local = 1.0;
            #else
                float local = 2.0;
            #endif
                float <error descr="Redefinition of 'local'">local</error> = 3.0;
            }
            """,
        )
    }

    fun `test unmatched directives`() {
        doHighlightTest(
            """
            shader_type spatial;
            <error descr="Unmatched 'endif' directive.">#endif</error>
            <error descr="Unmatched 'else' directive.">#else</error>
            #ifdef A
            #else
            <error descr="Unmatched 'elif' directive.">#elif B</error>
            #endif
            <error descr="Unmatched conditional statement.">#ifdef C</error>
            """,
        )
    }

    fun `test error directive in active code`() {
        doHighlightTest(
            """
            shader_type spatial;
            #if 0
            #error disabled
            #endif
            #ifndef FROM_INCLUDE
            #error maybe disabled
            #endif
            <error descr="Missing feature">#error Missing feature</error>
            """,
        )
    }

    fun `test macro redefinition`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define A 1
            <error descr="Macro redefinition.">#define A 1</error>
            #undef A
            #define A 2
            #ifdef FROM_INCLUDE
            #define B 1
            #else
            #define B 2
            #endif
            <error descr="Macro redefinition.">#define CURRENT_RENDERER 1</error>
            <error descr="Cannot use 'undef' on built-in define.">#undef RENDERER_MOBILE</error>
            """,
        )
    }

    fun `test object macro as type`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define REAL float
            uniform REAL u = 1.0;
            REAL half_of(REAL v) { return v * 0.5; }
            void fragment() {
                REAL x = half_of(u);
                REAL y = <error descr="Cannot assign a value of type 'bool' to type 'float'">true</error>;
            }
            """,
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code.trimIndent())
        myFixture.checkHighlighting(false, false, true)
    }
}

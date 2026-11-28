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
            #elif CURRENT_RENDERER == RENDERER_MOBILE
            uniform float value;
            struct Data { float x; };
            float f() { return 1.0; }
            #else
            uniform float value;
            struct Data { float x; };
            float f() { return 2.0; }
            #endif
            void fragment() {
            #if CURRENT_RENDERER == RENDERER_MOBILE
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
            #if CURRENT_RENDERER == RENDERER_MOBILE
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
            #if CURRENT_RENDERER == RENDERER_MOBILE
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

    fun `test define continued on the next line`() {
        doHighlightTest(
            """
            shader_type spatial;
            #define SCALE(x) \
                ((x) * 2.0)
            #define ENABLED \
                1
            #if ENABLED
            void f() {}
            #else
            void f() { not valid
            #endif
            void fragment() { float y = SCALE(1.0); }
            """,
        )
    }

    fun `test render modes in exclusive branches`() {
        val code =
            """
            shader_type canvas_item;
            #if defined(BLEND_ADD)
            render_mode blend_add;
            #elif defined(BLEND_MUL)
            render_mode blend_mul;
            #endif
            void fragment() {}
            """
        doHighlightTest(code)
        doHighlightTest(code, "test_include.gdshaderinc")
    }

    fun `test ifdef and ifndef of the same macro are exclusive`() {
        val code =
            """
            shader_type spatial;
            #ifdef PER_VERTEX
            varying vec4 v_tint;
            #endif
            #if !defined(PER_VERTEX)
            uniform float scale;
            #endif
            #if defined PER_VERTEX
            uniform float scale;
            #endif
            void fragment() {
            #ifndef PER_VERTEX
                vec4 v_tint = vec4(1.0);
            #endif
                ALBEDO = v_tint.rgb;
            }
            """
        doHighlightTest(code)
        doHighlightTest(code, "test_include.gdshaderinc")
    }

    fun `test else branch is exclusive with the same macro in another chain`() {
        doHighlightTest(
            """
            #ifdef A
            #else
            uniform float x;
            #endif
            #ifdef A
            uniform float x;
            #endif
            #ifndef A
            uniform float <error descr="Redefinition of 'x'">x</error>;
            #endif
            """,
            "test_include.gdshaderinc",
        )
    }

    fun `test macros not mentioned in a shader are undefined`() {
        doHighlightTest(
            """
            shader_type spatial;
            render_mode unshaded
            #if defined(NO_DEPTH)
            , depth_test_disabled;
            #else
            ;
            #endif
            #ifdef GL_ES
            precision highp float;
            #endif
            #ifdef A
            uniform float x;
            #endif
            #ifdef B
            uniform float x;
            #endif
            void fragment() {}
            """,
        )
    }

    fun `test macros stay unknown in include files and after an include`() {
        myFixture.addFileToProject("defs.gdshaderinc", "")
        val code =
            """
            #ifdef A
            uniform float x;
            #endif
            #ifdef B
            uniform float <error descr="Redefinition of 'x'">x</error>;
            #endif
            #if MODE == 1
            #endif
            """
        doHighlightTest(code, "test_include.gdshaderinc")
        doHighlightTest("shader_type spatial;\n#include \"res://defs.gdshaderinc\"\n" + code.trimIndent())
    }

    fun `test undefined name in condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            <error descr="Condition evaluation error.">#if MODE == 1</error>
            #endif
            <error descr="Unexpected character.">#if MODE == "abc"</error>
            #endif
            <error descr="Unexpected character.">#if 'a'</error>
            #endif
            <error descr="Condition evaluation error.">#if defined(MODE) && MODE > 1</error>
            #endif
            #if 1
            <error descr="Condition evaluation error.">#elif MODE</error>
            #endif
            #define M 1
            #undef M
            <error descr="Condition evaluation error.">#if M</error>
            #endif
            #define N MODE
            <error descr="Condition evaluation error.">#if N</error>
            #endif
            #if 0
            #if MODE
            #endif
            #endif
            #if defined(MODE) || !defined(M)
            #endif
            """,
        )
    }

    fun `test float literal in condition`() {
        doHighlightTest(
            """
            shader_type spatial;
            <error descr="Floating-point number operations are not supported by the shader preprocessor.">#if 1.0</error>
            void f() {}
            #endif
            <error descr="Floating-point number operations are not supported by the shader preprocessor.">#if MODE > 1e3</error>
            #endif
            #define SCALE 1.5f
            <error descr="Floating-point number operations are not supported by the shader preprocessor.">#if SCALE > 1</error>
            #endif
            #if 0
            <error descr="Floating-point number operations are not supported by the shader preprocessor.">#elif 2.0</error>
            #endif
            #if defined(SCALE) && 0x1E > 1
            #endif
            #if 0
            #if 1.0
            #endif
            #endif
            """,
        )
        doHighlightTest(
            """
            <error descr="Floating-point number operations are not supported by the shader preprocessor.">#if FROM_INCLUDE > 0.5</error>
            #endif
            """,
            "test_include.gdshaderinc",
        )
    }

    private fun doHighlightTest(
        code: String,
        fileName: String = "test_shader.gdshader",
    ) {
        myFixture.configureByText(fileName, code.trimIndent())
        myFixture.checkHighlighting(false, false, true)
    }
}

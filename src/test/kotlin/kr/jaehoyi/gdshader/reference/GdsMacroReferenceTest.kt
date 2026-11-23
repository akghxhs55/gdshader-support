package kr.jaehoyi.gdshader.reference

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FloatType
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.VectorType
import kr.jaehoyi.gdshader.psi.GdsConstantEvaluator
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement
import kr.jaehoyi.gdshader.resolve.GdsPreprocessorDefinitions

class GdsMacroReferenceTest : BasePlatformTestCase() {
    fun `test parse object-like macro strips comments`() {
        configure(
            """
            #define TINT vec3(1.0, /* r */ 0.5, 0.0) // warm
            void fragment() { vec3 c = TINT; }
            """,
        )
        val macro = visibleMacros().single()
        assertEquals("TINT", macro.name)
        assertNull(macro.parameters)
        assertEquals("vec3(1.0, 0.5, 0.0)", macro.bodyText)
    }

    fun `test parse function-like macro`() {
        configure(
            """
            #define SQ(x) ((x) * (x))
            void fragment() { float y = SQ(2.0); }
            """,
        )
        val macro = visibleMacros().single()
        assertEquals(listOf("x"), macro.parameters)
        assertEquals("((a + 1.0) * (a + 1.0))", macro.substitute(listOf("a + 1.0")))
        assertNull(macro.substitute(emptyList()))
    }

    fun `test space before parenthesis makes object-like macro`() {
        configure(
            """
            #define PAREN (1.0)
            void fragment() { float y = PAREN; }
            """,
        )
        val macro = visibleMacros().single()
        assertNull(macro.parameters)
        assertEquals("(1.0)", macro.bodyText)
    }

    fun `test undef hides macro`() {
        configure(
            """
            #define A 1
            #undef A
            #define B 2
            void fragment() { int x = B; }
            """,
        )
        assertEquals(listOf("B"), visibleMacros().map { it.name })
    }

    fun `test redefinition wins`() {
        configure(
            """
            #define A 1
            #define A 2.0
            void fragment() { float x = A; }
            """,
        )
        assertEquals("2.0", visibleMacros().single().bodyText)
    }

    fun `test included macros are visible only after include`() {
        myFixture.addFileToProject("defs.gdshaderinc", "#define FROM_INCLUDE 1")
        myFixture.configureByText(
            "main.gdshader",
            """
            shader_type spatial;
            const int BEFORE = 0;
            #include "res://defs.gdshaderinc"
            void fragment() { int x = FROM_INCLUDE; }
            """.trimIndent(),
        )
        val file = myFixture.file
        val before = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java).single()
        assertEquals(listOf("FROM_INCLUDE"), GdsPreprocessorDefinitions.getVisibleMacros(before).map { it.name })

        val constant = file.text.indexOf("BEFORE")
        val early = file.findElementAt(constant)!!
        assertEmpty(GdsPreprocessorDefinitions.getVisibleMacros(early))
    }

    fun `test reference resolves to macro name`() {
        configure(
            """
            #define TINT vec3(1.0)
            void fragment() { vec3 c = TI<caret>NT; }
            """,
        )
        val target = myFixture.elementAtCaret
        assertInstanceOf(target, GdsMacroElement::class.java)
        assertEquals("TINT", (target as GdsMacroElement).name)
        assertEquals(myFixture.file.text.indexOf("TINT"), target.textOffset)
    }

    fun `test function-like macro call resolves`() {
        configure(
            """
            #define SQ(x) ((x) * (x))
            void fragment() { float y = S<caret>Q(2.0); }
            """,
        )
        assertInstanceOf(myFixture.elementAtCaret, GdsMacroElement::class.java)
    }

    fun `test rename macro from usage`() {
        configure(
            """
            #define TINT vec3(1.0)
            void fragment() { vec3 c = TI<caret>NT; vec3 d = TINT * 2.0; }
            """,
        )
        myFixture.renameElementAtCaret("COLOR_TINT")
        myFixture.checkResult(
            """
            shader_type spatial;
            #define COLOR_TINT vec3(1.0)
            void fragment() { vec3 c = COLOR_TINT; vec3 d = COLOR_TINT * 2.0; }
            """.trimIndent(),
        )
    }

    fun `test infer object-like macro type`() {
        configure(
            """
            #define TINT vec3(1.0)
            void fragment() { vec3 c = TINT; }
            """,
        )
        assertEquals(VectorType.VEC3, initializerType("c"))
    }

    fun `test infer function-like macro type with argument from use site`() {
        configure(
            """
            #define SQ(x) ((x) * (x))
            void fragment() {
                vec2 v = vec2(1.0);
                vec2 y = SQ(v);
            }
            """,
        )
        assertEquals(VectorType.VEC2, initializerType("y"))
    }

    fun `test infer nested macro and builtin`() {
        configure(
            """
            #define RADIUS 2.0
            #define AREA (PI * RADIUS * RADIUS)
            void fragment() { float a = AREA; }
            """,
        )
        assertEquals(FloatType.DEFAULT, initializerType("a"))
    }

    fun `test self-referencing macro does not recurse forever`() {
        configure(
            """
            #define LOOP (LOOP + 1)
            void fragment() { int x = LOOP; }
            """,
        )
        assertNull(initializerType("x"))
    }

    fun `test non-expression macro body has no type`() {
        configure(
            """
            #define KEYWORD uniform float
            void fragment() { float x = KEYWORD; }
            """,
        )
        assertNull(initializerType("x"))
    }

    fun `test evaluate function-like macro`() {
        configure(
            """
            #define DOUBLE(x) ((x) * 2)
            void fragment() { int x = DOUBLE(3); }
            """,
        )
        assertEquals(6, initializerValue("x"))
        assertEquals(IntType, initializerType("x"))
    }

    fun `test evaluate macro referring to later macro`() {
        configure(
            """
            #define A (B + 1)
            #define B 2
            void fragment() { int x = A; }
            """,
        )
        assertEquals(3, initializerValue("x"))
    }

    private fun configure(body: String) {
        myFixture.configureByText("test.gdshader", "shader_type spatial;\n" + body.trimIndent())
    }

    private fun visibleMacros(): List<GdsPreprocessorDefinitions.GdsMacro> {
        val declarator = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsLocalVariableDeclarator::class.java).last()
        return GdsPreprocessorDefinitions.getVisibleMacros(declarator)
    }

    private fun initializer(name: String) =
        PsiTreeUtil
            .findChildrenOfType(myFixture.file, GdsLocalVariableDeclarator::class.java)
            .single { it.variableNameDecl.text == name }
            .initializer!!
            .expression!!

    private fun initializerType(name: String): DataType? = GdsExpressionTypeInference.inferType(initializer(name))

    private fun initializerValue(name: String): Any? = GdsConstantEvaluator.evaluate(initializer(name))
}

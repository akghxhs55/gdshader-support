package kr.jaehoyi.gdshader.inference

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.*

class GdsConstantEvaluatorTest : BasePlatformTestCase() {
    private fun evaluateExpression(expression: String): Any? {
        val code =
            """
            shader_type spatial;

            void fragment() {
                int x = $expression;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        // Find the initializer expression
        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val expr = requireNotNull(declarator?.initializer?.expression) { "Initializer not found: $expression" }

        return GdsConstantEvaluator.evaluate(expr)
    }

    private fun evaluateAsIntExpression(expression: String): Int? {
        val code =
            """
            shader_type spatial;

            void fragment() {
                int x = $expression;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val expr = requireNotNull(declarator?.initializer?.expression) { "Initializer not found: $expression" }

        return GdsConstantEvaluator.evaluateAsInt(expr)
    }

    // ===== Literal Tests =====

    fun `test int literal`() {
        assertEquals(42, evaluateExpression("42"))
    }

    fun `test negative int literal`() {
        assertEquals(-10, evaluateExpression("-10"))
    }

    fun `test hex literal`() {
        assertEquals(255, evaluateExpression("0xFF"))
    }

    fun `test uint literal`() {
        assertEquals(42L, evaluateExpression("42u"))
    }

    fun `test int literal with leading zero is decimal`() {
        assertEquals(10, evaluateExpression("010"))
    }

    fun `test int literal with leading zero and non-octal digits`() {
        assertEquals(8, evaluateExpression("08"))
        assertEquals(9, evaluateExpression("09"))
    }

    fun `test uint literal with leading zero is decimal`() {
        assertEquals(10L, evaluateExpression("010u"))
        assertEquals(9L, evaluateExpression("09u"))
    }

    fun `test float literal`() {
        assertEquals(3.14f, evaluateExpression("3.14"))
    }

    fun `test float literal with f suffix`() {
        assertEquals(2.5f, evaluateExpression("2.5f"))
    }

    fun `test true literal`() {
        assertEquals(true, evaluateExpression("true"))
    }

    fun `test false literal`() {
        assertEquals(false, evaluateExpression("false"))
    }

    // ===== Arithmetic Tests =====

    fun `test addition`() {
        assertEquals(5, evaluateExpression("2 + 3"))
    }

    fun `test subtraction`() {
        assertEquals(7, evaluateExpression("10 - 3"))
    }

    fun `test multiplication`() {
        assertEquals(12, evaluateExpression("3 * 4"))
    }

    fun `test division`() {
        assertEquals(5, evaluateExpression("15 / 3"))
    }

    fun `test modulo`() {
        assertEquals(1, evaluateExpression("10 % 3"))
    }

    fun `test complex arithmetic`() {
        assertEquals(14, evaluateExpression("2 + 3 * 4"))
    }

    fun `test parentheses`() {
        assertEquals(20, evaluateExpression("(2 + 3) * 4"))
    }

    fun `test float arithmetic`() {
        assertEquals(5.5f, evaluateExpression("2.5 + 3.0"))
    }

    // ===== Bitwise Tests =====

    fun `test bitwise and`() {
        assertEquals(4, evaluateExpression("6 & 5"))
    }

    fun `test bitwise or`() {
        assertEquals(7, evaluateExpression("6 | 5"))
    }

    fun `test bitwise xor`() {
        assertEquals(3, evaluateExpression("6 ^ 5"))
    }

    fun `test bitwise not`() {
        assertEquals(-1, evaluateExpression("~0"))
    }

    fun `test left shift`() {
        assertEquals(8, evaluateExpression("1 << 3"))
    }

    fun `test right shift`() {
        assertEquals(2, evaluateExpression("8 >> 2"))
    }

    // ===== uint Wrap-Around Tests =====

    fun `test uint addition wraps around`() {
        assertEquals(0L, evaluateExpression("0xFFFFFFFFu + 1u"))
    }

    fun `test uint subtraction wraps around`() {
        assertEquals(4294967295L, evaluateExpression("0u - 1u"))
    }

    fun `test uint multiplication wraps around`() {
        assertEquals(4294967294L, evaluateExpression("0xFFFFFFFFu * 2u"))
        assertEquals(1L, evaluateExpression("0xFFFFFFFFu * 0xFFFFFFFFu"))
    }

    fun `test uint bitwise not wraps around`() {
        assertEquals(4294967295L, evaluateExpression("~0u"))
    }

    fun `test uint negation wraps around`() {
        assertEquals(4294967295L, evaluateExpression("-1u"))
    }

    fun `test uint left shift wraps around`() {
        assertEquals(4294967280L, evaluateExpression("0xFFFFFFFFu << 4u"))
    }

    fun `test uint right shift`() {
        assertEquals(268435455L, evaluateExpression("0xFFFFFFFFu >> 4u"))
    }

    fun `test wrapped uint as array size`() {
        assertEquals(1, evaluateAsIntExpression("0xFFFFFFFFu + 2u"))
    }

    // ===== Shift Range Tests =====

    fun `test shift by 31 is allowed`() {
        assertEquals(Int.MIN_VALUE, evaluateExpression("1 << 31"))
        assertEquals(2147483648L, evaluateExpression("1u << 31u"))
    }

    fun `test shift by 32 or more returns null`() {
        assertNull(evaluateExpression("1 << 32"))
        assertNull(evaluateExpression("1u << 32u"))
        assertNull(evaluateExpression("8 >> 32"))
        assertNull(evaluateExpression("8u >> 40u"))
    }

    fun `test shift by negative amount returns null`() {
        assertNull(evaluateExpression("1 << -1"))
    }

    fun `test shift with mixed int and uint returns null`() {
        assertNull(evaluateExpression("1u << 4"))
        assertNull(evaluateExpression("1 << 4u"))
        assertNull(evaluateExpression("0xFFu >> 4"))
        assertNull(evaluateExpression("255 >> 4u"))
    }

    fun `test shift with non-integer operand returns null`() {
        assertNull(evaluateExpression("1.0 << 2"))
        assertNull(evaluateExpression("1 << 2.0"))
        assertNull(evaluateExpression("true << 1"))
    }

    // ===== Mixed int/uint Tests =====

    fun `test arithmetic with mixed int and uint returns null`() {
        assertNull(evaluateExpression("1u + 1"))
        assertNull(evaluateExpression("1 - 1u"))
        assertNull(evaluateExpression("2u * 3"))
        assertNull(evaluateExpression("6 / 2u"))
        assertNull(evaluateExpression("7u % 2"))
    }

    fun `test bitwise with mixed int and uint returns null`() {
        assertNull(evaluateExpression("6u & 5"))
        assertNull(evaluateExpression("6 | 5u"))
        assertNull(evaluateExpression("6u ^ 5"))
    }

    fun `test comparison with mixed int and uint returns null`() {
        assertNull(evaluateExpression("1u < 2"))
        assertNull(evaluateExpression("2 >= 1u"))
        assertNull(evaluateExpression("1u == 1"))
        assertNull(evaluateExpression("1 != 2u"))
    }

    fun `test uint operations without mixing`() {
        assertEquals(5L, evaluateExpression("2u + 3u"))
        assertEquals(1L, evaluateExpression("7u % 2u"))
        assertEquals(4L, evaluateExpression("6u & 5u"))
        assertEquals(true, evaluateExpression("1u < 2u"))
        assertEquals(true, evaluateExpression("0xFFFFFFFFu == 4294967295u"))
        assertEquals(false, evaluateExpression("1u != 1u"))
    }

    // ===== Comparison Tests =====

    fun `test less than`() {
        assertEquals(true, evaluateExpression("1 < 2"))
    }

    fun `test greater than`() {
        assertEquals(false, evaluateExpression("1 > 2"))
    }

    fun `test less than or equal`() {
        assertEquals(true, evaluateExpression("2 <= 2"))
    }

    fun `test greater than or equal`() {
        assertEquals(true, evaluateExpression("3 >= 2"))
    }

    fun `test equality`() {
        assertEquals(true, evaluateExpression("5 == 5"))
    }

    fun `test inequality`() {
        assertEquals(true, evaluateExpression("5 != 3"))
    }

    // ===== Logical Tests =====

    fun `test logical and`() {
        assertEquals(false, evaluateExpression("true && false"))
    }

    fun `test logical or`() {
        assertEquals(true, evaluateExpression("true || false"))
    }

    fun `test logical not`() {
        assertEquals(false, evaluateExpression("!true"))
    }

    // ===== Conditional Expression Tests =====

    fun `test ternary true`() {
        assertEquals(1, evaluateExpression("true ? 1 : 2"))
    }

    fun `test ternary false`() {
        assertEquals(2, evaluateExpression("false ? 1 : 2"))
    }

    // ===== Constructor Tests =====

    fun `test int constructor from float`() {
        assertEquals(3, evaluateExpression("int(3.7)"))
    }

    fun `test float constructor from int`() {
        assertEquals(5.0f, evaluateExpression("float(5)"))
    }

    fun `test bool constructor from int`() {
        assertEquals(true, evaluateExpression("bool(1)"))
    }

    fun `test bool constructor from zero`() {
        assertEquals(false, evaluateExpression("bool(0)"))
    }

    // ===== Const Variable Reference Tests =====

    fun `test const variable reference`() {
        val code =
            """
            shader_type spatial;

            const int SIZE = 10;

            void fragment() {
                int x = SIZE;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val initializer = declarator?.initializer
        val expr = initializer?.expression

        assertEquals(10, GdsConstantEvaluator.evaluate(requireNotNull(expr)))
    }

    fun `test const variable in expression`() {
        val code =
            """
            shader_type spatial;

            const int SIZE = 10;

            void fragment() {
                int x = SIZE + 5;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val initializer = declarator?.initializer
        val expr = initializer?.expression

        assertEquals(15, GdsConstantEvaluator.evaluate(requireNotNull(expr)))
    }

    fun `test chained const references`() {
        val code =
            """
            shader_type spatial;

            const int A = 5;
            const int B = A + 3;

            void fragment() {
                int x = B;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val initializer = declarator?.initializer
        val expr = initializer?.expression

        assertEquals(8, GdsConstantEvaluator.evaluate(requireNotNull(expr)))
    }

    fun `test literal define reference`() {
        val code =
            """
            shader_type spatial;

            #define EPS 0.01

            void fragment() {
                float x = EPS;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val initializer = declarator?.initializer
        val expr = initializer?.expression

        assertEquals(0.01f, GdsConstantEvaluator.evaluate(requireNotNull(expr)))
    }

    fun `test literal define in expression`() {
        val code =
            """
            shader_type spatial;

            #define SIZE 4

            void fragment() {
                int x = SIZE * 2;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val initializer = declarator?.initializer
        val expr = initializer?.expression

        assertEquals(8, GdsConstantEvaluator.evaluate(requireNotNull(expr)))
    }

    // ===== evaluateAsInt Tests =====

    fun `test evaluateAsInt with int`() {
        assertEquals(42, evaluateAsIntExpression("42"))
    }

    fun `test evaluateAsInt with float`() {
        assertEquals(3, evaluateAsIntExpression("3.7"))
    }

    fun `test evaluateAsInt with expression`() {
        assertEquals(10, evaluateAsIntExpression("2 + 8"))
    }

    // ===== Division by Zero Tests =====

    fun `test int division by zero returns null`() {
        assertNull(evaluateExpression("10 / 0"))
    }

    fun `test float division by zero returns null`() {
        assertNull(evaluateExpression("10.0 / 0.0"))
    }

    fun `test modulo by zero returns null`() {
        assertNull(evaluateExpression("10 % 0"))
    }

    // ===== Non-Constant Expression Tests =====

    fun `test non-const variable returns null`() {
        val code =
            """
            shader_type spatial;

            void fragment() {
                int y = 5;
                int x = y;
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val initializer = declarator?.initializer
        val expr = initializer?.expression

        assertNull(GdsConstantEvaluator.evaluate(requireNotNull(expr)))
    }
}

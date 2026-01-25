package kr.jaehoyi.gdshader.inference

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.*

class GdsConstantEvaluatorTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"

    private fun evaluateExpression(expression: String): Any? {
        val code = """
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
        val initializer = declarator?.initializer ?: return null
        val expr = initializer.expression ?: return null

        return GdsConstantEvaluator.evaluate(expr)
    }

    private fun evaluateAsIntExpression(expression: String): Int? {
        val code = """
            shader_type spatial;

            void fragment() {
                int x = $expression;
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val file = myFixture.file

        val declarators = PsiTreeUtil.findChildrenOfType(file, GdsLocalVariableDeclarator::class.java)
        val declarator = declarators.find { it.variableNameDecl.text == "x" }
        val initializer = declarator?.initializer ?: return null
        val expr = initializer.expression ?: return null

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
        val code = """
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

        assertEquals(10, expr?.let { GdsConstantEvaluator.evaluate(it) })
    }

    fun `test const variable in expression`() {
        val code = """
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

        assertEquals(15, expr?.let { GdsConstantEvaluator.evaluate(it) })
    }

    fun `test chained const references`() {
        val code = """
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

        assertEquals(8, expr?.let { GdsConstantEvaluator.evaluate(it) })
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
        val code = """
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

        assertNull(expr?.let { GdsConstantEvaluator.evaluate(it) })
    }
}

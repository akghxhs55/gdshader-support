package kr.jaehoyi.gdshader.inference

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*

class GdsExpressionTypeInferenceTest : BasePlatformTestCase() {

    fun `test bool literal true`() {
        doLiteralTest("true", BoolType)
    }

    fun `test bool literal false`() {
        doLiteralTest("false", BoolType)
    }

    fun `test float literal`() {
        doLiteralTest("1.0", FloatType.DEFAULT)
    }

    fun `test int literal`() {
        doLiteralTest("42", IntType)
    }

    fun `test uint literal`() {
        doLiteralTest("42u", UIntType)
    }

    fun `test vec3 constructor`() {
        doConstructorTest("vec3(1.0, 2.0, 3.0)", VectorType.VEC3)
    }

    fun `test vec4 constructor`() {
        doConstructorTest("vec4(1.0)", VectorType.VEC4)
    }

    fun `test mat4 constructor`() {
        doConstructorTest("mat4(1.0)", MatrixType.MAT4)
    }

    fun `test ivec2 constructor`() {
        doConstructorTest("ivec2(1, 2)", VectorType.IVEC2)
    }

    fun `test array constructor`() {
        val code = """
            shader_type spatial;
            void fragment() {
                int arr[3] = int[3](1, 2, 3);
                int a = <caret>arr[0];
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val file = myFixture.file
        val functionCalls = PsiTreeUtil.findChildrenOfType(file, GdsFunctionCall::class.java)
        val arrayConstructor = functionCalls.find { it.text.startsWith("int[3]") }

        val nonNullArrayConstructor = requireNotNull(arrayConstructor) { "Should find array constructor" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullArrayConstructor)
        assertNotNull("Should infer type", inferredType)
        assertTrue("Should be ArrayType", inferredType is ArrayType)

        val arrayType = inferredType as ArrayType
        assertEquals(IntType, arrayType.elementType)
        assertEquals(3, arrayType.containerSize)
    }

    fun `test sin float`() {
        doBuiltinFunctionTest("sin(1.0)", FloatType.DEFAULT)
    }

    fun `test sin vec3`() {
        doBuiltinFunctionTest("sin(vec3(1.0))", VectorType.VEC3)
    }

    fun `test length vec3`() {
        doBuiltinFunctionTest("length(vec3(1.0, 2.0, 3.0))", FloatType.DEFAULT)
    }

    fun `test dot vec3`() {
        doBuiltinFunctionTest("dot(vec3(1.0), vec3(2.0))", FloatType.DEFAULT)
    }

    fun `test cross vec3`() {
        doBuiltinFunctionTest("cross(vec3(1.0), vec3(2.0))", VectorType.VEC3)
    }

    fun `test normalize vec3`() {
        doBuiltinFunctionTest("normalize(vec3(1.0, 2.0, 3.0))", VectorType.VEC3)
    }

    fun `test mix float`() {
        doBuiltinFunctionTest("mix(0.0, 1.0, 0.5)", FloatType.DEFAULT)
    }

    fun `test mix vec3`() {
        doBuiltinFunctionTest("mix(vec3(0.0), vec3(1.0), 0.5)", VectorType.VEC3)
    }

    fun `test clamp float`() {
        doBuiltinFunctionTest("clamp(0.5, 0.0, 1.0)", FloatType.DEFAULT)
    }

    fun `test abs int`() {
        doBuiltinFunctionTest("abs(-5)", IntType)
    }

    fun `test abs vec3`() {
        doBuiltinFunctionTest("abs(vec3(-1.0))", VectorType.VEC3)
    }

    fun `test local variable ref`() {
        val code = """
            shader_type spatial;
            void fragment() {
                float my_var = 1.0;
                float b = <caret>my_var;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val element = requireNotNull(findVariableRefAtCaret()) { "Should find variable reference" }

        val inferredType = GdsExpressionTypeInference.inferType(element)
        assertEquals(FloatType.DEFAULT, inferredType)
    }

    fun `test uniform variable ref`() {
        val code = """
            shader_type spatial;
            uniform vec3 u_color;
            void fragment() {
                vec3 c = <caret>u_color;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val element = requireNotNull(findVariableRefAtCaret()) { "Should find variable reference" }

        val inferredType = GdsExpressionTypeInference.inferType(element)
        assertEquals(VectorType.VEC3, inferredType)
    }

    fun `test parameter ref`() {
        val code = """
            shader_type spatial;
            float my_func(mat4 transform) {
                mat4 t = <caret>transform;
                return 1.0;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val element = requireNotNull(findVariableRefAtCaret()) { "Should find variable reference" }

        val inferredType = GdsExpressionTypeInference.inferType(element)
        assertEquals(MatrixType.MAT4, inferredType)
    }

    fun `test vector swizzle xyz`() {
        val code = """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                vec3 xyz = v.<caret>xyz;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val postfixExpr = requireNotNull(findPostfixExprContainingCaret()) { "Should find postfix expression" }

        val inferredType = GdsExpressionTypeInference.inferType(postfixExpr)
        assertEquals(VectorType.VEC3, inferredType)
    }

    fun `test vector swizzle single`() {
        val code = """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                float x = v.<caret>x;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val postfixExpr = requireNotNull(findPostfixExprContainingCaret()) { "Should find postfix expression" }

        val inferredType = GdsExpressionTypeInference.inferType(postfixExpr)
        assertEquals(FloatType.DEFAULT, inferredType)
    }

    fun `test vector swizzle rgba`() {
        val code = """
            shader_type spatial;
            void fragment() {
                vec4 color = vec4(1.0);
                vec2 rg = color.<caret>rg;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val postfixExpr = requireNotNull(findPostfixExprContainingCaret()) { "Should find postfix expression" }

        val inferredType = GdsExpressionTypeInference.inferType(postfixExpr)
        assertEquals(VectorType.VEC2, inferredType)
    }

    fun `test vector indexing`() {
        val code = """
            shader_type spatial;
            void fragment() {
                vec4 v = vec4(1.0);
                float elem = <caret>v[0];
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val postfixExpr = requireNotNull(findPostfixExprContainingCaret()) { "Should find postfix expression" }

        val inferredType = GdsExpressionTypeInference.inferType(postfixExpr)
        assertEquals(FloatType.DEFAULT, inferredType)
    }

    fun `test matrix indexing`() {
        val code = """
            shader_type spatial;
            void fragment() {
                mat4 m = mat4(1.0);
                vec4 col = <caret>m[0];
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val postfixExpr = requireNotNull(findPostfixExprContainingCaret()) { "Should find postfix expression" }

        val inferredType = GdsExpressionTypeInference.inferType(postfixExpr)
        assertEquals(VectorType.VEC4, inferredType)
    }

    fun `test vec3 add vec3`() {
        doBinaryExprTest("vec3(1.0) + vec3(2.0)", VectorType.VEC3)
    }

    fun `test vec3 mul float`() {
        doBinaryExprTest("vec3(1.0) * 2.0", VectorType.VEC3)
    }

    fun `test float mul vec3`() {
        doBinaryExprTest("2.0 * vec3(1.0)", VectorType.VEC3)
    }

    fun `test mat4 mul vec4`() {
        doBinaryExprTest("mat4(1.0) * vec4(1.0)", VectorType.VEC4)
    }

    fun `test mat4 mul mat4`() {
        doBinaryExprTest("mat4(1.0) * mat4(1.0)", MatrixType.MAT4)
    }

    fun `test less than`() {
        doComparisonTest("1.0 < 2.0", BoolType)
    }

    fun `test equal`() {
        doComparisonTest("vec3(1.0) == vec3(1.0)", BoolType)
    }

    fun `test not equal`() {
        doComparisonTest("1 != 2", BoolType)
    }

    fun `test logical and`() {
        doLogicalTest("true && false", BoolType)
    }

    fun `test logical or`() {
        doLogicalTest("true || false", BoolType)
    }

    fun `test logical not`() {
        doUnaryTest("!true", BoolType)
    }

    fun `test negation`() {
        doUnaryTest("-1.0", FloatType.DEFAULT)
    }

    fun `test bitwise not`() {
        doUnaryTest("~5", IntType)
    }

    fun `test shift left`() {
        doShiftExprTest("1 << 2", IntType)
    }

    fun `test shift right`() {
        doShiftExprTest("8 >> 1", IntType)
    }

    fun `test bitwise and`() {
        doBitwiseExprTest("5 & 3", IntType)
    }

    fun `test bitwise xor`() {
        doBitwiseExprTest("5 ^ 3", IntType)
    }

    fun `test bitwise or`() {
        doBitwiseExprTest("5 | 3", IntType)
    }

    fun `test ternary operator`() {
        doConditionalTest("true ? 1.0 : 0.0", FloatType.DEFAULT)
    }

    fun `test ternary operator same type`() {
        doConditionalTest("true ? 1 : 0", IntType)
    }

    fun `test assignment`() {
        val code = """
            shader_type spatial;
            void fragment() {
                float a = 1.0;
                float b = <caret>(a = 2.0);
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
        val assignExpr = PsiTreeUtil.getParentOfType(elementAtCaret, GdsAssignExpr::class.java)

        val nonNullAssignExpr = requireNotNull(assignExpr) { "Should find assignment expression" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullAssignExpr)
        assertEquals(FloatType.DEFAULT, inferredType)
    }

    fun `test invalid constructor`() {
        val code = """
            shader_type spatial;
            void fragment() {
                vec3 v = vec3(1.0, 1.0);
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val functionCalls = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionCall::class.java)
        val functionCall = functionCalls.find { it.text == "vec3(1.0, 1.0)" }

        val nonNullFunctionCall = requireNotNull(functionCall) { "Should find function call: vec3(1.0, 1.0)" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullFunctionCall)
        assertNull("Should not infer type for invalid constructor", inferredType)
    }

    fun `test function overload mismatch`() {
        val code = """
            shader_type spatial;
            void fragment() {
                vec3 v = min(vec3(1.0), vec4(1.0));
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val functionCalls = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionCall::class.java)
        val functionCall = functionCalls.find { it.text.contains("min") }

        val nonNullFunctionCall = requireNotNull(functionCall) { "Should find function call: min" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullFunctionCall)
        assertNull("Should not infer type for mismatched vector sizes in function call", inferredType)
    }

    // ===== Helper Methods =====

    private fun doLiteralTest(literalText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                float dummy = $literalText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val literals = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsLiteral::class.java)
        val literal = literals.find { it.text == literalText }

        val nonNullLiteral = requireNotNull(literal) { "Should find literal: $literalText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullLiteral)
        assertEquals("Type mismatch for literal: $literalText", expectedType, inferredType)
    }

    private fun doConstructorTest(constructorText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                float dummy = $constructorText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val functionCalls = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionCall::class.java)
        val constructorCall = functionCalls.find { it.text == constructorText }

        val nonNullConstructorCall = requireNotNull(constructorCall) { "Should find constructor: $constructorText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullConstructorCall)
        assertEquals("Type mismatch for constructor: $constructorText", expectedType, inferredType)
    }

    private fun doBuiltinFunctionTest(functionCallText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                float dummy = $functionCallText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val functionCalls = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionCall::class.java)
        val functionCall = functionCalls.find { it.text == functionCallText }

        val nonNullFunctionCall = requireNotNull(functionCall) { "Should find function call: $functionCallText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullFunctionCall)
        assertEquals("Type mismatch for function: $functionCallText", expectedType, inferredType)
    }

    private fun doBinaryExprTest(expressionText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                float dummy = $expressionText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val additiveExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsAdditiveExpr::class.java)
        val multiplicativeExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsMultiplicativeExpr::class.java)

        val binaryExpr = additiveExprs.find { it.text == expressionText }
            ?: multiplicativeExprs.find { it.text == expressionText }

        val nonNullBinaryExpr = requireNotNull(binaryExpr) { "Should find binary expression: $expressionText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullBinaryExpr)
        assertEquals("Type mismatch for expression: $expressionText", expectedType, inferredType)
    }

    private fun doShiftExprTest(expressionText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                int dummy = $expressionText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val shiftExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsShiftExpr::class.java)
        val shiftExpr = shiftExprs.find { it.text == expressionText }

        val nonNullShiftExpr = requireNotNull(shiftExpr) { "Should find shift expression: $expressionText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullShiftExpr)
        assertEquals("Type mismatch for shift expression: $expressionText", expectedType, inferredType)
    }

    private fun doBitwiseExprTest(expressionText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                int dummy = $expressionText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val andExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsBitwiseAndExpr::class.java)
        val xorExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsBitwiseXorExpr::class.java)
        val orExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsBitwiseOrExpr::class.java)

        val bitwiseExpr = andExprs.find { it.text == expressionText }
            ?: xorExprs.find { it.text == expressionText }
            ?: orExprs.find { it.text == expressionText }

        val nonNullBitwiseExpr = requireNotNull(bitwiseExpr) { "Should find bitwise expression: $expressionText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullBitwiseExpr)
        assertEquals("Type mismatch for bitwise expression: $expressionText", expectedType, inferredType)
    }

    private fun doComparisonTest(expressionText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                bool dummy = $expressionText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val relationalExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsRelationalExpr::class.java)
        val equalityExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsEqualityExpr::class.java)

        val comparisonExpr = relationalExprs.find { it.text == expressionText }
            ?: equalityExprs.find { it.text == expressionText }

        val nonNullComparisonExpr = requireNotNull(comparisonExpr) { "Should find comparison expression: $expressionText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullComparisonExpr)
        assertEquals("Type mismatch for expression: $expressionText", expectedType, inferredType)
    }

    private fun doLogicalTest(expressionText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                bool dummy = $expressionText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val logicAndExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsLogicAndExpr::class.java)
        val logicOrExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsLogicOrExpr::class.java)

        val logicalExpr = logicAndExprs.find { it.text == expressionText }
            ?: logicOrExprs.find { it.text == expressionText }

        val nonNullLogicalExpr = requireNotNull(logicalExpr) { "Should find logical expression: $expressionText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullLogicalExpr)
        assertEquals("Type mismatch for expression: $expressionText", expectedType, inferredType)
    }

    private fun doConditionalTest(expressionText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                float dummy = $expressionText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val conditionalExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsConditionalExpr::class.java)
        val conditionalExpr = conditionalExprs.find { it.text == expressionText }

        val nonNullConditionalExpr = requireNotNull(conditionalExpr) { "Should find conditional expression: $expressionText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullConditionalExpr)
        assertEquals("Type mismatch for conditional expression: $expressionText", expectedType, inferredType)
    }

    private fun doUnaryTest(expressionText: String, expectedType: DataType) {
        val code = """
            shader_type spatial;
            void fragment() {
                float dummy = $expressionText;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val unaryExprs = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsUnaryExpr::class.java)
        val unaryExpr = unaryExprs.find { it.text == expressionText }

        val nonNullUnaryExpr = requireNotNull(unaryExpr) { "Should find unary expression: $expressionText" }

        val inferredType = GdsExpressionTypeInference.inferType(nonNullUnaryExpr)
        assertEquals("Type mismatch for expression: $expressionText", expectedType, inferredType)
    }

    private fun findVariableRefAtCaret(): GdsVariableNameRef? {
        val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset) ?: return null
        return PsiTreeUtil.getParentOfType(elementAtCaret, GdsVariableNameRef::class.java)
    }

    private fun findPostfixExprContainingCaret(): GdsPostfixExpr? {
        val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset) ?: return null
        return PsiTreeUtil.getParentOfType(elementAtCaret, GdsPostfixExpr::class.java)
    }

}

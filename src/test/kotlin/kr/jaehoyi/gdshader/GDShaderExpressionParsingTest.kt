package kr.jaehoyi.gdshader

import com.intellij.testFramework.ParsingTestCase

class GDShaderExpressionParsingTest : ParsingTestCase(
    "parsing/expressions",
    "gdshader",
    GDShaderParserDefinition()
) {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testConditionalExpression() {
        doTest(true)
    }
    
    fun testAssignExpression() {
        doTest(true)
    }
    
    fun testLogicalOrExpression() {
        doTest(true)
    }
    
    fun testLogicalAndExpression() {
        doTest(true)
    }
    
    fun testBitwiseOrExpression() {
        doTest(true)
    }
    
    fun testBitwiseXorExpression() {
        doTest(true)
    }
    
    fun testBitwiseAndExpression() {
        doTest(true)
    }
    
    fun testEqualityExpression() {
        doTest(true)
    }
    
    fun testRelationalExpression() {
        doTest(true)
    }
    
    fun testShiftExpression() {
        doTest(true)
    }
    
    fun testAdditiveExpression() {
        doTest(true)
    }
    
    fun testMultiplicativeExpression() {
        doTest(true)
    }
    
    fun testUnaryExpression() {
        doTest(true)
    }
    
    fun testPostfixExpression() {
        doTest(true)
    }
    
    fun testPrecedence() {
        doTest(true)
    }
    
    fun testComplexContext() {
        doTest(true)
    }
    
    fun testTrueAndFalse() {
        doTest(true)
    }
    
    fun testFloatConstant() {
        doTest(true)
    }
    
    fun testIntConstant() {
        doTest(true)
    }
    
    fun testUintConstant() {
        doTest(true)
    }
}

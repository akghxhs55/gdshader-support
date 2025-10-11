package kr.jaehoyi.gdshader.parsing

import com.intellij.testFramework.ParsingTestCase
import kr.jaehoyi.gdshader.GDShaderParserDefinition

class ErrorParsingTest : ParsingTestCase(
    "parsing/errors",
    "gdshader",
    GDShaderParserDefinition()
) {
    
    override fun getTestDataPath(): String = "src/test/testData"

    fun testExpressionStatementInTopLevel() {
        doTest(true)
    }
    
    fun testIfStatementInTopLevel() {
        doTest(true)
    }
    
    fun testIncompleteConstantDeclaration() {
        doTest(true)
    }
    
}
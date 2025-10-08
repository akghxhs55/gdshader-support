package kr.jaehoyi.gdshader.parsing

import com.intellij.testFramework.ParsingTestCase
import kr.jaehoyi.gdshader.GDShaderParserDefinition

class GDShaderStatementParsingTest : ParsingTestCase(
    "parsing/statements",
    "gdshader",
    GDShaderParserDefinition()
) {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testBlock() {
        doTest(true)
    }

    fun testIfStatement() {
        doTest(true)
    }
    
    fun testForStatement() {
        doTest(true)
    }
    
    fun testWhileStatement() {
        doTest(true)
    }
    
    fun testDoWhileStatement() {
        doTest(true)
    }
    
    fun testSwitchStatement() {
        doTest(true)
    }
    
    fun testReturnStatement() {
        doTest(true)
    }
    
    fun testSimpleStatement() {
        doTest(true)
    }
}

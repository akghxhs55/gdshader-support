package kr.jaehoyi.gdshader

import com.intellij.testFramework.ParsingTestCase

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
}

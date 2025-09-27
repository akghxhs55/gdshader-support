package kr.jaehoyi.gdshader

import com.intellij.testFramework.ParsingTestCase;

class GDShaderErrorParsingTest : ParsingTestCase(
    "parsing/errors",
    "gdshader",
    GDShaderParserDefinition()
) {
    override fun getTestDataPath(): String = "src/test/testData"

    override fun includeRanges(): Boolean = true

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
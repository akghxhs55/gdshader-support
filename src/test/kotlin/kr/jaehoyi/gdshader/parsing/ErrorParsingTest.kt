package kr.jaehoyi.gdshader.parsing

import com.intellij.testFramework.ParsingTestCase
import kr.jaehoyi.gdshader.GdsParserDefinition

class ErrorParsingTest : ParsingTestCase(
    "parsing/errors",
    "gdshader",
    GdsParserDefinition()
) {

    override fun getTestDataPath(): String = "src/test/testData"

    fun testExpressionStatementInTopLevel() {
        doTest(true)
    }

    fun testIncompleteConstantDeclaration() {
        doTest(true)
    }

}

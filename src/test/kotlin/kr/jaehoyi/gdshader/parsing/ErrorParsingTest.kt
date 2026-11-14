package kr.jaehoyi.gdshader.parsing

import com.intellij.testFramework.ParsingTestCase
import kr.jaehoyi.gdshader.GdsParserDefinition

class ErrorParsingTest :
    ParsingTestCase(
        "parsing/errors",
        "gdshader",
        GdsParserDefinition(),
    ) {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testExpressionStatementInTopLevel() {
        doTest(true)
    }

    fun testIncompleteConstantDeclaration() {
        doTest(true)
    }

    fun testStrayIdentifierBeforeDeclaration() {
        doTest(true)
    }

    fun testStrayIdentifierBeforeAssignment() {
        doTest(true)
    }

    fun testStrayIdentifierInNestedBlock() {
        doTest(true)
    }

    fun testStrayIdentifierInTopLevel() {
        doTest(true)
    }

    fun testIncompleteFunctionInTopLevel() {
        doTest(true)
    }

    fun testIncompleteDeclarationInBlock() {
        doTest(true)
    }

    fun testIncompleteReturnStatement() {
        doTest(true)
    }

    fun testIncompleteMemberAccess() {
        doTest(true)
    }

    fun testStrayIdentifierInStruct() {
        doTest(true)
    }

    fun testStructTypedLocalVariable() {
        doTest(true)
    }

    fun testGlobalWithoutUniform() {
        doTest(true)
    }
}

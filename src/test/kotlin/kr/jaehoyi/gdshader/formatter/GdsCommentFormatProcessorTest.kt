package kr.jaehoyi.gdshader.formatter

import com.intellij.application.options.CodeStyle
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.TextRange
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsCommentFormatProcessorTest : BasePlatformTestCase() {
    fun `test documentation opener and existing spacing policy`() {
        val cases =
            mapOf(
                "/** Inspector tooltip. */" to "/** Inspector tooltip. */",
                "/**Tooltip*/" to "/**Tooltip */",
                "/**\n * Tooltip\n */" to "/**\n * Tooltip\n */",
                "/*Comment*/" to "/* Comment */",
                "//Comment" to "// Comment",
                "/**/" to "/**/",
                "/* */" to "/* */",
                "/**" to "/**",
                "/**Tooltip" to "/**Tooltip",
            )
        for ((before, expected) in cases) {
            myFixture.configureByText("test.gdshader", before)
            val result = process(TextRange(0, before.length))
            myFixture.checkResult(expected)
            assertEquals(TextRange(0, expected.length), result)
            val repeated = process(result)
            myFixture.checkResult(expected)
            assertEquals(result, repeated)
        }
    }

    fun `test partial ranges preserve documentation opener`() {
        val code = "/**Tooltip*/"
        for (range in listOf(TextRange(0, 2), TextRange(2, 3), TextRange(3, 8))) {
            myFixture.configureByText("test.gdshader", code)
            assertEquals(range, process(range))
            myFixture.checkResult(code)
        }
        myFixture.configureByText("test.gdshader", code)
        assertEquals(TextRange(3, code.length + 1), process(TextRange(3, code.length)))
        myFixture.checkResult("/**Tooltip */")
    }

    fun `test mixed comments return actual insertion count`() {
        val code = "//Line\n/**Tooltip*/\n/*Block*/"
        val expected = "// Line\n/**Tooltip */\n/* Block */"
        myFixture.configureByText("test.gdshader", code)
        assertEquals(TextRange(0, expected.length), process(TextRange(0, code.length)))
        myFixture.checkResult(expected)
    }

    private fun process(range: TextRange): TextRange {
        var result = range
        WriteCommandAction.runWriteCommandAction(project) {
            result = GdsCommentFormatProcessor().processText(myFixture.file, range, CodeStyle.getSettings(myFixture.file))
        }
        return result
    }
}

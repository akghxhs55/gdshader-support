package kr.jaehoyi.gdshader.formatter

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsPreprocessorFormatterTest : BasePlatformTestCase() {
    fun `test preprocessor indentation`() {
        val code =
            """
            void fragment() {
                #define TEST
                if (true) {
                    #include "test.gdshaderinc"
                }
            }
            """.trimIndent()

        val expected =
            """
            void fragment() {
            #define TEST
            	if (true) {
            #include "test.gdshaderinc"
            	}
            }
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(myFixture.file)
        }

        myFixture.checkResult(expected)
    }

    fun `test nested preprocessor indentation`() {
        val code =
            """
            #if TRUE
                #define NESTED
            #endif
            """.trimIndent()

        val expected =
            """
            #if TRUE
            #define NESTED
            #endif
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(myFixture.file)
        }

        myFixture.checkResult(expected)
    }
}

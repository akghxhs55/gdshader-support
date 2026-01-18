package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ConstantCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testConstantKeywordInToplevel() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()
        
        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "const")
    }
    
    fun testAfterConstInToplevel() {
        myFixture.configureByText("test.gdshader", """
            const <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }

    fun testAfterPrecisionInToplevel() {
        myFixture.configureByText("test.gdshader", """
            const highp <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }

    fun testAfterTypeInToplevel() {
        myFixture.configureByText("test.gdshader", """
            const float <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }

    fun testConstKeywordInFunction() {
        myFixture.configureByText("test.gdshader", """
            void f() {
	            <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "const")
    }

    fun testAfterConstInFunction() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                const <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }

    fun testAfterPrecisionInFunction() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                const highp <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }

    fun testAfterTypeInFunction() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                const float <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }

}
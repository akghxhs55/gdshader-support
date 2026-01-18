package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class StatementCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }

    fun testInFunctionBody() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "if", "for", "while", "do", "switch")
        assertDoesntContain(completions, "else")
    }

    fun testInsideIfCondition() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (<caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "true", "false")
    }

    fun testInsideIfBody() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (true) {
                    <caret>
                }
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "if", "return")
    }

    fun testElseAfterIfStatement() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (true) {
                    
                }
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "else")
    }

    fun testIfAfterElse() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                if (true) {
                    
                }
                else <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "if")
        assertDoesntContain(completions, "else")
    }

    fun testInsideForInit() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (<caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "highp")
        assertDoesntContain(completions, "for", "if")
    }
    
    fun testInsideForCondition() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (int i = 1; <caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "true", "false")
        assertDoesntContain(completions, "for", "if")
    }
    
    fun testInsideForIteration() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (int i = 1; i < 10; <caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "true", "false")
        assertDoesntContain(completions, "for", "if")
    }
    
    fun testAfterStatement() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                for (;;) {
                }
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "if", "for", "return")
    }

}
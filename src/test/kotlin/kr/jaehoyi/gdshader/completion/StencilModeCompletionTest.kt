package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class StencilModeCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }

    fun testStencilModeKeyword() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "stencil_mode")
    }
    
    fun testStencilModeValues() {
        myFixture.configureByText("test.gdshader", """
            stencil_mode <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, GdsKeywords.STENCIL_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
    fun testSecondStencilModeValues() {
        myFixture.configureByText("test.gdshader", """
            stencil_mode write, <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, GdsKeywords.STENCIL_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
}
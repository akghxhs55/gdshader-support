package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class RenderModeCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testRenderModeKeyword() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "render_mode")
    }
    
    fun testRenderModeValues() {
        myFixture.configureByText("test.gdshader", """
            render_mode <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, GdsKeywords.RENDER_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
    fun testSecondRenderModeValues() {
        myFixture.configureByText("test.gdshader", """
            render_mode blend_add, <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, GdsKeywords.RENDER_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
}
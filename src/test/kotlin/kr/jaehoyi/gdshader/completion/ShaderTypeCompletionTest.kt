package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ShaderTypeCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testShaderTypeKeyword() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "shader_type")
    }
    
    fun testShaderTypeValues() {
        myFixture.configureByText("test.gdshader", """
            shader_type <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, GdsKeywords.SHADER_TYPES)
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
}
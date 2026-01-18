package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ToplevelCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testInEmptyFile() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertTopLevelKeywords(completions)
    }
    
    fun testBeforeToplevelDeclaration() {
        myFixture.configureByText("test.gdshader", """
            <caret>
            
            shader_type spatial;
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertTopLevelKeywords(completions)
    }
    
    fun testAfterToplevelDeclaration() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertTopLevelKeywords(completions)
    }
    
    fun testBetweenToplevelDeclaration() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            <caret>
            
            void fragment() {
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertTopLevelKeywords(completions)
    }
    
    fun testInFunctionBody() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "shader_type", "render_mode", "stencil_mode", "group_uniforms")
    }

    private fun assertTopLevelKeywords(completions: List<String>) {
        assertContainsElements(completions, 
            "shader_type", "render_mode", "stencil_mode", "group_uniforms", "uniform", "const", "varying",
            "struct", "highp", "mediump", "lowp", "void", "bool", "int", "float", "vec2", "vec3", "vec4")
    }
    
}
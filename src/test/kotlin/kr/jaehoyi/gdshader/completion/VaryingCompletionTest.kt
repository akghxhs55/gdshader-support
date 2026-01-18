package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class VaryingCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testVaryingKeyword() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "varying")
    }
    
    fun testAfterVarying() {
        myFixture.configureByText("test.gdshader", """
            varying <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec2", "flat", "smooth", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "varying", "shader_type", "uniform")
    }
    
    fun testAfterInterpolationModifier() {
        myFixture.configureByText("test.gdshader", """
            varying flat <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec2", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "varying", "flat", "smooth", "shader_type", "uniform")
    }
    
    fun testAfterPrecisionModifier() {
        myFixture.configureByText("test.gdshader", """
            varying highp <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec2")
        assertDoesntContain(completions, "varying", "flat", "smooth", "highp", "mediump", "lowp", "shader_type", "uniform")
    }
    
    fun testAfterType() {
        myFixture.configureByText("test.gdshader", """
            varying float <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "varying", "flat", "smooth", "highp", "mediump", "lowp", "int", "float", "vec2", "shader_type", "uniform")
    }
    
}
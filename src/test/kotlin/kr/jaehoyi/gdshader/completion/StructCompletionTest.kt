package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class StructCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testStructKeyword() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "struct")
    }
    
    fun testAfterStruct() {
        myFixture.configureByText("test.gdshader", """
            struct <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "struct", "shader_type", "void", "uniform")
    }
    
    fun testFirstMember() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec2", "vec3", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if")
    }
    
    fun testMemberAfterPrecision() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                highp <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec2", "vec3")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if", "highp", "mediump", "lowp")
    }
    
    fun testMemberAfterType() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                float <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if", "highp", "mediump", "lowp", "int", "float", "vec2", "vec3")
    }
    
    fun testAfterMember() {
        myFixture.configureByText("test.gdshader", """
            struct S {
                int a;
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "vec2", "vec3", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if")
    }
    
}
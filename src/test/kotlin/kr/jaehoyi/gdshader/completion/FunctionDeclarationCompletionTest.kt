package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FunctionDeclarationCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testToplevel() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "void", "int", "float", "highp", "mediump")
    }
    
    fun testReturnTypeAfterPrecision() {
        myFixture.configureByText("test.gdshader", """
            highp <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "void", "int", "float")
        assertDoesntContain(completions, "highp", "mediump", "lowp", "shader_type", "uniform")
    }
    
    fun testAfterReturnType() {
        myFixture.configureByText("test.gdshader", """
            float <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "void", "int", "float", "highp", "mediump", "lowp", "shader_type", "uniform")
    }
    
    fun testParameterHeader() {
        myFixture.configureByText("test.gdshader", """
            void f(<caret>)
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "in", "out", "inout", "highp", "mediump", "lowp", "const")
        assertDoesntContain(completions, "shader_type", "uniform")
    }
    
    fun testParameterHeaderAfterConst() {
        myFixture.configureByText("test.gdshader", """
            void f(const <caret>)
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "in", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "out", "inout", "shader_type", "uniform", "const", "void")
    }
    
    fun testParameterHeaderAfterQualifier() {
        myFixture.configureByText("test.gdshader", """
            void f(in <caret>)
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "in", "out", "inout", "shader_type", "uniform", "void")
    }
    
    fun testParameterHeaderAfterPrecision() {
        myFixture.configureByText("test.gdshader", """
            void f(highp <caret>)
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float")
        assertDoesntContain(completions, "highp", "mediump", "lowp", "in", "out", "inout", "shader_type", "uniform", "void")
    }
    
    fun testAfterParameterHeader() {
        myFixture.configureByText("test.gdshader", """
            void f(float <caret>)
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "int", "float", "highp", "mediump", "lowp", "in", "out", "inout", "shader_type", "uniform", "const", "void")
    }
    
    fun testMultipleParameters() {
        myFixture.configureByText("test.gdshader", """
            void f(float a, <caret>)
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "in", "out", "inout", "highp", "mediump", "lowp", "const")
        assertDoesntContain(completions, "shader_type", "uniform", "void")
    }
    
    fun testNoToplevelCompletionInFunctionBody() {
        myFixture.configureByText("test.gdshader", """
            void f() {
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "shader_type", "uniform", "varying", "struct")
    }
    
}
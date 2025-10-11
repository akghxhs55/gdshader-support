package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ToplevelCompletionTest : BasePlatformTestCase() {
    
    override fun getTestDataPath(): String = "src/test/testData"
    
    private val testPath = "completion/toplevel"
    
    fun testInEmptyFile() {
        myFixture.configureByFile("$testPath/InEmptyFile.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertTopLevelKeywords(completions)
    }
    
    fun testBeforeToplevelDeclaration() {
        myFixture.configureByFile("$testPath/BeforeToplevelDeclaration.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertTopLevelKeywords(completions)
    }
    
    fun testAfterToplevelDeclaration() {
        myFixture.configureByFile("$testPath/AfterToplevelDeclaration.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertTopLevelKeywords(completions)
    }
    
    fun testBetweenToplevelDeclaration() {
        myFixture.configureByFile("$testPath/BetweenToplevelDeclaration.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertTopLevelKeywords(completions)
    }

    private fun assertTopLevelKeywords(completions: List<String>?) {
        assertNotNull(completions)
        assertContainsElements(completions!!, 
            "shader_type", "render_mode", "stencil_mode", "group_uniforms", "uniform", "const", "varying",
            "struct", "highp", "mediump", "lowp", "void", "bool", "int", "float", "vec2", "vec3", "vec4")
    }
    
}
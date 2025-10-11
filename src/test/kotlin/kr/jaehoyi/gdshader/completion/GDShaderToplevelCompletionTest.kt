package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.junit.Test

class GDShaderToplevelCompletionTest : LightPlatformCodeInsightFixture4TestCase() {
    override fun getTestDataPath(): String = "src/test/testData"
    
    @Test
    fun testInEmptyFile() {
        myFixture.configureByFile("completion/toplevel/InEmptyFile.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertTopLevelKeywords(completions)
    }
    
    @Test
    fun testBeforeToplevelDeclaration() {
        myFixture.configureByFile("completion/toplevel/BeforeToplevelDeclaration.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertTopLevelKeywords(completions)
    }
    
    @Test
    fun testAfterToplevelDeclaration() {
        myFixture.configureByFile("completion/toplevel/AfterToplevelDeclaration.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertTopLevelKeywords(completions)
    }
    
    @Test
    fun testBetweenToplevelDeclaration() {
        myFixture.configureByFile("completion/toplevel/BetweenToplevelDeclaration.gdshader")
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
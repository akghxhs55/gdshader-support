package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.GDShaderUtil

class RednerModeDeclarationCompletionTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"
    
    private val testPath = "completion/renderModeDeclaration"
    
    fun testRenderModeKeyword() {
        myFixture.configureByFile("$testPath/RenderModeKeyword.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, "render_mode")
    }
    
    fun testRenderModeValues() {
        myFixture.configureByFile("$testPath/RenderModeValues.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, GDShaderUtil.renderModeMap.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
    fun testSecondRenderModeValues() {
        myFixture.configureByFile("$testPath/SecondRenderModeValues.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, GDShaderUtil.renderModeMap.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
}
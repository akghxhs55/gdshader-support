package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.GDShaderUtil

class StencilModeDeclarationCompletionTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/testData"
    
    private val testPath = "completion/stencilModeDeclaration"
    
    fun testStencilModeKeyword() {
        myFixture.configureByFile("$testPath/StencilModeKeyword.gdshader")
        myFixture.completeBasic()

        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, "stencil_mode")
    }
    
    fun testStencilModeValues() {
        myFixture.configureByFile("$testPath/StencilModeValues.gdshader")
        myFixture.completeBasic()

        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, GDShaderUtil.stencilModeMap.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
    fun testSecondStencilModeValues() {
        myFixture.configureByFile("$testPath/SecondStencilModeValues.gdshader")
        myFixture.completeBasic()

        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, GDShaderUtil.stencilModeMap.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
}
package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.GDShaderUtil

class ShaderTypeDeclarationCompletionTest : BasePlatformTestCase() {
    
    override fun getTestDataPath(): String = "src/test/testData"
    
    private val testPath = "completion/shaderTypeDeclaration"
    
    fun testShaderTypeKeyword() {
        myFixture.configureByFile("$testPath/ShaderTypeKeyword.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, "shader_type")
    }
    
    fun testShaderTypeValues() {
        myFixture.configureByFile("$testPath/ShaderTypeValues.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, GDShaderUtil.shaderTypes)
    }
    
}
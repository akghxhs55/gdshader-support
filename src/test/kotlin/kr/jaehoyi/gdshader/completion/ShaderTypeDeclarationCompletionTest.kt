package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import kr.jaehoyi.gdshader.GDShaderUtil
import org.junit.Test

class ShaderTypeDeclarationCompletionTest : LightPlatformCodeInsightFixture4TestCase() {
    
    override fun getTestDataPath(): String = "src/test/testData"
    
    private val testPath = "completion/shaderTypeDeclaration"
    
    @Test
    fun testShaderTypeKeyword() {
        myFixture.configureByFile("$testPath/ShaderTypeKeyword.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, "shader_type")
    }
    
    @Test
    fun testShaderTypeValues() {
        myFixture.configureByFile("$testPath/ShaderTypeValues.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, GDShaderUtil.shaderTypes)
    }
    
}
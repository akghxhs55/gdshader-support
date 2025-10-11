package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import kr.jaehoyi.gdshader.GDShaderUtil
import org.junit.Test

class GDShaderShaderTypeDeclarationCompletionTest : LightPlatformCodeInsightFixture4TestCase() {
    override fun getTestDataPath(): String = "src/test/testData"
    
    @Test
    fun testShaderTypeKeyword() {
        myFixture.configureByFile("completion/shaderType/ShaderTypeKeyword.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, "shader_type")
    }
    
    @Test
    fun testShaderTypeValues() {
        myFixture.configureByFile("completion/shaderType/ShaderTypeValues.gdshader")
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        assertContainsElements(completions!!, GDShaderUtil.shaderTypes)
    }
}
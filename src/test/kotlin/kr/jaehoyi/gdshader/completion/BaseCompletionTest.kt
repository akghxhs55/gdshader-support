package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class BaseCompletionTest : BasePlatformTestCase() {
    
    override fun getTestDataPath(): String = "src/test/testData"
    
    protected abstract val testPath: String
    
    protected fun getCompletionsForTestFile(): List<String> {
        val filePath = "$testPath/${getTestName(false)}.gdshader"
        myFixture.configureByFile(filePath)
        myFixture.completeBasic()
        
        val completions = myFixture.lookupElementStrings
        assertNotNull(completions)
        return completions!!
    }
    
}
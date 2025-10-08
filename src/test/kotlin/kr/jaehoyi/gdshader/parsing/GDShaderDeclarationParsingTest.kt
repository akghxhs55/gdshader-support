package kr.jaehoyi.gdshader.parsing

import com.intellij.testFramework.ParsingTestCase
import kr.jaehoyi.gdshader.GDShaderParserDefinition

class GDShaderDeclarationParsingTest : ParsingTestCase(
    "parsing/declarations",
    "gdshader",
    GDShaderParserDefinition()
) {
    override fun getTestDataPath(): String = "src/test/testData"

    fun testShaderTypeDeclaration() {
        doTest(true)
    }
    
    fun testRenderModeDeclaration() {
        doTest(true)
    }
    
    fun testStencilModeDeclaration() {
        doTest(true)
    }
    
    fun testUniformGroupDeclaration() {
        doTest(true)
    }
    
    fun testUniformDeclaration() {
        doTest(true)
    }
    
    fun testConstantDeclaration() {
        doTest(true)
    }
    
    fun testVaryingDeclaration() {
        doTest(true)
    }
    
    fun testFunctionDeclaration() {
        doTest(true)
    }
    
    fun testStructDeclaration() {
        doTest(true)
    }
}

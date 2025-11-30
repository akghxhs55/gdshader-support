package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.GDShaderUtil

class ShaderTypeCompletionTest : BaseCompletionTest() {
    
    override val testPath = "completion/shaderType"
    
    fun testShaderTypeKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "shader_type")
    }
    
    fun testShaderTypeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GDShaderUtil.shaderTypeKeywords)
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
}
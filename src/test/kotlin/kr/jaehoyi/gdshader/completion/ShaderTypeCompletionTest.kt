package kr.jaehoyi.gdshader.completion

class ShaderTypeCompletionTest : BaseCompletionTest() {
    
    override val testPath = "completion/shaderType"
    
    fun testShaderTypeKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "shader_type")
    }
    
    fun testShaderTypeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GDShaderKeywords.SHADER_TYPES)
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
}
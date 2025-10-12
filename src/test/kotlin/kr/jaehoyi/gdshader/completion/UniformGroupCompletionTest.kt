package kr.jaehoyi.gdshader.completion

class UniformGroupCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/uniformGroup"
    
    fun testUniformGroupKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "group_uniforms")
    }
    
    fun testUniformGroupValues() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "shader_type", "group_uniforms", "void", "uniform")
    }
}
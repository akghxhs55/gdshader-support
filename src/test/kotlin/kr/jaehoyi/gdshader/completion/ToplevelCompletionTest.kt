package kr.jaehoyi.gdshader.completion


class ToplevelCompletionTest : BaseCompletionTest() {
    
    override val testPath = "completion/toplevel"
    
    fun testInEmptyFile() {
        val completions = getCompletionsForTestFile()
        assertTopLevelKeywords(completions)
    }
    
    fun testBeforeToplevelDeclaration() {
        val completions = getCompletionsForTestFile()
        assertTopLevelKeywords(completions)
    }
    
    fun testAfterToplevelDeclaration() {
        val completions = getCompletionsForTestFile()
        assertTopLevelKeywords(completions)
    }
    
    fun testBetweenToplevelDeclaration() {
        val completions = getCompletionsForTestFile()
        assertTopLevelKeywords(completions)
    }
    
    fun testInFunctionBody() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "shader_type", "render_mode", "stencil_mode", "group_uniforms")
    }

    private fun assertTopLevelKeywords(completions: List<String>) {
        assertContainsElements(completions, 
            "shader_type", "render_mode", "stencil_mode", "group_uniforms", "uniform", "const", "varying",
            "struct", "highp", "mediump", "lowp", "void", "bool", "int", "float", "vec2", "vec3", "vec4")
    }
    
}
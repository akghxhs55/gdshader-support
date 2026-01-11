package kr.jaehoyi.gdshader.completion

class StencilModeCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/stencilMode"
    
    fun testStencilModeKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "stencil_mode")
    }
    
    fun testStencilModeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GdsKeywords.STENCIL_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
    fun testSecondStencilModeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GdsKeywords.STENCIL_MODES.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
}
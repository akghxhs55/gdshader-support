package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.GDShaderUtil

class RenderModeCompletionTest : BaseCompletionTest() {
    
    override val testPath = "completion/renderMode"
    
    fun testRenderModeKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "render_mode")
    }
    
    fun testRenderModeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GDShaderUtil.renderModeKeywords.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
    fun testSecondRenderModeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GDShaderUtil.renderModeKeywords.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "render_mode", "void", "uniform")
    }
    
}
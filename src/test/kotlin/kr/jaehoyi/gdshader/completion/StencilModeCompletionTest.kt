package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.GDShaderUtil

class StencilModeCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/stencilMode"
    
    fun testStencilModeKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "stencil_mode")
    }
    
    fun testStencilModeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GDShaderUtil.stencilModeMap.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
    fun testSecondStencilModeValues() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, GDShaderUtil.stencilModeMap.flatMap { it.value })
        assertDoesntContain(completions, "shader_type", "stencil_mode", "void", "uniform")
    }
    
}
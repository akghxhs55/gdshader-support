package kr.jaehoyi.gdshader.completion

class VaryingCompletionTest : BaseCompletionTest() {
    
    override val testPath = "completion/varying"
    
    fun testVaryingKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "varying")
    }
    
    fun testAfterVarying() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec2", "flat", "smooth", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "varying", "shader_type", "uniform")
    }
    
    fun testAfterInterpolationModifier() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec2", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "varying", "flat", "smooth", "shader_type", "uniform")
    }
    
    fun testAfterPrecisionModifier() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec2")
        assertDoesntContain(completions, "varying", "flat", "smooth", "highp", "mediump", "lowp", "shader_type", "uniform")
    }
    
    fun testAfterType() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "varying", "flat", "smooth", "highp", "mediump", "lowp", "int", "float", "vec2", "shader_type", "uniform")
    }
    
}
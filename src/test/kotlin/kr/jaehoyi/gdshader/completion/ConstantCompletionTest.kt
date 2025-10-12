package kr.jaehoyi.gdshader.completion

class ConstantCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/constant"
    
    fun testConstantKeywordInToplevel() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "const")
    }
    
    fun testAfterConstInToplevel() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }
    
    fun testAfterPrecisionInToplevel() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }
    
    fun testAfterTypeInToplevel() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }
    
    fun testConstantKeywordInFunction() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "const")
    }
    
    fun testAfterConstInFunction() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec3", "highp", "lowp", "mediump")
        assertDoesntContain(completions, "const", "shader_type", "uniform")
    }
    
    fun testAfterPrecisionInFunction() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec3")
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump")
    }
    
    fun testAfterTypeInFunction() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "const", "shader_type", "uniform", "highp", "lowp", "mediump", "int", "float", "vec3")
    }

}
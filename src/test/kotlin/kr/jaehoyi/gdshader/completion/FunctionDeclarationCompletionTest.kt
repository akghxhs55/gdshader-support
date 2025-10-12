package kr.jaehoyi.gdshader.completion

class FunctionDeclarationCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/functionDeclaration"
    
    fun testToplevel() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "void", "int", "float", "highp", "mediump")
    }
    
    fun testReturnTypeAfterPrecision() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "void", "int", "float")
        assertDoesntContain(completions, "highp", "mediump", "lowp", "shader_type", "uniform")
    }
    
    fun testAfterReturnType() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "void", "int", "float", "highp", "mediump", "lowp", "shader_type", "uniform")
    }
    
    fun testParameterHeader() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "void", "int", "float", "in", "out", "inout", "highp", "mediump", "lowp", "const")
        assertDoesntContain(completions, "shader_type", "uniform")
    }
    
    fun testParameterHeaderAfterConst() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "void", "int", "float", "in", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "out", "inout", "shader_type", "uniform", "const")
    }
    
    fun testParameterHeaderAfterQualifier() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "void", "int", "float", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "in", "out", "inout", "shader_type", "uniform")
    }
    
    fun testParameterHeaderAfterPrecision() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "void", "int", "float")
        assertDoesntContain(completions, "highp", "mediump", "lowp", "in", "out", "inout", "shader_type", "uniform")
    }
    
    fun testAfterParameterHeader() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "void", "int", "float", "highp", "mediump", "lowp", "in", "out", "inout", "shader_type", "uniform", "const")
    }
    
    fun testMultipleParameters() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "void", "int", "float", "in", "out", "inout", "highp", "mediump", "lowp", "const")
        assertDoesntContain(completions, "shader_type", "uniform")
    }
    
    fun testNoToplevelCompletionInFunctionBody() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "shader_type", "uniform", "varying", "struct")
    }
    
}
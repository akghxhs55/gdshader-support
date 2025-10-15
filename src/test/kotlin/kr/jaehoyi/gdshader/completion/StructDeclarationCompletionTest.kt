package kr.jaehoyi.gdshader.completion

class StructDeclarationCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/structDeclaration"
    
    fun testStructKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "struct")
    }
    
    fun testAfterStruct() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "struct", "shader_type", "void", "uniform")
    }
    
    fun testFirstMember() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec2", "vec3", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if")
    }
    
    fun testMemberAfterPrecision() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec2", "vec3")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if", "highp", "mediump", "lowp")
    }
    
    fun testMemberAfterType() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if", "highp", "mediump", "lowp", "int", "float", "vec2", "vec3")
    }
    
    fun testAfterMember() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "vec2", "vec3", "highp", "mediump", "lowp")
        assertDoesntContain(completions, "struct", "shader_type", "uniform", "if")
    }
    
}
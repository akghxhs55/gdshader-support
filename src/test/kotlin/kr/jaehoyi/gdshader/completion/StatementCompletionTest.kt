package kr.jaehoyi.gdshader.completion

class StatementCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/statement"

    fun testInFunctionBody() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "if", "for", "while", "do", "switch")
        assertDoesntContain(completions, "else")
    }

    fun testInsideIfCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }

    fun testInsideIfBody() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "if", "return")
    }

    fun testElseAfterIfStatement() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "else")
    }

    fun testIfAfterElse() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "if")
        assertDoesntContain(completions, "else")
    }

    fun testInsideForInit() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "highp")
        assertDoesntContain(completions, "for", "if")
    }
    
    fun testInsideForCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
        assertDoesntContain(completions, "for", "if")
    }
    
    fun testInsideForIteration() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
        assertDoesntContain(completions, "for", "if")
    }
    
    fun testAfterStatement() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "if", "for", "return")
    }

}
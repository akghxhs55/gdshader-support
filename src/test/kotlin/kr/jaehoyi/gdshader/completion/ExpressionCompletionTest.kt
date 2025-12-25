package kr.jaehoyi.gdshader.completion

class ExpressionCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/expression"
    
    fun testInInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }

    fun testInFunctionBody() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionBodyAfterStatement() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionBodyBeforeStatement() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionBodyBetweenStatements() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }

    fun testInIfStatementCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterIfStatementCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInForStatementCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }

    fun testInForStatementIteration() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInSwitchStatementExpression() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInSwitchBody() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterCaseKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterDoKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterReturnKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionArgument() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterOperator() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterPrimary() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "radians")
    }
    
    fun testUniformDeclarationInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testConstantDeclarationInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testLocalVariableDeclarationInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInInitializerList() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInArraySize() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
    fun testInConstantDeclarationArraySize() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "radians")
    }
    
}
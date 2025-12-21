package kr.jaehoyi.gdshader.completion

class ExpressionCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/expression"
    
    fun testInInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }

    fun testInFunctionBody() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInFunctionBodyAfterStatement() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInFunctionBodyBeforeStatement() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInFunctionBodyBetweenStatements() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }

    fun testInIfStatementCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testAfterIfStatementCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInForStatementCondition() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }

    fun testInForStatementIteration() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInSwitchStatementExpression() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInSwitchBody() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testAfterCaseKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testAfterDoKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testAfterReturnKeyword() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInFunctionArgument() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testAfterOperator() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testAfterPrimary() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "true", "false")
    }
    
    fun testUniformDeclarationInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testConstantDeclarationInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testLocalVariableDeclarationInitializer() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInInitializerList() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInArraySize() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
    fun testInConstantDeclarationArraySize() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "true", "false")
    }
    
}
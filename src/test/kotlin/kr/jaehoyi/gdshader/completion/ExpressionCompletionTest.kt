package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ExpressionCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testInInitializer() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;
            
            uniform bool t = <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }

    fun testInFunctionBody() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionBodyAfterStatement() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                ;
                <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionBodyBeforeStatement() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                <caret>
                ;
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionBodyBetweenStatements() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                ;
                <caret>
                test;
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }

    fun testInIfStatementCondition() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                if (<caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterIfStatementCondition() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                if (true)
                    <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testIfStatementBody() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                if (test) <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInForStatementCondition() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                for (; <caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }

    fun testInForStatementIteration() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                for (;;<caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInSwitchStatementExpression() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                switch (<caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInSwitchBody() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                switch (test) {
                    case 0:
                    <caret>
                }
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterCaseKeyword() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                switch (test) {
                    case <caret>
                }
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterDoKeyword() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                do <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterReturnKeyword() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                return <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInFunctionArgument() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                test(<caret>)
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterOperator() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            uniform int t = 1 + <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testAfterPrimary() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                1 <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "radians")
    }
    
    fun testUniformDeclarationInitializer() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            uniform int test = <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testConstantDeclarationInitializer() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            const int test = <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testLocalVariableDeclarationInitializer() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                int test = <caret>
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInInitializerList() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            const int t[1] = { <caret> }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInArraySize() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            void f() {
                test[<caret>]
            }
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    fun testInConstantDeclarationArraySize() {
        myFixture.configureByText("test.gdshader", """
            shader_type spatial;

            const int test[<caret>]
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "radians")
    }
    
    
    
}
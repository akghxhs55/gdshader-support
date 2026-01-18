package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class VariableCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }

    fun `test local variable completion`() {
        myFixture.configureByText("test.gdshader", """
            void fragment() {
                float my_local_var = 1.0;
                my_loc<caret>
            }
        """.trimIndent())

        myFixture.completeBasic()
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)

        assertTrue("Should contain 'my_local_var'", lookupStrings.contains("my_local_var"))
    }

    fun `test parameter completion`() {
        myFixture.configureByText("test.gdshader", """
            void my_func(vec3 my_param_vec) {
                my_par<caret>
            }
        """.trimIndent())

        myFixture.completeBasic()
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)

        assertTrue("Should contain 'my_param_vec'", lookupStrings.contains("my_param_vec"))
    }

    fun `test global variable completion`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            
            uniform float u_time;
            const float MAX_SPEED = 10.0;
            varying vec3 v_normal;

            void fragment() {
                <caret>
            }
        """.trimIndent())

        myFixture.completeBasic()
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)

        assertTrue("Should contain 'u_time'", lookupStrings.contains("u_time"))
        assertTrue("Should contain 'MAX_SPEED'", lookupStrings.contains("MAX_SPEED"))
        assertTrue("Should contain 'v_normal'", lookupStrings.contains("v_normal"))
    }

    fun `test builtin variable completion`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            void fragment() {
                TIM<caret> 
            }
        """.trimIndent())

        myFixture.completeBasic()
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)

        assertTrue("Should contain 'TIME'", lookupStrings.contains("TIME"))
    }

    fun `test variable scope visibility`() {
        myFixture.configureByText("test.gdshader", """
            void func_a() {
                float invisible_var = 1.0;
            }

            void func_b() {
                invis<caret> 
            }
        """.trimIndent())

        myFixture.completeBasic()
        val lookupStrings = myFixture.lookupElementStrings

        if (lookupStrings != null) {
            assertFalse("Should NOT contain 'invisible_var' from another function",
                lookupStrings.contains("invisible_var"))
        }
    }
    
    fun `test for loop variable completion`() {
        myFixture.configureByText("test.gdshader", """
            void fragment() {
                for (int i = 0; i < 10; i++) {
                    i<caret>
                }
            }
        """.trimIndent())

        myFixture.completeBasic()
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)

        assertTrue("Should contain 'i'", lookupStrings.contains("i"))
    }
    
    fun `test for loop variable in condition`() {
        myFixture.configureByText("test.gdshader", """
            void fragment() {
                for (int i = 0; i<caret> < 10; i++) {
                }
            }
        """.trimIndent())

        myFixture.completeBasic()
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)

        assertTrue("Should contain 'i'", lookupStrings.contains("i"))
    }

    fun `test completion shows variables from included file`() {
        myFixture.addFileToProject("constants.gdshaderinc", "const int MAX_LIGHTS = 10;")

        myFixture.configureByText("main.gdshader", """
            #include "res://constants.gdshaderinc"
            
            void light() {
                int count = MAX_<caret>;
            }
        """.trimIndent())
        
        myFixture.completeBasic()
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(lookupStrings, "MAX_LIGHTS")
    }

    fun `test transitive include resolution`() {
        myFixture.addFileToProject("level_a.gdshaderinc", "uniform vec3 deep_color;")
        
        myFixture.addFileToProject("level_b.gdshaderinc", """
            #include "res://level_a.gdshaderinc"
        """.trimIndent())
        
        myFixture.configureByText("main.gdshader", """
            #include "res://level_b.gdshaderinc"
            
            void fragment() {
                vec3 c = deep_<caret>color;
            }
        """.trimIndent())
        
        val element = myFixture.elementAtCaret
        
        assertEquals("deep_color", element.text)
        assertEquals("level_a.gdshaderinc", element.containingFile.name)
    }
    
}

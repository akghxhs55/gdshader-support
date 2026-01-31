package kr.jaehoyi.gdshader.documentation

import com.intellij.lang.LanguageDocumentation
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsDocumentationTest : BasePlatformTestCase() {

    fun `test builtin function documentation`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            void fragment() {
                float x = <caret>sin(1.0);
            }
        """.trimIndent())
        
        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")
        
        assertTrue(plainDoc.contains("vec_type sin(vec_type x)"))
    }
    
    fun `test builtin variable documentation`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            void fragment() {
                vec4 c = <caret>COLOR;
            }
        """.trimIndent())

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("inout vec4 COLOR"))
    }

    fun `test user defined function documentation`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            
            // Calculates something important.
            // @param x Input value
            float my_func(float x) {
                return x * 2.0;
            }
            
            void fragment() {
                float y = <caret>my_func(1.0);
            }
        """.trimIndent())

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("float my_func(float x)"))
        assertTrue(plainDoc.contains("Calculates something important."))
        assertTrue(plainDoc.contains("@param x Input value"))
    }

    fun `test user defined uniform documentation`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            
            // The main color.
            uniform vec4 <caret>u_color : source_color;
        """.trimIndent())

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("uniform vec4 u_color"))
        assertTrue(plainDoc.contains("The main color."))
    }
    
    fun `test user defined struct documentation`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            
            // My custom data structure
            struct <caret>MyData {
                float x;
                float y;
            };
        """.trimIndent())

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("struct MyData"))
        assertTrue(plainDoc.contains("My custom data structure"))
    }
}
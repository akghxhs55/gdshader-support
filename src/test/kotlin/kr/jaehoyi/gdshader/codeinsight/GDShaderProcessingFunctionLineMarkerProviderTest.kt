package kr.jaehoyi.gdshader.codeinsight

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GDShaderProcessingFunctionLineMarkerProviderTest : BasePlatformTestCase() {

    fun `test valid vertex function in spatial shader has line marker`() {
        val code = """
            shader_type spatial;
            
            void ver<caret>tex() {
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)

        val gutters = myFixture.findAllGutters()

        assertSize(1, gutters)

        val tooltip = requireNotNull(gutters[0].tooltipText)
        assertTrue(tooltip.contains("vertex"))
        assertTrue(tooltip.contains("processing function"))
        assertTrue(tooltip.contains("<html>"))
    }

    fun `test invalid function for shader type has no marker`() {
        val code = """
            shader_type sky;
            
            void lig<caret>ht() {
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)

        val gutters = myFixture.findAllGutters()

        assertEmpty(gutters)
    }

    fun `test custom function has no marker`() {
        val code = """
            shader_type spatial;
            
            void my_cust<caret>om_function() {
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)

        val gutters = myFixture.findAllGutters()

        assertEmpty(gutters)
    }
    
}
package kr.jaehoyi.gdshader.codeinsight

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.awt.Color

class GdsUniformColorProviderTest : BasePlatformTestCase() {
    
    private val provider = GdsUniformColorProvider()
    
    fun `test extract color from vec3`() {
        val code = "uniform vec3 my<caret>Color : source_color = vec3(1.0, 0.0, 0.0);"
        
        myFixture.configureByText("test.gdshader", code)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))
        
        val color = requireNotNull(provider.getColorFrom(element))
        
        assertEquals(255, color.red)
        assertEquals(0, color.green)
        assertEquals(0, color.blue)
        assertEquals(255, color.alpha)
    }

    fun `test extract color from vec4 with alpha`() {
        val code = "uniform vec4 my<caret>Color : source_color = vec4(0.0, 1.0, 0.0, 0.5);"

        myFixture.configureByText("test.gdshader", code)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        val color = requireNotNull(provider.getColorFrom(element))

        assertEquals(0, color.red)
        assertEquals(255, color.green)
        assertEquals(0, color.blue)

        assertTrue(color.alpha in 127..128)
    }

    fun `test update existing color expression`() {
        val before = "uniform vec3 my<caret>Color : source_color = vec3(1.0, 0.0, 0.0);"
        val after = "uniform vec3 myColor : source_color = vec3(0.0, 0.0, 1.0);"

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.BLUE)

        myFixture.checkResult(after)
    }

    fun `test insert initializer if missing`() {
        val before = "uniform vec3 my<caret>Color : source_color;"
        val after = "uniform vec3 myColor : source_color = vec3(1.0, 0.0, 0.0);"

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.RED)

        myFixture.checkResult(after)
    }
    
}
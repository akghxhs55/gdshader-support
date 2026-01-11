package kr.jaehoyi.gdshader.codeinsight

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.awt.Color

class GdsConstantColorProviderTest : BasePlatformTestCase() {

    private val provider = GdsConstantColorProvider()

    fun `test extract color from const vec3`() {
        val code = "const vec3 <caret>RED = vec3(1.0, 0.0, 0.0);"

        myFixture.configureByText("test.gdshader", code)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        val color = requireNotNull(provider.getColorFrom(element))

        assertEquals(255, color.red)
    }

    fun `test ignore variable if name is not color-related`() {
        val code = "const vec3 <caret>POS = vec3(1.0, 0.0, 0.0);"

        myFixture.configureByText("test.gdshader", code)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        val color = provider.getColorFrom(element)

        assertNull(color)
    }

    fun `test update const value`() {
        val before = "const vec3 <caret>MY_COLOR = vec3(1.0, 0.0, 0.0);"
        val after = "const vec3 MY_COLOR = vec3(0.0, 0.0, 1.0);"

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.BLUE)

        myFixture.checkResult(after)
    }

    fun `test update specific constant in multi-declaration`() {
        val before = "const vec3 RED = vec3(1.0, 0.0, 0.0), <caret>BLUE = vec3(0.0, 0.0, 1.0);"
        val after = "const vec3 RED = vec3(1.0, 0.0, 0.0), BLUE = vec3(0.0, 1.0, 0.0);"

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.GREEN)

        myFixture.checkResult(after)
    }

    fun `test extract and update vec4 constant`() {
        val before = "const vec4 <caret>TINT = vec4(1.0, 0.0, 0.0, 0.5);"
        val after = "const vec4 TINT = vec4(1.0, 0.0, 0.0, 1.0);"

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))
        
        val color = requireNotNull(provider.getColorFrom(element))
        assertEquals(255, color.red)
        assertTrue(color.alpha in 127..128)
        
        provider.setColorTo(element, Color.RED)
        
        myFixture.checkResult(after)
    }
}
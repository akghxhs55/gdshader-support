package kr.jaehoyi.gdshader.codeinsight

import org.junit.Assert.*
import org.junit.Test
import java.awt.Color

class GdsColorUtilTest {
    
    @Test
    fun `test extract standard vec3 color`() {
        val text = "vec3(1.0, 0.0, 0.0)"
        val color = requireNotNull(GdsColorUtil.extractColorFromText(text))

        assertNotNull(color)
        assertEquals(255, color.red)
        assertEquals(0, color.green)
        assertEquals(0, color.blue)
        assertEquals(255, color.alpha)
    }

    @Test
    fun `test extract vec4 color with alpha`() {
        val text = "vec4(0.0, 1.0, 0.0, 0.5)"
        val color = requireNotNull(GdsColorUtil.extractColorFromText(text))

        assertNotNull(color)
        assertEquals(0, color.red)
        assertEquals(255, color.green)
        assertEquals(0, color.blue)
        assertTrue(color.alpha in 127..128)
    }

    @Test
    fun `test extract grayscale with single argument`() {
        val text = "vec3(0.5)"
        val color = requireNotNull(GdsColorUtil.extractColorFromText(text))

        assertTrue(color.red in 127..128)
        assertEquals(color.red, color.green)
        assertEquals(color.green, color.blue)
    }

    @Test
    fun `test extract color with messy whitespace`() {
        val text = "vec3  (  1.0 , 0.5,0.0 ) "
        val color = requireNotNull(GdsColorUtil.extractColorFromText(text))

        assertEquals(255, color.red)
        assertTrue(color.green in 127..128)
        assertEquals(0, color.blue)
    }

    @Test
    fun `test value clamping`() {
        val text = "vec3(2.0, -1.0, 0.5)"
        val color = requireNotNull(GdsColorUtil.extractColorFromText(text))

        assertNotNull(color)
        assertEquals(255, color.red)
        assertEquals(0, color.green)
        assertTrue(color.blue in 127..128)
    }

    @Test
    fun `test invalid formats returns null`() {
        assertNull(GdsColorUtil.extractColorFromText("vec3(1.0, 0.0)"))
        assertNull(GdsColorUtil.extractColorFromText("vec2(1.0, 0.0)"))
        assertNull(GdsColorUtil.extractColorFromText("color(1.0, 0.0, 0.0)"))
        assertNull(GdsColorUtil.extractColorFromText("vec3(1.0, 0.0, 0.0, 1.0)"))
    }

    @Test
    fun `test convert color to vec3 string`() {
        val color = Color(255, 0, 0)
        val result = GdsColorUtil.convertColorToVecString(color, isVec4 = false)

        assertEquals("vec3(1.0, 0.0, 0.0)", result)
    }

    @Test
    fun `test convert color to vec4 string`() {
        val color = Color(0, 255, 0, 128)
        val result = GdsColorUtil.convertColorToVecString(color, isVec4 = true)

        assertEquals("vec4(0.0, 1.0, 0.0, 0.502)", result)
    }

    @Test
    fun `test float formatting logic`() {
        val gray = Color(127, 127, 127)
        val result = GdsColorUtil.convertColorToVecString(gray, isVec4 = false)

        assertEquals("vec3(0.498, 0.498, 0.498)", result)
    }

    @Test
    fun `test trimming zeros`() {
        val gray = Color(51, 51, 51)
        
        val result = GdsColorUtil.convertColorToVecString(gray, isVec4 = false)
        assertEquals("vec3(0.2, 0.2, 0.2)", result)
    }

    @Test
    fun `test color variable name matching`() {
        assertTrue(GdsColorUtil.isColorVariableName("myColor"))
        assertTrue(GdsColorUtil.isColorVariableName("albedoMap"))
        assertTrue(GdsColorUtil.isColorVariableName("tintColor"))
        assertTrue(GdsColorUtil.isColorVariableName("emission"))

        assertTrue(GdsColorUtil.isColorVariableName("red"))
        assertTrue(GdsColorUtil.isColorVariableName("dark_blue"))

        assertTrue(GdsColorUtil.isColorVariableName("MyColor"))
        assertTrue(GdsColorUtil.isColorVariableName("RED"))
    }

    @Test
    fun `test exclude keywords take precedence`() {
        assertFalse(GdsColorUtil.isColorVariableName("colorPosition"))
        assertFalse(GdsColorUtil.isColorVariableName("normalMap"))
        assertFalse(GdsColorUtil.isColorVariableName("sceneDepth"))
    }

    @Test
    fun `test unrelated names return false`() {
        assertFalse(GdsColorUtil.isColorVariableName("vector"))
        assertFalse(GdsColorUtil.isColorVariableName("velocity"))
        assertFalse(GdsColorUtil.isColorVariableName("angle"))
    }
}

package kr.jaehoyi.gdshader.codeinsight

import org.junit.Assert.*
import org.junit.Test
import java.awt.Color

class GDShaderColorUtilTest {
    
    @Test
    fun `test extract standard vec3 color`() {
        val text = "vec3(1.0, 0.0, 0.0)"
        val color = requireNotNull(GDShaderColorUtil.extractColorFromText(text))

        assertNotNull(color)
        assertEquals(255, color.red)
        assertEquals(0, color.green)
        assertEquals(0, color.blue)
        assertEquals(255, color.alpha)
    }

    @Test
    fun `test extract vec4 color with alpha`() {
        val text = "vec4(0.0, 1.0, 0.0, 0.5)"
        val color = requireNotNull(GDShaderColorUtil.extractColorFromText(text))

        assertNotNull(color)
        assertEquals(0, color.red)
        assertEquals(255, color.green)
        assertEquals(0, color.blue)
        assertTrue(color.alpha in 127..128)
    }

    @Test
    fun `test extract grayscale with single argument`() {
        val text = "vec3(0.5)"
        val color = requireNotNull(GDShaderColorUtil.extractColorFromText(text))

        assertTrue(color.red in 127..128)
        assertEquals(color.red, color.green)
        assertEquals(color.green, color.blue)
    }

    @Test
    fun `test extract color with messy whitespace`() {
        val text = "vec3  (  1.0 , 0.5,0.0 ) "
        val color = requireNotNull(GDShaderColorUtil.extractColorFromText(text))

        assertEquals(255, color.red)
        assertTrue(color.green in 127..128)
        assertEquals(0, color.blue)
    }

    @Test
    fun `test value clamping`() {
        val text = "vec3(2.0, -1.0, 0.5)"
        val color = requireNotNull(GDShaderColorUtil.extractColorFromText(text))

        assertNotNull(color)
        assertEquals(255, color.red)
        assertEquals(0, color.green)
        assertTrue(color.blue in 127..128)
    }

    @Test
    fun `test invalid formats returns null`() {
        assertNull(GDShaderColorUtil.extractColorFromText("vec3(1.0, 0.0)"))
        assertNull(GDShaderColorUtil.extractColorFromText("vec2(1.0, 0.0)"))
        assertNull(GDShaderColorUtil.extractColorFromText("color(1.0, 0.0, 0.0)"))
        assertNull(GDShaderColorUtil.extractColorFromText("vec3(1.0, 0.0, 0.0, 1.0)"))
    }

    @Test
    fun `test convert color to vec3 string`() {
        val color = Color(255, 0, 0)
        val result = GDShaderColorUtil.convertColorToVecString(color, isVec4 = false)

        assertEquals("vec3(1.0, 0.0, 0.0)", result)
    }

    @Test
    fun `test convert color to vec4 string`() {
        val color = Color(0, 255, 0, 128)
        val result = GDShaderColorUtil.convertColorToVecString(color, isVec4 = true)

        assertEquals("vec4(0.0, 1.0, 0.0, 0.502)", result)
    }

    @Test
    fun `test float formatting logic`() {
        val gray = Color(127, 127, 127)
        val result = GDShaderColorUtil.convertColorToVecString(gray, isVec4 = false)

        assertEquals("vec3(0.498, 0.498, 0.498)", result)
    }

    @Test
    fun `test trimming zeros`() {
        val gray = Color(51, 51, 51)
        
        val result = GDShaderColorUtil.convertColorToVecString(gray, isVec4 = false)
        assertEquals("vec3(0.2, 0.2, 0.2)", result)
    }

    @Test
    fun `test color variable name matching`() {
        assertTrue(GDShaderColorUtil.isColorVariableName("myColor"))
        assertTrue(GDShaderColorUtil.isColorVariableName("albedoMap"))
        assertTrue(GDShaderColorUtil.isColorVariableName("tintColor"))
        assertTrue(GDShaderColorUtil.isColorVariableName("emission"))

        assertTrue(GDShaderColorUtil.isColorVariableName("red"))
        assertTrue(GDShaderColorUtil.isColorVariableName("dark_blue"))

        assertTrue(GDShaderColorUtil.isColorVariableName("MyColor"))
        assertTrue(GDShaderColorUtil.isColorVariableName("RED"))
    }

    @Test
    fun `test exclude keywords take precedence`() {
        assertFalse(GDShaderColorUtil.isColorVariableName("colorPosition"))
        assertFalse(GDShaderColorUtil.isColorVariableName("normalMap"))
        assertFalse(GDShaderColorUtil.isColorVariableName("sceneDepth"))
    }

    @Test
    fun `test unrelated names return false`() {
        assertFalse(GDShaderColorUtil.isColorVariableName("vector"))
        assertFalse(GDShaderColorUtil.isColorVariableName("velocity"))
        assertFalse(GDShaderColorUtil.isColorVariableName("angle"))
    }
}

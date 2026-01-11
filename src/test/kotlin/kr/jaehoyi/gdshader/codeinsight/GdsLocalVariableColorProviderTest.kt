package kr.jaehoyi.gdshader.codeinsight

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.awt.Color

class GdsLocalVariableColorProviderTest : BasePlatformTestCase() {

    private val provider = GdsLocalVariableColorProvider()

    fun `test extract color from initialized local variable`() {
        val code = """
            void main() {
                vec3 my<caret>Color = vec3(1.0, 0.0, 0.0);
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        val color = requireNotNull(provider.getColorFrom(element))

        assertEquals(255, color.red)
    }

    fun `test extract default black color from uninitialized variable`() {
        val code = """
            void main() {
                vec3 my<caret>Color;
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        val color = requireNotNull(provider.getColorFrom(element))

        assertEquals(Color.BLACK, color)
    }

    fun `test ignore variable if name is not color-related`() {
        val code = """
            void main() {
                vec3 <caret>position = vec3(1.0, 0.0, 0.0);
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        val color = provider.getColorFrom(element)

        assertNull(color)
    }

    fun `test update existing local variable initializer`() {
        val before = """
            void main() {
                vec3 my<caret>Color = vec3(1.0, 0.0, 0.0);
            }
        """.trimIndent()

        val after = """
            void main() {
                vec3 myColor = vec3(0.0, 0.0, 1.0);
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.BLUE)

        myFixture.checkResult(after)
    }

    fun `test insert initializer to uninitialized variable`() {
        val before = """
            void main() {
                vec3 my<caret>Color;
            }
        """.trimIndent()

        val after = """
            void main() {
                vec3 myColor = vec3(1.0, 0.0, 0.0);
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.RED)

        myFixture.checkResult(after)
    }

    fun `test insert initializer in multi-variable declaration`() {
        val before = """
            void main() {
                vec3 a, my<caret>Color, b;
            }
        """.trimIndent()

        val after = """
            void main() {
                vec3 a, myColor = vec3(0.0, 1.0, 0.0), b;
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.GREEN)

        myFixture.checkResult(after)
    }

    fun `test insert vec4 initializer with alpha`() {
        val before = """
            void main() {
                vec4 my<caret>Color;
            }
        """.trimIndent()

        val after = """
            void main() {
                vec4 myColor = vec4(1.0, 0.0, 0.0, 1.0);
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", before)
        val offset = myFixture.editor.caretModel.offset
        val element = requireNotNull(myFixture.file.findElementAt(offset))

        provider.setColorTo(element, Color.RED)

        myFixture.checkResult(after)
    }
    
}

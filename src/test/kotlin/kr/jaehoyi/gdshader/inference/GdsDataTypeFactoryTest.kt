package kr.jaehoyi.gdshader.inference

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.FloatType
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration

class GdsDataTypeFactoryTest : BasePlatformTestCase() {
    fun `test local array size evaluates constant expression`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            void fragment() {
                float values[1 + 2];
            }
            """.trimIndent(),
        )

        val declarator = requireNotNull(PsiTreeUtil.findChildOfType(myFixture.file, GdsLocalVariableDeclarator::class.java))
        val type = GdsDataTypeFactory.createFromLocalVariableDeclaration(declarator)

        assertInstanceOf(type, ArrayType::class.java)
        val arrayType = type as ArrayType
        assertEquals(FloatType.DEFAULT, arrayType.elementType)
        assertEquals("Array size must be evaluated instead of concatenating digits", 3, arrayType.containerSize)
    }

    fun `test uniform array size resolves named constant`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            const int COUNT = 4;
            uniform float values[COUNT];
            """.trimIndent(),
        )

        val declaration = requireNotNull(PsiTreeUtil.findChildOfType(myFixture.file, GdsUniformDeclaration::class.java))
        val type = GdsDataTypeFactory.createFromUniformDeclaration(declaration)

        assertInstanceOf(type, ArrayType::class.java)
        val arrayType = type as ArrayType
        assertEquals(FloatType.DEFAULT, arrayType.elementType)
        assertEquals("Named array size must preserve the array type and its size", 4, arrayType.containerSize)
    }
}

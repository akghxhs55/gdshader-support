package kr.jaehoyi.gdshader.reference

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl

class GdsStructReferenceTest : BasePlatformTestCase() {

    fun `test struct reference resolve`() {
        val code = """
            shader_type canvas_item;

            struct MyCustomData {
                float value;
            };

            void fragment() {
                <caret>MyCustomData data;
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)

        val resolvedElement = myFixture.elementAtCaret

        assertNotNull("Reference should resolve to something", resolvedElement)
        // 3. 타입 검증: 구조체 이름 선언부(GdsStructNameDecl)여야 함
        assertInstanceOf(resolvedElement, GdsStructNameDecl::class.java)
        assertEquals("MyCustomData", (resolvedElement as GdsStructNameDecl).name)
    }

    fun `test struct usage inside another struct`() {
        val code = """
            struct Inner { float a; };
            
            struct Outer {
                <caret>Inner inner_member;
            };
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val resolvedElement = myFixture.elementAtCaret

        assertNotNull(resolvedElement)
        assertEquals("Inner", (resolvedElement as GdsStructNameDecl).name)
    }

    fun `test struct completion`() {
        val code = """
            shader_type canvas_item;
            
            struct PlayerInfo {};

            void fragment() {
                Pla<caret>
            }
        """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        myFixture.completeBasic()

        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)
        assertNotNull(lookupStrings)

        assertTrue("Should contain 'PlayerInfo'", lookupStrings.contains("PlayerInfo"))
    }
    
}
package kr.jaehoyi.gdshader.reference

import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructNameRef

class GdsStructReferenceTest : BasePlatformTestCase() {
    fun `test struct reference resolve`() {
        val code =
            """
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

        assertInstanceOf(resolvedElement, GdsStructNameDecl::class.java)
        assertEquals("MyCustomData", (resolvedElement as GdsStructNameDecl).name)
    }

    fun `test struct usage inside another struct`() {
        val code =
            """
            struct Inner { float a; };
            
            struct Outer {
                <caret>Inner inner_member;
            };
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val resolvedElement = myFixture.elementAtCaret

        assertEquals("Inner", (resolvedElement as GdsStructNameDecl).name)
    }

    fun `test struct declared at end of included file resolves`() {
        // Pad the include so the struct declaration offset exceeds the use-site
        // offset in the main file. Position inside the included file must not matter.
        val padding = "// " + "p".repeat(600) + "\n"
        myFixture.addFileToProject(
            "lib/late_structs.gdshaderinc",
            padding + "struct LateData { float value; };",
        )

        myFixture.addFileToProject(
            "lib/main_structs.gdshader",
            """
            #include "late_structs.gdshaderinc"

            void fragment() {
                <caret>LateData data;
            }
            """.trimIndent(),
        )

        myFixture.configureFromTempProjectFile("lib/main_structs.gdshader")

        val resolved = resolveStructAtCaret()

        assertNotNull("Struct declared after the use-site offset in an included file should still resolve", resolved)
        assertInstanceOf(resolved, GdsStructNameDecl::class.java)
        assertEquals("LateData", (resolved as GdsStructNameDecl).name)
    }

    fun `test struct constructor call resolves to struct`() {
        val code =
            """
            struct FooStruct { float a; };

            const FooStruct FOO = <caret>FooStruct(1.0);
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val resolvedElement = myFixture.elementAtCaret

        assertInstanceOf(resolvedElement, GdsStructNameDecl::class.java)
        assertEquals("FooStruct", (resolvedElement as GdsStructNameDecl).name)
    }

    fun `test struct array constructor call resolves to struct`() {
        val code =
            """
            struct FooStruct { float a; };

            const FooStruct FOO[1] = <caret>FooStruct[1](FooStruct(1.0));
            """.trimIndent()

        myFixture.configureByText("test.gdshader", code)
        val resolvedElement = myFixture.elementAtCaret

        assertInstanceOf(resolvedElement, GdsStructNameDecl::class.java)
    }

    fun `test rename struct updates constructor calls`() {
        val code =
            """
            struct <caret>FooStruct {
                float a;
                float b;
            };

            const FooStruct FOO = FooStruct(1.0, 2.0);
            const FooStruct BAR[1] = FooStruct[1](FooStruct(1.0, 2.0));
            """.trimIndent()

        myFixture.configureByText("foo.gdshaderinc", code)
        myFixture.renameElementAtCaret("BarStruct")

        myFixture.checkResult(
            """
            struct BarStruct {
                float a;
                float b;
            };

            const BarStruct FOO = BarStruct(1.0, 2.0);
            const BarStruct BAR[1] = BarStruct[1](BarStruct(1.0, 2.0));
            """.trimIndent(),
        )
    }

    private fun resolveStructAtCaret(): PsiElement? {
        val elementAtCaret = requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset))

        if (elementAtCaret is GdsStructNameDecl) return elementAtCaret

        val refElement =
            generateSequence(elementAtCaret) { it.parent }
                .takeWhile { it != myFixture.file }
                .firstOrNull { it is GdsStructNameRef } as? GdsStructNameRef

        return refElement?.reference?.resolve()
    }
}

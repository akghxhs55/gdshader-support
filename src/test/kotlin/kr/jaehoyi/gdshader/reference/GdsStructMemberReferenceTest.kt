package kr.jaehoyi.gdshader.reference

import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef

class GdsStructMemberReferenceTest : BasePlatformTestCase() {
    fun `test struct member reference`() {
        val code = """
            shader_type spatial;

            struct MyStruct {
                float my_field;
            };

            void fragment() {
                MyStruct s;
                s.my_field = 1.0;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val file = myFixture.file
        val refs = PsiTreeUtil.findChildrenOfType(file, GdsStructMemberNameRef::class.java)
        val referenceElement = refs.find { it.text == "my_field" }
        val nonNullReferenceElement = requireNotNull(referenceElement) { "Reference element not found" }

        val resolved = nonNullReferenceElement.reference.resolve()
        assertNotNull("Reference not resolved", resolved)
        assertTrue("Resolved element should be GdsStructMemberNameDecl", resolved is GdsStructMemberNameDecl)
        assertEquals("my_field", (resolved as GdsStructMemberNameDecl).name)
    }

    fun `test nested struct member reference`() {
        val code = """
            shader_type spatial;

            struct Inner {
                float val;
            };

            struct Outer {
                Inner inner;
            };

            void fragment() {
                Outer o;
                float v = o.inner.val;
            }
        """
        myFixture.configureByText("test.gdshader", code)

        val file = myFixture.file
        val refs = PsiTreeUtil.findChildrenOfType(file, GdsStructMemberNameRef::class.java)

        val referenceElement = refs.find { it.text == "val" }
        val nonNullReferenceElement = requireNotNull(referenceElement) { "Reference element 'val' not found" }

        val resolved = nonNullReferenceElement.reference.resolve()
        assertNotNull("Reference 'val' not resolved", resolved)
        assertTrue("Resolved element should be GdsStructMemberNameDecl", resolved is GdsStructMemberNameDecl)
        assertEquals("val", (resolved as GdsStructMemberNameDecl).name)
    }

    fun `test renaming struct member updates declaration and references`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            struct Data {
                float <caret>value;
            };
            void fragment() {
                Data data;
                data.value = 1.0;
                ALBEDO = vec3(data.value);
            }
            """.trimIndent(),
        )

        myFixture.renameElementAtCaret("renamed")

        myFixture.checkResult(
            """
            shader_type spatial;
            struct Data {
                float renamed;
            };
            void fragment() {
                Data data;
                data.renamed = 1.0;
                ALBEDO = vec3(data.renamed);
            }
            """.trimIndent(),
        )
    }

    fun `test rename from nested array member usage preserves unrelated members and swizzles`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            struct Inner { float x; };
            struct Outer { Inner inner; };
            struct Other { float x; };
            void fragment() {
                Outer items[2];
                Other other;
                vec3 vector = vec3(1.0);
                items[0].inner.<caret>x = 1.0;
                ALBEDO = vec3(items[1].inner.x + other.x + vector.x);
            }
            """.trimIndent(),
        )

        myFixture.renameElementAtCaret("renamed")

        myFixture.checkResult(
            """
            shader_type spatial;
            struct Inner { float renamed; };
            struct Outer { Inner inner; };
            struct Other { float x; };
            void fragment() {
                Outer items[2];
                Other other;
                vec3 vector = vec3(1.0);
                items[0].inner.renamed = 1.0;
                ALBEDO = vec3(items[1].inner.renamed + other.x + vector.x);
            }
            """.trimIndent(),
        )
        val references =
            PsiTreeUtil
                .findChildrenOfType(myFixture.file, GdsStructMemberNameRef::class.java)
                .filter { it.text == "renamed" }
        assertEquals(2, references.size)
        for (reference in references) {
            assertEquals("renamed", (reference.reference.resolve() as? GdsStructMemberNameDecl)?.name)
        }
    }

    fun `test rename included member updates usages across files`() {
        myFixture.addFileToProject("data.gdshaderinc", "struct Data { float value; };")
        val usageFiles = mutableListOf<PsiFile>()
        for (name in listOf("first", "second")) {
            usageFiles +=
                myFixture.addFileToProject(
                    "$name.gdshader",
                    """
                    shader_type spatial;
                    #include "data.gdshaderinc"
                    void fragment() {
                        Data data;
                        ALBEDO = vec3(data.value);
                    }
                    """.trimIndent(),
                )
        }
        myFixture.configureFromTempProjectFile("data.gdshaderinc")
        val declaration = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsStructMemberNameDecl::class.java).single()
        myFixture.editor.caretModel.moveToOffset(declaration.textOffset)

        myFixture.renameElementAtCaret("renamed")

        myFixture.checkResult("struct Data { float renamed; };")
        for (file in usageFiles) {
            assertEquals(
                """
                shader_type spatial;
                #include "data.gdshaderinc"
                void fragment() {
                    Data data;
                    ALBEDO = vec3(data.renamed);
                }
                """.trimIndent(),
                file.text,
            )
        }
    }
}

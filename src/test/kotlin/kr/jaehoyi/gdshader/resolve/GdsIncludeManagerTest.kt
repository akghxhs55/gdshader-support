package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef

class GdsIncludeManagerTest : BasePlatformTestCase() {
    fun `test transitive include enumerates each function once`() {
        myFixture.addFileToProject("common.gdshaderinc", "float helper() { return 1.0; }")
        myFixture.addFileToProject("wrapper.gdshaderinc", "#include \"common.gdshaderinc\"\n")
        val mainFile =
            myFixture.addFileToProject(
                "main.gdshader",
                """
                shader_type spatial;
                #include "wrapper.gdshaderinc"
                void fragment() {
                    ALBEDO = vec3(helper());
                }
                """.trimIndent(),
            )

        val reference =
            PsiTreeUtil
                .findChildrenOfType(mainFile, GdsFunctionNameRef::class.java)
                .single { it.text == "helper" }
        var helperCount = 0

        GdsResolver.processFunctionDeclaration(reference) { function ->
            if (function.name == "helper") helperCount++
            true
        }

        assertEquals("A transitively included function must not be enumerated twice", 1, helperCount)
    }

    fun `test unresolved function in cyclic includes terminates`() {
        myFixture.addFileToProject("cycle_a.gdshaderinc", "#include \"cycle_b.gdshaderinc\"\n")
        myFixture.addFileToProject("cycle_b.gdshaderinc", "#include \"cycle_a.gdshaderinc\"\n")
        val mainFile =
            myFixture.addFileToProject(
                "main.gdshader",
                """
                shader_type spatial;
                #include "cycle_a.gdshaderinc"
                void fragment() {
                    missing();
                }
                """.trimIndent(),
            )

        val reference =
            PsiTreeUtil
                .findChildrenOfType(mainFile, GdsFunctionNameRef::class.java)
                .single { it.text == "missing" }

        // Unlike a successful lookup, this must traverse the whole include graph.
        try {
            assertNull("Missing function must remain unresolved", reference.reference.resolve())
        } catch (_: StackOverflowError) {
            fail("Reference resolution must terminate even when shader includes form a cycle")
        }
    }

    fun `test shared and repeated includes preserve first visit order`() {
        myFixture.addFileToProject("common.gdshaderinc", "float common() { return 1.0; }")
        myFixture.addFileToProject("left.gdshaderinc", "#include \"common.gdshaderinc\"\nfloat left() { return 1.0; }")
        myFixture.addFileToProject("right.gdshaderinc", "#include \"common.gdshaderinc\"\nfloat right() { return 1.0; }")
        val file =
            myFixture.addFileToProject(
                "main.gdshader",
                """
                shader_type spatial;
                #include "left.gdshaderinc"
                #include "right.gdshaderinc"
                #include "common.gdshaderinc"
                #include "left.gdshaderinc"
                void fragment() {}
                """.trimIndent(),
            )
        val names = mutableListOf<String?>()

        assertTrue(
            processFunctions(file) {
                names.add(it.name)
                true
            },
        )
        assertEquals(listOf("fragment", "left", "common", "right"), names)
    }

    fun `test cycle starting in include does not repeat root declarations`() {
        myFixture.addFileToProject("b.gdshaderinc", "#include \"a.gdshaderinc\"\nfloat b() { return 1.0; }")
        val file =
            myFixture.addFileToProject(
                "a.gdshaderinc",
                """
                #include "a.gdshaderinc"
                #include "b.gdshaderinc"
                float a() { return 1.0; }
                """.trimIndent(),
            )
        val names = mutableListOf<String?>()

        assertTrue(
            processFunctions(file) {
                names.add(it.name)
                true
            },
        )
        assertEquals(listOf("a", "b"), names)
    }

    fun `test processor can stop in root or included declarations`() {
        myFixture.addFileToProject("first.gdshaderinc", "float first() { return 1.0; }\nfloat next() { return 2.0; }")
        myFixture.addFileToProject("last.gdshaderinc", "float last() { return 1.0; }")
        val file =
            myFixture.addFileToProject(
                "main.gdshader",
                """
                shader_type spatial;
                #include "first.gdshaderinc"
                #include "last.gdshaderinc"
                void fragment() {}
                """.trimIndent(),
            )
        val names = mutableListOf<String?>()

        assertFalse(
            processFunctions(file) {
                names.add(it.name)
                false
            },
        )
        assertEquals(listOf("fragment"), names)

        names.clear()
        assertFalse(
            processFunctions(file) {
                names.add(it.name)
                it.name != "first"
            },
        )
        assertEquals(listOf("fragment", "first"), names)
    }

    fun `test different overloads in included files are preserved`() {
        myFixture.addFileToProject("float.gdshaderinc", "float helper(float value) { return value; }")
        myFixture.addFileToProject("int.gdshaderinc", "int helper(int value) { return value; }")
        val file =
            myFixture.addFileToProject(
                "main.gdshader",
                """
                shader_type spatial;
                #include "float.gdshaderinc"
                #include "int.gdshaderinc"
                """.trimIndent(),
            )
        val functions = mutableListOf<GdsFunction>()

        assertTrue(
            processFunctions(file) {
                functions.add(it)
                true
            },
        )
        assertEquals(listOf("helper", "helper"), functions.map { it.name })
        assertEquals(listOf("float.gdshaderinc", "int.gdshaderinc"), functions.map { it.containingFile.name })
    }

    private fun processFunctions(
        file: PsiFile,
        processor: (GdsFunction) -> Boolean,
    ): Boolean =
        file.processDeclarations(
            GdsScopeProcessor(GdsFunction::class.java, Int.MAX_VALUE, file.originalFile, processor),
            ResolveState.initial(),
            null,
            file,
        )
}

package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
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
}

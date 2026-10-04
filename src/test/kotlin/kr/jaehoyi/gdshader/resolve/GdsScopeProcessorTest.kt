package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.ResolveState
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.FloatType
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef

class GdsScopeProcessorTest : BasePlatformTestCase() {
    fun `test all constants are enumerated in source order and can stop early`() {
        myFixture.configureByText("test.gdshader", "const float A = 1.0, B = 2.0, C = 3.0;")
        val names = mutableListOf<String?>()
        assertTrue(
            processVariables(Int.MAX_VALUE) {
                names.add(it.name)
                true
            },
        )
        assertEquals(listOf("A", "B", "C"), names)

        names.clear()
        assertFalse(
            processVariables(Int.MAX_VALUE) {
                names.add(it.name)
                it.name != "B"
            },
        )
        assertEquals(listOf("A", "B"), names)
    }

    fun `test position filter applies to each declarator`() {
        myFixture.configureByText("test.gdshader", "const float A = 1.0, B = 2.0, C = 3.0;")
        val second = declarations().single { it.name == "B" }
        val names = mutableListOf<String?>()
        assertTrue(
            processVariables(second.textOffset) {
                names.add(it.name)
                true
            },
        )
        assertEquals(listOf("A"), names)
    }

    fun `test included constants resolve and determine array size`() {
        myFixture.addFileToProject("constants.gdshaderinc", "\n".repeat(100) + "const int FIRST = 1, COUNT = 4;")
        myFixture.addFileToProject(
            "main.gdshader",
            "shader_type spatial;\n#include \"constants.gdshaderinc\"\nuniform float values[COUNT];",
        )
        myFixture.configureFromTempProjectFile("main.gdshader")
        val reference =
            PsiTreeUtil
                .findChildrenOfType(myFixture.file, GdsVariableNameRef::class.java)
                .single { it.text == "COUNT" }
        val resolved = reference.reference.resolve() as? GdsVariableNameDecl
        assertNotNull(resolved)
        assertEquals("constants.gdshaderinc", resolved!!.containingFile.name)
        val values = declarations().single { it.name == "values" }
        assertEquals(ArrayType(FloatType.DEFAULT, 4), values.variableSpec?.type)
    }

    fun `test second constant supports usage search and rename`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            const float A = 1.0, <caret>B = 2.0;
            void fragment() { ALBEDO = vec3(B + B); }
            """.trimIndent(),
        )
        val declaration = declarations().single { it.name == "B" }
        assertEquals(2, ReferencesSearch.search(declaration).findAll().size)
        myFixture.renameElementAtCaret("RENAMED")
        myFixture.checkResult(
            """
            shader_type spatial;
            const float A = 1.0, RENAMED = 2.0;
            void fragment() { ALBEDO = vec3(RENAMED + RENAMED); }
            """.trimIndent(),
        )
    }

    private fun declarations(): Collection<GdsVariableNameDecl> = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsVariableNameDecl::class.java)

    private fun processVariables(
        offset: Int,
        processor: (GdsVariable) -> Boolean,
    ): Boolean =
        myFixture.file.processDeclarations(
            GdsScopeProcessor(GdsVariable::class.java, offset, myFixture.file.originalFile, processor),
            ResolveState.initial(),
            null,
            myFixture.file,
        )
}

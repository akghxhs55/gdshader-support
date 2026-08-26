package kr.jaehoyi.gdshader.reference

import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction

class GdsFunctionReferenceTest : BasePlatformTestCase() {
    fun `test user function resolve`() {
        val code = """
            shader_type canvas_item;

            void my_custom_func(float x) {}

            void fragment() {
                <caret>my_custom_func(1.0);
            }
        """
        doTest(code) { spec ->
            assertEquals("my_custom_func", spec.name)
            assertEquals(1, spec.parameters.size)
        }
    }

    fun `test builtin function resolve`() {
        val code = """
            shader_type canvas_item;
            void fragment() {
                float y = <caret>sin(0.5);
            }
        """
        doTest(code) { spec ->
            assertEquals("sin", spec.name)
        }
    }

    private fun doTest(
        code: String,
        checkSpec: (FunctionSpec) -> Unit,
    ) {
        myFixture.configureByText("test_shader.gdshader", code)

        val elementAtCaret =
            requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset)) {
                "No element found at caret"
            }

        val refElement =
            requireNotNull(elementAtCaret.parent as? GdsFunctionNameRef) {
                "Parent is not GdsFunctionNameRef. Found: ${elementAtCaret.parent}"
            }

        val resolvedElement =
            requireNotNull(refElement.reference.resolve()) {
                "Reference failed to resolve: ${refElement.text}"
            }

        if (resolvedElement is GdsFunctionNameDecl) {
            val spec = requireNotNull(resolvedElement.functionSpec) { "FunctionSpec is null for Decl" }
            checkSpec(spec)
            return
        } else if (resolvedElement is GdsLightFunction) {
            val spec = requireNotNull(resolvedElement.functionSpec) { "FunctionSpec is null for LightFunction" }
            checkSpec(spec)
            return
        }

        fail("Resolved element is of unexpected type: ${resolvedElement::class.java}")
    }

    fun `test resolve function from included file with relative path`() {
        myFixture.addFileToProject(
            "lib/math_utils.gdshaderinc",
            "float calculate_pi() { return 3.14; }",
        )

        val mainFile = "lib/mylib.gdshader"
        myFixture.addFileToProject(
            mainFile,
            """
            #include "math_utils.gdshaderinc"

            void run() {
                float p = calculate<caret>_pi();
            }
            """.trimIndent(),
        )

        myFixture.configureFromTempProjectFile(mainFile)

        val element = myFixture.elementAtCaret

        assertTrue(element is GdsFunctionNameDecl)
        assertEquals("calculate_pi", element.text)
        assertTrue(element.containingFile.name.contains("math_utils.gdshaderinc"))
    }

    fun `test resolve function declared at end of included file`() {
        // Pad the include so the declaration offset exceeds the use-site offset
        // in the main file. Position inside the included file must not matter.
        val padding = "// " + "p".repeat(600) + "\n"
        myFixture.addFileToProject(
            "lib/late_functions.gdshaderinc",
            padding + "float late_function() { return 1.0; }",
        )

        myFixture.addFileToProject(
            "lib/main_late.gdshader",
            """
            #include "late_functions.gdshaderinc"

            void fragment() {
                float v = late<caret>_function();
            }
            """.trimIndent(),
        )

        myFixture.configureFromTempProjectFile("lib/main_late.gdshader")

        val resolved = resolveFunctionAtCaret()

        assertNotNull("Function declared after the use-site offset in an included file should still resolve", resolved)
        assertEquals("late_function", resolved!!.text)
    }

    fun `test resolve function through nested includes`() {
        myFixture.addFileToProject(
            "lib/leaf_functions.gdshaderinc",
            "float leaf_function() { return 2.0; }",
        )
        myFixture.addFileToProject(
            "lib/umbrella.gdshaderinc",
            "#include \"leaf_functions.gdshaderinc\"",
        )

        myFixture.addFileToProject(
            "lib/main_nested.gdshader",
            """
            #include "umbrella.gdshaderinc"

            void fragment() {
                float v = leaf<caret>_function();
            }
            """.trimIndent(),
        )

        myFixture.configureFromTempProjectFile("lib/main_nested.gdshader")

        val resolved = resolveFunctionAtCaret()

        assertNotNull("Function from a transitively included file should resolve", resolved)
        assertEquals("leaf_function", resolved!!.text)
    }

    fun `test same file function declared after use does not resolve`() {
        val code =
            """
            shader_type canvas_item;

            void fragment() {
                float v = future<caret>_func();
            }

            float future_func() { return 1.0; }
            """.trimIndent()

        myFixture.configureByText("test_shader.gdshader", code)

        val resolved = resolveFunctionAtCaret()

        assertNull("Same-file declarations after the use site must stay unresolved", resolved)
    }

    private fun resolveFunctionAtCaret(): PsiElement? {
        val elementAtCaret = requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset))

        if (elementAtCaret is GdsFunctionNameDecl) return elementAtCaret

        val refElement =
            generateSequence(elementAtCaret) { it.parent }
                .takeWhile { it != myFixture.file }
                .firstOrNull { it is GdsFunctionNameRef } as? GdsFunctionNameRef

        return refElement?.reference?.resolve()
    }
}

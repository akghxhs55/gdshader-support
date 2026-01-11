package kr.jaehoyi.gdshader.reference

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.functionSpec
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction

class GdsFunctionReferenceTest : BasePlatformTestCase() {

    fun testUserFunctionResolve() {
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

    fun testBuiltinFunctionResolve() {
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

    private fun doTest(code: String, checkSpec: (FunctionSpec) -> Unit) {
        myFixture.configureByText("test_shader.gdshader", code)

        val elementAtCaret = requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset)) {
            "No element found at caret"
        }

        val refElement = requireNotNull(elementAtCaret.parent as? GdsFunctionNameRef) {
            "Parent is not GdsFunctionNameRef. Found: ${elementAtCaret.parent}"
        }

        val resolvedElement = requireNotNull(refElement.reference.resolve()) {
            "Reference failed to resolve: ${refElement.text}"
        }

        if (resolvedElement is GdsFunctionNameDecl) {
            val spec = requireNotNull(resolvedElement.functionSpec) { "FunctionSpec is null for Decl" }
            checkSpec(spec)
            return
        }
        else if (resolvedElement is GdsLightFunction) {
            val spec = requireNotNull(resolvedElement.functionSpec) { "FunctionSpec is null for LightFunction" }
            checkSpec(spec)
            return
        }

        fail("Resolved element is of unexpected type: ${resolvedElement::class.java}")
    }
    
}

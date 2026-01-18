package kr.jaehoyi.gdshader.reference

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.LocalVariableSpec
import kr.jaehoyi.gdshader.model.ParameterSpec
import kr.jaehoyi.gdshader.model.UniformSpec
import kr.jaehoyi.gdshader.model.VariableSpec
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable

class GdsVariableReferenceTest : BasePlatformTestCase() {

    fun testLocalVariableResolve() {
        val code = """
            void fragment() {
                float my_var = 1.0;
                float b = <caret>my_var + 2.0;
            }
        """
        doTest(code) { spec ->
            assertEquals("my_var", spec.name)
            assertInstanceOf(spec, LocalVariableSpec::class.java)
            assertTrue(spec.isMutable)
        }
    }
    
    fun testForInitResolve() {
        val code = """
            void fragment() {
                for (int i = 0; i < 10; i++) {
                    float a = <caret>i * 2.0;
                }
            }
        """
        doTest(code) { spec ->
            assertEquals("i", spec.name)
            assertInstanceOf(spec, LocalVariableSpec::class.java)
            assertTrue(spec.isMutable)
        }
    }

    fun testParameterResolve() {
        val code = """
            void my_func(float param_a) {
                float b = <caret>param_a;
            }
        """
        doTest(code) { spec ->
            assertEquals("param_a", spec.name)
            assertInstanceOf(spec, ParameterSpec::class.java)
        }
    }

    fun testUniformResolve() {
        val code = """
            shader_type spatial;
            uniform float u_time;
            void fragment() {
                float t = <caret>u_time;
            }
        """
        doTest(code) { spec ->
            assertEquals("u_time", spec.name)
            assertInstanceOf(spec, UniformSpec::class.java)
        }
    }

    fun testConstantResolve() {
        val code = """
            const float MAX_SPEED = 10.0;
            void fragment() {
                float s = <caret>MAX_SPEED;
            }
        """
        doTest(code) { spec ->
            assertEquals("MAX_SPEED", spec.name)
            assertInstanceOf(spec, ConstantSpec::class.java)
            assertFalse(spec.isMutable)
        }
    }

    fun testBuiltinVariableResolve() {
        val code = """
            shader_type canvas_item;
            void fragment() {
                <caret>COLOR = vec4(1.0);
            }
        """
        doTest(code) { spec ->
            assertEquals("COLOR", spec.name) 
        }
    }

    fun testShadowingResolve() {
        val code = """
            uniform float value;
            void fragment() {
                float value = 1.0;
                float a = <caret>value;
            }
        """
        doTest(code) { spec ->
            assertEquals("value", spec.name)
            assertInstanceOf(spec, LocalVariableSpec::class.java)
        }
    }

    private fun doTest(code: String, checkSpec: (VariableSpec) -> Unit) {
        myFixture.configureByText("test_shader.gdshader", code)

        val elementAtCaret = requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset))

        val refElement = requireNotNull(elementAtCaret.parent as? GdsVariableNameRef)

        val resolvedElement = requireNotNull(refElement.reference.resolve())

        if (resolvedElement is GdsVariableNameDecl) {
            val spec = requireNotNull(resolvedElement.variableSpec)
            checkSpec(spec)
            return
        }
        else if (resolvedElement is GdsLightVariable) {
            val spec = requireNotNull(resolvedElement.variableSpec)
            checkSpec(spec)
            return
        }
    }

    fun `test resolve variable from included file`() {
        val includedFile = myFixture.addFileToProject(
            "utils.gdshaderinc",
            "uniform float u_global_time;"
        )

        myFixture.configureByText("main.gdshader", """
            #include "res://utils.gdshaderinc"
            
            void fragment() {
                float t = u_global_<caret>time;
            }
        """.trimIndent())

        val element = myFixture.elementAtCaret

        assertTrue("Reference should resolve to a variable declaration", element is GdsVariableNameDecl)
        assertEquals("u_global_time", element.text)

        assertEquals(
            "Resolved element should be in the included file",
            includedFile.virtualFile.path,
            element.containingFile.virtualFile.path
        )
    }

    fun `test cyclic include does not crash`() {
        myFixture.addFileToProject("cycle_a.gdshaderinc", """
            #include "res://cycle_b.gdshaderinc"
            uniform float var_a;
        """.trimIndent())

        myFixture.addFileToProject("cycle_b.gdshaderinc", """
            #include "res://cycle_a.gdshaderinc"
            uniform float var_b;
        """.trimIndent())

        myFixture.configureByText("main.gdshader", """
            #include "res://cycle_a.gdshaderinc"
            void f() {
                var_a; 
                var_<caret>b; 
            }
        """.trimIndent())

        val element = myFixture.elementAtCaret

        assertEquals("var_b", element.text)
    }
    
}

package kr.jaehoyi.gdshader.reference

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.StorageQualifier
import kr.jaehoyi.gdshader.model.VariableSpec
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameRef
import kr.jaehoyi.gdshader.psi.impl.GDShaderLightVariable
import kr.jaehoyi.gdshader.psi.variableSpec

class GDShaderVariableReferenceTest : BasePlatformTestCase() {

    fun testLocalVariableResolve() {
        val code = """
            void fragment() {
                float my_var = 1.0;
                float b = <caret>my_var + 2.0;
            }
        """
        doTest(code) { spec ->
            assertEquals("my_var", spec.name)
            assertEquals(StorageQualifier.LOCAL, spec.storageQualifier)
            assertFalse(spec.isReadOnly)
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
            assertEquals(StorageQualifier.PARAMETER, spec.storageQualifier)
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
            assertEquals(StorageQualifier.UNIFORM, spec.storageQualifier)
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
            assertTrue(spec.isReadOnly)
            assertTrue(spec.storageQualifier == StorageQualifier.CONSTANT)
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
            assertEquals(StorageQualifier.LOCAL, spec.storageQualifier)
        }
    }

    private fun doTest(code: String, checkSpec: (VariableSpec) -> Unit) {
        myFixture.configureByText("test_shader.gdshader", code)

        val elementAtCaret = requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset))

        val refElement = requireNotNull(elementAtCaret.parent as? GDShaderVariableNameRef)

        val resolvedElement = requireNotNull(refElement.reference.resolve())

        if (resolvedElement is GDShaderVariableNameDecl) {
            val spec = requireNotNull(resolvedElement.variableSpec)
            checkSpec(spec)
            return
        }
        else if (resolvedElement is GDShaderLightVariable) {
            val spec = requireNotNull(resolvedElement.variableSpec)
            checkSpec(spec)
            return
        }
    }
}
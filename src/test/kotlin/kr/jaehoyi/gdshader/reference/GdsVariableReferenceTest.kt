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
import kr.jaehoyi.gdshader.psi.variableSpec

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
}
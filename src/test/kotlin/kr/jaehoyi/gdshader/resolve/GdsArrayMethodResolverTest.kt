package kr.jaehoyi.gdshader.resolve

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.FloatType
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction

class GdsArrayMethodResolverTest : BasePlatformTestCase() {
    fun `test array methods resolve independently of global functions`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            float length(float x) { return x; }
            struct Data { float values[3]; };
            void fragment() {
                float values[3];
                float unknown[];
                Data data[2];
                int a = values.length();
                int b = unknown.length();
                int c = data[0].values.length();
                float d = length(vec3(1.0));
            }
            """.trimIndent(),
        )
        val calls =
            PsiTreeUtil
                .findChildrenOfType(myFixture.file, GdsFunctionCall::class.java)
                .filter { it.functionNameRef?.text == "length" }
        assertEquals(4, calls.size)
        for (call in calls) {
            if (GdsArrayMethodResolver.isMemberCall(call)) {
                val resolved = call.functionNameRef!!.reference.resolve() as? GdsLightFunction
                assertNotNull(resolved)
                assertEmpty(resolved!!.functionSpec.parameters)
                assertEquals(IntType, GdsExpressionTypeInference.inferType(call))
            } else {
                assertNull(GdsArrayMethodResolver.resolve(call))
                assertEquals(FloatType.DEFAULT, GdsExpressionTypeInference.inferType(call))
            }
        }
    }

    fun `test member diagnostics never fall back to global functions or struct constructors`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            struct length { float value; };
            void fragment() {
                float scalar;
                scalar.<warning descr="Unresolved reference 'length'">length</warning>();
                <warning descr="Unresolved reference 'missing'">missing</warning>.length();
                float values[];
                int count = values.length();
            }
            """.trimIndent(),
        )
        myFixture.checkHighlighting(true, false, true)
    }

    fun `test invalid member calls do not resolve to global functions or constructors`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            struct length { float value; };
            void fragment() {
                float scalar;
                scalar.length();
                missing.length();
                float values[3];
                values.sin(1.0);
            }
            """.trimIndent(),
        )
        for (call in PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionCall::class.java)) {
            assertTrue(GdsArrayMethodResolver.isMemberCall(call))
            assertNull(call.functionNameRef!!.reference.resolve())
            assertNull(GdsExpressionTypeInference.inferType(call))
        }
    }
}

package kr.jaehoyi.gdshader.codeinsight

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.utils.parameterInfo.MockCreateParameterInfoContext
import com.intellij.testFramework.utils.parameterInfo.MockUpdateParameterInfoContext
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.psi.GdsFunctionCall

class GdsParameterInfoHandlerTest : BasePlatformTestCase() {
    private val structs =
        """
        shader_type canvas_item;
        struct InnerStruct { float a; vec2 b; };
        struct OuterStruct { float x; InnerStruct y; };
        """.trimIndent()

    fun `test nested struct constructor with empty arguments`() {
        val items =
            showParameterInfo(
                """
                $structs
                void fragment() {
                    OuterStruct o = OuterStruct(
                        1.0,
                        InnerStruct(<caret>)
                    );
                }
                """.trimIndent(),
            )

        assertEquals(listOf("InnerStruct(a, b)"), items.map(::signature))
    }

    fun `test nested call with caret before closing parenthesis`() {
        val items =
            showParameterInfo(
                """
                $structs
                void fragment() {
                    OuterStruct o = OuterStruct(1.0, InnerStruct(1.0<caret>));
                }
                """.trimIndent(),
            )

        assertEquals(listOf("InnerStruct(a, b)"), items.map(::signature))
    }

    fun `test caret before nested call name belongs to outer call`() {
        val items =
            showParameterInfo(
                """
                $structs
                void fragment() {
                    OuterStruct o = OuterStruct(1.0, <caret>InnerStruct(1.0, vec2(0.0)));
                }
                """.trimIndent(),
            )

        assertEquals(listOf("OuterStruct(x, y)"), items.map(::signature))
    }

    fun `test nested builtin function call`() {
        val items =
            showParameterInfo(
                """
                shader_type spatial;
                void fragment() {
                    float v = max(1.0, min(2.0<caret>));
                }
                """.trimIndent(),
            )

        assertTrue(items.isNotEmpty())
        assertTrue(items.all { it.name == "min" })
    }

    fun `test update keeps innermost call`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            $structs
            void fragment() {
                OuterStruct o = OuterStruct(1.0, InnerStruct(<caret>));
            }
            """.trimIndent(),
        )

        val context = MockUpdateParameterInfoContext(myFixture.editor, myFixture.file)
        val argumentList = GdsParameterInfoHandler().findElementForUpdatingParameterInfo(context)

        val call = argumentList?.parent as? GdsFunctionCall
        assertEquals("InnerStruct()", call?.text)
    }

    private fun showParameterInfo(code: String): List<FunctionSpec> {
        myFixture.configureByText("test.gdshader", code)
        val context = MockCreateParameterInfoContext(myFixture.editor, myFixture.file)
        assertNotNull(GdsParameterInfoHandler().findElementForParameterInfo(context))
        return context.itemsToShow.orEmpty().map { it as FunctionSpec }
    }

    private fun signature(spec: FunctionSpec): String = "${spec.name}(${spec.parameters.joinToString { it.name }})"
}

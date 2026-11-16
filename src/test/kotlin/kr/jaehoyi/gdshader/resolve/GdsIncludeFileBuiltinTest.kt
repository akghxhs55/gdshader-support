package kr.jaehoyi.gdshader.resolve

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.lang.LanguageDocumentation
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.utils.parameterInfo.MockCreateParameterInfoContext
import kr.jaehoyi.gdshader.codeinsight.GdsParameterInfoHandler
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.model.VectorType
import kr.jaehoyi.gdshader.psi.GdsExpression
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable

class GdsIncludeFileBuiltinTest : BasePlatformTestCase() {
    fun `test builtin function resolves in include file`() {
        configureInclude(
            """
            vec2 rotate(vec2 uv) {
                float a = <caret>atan(uv.x / uv.y);
                return uv;
            }
            """,
        )

        val resolved = functionRefAtCaret().reference.resolve()
        assertInstanceOf(resolved, GdsLightFunction::class.java)
        assertEquals("atan", (resolved as GdsLightFunction).functionSpec.name)
    }

    fun `test builtin function documentation in include file`() {
        configureInclude(
            """
            void f() {
                float a = <caret>sin(1.0);
            }
            """,
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("vec_type sin(vec_type angle)"))
    }

    fun `test builtin function documentation without shader type in gdshader file`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void f() {
                float a = <caret>sin(1.0);
            }
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        assertNotNull(provider.generateDoc(element, null))
    }

    fun `test parameter info in include file has no duplicate overloads`() {
        configureInclude(
            """
            void f() {
                float a = atan(<caret>1.0);
            }
            """,
        )

        val context = MockCreateParameterInfoContext(myFixture.editor, myFixture.file)
        assertNotNull(GdsParameterInfoHandler().findElementForParameterInfo(context))

        val items = requireNotNull(context.itemsToShow).map { it as FunctionSpec }
        val expected = Builtins.getFunctions(ShaderType.SPATIAL, FunctionContext.COMMON, "atan")!!
        assertEquals(expected.size, items.size)
        assertTrue(items.all { it.name == "atan" })
    }

    fun `test shader type specific builtin is available in include file`() {
        configureInclude(
            """
            void f() {
                <caret>emit_subparticle(mat4(1.0), vec3(0.0), vec4(0.0), vec4(0.0), 0u);
            }
            """,
        )

        assertInstanceOf(functionRefAtCaret().reference.resolve(), GdsLightFunction::class.java)
    }

    fun `test builtin variable with same type across shader types keeps its type`() {
        configureInclude(
            """
            void f() {
                float t = <caret>TIME;
            }
            """,
        )

        val ref = variableRefAtCaret()
        val resolved = ref.reference.resolve() as GdsLightVariable
        assertFalse(resolved.isTypeAmbiguous)
        assertNotNull(GdsExpressionTypeInference.inferType(PsiTreeUtil.getParentOfType(ref, GdsExpression::class.java)!!))
    }

    fun `test builtin variable with conflicting types is resolved but type is unknown`() {
        configureInclude(
            """
            void vertex() {
                <caret>VERTEX;
            }
            """,
        )

        val ref = variableRefAtCaret()
        val resolved = ref.reference.resolve() as GdsLightVariable
        assertTrue(resolved.isTypeAmbiguous)
        assertEquals("VERTEX", resolved.variableSpec.name)

        val expression = PsiTreeUtil.getParentOfType(ref, GdsExpression::class.java)!!
        assertNull(GdsExpressionTypeInference.inferType(expression))
    }

    fun `test builtin variable keeps shader specific type when shader type is declared`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            void vertex() {
                <caret>VERTEX;
            }
            """.trimIndent(),
        )

        val ref = variableRefAtCaret()
        val resolved = ref.reference.resolve() as GdsLightVariable
        assertFalse(resolved.isTypeAmbiguous)

        val expression = PsiTreeUtil.getParentOfType(ref, GdsExpression::class.java)!!
        assertEquals(VectorType.VEC2, GdsExpressionTypeInference.inferType(expression))
    }

    fun `test completion in include file includes builtins of all shader types`() {
        configureInclude(
            """
            void f() {
                <caret>
            }
            """,
        )

        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
        try {
            myFixture.completeBasic()
        } finally {
            CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        }
        val lookupStrings = requireNotNull(myFixture.lookupElementStrings)

        assertTrue(lookupStrings.contains("sin"))
        assertTrue(lookupStrings.contains("emit_subparticle"))
        assertTrue(lookupStrings.contains("TIME"))
    }

    private fun configureInclude(code: String) {
        myFixture.configureByText("test.gdshaderinc", code.trimIndent())
    }

    private fun functionRefAtCaret(): GdsFunctionNameRef {
        val leaf = requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset))
        return requireNotNull(PsiTreeUtil.getParentOfType(leaf, GdsFunctionNameRef::class.java, false))
    }

    private fun variableRefAtCaret(): GdsVariableNameRef {
        val leaf = requireNotNull(myFixture.file.findElementAt(myFixture.caretOffset))
        return requireNotNull(PsiTreeUtil.getParentOfType(leaf, GdsVariableNameRef::class.java, false))
    }
}

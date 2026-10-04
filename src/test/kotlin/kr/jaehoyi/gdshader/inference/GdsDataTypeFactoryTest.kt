package kr.jaehoyi.gdshader.inference

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver

class GdsDataTypeFactoryTest : BasePlatformTestCase() {
    fun `test local array size evaluates constant expression`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            void fragment() {
                float values[1 + 2];
            }
            """.trimIndent(),
        )

        val declarator = requireNotNull(PsiTreeUtil.findChildOfType(myFixture.file, GdsLocalVariableDeclarator::class.java))
        val type = GdsDataTypeFactory.createFromLocalVariableDeclaration(declarator)

        assertInstanceOf(type, ArrayType::class.java)
        val arrayType = type as ArrayType
        assertEquals(FloatType.DEFAULT, arrayType.elementType)
        assertEquals("Array size must be evaluated instead of concatenating digits", 3, arrayType.containerSize)
    }

    fun `test uniform array size resolves named constant`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            const int COUNT = 4;
            uniform float values[COUNT];
            """.trimIndent(),
        )

        val declaration = requireNotNull(PsiTreeUtil.findChildOfType(myFixture.file, GdsUniformDeclaration::class.java))
        val type = GdsDataTypeFactory.createFromUniformDeclaration(declaration)

        assertInstanceOf(type, ArrayType::class.java)
        val arrayType = type as ArrayType
        assertEquals(FloatType.DEFAULT, arrayType.elementType)
        assertEquals("Named array size must preserve the array type and its size", 4, arrayType.containerSize)
    }

    fun `test array sizes use integer constant evaluation`() {
        val cases = mapOf("(1 + 2) * 3" to 9, "0x10" to 16, "2u + 3u" to 5, "int(3.8)" to 3)
        for ((expression, size) in cases) {
            configure("void fragment() { float values[$expression]; }")
            assertEquals(expression, ArrayType(FloatType.DEFAULT, size), variableType("values"))
        }
    }

    fun `test unknown and invalid sizes preserve array type`() {
        for (expression in listOf("", "MISSING", "0", "-2", "2.5", "true", "1 / 0", "2147483648u")) {
            configure("void fragment() { float values[$expression]; }")
            assertEquals(expression, ArrayType(FloatType.DEFAULT), variableType("values"))
        }
        configure("void fragment() { int count = 3; float values[count]; }")
        assertEquals(ArrayType(FloatType.DEFAULT), variableType("values"))
    }

    fun `test every declaration path retains arrays`() {
        for ((size, expected) in listOf("1 + 2" to 3, "MISSING" to null)) {
            configure(
                """
                uniform float uniform_values[$size];
                varying float varying_values[$size];
                const float const_values[$size] = {1.0, 2.0, 3.0};
                void helper(float parameter_values[$size]) {}
                void fragment() {
                    float local_values[$size];
                    float[$size] type_values;
                    const float[$size] type_constants = {1.0, 2.0, 3.0};
                    for (float loop_values[$size]; false;) {}
                    float scalar_value;
                }
                """.trimIndent(),
            )
            for (name in listOf(
                "uniform_values",
                "varying_values",
                "const_values",
                "parameter_values",
                "local_values",
                "type_values",
                "type_constants",
                "loop_values",
            )) {
                assertEquals(name, ArrayType(FloatType.DEFAULT, expected), variableType(name))
            }
            assertEquals(FloatType.DEFAULT, variableType("scalar_value"))
        }
    }

    fun `test include constants and literal defines determine size`() {
        myFixture.addFileToProject("sizes.gdshaderinc", "const int COUNT = 3;\n#define EXTRA 2\n")
        myFixture.addFileToProject(
            "main.gdshader",
            "shader_type spatial;\n#include \"sizes.gdshaderinc\"\nuniform float values[COUNT + EXTRA];",
        )
        myFixture.configureFromTempProjectFile("main.gdshader")
        assertEquals(ArrayType(FloatType.DEFAULT, 5), variableType("values"))
    }

    fun `test unsized initialized arrays remain unknown`() {
        configure("void fragment() { float values[] = float[](1.0, 2.0); }")
        assertEquals(ArrayType(FloatType.DEFAULT), variableType("values"))
        val constructor = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionCall::class.java).single()
        assertEquals(ArrayType(FloatType.DEFAULT), GdsExpressionTypeInference.inferType(constructor))
    }

    fun `test constructor and struct member sizes follow same policy`() {
        for ((size, expected) in listOf("1 + 2" to 3, "true" to null, "MISSING" to null)) {
            configure(
                """
                struct Data { float values[$size]; };
                void fragment() {
                    Data data;
                    float result = data.values[0];
                    float array[] = float[$size](1.0, 2.0, 3.0);
                }
                """.trimIndent(),
            )
            val struct = variableType("data") as StructType
            assertEquals(ArrayType(FloatType.DEFAULT, expected), struct.resolveMember("values"))
            val constructor = PsiTreeUtil.findChildrenOfType(myFixture.file, GdsFunctionCall::class.java).single()
            assertEquals(ArrayType(FloatType.DEFAULT, expected), GdsExpressionTypeInference.inferType(constructor))
            val result =
                PsiTreeUtil
                    .findChildrenOfType(myFixture.file, GdsLocalVariableDeclarator::class.java)
                    .single { it.variableNameDecl.name == "result" }
            assertEquals(FloatType.DEFAULT, GdsExpressionTypeInference.inferType(requireNotNull(result.initializer?.expression)))
        }
    }

    fun `test evaluated array size matches function parameter without relaxing unknown size`() {
        configure(
            """
            void helper(float values[3]) {}
            void fragment() { float known[1 + 2]; float unknown[]; }
            """.trimIndent(),
        )
        val function =
            PsiTreeUtil
                .findChildrenOfType(myFixture.file, GdsFunctionNameDecl::class.java)
                .single { it.name == "helper" }
                .functionSpec!!
        assertEquals(function, GdsOverloadResolver.resolveFunctionOverload(listOf(function), listOf(variableType("known")!!)))
        assertNull(GdsOverloadResolver.resolveFunctionOverload(listOf(function), listOf(variableType("unknown")!!)))
    }

    private fun configure(code: String) {
        myFixture.configureByText("test.gdshader", "shader_type spatial;\n$code")
    }

    private fun variableType(name: String): DataType? =
        PsiTreeUtil
            .findChildrenOfType(myFixture.file, GdsVariableNameDecl::class.java)
            .single { it.name == name }
            .variableSpec
            ?.type
}

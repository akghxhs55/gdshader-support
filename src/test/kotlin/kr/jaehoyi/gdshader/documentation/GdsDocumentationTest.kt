package kr.jaehoyi.gdshader.documentation

import com.intellij.lang.LanguageDocumentation
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsDocumentationTest : BasePlatformTestCase() {
    fun `test builtin function documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            void fragment() {
                float x = <caret>sin(1.0);
            }
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("vec_type sin(vec_type angle)"))
    }

    fun `test builtin variable documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            void fragment() {
                vec4 c = <caret>COLOR;
            }
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("inout vec4 COLOR"))
    }

    fun `test user defined function documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            
            // Calculates something important.
            // @param x Input value
            float my_func(float x) {
                return x * 2.0;
            }
            
            void fragment() {
                float y = <caret>my_func(1.0);
            }
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("float my_func(float x)"))
        assertTrue(plainDoc.contains("Calculates something important."))
        assertTrue(plainDoc.contains("@param x Input value"))
    }

    fun `test user defined uniform documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            
            // The main color.
            uniform vec4 <caret>u_color : source_color;
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("uniform vec4 u_color"))
        assertTrue(plainDoc.contains("The main color."))
    }

    fun `test instance uniform documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            instance uniform vec4 <caret>tint;
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val plainDoc = requireNotNull(provider.generateDoc(element, null)).replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("instance uniform vec4 tint"))
    }

    fun `test user defined struct documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            
            // My custom data structure
            struct <caret>MyData {
                float x;
                float y;
            };
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val doc = requireNotNull(provider.generateDoc(element, null))
        val plainDoc = doc.replace(Regex("<[^>]*>"), "")

        assertTrue(plainDoc.contains("struct MyData"))
        assertTrue(plainDoc.contains("My custom data structure"))
    }

    fun `test builtin function documentation without shader type`() {
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

    fun `test object-like macro documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            // Warm tint.
            #define TINT vec3(1.0, 0.5, 0.0)
            void fragment() {
                vec3 c = <caret>TINT;
            }
            """.trimIndent(),
        )

        val plainDoc = plainDocAtCaret()
        assertTrue(plainDoc.contains("#define TINT vec3(1.0, 0.5, 0.0)"))
        assertTrue(plainDoc.contains("Warm tint."))
    }

    fun `test function-like macro documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            #define LESS(a, b) ((a) < (b))
            void fragment() {
                bool x = <caret>LESS(1.0, 2.0);
            }
            """.trimIndent(),
        )

        val doc = docAtCaret()
        assertTrue(doc.contains("((a) &lt; (b))"))
        assertTrue(plain(doc).contains("#define LESS(a, b) ((a) &lt; (b))"))
    }

    fun `test builtin define documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            void fragment() {
                int r = <caret>RENDERER_MOBILE;
            }
            """.trimIndent(),
        )

        assertTrue(plainDocAtCaret().contains("#define RENDERER_MOBILE 1"))
    }

    fun `test macro quick navigate info`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            #define TINT vec3(1.0)
            void fragment() {
                vec3 c = <caret>TINT;
            }
            """.trimIndent(),
        )

        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        val info = requireNotNull(provider.getQuickNavigateInfo(element, null))
        assertEquals("#define TINT vec3(1.0)", plain(info))
    }

    fun `test adjacent macros are not included in macro documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            #define COLOR_DIAG vec4(1.0, 0.0, 0.0, 0.0)
            #define COLOR_VERT vec4(0.0, 1.0, 0.0, 0.0)
            #define COLOR_HORI vec4(0.0, 0.0, 1.0, 0.0)
            void fragment() {
                vec4 c = <caret>COLOR_HORI;
            }
            """.trimIndent(),
        )

        val plainDoc = plainDocAtCaret()
        assertTrue(plainDoc.contains("#define COLOR_HORI"))
        assertFalse(plainDoc.contains("COLOR_DIAG"))
        assertFalse(plainDoc.contains("COLOR_VERT"))
    }

    fun `test preprocessor line is not included in function documentation`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            // Not documentation of my_func.
            #define SCALE 2.0
            float my_func(float x) {
                return x * SCALE;
            }
            void fragment() {
                float y = <caret>my_func(1.0);
            }
            """.trimIndent(),
        )

        val plainDoc = plainDocAtCaret()
        assertFalse(plainDoc.contains("SCALE"))
        assertFalse(plainDoc.contains("Not documentation"))
    }

    private fun docAtCaret(): String {
        val element = myFixture.elementAtCaret
        val provider = LanguageDocumentation.INSTANCE.forLanguage(element.language)
        return requireNotNull(provider.generateDoc(element, null))
    }

    private fun plainDocAtCaret(): String = plain(docAtCaret())

    private fun plain(html: String): String = html.replace(Regex("<[^>]*>"), "")
}

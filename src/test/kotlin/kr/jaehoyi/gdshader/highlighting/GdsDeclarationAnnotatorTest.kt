package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsDeclarationAnnotatorTest : BasePlatformTestCase() {

    // === Empty struct ===

    fun `test empty struct`() {
        doHighlightTest("""
            shader_type spatial;
            <error descr="Empty structs are not allowed">struct Empty {};</error>
        """)
    }

    fun `test non-empty struct`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float x; };
        """)
    }

    // === Include file extension ===

    fun `test include gdshaderinc file`() {
        myFixture.addFileToProject("utils.gdshaderinc", "float add(float a, float b) { return a + b; }")
        doHighlightTest("""
            shader_type spatial;
            #include "res://utils.gdshaderinc"
        """)
    }

    fun `test include non-gdshaderinc file`() {
        doHighlightTest("""
            shader_type spatial;
            #include "<error descr="Only '.gdshaderinc' files can be included">res://other.gdshader</error>"
        """)
    }

    // === Include file existence ===

    fun `test include nonexistent file`() {
        doHighlightTest("""
            shader_type spatial;
            #include "<error descr="Cannot resolve file 'res://nonexistent.gdshaderinc'">res://nonexistent.gdshaderinc</error>"
        """)
    }

    fun `test include existing file`() {
        myFixture.addFileToProject("helpers.gdshaderinc", "// helper")
        doHighlightTest("""
            shader_type spatial;
            #include "res://helpers.gdshaderinc"
        """)
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}
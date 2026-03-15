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

    // === Double array size ===

    fun `test uniform single array size`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float arr[3];
        """)
    }

    fun `test uniform double array size`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float[3] arr<error descr="Array size is already defined">[2]</error>;
        """)
    }

    fun `test parameter double array size`() {
        doHighlightTest("""
            shader_type spatial;
            void my_func(float[3] arr<error descr="Array size is already defined">[2]</error>) {}
        """)
    }

    fun `test struct member double array size`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float[3] x<error descr="Array size is already defined">[2]</error>; };
        """)
    }

    fun `test local variable double array size`() {
        doHighlightTest("""
            shader_type spatial;
            void fragment() {
                float[3] arr<error descr="Array size is already defined">[2]</error>;
            }
        """)
    }

    // === Duplicate declarations ===

    fun `test duplicate function`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            float <error descr="Redefinition of 'add'">add</error>(float x, float y) { return x + y; }
        """)
    }

    fun `test no duplicate function`() {
        doHighlightTest("""
            shader_type spatial;
            float add(float a, float b) { return a + b; }
            float sub(float a, float b) { return a - b; }
        """)
    }

    fun `test duplicate struct`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float x; };
            struct <error descr="Redefinition of 'Data'">Data</error> { float y; };
        """)
    }

    fun `test no duplicate struct`() {
        doHighlightTest("""
            shader_type spatial;
            struct Data { float x; };
            struct Info { float y; };
        """)
    }

    fun `test duplicate uniform`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float speed;
            uniform float <error descr="Redefinition of 'speed'">speed</error>;
        """)
    }

    fun `test duplicate uniform and varying`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float value;
            varying float <error descr="Redefinition of 'value'">value</error>;
        """)
    }

    fun `test duplicate uniform and constant`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float speed;
            const float <error descr="Redefinition of 'speed'">speed</error> = 1.0;
        """)
    }

    fun `test no duplicate variables`() {
        doHighlightTest("""
            shader_type spatial;
            uniform float speed;
            uniform float strength;
            const float MAX = 10.0;
        """)
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(false, false, true)
    }
}
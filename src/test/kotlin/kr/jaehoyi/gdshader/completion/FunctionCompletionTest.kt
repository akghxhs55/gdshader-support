package kr.jaehoyi.gdshader.completion

class FunctionCompletionTest : GdsCompletionTestBase() {

    fun `test builtin function completion`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            void fragment() {
                float x = si<caret>;
            }
        """.trimIndent())

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'sin'", lookupStrings.contains("sin"))
        assertTrue("Should contain 'sign'", lookupStrings.contains("sign"))
    }

    fun `test constructor completion`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;
            void fragment() {
                ve<caret>
            }
        """.trimIndent())

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'vec3'", lookupStrings.contains("vec3"))
        assertTrue("Should contain 'vec4'", lookupStrings.contains("vec4"))
    }

    fun `test user defined function completion`() {
        myFixture.configureByText("test.gdshader", """
            shader_type canvas_item;

            void my_helper_func() {}

            void fragment() {
                <caret>
            }
        """.trimIndent())

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'my_helper_func'", lookupStrings.contains("my_helper_func"))
    }

}

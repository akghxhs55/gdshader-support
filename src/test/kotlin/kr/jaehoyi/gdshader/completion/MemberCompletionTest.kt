package kr.jaehoyi.gdshader.completion

class MemberCompletionTest : GdsCompletionTestBase() {
    fun `test struct member completion`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;

            struct MyStruct {
                float my_field_a;
                vec3 my_field_b;
            };

            void fragment() {
                MyStruct s;
                s.<caret>
            }
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "my_field_a", "my_field_b")
    }

    fun `test vector swizzle completion`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;

            void fragment() {
                vec3 v = vec3(1.0);
                v.<caret>
            }
            """.trimIndent(),
        )

        val completions = completeAndGetStrings()

        assertContainsElements(completions, "x", "y", "z", "r", "xy", "rgb", "xyz")
        assertDoesntContain(completions, "w", "a")
    }
}

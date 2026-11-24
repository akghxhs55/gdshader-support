package kr.jaehoyi.gdshader.completion

class VariableCompletionTest : GdsCompletionTestBase() {
    fun `test local variable completion`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void fragment() {
                float my_local_var = 1.0;
                my_loc<caret>
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'my_local_var'", lookupStrings.contains("my_local_var"))
    }

    fun `test parameter completion`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void my_func(vec3 my_param_vec) {
                my_par<caret>
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'my_param_vec'", lookupStrings.contains("my_param_vec"))
    }

    fun `test global variable completion`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;

            uniform float u_time;
            const float MAX_SPEED = 10.0;
            varying vec3 v_normal;

            void fragment() {
                <caret>
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'u_time'", lookupStrings.contains("u_time"))
        assertTrue("Should contain 'MAX_SPEED'", lookupStrings.contains("MAX_SPEED"))
        assertTrue("Should contain 'v_normal'", lookupStrings.contains("v_normal"))
    }

    fun `test builtin variable completion`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            void fragment() {
                TIM<caret>
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'TIME'", lookupStrings.contains("TIME"))
    }

    fun `test variable scope visibility`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void func_a() {
                float invisible_var = 1.0;
            }

            void func_b() {
                invis<caret>
            }
            """.trimIndent(),
        )

        myFixture.completeBasic()
        val lookupStrings = myFixture.lookupElementStrings

        if (lookupStrings != null) {
            assertFalse(
                "Should NOT contain 'invisible_var' from another function",
                lookupStrings.contains("invisible_var"),
            )
        }
    }

    fun `test for loop variable completion`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void fragment() {
                for (int i = 0; i < 10; i++) {
                    i<caret>
                }
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'i'", lookupStrings.contains("i"))
    }

    fun `test for loop variable in condition`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            void fragment() {
                for (int i = 0; i<caret> < 10; i++) {
                }
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertTrue("Should contain 'i'", lookupStrings.contains("i"))
    }

    fun `test completion shows variables from included file`() {
        myFixture.addFileToProject("constants.gdshaderinc", "const int MAX_LIGHTS = 10;")

        myFixture.configureByText(
            "main.gdshader",
            """
            #include "res://constants.gdshaderinc"

            void light() {
                int count = MAX_<caret>;
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertContainsElements(lookupStrings, "MAX_LIGHTS")
    }

    fun `test completion shows variables from file included from nested godot project`() {
        myFixture.addFileToProject("game/project.godot", "")
        myFixture.addFileToProject("game/lib/constants.gdshaderinc", "const int MAX_LIGHTS = 10;")

        val filePath = "game/shaders/main.gdshader"
        myFixture.addFileToProject(
            filePath,
            """
            #include "res://lib/constants.gdshaderinc"

            void light() {
                int count = MAX_<caret>;
            }
            """.trimIndent(),
        )
        myFixture.configureFromTempProjectFile(filePath)

        val lookupStrings = completeAndGetStrings()

        assertContainsElements(lookupStrings, "MAX_LIGHTS")
    }

    fun `test completion shows literal define`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            #define EPSILON 0.01

            void fragment() {
                float x = EPS<caret>;
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertContainsElements(lookupStrings, "EPSILON")
    }

    fun `test completion shows non-literal and function-like macros`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            #define MACRO_TINT vec3(1.0)
            #define MACRO_SQ(x) ((x) * (x))

            void fragment() {
                vec3 c = MACRO_<caret>;
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertContainsElements(lookupStrings, "MACRO_TINT", "MACRO_SQ")
    }

    fun `test completion shows literal define from included file`() {
        myFixture.addFileToProject("constants.gdshaderinc", "#define MAX_LIGHTS 8")

        myFixture.configureByText(
            "main.gdshader",
            """
            #include "res://constants.gdshaderinc"

            void light() {
                int count = MAX_<caret>;
            }
            """.trimIndent(),
        )

        val lookupStrings = completeAndGetStrings()

        assertContainsElements(lookupStrings, "MAX_LIGHTS")
    }
}

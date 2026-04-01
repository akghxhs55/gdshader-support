package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsUnusedSymbolInspectionTest : BasePlatformTestCase() {
    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(GdsUnusedSymbolInspection())
    }

    fun `test unused local variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float <warning descr="Variable 'x' is never used">x</warning> = 1.0;
            }
            """.trimIndent(),
        )
    }

    fun `test used local variable`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                float x = 1.0;
                ALBEDO = vec3(x);
            }
            """.trimIndent(),
        )
    }

    fun `test unused constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                const float <warning descr="Constant 'PI2' is never used">PI2</warning> = 6.28;
            }
            """.trimIndent(),
        )
    }

    fun `test used constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            void fragment() {
                const float PI2 = 6.28;
                ALBEDO = vec3(PI2);
            }
            """.trimIndent(),
        )
    }

    fun `test unused parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            float my_func(float <warning descr="Parameter 'x' is never used">x</warning>) {
                return 1.0;
            }
            void fragment() {
                ALBEDO = vec3(my_func(1.0));
            }
            """.trimIndent(),
        )
    }

    fun `test used parameter`() {
        doHighlightTest(
            """
            shader_type spatial;
            float my_func(float x) {
                return x;
            }
            void fragment() {
                ALBEDO = vec3(my_func(1.0));
            }
            """.trimIndent(),
        )
    }

    fun `test processing function not flagged`() {
        doHighlightTest(
            """
            shader_type particles;
            void start() {
                VELOCITY = vec3(1.0);
            }
            """.trimIndent(),
        )
    }

    fun `test unused function`() {
        doHighlightTest(
            """
            shader_type spatial;
            float <warning descr="Function 'my_func' is never used">my_func</warning>() {
                return 1.0;
            }
            void fragment() {
            }
            """.trimIndent(),
        )
    }

    fun `test used function`() {
        doHighlightTest(
            """
            shader_type spatial;
            float my_func() {
                return 1.0;
            }
            void fragment() {
                ALBEDO = vec3(my_func());
            }
            """.trimIndent(),
        )
    }

    fun `test remove unused parameter fix`() {
        doFixTest(
            before =
                """
                shader_type spatial;
                float my_func(float x) {
                    return 1.0;
                }
                void fragment() {
                    ALBEDO = vec3(my_func(0.5));
                }
                """.trimIndent(),
            after =
                """
                shader_type spatial;
                float my_func() {
                    return 1.0;
                }
                void fragment() {
                    ALBEDO = vec3(my_func());
                }
                """.trimIndent(),
            fixName = "Remove 'x'",
        )
    }

    fun `test remove first parameter fix`() {
        doFixTest(
            before =
                """
                shader_type spatial;
                float my_func(float x, float y) {
                    return y;
                }
                void fragment() {
                    ALBEDO = vec3(my_func(0.5, 1.0));
                }
                """.trimIndent(),
            after =
                """
                shader_type spatial;
                float my_func(float y) {
                    return y;
                }
                void fragment() {
                    ALBEDO = vec3(my_func(1.0));
                }
                """.trimIndent(),
            fixName = "Remove 'x'",
        )
    }

    fun `test remove single variable fix`() {
        doFixTest(
            before =
                """
                shader_type spatial;
                void fragment() {
                    float x = 1.0;
                }
                """.trimIndent(),
            after =
                """
                shader_type spatial;
                void fragment() {
                }
                """.trimIndent(),
            fixName = "Remove 'x'",
        )
    }

    fun `test remove first variable from multi-declaration fix`() {
        doFixTest(
            before =
                """
                shader_type spatial;
                void fragment() {
                    float x = 1.0, y = 2.0;
                    ALBEDO = vec3(y);
                }
                """.trimIndent(),
            after =
                """
                shader_type spatial;
                void fragment() {
                    float y = 2.0;
                    ALBEDO = vec3(y);
                }
                """.trimIndent(),
            fixName = "Remove 'x'",
        )
    }

    fun `test remove last variable from multi-declaration fix`() {
        doFixTest(
            before =
                """
                shader_type spatial;
                void fragment() {
                    float x = 1.0, y = 2.0;
                    ALBEDO = vec3(x);
                }
                """.trimIndent(),
            after =
                """
                shader_type spatial;
                void fragment() {
                    float x = 1.0;
                    ALBEDO = vec3(x);
                }
                """.trimIndent(),
            fixName = "Remove 'y'",
        )
    }

    fun `test remove unused function fix`() {
        doFixTest(
            before =
                """
                shader_type spatial;
                float my_func() {
                    return 1.0;
                }
                void fragment() {
                }
                """.trimIndent(),
            after =
                """
                shader_type spatial;
                void fragment() {
                }
                """.trimIndent(),
            fixName = "Remove 'my_func'",
        )
    }

    private fun doHighlightTest(code: String) {
        myFixture.configureByText("test_shader.gdshader", code)
        myFixture.checkHighlighting(true, false, true)
    }

    private fun doFixTest(
        before: String,
        after: String,
        fixName: String,
    ) {
        myFixture.configureByText("test_shader.gdshader", before)
        val fixes = myFixture.getAllQuickFixes()
        val action =
            fixes.firstOrNull { it.text == fixName }
                ?: error("Quick fix '$fixName' not found. Available: ${fixes.map { it.text }}")
        myFixture.launchAction(action)
        myFixture.checkResult(after)
    }
}

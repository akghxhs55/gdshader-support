package kr.jaehoyi.gdshader.highlighting

import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.withDirectoryOutsideProjectScope

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

    fun `test unused uniforms`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float <warning descr="Uniform 'u' is never used">u</warning>;
            global uniform float <warning descr="Uniform 'g' is never used">g</warning>;
            instance uniform float <warning descr="Uniform 'i' is never used">i</warning>;
            void fragment() {
            }
            """.trimIndent(),
        )
    }

    fun `test used uniform`() {
        doHighlightTest(
            """
            shader_type spatial;
            uniform float u;
            void fragment() {
                ALBEDO = vec3(u);
            }
            """.trimIndent(),
        )
    }

    fun `test unused varying`() {
        doHighlightTest(
            """
            shader_type spatial;
            varying float <warning descr="Varying 'v' is never used">v</warning>;
            void fragment() {
            }
            """.trimIndent(),
        )
    }

    fun `test write-only varying is used`() {
        doHighlightTest(
            """
            shader_type spatial;
            varying float v;
            void vertex() {
                v = 1.0;
            }
            """.trimIndent(),
        )
    }

    fun `test unused struct`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct <warning descr="Struct 'S' is never used">S</warning> {
                float a;
            };
            void fragment() {
            }
            """.trimIndent(),
        )
    }

    fun `test struct used as local variable type`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct S {
                float a;
            };
            void fragment() {
                S s;
                s.a = 1.0;
                ALBEDO = vec3(s.a);
            }
            """.trimIndent(),
        )
    }

    fun `test struct used as member type`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct Inner {
                float a;
            };
            struct Outer {
                Inner inner;
            };
            void fragment() {
                Outer o;
                ALBEDO = vec3(o.inner.a);
            }
            """.trimIndent(),
        )
    }

    fun `test struct used as parameter type`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct S {
                float a;
            };
            float get(S s) {
                return s.a;
            }
            void fragment() {
                ALBEDO = vec3(get(S(1.0)));
            }
            """.trimIndent(),
        )
    }

    fun `test struct used only as return type and constructor`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct S {
                float a;
            };
            S make() {
                return S(1.0);
            }
            void fragment() {
                ALBEDO = vec3(make().a);
            }
            """.trimIndent(),
        )
    }

    fun `test struct used only in constant`() {
        doHighlightTest(
            """
            shader_type spatial;
            struct S {
                float a;
            };
            const S K = S(1.0);
            void fragment() {
                ALBEDO = vec3(K.a);
            }
            """.trimIndent(),
        )
    }

    fun `test include top-level symbols used by shader file`() {
        myFixture.enableInspections(GdsUnusedSymbolInspection().apply { reportIncludeTopLevelSymbols = true })
        myFixture.addFileToProject(
            "shader.gdshader",
            """
            shader_type spatial;
            #include "inc.gdshaderinc"
            void vertex() {
                v = u;
            }
            void fragment() {
                S s = S(v);
                ALBEDO = vec3(s.a);
            }
            """.trimIndent(),
        )
        doHighlightTest(
            """
            uniform float u;
            varying float v;
            struct S {
                float a;
            };
            """.trimIndent(),
            "inc.gdshaderinc",
        )
    }

    fun `test used local variable outside project scope`() {
        val file =
            myFixture.addFileToProject(
                "external/test_shader.gdshader",
                """
                shader_type spatial;
                void fragment() {
                    float x = 1.0;
                    ALBEDO = vec3(x);
                }
                """.trimIndent(),
            )

        withDirectoryOutsideProjectScope(module, file.virtualFile.parent) {
            myFixture.configureFromExistingVirtualFile(file.virtualFile)
            myFixture.checkHighlighting(true, false, true)
        }
    }

    fun `test symbols in unindexed file`() {
        val file =
            myFixture.addFileToProject(
                "excluded/test_shader.gdshader",
                """
                shader_type spatial;
                float my_func(float x, float <warning descr="Parameter 'y' is never used">y</warning>) {
                    const float K = 2.0;
                    float z = x * K;
                    return z;
                }
                float <warning descr="Function 'unused_func' is never used">unused_func</warning>() {
                    return 1.0;
                }
                void fragment() {
                    ALBEDO = vec3(my_func(1.0, 2.0));
                }
                """.trimIndent(),
            )

        val directory = file.virtualFile.parent
        PsiTestUtil.addExcludedRoot(module, directory)
        try {
            myFixture.configureFromExistingVirtualFile(file.virtualFile)
            myFixture.checkHighlighting(true, false, true)
        } finally {
            PsiTestUtil.removeExcludedRoot(module, directory)
        }
    }

    fun `test top-level symbols in include file not flagged by default`() {
        doHighlightTest(
            """
            const float K = 2.0;
            uniform float u;
            varying float v;
            struct S {
                float a;
            };
            float helper(float x, float <warning descr="Parameter 'y' is never used">y</warning>) {
                const float <warning descr="Constant 'C' is never used">C</warning> = 1.0;
                float <warning descr="Variable 'z' is never used">z</warning> = x;
                return x;
            }
            """.trimIndent(),
            "test_include.gdshaderinc",
        )
    }

    fun `test top-level symbols in include file flagged when enabled`() {
        myFixture.enableInspections(GdsUnusedSymbolInspection().apply { reportIncludeTopLevelSymbols = true })
        doHighlightTest(
            """
            const float <warning descr="Constant 'K' is never used">K</warning> = 2.0;
            uniform float <warning descr="Uniform 'u' is never used">u</warning>;
            varying float <warning descr="Varying 'v' is never used">v</warning>;
            struct <warning descr="Struct 'S' is never used">S</warning> {
                float a;
            };
            float <warning descr="Function 'helper' is never used">helper</warning>() {
                return 1.0;
            }
            """.trimIndent(),
            "test_include.gdshaderinc",
        )
    }

    fun `test top-level symbols in shader file flagged by default`() {
        doHighlightTest(
            """
            shader_type spatial;
            const float <warning descr="Constant 'K' is never used">K</warning> = 2.0;
            void fragment() {
            }
            """.trimIndent(),
        )
    }

    private fun doHighlightTest(
        code: String,
        fileName: String = "test_shader.gdshader",
    ) {
        myFixture.configureByText(fileName, code)
        myFixture.checkHighlighting(true, false, true)
        val removalFixes = myFixture.getAllQuickFixes().filter { it.text.startsWith("Remove '") }
        assertEmpty("Unused symbols should only produce warnings", removalFixes)
    }
}

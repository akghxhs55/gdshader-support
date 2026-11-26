package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.codeInsight.daemon.LineMarkerProviders
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionEP
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.LanguageAnnotators
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.testFramework.DumbModeTestUtils
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.GdsLanguage

// Rider runs only DumbAware highlighting extensions on files that are not indexable, such as ones in folders
// excluded from indexing, the same way IntelliJ does for every file in dumb mode.
class GdsDumbModeHighlightingTest : BasePlatformTestCase() {
    override fun setUp() {
        super.setUp()
        (DaemonCodeAnalyzer.getInstance(project) as DaemonCodeAnalyzerImpl).mustWaitForSmartMode(false, testRootDisposable)
    }

    fun `test annotators are dumb aware`() {
        val annotators = LanguageAnnotators.INSTANCE.allForLanguage(GdsLanguage)
        assertNotEmpty(annotators)
        assertEquals(emptyList<String>(), annotators.filterNot(DumbService::isDumbAware).map { it.javaClass.name })
    }

    fun `test inspections are dumb aware`() {
        val inspections =
            LocalInspectionEP.LOCAL_INSPECTION.extensionList
                .filter { it.language == GdsLanguage.id }
                .map { it.instantiateTool() }
        assertNotEmpty(inspections)
        assertEquals(emptyList<String>(), inspections.filterNot(DumbService::isDumbAware).map { it.javaClass.name })
    }

    fun `test line marker and color providers are dumb aware`() {
        val providers =
            LineMarkerProviders.getInstance().allForLanguage(GdsLanguage) +
                ElementColorProvider.EP_NAME.extensionList.filter { it.javaClass.name.startsWith("kr.jaehoyi.gdshader.") }
        assertNotEmpty(providers)
        assertEquals(emptyList<String>(), providers.filterNot(DumbService::isDumbAware).map { it.javaClass.name })
    }

    fun `test semantic highlighting in dumb mode`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type canvas_item;
            float scale(float value) {
                float doubled = value * 2.0;
                return doubled;
            }
            void fragment() {
                COLOR = vec4(scale(UV.x));
            }
            """.trimIndent(),
        )

        val colors =
            DumbModeTestUtils.computeInDumbModeSynchronously(project) {
                myFixture
                    .doHighlighting()
                    .filter { it.severity == HighlightSeverity.INFORMATION && it.forcedTextAttributesKey != null }
                    .map { it.text to it.forcedTextAttributesKey }
            }

        assertTrue(colors.contains("value" to GdsSyntaxHighlighter.PARAMETER))
        assertTrue(colors.contains("doubled" to GdsSyntaxHighlighter.LOCAL_VARIABLE))
        assertTrue(colors.contains("scale" to GdsSyntaxHighlighter.FUNCTION_CALL))
        assertTrue(colors.contains("COLOR" to GdsSyntaxHighlighter.BUILTIN_VARIABLE))
    }

    fun `test unused symbols in dumb mode`() {
        val problems =
            findUnusedSymbolsInDumbMode(
                "test.gdshader",
                """
                shader_type spatial;
                float my_func(float x, float y) {
                    float z = x * 2.0;
                    float w = 1.0;
                    return z;
                }
                float unused_func() {
                    return 1.0;
                }
                void fragment() {
                    ALBEDO = vec3(my_func(1.0, 2.0));
                }
                """.trimIndent(),
            )

        assertEquals(listOf("y", "w", "unused_func"), problems)
    }

    fun `test unused symbols in include file in dumb mode`() {
        val problems =
            findUnusedSymbolsInDumbMode(
                "common.gdshaderinc",
                """
                float helper(float x, float y) {
                    float w = 1.0;
                    return x;
                }
                """.trimIndent(),
            )

        assertEquals(listOf("y", "w"), problems)
    }

    // The 2024.2 test platform skips every inspection in dumb mode, so the visitor is run directly.
    private fun findUnusedSymbolsInDumbMode(
        fileName: String,
        text: String,
    ): List<String> {
        myFixture.configureByText(fileName, text)

        return DumbModeTestUtils.computeInDumbModeSynchronously(project) {
            val holder = ProblemsHolder(InspectionManager.getInstance(project), myFixture.file, true)
            val visitor = GdsUnusedSymbolInspection().buildVisitor(holder, true)
            myFixture.file.accept(
                object : PsiRecursiveElementWalkingVisitor() {
                    override fun visitElement(element: PsiElement) {
                        element.accept(visitor)
                        super.visitElement(element)
                    }
                },
            )
            holder.results.map { it.psiElement.text }
        }
    }
}

package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInsight.daemon.impl.analysis.DefaultHighlightingSettingProvider
import com.intellij.codeInsight.daemon.impl.analysis.FileHighlightingSetting
import com.intellij.codeInsight.daemon.impl.analysis.HighlightingLevelManager
import com.intellij.openapi.extensions.LoadingOrder
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsHighlightingSettingProviderTest : BasePlatformTestCase() {
    override fun setUp() {
        super.setUp()
        // Stands in for Rider's provider, which is registered last and skips files outside the solution.
        DefaultHighlightingSettingProvider.EP_NAME.point.registerExtension(
            SkipAllProvider(),
            LoadingOrder.LAST,
            testRootDisposable,
        )
    }

    fun `test shader in godot project is highlighted`() {
        myFixture.addFileToProject("game/project.godot", "")
        val file = myFixture.addFileToProject("game/shaders/a.gdshader", "shader_type spatial;")

        assertTrue(shouldHighlight(file))
        assertTrue(shouldInspect(file))
    }

    fun `test include in godot project is highlighted`() {
        myFixture.addFileToProject("game/project.godot", "")
        val file = myFixture.addFileToProject("game/lib/common.gdshaderinc", "float f() { return 1.0; }")

        assertTrue(shouldHighlight(file))
        assertTrue(shouldInspect(file))
    }

    fun `test shader outside godot project is left to other providers`() {
        val file = myFixture.addFileToProject("loose/a.gdshader", "shader_type spatial;")

        assertFalse(shouldHighlight(file))
    }

    fun `test other file in godot project is left to other providers`() {
        myFixture.addFileToProject("game/project.godot", "")
        val file = myFixture.addFileToProject("game/notes.txt", "")

        assertFalse(shouldHighlight(file))
    }

    private fun shouldHighlight(file: PsiFile): Boolean = HighlightingLevelManager.getInstance(project).shouldHighlight(file)

    private fun shouldInspect(file: PsiFile): Boolean = HighlightingLevelManager.getInstance(project).shouldInspect(file)

    private class SkipAllProvider : DefaultHighlightingSettingProvider() {
        override fun getDefaultSetting(
            project: Project,
            file: VirtualFile,
        ): FileHighlightingSetting = FileHighlightingSetting.SKIP_HIGHLIGHTING
    }
}

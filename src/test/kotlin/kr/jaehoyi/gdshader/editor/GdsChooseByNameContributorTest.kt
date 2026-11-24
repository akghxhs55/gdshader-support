package kr.jaehoyi.gdshader.editor

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.withDirectoryOutsideProjectScope

class GdsChooseByNameContributorTest : BasePlatformTestCase() {
    fun `test symbols are found outside project scope`() {
        val file = myFixture.addFileToProject("external/main.gdshader", "shader_type spatial;\nfloat helper() { return 1.0; }")

        withDirectoryOutsideProjectScope(module, file.virtualFile.parent) {
            val contributor = GdsChooseByNameContributor()

            assertContainsElements(contributor.getNames(project, false).toList(), "helper")
            assertSize(1, contributor.getItemsByName("helper", "helper", project, false))
        }
    }
}

package kr.jaehoyi.gdshader.rider

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.isFile
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRule
import com.jetbrains.rider.projectView.indexing.files.RiderFilesIndexingRuleProvider

class GDShaderFilesIndexingRuleProvider : RiderFilesIndexingRuleProvider {
    override suspend fun collectRules(project: Project): List<RiderFilesIndexingRule> =
        listOf(
            RiderFilesIndexingRule({ file ->
                if (file.isFile &&
                    file.name.endsWith(".gdshader", ignoreCase = true)) {
                    return@RiderFilesIndexingRule file
                }
                else {
                    return@RiderFilesIndexingRule null
                }
            })
        )
}
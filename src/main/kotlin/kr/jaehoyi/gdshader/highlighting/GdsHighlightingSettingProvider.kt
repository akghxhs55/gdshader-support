package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInsight.daemon.impl.analysis.DefaultHighlightingSettingProvider
import com.intellij.codeInsight.daemon.impl.analysis.FileHighlightingSetting
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import kr.jaehoyi.gdshader.GdsFileType
import kr.jaehoyi.gdshader.GdsIncludeFileType
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

// Rider skips highlighting for files that are neither indexed nor in the solution model, such as Godot files
// outside the solution directory.
class GdsHighlightingSettingProvider :
    DefaultHighlightingSettingProvider(),
    DumbAware {
    override fun getDefaultSetting(
        project: Project,
        file: VirtualFile,
    ): FileHighlightingSetting? {
        val fileType = file.fileType
        if (fileType != GdsFileType && fileType != GdsIncludeFileType) {
            return null
        }
        if (GdsPathUtil.findGodotProjectDir(file) == null) {
            return null
        }
        return FileHighlightingSetting.FORCE_HIGHLIGHTING
    }
}

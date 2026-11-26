package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.openapi.project.DumbAware
import kr.jaehoyi.gdshader.GdsBundle

abstract class GdsLocalizedInspection(
    private val descriptionKey: String,
) : LocalInspectionTool(),
    DumbAware {
    override fun getStaticDescription(): String = GdsBundle.message(descriptionKey)
}

package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInspection.LocalInspectionTool
import kr.jaehoyi.gdshader.GdsBundle

abstract class GdsLocalizedInspection(
    private val descriptionKey: String,
) : LocalInspectionTool() {
    override fun getStaticDescription(): String = GdsBundle.message(descriptionKey)
}

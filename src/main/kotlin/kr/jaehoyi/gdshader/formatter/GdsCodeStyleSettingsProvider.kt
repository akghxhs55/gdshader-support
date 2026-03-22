package kr.jaehoyi.gdshader.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import kr.jaehoyi.gdshader.GdsLanguage

class GdsCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun getLanguage(): Language = GdsLanguage

    override fun createCustomSettings(settings: CodeStyleSettings) =
        GdsCodeStyleSettings(settings)

    override fun getConfigurableDisplayName(): String = "GDShader"

    override fun createConfigurable(
        settings: CodeStyleSettings,
        modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, "GDShader") {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel
                = GdsCodeStyleMainPanel(currentSettings, settings)
        }
    }
}
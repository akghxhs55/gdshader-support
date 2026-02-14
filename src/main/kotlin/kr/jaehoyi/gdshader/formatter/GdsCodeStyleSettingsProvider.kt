package kr.jaehoyi.gdshader.formatter

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider

class GdsCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    
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
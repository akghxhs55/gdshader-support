package kr.jaehoyi.gdshader.formatter

import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleSettings
import kr.jaehoyi.gdshader.GdsLanguage

class GdsCodeStyleMainPanel(
    currentSettings: CodeStyleSettings,
    settings: CodeStyleSettings
) : TabbedLanguageCodeStylePanel(GdsLanguage, currentSettings, settings) {
    
    override fun initTabs(settings: CodeStyleSettings) {
        addIndentOptionsTab(settings)

        addSpacesTab(settings)

        addWrappingAndBracesTab(settings)

        addBlankLinesTab(settings)
    }
}

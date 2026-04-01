package kr.jaehoyi.gdshader.formatter

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class GdsCodeStyleSettings(
    container: CodeStyleSettings,
) : CustomCodeStyleSettings("GdsCodeStyleSettings", container) {
    @JvmField var spaceBeforeForSemicolon = false

    @JvmField var spaceAfterForSemicolon = true

    @JvmField var spaceBeforeHintColon: Boolean = true

    @JvmField var spaceAfterHintColon: Boolean = true

    @JvmField var spaceBeforeTernaryColon: Boolean = true

    @JvmField var spaceAfterTernaryColon: Boolean = true

    @JvmField var spaceBeforeCaseColon: Boolean = false
}

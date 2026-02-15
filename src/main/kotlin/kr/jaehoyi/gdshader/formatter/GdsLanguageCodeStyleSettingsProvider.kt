package kr.jaehoyi.gdshader.formatter

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import kr.jaehoyi.gdshader.GdsLanguage

class GdsLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = GdsLanguage

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        indentOptions.INDENT_SIZE = 4
        indentOptions.TAB_SIZE = 4
        indentOptions.USE_TAB_CHARACTER = true

        commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true
        commonSettings.SPACE_AROUND_LOGICAL_OPERATORS = true
        commonSettings.SPACE_AROUND_EQUALITY_OPERATORS = true
        commonSettings.SPACE_AROUND_RELATIONAL_OPERATORS = true
        commonSettings.SPACE_AROUND_ADDITIVE_OPERATORS = true
        commonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS = true

        commonSettings.SPACE_AFTER_COMMA = true
        commonSettings.SPACE_BEFORE_METHOD_LBRACE = true
        commonSettings.SPACE_BEFORE_IF_LBRACE = true
        commonSettings.SPACE_BEFORE_ELSE_LBRACE = true
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions(
                // Before Parentheses
                "SPACE_BEFORE_IF_PARENTHESES",
                "SPACE_BEFORE_FOR_PARENTHESES",
                "SPACE_BEFORE_WHILE_PARENTHESES",
                "SPACE_BEFORE_SWITCH_PARENTHESES",

                // Around Operators
                "SPACE_AROUND_ASSIGNMENT_OPERATORS",
                "SPACE_AROUND_LOGICAL_OPERATORS",
                "SPACE_AROUND_EQUALITY_OPERATORS",
                "SPACE_AROUND_RELATIONAL_OPERATORS",
                "SPACE_AROUND_BITWISE_OPERATORS",
                "SPACE_AROUND_ADDITIVE_OPERATORS",
                "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
                "SPACE_AROUND_SHIFT_OPERATORS",

                // Before Left Braces
                "SPACE_BEFORE_CLASS_LBRACE",
                "SPACE_BEFORE_METHOD_LBRACE",
                "SPACE_BEFORE_IF_LBRACE",
                "SPACE_BEFORE_ELSE_LBRACE",
                "SPACE_BEFORE_FOR_LBRACE",
                "SPACE_BEFORE_WHILE_LBRACE",
                "SPACE_BEFORE_DO_LBRACE",
                "SPACE_BEFORE_SWITCH_LBRACE",

                // Before Keywords
                "SPACE_BEFORE_ELSE_KEYWORD",
                "SPACE_BEFORE_WHILE_KEYWORD",

                // Within
                "SPACE_WITHIN_BRACES",

                // Other
                "SPACE_AFTER_COMMA",
                "SPACE_AFTER_SEMICOLON"
            )

            consumer.renameStandardOption("SPACE_WITHIN_BRACES", "Braces")

            val forGroup = "Within 'for' header"

            consumer.showCustomOption(GdsCodeStyleSettings::class.java, "SPACE_BEFORE_FOR_SEMICOLON", "Before 'for' semicolon", forGroup)
            consumer.showCustomOption(GdsCodeStyleSettings::class.java, "SPACE_AFTER_FOR_SEMICOLON", "After 'for' semicolon", forGroup)
            
            val colonGroup = "Colon"
            
            consumer.showCustomOption(GdsCodeStyleSettings::class.java, "SPACE_BEFORE_HINT_COLON", "Before hint colon ':'", colonGroup)
            consumer.showCustomOption(GdsCodeStyleSettings::class.java, "SPACE_AFTER_HINT_COLON", "After hint colon ':'", colonGroup)
            
            consumer.showCustomOption(GdsCodeStyleSettings::class.java, "SPACE_BEFORE_TERNARY_COLON", "Before ternary colon ':'", colonGroup)
            consumer.showCustomOption(GdsCodeStyleSettings::class.java, "SPACE_AFTER_TERNARY_COLON", "After ternary colon ':'", colonGroup)
            
            consumer.showCustomOption(GdsCodeStyleSettings::class.java, "SPACE_BEFORE_CASE_COLON", "Before case colon ':'", colonGroup)
        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            shader_type canvas_item;
            
            uniform float size : hint_range(0.0, 1.0) = 0.5;
            uniform vec4 color : source_color;

            struct Light {
                vec3 position;
                float intensity;
            };

            void fragment() {
                float x = (1.0 + 2.0) * 0.5;
                int arr[] = { 1, 2, 3 };
                
                if (x > 0.5) {
                    x -= 0.1;
                } else {
                    x += 0.1;
                }
                
                for (int i = 0; i < 10; i++) {
                    x += float(i) * 0.1;
                }
                
                while (x < 1.0) {
                    x *= 1.1;
                }
                
                do {
                    x *= 0.9;
                } while (x > 0.1);
                
                switch (int(x)) {
                    case 0:
                        break;
                    default:
                        break;
                }
               
                
                float y = (x > 0.0) ? 1.0 : 0.0;
                
                vec4 tex = texture(TEXTURE, UV);
            }
        """.trimIndent()
    }
}
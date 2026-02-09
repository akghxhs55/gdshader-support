package kr.jaehoyi.gdshader.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import kr.jaehoyi.gdshader.GdsLanguage
import kr.jaehoyi.gdshader.psi.GdsTokenSets
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings

        val spacingBuilder = createSpacingBuilder(settings)

        val rootBlock = GdsBlock(
            formattingContext.node,
            Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(),
            Indent.getNoneIndent(),
            spacingBuilder
        )

        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            rootBlock,
            settings
        )
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val commonSettings = settings.getCommonSettings(GdsLanguage.id)
        
        return SpacingBuilder(settings, GdsLanguage)
            .around(GdsTokenSets.ASSIGNMENT_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
            .around(GdsTokenSets.EQUALITY_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_EQUALITY_OPERATORS)
            .around(GdsTokenSets.RELATIONAL_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
            .around(GdsTokenSets.LOGICAL_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_LOGICAL_OPERATORS)
            .around(GdsTokenSets.BITWISE_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_BITWISE_OPERATORS)
            .around(GdsTokenSets.ADDITIVE_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
            .around(GdsTokenSets.MULTIPLICATIVE_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
            .around(GdsTokenSets.SHIFT_OPERATORS).spaceIf(commonSettings.SPACE_AROUND_SHIFT_OPERATORS)
            .after(GdsTokenSets.UNARY_OPERATORS).none()
            // 2. 콤마 뒤 공백 (func(a, b))
            .after(GdsTypes.COMMA).spaceIf(settings.getCommonSettings(GdsLanguage.id).SPACE_AFTER_COMMA)
            .before(GdsTypes.COMMA).spaceIf(settings.getCommonSettings(GdsLanguage.id).SPACE_BEFORE_COMMA)
            // 3. 블록 주변 (void main() { )
            .before(GdsTypes.BLOCK).spaces(1)
            // 4. 소괄호 안쪽 공백 제거 (func( a )) -> (func(a))
            .after(GdsTypes.PARENTHESIS_OPEN).none()
            .before(GdsTypes.PARENTHESIS_CLOSE).none()
    }
}
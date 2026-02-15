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
        val common = settings.getCommonSettings(GdsLanguage.id)
        val gds = settings.getCustomSettings(GdsCodeStyleSettings::class.java)

        return SpacingBuilder(settings, GdsLanguage)
            // Unary Operators
            .afterInside(GdsTokenSets.UNARY_OPERATORS, GdsTypes.UNARY_EXPR).none()
            
            // Inner Parentheses
            
            // Function Call
            .afterInside(GdsTypes.PARENTHESIS_OPEN, GdsTypes.FUNCTION_CALL)
            .spaceIf(common.SPACE_WITHIN_METHOD_CALL_PARENTHESES)
            .beforeInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.FUNCTION_CALL)
            .spaceIf(common.SPACE_WITHIN_METHOD_CALL_PARENTHESES)

            // Function Declaration
            .afterInside(GdsTypes.PARENTHESIS_OPEN, GdsTypes.FUNCTION_DECLARATION)
            .spaceIf(common.SPACE_WITHIN_METHOD_PARENTHESES)
            .beforeInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.FUNCTION_DECLARATION)
            .spaceIf(common.SPACE_WITHIN_METHOD_PARENTHESES)

            // Control Flow Statements (If, For, While, Switch)
            .afterInside(GdsTypes.PARENTHESIS_OPEN, GdsTypes.IF_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_IF_PARENTHESES)
            .beforeInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.IF_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_IF_PARENTHESES)

            .afterInside(GdsTypes.PARENTHESIS_OPEN, GdsTypes.FOR_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_FOR_PARENTHESES)
            .beforeInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.FOR_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_FOR_PARENTHESES)

            .afterInside(GdsTypes.PARENTHESIS_OPEN, GdsTypes.WHILE_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_WHILE_PARENTHESES)
            .beforeInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.WHILE_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_WHILE_PARENTHESES)

            .afterInside(GdsTypes.PARENTHESIS_OPEN, GdsTypes.SWITCH_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_SWITCH_PARENTHESES)
            .beforeInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.SWITCH_STATEMENT)
            .spaceIf(common.SPACE_WITHIN_SWITCH_PARENTHESES)

            // Parentheses & Brackets (General)
            .after(GdsTypes.PARENTHESIS_OPEN).spaceIf(common.SPACE_WITHIN_PARENTHESES)
            .before(GdsTypes.PARENTHESIS_CLOSE).spaceIf(common.SPACE_WITHIN_PARENTHESES)
            .after(GdsTypes.BRACKET_OPEN).spaceIf(common.SPACE_WITHIN_BRACKETS)
            .before(GdsTypes.BRACKET_CLOSE).spaceIf(common.SPACE_WITHIN_BRACKETS)

            // Before Parentheses
            .between(GdsTypes.FUNCTION_NAME_REF, GdsTypes.PARENTHESIS_OPEN)
            .spaceIf(common.SPACE_BEFORE_METHOD_CALL_PARENTHESES)
            .between(GdsTypes.FUNCTION_NAME_DECL, GdsTypes.PARENTHESIS_OPEN)
            .spaceIf(common.SPACE_BEFORE_METHOD_PARENTHESES)

            .after(GdsTypes.CF_IF).spaceIf(common.SPACE_BEFORE_IF_PARENTHESES)
            .after(GdsTypes.CF_FOR).spaceIf(common.SPACE_BEFORE_FOR_PARENTHESES)
            .after(GdsTypes.CF_WHILE).spaceIf(common.SPACE_BEFORE_WHILE_PARENTHESES)
            .after(GdsTypes.CF_SWITCH).spaceIf(common.SPACE_BEFORE_SWITCH_PARENTHESES)

            // Control Flow

            // Left Braces
            // if (true) { ... }
            .beforeInside(GdsTypes.BLOCK, GdsTypes.IF_STATEMENT).spaceIf(common.SPACE_BEFORE_IF_LBRACE)
            // else { ... }
            .beforeInside(GdsTypes.BLOCK, GdsTypes.ELSE_CLAUSE).spaceIf(common.SPACE_BEFORE_ELSE_LBRACE)
            // for (...) { ... }
            .beforeInside(GdsTypes.BLOCK, GdsTypes.FOR_STATEMENT).spaceIf(common.SPACE_BEFORE_FOR_LBRACE)
            // while (...) { ... }
            .beforeInside(GdsTypes.BLOCK, GdsTypes.WHILE_STATEMENT).spaceIf(common.SPACE_BEFORE_WHILE_LBRACE)
            // do { ... }
            .beforeInside(GdsTypes.BLOCK, GdsTypes.DO_WHILE_STATEMENT).spaceIf(common.SPACE_BEFORE_DO_LBRACE)
            // switch (...) { ... }
            .beforeInside(GdsTypes.SWITCH_BLOCK, GdsTypes.SWITCH_STATEMENT).spaceIf(common.SPACE_BEFORE_SWITCH_LBRACE)
            // void func() { ... }
            .beforeInside(GdsTypes.BLOCK, GdsTypes.FUNCTION_DECLARATION).spaceIf(common.SPACE_BEFORE_METHOD_LBRACE)
            // struct A { ... }
            .beforeInside(GdsTypes.STRUCT_BLOCK, GdsTypes.STRUCT_DECLARATION).spaceIf(common.SPACE_BEFORE_CLASS_LBRACE)

            // Keywords
            // } else { ... } -> front 'else'
            .before(GdsTypes.ELSE_CLAUSE).spaceIf(common.SPACE_BEFORE_ELSE_KEYWORD)

            // else if (...) -> between 'else' and 'if'
            .between(GdsTypes.CF_ELSE, GdsTypes.IF_STATEMENT).spaces(1)

            // } while (...); -> front 'while'
            .beforeInside(GdsTypes.CF_WHILE, GdsTypes.DO_WHILE_STATEMENT).spaceIf(common.SPACE_BEFORE_WHILE_KEYWORD)

            // After case colon: case 0: statement;
            .afterInside(GdsTypes.COLON, GdsTypes.CASE_CLAUSE).spaces(1)

            // After closing parenthesis (non-block body): if (true) statement;
            .afterInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.IF_STATEMENT).spaces(1)
            .afterInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.FOR_STATEMENT).spaces(1)
            .afterInside(GdsTypes.PARENTHESIS_CLOSE, GdsTypes.WHILE_STATEMENT).spaces(1)

            // After keywords (non-block body): else statement; / do statement;
            .afterInside(GdsTypes.CF_ELSE, GdsTypes.ELSE_CLAUSE).spaces(1)
            .afterInside(GdsTypes.CF_DO, GdsTypes.DO_WHILE_STATEMENT).spaces(1)

            // Binary Operators
            .around(GdsTokenSets.ASSIGNMENT_OPERATORS).spaceIf(common.SPACE_AROUND_ASSIGNMENT_OPERATORS)
            .around(GdsTokenSets.EQUALITY_OPERATORS).spaceIf(common.SPACE_AROUND_EQUALITY_OPERATORS)
            .around(GdsTokenSets.RELATIONAL_OPERATORS).spaceIf(common.SPACE_AROUND_RELATIONAL_OPERATORS)
            .around(GdsTokenSets.BITWISE_OPERATORS).spaceIf(common.SPACE_AROUND_BITWISE_OPERATORS)
            .around(GdsTokenSets.ADDITIVE_OPERATORS).spaceIf(common.SPACE_AROUND_ADDITIVE_OPERATORS)
            .around(GdsTokenSets.MULTIPLICATIVE_OPERATORS).spaceIf(common.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
            .around(GdsTokenSets.SHIFT_OPERATORS).spaceIf(common.SPACE_AROUND_SHIFT_OPERATORS)
            .around(GdsTokenSets.LOGICAL_OPERATORS).spaceIf(common.SPACE_AROUND_LOGICAL_OPERATORS)

            // Punctuations
            .after(GdsTypes.COMMA).spaceIf(common.SPACE_AFTER_COMMA)
            .before(GdsTypes.COMMA).spaceIf(common.SPACE_BEFORE_COMMA)
            .beforeInside(GdsTypes.SEMICOLON, GdsTypes.FOR_STATEMENT).spaceIf(gds.SPACE_BEFORE_FOR_SEMICOLON)
            .afterInside(GdsTypes.SEMICOLON, GdsTypes.FOR_STATEMENT).spaceIf(gds.SPACE_AFTER_FOR_SEMICOLON)
            .before(GdsTypes.SEMICOLON).spaceIf(common.SPACE_BEFORE_SEMICOLON)
            .after(GdsTypes.SEMICOLON).spaceIf(common.SPACE_AFTER_SEMICOLON)
            .after(GdsTypes.CURLY_BRACKET_OPEN).spaceIf(common.SPACE_WITHIN_BRACES)
            .before(GdsTypes.CURLY_BRACKET_CLOSE).spaceIf(common.SPACE_WITHIN_BRACES)
            .beforeInside(GdsTypes.COLON, GdsTypes.UNIFORM_DECLARATION).spaceIf(gds.SPACE_BEFORE_HINT_COLON)
            .afterInside(GdsTypes.COLON, GdsTypes.UNIFORM_DECLARATION).spaceIf(gds.SPACE_AFTER_HINT_COLON)
            .beforeInside(GdsTypes.COLON, GdsTypes.CONDITIONAL_EXPR).spaceIf(gds.SPACE_BEFORE_TERNARY_COLON)
            .afterInside(GdsTypes.COLON, GdsTypes.CONDITIONAL_EXPR).spaceIf(gds.SPACE_AFTER_TERNARY_COLON)
            .beforeInside(GdsTypes.COLON, GdsTypes.CASE_CLAUSE).spaceIf(gds.SPACE_BEFORE_CASE_COLON)
            .before(GdsTypes.QUESTION).spaceIf(common.SPACE_BEFORE_QUEST)
            .after(GdsTypes.QUESTION).spaceIf(common.SPACE_AFTER_QUEST)
    }
}
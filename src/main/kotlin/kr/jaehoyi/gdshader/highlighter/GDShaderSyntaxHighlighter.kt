package kr.jaehoyi.gdshader.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.GDShaderLexerAdapter
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        private val log = Logger.getInstance("kr.jaehoyi.gdshader.highlighter.GDShaderSyntaxHighlighter")
        
        val IDENTIFIER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val HINT = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_HINT", DefaultLanguageHighlighterColors.METADATA)
        val STRING = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_STRING", DefaultLanguageHighlighterColors.STRING)
        val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
        val LINE_COMMENT = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val COLON = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_COLON", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val CURLY_BRACKET = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_BRACE", DefaultLanguageHighlighterColors.BRACES)
        val PERIOD = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_PERIOD", DefaultLanguageHighlighterColors.DOT)
        val SEMICOLON = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMA = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val PARENTHESIS = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_PAREN", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACKET = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_BRACKET", DefaultLanguageHighlighterColors.BRACKETS)
        val CONSTANT = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        val GLOBAL_VARIABLE = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_GLOBAL_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE)
        val FUNCTION_DECLARATION = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_FUNCTION_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val FUNCTION_CALL = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL)
        val STRUCT = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_STRUCT", DefaultLanguageHighlighterColors.CLASS_NAME)
        val TYPE = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME)
        val PREPROCESSOR = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_PREPROCESSOR", DefaultLanguageHighlighterColors.METADATA)
        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val HINT_KEYS = arrayOf(HINT)
        private val STRING_KEYS = arrayOf(STRING)
        private val BLOCK_COMMENT_KEYS = arrayOf(BLOCK_COMMENT)
        private val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val COLON_KEYS = arrayOf(COLON)
        private val CURLY_BRACKET_KEYS = arrayOf(CURLY_BRACKET)
        private val PERIOD_KEYS = arrayOf(PERIOD)
        private val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        private val COMMA_KEYS = arrayOf(COMMA)
        private val PARENTHESIS_KEYS = arrayOf(PARENTHESIS)
        private val BRACKET_KEYS = arrayOf(BRACKET)
        private val CONSTANT_KEYS = arrayOf(CONSTANT)
        private val GLOBAL_VARIABLE_KEYS = arrayOf(GLOBAL_VARIABLE)
        private val FUNCTION_DECLARATION_KEYS = arrayOf(FUNCTION_DECLARATION)
        private val FUNCTION_CALL_KEYS = arrayOf(FUNCTION_CALL)
        private val STRUCT_KEYS = arrayOf(STRUCT)
        private val TYPE_KEYS = arrayOf(TYPE)
        private val PREPROCESSOR_KEYS = arrayOf(PREPROCESSOR)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
    
    override fun getHighlightingLexer(): Lexer = GDShaderLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<out TextAttributesKey?> {
        return try {
            when (tokenType) {
                GDShaderTypes.IDENTIFIER
                    -> IDENTIFIER_KEYS

                GDShaderTypes.LINE_COMMENT
                    -> LINE_COMMENT_KEYS

                GDShaderTypes.BLOCK_COMMENT
                    -> BLOCK_COMMENT_KEYS

                GDShaderTypes.FLOAT_CONSTANT, GDShaderTypes.INT_CONSTANT, GDShaderTypes.UINT_CONSTANT
                    -> NUMBER_KEYS

                GDShaderTypes.STRING_CONSTANT
                    -> STRING_KEYS

                GDShaderTypes.CF_IF, GDShaderTypes.CF_ELSE, GDShaderTypes.CF_FOR, GDShaderTypes.CF_WHILE,
                GDShaderTypes.CF_DO, GDShaderTypes.CF_SWITCH, GDShaderTypes.CF_CASE, GDShaderTypes.CF_DEFAULT,
                GDShaderTypes.CF_BREAK, GDShaderTypes.CF_CONTINUE, GDShaderTypes.CF_RETURN, GDShaderTypes.CF_DISCARD,
                GDShaderTypes.SHADER_TYPE, GDShaderTypes.RENDER_MODE, GDShaderTypes.STENCIL_MODE, GDShaderTypes.CONST,
                GDShaderTypes.STRUCT, GDShaderTypes.PRECISION_LOW, GDShaderTypes.PRECISION_MEDIUM,
                GDShaderTypes.PRECISION_HIGH, GDShaderTypes.UNIFORM, GDShaderTypes.UNIFORM_GROUP, GDShaderTypes.INSTANCE,
                GDShaderTypes.GLOBAL, GDShaderTypes.VARYING, GDShaderTypes.ARG_IN, GDShaderTypes.ARG_OUT,
                GDShaderTypes.ARG_INOUT, GDShaderTypes.INTERPOLATION_FLAT, GDShaderTypes.INTERPOLATION_SMOOTH
                    -> KEYWORD_KEYS

                GDShaderTypes.HINT_DEFAULT_WHITE_TEXTURE, GDShaderTypes.HINT_DEFAULT_BLACK_TEXTURE,
                GDShaderTypes.HINT_DEFAULT_TRANSPARENT_TEXTURE, GDShaderTypes.HINT_NORMAL_TEXTURE,
                GDShaderTypes.HINT_ROUGHNESS_NORMAL_TEXTURE, GDShaderTypes.HINT_ROUGHNESS_R, GDShaderTypes.HINT_ROUGHNESS_G,
                GDShaderTypes.HINT_ROUGHNESS_B, GDShaderTypes.HINT_ROUGHNESS_A, GDShaderTypes.HINT_ROUGHNESS_GRAY,
                GDShaderTypes.HINT_ANISOTROPY_TEXTURE, GDShaderTypes.HINT_SOURCE_COLOR,
                GDShaderTypes.HINT_COLOR_CONVERSION_DISABLED, GDShaderTypes.HINT_RANGE, GDShaderTypes.HINT_ENUM,
                GDShaderTypes.HINT_INSTANCE_INDEX, GDShaderTypes.HINT_SCREEN_TEXTURE,
                GDShaderTypes.HINT_NORMAL_ROUGHNESS_TEXTURE, GDShaderTypes.HINT_DEPTH_TEXTURE, GDShaderTypes.FILTER_NEAREST,
                GDShaderTypes.FILTER_LINEAR, GDShaderTypes.FILTER_NEAREST_MIPMAP, GDShaderTypes.FILTER_LINEAR_MIPMAP,
                GDShaderTypes.FILTER_NEAREST_MIPMAP_ANISOTROPIC, GDShaderTypes.FILTER_LINEAR_MIPMAP_ANISOTROPIC,
                GDShaderTypes.REPEAT_ENABLE, GDShaderTypes.REPEAT_DISABLE
                    -> HINT_KEYS

                GDShaderTypes.OP_EQUAL, GDShaderTypes.OP_NOT_EQUAL, GDShaderTypes.OP_LESS, GDShaderTypes.OP_LESS_EQUAL,
                GDShaderTypes.OP_GREATER, GDShaderTypes.OP_GREATER_EQUAL, GDShaderTypes.OP_AND, GDShaderTypes.OP_OR,
                GDShaderTypes.OP_NOT, GDShaderTypes.OP_ADD, GDShaderTypes.OP_SUB, GDShaderTypes.OP_MUL,
                GDShaderTypes.OP_DIV, GDShaderTypes.OP_MOD, GDShaderTypes.OP_SHIFT_LEFT, GDShaderTypes.OP_SHIFT_RIGHT,
                GDShaderTypes.OP_ASSIGN, GDShaderTypes.OP_ASSIGN_ADD, GDShaderTypes.OP_ASSIGN_SUB,
                GDShaderTypes.OP_ASSIGN_MUL, GDShaderTypes.OP_ASSIGN_DIV, GDShaderTypes.OP_ASSIGN_MOD,
                GDShaderTypes.OP_ASSIGN_SHIFT_LEFT, GDShaderTypes.OP_ASSIGN_SHIFT_RIGHT, GDShaderTypes.OP_BIT_AND,
                GDShaderTypes.OP_BIT_OR, GDShaderTypes.OP_BIT_XOR, GDShaderTypes.OP_BIT_INVERT, GDShaderTypes.OP_INCREMENT,
                GDShaderTypes.OP_DECREMENT, GDShaderTypes.QUESTION
                    -> OPERATOR_KEYS

                GDShaderTypes.COLON
                    -> COLON_KEYS

                GDShaderTypes.CURLY_BRACKET_OPEN, GDShaderTypes.CURLY_BRACKET_CLOSE
                    -> CURLY_BRACKET_KEYS

                GDShaderTypes.PERIOD
                    -> PERIOD_KEYS

                GDShaderTypes.SEMICOLON
                    -> SEMICOLON_KEYS

                GDShaderTypes.COMMA
                    -> COMMA_KEYS

                GDShaderTypes.PARENTHESIS_OPEN, GDShaderTypes.PARENTHESIS_CLOSE
                    -> PARENTHESIS_KEYS

                GDShaderTypes.BRACKET_OPEN, GDShaderTypes.BRACKET_CLOSE
                    -> BRACKET_KEYS

                GDShaderTypes.TRUE, GDShaderTypes.FALSE
                    -> CONSTANT_KEYS

                GDShaderTypes.TYPE_VOID, GDShaderTypes.TYPE_BOOL, GDShaderTypes.TYPE_BVEC2, GDShaderTypes.TYPE_BVEC3,
                GDShaderTypes.TYPE_BVEC4, GDShaderTypes.TYPE_INT, GDShaderTypes.TYPE_IVEC2, GDShaderTypes.TYPE_IVEC3,
                GDShaderTypes.TYPE_IVEC4, GDShaderTypes.TYPE_UINT, GDShaderTypes.TYPE_UVEC2, GDShaderTypes.TYPE_UVEC3,
                GDShaderTypes.TYPE_UVEC4, GDShaderTypes.TYPE_FLOAT, GDShaderTypes.TYPE_VEC2, GDShaderTypes.TYPE_VEC3,
                GDShaderTypes.TYPE_VEC4, GDShaderTypes.TYPE_MAT2, GDShaderTypes.TYPE_MAT3, GDShaderTypes.TYPE_MAT4,
                GDShaderTypes.TYPE_SAMPLER2D, GDShaderTypes.TYPE_ISAMPLER2D, GDShaderTypes.TYPE_USAMPLER2D,
                GDShaderTypes.TYPE_SAMPLER3D, GDShaderTypes.TYPE_ISAMPLER3D, GDShaderTypes.TYPE_USAMPLER3D,
                GDShaderTypes.TYPE_SAMPLERCUBE, GDShaderTypes.TYPE_SAMPLERCUBEARRAY, GDShaderTypes.TYPE_SAMPLEREXT
                    -> TYPE_KEYS

                GDShaderTypes.PP_DEFINE_LINE, GDShaderTypes.PP_UNDEF_LINE, GDShaderTypes.PP_IF_LINE,
                GDShaderTypes.PP_ELSE_LINE, GDShaderTypes.PP_ELIF_LINE, GDShaderTypes.PP_ENDIF_LINE,
                GDShaderTypes.PP_IFDEF_LINE, GDShaderTypes.PP_IFNDEF_LINE, GDShaderTypes.PP_ERROR_LINE,
                GDShaderTypes.PP_INCLUDE_LINE, GDShaderTypes.PP_PRAGMA_LINE
                    -> PREPROCESSOR_KEYS

                TokenType.BAD_CHARACTER
                    -> BAD_CHARACTER_KEYS

                else
                    -> EMPTY_KEYS
            }
        }
        catch (e: Exception) {
            log.error("Error while highlighting token: $tokenType", e)
            EMPTY_KEYS
        }
    }
}
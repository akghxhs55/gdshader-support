package kr.jaehoyi.gdshader.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.GdsLexerAdapter
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsSyntaxHighlighter : SyntaxHighlighterBase() {
    
    companion object {
        private val log = Logger.getInstance("kr.jaehoyi.gdshader.highlighter.GdsSyntaxHighlighter")
        
        val IDENTIFIER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val UNIFORM_HINT = TextAttributesKey.createTextAttributesKey(
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
        val LOCAL_VARIABLE = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_LOCAL_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)
        val UNIFORM_VARIABLE = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_GLOBAL_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE)
        val VARYING_VARIABLE = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_VARYING_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE)
        val STRUCT_MEMBER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_STRUCT_MEMBER", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val BUILTIN_CONSTANT = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_BUILTIN_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        val BUILTIN_VARIABLE = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_BUILTIN_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE)
        val FUNCTION = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_FUNCTION", DefaultLanguageHighlighterColors.INSTANCE_METHOD)
        val PARAMETER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_PARAMETER", DefaultLanguageHighlighterColors.PARAMETER)
        val STRUCT = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_STRUCT", DefaultLanguageHighlighterColors.CLASS_NAME)
        val TYPE = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_TYPE", DefaultLanguageHighlighterColors.KEYWORD)
        val PREPROCESSOR = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_PREPROCESSOR", DefaultLanguageHighlighterColors.METADATA)
        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "GDSHADER_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val UNIFORM_HINT_KEYS = arrayOf(UNIFORM_HINT)
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
        private val TYPE_KEYS = arrayOf(TYPE)
        private val PREPROCESSOR_KEYS = arrayOf(PREPROCESSOR)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
    
    override fun getHighlightingLexer(): Lexer = GdsLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<out TextAttributesKey?> {
        return try {
            when (tokenType) {
                GdsTypes.IDENTIFIER
                    -> IDENTIFIER_KEYS

                GdsTypes.LINE_COMMENT
                    -> LINE_COMMENT_KEYS

                GdsTypes.BLOCK_COMMENT_START,
                GdsTypes.BLOCK_COMMENT_END,
                GdsTypes.BLOCK_COMMENT_CONTENT,
                    -> BLOCK_COMMENT_KEYS

                GdsTypes.FLOAT_CONSTANT, GdsTypes.INT_CONSTANT, GdsTypes.UINT_CONSTANT
                    -> NUMBER_KEYS

                GdsTypes.STRING_CONSTANT
                    -> STRING_KEYS

                GdsTypes.CF_IF, GdsTypes.CF_ELSE, GdsTypes.CF_FOR, GdsTypes.CF_WHILE,
                GdsTypes.CF_DO, GdsTypes.CF_SWITCH, GdsTypes.CF_CASE, GdsTypes.CF_DEFAULT,
                GdsTypes.CF_BREAK, GdsTypes.CF_CONTINUE, GdsTypes.CF_RETURN, GdsTypes.CF_DISCARD,
                GdsTypes.SHADER_TYPE, GdsTypes.RENDER_MODE, GdsTypes.STENCIL_MODE, GdsTypes.CONST,
                GdsTypes.STRUCT, GdsTypes.PRECISION_LOW, GdsTypes.PRECISION_MEDIUM,
                GdsTypes.PRECISION_HIGH, GdsTypes.UNIFORM, GdsTypes.UNIFORM_GROUP, GdsTypes.INSTANCE,
                GdsTypes.GLOBAL, GdsTypes.VARYING, GdsTypes.ARG_IN, GdsTypes.ARG_OUT,
                GdsTypes.ARG_INOUT, GdsTypes.INTERPOLATION_FLAT, GdsTypes.INTERPOLATION_SMOOTH,
                GdsTypes.TRUE, GdsTypes.FALSE
                    -> KEYWORD_KEYS

                GdsTypes.HINT_DEFAULT_WHITE_TEXTURE, GdsTypes.HINT_DEFAULT_BLACK_TEXTURE,
                GdsTypes.HINT_DEFAULT_TRANSPARENT_TEXTURE, GdsTypes.HINT_NORMAL_TEXTURE,
                GdsTypes.HINT_ROUGHNESS_NORMAL_TEXTURE, GdsTypes.HINT_ROUGHNESS_R, GdsTypes.HINT_ROUGHNESS_G,
                GdsTypes.HINT_ROUGHNESS_B, GdsTypes.HINT_ROUGHNESS_A, GdsTypes.HINT_ROUGHNESS_GRAY,
                GdsTypes.HINT_ANISOTROPY_TEXTURE, GdsTypes.HINT_SOURCE_COLOR,
                GdsTypes.HINT_COLOR_CONVERSION_DISABLED, GdsTypes.HINT_RANGE, GdsTypes.HINT_ENUM,
                GdsTypes.HINT_INSTANCE_INDEX, GdsTypes.HINT_SCREEN_TEXTURE,
                GdsTypes.HINT_NORMAL_ROUGHNESS_TEXTURE, GdsTypes.HINT_DEPTH_TEXTURE, GdsTypes.FILTER_NEAREST,
                GdsTypes.FILTER_LINEAR, GdsTypes.FILTER_NEAREST_MIPMAP, GdsTypes.FILTER_LINEAR_MIPMAP,
                GdsTypes.FILTER_NEAREST_MIPMAP_ANISOTROPIC, GdsTypes.FILTER_LINEAR_MIPMAP_ANISOTROPIC,
                GdsTypes.REPEAT_ENABLE, GdsTypes.REPEAT_DISABLE
                    -> UNIFORM_HINT_KEYS

                GdsTypes.OP_EQUAL, GdsTypes.OP_NOT_EQUAL, GdsTypes.OP_LESS, GdsTypes.OP_LESS_EQUAL,
                GdsTypes.OP_GREATER, GdsTypes.OP_GREATER_EQUAL, GdsTypes.OP_AND, GdsTypes.OP_OR,
                GdsTypes.OP_NOT, GdsTypes.OP_ADD, GdsTypes.OP_SUB, GdsTypes.OP_MUL,
                GdsTypes.OP_DIV, GdsTypes.OP_MOD, GdsTypes.OP_SHIFT_LEFT, GdsTypes.OP_SHIFT_RIGHT,
                GdsTypes.OP_ASSIGN, GdsTypes.OP_ASSIGN_ADD, GdsTypes.OP_ASSIGN_SUB,
                GdsTypes.OP_ASSIGN_MUL, GdsTypes.OP_ASSIGN_DIV, GdsTypes.OP_ASSIGN_MOD,
                GdsTypes.OP_ASSIGN_SHIFT_LEFT, GdsTypes.OP_ASSIGN_SHIFT_RIGHT, GdsTypes.OP_BIT_AND,
                GdsTypes.OP_BIT_OR, GdsTypes.OP_BIT_XOR, GdsTypes.OP_BIT_INVERT, GdsTypes.OP_INCREMENT,
                GdsTypes.OP_DECREMENT, GdsTypes.QUESTION
                    -> OPERATOR_KEYS

                GdsTypes.COLON
                    -> COLON_KEYS

                GdsTypes.CURLY_BRACKET_OPEN, GdsTypes.CURLY_BRACKET_CLOSE
                    -> CURLY_BRACKET_KEYS

                GdsTypes.PERIOD
                    -> PERIOD_KEYS

                GdsTypes.SEMICOLON
                    -> SEMICOLON_KEYS

                GdsTypes.COMMA
                    -> COMMA_KEYS

                GdsTypes.PARENTHESIS_OPEN, GdsTypes.PARENTHESIS_CLOSE
                    -> PARENTHESIS_KEYS

                GdsTypes.BRACKET_OPEN, GdsTypes.BRACKET_CLOSE
                    -> BRACKET_KEYS

                GdsTypes.TYPE_VOID, GdsTypes.TYPE_BOOL, GdsTypes.TYPE_BVEC2, GdsTypes.TYPE_BVEC3,
                GdsTypes.TYPE_BVEC4, GdsTypes.TYPE_INT, GdsTypes.TYPE_IVEC2, GdsTypes.TYPE_IVEC3,
                GdsTypes.TYPE_IVEC4, GdsTypes.TYPE_UINT, GdsTypes.TYPE_UVEC2, GdsTypes.TYPE_UVEC3,
                GdsTypes.TYPE_UVEC4, GdsTypes.TYPE_FLOAT, GdsTypes.TYPE_VEC2, GdsTypes.TYPE_VEC3,
                GdsTypes.TYPE_VEC4, GdsTypes.TYPE_MAT2, GdsTypes.TYPE_MAT3, GdsTypes.TYPE_MAT4,
                GdsTypes.TYPE_SAMPLER2D, GdsTypes.TYPE_ISAMPLER2D, GdsTypes.TYPE_USAMPLER2D,
                GdsTypes.TYPE_SAMPLER3D, GdsTypes.TYPE_ISAMPLER3D, GdsTypes.TYPE_USAMPLER3D,
                GdsTypes.TYPE_SAMPLERCUBE, GdsTypes.TYPE_SAMPLERCUBEARRAY, GdsTypes.TYPE_SAMPLEREXT
                    -> TYPE_KEYS

                GdsTypes.PP_DEFINE_LINE, GdsTypes.PP_UNDEF_LINE, GdsTypes.PP_IF_LINE,
                GdsTypes.PP_ELSE_LINE, GdsTypes.PP_ELIF_LINE, GdsTypes.PP_ENDIF_LINE,
                GdsTypes.PP_IFDEF_LINE, GdsTypes.PP_IFNDEF_LINE, GdsTypes.PP_ERROR_LINE,
                GdsTypes.PP_INCLUDE_LINE, GdsTypes.PP_PRAGMA_LINE, GdsTypes.PP_UNKNOWN_LINE
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
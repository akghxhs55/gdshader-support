package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.TokenSet

object GdsTokenSets {
    
    val COMMENTS = TokenSet.create(GdsTypes.LINE_COMMENT, GdsTypes.BLOCK_COMMENT, GdsTypes.PP_DEFINE_LINE,
        GdsTypes.PP_UNDEF_LINE, GdsTypes.PP_ELSE_LINE, GdsTypes.PP_ELIF_LINE,
        GdsTypes.PP_ENDIF_LINE, GdsTypes.PP_IFDEF_LINE, GdsTypes.PP_IFNDEF_LINE,
        GdsTypes.PP_IF_LINE, GdsTypes.PP_ERROR_LINE, GdsTypes.PP_INCLUDE_LINE,
        GdsTypes.PP_PRAGMA_LINE)
    
    val OPERATORS = TokenSet.create(GdsTypes.OP_ASSIGN, GdsTypes.OP_ASSIGN_ADD, GdsTypes.OP_ASSIGN_SUB,
        GdsTypes.OP_ASSIGN_MUL, GdsTypes.OP_ASSIGN_DIV, GdsTypes.OP_ASSIGN_MOD,
        GdsTypes.OP_ASSIGN_SHIFT_LEFT, GdsTypes.OP_ASSIGN_SHIFT_RIGHT, GdsTypes.OP_ASSIGN_BIT_AND,
        GdsTypes.OP_ASSIGN_BIT_OR, GdsTypes.OP_ASSIGN_BIT_XOR, GdsTypes.OP_EQUAL,
        GdsTypes.OP_NOT_EQUAL, GdsTypes.OP_LESS, GdsTypes.OP_LESS_EQUAL, GdsTypes.OP_GREATER,
        GdsTypes.OP_GREATER_EQUAL, GdsTypes.OP_AND, GdsTypes.OP_OR, GdsTypes.OP_NOT,
        GdsTypes.OP_ADD, GdsTypes.OP_SUB, GdsTypes.OP_MUL, GdsTypes.OP_DIV,
        GdsTypes.OP_MOD, GdsTypes.OP_SHIFT_LEFT, GdsTypes.OP_SHIFT_RIGHT,  
    )
    
}
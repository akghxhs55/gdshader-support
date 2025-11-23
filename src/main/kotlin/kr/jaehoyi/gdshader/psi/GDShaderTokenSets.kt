package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.TokenSet

object GDShaderTokenSets {
    
    val COMMENTS = TokenSet.create(GDShaderTypes.LINE_COMMENT, GDShaderTypes.BLOCK_COMMENT, GDShaderTypes.PP_DEFINE_LINE,
        GDShaderTypes.PP_UNDEF_LINE, GDShaderTypes.PP_ELSE_LINE, GDShaderTypes.PP_ELIF_LINE,
        GDShaderTypes.PP_ENDIF_LINE, GDShaderTypes.PP_IFDEF_LINE, GDShaderTypes.PP_IFNDEF_LINE,
        GDShaderTypes.PP_IF_LINE, GDShaderTypes.PP_ERROR_LINE, GDShaderTypes.PP_INCLUDE_LINE,
        GDShaderTypes.PP_PRAGMA_LINE)
    
    val OPERATORS = TokenSet.create(GDShaderTypes.OP_ASSIGN, GDShaderTypes.OP_ASSIGN_ADD, GDShaderTypes.OP_ASSIGN_SUB,
        GDShaderTypes.OP_ASSIGN_MUL, GDShaderTypes.OP_ASSIGN_DIV, GDShaderTypes.OP_ASSIGN_MOD,
        GDShaderTypes.OP_ASSIGN_SHIFT_LEFT, GDShaderTypes.OP_ASSIGN_SHIFT_RIGHT, GDShaderTypes.OP_ASSIGN_BIT_AND,
        GDShaderTypes.OP_ASSIGN_BIT_OR, GDShaderTypes.OP_ASSIGN_BIT_XOR, GDShaderTypes.OP_EQUAL,
        GDShaderTypes.OP_NOT_EQUAL, GDShaderTypes.OP_LESS, GDShaderTypes.OP_LESS_EQUAL, GDShaderTypes.OP_GREATER,
        GDShaderTypes.OP_GREATER_EQUAL, GDShaderTypes.OP_AND, GDShaderTypes.OP_OR, GDShaderTypes.OP_NOT,
        GDShaderTypes.OP_ADD, GDShaderTypes.OP_SUB, GDShaderTypes.OP_MUL, GDShaderTypes.OP_DIV,
        GDShaderTypes.OP_MOD, GDShaderTypes.OP_SHIFT_LEFT, GDShaderTypes.OP_SHIFT_RIGHT,  
    )
    
}
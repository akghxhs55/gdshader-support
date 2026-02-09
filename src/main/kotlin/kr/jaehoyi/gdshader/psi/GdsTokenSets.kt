package kr.jaehoyi.gdshader.psi

import com.intellij.psi.tree.TokenSet

object GdsTokenSets {
    
    val COMMENTS = TokenSet.create(GdsTypes.LINE_COMMENT, GdsTypes.BLOCK_COMMENT_START, GdsTypes.BLOCK_COMMENT_END,
        GdsTypes.BLOCK_COMMENT_CONTENT, GdsTypes.PP_DEFINE_LINE, GdsTypes.PP_UNDEF_LINE, GdsTypes.PP_ELSE_LINE, 
        GdsTypes.PP_ELIF_LINE, GdsTypes.PP_ENDIF_LINE, GdsTypes.PP_IFDEF_LINE, GdsTypes.PP_IFNDEF_LINE, 
        GdsTypes.PP_IF_LINE, GdsTypes.PP_ERROR_LINE, GdsTypes.PP_INCLUDE_LINE, GdsTypes.PP_PRAGMA_LINE
    )
    
    val OPERATORS = TokenSet.create(GdsTypes.OP_ASSIGN, GdsTypes.OP_ASSIGN_ADD, GdsTypes.OP_ASSIGN_SUB,
        GdsTypes.OP_ASSIGN_MUL, GdsTypes.OP_ASSIGN_DIV, GdsTypes.OP_ASSIGN_MOD,
        GdsTypes.OP_ASSIGN_SHIFT_LEFT, GdsTypes.OP_ASSIGN_SHIFT_RIGHT, GdsTypes.OP_ASSIGN_BIT_AND,
        GdsTypes.OP_ASSIGN_BIT_OR, GdsTypes.OP_ASSIGN_BIT_XOR, GdsTypes.OP_EQUAL,
        GdsTypes.OP_NOT_EQUAL, GdsTypes.OP_LESS, GdsTypes.OP_LESS_EQUAL, GdsTypes.OP_GREATER,
        GdsTypes.OP_GREATER_EQUAL, GdsTypes.OP_AND, GdsTypes.OP_OR, GdsTypes.OP_NOT,
        GdsTypes.OP_ADD, GdsTypes.OP_SUB, GdsTypes.OP_MUL, GdsTypes.OP_DIV,
        GdsTypes.OP_MOD, GdsTypes.OP_SHIFT_LEFT, GdsTypes.OP_SHIFT_RIGHT,  
    )

    val ASSIGNMENT_OPERATORS = TokenSet.create(
        GdsTypes.OP_ASSIGN, GdsTypes.OP_ASSIGN_ADD, GdsTypes.OP_ASSIGN_SUB,
        GdsTypes.OP_ASSIGN_MUL, GdsTypes.OP_ASSIGN_DIV, GdsTypes.OP_ASSIGN_MOD,
        GdsTypes.OP_ASSIGN_SHIFT_LEFT, GdsTypes.OP_ASSIGN_SHIFT_RIGHT,
        GdsTypes.OP_ASSIGN_BIT_AND, GdsTypes.OP_ASSIGN_BIT_OR, GdsTypes.OP_ASSIGN_BIT_XOR
    )

    val EQUALITY_OPERATORS = TokenSet.create(
        GdsTypes.OP_EQUAL, GdsTypes.OP_NOT_EQUAL
    )

    val RELATIONAL_OPERATORS = TokenSet.create(
        GdsTypes.OP_LESS, GdsTypes.OP_LESS_EQUAL,
        GdsTypes.OP_GREATER, GdsTypes.OP_GREATER_EQUAL
    )

    val BITWISE_OPERATORS = TokenSet.create(
        GdsTypes.OP_BIT_AND, GdsTypes.OP_BIT_OR, GdsTypes.OP_BIT_XOR
    )

    val ADDITIVE_OPERATORS = TokenSet.create(
        GdsTypes.OP_ADD, GdsTypes.OP_SUB
    )

    val MULTIPLICATIVE_OPERATORS = TokenSet.create(
        GdsTypes.OP_MUL, GdsTypes.OP_DIV, GdsTypes.OP_MOD
    )

    val SHIFT_OPERATORS = TokenSet.create(
        GdsTypes.OP_SHIFT_LEFT, GdsTypes.OP_SHIFT_RIGHT
    )

    val LOGICAL_OPERATORS = TokenSet.create(
        GdsTypes.OP_AND, GdsTypes.OP_OR
    )

    val UNARY_OPERATORS = TokenSet.create(
        GdsTypes.OP_NOT, GdsTypes.OP_BIT_INVERT,
        GdsTypes.OP_INCREMENT, GdsTypes.OP_DECREMENT
    )
    
    val STRING_LITERALS = TokenSet.create(GdsTypes.STRING_CONSTANT, GdsTypes.UNTERMINATED_STRING_CONSTANT)
    
}
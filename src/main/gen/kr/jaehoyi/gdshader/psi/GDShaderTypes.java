// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import kr.jaehoyi.gdshader.psi.impl.*;

public interface GDShaderTypes {

  IElementType ADDITIVE_EXPR = new GDShaderElementType("ADDITIVE_EXPR");
  IElementType ARGUMENT_LIST = new GDShaderElementType("ARGUMENT_LIST");
  IElementType ARRAY_SIZE = new GDShaderElementType("ARRAY_SIZE");
  IElementType ASSIGNMENT_OPERATOR = new GDShaderElementType("ASSIGNMENT_OPERATOR");
  IElementType ASSIGN_EXPR = new GDShaderElementType("ASSIGN_EXPR");
  IElementType BITWISE_AND_EXPR = new GDShaderElementType("BITWISE_AND_EXPR");
  IElementType BITWISE_OR_EXPR = new GDShaderElementType("BITWISE_OR_EXPR");
  IElementType BITWISE_XOR_EXPR = new GDShaderElementType("BITWISE_XOR_EXPR");
  IElementType BLOCK = new GDShaderElementType("BLOCK");
  IElementType CASE_CLAUSE = new GDShaderElementType("CASE_CLAUSE");
  IElementType CONDITIONAL_EXPR = new GDShaderElementType("CONDITIONAL_EXPR");
  IElementType CONST_VARIABLE_DECLARATION = new GDShaderElementType("CONST_VARIABLE_DECLARATION");
  IElementType CONTROL_STATEMENT = new GDShaderElementType("CONTROL_STATEMENT");
  IElementType DO_WHILE_STATEMENT = new GDShaderElementType("DO_WHILE_STATEMENT");
  IElementType ENUM_HINT = new GDShaderElementType("ENUM_HINT");
  IElementType EQUALITY_EXPR = new GDShaderElementType("EQUALITY_EXPR");
  IElementType EXPRESSION = new GDShaderElementType("EXPRESSION");
  IElementType EXPRESSION_STATEMENT = new GDShaderElementType("EXPRESSION_STATEMENT");
  IElementType FOR_STATEMENT = new GDShaderElementType("FOR_STATEMENT");
  IElementType FUNCTION_CALL = new GDShaderElementType("FUNCTION_CALL");
  IElementType FUNCTION_DECLARATION = new GDShaderElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_NAME = new GDShaderElementType("FUNCTION_NAME");
  IElementType HINT = new GDShaderElementType("HINT");
  IElementType HINTS = new GDShaderElementType("HINTS");
  IElementType HINT_IDENTIFIER = new GDShaderElementType("HINT_IDENTIFIER");
  IElementType IF_STATEMENT = new GDShaderElementType("IF_STATEMENT");
  IElementType INITIALIZER = new GDShaderElementType("INITIALIZER");
  IElementType INITIALIZER_LIST = new GDShaderElementType("INITIALIZER_LIST");
  IElementType INSTANCE_INDEX_HINT = new GDShaderElementType("INSTANCE_INDEX_HINT");
  IElementType LITERAL = new GDShaderElementType("LITERAL");
  IElementType LOGIC_AND_EXPR = new GDShaderElementType("LOGIC_AND_EXPR");
  IElementType LOGIC_OR_EXPR = new GDShaderElementType("LOGIC_OR_EXPR");
  IElementType MULTIPLICATIVE_EXPR = new GDShaderElementType("MULTIPLICATIVE_EXPR");
  IElementType NUMBER = new GDShaderElementType("NUMBER");
  IElementType PARAMETER = new GDShaderElementType("PARAMETER");
  IElementType PARAMETER_LIST = new GDShaderElementType("PARAMETER_LIST");
  IElementType POSTFIX_EXPR = new GDShaderElementType("POSTFIX_EXPR");
  IElementType PRECISION = new GDShaderElementType("PRECISION");
  IElementType PRIMARY = new GDShaderElementType("PRIMARY");
  IElementType RANGE_HINT = new GDShaderElementType("RANGE_HINT");
  IElementType REGULAR_VARIABLE_DECLARATION = new GDShaderElementType("REGULAR_VARIABLE_DECLARATION");
  IElementType RELATIONAL_EXPR = new GDShaderElementType("RELATIONAL_EXPR");
  IElementType RENDER_MODE_DECLARATION = new GDShaderElementType("RENDER_MODE_DECLARATION");
  IElementType RETURN_STATEMENT = new GDShaderElementType("RETURN_STATEMENT");
  IElementType SHADER_TYPE_DECLARATION = new GDShaderElementType("SHADER_TYPE_DECLARATION");
  IElementType SHIFT_EXPR = new GDShaderElementType("SHIFT_EXPR");
  IElementType SIMPLE_HINT = new GDShaderElementType("SIMPLE_HINT");
  IElementType SIMPLE_STATEMENT = new GDShaderElementType("SIMPLE_STATEMENT");
  IElementType STATEMENT = new GDShaderElementType("STATEMENT");
  IElementType STENCIL_MODE_DECLARATION = new GDShaderElementType("STENCIL_MODE_DECLARATION");
  IElementType STRUCT_DECLARATION = new GDShaderElementType("STRUCT_DECLARATION");
  IElementType STRUCT_MEMBER = new GDShaderElementType("STRUCT_MEMBER");
  IElementType SWITCH_STATEMENT = new GDShaderElementType("SWITCH_STATEMENT");
  IElementType TYPE = new GDShaderElementType("TYPE");
  IElementType UNARY_EXPR = new GDShaderElementType("UNARY_EXPR");
  IElementType UNIFORM_GROUP_DECLARATION = new GDShaderElementType("UNIFORM_GROUP_DECLARATION");
  IElementType UNIFORM_VARIABLE_DECLARATION = new GDShaderElementType("UNIFORM_VARIABLE_DECLARATION");
  IElementType VARIABLE_DECLARATION = new GDShaderElementType("VARIABLE_DECLARATION");
  IElementType VARYING_VARIABLE_DECLARATION = new GDShaderElementType("VARYING_VARIABLE_DECLARATION");
  IElementType WHILE_STATEMENT = new GDShaderElementType("WHILE_STATEMENT");

  IElementType ARG_IN = new GDShaderTokenType("ARG_IN");
  IElementType ARG_INOUT = new GDShaderTokenType("ARG_INOUT");
  IElementType ARG_OUT = new GDShaderTokenType("ARG_OUT");
  IElementType BLOCK_COMMENT = new GDShaderTokenType("BLOCK_COMMENT");
  IElementType BRACKET_CLOSE = new GDShaderTokenType("BRACKET_CLOSE");
  IElementType BRACKET_OPEN = new GDShaderTokenType("BRACKET_OPEN");
  IElementType CF_BREAK = new GDShaderTokenType("CF_BREAK");
  IElementType CF_CASE = new GDShaderTokenType("CF_CASE");
  IElementType CF_CONTINUE = new GDShaderTokenType("CF_CONTINUE");
  IElementType CF_DEFAULT = new GDShaderTokenType("CF_DEFAULT");
  IElementType CF_DISCARD = new GDShaderTokenType("CF_DISCARD");
  IElementType CF_DO = new GDShaderTokenType("CF_DO");
  IElementType CF_ELSE = new GDShaderTokenType("CF_ELSE");
  IElementType CF_FOR = new GDShaderTokenType("CF_FOR");
  IElementType CF_IF = new GDShaderTokenType("CF_IF");
  IElementType CF_RETURN = new GDShaderTokenType("CF_RETURN");
  IElementType CF_SWITCH = new GDShaderTokenType("CF_SWITCH");
  IElementType CF_WHILE = new GDShaderTokenType("CF_WHILE");
  IElementType COLON = new GDShaderTokenType("COLON");
  IElementType COMMA = new GDShaderTokenType("COMMA");
  IElementType CONST = new GDShaderTokenType("CONST");
  IElementType CURLY_BRACKET_CLOSE = new GDShaderTokenType("CURLY_BRACKET_CLOSE");
  IElementType CURLY_BRACKET_OPEN = new GDShaderTokenType("CURLY_BRACKET_OPEN");
  IElementType FALSE = new GDShaderTokenType("FALSE");
  IElementType FILTER_LINEAR = new GDShaderTokenType("FILTER_LINEAR");
  IElementType FILTER_LINEAR_MIPMAP = new GDShaderTokenType("FILTER_LINEAR_MIPMAP");
  IElementType FILTER_LINEAR_MIPMAP_ANISOTROPIC = new GDShaderTokenType("FILTER_LINEAR_MIPMAP_ANISOTROPIC");
  IElementType FILTER_NEAREST = new GDShaderTokenType("FILTER_NEAREST");
  IElementType FILTER_NEAREST_MIPMAP = new GDShaderTokenType("FILTER_NEAREST_MIPMAP");
  IElementType FILTER_NEAREST_MIPMAP_ANISOTROPIC = new GDShaderTokenType("FILTER_NEAREST_MIPMAP_ANISOTROPIC");
  IElementType FLOAT_CONSTANT = new GDShaderTokenType("FLOAT_CONSTANT");
  IElementType GLOBAL = new GDShaderTokenType("GLOBAL");
  IElementType HINT_ANISOTROPY_TEXTURE = new GDShaderTokenType("HINT_ANISOTROPY_TEXTURE");
  IElementType HINT_COLOR_CONVERSION_DISABLED = new GDShaderTokenType("HINT_COLOR_CONVERSION_DISABLED");
  IElementType HINT_DEFAULT_BLACK_TEXTURE = new GDShaderTokenType("HINT_DEFAULT_BLACK_TEXTURE");
  IElementType HINT_DEFAULT_TRANSPARENT_TEXTURE = new GDShaderTokenType("HINT_DEFAULT_TRANSPARENT_TEXTURE");
  IElementType HINT_DEFAULT_WHITE_TEXTURE = new GDShaderTokenType("HINT_DEFAULT_WHITE_TEXTURE");
  IElementType HINT_DEPTH_TEXTURE = new GDShaderTokenType("HINT_DEPTH_TEXTURE");
  IElementType HINT_ENUM = new GDShaderTokenType("HINT_ENUM");
  IElementType HINT_INSTANCE_INDEX = new GDShaderTokenType("HINT_INSTANCE_INDEX");
  IElementType HINT_NORMAL_ROUGHNESS_TEXTURE = new GDShaderTokenType("HINT_NORMAL_ROUGHNESS_TEXTURE");
  IElementType HINT_NORMAL_TEXTURE = new GDShaderTokenType("HINT_NORMAL_TEXTURE");
  IElementType HINT_RANGE = new GDShaderTokenType("HINT_RANGE");
  IElementType HINT_ROUGHNESS_A = new GDShaderTokenType("HINT_ROUGHNESS_A");
  IElementType HINT_ROUGHNESS_B = new GDShaderTokenType("HINT_ROUGHNESS_B");
  IElementType HINT_ROUGHNESS_G = new GDShaderTokenType("HINT_ROUGHNESS_G");
  IElementType HINT_ROUGHNESS_GRAY = new GDShaderTokenType("HINT_ROUGHNESS_GRAY");
  IElementType HINT_ROUGHNESS_NORMAL_TEXTURE = new GDShaderTokenType("HINT_ROUGHNESS_NORMAL_TEXTURE");
  IElementType HINT_ROUGHNESS_R = new GDShaderTokenType("HINT_ROUGHNESS_R");
  IElementType HINT_SCREEN_TEXTURE = new GDShaderTokenType("HINT_SCREEN_TEXTURE");
  IElementType HINT_SOURCE_COLOR = new GDShaderTokenType("HINT_SOURCE_COLOR");
  IElementType IDENTIFIER = new GDShaderTokenType("IDENTIFIER");
  IElementType INSTANCE = new GDShaderTokenType("INSTANCE");
  IElementType INTERPOLATION_FLAT = new GDShaderTokenType("INTERPOLATION_FLAT");
  IElementType INTERPOLATION_SMOOTH = new GDShaderTokenType("INTERPOLATION_SMOOTH");
  IElementType INT_CONSTANT = new GDShaderTokenType("INT_CONSTANT");
  IElementType LINE_COMMENT = new GDShaderTokenType("LINE_COMMENT");
  IElementType OP_ADD = new GDShaderTokenType("OP_ADD");
  IElementType OP_AND = new GDShaderTokenType("OP_AND");
  IElementType OP_ASSIGN = new GDShaderTokenType("OP_ASSIGN");
  IElementType OP_ASSIGN_ADD = new GDShaderTokenType("OP_ASSIGN_ADD");
  IElementType OP_ASSIGN_DIV = new GDShaderTokenType("OP_ASSIGN_DIV");
  IElementType OP_ASSIGN_MOD = new GDShaderTokenType("OP_ASSIGN_MOD");
  IElementType OP_ASSIGN_MUL = new GDShaderTokenType("OP_ASSIGN_MUL");
  IElementType OP_ASSIGN_SHIFT_LEFT = new GDShaderTokenType("OP_ASSIGN_SHIFT_LEFT");
  IElementType OP_ASSIGN_SHIFT_RIGHT = new GDShaderTokenType("OP_ASSIGN_SHIFT_RIGHT");
  IElementType OP_ASSIGN_SUB = new GDShaderTokenType("OP_ASSIGN_SUB");
  IElementType OP_BIT_AND = new GDShaderTokenType("OP_BIT_AND");
  IElementType OP_BIT_INVERT = new GDShaderTokenType("OP_BIT_INVERT");
  IElementType OP_BIT_OR = new GDShaderTokenType("OP_BIT_OR");
  IElementType OP_BIT_XOR = new GDShaderTokenType("OP_BIT_XOR");
  IElementType OP_DECREMENT = new GDShaderTokenType("OP_DECREMENT");
  IElementType OP_DIV = new GDShaderTokenType("OP_DIV");
  IElementType OP_EQUAL = new GDShaderTokenType("OP_EQUAL");
  IElementType OP_GREATER = new GDShaderTokenType("OP_GREATER");
  IElementType OP_GREATER_EQUAL = new GDShaderTokenType("OP_GREATER_EQUAL");
  IElementType OP_INCREMENT = new GDShaderTokenType("OP_INCREMENT");
  IElementType OP_LESS = new GDShaderTokenType("OP_LESS");
  IElementType OP_LESS_EQUAL = new GDShaderTokenType("OP_LESS_EQUAL");
  IElementType OP_MOD = new GDShaderTokenType("OP_MOD");
  IElementType OP_MUL = new GDShaderTokenType("OP_MUL");
  IElementType OP_NOT = new GDShaderTokenType("OP_NOT");
  IElementType OP_NOT_EQUAL = new GDShaderTokenType("OP_NOT_EQUAL");
  IElementType OP_OR = new GDShaderTokenType("OP_OR");
  IElementType OP_SHIFT_LEFT = new GDShaderTokenType("OP_SHIFT_LEFT");
  IElementType OP_SHIFT_RIGHT = new GDShaderTokenType("OP_SHIFT_RIGHT");
  IElementType OP_SUB = new GDShaderTokenType("OP_SUB");
  IElementType PARENTHESIS_CLOSE = new GDShaderTokenType("PARENTHESIS_CLOSE");
  IElementType PARENTHESIS_OPEN = new GDShaderTokenType("PARENTHESIS_OPEN");
  IElementType PERIOD = new GDShaderTokenType("PERIOD");
  IElementType PRECISION_HIGH = new GDShaderTokenType("PRECISION_HIGH");
  IElementType PRECISION_LOW = new GDShaderTokenType("PRECISION_LOW");
  IElementType PRECISION_MEDIUM = new GDShaderTokenType("PRECISION_MEDIUM");
  IElementType QUESTION = new GDShaderTokenType("QUESTION");
  IElementType RENDER_MODE = new GDShaderTokenType("RENDER_MODE");
  IElementType REPEAT_DISABLE = new GDShaderTokenType("REPEAT_DISABLE");
  IElementType REPEAT_ENABLE = new GDShaderTokenType("REPEAT_ENABLE");
  IElementType SEMICOLON = new GDShaderTokenType("SEMICOLON");
  IElementType SHADER_TYPE = new GDShaderTokenType("SHADER_TYPE");
  IElementType STENCIL_MODE = new GDShaderTokenType("STENCIL_MODE");
  IElementType STRING_CONSTANT = new GDShaderTokenType("STRING_CONSTANT");
  IElementType STRUCT = new GDShaderTokenType("STRUCT");
  IElementType TRUE = new GDShaderTokenType("TRUE");
  IElementType TYPE_BOOL = new GDShaderTokenType("TYPE_BOOL");
  IElementType TYPE_BVEC2 = new GDShaderTokenType("TYPE_BVEC2");
  IElementType TYPE_BVEC3 = new GDShaderTokenType("TYPE_BVEC3");
  IElementType TYPE_BVEC4 = new GDShaderTokenType("TYPE_BVEC4");
  IElementType TYPE_FLOAT = new GDShaderTokenType("TYPE_FLOAT");
  IElementType TYPE_INT = new GDShaderTokenType("TYPE_INT");
  IElementType TYPE_ISAMPLER2D = new GDShaderTokenType("TYPE_ISAMPLER2D");
  IElementType TYPE_ISAMPLER3D = new GDShaderTokenType("TYPE_ISAMPLER3D");
  IElementType TYPE_IVEC2 = new GDShaderTokenType("TYPE_IVEC2");
  IElementType TYPE_IVEC3 = new GDShaderTokenType("TYPE_IVEC3");
  IElementType TYPE_IVEC4 = new GDShaderTokenType("TYPE_IVEC4");
  IElementType TYPE_MAT2 = new GDShaderTokenType("TYPE_MAT2");
  IElementType TYPE_MAT3 = new GDShaderTokenType("TYPE_MAT3");
  IElementType TYPE_MAT4 = new GDShaderTokenType("TYPE_MAT4");
  IElementType TYPE_SAMPLER2D = new GDShaderTokenType("TYPE_SAMPLER2D");
  IElementType TYPE_SAMPLER3D = new GDShaderTokenType("TYPE_SAMPLER3D");
  IElementType TYPE_SAMPLERCUBE = new GDShaderTokenType("TYPE_SAMPLERCUBE");
  IElementType TYPE_SAMPLERCUBEARRAY = new GDShaderTokenType("TYPE_SAMPLERCUBEARRAY");
  IElementType TYPE_SAMPLEREXT = new GDShaderTokenType("TYPE_SAMPLEREXT");
  IElementType TYPE_UINT = new GDShaderTokenType("TYPE_UINT");
  IElementType TYPE_USAMPLER2D = new GDShaderTokenType("TYPE_USAMPLER2D");
  IElementType TYPE_USAMPLER3D = new GDShaderTokenType("TYPE_USAMPLER3D");
  IElementType TYPE_UVEC2 = new GDShaderTokenType("TYPE_UVEC2");
  IElementType TYPE_UVEC3 = new GDShaderTokenType("TYPE_UVEC3");
  IElementType TYPE_UVEC4 = new GDShaderTokenType("TYPE_UVEC4");
  IElementType TYPE_VEC2 = new GDShaderTokenType("TYPE_VEC2");
  IElementType TYPE_VEC3 = new GDShaderTokenType("TYPE_VEC3");
  IElementType TYPE_VEC4 = new GDShaderTokenType("TYPE_VEC4");
  IElementType TYPE_VOID = new GDShaderTokenType("TYPE_VOID");
  IElementType UINT_CONSTANT = new GDShaderTokenType("UINT_CONSTANT");
  IElementType UNIFORM = new GDShaderTokenType("UNIFORM");
  IElementType UNIFORM_GROUP = new GDShaderTokenType("UNIFORM_GROUP");
  IElementType VARYING = new GDShaderTokenType("VARYING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADDITIVE_EXPR) {
        return new GdshaderAdditiveExprImpl(node);
      }
      else if (type == ARGUMENT_LIST) {
        return new GdshaderArgumentListImpl(node);
      }
      else if (type == ARRAY_SIZE) {
        return new GdshaderArraySizeImpl(node);
      }
      else if (type == ASSIGNMENT_OPERATOR) {
        return new GdshaderAssignmentOperatorImpl(node);
      }
      else if (type == ASSIGN_EXPR) {
        return new GdshaderAssignExprImpl(node);
      }
      else if (type == BITWISE_AND_EXPR) {
        return new GdshaderBitwiseAndExprImpl(node);
      }
      else if (type == BITWISE_OR_EXPR) {
        return new GdshaderBitwiseOrExprImpl(node);
      }
      else if (type == BITWISE_XOR_EXPR) {
        return new GdshaderBitwiseXorExprImpl(node);
      }
      else if (type == BLOCK) {
        return new GdshaderBlockImpl(node);
      }
      else if (type == CASE_CLAUSE) {
        return new GdshaderCaseClauseImpl(node);
      }
      else if (type == CONDITIONAL_EXPR) {
        return new GdshaderConditionalExprImpl(node);
      }
      else if (type == CONST_VARIABLE_DECLARATION) {
        return new GdshaderConstVariableDeclarationImpl(node);
      }
      else if (type == CONTROL_STATEMENT) {
        return new GdshaderControlStatementImpl(node);
      }
      else if (type == DO_WHILE_STATEMENT) {
        return new GdshaderDoWhileStatementImpl(node);
      }
      else if (type == ENUM_HINT) {
        return new GdshaderEnumHintImpl(node);
      }
      else if (type == EQUALITY_EXPR) {
        return new GdshaderEqualityExprImpl(node);
      }
      else if (type == EXPRESSION) {
        return new GdshaderExpressionImpl(node);
      }
      else if (type == EXPRESSION_STATEMENT) {
        return new GdshaderExpressionStatementImpl(node);
      }
      else if (type == FOR_STATEMENT) {
        return new GdshaderForStatementImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new GdshaderFunctionCallImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new GdshaderFunctionDeclarationImpl(node);
      }
      else if (type == FUNCTION_NAME) {
        return new GdshaderFunctionNameImpl(node);
      }
      else if (type == HINT) {
        return new GdshaderHintImpl(node);
      }
      else if (type == HINTS) {
        return new GdshaderHintsImpl(node);
      }
      else if (type == HINT_IDENTIFIER) {
        return new GdshaderHintIdentifierImpl(node);
      }
      else if (type == IF_STATEMENT) {
        return new GdshaderIfStatementImpl(node);
      }
      else if (type == INITIALIZER) {
        return new GdshaderInitializerImpl(node);
      }
      else if (type == INITIALIZER_LIST) {
        return new GdshaderInitializerListImpl(node);
      }
      else if (type == INSTANCE_INDEX_HINT) {
        return new GdshaderInstanceIndexHintImpl(node);
      }
      else if (type == LITERAL) {
        return new GdshaderLiteralImpl(node);
      }
      else if (type == LOGIC_AND_EXPR) {
        return new GdshaderLogicAndExprImpl(node);
      }
      else if (type == LOGIC_OR_EXPR) {
        return new GdshaderLogicOrExprImpl(node);
      }
      else if (type == MULTIPLICATIVE_EXPR) {
        return new GdshaderMultiplicativeExprImpl(node);
      }
      else if (type == NUMBER) {
        return new GdshaderNumberImpl(node);
      }
      else if (type == PARAMETER) {
        return new GdshaderParameterImpl(node);
      }
      else if (type == PARAMETER_LIST) {
        return new GdshaderParameterListImpl(node);
      }
      else if (type == POSTFIX_EXPR) {
        return new GdshaderPostfixExprImpl(node);
      }
      else if (type == PRECISION) {
        return new GdshaderPrecisionImpl(node);
      }
      else if (type == PRIMARY) {
        return new GdshaderPrimaryImpl(node);
      }
      else if (type == RANGE_HINT) {
        return new GdshaderRangeHintImpl(node);
      }
      else if (type == REGULAR_VARIABLE_DECLARATION) {
        return new GdshaderRegularVariableDeclarationImpl(node);
      }
      else if (type == RELATIONAL_EXPR) {
        return new GdshaderRelationalExprImpl(node);
      }
      else if (type == RENDER_MODE_DECLARATION) {
        return new GdshaderRenderModeDeclarationImpl(node);
      }
      else if (type == RETURN_STATEMENT) {
        return new GdshaderReturnStatementImpl(node);
      }
      else if (type == SHADER_TYPE_DECLARATION) {
        return new GdshaderShaderTypeDeclarationImpl(node);
      }
      else if (type == SHIFT_EXPR) {
        return new GdshaderShiftExprImpl(node);
      }
      else if (type == SIMPLE_HINT) {
        return new GdshaderSimpleHintImpl(node);
      }
      else if (type == SIMPLE_STATEMENT) {
        return new GdshaderSimpleStatementImpl(node);
      }
      else if (type == STATEMENT) {
        return new GdshaderStatementImpl(node);
      }
      else if (type == STENCIL_MODE_DECLARATION) {
        return new GdshaderStencilModeDeclarationImpl(node);
      }
      else if (type == STRUCT_DECLARATION) {
        return new GdshaderStructDeclarationImpl(node);
      }
      else if (type == STRUCT_MEMBER) {
        return new GdshaderStructMemberImpl(node);
      }
      else if (type == SWITCH_STATEMENT) {
        return new GdshaderSwitchStatementImpl(node);
      }
      else if (type == TYPE) {
        return new GdshaderTypeImpl(node);
      }
      else if (type == UNARY_EXPR) {
        return new GdshaderUnaryExprImpl(node);
      }
      else if (type == UNIFORM_GROUP_DECLARATION) {
        return new GdshaderUniformGroupDeclarationImpl(node);
      }
      else if (type == UNIFORM_VARIABLE_DECLARATION) {
        return new GdshaderUniformVariableDeclarationImpl(node);
      }
      else if (type == VARIABLE_DECLARATION) {
        return new GdshaderVariableDeclarationImpl(node);
      }
      else if (type == VARYING_VARIABLE_DECLARATION) {
        return new GdshaderVaryingVariableDeclarationImpl(node);
      }
      else if (type == WHILE_STATEMENT) {
        return new GdshaderWhileStatementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

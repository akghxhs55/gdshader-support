// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import kr.jaehoyi.gdshader.psi.impl.*;

public interface GdsTypes {

  IElementType ADDITIVE_EXPR = new GdsElementType("ADDITIVE_EXPR");
  IElementType ARGUMENT_LIST = new GdsElementType("ARGUMENT_LIST");
  IElementType ARRAY_SIZE = new GdsElementType("ARRAY_SIZE");
  IElementType ASSIGNMENT_OPERATOR = new GdsElementType("ASSIGNMENT_OPERATOR");
  IElementType ASSIGN_EXPR = new GdsElementType("ASSIGN_EXPR");
  IElementType BITWISE_AND_EXPR = new GdsElementType("BITWISE_AND_EXPR");
  IElementType BITWISE_OR_EXPR = new GdsElementType("BITWISE_OR_EXPR");
  IElementType BITWISE_XOR_EXPR = new GdsElementType("BITWISE_XOR_EXPR");
  IElementType BLOCK = new GdsElementType("BLOCK");
  IElementType BLOCK_BODY = new GdsElementType("BLOCK_BODY");
  IElementType CASE_BODY = new GdsElementType("CASE_BODY");
  IElementType CASE_CLAUSE = new GdsElementType("CASE_CLAUSE");
  IElementType CONDITIONAL_EXPR = new GdsElementType("CONDITIONAL_EXPR");
  IElementType CONSTANT_DECLARATION = new GdsElementType("CONSTANT_DECLARATION");
  IElementType CONSTANT_DECLARATOR = new GdsElementType("CONSTANT_DECLARATOR");
  IElementType CONSTANT_DECLARATOR_LIST = new GdsElementType("CONSTANT_DECLARATOR_LIST");
  IElementType CONTROL_STATEMENT = new GdsElementType("CONTROL_STATEMENT");
  IElementType DO_WHILE_STATEMENT = new GdsElementType("DO_WHILE_STATEMENT");
  IElementType ELSE_CLAUSE = new GdsElementType("ELSE_CLAUSE");
  IElementType ENUM_HINT = new GdsElementType("ENUM_HINT");
  IElementType EQUALITY_EXPR = new GdsElementType("EQUALITY_EXPR");
  IElementType EXPRESSION = new GdsElementType("EXPRESSION");
  IElementType EXPRESSION_STATEMENT = new GdsElementType("EXPRESSION_STATEMENT");
  IElementType FOR_CONDITION = new GdsElementType("FOR_CONDITION");
  IElementType FOR_INIT = new GdsElementType("FOR_INIT");
  IElementType FOR_ITERATION = new GdsElementType("FOR_ITERATION");
  IElementType FOR_STATEMENT = new GdsElementType("FOR_STATEMENT");
  IElementType FUNCTION_CALL = new GdsElementType("FUNCTION_CALL");
  IElementType FUNCTION_CALL_STATEMENT = new GdsElementType("FUNCTION_CALL_STATEMENT");
  IElementType FUNCTION_DECLARATION = new GdsElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_NAME_DECL = new GdsElementType("FUNCTION_NAME_DECL");
  IElementType FUNCTION_NAME_REF = new GdsElementType("FUNCTION_NAME_REF");
  IElementType HINT = new GdsElementType("HINT");
  IElementType HINT_IDENTIFIER = new GdsElementType("HINT_IDENTIFIER");
  IElementType HINT_LIST = new GdsElementType("HINT_LIST");
  IElementType HINT_SECTION = new GdsElementType("HINT_SECTION");
  IElementType IF_STATEMENT = new GdsElementType("IF_STATEMENT");
  IElementType INITIALIZER = new GdsElementType("INITIALIZER");
  IElementType INITIALIZER_LIST = new GdsElementType("INITIALIZER_LIST");
  IElementType INSTANCE_INDEX_HINT = new GdsElementType("INSTANCE_INDEX_HINT");
  IElementType INTERPOLATION_QUALIFIER = new GdsElementType("INTERPOLATION_QUALIFIER");
  IElementType ITEM = new GdsElementType("ITEM");
  IElementType LITERAL = new GdsElementType("LITERAL");
  IElementType LOCAL_VARIABLE_DECLARATION = new GdsElementType("LOCAL_VARIABLE_DECLARATION");
  IElementType LOCAL_VARIABLE_DECLARATOR = new GdsElementType("LOCAL_VARIABLE_DECLARATOR");
  IElementType LOCAL_VARIABLE_DECLARATOR_LIST = new GdsElementType("LOCAL_VARIABLE_DECLARATOR_LIST");
  IElementType LOGIC_AND_EXPR = new GdsElementType("LOGIC_AND_EXPR");
  IElementType LOGIC_OR_EXPR = new GdsElementType("LOGIC_OR_EXPR");
  IElementType MULTIPLICATIVE_EXPR = new GdsElementType("MULTIPLICATIVE_EXPR");
  IElementType NUMBER = new GdsElementType("NUMBER");
  IElementType PARAMETER = new GdsElementType("PARAMETER");
  IElementType PARAMETER_LIST = new GdsElementType("PARAMETER_LIST");
  IElementType PARAMETER_QUALIFIER = new GdsElementType("PARAMETER_QUALIFIER");
  IElementType POSTFIX_EXPR = new GdsElementType("POSTFIX_EXPR");
  IElementType PRECISION = new GdsElementType("PRECISION");
  IElementType PRIMARY = new GdsElementType("PRIMARY");
  IElementType PRIMITIVE_TYPE = new GdsElementType("PRIMITIVE_TYPE");
  IElementType RANGE_HINT = new GdsElementType("RANGE_HINT");
  IElementType RELATIONAL_EXPR = new GdsElementType("RELATIONAL_EXPR");
  IElementType RENDER_MODE_DECLARATION = new GdsElementType("RENDER_MODE_DECLARATION");
  IElementType RENDER_MODE_DECLARATOR_LIST = new GdsElementType("RENDER_MODE_DECLARATOR_LIST");
  IElementType RENDER_MODE_NAME = new GdsElementType("RENDER_MODE_NAME");
  IElementType RETURN_STATEMENT = new GdsElementType("RETURN_STATEMENT");
  IElementType SHADER_TYPE_DECLARATION = new GdsElementType("SHADER_TYPE_DECLARATION");
  IElementType SHADER_TYPE_NAME = new GdsElementType("SHADER_TYPE_NAME");
  IElementType SHIFT_EXPR = new GdsElementType("SHIFT_EXPR");
  IElementType SIMPLE_HINT = new GdsElementType("SIMPLE_HINT");
  IElementType SIMPLE_STATEMENT = new GdsElementType("SIMPLE_STATEMENT");
  IElementType STATEMENT = new GdsElementType("STATEMENT");
  IElementType STATEMENT_BODY = new GdsElementType("STATEMENT_BODY");
  IElementType STENCIL_MODE_DECLARATION = new GdsElementType("STENCIL_MODE_DECLARATION");
  IElementType STENCIL_MODE_DECLARATOR_LIST = new GdsElementType("STENCIL_MODE_DECLARATOR_LIST");
  IElementType STENCIL_MODE_NAME = new GdsElementType("STENCIL_MODE_NAME");
  IElementType STRUCT_BLOCK = new GdsElementType("STRUCT_BLOCK");
  IElementType STRUCT_DECLARATION = new GdsElementType("STRUCT_DECLARATION");
  IElementType STRUCT_MEMBER = new GdsElementType("STRUCT_MEMBER");
  IElementType STRUCT_MEMBER_LIST = new GdsElementType("STRUCT_MEMBER_LIST");
  IElementType STRUCT_MEMBER_NAME_DECL = new GdsElementType("STRUCT_MEMBER_NAME_DECL");
  IElementType STRUCT_MEMBER_NAME_REF = new GdsElementType("STRUCT_MEMBER_NAME_REF");
  IElementType STRUCT_NAME_DECL = new GdsElementType("STRUCT_NAME_DECL");
  IElementType STRUCT_NAME_REF = new GdsElementType("STRUCT_NAME_REF");
  IElementType SWITCH_BLOCK = new GdsElementType("SWITCH_BLOCK");
  IElementType SWITCH_BODY = new GdsElementType("SWITCH_BODY");
  IElementType SWITCH_STATEMENT = new GdsElementType("SWITCH_STATEMENT");
  IElementType TOP_LEVEL_DECLARATION = new GdsElementType("TOP_LEVEL_DECLARATION");
  IElementType TYPE = new GdsElementType("TYPE");
  IElementType UNARY_EXPR = new GdsElementType("UNARY_EXPR");
  IElementType UNIFORM_DECLARATION = new GdsElementType("UNIFORM_DECLARATION");
  IElementType UNIFORM_GROUP_DECLARATION = new GdsElementType("UNIFORM_GROUP_DECLARATION");
  IElementType UNIFORM_GROUP_NAME = new GdsElementType("UNIFORM_GROUP_NAME");
  IElementType UNIFORM_HEADER = new GdsElementType("UNIFORM_HEADER");
  IElementType UNIFORM_QUALIFIER = new GdsElementType("UNIFORM_QUALIFIER");
  IElementType VARIABLE_NAME_DECL = new GdsElementType("VARIABLE_NAME_DECL");
  IElementType VARIABLE_NAME_REF = new GdsElementType("VARIABLE_NAME_REF");
  IElementType VARYING_DECLARATION = new GdsElementType("VARYING_DECLARATION");
  IElementType WHILE_STATEMENT = new GdsElementType("WHILE_STATEMENT");

  IElementType ARG_IN = new GdsTokenType("ARG_IN");
  IElementType ARG_INOUT = new GdsTokenType("ARG_INOUT");
  IElementType ARG_OUT = new GdsTokenType("ARG_OUT");
  IElementType BLOCK_COMMENT_CONTENT = new GdsTokenType("Block comment content");
  IElementType BLOCK_COMMENT_END = new GdsTokenType("Block comment end");
  IElementType BLOCK_COMMENT_START = new GdsTokenType("Block comment start");
  IElementType BRACKET_CLOSE = new GdsTokenType("BRACKET_CLOSE");
  IElementType BRACKET_OPEN = new GdsTokenType("BRACKET_OPEN");
  IElementType CF_BREAK = new GdsTokenType("CF_BREAK");
  IElementType CF_CASE = new GdsTokenType("CF_CASE");
  IElementType CF_CONTINUE = new GdsTokenType("CF_CONTINUE");
  IElementType CF_DEFAULT = new GdsTokenType("CF_DEFAULT");
  IElementType CF_DISCARD = new GdsTokenType("CF_DISCARD");
  IElementType CF_DO = new GdsTokenType("CF_DO");
  IElementType CF_ELSE = new GdsTokenType("CF_ELSE");
  IElementType CF_FOR = new GdsTokenType("CF_FOR");
  IElementType CF_IF = new GdsTokenType("CF_IF");
  IElementType CF_RETURN = new GdsTokenType("CF_RETURN");
  IElementType CF_SWITCH = new GdsTokenType("CF_SWITCH");
  IElementType CF_WHILE = new GdsTokenType("CF_WHILE");
  IElementType COLON = new GdsTokenType("COLON");
  IElementType COMMA = new GdsTokenType("COMMA");
  IElementType CONST = new GdsTokenType("CONST");
  IElementType CURLY_BRACKET_CLOSE = new GdsTokenType("CURLY_BRACKET_CLOSE");
  IElementType CURLY_BRACKET_OPEN = new GdsTokenType("CURLY_BRACKET_OPEN");
  IElementType FALSE = new GdsTokenType("FALSE");
  IElementType FILTER_LINEAR = new GdsTokenType("FILTER_LINEAR");
  IElementType FILTER_LINEAR_MIPMAP = new GdsTokenType("FILTER_LINEAR_MIPMAP");
  IElementType FILTER_LINEAR_MIPMAP_ANISOTROPIC = new GdsTokenType("FILTER_LINEAR_MIPMAP_ANISOTROPIC");
  IElementType FILTER_NEAREST = new GdsTokenType("FILTER_NEAREST");
  IElementType FILTER_NEAREST_MIPMAP = new GdsTokenType("FILTER_NEAREST_MIPMAP");
  IElementType FILTER_NEAREST_MIPMAP_ANISOTROPIC = new GdsTokenType("FILTER_NEAREST_MIPMAP_ANISOTROPIC");
  IElementType FLOAT_CONSTANT = new GdsTokenType("FLOAT_CONSTANT");
  IElementType GLOBAL = new GdsTokenType("GLOBAL");
  IElementType HINT_ANISOTROPY_TEXTURE = new GdsTokenType("HINT_ANISOTROPY_TEXTURE");
  IElementType HINT_COLOR_CONVERSION_DISABLED = new GdsTokenType("HINT_COLOR_CONVERSION_DISABLED");
  IElementType HINT_DEFAULT_BLACK_TEXTURE = new GdsTokenType("HINT_DEFAULT_BLACK_TEXTURE");
  IElementType HINT_DEFAULT_TRANSPARENT_TEXTURE = new GdsTokenType("HINT_DEFAULT_TRANSPARENT_TEXTURE");
  IElementType HINT_DEFAULT_WHITE_TEXTURE = new GdsTokenType("HINT_DEFAULT_WHITE_TEXTURE");
  IElementType HINT_DEPTH_TEXTURE = new GdsTokenType("HINT_DEPTH_TEXTURE");
  IElementType HINT_ENUM = new GdsTokenType("HINT_ENUM");
  IElementType HINT_INSTANCE_INDEX = new GdsTokenType("HINT_INSTANCE_INDEX");
  IElementType HINT_NORMAL_ROUGHNESS_TEXTURE = new GdsTokenType("HINT_NORMAL_ROUGHNESS_TEXTURE");
  IElementType HINT_NORMAL_TEXTURE = new GdsTokenType("HINT_NORMAL_TEXTURE");
  IElementType HINT_RANGE = new GdsTokenType("HINT_RANGE");
  IElementType HINT_ROUGHNESS_A = new GdsTokenType("HINT_ROUGHNESS_A");
  IElementType HINT_ROUGHNESS_B = new GdsTokenType("HINT_ROUGHNESS_B");
  IElementType HINT_ROUGHNESS_G = new GdsTokenType("HINT_ROUGHNESS_G");
  IElementType HINT_ROUGHNESS_GRAY = new GdsTokenType("HINT_ROUGHNESS_GRAY");
  IElementType HINT_ROUGHNESS_NORMAL_TEXTURE = new GdsTokenType("HINT_ROUGHNESS_NORMAL_TEXTURE");
  IElementType HINT_ROUGHNESS_R = new GdsTokenType("HINT_ROUGHNESS_R");
  IElementType HINT_SCREEN_TEXTURE = new GdsTokenType("HINT_SCREEN_TEXTURE");
  IElementType HINT_SOURCE_COLOR = new GdsTokenType("HINT_SOURCE_COLOR");
  IElementType IDENTIFIER = new GdsTokenType("IDENTIFIER");
  IElementType INSTANCE = new GdsTokenType("INSTANCE");
  IElementType INTERPOLATION_FLAT = new GdsTokenType("INTERPOLATION_FLAT");
  IElementType INTERPOLATION_SMOOTH = new GdsTokenType("INTERPOLATION_SMOOTH");
  IElementType INT_CONSTANT = new GdsTokenType("INT_CONSTANT");
  IElementType LINE_COMMENT = new GdsTokenType("Line comment");
  IElementType OP_ADD = new GdsTokenType("OP_ADD");
  IElementType OP_AND = new GdsTokenType("OP_AND");
  IElementType OP_ASSIGN = new GdsTokenType("OP_ASSIGN");
  IElementType OP_ASSIGN_ADD = new GdsTokenType("OP_ASSIGN_ADD");
  IElementType OP_ASSIGN_BIT_AND = new GdsTokenType("OP_ASSIGN_BIT_AND");
  IElementType OP_ASSIGN_BIT_OR = new GdsTokenType("OP_ASSIGN_BIT_OR");
  IElementType OP_ASSIGN_BIT_XOR = new GdsTokenType("OP_ASSIGN_BIT_XOR");
  IElementType OP_ASSIGN_DIV = new GdsTokenType("OP_ASSIGN_DIV");
  IElementType OP_ASSIGN_MOD = new GdsTokenType("OP_ASSIGN_MOD");
  IElementType OP_ASSIGN_MUL = new GdsTokenType("OP_ASSIGN_MUL");
  IElementType OP_ASSIGN_SHIFT_LEFT = new GdsTokenType("OP_ASSIGN_SHIFT_LEFT");
  IElementType OP_ASSIGN_SHIFT_RIGHT = new GdsTokenType("OP_ASSIGN_SHIFT_RIGHT");
  IElementType OP_ASSIGN_SUB = new GdsTokenType("OP_ASSIGN_SUB");
  IElementType OP_BIT_AND = new GdsTokenType("OP_BIT_AND");
  IElementType OP_BIT_INVERT = new GdsTokenType("OP_BIT_INVERT");
  IElementType OP_BIT_OR = new GdsTokenType("OP_BIT_OR");
  IElementType OP_BIT_XOR = new GdsTokenType("OP_BIT_XOR");
  IElementType OP_DECREMENT = new GdsTokenType("OP_DECREMENT");
  IElementType OP_DIV = new GdsTokenType("OP_DIV");
  IElementType OP_EQUAL = new GdsTokenType("OP_EQUAL");
  IElementType OP_GREATER = new GdsTokenType("OP_GREATER");
  IElementType OP_GREATER_EQUAL = new GdsTokenType("OP_GREATER_EQUAL");
  IElementType OP_INCREMENT = new GdsTokenType("OP_INCREMENT");
  IElementType OP_LESS = new GdsTokenType("OP_LESS");
  IElementType OP_LESS_EQUAL = new GdsTokenType("OP_LESS_EQUAL");
  IElementType OP_MOD = new GdsTokenType("OP_MOD");
  IElementType OP_MUL = new GdsTokenType("OP_MUL");
  IElementType OP_NOT = new GdsTokenType("OP_NOT");
  IElementType OP_NOT_EQUAL = new GdsTokenType("OP_NOT_EQUAL");
  IElementType OP_OR = new GdsTokenType("OP_OR");
  IElementType OP_SHIFT_LEFT = new GdsTokenType("OP_SHIFT_LEFT");
  IElementType OP_SHIFT_RIGHT = new GdsTokenType("OP_SHIFT_RIGHT");
  IElementType OP_SUB = new GdsTokenType("OP_SUB");
  IElementType PARENTHESIS_CLOSE = new GdsTokenType("PARENTHESIS_CLOSE");
  IElementType PARENTHESIS_OPEN = new GdsTokenType("PARENTHESIS_OPEN");
  IElementType PERIOD = new GdsTokenType("PERIOD");
  IElementType PP_DEFINE_LINE = new GdsTokenType("Preprocessor define line");
  IElementType PP_ELIF_LINE = new GdsTokenType("Preprocessor elif line");
  IElementType PP_ELSE_LINE = new GdsTokenType("Preprocessor else line");
  IElementType PP_ENDIF_LINE = new GdsTokenType("Preprocessor endif line");
  IElementType PP_ERROR_LINE = new GdsTokenType("Preprocessor error line");
  IElementType PP_IFDEF_LINE = new GdsTokenType("Preprocessor ifdef line");
  IElementType PP_IFNDEF_LINE = new GdsTokenType("Preprocessor ifndef line");
  IElementType PP_IF_LINE = new GdsTokenType("Preprocessor if line");
  IElementType PP_INCLUDE_LINE = new GdsTokenType("Preprocessor include line");
  IElementType PP_PRAGMA_LINE = new GdsTokenType("Preprocessor pragma line");
  IElementType PP_UNDEF_LINE = new GdsTokenType("Preprocessor undef line");
  IElementType PP_UNKNOWN_LINE = new GdsTokenType("Preprocessor unknown line");
  IElementType PRECISION_HIGH = new GdsTokenType("PRECISION_HIGH");
  IElementType PRECISION_LOW = new GdsTokenType("PRECISION_LOW");
  IElementType PRECISION_MEDIUM = new GdsTokenType("PRECISION_MEDIUM");
  IElementType QUESTION = new GdsTokenType("QUESTION");
  IElementType RENDER_MODE = new GdsTokenType("RENDER_MODE");
  IElementType REPEAT_DISABLE = new GdsTokenType("REPEAT_DISABLE");
  IElementType REPEAT_ENABLE = new GdsTokenType("REPEAT_ENABLE");
  IElementType SEMICOLON = new GdsTokenType("SEMICOLON");
  IElementType SHADER_TYPE = new GdsTokenType("SHADER_TYPE");
  IElementType STENCIL_MODE = new GdsTokenType("STENCIL_MODE");
  IElementType STRING_CONSTANT = new GdsTokenType("STRING_CONSTANT");
  IElementType STRUCT = new GdsTokenType("STRUCT");
  IElementType TRUE = new GdsTokenType("TRUE");
  IElementType TYPE_BOOL = new GdsTokenType("TYPE_BOOL");
  IElementType TYPE_BVEC2 = new GdsTokenType("TYPE_BVEC2");
  IElementType TYPE_BVEC3 = new GdsTokenType("TYPE_BVEC3");
  IElementType TYPE_BVEC4 = new GdsTokenType("TYPE_BVEC4");
  IElementType TYPE_FLOAT = new GdsTokenType("TYPE_FLOAT");
  IElementType TYPE_INT = new GdsTokenType("TYPE_INT");
  IElementType TYPE_ISAMPLER2D = new GdsTokenType("TYPE_ISAMPLER2D");
  IElementType TYPE_ISAMPLER3D = new GdsTokenType("TYPE_ISAMPLER3D");
  IElementType TYPE_IVEC2 = new GdsTokenType("TYPE_IVEC2");
  IElementType TYPE_IVEC3 = new GdsTokenType("TYPE_IVEC3");
  IElementType TYPE_IVEC4 = new GdsTokenType("TYPE_IVEC4");
  IElementType TYPE_MAT2 = new GdsTokenType("TYPE_MAT2");
  IElementType TYPE_MAT3 = new GdsTokenType("TYPE_MAT3");
  IElementType TYPE_MAT4 = new GdsTokenType("TYPE_MAT4");
  IElementType TYPE_SAMPLER2D = new GdsTokenType("TYPE_SAMPLER2D");
  IElementType TYPE_SAMPLER3D = new GdsTokenType("TYPE_SAMPLER3D");
  IElementType TYPE_SAMPLERCUBE = new GdsTokenType("TYPE_SAMPLERCUBE");
  IElementType TYPE_SAMPLERCUBEARRAY = new GdsTokenType("TYPE_SAMPLERCUBEARRAY");
  IElementType TYPE_SAMPLEREXT = new GdsTokenType("TYPE_SAMPLEREXT");
  IElementType TYPE_UINT = new GdsTokenType("TYPE_UINT");
  IElementType TYPE_USAMPLER2D = new GdsTokenType("TYPE_USAMPLER2D");
  IElementType TYPE_USAMPLER3D = new GdsTokenType("TYPE_USAMPLER3D");
  IElementType TYPE_UVEC2 = new GdsTokenType("TYPE_UVEC2");
  IElementType TYPE_UVEC3 = new GdsTokenType("TYPE_UVEC3");
  IElementType TYPE_UVEC4 = new GdsTokenType("TYPE_UVEC4");
  IElementType TYPE_VEC2 = new GdsTokenType("TYPE_VEC2");
  IElementType TYPE_VEC3 = new GdsTokenType("TYPE_VEC3");
  IElementType TYPE_VEC4 = new GdsTokenType("TYPE_VEC4");
  IElementType TYPE_VOID = new GdsTokenType("TYPE_VOID");
  IElementType UINT_CONSTANT = new GdsTokenType("UINT_CONSTANT");
  IElementType UNIFORM = new GdsTokenType("UNIFORM");
  IElementType UNIFORM_GROUP = new GdsTokenType("UNIFORM_GROUP");
  IElementType UNTERMINATED_STRING_CONSTANT = new GdsTokenType("Unterminated string");
  IElementType VARYING = new GdsTokenType("VARYING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADDITIVE_EXPR) {
        return new GdsAdditiveExprImpl(node);
      }
      else if (type == ARGUMENT_LIST) {
        return new GdsArgumentListImpl(node);
      }
      else if (type == ARRAY_SIZE) {
        return new GdsArraySizeImpl(node);
      }
      else if (type == ASSIGNMENT_OPERATOR) {
        return new GdsAssignmentOperatorImpl(node);
      }
      else if (type == ASSIGN_EXPR) {
        return new GdsAssignExprImpl(node);
      }
      else if (type == BITWISE_AND_EXPR) {
        return new GdsBitwiseAndExprImpl(node);
      }
      else if (type == BITWISE_OR_EXPR) {
        return new GdsBitwiseOrExprImpl(node);
      }
      else if (type == BITWISE_XOR_EXPR) {
        return new GdsBitwiseXorExprImpl(node);
      }
      else if (type == BLOCK) {
        return new GdsBlockImpl(node);
      }
      else if (type == BLOCK_BODY) {
        return new GdsBlockBodyImpl(node);
      }
      else if (type == CASE_BODY) {
        return new GdsCaseBodyImpl(node);
      }
      else if (type == CASE_CLAUSE) {
        return new GdsCaseClauseImpl(node);
      }
      else if (type == CONDITIONAL_EXPR) {
        return new GdsConditionalExprImpl(node);
      }
      else if (type == CONSTANT_DECLARATION) {
        return new GdsConstantDeclarationImpl(node);
      }
      else if (type == CONSTANT_DECLARATOR) {
        return new GdsConstantDeclaratorImpl(node);
      }
      else if (type == CONSTANT_DECLARATOR_LIST) {
        return new GdsConstantDeclaratorListImpl(node);
      }
      else if (type == CONTROL_STATEMENT) {
        return new GdsControlStatementImpl(node);
      }
      else if (type == DO_WHILE_STATEMENT) {
        return new GdsDoWhileStatementImpl(node);
      }
      else if (type == ELSE_CLAUSE) {
        return new GdsElseClauseImpl(node);
      }
      else if (type == ENUM_HINT) {
        return new GdsEnumHintImpl(node);
      }
      else if (type == EQUALITY_EXPR) {
        return new GdsEqualityExprImpl(node);
      }
      else if (type == EXPRESSION) {
        return new GdsExpressionImpl(node);
      }
      else if (type == EXPRESSION_STATEMENT) {
        return new GdsExpressionStatementImpl(node);
      }
      else if (type == FOR_CONDITION) {
        return new GdsForConditionImpl(node);
      }
      else if (type == FOR_INIT) {
        return new GdsForInitImpl(node);
      }
      else if (type == FOR_ITERATION) {
        return new GdsForIterationImpl(node);
      }
      else if (type == FOR_STATEMENT) {
        return new GdsForStatementImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new GdsFunctionCallImpl(node);
      }
      else if (type == FUNCTION_CALL_STATEMENT) {
        return new GdsFunctionCallStatementImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new GdsFunctionDeclarationImpl(node);
      }
      else if (type == FUNCTION_NAME_DECL) {
        return new GdsFunctionNameDeclImpl(node);
      }
      else if (type == FUNCTION_NAME_REF) {
        return new GdsFunctionNameRefImpl(node);
      }
      else if (type == HINT) {
        return new GdsHintImpl(node);
      }
      else if (type == HINT_IDENTIFIER) {
        return new GdsHintIdentifierImpl(node);
      }
      else if (type == HINT_LIST) {
        return new GdsHintListImpl(node);
      }
      else if (type == HINT_SECTION) {
        return new GdsHintSectionImpl(node);
      }
      else if (type == IF_STATEMENT) {
        return new GdsIfStatementImpl(node);
      }
      else if (type == INITIALIZER) {
        return new GdsInitializerImpl(node);
      }
      else if (type == INITIALIZER_LIST) {
        return new GdsInitializerListImpl(node);
      }
      else if (type == INSTANCE_INDEX_HINT) {
        return new GdsInstanceIndexHintImpl(node);
      }
      else if (type == INTERPOLATION_QUALIFIER) {
        return new GdsInterpolationQualifierImpl(node);
      }
      else if (type == ITEM) {
        return new GdsItemImpl(node);
      }
      else if (type == LITERAL) {
        return new GdsLiteralImpl(node);
      }
      else if (type == LOCAL_VARIABLE_DECLARATION) {
        return new GdsLocalVariableDeclarationImpl(node);
      }
      else if (type == LOCAL_VARIABLE_DECLARATOR) {
        return new GdsLocalVariableDeclaratorImpl(node);
      }
      else if (type == LOCAL_VARIABLE_DECLARATOR_LIST) {
        return new GdsLocalVariableDeclaratorListImpl(node);
      }
      else if (type == LOGIC_AND_EXPR) {
        return new GdsLogicAndExprImpl(node);
      }
      else if (type == LOGIC_OR_EXPR) {
        return new GdsLogicOrExprImpl(node);
      }
      else if (type == MULTIPLICATIVE_EXPR) {
        return new GdsMultiplicativeExprImpl(node);
      }
      else if (type == NUMBER) {
        return new GdsNumberImpl(node);
      }
      else if (type == PARAMETER) {
        return new GdsParameterImpl(node);
      }
      else if (type == PARAMETER_LIST) {
        return new GdsParameterListImpl(node);
      }
      else if (type == PARAMETER_QUALIFIER) {
        return new GdsParameterQualifierImpl(node);
      }
      else if (type == POSTFIX_EXPR) {
        return new GdsPostfixExprImpl(node);
      }
      else if (type == PRECISION) {
        return new GdsPrecisionImpl(node);
      }
      else if (type == PRIMARY) {
        return new GdsPrimaryImpl(node);
      }
      else if (type == PRIMITIVE_TYPE) {
        return new GdsPrimitiveTypeImpl(node);
      }
      else if (type == RANGE_HINT) {
        return new GdsRangeHintImpl(node);
      }
      else if (type == RELATIONAL_EXPR) {
        return new GdsRelationalExprImpl(node);
      }
      else if (type == RENDER_MODE_DECLARATION) {
        return new GdsRenderModeDeclarationImpl(node);
      }
      else if (type == RENDER_MODE_DECLARATOR_LIST) {
        return new GdsRenderModeDeclaratorListImpl(node);
      }
      else if (type == RENDER_MODE_NAME) {
        return new GdsRenderModeNameImpl(node);
      }
      else if (type == RETURN_STATEMENT) {
        return new GdsReturnStatementImpl(node);
      }
      else if (type == SHADER_TYPE_DECLARATION) {
        return new GdsShaderTypeDeclarationImpl(node);
      }
      else if (type == SHADER_TYPE_NAME) {
        return new GdsShaderTypeNameImpl(node);
      }
      else if (type == SHIFT_EXPR) {
        return new GdsShiftExprImpl(node);
      }
      else if (type == SIMPLE_HINT) {
        return new GdsSimpleHintImpl(node);
      }
      else if (type == SIMPLE_STATEMENT) {
        return new GdsSimpleStatementImpl(node);
      }
      else if (type == STATEMENT) {
        return new GdsStatementImpl(node);
      }
      else if (type == STATEMENT_BODY) {
        return new GdsStatementBodyImpl(node);
      }
      else if (type == STENCIL_MODE_DECLARATION) {
        return new GdsStencilModeDeclarationImpl(node);
      }
      else if (type == STENCIL_MODE_DECLARATOR_LIST) {
        return new GdsStencilModeDeclaratorListImpl(node);
      }
      else if (type == STENCIL_MODE_NAME) {
        return new GdsStencilModeNameImpl(node);
      }
      else if (type == STRUCT_BLOCK) {
        return new GdsStructBlockImpl(node);
      }
      else if (type == STRUCT_DECLARATION) {
        return new GdsStructDeclarationImpl(node);
      }
      else if (type == STRUCT_MEMBER) {
        return new GdsStructMemberImpl(node);
      }
      else if (type == STRUCT_MEMBER_LIST) {
        return new GdsStructMemberListImpl(node);
      }
      else if (type == STRUCT_MEMBER_NAME_DECL) {
        return new GdsStructMemberNameDeclImpl(node);
      }
      else if (type == STRUCT_MEMBER_NAME_REF) {
        return new GdsStructMemberNameRefImpl(node);
      }
      else if (type == STRUCT_NAME_DECL) {
        return new GdsStructNameDeclImpl(node);
      }
      else if (type == STRUCT_NAME_REF) {
        return new GdsStructNameRefImpl(node);
      }
      else if (type == SWITCH_BLOCK) {
        return new GdsSwitchBlockImpl(node);
      }
      else if (type == SWITCH_BODY) {
        return new GdsSwitchBodyImpl(node);
      }
      else if (type == SWITCH_STATEMENT) {
        return new GdsSwitchStatementImpl(node);
      }
      else if (type == TOP_LEVEL_DECLARATION) {
        return new GdsTopLevelDeclarationImpl(node);
      }
      else if (type == TYPE) {
        return new GdsTypeImpl(node);
      }
      else if (type == UNARY_EXPR) {
        return new GdsUnaryExprImpl(node);
      }
      else if (type == UNIFORM_DECLARATION) {
        return new GdsUniformDeclarationImpl(node);
      }
      else if (type == UNIFORM_GROUP_DECLARATION) {
        return new GdsUniformGroupDeclarationImpl(node);
      }
      else if (type == UNIFORM_GROUP_NAME) {
        return new GdsUniformGroupNameImpl(node);
      }
      else if (type == UNIFORM_HEADER) {
        return new GdsUniformHeaderImpl(node);
      }
      else if (type == UNIFORM_QUALIFIER) {
        return new GdsUniformQualifierImpl(node);
      }
      else if (type == VARIABLE_NAME_DECL) {
        return new GdsVariableNameDeclImpl(node);
      }
      else if (type == VARIABLE_NAME_REF) {
        return new GdsVariableNameRefImpl(node);
      }
      else if (type == VARYING_DECLARATION) {
        return new GdsVaryingDeclarationImpl(node);
      }
      else if (type == WHILE_STATEMENT) {
        return new GdsWhileStatementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}

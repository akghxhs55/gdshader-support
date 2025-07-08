// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static kr.jaehoyi.gdshader.psi.GDShaderTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class GDShaderParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return GDShaderFile(b, l + 1);
  }

  /* ********************************************************** */
  // LINE_COMMENT | BLOCK_COMMENT | BRACKET_OPEN | BRACKET_CLOSE | QUESTION | PERIOD | CF_DO | CF_BREAK
  // 		| CF_CONTINUE | CF_RETURN | CF_DISCARD | SHADER_TYPE | RENDER_MODE | STENCIL_MODE | PRECISION_LOW
  // 		| PRECISION_MEDIUM | PRECISION_HIGH | UNIFORM_GROUP | INSTANCE | GLOBAL | ARG_IN | ARG_OUT | ARG_INOUT
  // 		| INTERPOLATION_FLAT | INTERPOLATION_SMOOTH
  public static boolean DUMMY(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "DUMMY")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DUMMY, "<dummy>");
    r = consumeToken(b, LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    if (!r) r = consumeToken(b, BRACKET_OPEN);
    if (!r) r = consumeToken(b, BRACKET_CLOSE);
    if (!r) r = consumeToken(b, QUESTION);
    if (!r) r = consumeToken(b, PERIOD);
    if (!r) r = consumeToken(b, CF_DO);
    if (!r) r = consumeToken(b, CF_BREAK);
    if (!r) r = consumeToken(b, CF_CONTINUE);
    if (!r) r = consumeToken(b, CF_RETURN);
    if (!r) r = consumeToken(b, CF_DISCARD);
    if (!r) r = consumeToken(b, SHADER_TYPE);
    if (!r) r = consumeToken(b, RENDER_MODE);
    if (!r) r = consumeToken(b, STENCIL_MODE);
    if (!r) r = consumeToken(b, PRECISION_LOW);
    if (!r) r = consumeToken(b, PRECISION_MEDIUM);
    if (!r) r = consumeToken(b, PRECISION_HIGH);
    if (!r) r = consumeToken(b, UNIFORM_GROUP);
    if (!r) r = consumeToken(b, INSTANCE);
    if (!r) r = consumeToken(b, GLOBAL);
    if (!r) r = consumeToken(b, ARG_IN);
    if (!r) r = consumeToken(b, ARG_OUT);
    if (!r) r = consumeToken(b, ARG_INOUT);
    if (!r) r = consumeToken(b, INTERPOLATION_FLAT);
    if (!r) r = consumeToken(b, INTERPOLATION_SMOOTH);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // statement*
  static boolean GDShaderFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GDShaderFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "GDShaderFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // multiplicative_expr ((OP_ADD | OP_SUB) multiplicative_expr)*
  public static boolean additive_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADDITIVE_EXPR, "<additive expr>");
    r = multiplicative_expr(b, l + 1);
    r = r && additive_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((OP_ADD | OP_SUB) multiplicative_expr)*
  private static boolean additive_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!additive_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "additive_expr_1", c)) break;
    }
    return true;
  }

  // (OP_ADD | OP_SUB) multiplicative_expr
  private static boolean additive_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = additive_expr_1_0_0(b, l + 1);
    r = r && multiplicative_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_ADD | OP_SUB
  private static boolean additive_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_ADD);
    if (!r) r = consumeToken(b, OP_SUB);
    return r;
  }

  /* ********************************************************** */
  // expression (COMMA expression)*
  public static boolean argument_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT_LIST, "<argument list>");
    r = expression(b, l + 1);
    r = r && argument_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA expression)*
  private static boolean argument_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!argument_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argument_list_1", c)) break;
    }
    return true;
  }

  // COMMA expression
  private static boolean argument_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // logic_or_expr (assignment_operator assign_expr)?
  public static boolean assign_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN_EXPR, "<assign expr>");
    r = logic_or_expr(b, l + 1);
    r = r && assign_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (assignment_operator assign_expr)?
  private static boolean assign_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_expr_1")) return false;
    assign_expr_1_0(b, l + 1);
    return true;
  }

  // assignment_operator assign_expr
  private static boolean assign_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = assignment_operator(b, l + 1);
    r = r && assign_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OP_ASSIGN | OP_ASSIGN_ADD | OP_ASSIGN_SUB | OP_ASSIGN_MUL | OP_ASSIGN_DIV 
  //                         | OP_ASSIGN_MOD | OP_ASSIGN_SHIFT_LEFT | OP_ASSIGN_SHIFT_RIGHT
  public static boolean assignment_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT_OPERATOR, "<assignment operator>");
    r = consumeToken(b, OP_ASSIGN);
    if (!r) r = consumeToken(b, OP_ASSIGN_ADD);
    if (!r) r = consumeToken(b, OP_ASSIGN_SUB);
    if (!r) r = consumeToken(b, OP_ASSIGN_MUL);
    if (!r) r = consumeToken(b, OP_ASSIGN_DIV);
    if (!r) r = consumeToken(b, OP_ASSIGN_MOD);
    if (!r) r = consumeToken(b, OP_ASSIGN_SHIFT_LEFT);
    if (!r) r = consumeToken(b, OP_ASSIGN_SHIFT_RIGHT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // equality_expr (OP_BIT_AND equality_expr)*
  public static boolean bitwise_and_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_and_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BITWISE_AND_EXPR, "<bitwise and expr>");
    r = equality_expr(b, l + 1);
    r = r && bitwise_and_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_BIT_AND equality_expr)*
  private static boolean bitwise_and_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_and_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bitwise_and_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitwise_and_expr_1", c)) break;
    }
    return true;
  }

  // OP_BIT_AND equality_expr
  private static boolean bitwise_and_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_and_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_BIT_AND);
    r = r && equality_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // bitwise_xor_expr (OP_BIT_OR bitwise_xor_expr)*
  public static boolean bitwise_or_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_or_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BITWISE_OR_EXPR, "<bitwise or expr>");
    r = bitwise_xor_expr(b, l + 1);
    r = r && bitwise_or_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_BIT_OR bitwise_xor_expr)*
  private static boolean bitwise_or_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_or_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bitwise_or_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitwise_or_expr_1", c)) break;
    }
    return true;
  }

  // OP_BIT_OR bitwise_xor_expr
  private static boolean bitwise_or_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_or_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_BIT_OR);
    r = r && bitwise_xor_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // bitwise_and_expr (OP_BIT_XOR bitwise_and_expr)*
  public static boolean bitwise_xor_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_xor_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BITWISE_XOR_EXPR, "<bitwise xor expr>");
    r = bitwise_and_expr(b, l + 1);
    r = r && bitwise_xor_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_BIT_XOR bitwise_and_expr)*
  private static boolean bitwise_xor_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_xor_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bitwise_xor_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitwise_xor_expr_1", c)) break;
    }
    return true;
  }

  // OP_BIT_XOR bitwise_and_expr
  private static boolean bitwise_xor_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_xor_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_BIT_XOR);
    r = r && bitwise_and_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CURLY_BRACKET_OPEN statement* CURLY_BRACKET_CLOSE
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, CURLY_BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_BRACKET_OPEN);
    r = r && block_1(b, l + 1);
    r = r && consumeToken(b, CURLY_BRACKET_CLOSE);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  // statement*
  private static boolean block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CF_CASE expression COLON statement* | CF_DEFAULT COLON statement*
  public static boolean case_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_clause")) return false;
    if (!nextTokenIs(b, "<case clause>", CF_CASE, CF_DEFAULT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CASE_CLAUSE, "<case clause>");
    r = case_clause_0(b, l + 1);
    if (!r) r = case_clause_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CF_CASE expression COLON statement*
  private static boolean case_clause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_clause_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CF_CASE);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && case_clause_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement*
  private static boolean case_clause_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_clause_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "case_clause_0_3", c)) break;
    }
    return true;
  }

  // CF_DEFAULT COLON statement*
  private static boolean case_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_clause_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_DEFAULT, COLON);
    r = r && case_clause_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement*
  private static boolean case_clause_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_clause_1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "case_clause_1_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // if_statement
  // 					| for_statement
  // 					| while_statement
  // 					| switch_statement
  public static boolean control_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "control_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONTROL_STATEMENT, "<control statement>");
    r = if_statement(b, l + 1);
    if (!r) r = for_statement(b, l + 1);
    if (!r) r = while_statement(b, l + 1);
    if (!r) r = switch_statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // relational_expr ((OP_EQUAL | OP_NOT_EQUAL) relational_expr)*
  public static boolean equality_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EQUALITY_EXPR, "<equality expr>");
    r = relational_expr(b, l + 1);
    r = r && equality_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((OP_EQUAL | OP_NOT_EQUAL) relational_expr)*
  private static boolean equality_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!equality_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "equality_expr_1", c)) break;
    }
    return true;
  }

  // (OP_EQUAL | OP_NOT_EQUAL) relational_expr
  private static boolean equality_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = equality_expr_1_0_0(b, l + 1);
    r = r && relational_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_EQUAL | OP_NOT_EQUAL
  private static boolean equality_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "equality_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_EQUAL);
    if (!r) r = consumeToken(b, OP_NOT_EQUAL);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  // 			 | literal
  // 			 | assign_expr
  // 			 | function_call
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION, "<expression>");
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = literal(b, l + 1);
    if (!r) r = assign_expr(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expression SEMICOLON
  public static boolean expression_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION_STATEMENT, "<expression statement>");
    r = expression(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CF_FOR PARENTHESIS_OPEN expression_statement expression_statement expression PARENTHESIS_CLOSE block
  public static boolean for_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement")) return false;
    if (!nextTokenIs(b, CF_FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_FOR, PARENTHESIS_OPEN);
    r = r && expression_statement(b, l + 1);
    r = r && expression_statement(b, l + 1);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    r = r && block(b, l + 1);
    exit_section_(b, m, FOR_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER PARENTHESIS_OPEN argument_list? PARENTHESIS_CLOSE
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENTIFIER, PARENTHESIS_OPEN);
    r = r && function_call_2(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    exit_section_(b, m, FUNCTION_CALL, r);
    return r;
  }

  // argument_list?
  private static boolean function_call_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2")) return false;
    argument_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type IDENTIFIER PARENTHESIS_OPEN parameter_list? PARENTHESIS_CLOSE block
  public static boolean function_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = type(b, l + 1);
    r = r && consumeTokens(b, 0, IDENTIFIER, PARENTHESIS_OPEN);
    r = r && function_declaration_3(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    r = r && block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // parameter_list?
  private static boolean function_declaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_declaration_3")) return false;
    parameter_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // CF_IF PARENTHESIS_OPEN expression PARENTHESIS_CLOSE block (CF_ELSE block)?
  public static boolean if_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement")) return false;
    if (!nextTokenIs(b, CF_IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_IF, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    r = r && block(b, l + 1);
    r = r && if_statement_5(b, l + 1);
    exit_section_(b, m, IF_STATEMENT, r);
    return r;
  }

  // (CF_ELSE block)?
  private static boolean if_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_5")) return false;
    if_statement_5_0(b, l + 1);
    return true;
  }

  // CF_ELSE block
  private static boolean if_statement_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CF_ELSE);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TRUE
  // 		  | FALSE
  // 		  | FLOAT_CONSTANT
  // 		  | INT_CONSTANT
  // 		  | UINT_CONSTANT
  // 		  | STRING_CONSTANT
  // 		  
  // {
  //   precedence {
  //     right OP_ASSIGN OP_ASSIGN_ADD OP_ASSIGN_SUB OP_ASSIGN_MUL OP_ASSIGN_DIV
  //           OP_ASSIGN_MOD OP_ASSIGN_SHIFT_LEFT OP_ASSIGN_SHIFT_RIGHT
  //     left OP_OR
  //     left OP_AND
  //     left OP_BIT_OR
  //     left OP_BIT_XOR
  //     left OP_BIT_AND
  //     left OP_EQUAL OP_NOT_EQUAL
  //     left OP_LESS OP_LESS_EQUAL OP_GREATER OP_GREATER_EQUAL
  //     left OP_SHIFT_LEFT OP_SHIFT_RIGHT
  //     left OP_ADD OP_SUB
  //     left OP_MUL OP_DIV OP_MOD
  //     left OP_NOT OP_BIT_INVERT OP_INCREMENT OP_DECREMENT
  //   }
  // }
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL, "<literal>");
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, FLOAT_CONSTANT);
    if (!r) r = consumeToken(b, INT_CONSTANT);
    if (!r) r = consumeToken(b, UINT_CONSTANT);
    if (!r) r = literal_5(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // STRING_CONSTANT
  // 		  
  // {
  //   precedence {
  //     right OP_ASSIGN OP_ASSIGN_ADD OP_ASSIGN_SUB OP_ASSIGN_MUL OP_ASSIGN_DIV
  //           OP_ASSIGN_MOD OP_ASSIGN_SHIFT_LEFT OP_ASSIGN_SHIFT_RIGHT
  //     left OP_OR
  //     left OP_AND
  //     left OP_BIT_OR
  //     left OP_BIT_XOR
  //     left OP_BIT_AND
  //     left OP_EQUAL OP_NOT_EQUAL
  //     left OP_LESS OP_LESS_EQUAL OP_GREATER OP_GREATER_EQUAL
  //     left OP_SHIFT_LEFT OP_SHIFT_RIGHT
  //     left OP_ADD OP_SUB
  //     left OP_MUL OP_DIV OP_MOD
  //     left OP_NOT OP_BIT_INVERT OP_INCREMENT OP_DECREMENT
  //   }
  // }
  private static boolean literal_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING_CONSTANT);
    r = r && literal_5_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // precedence {
  //     right OP_ASSIGN OP_ASSIGN_ADD OP_ASSIGN_SUB OP_ASSIGN_MUL OP_ASSIGN_DIV
  //           OP_ASSIGN_MOD OP_ASSIGN_SHIFT_LEFT OP_ASSIGN_SHIFT_RIGHT
  //     left OP_OR
  //     left OP_AND
  //     left OP_BIT_OR
  //     left OP_BIT_XOR
  //     left OP_BIT_AND
  //     left OP_EQUAL OP_NOT_EQUAL
  //     left OP_LESS OP_LESS_EQUAL OP_GREATER OP_GREATER_EQUAL
  //     left OP_SHIFT_LEFT OP_SHIFT_RIGHT
  //     left OP_ADD OP_SUB
  //     left OP_MUL OP_DIV OP_MOD
  //     left OP_NOT OP_BIT_INVERT OP_INCREMENT OP_DECREMENT
  //   }
  private static boolean literal_5_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_5_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PRECEDENCE);
    r = r && literal_5_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // right OP_ASSIGN OP_ASSIGN_ADD OP_ASSIGN_SUB OP_ASSIGN_MUL OP_ASSIGN_DIV
  //           OP_ASSIGN_MOD OP_ASSIGN_SHIFT_LEFT OP_ASSIGN_SHIFT_RIGHT
  //     left OP_OR
  //     left OP_AND
  //     left OP_BIT_OR
  //     left OP_BIT_XOR
  //     left OP_BIT_AND
  //     left OP_EQUAL OP_NOT_EQUAL
  //     left OP_LESS OP_LESS_EQUAL OP_GREATER OP_GREATER_EQUAL
  //     left OP_SHIFT_LEFT OP_SHIFT_RIGHT
  //     left OP_ADD OP_SUB
  //     left OP_MUL OP_DIV OP_MOD
  //     left OP_NOT OP_BIT_INVERT OP_INCREMENT OP_DECREMENT
  private static boolean literal_5_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_5_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RIGHT, OP_ASSIGN, OP_ASSIGN_ADD, OP_ASSIGN_SUB, OP_ASSIGN_MUL, OP_ASSIGN_DIV, OP_ASSIGN_MOD, OP_ASSIGN_SHIFT_LEFT, OP_ASSIGN_SHIFT_RIGHT, LEFT, OP_OR, LEFT, OP_AND, LEFT, OP_BIT_OR, LEFT, OP_BIT_XOR, LEFT, OP_BIT_AND, LEFT, OP_EQUAL, OP_NOT_EQUAL, LEFT, OP_LESS, OP_LESS_EQUAL, OP_GREATER, OP_GREATER_EQUAL, LEFT, OP_SHIFT_LEFT, OP_SHIFT_RIGHT, LEFT, OP_ADD, OP_SUB, LEFT, OP_MUL, OP_DIV, OP_MOD, LEFT, OP_NOT, OP_BIT_INVERT, OP_INCREMENT, OP_DECREMENT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // bitwise_or_expr (OP_AND bitwise_or_expr)*
  public static boolean logic_and_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logic_and_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOGIC_AND_EXPR, "<logic and expr>");
    r = bitwise_or_expr(b, l + 1);
    r = r && logic_and_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_AND bitwise_or_expr)*
  private static boolean logic_and_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logic_and_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!logic_and_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logic_and_expr_1", c)) break;
    }
    return true;
  }

  // OP_AND bitwise_or_expr
  private static boolean logic_and_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logic_and_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_AND);
    r = r && bitwise_or_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // logic_and_expr (OP_OR logic_and_expr)*
  public static boolean logic_or_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logic_or_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOGIC_OR_EXPR, "<logic or expr>");
    r = logic_and_expr(b, l + 1);
    r = r && logic_or_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_OR logic_and_expr)*
  private static boolean logic_or_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logic_or_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!logic_or_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logic_or_expr_1", c)) break;
    }
    return true;
  }

  // OP_OR logic_and_expr
  private static boolean logic_or_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logic_or_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_OR);
    r = r && logic_and_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // unary_expr ((OP_MUL | OP_DIV | OP_MOD) unary_expr)*
  public static boolean multiplicative_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MULTIPLICATIVE_EXPR, "<multiplicative expr>");
    r = unary_expr(b, l + 1);
    r = r && multiplicative_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((OP_MUL | OP_DIV | OP_MOD) unary_expr)*
  private static boolean multiplicative_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!multiplicative_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "multiplicative_expr_1", c)) break;
    }
    return true;
  }

  // (OP_MUL | OP_DIV | OP_MOD) unary_expr
  private static boolean multiplicative_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = multiplicative_expr_1_0_0(b, l + 1);
    r = r && unary_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_MUL | OP_DIV | OP_MOD
  private static boolean multiplicative_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_MUL);
    if (!r) r = consumeToken(b, OP_DIV);
    if (!r) r = consumeToken(b, OP_MOD);
    return r;
  }

  /* ********************************************************** */
  // type IDENTIFIER
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = type(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // parameter (COMMA parameter)*
  public static boolean parameter_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER_LIST, "<parameter list>");
    r = parameter(b, l + 1);
    r = r && parameter_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA parameter)*
  private static boolean parameter_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!parameter_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameter_list_1", c)) break;
    }
    return true;
  }

  // COMMA parameter
  private static boolean parameter_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER | literal | function_call | PARENTHESIS_OPEN expression PARENTHESIS_CLOSE
  public static boolean primary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMARY, "<primary>");
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = literal(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = primary_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PARENTHESIS_OPEN expression PARENTHESIS_CLOSE
  private static boolean primary_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // shift_expr ((OP_LESS | OP_LESS_EQUAL | OP_GREATER | OP_GREATER_EQUAL) shift_expr)*
  public static boolean relational_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RELATIONAL_EXPR, "<relational expr>");
    r = shift_expr(b, l + 1);
    r = r && relational_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((OP_LESS | OP_LESS_EQUAL | OP_GREATER | OP_GREATER_EQUAL) shift_expr)*
  private static boolean relational_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!relational_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "relational_expr_1", c)) break;
    }
    return true;
  }

  // (OP_LESS | OP_LESS_EQUAL | OP_GREATER | OP_GREATER_EQUAL) shift_expr
  private static boolean relational_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = relational_expr_1_0_0(b, l + 1);
    r = r && shift_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_LESS | OP_LESS_EQUAL | OP_GREATER | OP_GREATER_EQUAL
  private static boolean relational_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relational_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_LESS);
    if (!r) r = consumeToken(b, OP_LESS_EQUAL);
    if (!r) r = consumeToken(b, OP_GREATER);
    if (!r) r = consumeToken(b, OP_GREATER_EQUAL);
    return r;
  }

  /* ********************************************************** */
  // additive_expr ((OP_SHIFT_LEFT | OP_SHIFT_RIGHT) additive_expr)*
  public static boolean shift_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHIFT_EXPR, "<shift expr>");
    r = additive_expr(b, l + 1);
    r = r && shift_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ((OP_SHIFT_LEFT | OP_SHIFT_RIGHT) additive_expr)*
  private static boolean shift_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!shift_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "shift_expr_1", c)) break;
    }
    return true;
  }

  // (OP_SHIFT_LEFT | OP_SHIFT_RIGHT) additive_expr
  private static boolean shift_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = shift_expr_1_0_0(b, l + 1);
    r = r && additive_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_SHIFT_LEFT | OP_SHIFT_RIGHT
  private static boolean shift_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shift_expr_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_SHIFT_LEFT);
    if (!r) r = consumeToken(b, OP_SHIFT_RIGHT);
    return r;
  }

  /* ********************************************************** */
  // variable_declaration
  // 			| function_declaration
  // 			| struct_declaration
  // 			| control_statement
  // 			| expression_statement
  // 			| SEMICOLON
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = variable_declaration(b, l + 1);
    if (!r) r = function_declaration(b, l + 1);
    if (!r) r = struct_declaration(b, l + 1);
    if (!r) r = control_statement(b, l + 1);
    if (!r) r = expression_statement(b, l + 1);
    if (!r) r = consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STRUCT IDENTIFIER CURLY_BRACKET_OPEN struct_member* CURLY_BRACKET_CLOSE SEMICOLON
  public static boolean struct_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_declaration")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, STRUCT, IDENTIFIER, CURLY_BRACKET_OPEN);
    r = r && struct_declaration_3(b, l + 1);
    r = r && consumeTokens(b, 0, CURLY_BRACKET_CLOSE, SEMICOLON);
    exit_section_(b, m, STRUCT_DECLARATION, r);
    return r;
  }

  // struct_member*
  private static boolean struct_declaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_declaration_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!struct_member(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "struct_declaration_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // type IDENTIFIER SEMICOLON
  public static boolean struct_member(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_member")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_MEMBER, "<struct member>");
    r = type(b, l + 1);
    r = r && consumeTokens(b, 0, IDENTIFIER, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CF_SWITCH PARENTHESIS_OPEN expression PARENTHESIS_CLOSE CURLY_BRACKET_OPEN case_clause* CURLY_BRACKET_CLOSE
  public static boolean switch_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_statement")) return false;
    if (!nextTokenIs(b, CF_SWITCH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_SWITCH, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeTokens(b, 0, PARENTHESIS_CLOSE, CURLY_BRACKET_OPEN);
    r = r && switch_statement_5(b, l + 1);
    r = r && consumeToken(b, CURLY_BRACKET_CLOSE);
    exit_section_(b, m, SWITCH_STATEMENT, r);
    return r;
  }

  // case_clause*
  private static boolean switch_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_statement_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!case_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "switch_statement_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TYPE_VOID
  //        | TYPE_BOOL
  //        | TYPE_BVEC2
  //        | TYPE_BVEC3
  //        | TYPE_BVEC4
  //        | TYPE_INT
  //        | TYPE_IVEC2
  //        | TYPE_IVEC3
  //        | TYPE_IVEC4
  //        | TYPE_UINT
  //        | TYPE_UVEC2
  //        | TYPE_UVEC3
  //        | TYPE_UVEC4
  //        | TYPE_FLOAT
  //        | TYPE_VEC2
  //        | TYPE_VEC3
  //        | TYPE_VEC4
  //        | TYPE_MAT2
  //        | TYPE_MAT3
  //        | TYPE_MAT4
  //        | TYPE_SAMPLER2D
  //        | TYPE_ISAMPLER2D
  //        | TYPE_USAMPLER2D
  //        | TYPE_SAMPLER3D
  //        | TYPE_ISAMPLER3D
  //        | TYPE_USAMPLER3D
  //        | TYPE_SAMPLERCUBE
  //        | TYPE_SAMPLERCUBEARRAY
  //        | TYPE_SAMPLEREXT
  //        | IDENTIFIER
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE, "<type>");
    r = consumeToken(b, TYPE_VOID);
    if (!r) r = consumeToken(b, TYPE_BOOL);
    if (!r) r = consumeToken(b, TYPE_BVEC2);
    if (!r) r = consumeToken(b, TYPE_BVEC3);
    if (!r) r = consumeToken(b, TYPE_BVEC4);
    if (!r) r = consumeToken(b, TYPE_INT);
    if (!r) r = consumeToken(b, TYPE_IVEC2);
    if (!r) r = consumeToken(b, TYPE_IVEC3);
    if (!r) r = consumeToken(b, TYPE_IVEC4);
    if (!r) r = consumeToken(b, TYPE_UINT);
    if (!r) r = consumeToken(b, TYPE_UVEC2);
    if (!r) r = consumeToken(b, TYPE_UVEC3);
    if (!r) r = consumeToken(b, TYPE_UVEC4);
    if (!r) r = consumeToken(b, TYPE_FLOAT);
    if (!r) r = consumeToken(b, TYPE_VEC2);
    if (!r) r = consumeToken(b, TYPE_VEC3);
    if (!r) r = consumeToken(b, TYPE_VEC4);
    if (!r) r = consumeToken(b, TYPE_MAT2);
    if (!r) r = consumeToken(b, TYPE_MAT3);
    if (!r) r = consumeToken(b, TYPE_MAT4);
    if (!r) r = consumeToken(b, TYPE_SAMPLER2D);
    if (!r) r = consumeToken(b, TYPE_ISAMPLER2D);
    if (!r) r = consumeToken(b, TYPE_USAMPLER2D);
    if (!r) r = consumeToken(b, TYPE_SAMPLER3D);
    if (!r) r = consumeToken(b, TYPE_ISAMPLER3D);
    if (!r) r = consumeToken(b, TYPE_USAMPLER3D);
    if (!r) r = consumeToken(b, TYPE_SAMPLERCUBE);
    if (!r) r = consumeToken(b, TYPE_SAMPLERCUBEARRAY);
    if (!r) r = consumeToken(b, TYPE_SAMPLEREXT);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (OP_NOT | OP_BIT_INVERT | OP_INCREMENT | OP_DECREMENT)? primary
  public static boolean unary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNARY_EXPR, "<unary expr>");
    r = unary_expr_0(b, l + 1);
    r = r && primary(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_NOT | OP_BIT_INVERT | OP_INCREMENT | OP_DECREMENT)?
  private static boolean unary_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_0")) return false;
    unary_expr_0_0(b, l + 1);
    return true;
  }

  // OP_NOT | OP_BIT_INVERT | OP_INCREMENT | OP_DECREMENT
  private static boolean unary_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_NOT);
    if (!r) r = consumeToken(b, OP_BIT_INVERT);
    if (!r) r = consumeToken(b, OP_INCREMENT);
    if (!r) r = consumeToken(b, OP_DECREMENT);
    return r;
  }

  /* ********************************************************** */
  // (CONST | UNIFORM | VARYING)? type IDENTIFIER (OP_ASSIGN expression)? SEMICONLON
  public static boolean variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_DECLARATION, "<variable declaration>");
    r = variable_declaration_0(b, l + 1);
    r = r && type(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    r = r && variable_declaration_3(b, l + 1);
    r = r && consumeToken(b, SEMICONLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (CONST | UNIFORM | VARYING)?
  private static boolean variable_declaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_0")) return false;
    variable_declaration_0_0(b, l + 1);
    return true;
  }

  // CONST | UNIFORM | VARYING
  private static boolean variable_declaration_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_0_0")) return false;
    boolean r;
    r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, UNIFORM);
    if (!r) r = consumeToken(b, VARYING);
    return r;
  }

  // (OP_ASSIGN expression)?
  private static boolean variable_declaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_3")) return false;
    variable_declaration_3_0(b, l + 1);
    return true;
  }

  // OP_ASSIGN expression
  private static boolean variable_declaration_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASSIGN);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CF_WHILE PARENTHESIS_OPEN expression PARENTHESIS_CLOSE block
  public static boolean while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement")) return false;
    if (!nextTokenIs(b, CF_WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_WHILE, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    r = r && block(b, l + 1);
    exit_section_(b, m, WHILE_STATEMENT, r);
    return r;
  }

}

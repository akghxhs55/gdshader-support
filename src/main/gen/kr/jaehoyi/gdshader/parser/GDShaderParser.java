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
  // item*
  static boolean GDShaderFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "GDShaderFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item(b, l + 1)) break;
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
  // BRACKET_OPEN expression BRACKET_CLOSE
  public static boolean array_size(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_size")) return false;
    if (!nextTokenIs(b, BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSE);
    exit_section_(b, m, ARRAY_SIZE, r);
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
  // CURLY_BRACKET_OPEN statement_body* CURLY_BRACKET_CLOSE
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

  // statement_body*
  private static boolean block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement_body(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // CF_CASE expression COLON statement* | CF_DEFAULT COLON statement_body*
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
      if (!consumeToken(b, STATEMENT)) break;
      if (!empty_element_parsed_guard_(b, "case_clause_0_3", c)) break;
    }
    return true;
  }

  // CF_DEFAULT COLON statement_body*
  private static boolean case_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_clause_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_DEFAULT, COLON);
    r = r && case_clause_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_body*
  private static boolean case_clause_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_clause_1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement_body(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "case_clause_1_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // assign_expr (QUESTION expression COLON expression)?
  public static boolean conditional_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONDITIONAL_EXPR, "<conditional expr>");
    r = assign_expr(b, l + 1);
    r = r && conditional_expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (QUESTION expression COLON expression)?
  private static boolean conditional_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_expr_1")) return false;
    conditional_expr_1_0(b, l + 1);
    return true;
  }

  // QUESTION expression COLON expression
  private static boolean conditional_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, QUESTION);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CONST precision? type variable_declarator_list SEMICOLON
  public static boolean const_variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_variable_declaration")) return false;
    if (!nextTokenIs(b, CONST)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONST);
    r = r && const_variable_declaration_1(b, l + 1);
    r = r && type(b, l + 1);
    r = r && variable_declarator_list(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, CONST_VARIABLE_DECLARATION, r);
    return r;
  }

  // precision?
  private static boolean const_variable_declaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_variable_declaration_1")) return false;
    precision(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // if_statement
  // 					| for_statement
  // 					| while_statement
  // 					| do_while_statement
  // 					| switch_statement
  public static boolean control_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "control_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONTROL_STATEMENT, "<control statement>");
    r = if_statement(b, l + 1);
    if (!r) r = for_statement(b, l + 1);
    if (!r) r = while_statement(b, l + 1);
    if (!r) r = do_while_statement(b, l + 1);
    if (!r) r = switch_statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CF_DO statement_body CF_WHILE PARENTHESIS_OPEN expression PARENTHESIS_CLOSE SEMICOLON
  public static boolean do_while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_while_statement")) return false;
    if (!nextTokenIs(b, CF_DO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CF_DO);
    r = r && statement_body(b, l + 1);
    r = r && consumeTokens(b, 0, CF_WHILE, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeTokens(b, 0, PARENTHESIS_CLOSE, SEMICOLON);
    exit_section_(b, m, DO_WHILE_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // HINT_ENUM PARENTHESIS_OPEN STRING_CONSTANT (COMMA STRING_CONSTANT)* PARENTHESIS_CLOSE
  public static boolean enum_hint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_hint")) return false;
    if (!nextTokenIs(b, HINT_ENUM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, HINT_ENUM, PARENTHESIS_OPEN, STRING_CONSTANT);
    r = r && enum_hint_3(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    exit_section_(b, m, ENUM_HINT, r);
    return r;
  }

  // (COMMA STRING_CONSTANT)*
  private static boolean enum_hint_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_hint_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!enum_hint_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "enum_hint_3", c)) break;
    }
    return true;
  }

  // COMMA STRING_CONSTANT
  private static boolean enum_hint_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_hint_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, STRING_CONSTANT);
    exit_section_(b, m, null, r);
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
  // conditional_expr
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION, "<expression>");
    r = conditional_expr(b, l + 1);
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
  // CF_FOR PARENTHESIS_OPEN (expression | for_variable_declaration) SEMICOLON expression SEMICOLON expression PARENTHESIS_CLOSE statement_body
  public static boolean for_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement")) return false;
    if (!nextTokenIs(b, CF_FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_FOR, PARENTHESIS_OPEN);
    r = r && for_statement_2(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    r = r && statement_body(b, l + 1);
    exit_section_(b, m, FOR_STATEMENT, r);
    return r;
  }

  // expression | for_variable_declaration
  private static boolean for_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_2")) return false;
    boolean r;
    r = expression(b, l + 1);
    if (!r) r = for_variable_declaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // precision? type variable_declarator_list
  public static boolean for_variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_variable_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FOR_VARIABLE_DECLARATION, "<for variable declaration>");
    r = for_variable_declaration_0(b, l + 1);
    r = r && type(b, l + 1);
    r = r && variable_declarator_list(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // precision?
  private static boolean for_variable_declaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_variable_declaration_0")) return false;
    precision(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (primitive_type | function_name) PARENTHESIS_OPEN argument_list? PARENTHESIS_CLOSE
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_CALL, "<function call>");
    r = function_call_0(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_OPEN);
    r = r && function_call_2(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // primitive_type | function_name
  private static boolean function_call_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_0")) return false;
    boolean r;
    r = primitive_type(b, l + 1);
    if (!r) r = function_name(b, l + 1);
    return r;
  }

  // argument_list?
  private static boolean function_call_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2")) return false;
    argument_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type function_name PARENTHESIS_OPEN parameter_list? PARENTHESIS_CLOSE block
  public static boolean function_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = type(b, l + 1);
    r = r && function_name(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_OPEN);
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
  // IDENTIFIER
  public static boolean function_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, FUNCTION_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // simple_hint
  // 	   | range_hint
  // 	   | enum_hint
  // 	   | instance_index_hint
  public static boolean hint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hint")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HINT, "<hint>");
    r = simple_hint(b, l + 1);
    if (!r) r = range_hint(b, l + 1);
    if (!r) r = enum_hint(b, l + 1);
    if (!r) r = instance_index_hint(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // HINT_DEFAULT_WHITE_TEXTURE
  // 			      | HINT_DEFAULT_BLACK_TEXTURE
  // 			      | HINT_DEFAULT_TRANSPARENT_TEXTURE
  // 			      | HINT_NORMAL_TEXTURE
  // 			      | HINT_ROUGHNESS_NORMAL_TEXTURE
  // 			      | HINT_ROUGHNESS_R
  // 			      | HINT_ROUGHNESS_G
  // 			      | HINT_ROUGHNESS_B
  // 			      | HINT_ROUGHNESS_A
  // 			      | HINT_ROUGHNESS_GRAY
  // 			      | HINT_ANISOTROPY_TEXTURE
  // 			      | HINT_SOURCE_COLOR
  // 			      | HINT_COLOR_CONVERSION_DISABLED	
  // 			      | HINT_SCREEN_TEXTURE
  // 			      | HINT_NORMAL_ROUGHNESS_TEXTURE
  // 			      | HINT_DEPTH_TEXTURE
  // 			      | FILTER_NEAREST
  // 			      | FILTER_LINEAR
  // 			      | FILTER_NEAREST_MIPMAP
  // 			      | FILTER_LINEAR_MIPMAP
  // 			      | FILTER_NEAREST_MIPMAP_ANISOTROPIC
  // 			      | FILTER_LINEAR_MIPMAP_ANISOTROPIC
  // 			      | REPEAT_ENABLE
  // 			      | REPEAT_DISABLE
  public static boolean hint_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hint_identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HINT_IDENTIFIER, "<hint identifier>");
    r = consumeToken(b, HINT_DEFAULT_WHITE_TEXTURE);
    if (!r) r = consumeToken(b, HINT_DEFAULT_BLACK_TEXTURE);
    if (!r) r = consumeToken(b, HINT_DEFAULT_TRANSPARENT_TEXTURE);
    if (!r) r = consumeToken(b, HINT_NORMAL_TEXTURE);
    if (!r) r = consumeToken(b, HINT_ROUGHNESS_NORMAL_TEXTURE);
    if (!r) r = consumeToken(b, HINT_ROUGHNESS_R);
    if (!r) r = consumeToken(b, HINT_ROUGHNESS_G);
    if (!r) r = consumeToken(b, HINT_ROUGHNESS_B);
    if (!r) r = consumeToken(b, HINT_ROUGHNESS_A);
    if (!r) r = consumeToken(b, HINT_ROUGHNESS_GRAY);
    if (!r) r = consumeToken(b, HINT_ANISOTROPY_TEXTURE);
    if (!r) r = consumeToken(b, HINT_SOURCE_COLOR);
    if (!r) r = consumeToken(b, HINT_COLOR_CONVERSION_DISABLED);
    if (!r) r = consumeToken(b, HINT_SCREEN_TEXTURE);
    if (!r) r = consumeToken(b, HINT_NORMAL_ROUGHNESS_TEXTURE);
    if (!r) r = consumeToken(b, HINT_DEPTH_TEXTURE);
    if (!r) r = consumeToken(b, FILTER_NEAREST);
    if (!r) r = consumeToken(b, FILTER_LINEAR);
    if (!r) r = consumeToken(b, FILTER_NEAREST_MIPMAP);
    if (!r) r = consumeToken(b, FILTER_LINEAR_MIPMAP);
    if (!r) r = consumeToken(b, FILTER_NEAREST_MIPMAP_ANISOTROPIC);
    if (!r) r = consumeToken(b, FILTER_LINEAR_MIPMAP_ANISOTROPIC);
    if (!r) r = consumeToken(b, REPEAT_ENABLE);
    if (!r) r = consumeToken(b, REPEAT_DISABLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // hint (COMMA hint)*
  public static boolean hints(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hints")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, HINTS, "<hints>");
    r = hint(b, l + 1);
    r = r && hints_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA hint)*
  private static boolean hints_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hints_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!hints_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "hints_1", c)) break;
    }
    return true;
  }

  // COMMA hint
  private static boolean hints_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hints_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && hint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CF_IF PARENTHESIS_OPEN expression PARENTHESIS_CLOSE statement_body (CF_ELSE (if_statement | block))?
  public static boolean if_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement")) return false;
    if (!nextTokenIs(b, CF_IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_IF, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    r = r && statement_body(b, l + 1);
    r = r && if_statement_5(b, l + 1);
    exit_section_(b, m, IF_STATEMENT, r);
    return r;
  }

  // (CF_ELSE (if_statement | block))?
  private static boolean if_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_5")) return false;
    if_statement_5_0(b, l + 1);
    return true;
  }

  // CF_ELSE (if_statement | block)
  private static boolean if_statement_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CF_ELSE);
    r = r && if_statement_5_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // if_statement | block
  private static boolean if_statement_5_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_5_0_1")) return false;
    boolean r;
    r = if_statement(b, l + 1);
    if (!r) r = block(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // expression | initializer_list
  public static boolean initializer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "initializer")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INITIALIZER, "<initializer>");
    r = expression(b, l + 1);
    if (!r) r = initializer_list(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CURLY_BRACKET_OPEN expression (COMMA expression)* CURLY_BRACKET_CLOSE
  public static boolean initializer_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "initializer_list")) return false;
    if (!nextTokenIs(b, CURLY_BRACKET_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CURLY_BRACKET_OPEN);
    r = r && expression(b, l + 1);
    r = r && initializer_list_2(b, l + 1);
    r = r && consumeToken(b, CURLY_BRACKET_CLOSE);
    exit_section_(b, m, INITIALIZER_LIST, r);
    return r;
  }

  // (COMMA expression)*
  private static boolean initializer_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "initializer_list_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!initializer_list_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "initializer_list_2", c)) break;
    }
    return true;
  }

  // COMMA expression
  private static boolean initializer_list_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "initializer_list_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // HINT_INSTANCE_INDEX PARENTHESIS_OPEN INT_CONSTANT PARENTHESIS_CLOSE
  public static boolean instance_index_hint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "instance_index_hint")) return false;
    if (!nextTokenIs(b, HINT_INSTANCE_INDEX)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, HINT_INSTANCE_INDEX, PARENTHESIS_OPEN, INT_CONSTANT, PARENTHESIS_CLOSE);
    exit_section_(b, m, INSTANCE_INDEX_HINT, r);
    return r;
  }

  /* ********************************************************** */
  // top_level_declaration
  // 	   | SEMICOLON
  // 	   | LINE_COMMENT
  // 	   | BLOCK_COMMENT
  // 	   | preprocessor_directive
  public static boolean item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ITEM, "<item>");
    r = top_level_declaration(b, l + 1);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    if (!r) r = preprocessor_directive(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // TRUE
  // 		  | FALSE
  // 		  | FLOAT_CONSTANT
  // 		  | INT_CONSTANT
  // 		  | UINT_CONSTANT
  // 		  | STRING_CONSTANT
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL, "<literal>");
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, FLOAT_CONSTANT);
    if (!r) r = consumeToken(b, INT_CONSTANT);
    if (!r) r = consumeToken(b, UINT_CONSTANT);
    if (!r) r = consumeToken(b, STRING_CONSTANT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // precision? type variable_declarator_list SEMICOLON
  public static boolean local_variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_variable_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LOCAL_VARIABLE_DECLARATION, "<local variable declaration>");
    r = local_variable_declaration_0(b, l + 1);
    r = r && type(b, l + 1);
    r = r && variable_declarator_list(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // precision?
  private static boolean local_variable_declaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_variable_declaration_0")) return false;
    precision(b, l + 1);
    return true;
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
  // (OP_SUB)? FLOAT_CONSTANT
  // 		 | (OP_SUB)? INT_CONSTANT
  // 		 | UINT_CONSTANT
  public static boolean number(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NUMBER, "<number>");
    r = number_0(b, l + 1);
    if (!r) r = number_1(b, l + 1);
    if (!r) r = consumeToken(b, UINT_CONSTANT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_SUB)? FLOAT_CONSTANT
  private static boolean number_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = number_0_0(b, l + 1);
    r = r && consumeToken(b, FLOAT_CONSTANT);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OP_SUB)?
  private static boolean number_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number_0_0")) return false;
    consumeToken(b, OP_SUB);
    return true;
  }

  // (OP_SUB)? INT_CONSTANT
  private static boolean number_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = number_1_0(b, l + 1);
    r = r && consumeToken(b, INT_CONSTANT);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OP_SUB)?
  private static boolean number_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number_1_0")) return false;
    consumeToken(b, OP_SUB);
    return true;
  }

  /* ********************************************************** */
  // (ARG_IN | ARG_OUT | ARG_INOUT)? type parameter_name
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER, "<parameter>");
    r = parameter_0(b, l + 1);
    r = r && type(b, l + 1);
    r = r && parameter_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (ARG_IN | ARG_OUT | ARG_INOUT)?
  private static boolean parameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_0")) return false;
    parameter_0_0(b, l + 1);
    return true;
  }

  // ARG_IN | ARG_OUT | ARG_INOUT
  private static boolean parameter_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_0_0")) return false;
    boolean r;
    r = consumeToken(b, ARG_IN);
    if (!r) r = consumeToken(b, ARG_OUT);
    if (!r) r = consumeToken(b, ARG_INOUT);
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
  // IDENTIFIER
  public static boolean parameter_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, PARAMETER_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // primary (PERIOD struct_member_name | BRACKET_OPEN expression BRACKET_CLOSE)* (OP_INCREMENT | OP_DECREMENT)?
  public static boolean postfix_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POSTFIX_EXPR, "<postfix expr>");
    r = primary(b, l + 1);
    r = r && postfix_expr_1(b, l + 1);
    r = r && postfix_expr_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (PERIOD struct_member_name | BRACKET_OPEN expression BRACKET_CLOSE)*
  private static boolean postfix_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!postfix_expr_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "postfix_expr_1", c)) break;
    }
    return true;
  }

  // PERIOD struct_member_name | BRACKET_OPEN expression BRACKET_CLOSE
  private static boolean postfix_expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = postfix_expr_1_0_0(b, l + 1);
    if (!r) r = postfix_expr_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERIOD struct_member_name
  private static boolean postfix_expr_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERIOD);
    r = r && struct_member_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BRACKET_OPEN expression BRACKET_CLOSE
  private static boolean postfix_expr_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACKET_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, BRACKET_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OP_INCREMENT | OP_DECREMENT)?
  private static boolean postfix_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_2")) return false;
    postfix_expr_2_0(b, l + 1);
    return true;
  }

  // OP_INCREMENT | OP_DECREMENT
  private static boolean postfix_expr_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "postfix_expr_2_0")) return false;
    boolean r;
    r = consumeToken(b, OP_INCREMENT);
    if (!r) r = consumeToken(b, OP_DECREMENT);
    return r;
  }

  /* ********************************************************** */
  // PRECISION_LOW | PRECISION_MEDIUM | PRECISION_HIGH
  public static boolean precision(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "precision")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRECISION, "<precision>");
    r = consumeToken(b, PRECISION_LOW);
    if (!r) r = consumeToken(b, PRECISION_MEDIUM);
    if (!r) r = consumeToken(b, PRECISION_HIGH);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PP_DEFINE_LINE
  //                          | PP_UNDEF_LINE
  //                          | PP_IF_LINE
  //                          | PP_ELSE_LINE
  //                          | PP_ELIF_LINE
  //                          | PP_ENDIF_LINE
  //                          | PP_IFDEF_LINE
  //                          | PP_IFNDEF_LINE
  //                          | PP_ERROR_LINE
  //                          | PP_INCLUDE_LINE
  //                          | PP_PRAGMA_LINE
  public static boolean preprocessor_directive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "preprocessor_directive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PREPROCESSOR_DIRECTIVE, "<preprocessor directive>");
    r = consumeToken(b, PP_DEFINE_LINE);
    if (!r) r = consumeToken(b, PP_UNDEF_LINE);
    if (!r) r = consumeToken(b, PP_IF_LINE);
    if (!r) r = consumeToken(b, PP_ELSE_LINE);
    if (!r) r = consumeToken(b, PP_ELIF_LINE);
    if (!r) r = consumeToken(b, PP_ENDIF_LINE);
    if (!r) r = consumeToken(b, PP_IFDEF_LINE);
    if (!r) r = consumeToken(b, PP_IFNDEF_LINE);
    if (!r) r = consumeToken(b, PP_ERROR_LINE);
    if (!r) r = consumeToken(b, PP_INCLUDE_LINE);
    if (!r) r = consumeToken(b, PP_PRAGMA_LINE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // literal
  // 		  | function_call
  // 		  | PARENTHESIS_OPEN expression PARENTHESIS_CLOSE
  // 		  | variable_name
  public static boolean primary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMARY, "<primary>");
    r = literal(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = primary_2(b, l + 1);
    if (!r) r = variable_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PARENTHESIS_OPEN expression PARENTHESIS_CLOSE
  private static boolean primary_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TYPE_VOID
  // 			   | TYPE_BOOL
  // 			   | TYPE_BVEC2
  // 			   | TYPE_BVEC3
  // 			   | TYPE_BVEC4
  // 			   | TYPE_INT
  // 			   | TYPE_IVEC2
  // 			   | TYPE_IVEC3
  // 			   | TYPE_IVEC4
  // 			   | TYPE_UINT
  // 			   | TYPE_UVEC2
  // 			   | TYPE_UVEC3
  // 			   | TYPE_UVEC4
  // 			   | TYPE_FLOAT
  // 			   | TYPE_VEC2
  // 			   | TYPE_VEC3
  // 			   | TYPE_VEC4
  // 			   | TYPE_MAT2
  // 			   | TYPE_MAT3
  // 			   | TYPE_MAT4
  public static boolean primitive_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE_TYPE, "<primitive type>");
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
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // HINT_RANGE PARENTHESIS_OPEN number COMMA number (COMMA number)? PARENTHESIS_CLOSE
  public static boolean range_hint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_hint")) return false;
    if (!nextTokenIs(b, HINT_RANGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, HINT_RANGE, PARENTHESIS_OPEN);
    r = r && number(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && number(b, l + 1);
    r = r && range_hint_5(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    exit_section_(b, m, RANGE_HINT, r);
    return r;
  }

  // (COMMA number)?
  private static boolean range_hint_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_hint_5")) return false;
    range_hint_5_0(b, l + 1);
    return true;
  }

  // COMMA number
  private static boolean range_hint_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_hint_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && number(b, l + 1);
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
  // RENDER_MODE IDENTIFIER (COMMA IDENTIFIER)* SEMICOLON
  public static boolean render_mode_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "render_mode_declaration")) return false;
    if (!nextTokenIs(b, RENDER_MODE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RENDER_MODE, IDENTIFIER);
    r = r && render_mode_declaration_2(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, RENDER_MODE_DECLARATION, r);
    return r;
  }

  // (COMMA IDENTIFIER)*
  private static boolean render_mode_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "render_mode_declaration_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!render_mode_declaration_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "render_mode_declaration_2", c)) break;
    }
    return true;
  }

  // COMMA IDENTIFIER
  private static boolean render_mode_declaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "render_mode_declaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CF_RETURN expression? SEMICOLON
  public static boolean return_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_statement")) return false;
    if (!nextTokenIs(b, CF_RETURN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CF_RETURN);
    r = r && return_statement_1(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, RETURN_STATEMENT, r);
    return r;
  }

  // expression?
  private static boolean return_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_statement_1")) return false;
    expression(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SHADER_TYPE shader_type_name SEMICOLON
  public static boolean shader_type_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shader_type_declaration")) return false;
    if (!nextTokenIs(b, SHADER_TYPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SHADER_TYPE);
    r = r && shader_type_name(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, SHADER_TYPE_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean shader_type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shader_type_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, SHADER_TYPE_NAME, r);
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
  // hint_identifier (COMMA hint_identifier)*
  public static boolean simple_hint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_hint")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIMPLE_HINT, "<simple hint>");
    r = hint_identifier(b, l + 1);
    r = r && simple_hint_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (COMMA hint_identifier)*
  private static boolean simple_hint_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_hint_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!simple_hint_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "simple_hint_1", c)) break;
    }
    return true;
  }

  // COMMA hint_identifier
  private static boolean simple_hint_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_hint_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && hint_identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CF_BREAK SEMICOLON
  // 					| CF_CONTINUE SEMICOLON
  // 					| CF_DISCARD SEMICOLON
  public static boolean simple_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIMPLE_STATEMENT, "<simple statement>");
    r = parseTokens(b, 0, CF_BREAK, SEMICOLON);
    if (!r) r = parseTokens(b, 0, CF_CONTINUE, SEMICOLON);
    if (!r) r = parseTokens(b, 0, CF_DISCARD, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // block
  // 				 | control_statement
  // 				 | return_statement
  // 				 | simple_statement
  // 				 | expression_statement
  // 				 | const_variable_declaration
  // 				 | local_variable_declaration
  // 				 | SEMICOLON
  // 				 | LINE_COMMENT
  // 				 | BLOCK_COMMENT
  // 				 | preprocessor_directive
  public static boolean statement_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT_BODY, "<statement body>");
    r = block(b, l + 1);
    if (!r) r = control_statement(b, l + 1);
    if (!r) r = return_statement(b, l + 1);
    if (!r) r = simple_statement(b, l + 1);
    if (!r) r = expression_statement(b, l + 1);
    if (!r) r = const_variable_declaration(b, l + 1);
    if (!r) r = local_variable_declaration(b, l + 1);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    if (!r) r = preprocessor_directive(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STENCIL_MODE IDENTIFIER SEMICOLON
  public static boolean stencil_mode_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stencil_mode_declaration")) return false;
    if (!nextTokenIs(b, STENCIL_MODE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, STENCIL_MODE, IDENTIFIER, SEMICOLON);
    exit_section_(b, m, STENCIL_MODE_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // STRUCT struct_name CURLY_BRACKET_OPEN struct_member* CURLY_BRACKET_CLOSE SEMICOLON
  public static boolean struct_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_declaration")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRUCT);
    r = r && struct_name(b, l + 1);
    r = r && consumeToken(b, CURLY_BRACKET_OPEN);
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
  // type struct_member_name (array_size)? SEMICOLON
  public static boolean struct_member(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_member")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_MEMBER, "<struct member>");
    r = type(b, l + 1);
    r = r && struct_member_name(b, l + 1);
    r = r && struct_member_2(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (array_size)?
  private static boolean struct_member_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_member_2")) return false;
    struct_member_2_0(b, l + 1);
    return true;
  }

  // (array_size)
  private static boolean struct_member_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_member_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_size(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean struct_member_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_member_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, STRUCT_MEMBER_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean struct_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, STRUCT_NAME, r);
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
  // shader_type_declaration
  // 		    		    | render_mode_declaration
  // 		    		    | stencil_mode_declaration
  // 		    		    | uniform_group_declaration
  // 		    		    | uniform_variable_declaration
  // 		    		    | const_variable_declaration
  // 		    		    | varying_variable_declaration
  // 		    		    | function_declaration
  // 		    		    | struct_declaration
  public static boolean top_level_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TOP_LEVEL_DECLARATION, "<top level declaration>");
    r = shader_type_declaration(b, l + 1);
    if (!r) r = render_mode_declaration(b, l + 1);
    if (!r) r = stencil_mode_declaration(b, l + 1);
    if (!r) r = uniform_group_declaration(b, l + 1);
    if (!r) r = uniform_variable_declaration(b, l + 1);
    if (!r) r = const_variable_declaration(b, l + 1);
    if (!r) r = varying_variable_declaration(b, l + 1);
    if (!r) r = function_declaration(b, l + 1);
    if (!r) r = struct_declaration(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // primitive_type
  //        | TYPE_SAMPLER2D
  //        | TYPE_ISAMPLER2D
  //        | TYPE_USAMPLER2D
  //        | TYPE_SAMPLER3D
  //        | TYPE_ISAMPLER3D
  //        | TYPE_USAMPLER3D
  //        | TYPE_SAMPLERCUBE
  //        | TYPE_SAMPLERCUBEARRAY
  //        | TYPE_SAMPLEREXT
  //        | struct_name
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE, "<type>");
    r = primitive_type(b, l + 1);
    if (!r) r = consumeToken(b, TYPE_SAMPLER2D);
    if (!r) r = consumeToken(b, TYPE_ISAMPLER2D);
    if (!r) r = consumeToken(b, TYPE_USAMPLER2D);
    if (!r) r = consumeToken(b, TYPE_SAMPLER3D);
    if (!r) r = consumeToken(b, TYPE_ISAMPLER3D);
    if (!r) r = consumeToken(b, TYPE_USAMPLER3D);
    if (!r) r = consumeToken(b, TYPE_SAMPLERCUBE);
    if (!r) r = consumeToken(b, TYPE_SAMPLERCUBEARRAY);
    if (!r) r = consumeToken(b, TYPE_SAMPLEREXT);
    if (!r) r = struct_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (OP_NOT | OP_BIT_INVERT | OP_INCREMENT | OP_DECREMENT | OP_SUB)? postfix_expr
  public static boolean unary_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNARY_EXPR, "<unary expr>");
    r = unary_expr_0(b, l + 1);
    r = r && postfix_expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (OP_NOT | OP_BIT_INVERT | OP_INCREMENT | OP_DECREMENT | OP_SUB)?
  private static boolean unary_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_0")) return false;
    unary_expr_0_0(b, l + 1);
    return true;
  }

  // OP_NOT | OP_BIT_INVERT | OP_INCREMENT | OP_DECREMENT | OP_SUB
  private static boolean unary_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expr_0_0")) return false;
    boolean r;
    r = consumeToken(b, OP_NOT);
    if (!r) r = consumeToken(b, OP_BIT_INVERT);
    if (!r) r = consumeToken(b, OP_INCREMENT);
    if (!r) r = consumeToken(b, OP_DECREMENT);
    if (!r) r = consumeToken(b, OP_SUB);
    return r;
  }

  /* ********************************************************** */
  // UNIFORM_GROUP IDENTIFIER? (PERIOD IDENTIFIER)* SEMICOLON
  public static boolean uniform_group_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_group_declaration")) return false;
    if (!nextTokenIs(b, UNIFORM_GROUP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UNIFORM_GROUP);
    r = r && uniform_group_declaration_1(b, l + 1);
    r = r && uniform_group_declaration_2(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, UNIFORM_GROUP_DECLARATION, r);
    return r;
  }

  // IDENTIFIER?
  private static boolean uniform_group_declaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_group_declaration_1")) return false;
    consumeToken(b, IDENTIFIER);
    return true;
  }

  // (PERIOD IDENTIFIER)*
  private static boolean uniform_group_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_group_declaration_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!uniform_group_declaration_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "uniform_group_declaration_2", c)) break;
    }
    return true;
  }

  // PERIOD IDENTIFIER
  private static boolean uniform_group_declaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_group_declaration_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PERIOD, IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (GLOBAL | INSTANCE)? UNIFORM precision? type variable_name (COLON hints)? (OP_ASSIGN expression)? SEMICOLON
  public static boolean uniform_variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNIFORM_VARIABLE_DECLARATION, "<uniform variable declaration>");
    r = uniform_variable_declaration_0(b, l + 1);
    r = r && consumeToken(b, UNIFORM);
    r = r && uniform_variable_declaration_2(b, l + 1);
    r = r && type(b, l + 1);
    r = r && variable_name(b, l + 1);
    r = r && uniform_variable_declaration_5(b, l + 1);
    r = r && uniform_variable_declaration_6(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (GLOBAL | INSTANCE)?
  private static boolean uniform_variable_declaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration_0")) return false;
    uniform_variable_declaration_0_0(b, l + 1);
    return true;
  }

  // GLOBAL | INSTANCE
  private static boolean uniform_variable_declaration_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration_0_0")) return false;
    boolean r;
    r = consumeToken(b, GLOBAL);
    if (!r) r = consumeToken(b, INSTANCE);
    return r;
  }

  // precision?
  private static boolean uniform_variable_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration_2")) return false;
    precision(b, l + 1);
    return true;
  }

  // (COLON hints)?
  private static boolean uniform_variable_declaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration_5")) return false;
    uniform_variable_declaration_5_0(b, l + 1);
    return true;
  }

  // COLON hints
  private static boolean uniform_variable_declaration_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && hints(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OP_ASSIGN expression)?
  private static boolean uniform_variable_declaration_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration_6")) return false;
    uniform_variable_declaration_6_0(b, l + 1);
    return true;
  }

  // OP_ASSIGN expression
  private static boolean uniform_variable_declaration_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "uniform_variable_declaration_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASSIGN);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable_name array_size? (OP_ASSIGN initializer)?
  public static boolean variable_declarator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declarator")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_name(b, l + 1);
    r = r && variable_declarator_1(b, l + 1);
    r = r && variable_declarator_2(b, l + 1);
    exit_section_(b, m, VARIABLE_DECLARATOR, r);
    return r;
  }

  // array_size?
  private static boolean variable_declarator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declarator_1")) return false;
    array_size(b, l + 1);
    return true;
  }

  // (OP_ASSIGN initializer)?
  private static boolean variable_declarator_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declarator_2")) return false;
    variable_declarator_2_0(b, l + 1);
    return true;
  }

  // OP_ASSIGN initializer
  private static boolean variable_declarator_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declarator_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASSIGN);
    r = r && initializer(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable_declarator (COMMA variable_declarator)*
  public static boolean variable_declarator_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declarator_list")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_declarator(b, l + 1);
    r = r && variable_declarator_list_1(b, l + 1);
    exit_section_(b, m, VARIABLE_DECLARATOR_LIST, r);
    return r;
  }

  // (COMMA variable_declarator)*
  private static boolean variable_declarator_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declarator_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!variable_declarator_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variable_declarator_list_1", c)) break;
    }
    return true;
  }

  // COMMA variable_declarator
  private static boolean variable_declarator_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declarator_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && variable_declarator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean variable_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, VARIABLE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // VARYING (INTERPOLATION_FLAT | INTERPOLATION_SMOOTH)? precision? type variable_name array_size? (COLON hints)? SEMICOLON
  public static boolean varying_variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varying_variable_declaration")) return false;
    if (!nextTokenIs(b, VARYING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARYING);
    r = r && varying_variable_declaration_1(b, l + 1);
    r = r && varying_variable_declaration_2(b, l + 1);
    r = r && type(b, l + 1);
    r = r && variable_name(b, l + 1);
    r = r && varying_variable_declaration_5(b, l + 1);
    r = r && varying_variable_declaration_6(b, l + 1);
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, m, VARYING_VARIABLE_DECLARATION, r);
    return r;
  }

  // (INTERPOLATION_FLAT | INTERPOLATION_SMOOTH)?
  private static boolean varying_variable_declaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varying_variable_declaration_1")) return false;
    varying_variable_declaration_1_0(b, l + 1);
    return true;
  }

  // INTERPOLATION_FLAT | INTERPOLATION_SMOOTH
  private static boolean varying_variable_declaration_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varying_variable_declaration_1_0")) return false;
    boolean r;
    r = consumeToken(b, INTERPOLATION_FLAT);
    if (!r) r = consumeToken(b, INTERPOLATION_SMOOTH);
    return r;
  }

  // precision?
  private static boolean varying_variable_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varying_variable_declaration_2")) return false;
    precision(b, l + 1);
    return true;
  }

  // array_size?
  private static boolean varying_variable_declaration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varying_variable_declaration_5")) return false;
    array_size(b, l + 1);
    return true;
  }

  // (COLON hints)?
  private static boolean varying_variable_declaration_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varying_variable_declaration_6")) return false;
    varying_variable_declaration_6_0(b, l + 1);
    return true;
  }

  // COLON hints
  private static boolean varying_variable_declaration_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varying_variable_declaration_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && hints(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // CF_WHILE PARENTHESIS_OPEN expression PARENTHESIS_CLOSE statement_body
  public static boolean while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement")) return false;
    if (!nextTokenIs(b, CF_WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CF_WHILE, PARENTHESIS_OPEN);
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, PARENTHESIS_CLOSE);
    r = r && statement_body(b, l + 1);
    exit_section_(b, m, WHILE_STATEMENT, r);
    return r;
  }

}

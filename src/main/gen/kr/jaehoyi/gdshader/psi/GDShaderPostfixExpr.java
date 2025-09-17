// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderPostfixExpr extends PsiElement {

  @NotNull
  List<GDShaderArgumentList> getArgumentListList();

  @NotNull
  List<GDShaderExpression> getExpressionList();

  @NotNull
  List<GDShaderFunctionName> getFunctionNameList();

  @NotNull
  GDShaderPrimary getPrimary();

  @NotNull
  List<GDShaderStructMemberName> getStructMemberNameList();

}

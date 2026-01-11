// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsPostfixExpr extends PsiElement {

  @NotNull
  List<GdsExpression> getExpressionList();

  @NotNull
  List<GdsFunctionCall> getFunctionCallList();

  @NotNull
  GdsPrimary getPrimary();

  @NotNull
  List<GdsStructMemberNameRef> getStructMemberNameRefList();

}

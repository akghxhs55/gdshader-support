// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderIfStatement extends PsiElement {

  @Nullable
  GDShaderExpression getExpression();

  @Nullable
  GDShaderIfStatement getIfStatement();

  @NotNull
  List<GDShaderStatementBody> getStatementBodyList();

}

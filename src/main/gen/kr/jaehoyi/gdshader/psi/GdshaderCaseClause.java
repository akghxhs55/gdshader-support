// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdshaderCaseClause extends PsiElement {

  @Nullable
  GdshaderExpression getExpression();

  @NotNull
  List<GdshaderItem> getItemList();

  @NotNull
  List<GdshaderStatement> getStatementList();

}

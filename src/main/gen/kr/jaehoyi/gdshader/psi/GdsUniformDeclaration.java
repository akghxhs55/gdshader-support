// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsUniformDeclaration extends PsiElement {

  @NotNull
  List<GdsArraySize> getArraySizeList();

  @Nullable
  GdsExpression getExpression();

  @Nullable
  GdsHintSection getHintSection();

  @Nullable
  GdsPrecision getPrecision();

  @Nullable
  GdsType getType();

  @NotNull
  GdsUniformHeader getUniformHeader();

  @Nullable
  GdsVariableNameDecl getVariableNameDecl();

}

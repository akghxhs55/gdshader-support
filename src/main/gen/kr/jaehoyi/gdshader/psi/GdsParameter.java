// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsParameter extends PsiElement {

  @NotNull
  List<GdsArraySize> getArraySizeList();

  @Nullable
  GdsParameterQualifier getParameterQualifier();

  @Nullable
  GdsPrecision getPrecision();

  @NotNull
  GdsType getType();

  @NotNull
  GdsVariableNameDecl getVariableNameDecl();

}

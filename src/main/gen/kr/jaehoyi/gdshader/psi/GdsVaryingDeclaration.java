// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsVaryingDeclaration extends PsiElement {

  @NotNull
  List<GdsArraySize> getArraySizeList();

  @Nullable
  GdsInterpolationQualifier getInterpolationQualifier();

  @Nullable
  GdsPrecision getPrecision();

  @Nullable
  GdsType getType();

  @Nullable
  GdsVariableNameDecl getVariableNameDecl();

}

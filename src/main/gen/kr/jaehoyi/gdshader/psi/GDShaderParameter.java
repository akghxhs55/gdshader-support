// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderParameter extends PsiElement {

  @NotNull
  List<GDShaderExpression> getExpressionList();

  @Nullable
  GDShaderParameterName getParameterName();

  @Nullable
  GDShaderParameterQualifier getParameterQualifier();

  @Nullable
  GDShaderPrecision getPrecision();

  @NotNull
  GDShaderType getType();

}

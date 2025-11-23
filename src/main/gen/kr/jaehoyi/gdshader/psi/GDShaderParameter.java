// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderParameter extends PsiElement {

  @Nullable
  GDShaderArraySize getArraySize();

  @Nullable
  GDShaderParameterQualifier getParameterQualifier();

  @Nullable
  GDShaderPrecision getPrecision();

  @NotNull
  GDShaderType getType();

  @NotNull
  GDShaderVariableNameDecl getVariableNameDecl();

}

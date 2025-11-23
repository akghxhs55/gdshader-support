// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderUniformDeclaration extends PsiElement {

  @NotNull
  List<GDShaderArraySize> getArraySizeList();

  @Nullable
  GDShaderExpression getExpression();

  @Nullable
  GDShaderHintSection getHintSection();

  @Nullable
  GDShaderPrecision getPrecision();

  @Nullable
  GDShaderType getType();

  @NotNull
  GDShaderUniformHeader getUniformHeader();

  @Nullable
  GDShaderVariableNameDecl getVariableNameDecl();

}

// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderPrimary extends PsiElement {

  @Nullable
  GDShaderConstructorCall getConstructorCall();

  @Nullable
  GDShaderExpression getExpression();

  @Nullable
  GDShaderFunctionCall getFunctionCall();

  @Nullable
  GDShaderLiteral getLiteral();

  @Nullable
  GDShaderVariableName getVariableName();

  boolean isLiteral();

  boolean isFunctionCall();

  boolean isExpression();

  boolean isVariableName();

}

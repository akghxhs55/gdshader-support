// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderStatement extends PsiElement {

  @Nullable
  GDShaderControlStatement getControlStatement();

  @Nullable
  GDShaderExpressionStatement getExpressionStatement();

  @Nullable
  GDShaderFunctionDeclaration getFunctionDeclaration();

  @Nullable
  GDShaderRenderModeDeclaration getRenderModeDeclaration();

  @Nullable
  GDShaderReturnStatement getReturnStatement();

  @Nullable
  GDShaderShaderTypeDeclaration getShaderTypeDeclaration();

  @Nullable
  GDShaderSimpleStatement getSimpleStatement();

  @Nullable
  GDShaderStencilModeDeclaration getStencilModeDeclaration();

  @Nullable
  GDShaderStructDeclaration getStructDeclaration();

  @Nullable
  GDShaderUniformGroupDeclaration getUniformGroupDeclaration();

  @Nullable
  GDShaderVariableDeclaration getVariableDeclaration();

}

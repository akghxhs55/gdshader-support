// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdshaderStatement extends PsiElement {

  @Nullable
  GdshaderControlStatement getControlStatement();

  @Nullable
  GdshaderExpressionStatement getExpressionStatement();

  @Nullable
  GdshaderFunctionDeclaration getFunctionDeclaration();

  @Nullable
  GdshaderRenderModeDeclaration getRenderModeDeclaration();

  @Nullable
  GdshaderReturnStatement getReturnStatement();

  @Nullable
  GdshaderShaderTypeDeclaration getShaderTypeDeclaration();

  @Nullable
  GdshaderSimpleStatement getSimpleStatement();

  @Nullable
  GdshaderStencilModeDeclaration getStencilModeDeclaration();

  @Nullable
  GdshaderStructDeclaration getStructDeclaration();

  @Nullable
  GdshaderUniformGroupDeclaration getUniformGroupDeclaration();

  @Nullable
  GdshaderVariableDeclaration getVariableDeclaration();

}

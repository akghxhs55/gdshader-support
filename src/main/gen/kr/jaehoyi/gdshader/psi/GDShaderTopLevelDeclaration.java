// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderTopLevelDeclaration extends PsiElement {

  @Nullable
  GDShaderConstantDeclaration getConstantDeclaration();

  @Nullable
  GDShaderFunctionDeclaration getFunctionDeclaration();

  @Nullable
  GDShaderRenderModeDeclaration getRenderModeDeclaration();

  @Nullable
  GDShaderShaderTypeDeclaration getShaderTypeDeclaration();

  @Nullable
  GDShaderStencilModeDeclaration getStencilModeDeclaration();

  @Nullable
  GDShaderStructDeclaration getStructDeclaration();

  @Nullable
  GDShaderUniformGroupDeclaration getUniformGroupDeclaration();

  @Nullable
  GDShaderUniformVariableDeclaration getUniformVariableDeclaration();

  @Nullable
  GDShaderVaryingVariableDeclaration getVaryingVariableDeclaration();

}

// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsTopLevelDeclaration extends PsiElement {

  @Nullable
  GdsConstantDeclaration getConstantDeclaration();

  @Nullable
  GdsFunctionDeclaration getFunctionDeclaration();

  @Nullable
  GdsRenderModeDeclaration getRenderModeDeclaration();

  @Nullable
  GdsShaderTypeDeclaration getShaderTypeDeclaration();

  @Nullable
  GdsStencilModeDeclaration getStencilModeDeclaration();

  @Nullable
  GdsStructDeclaration getStructDeclaration();

  @Nullable
  GdsUniformDeclaration getUniformDeclaration();

  @Nullable
  GdsUniformGroupDeclaration getUniformGroupDeclaration();

  @Nullable
  GdsVaryingDeclaration getVaryingDeclaration();

}

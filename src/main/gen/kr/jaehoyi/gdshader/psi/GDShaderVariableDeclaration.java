// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderVariableDeclaration extends PsiElement {

  @Nullable
  GDShaderConstVariableDeclaration getConstVariableDeclaration();

  @Nullable
  GDShaderRegularVariableDeclaration getRegularVariableDeclaration();

  @Nullable
  GDShaderUniformVariableDeclaration getUniformVariableDeclaration();

  @Nullable
  GDShaderVaryingVariableDeclaration getVaryingVariableDeclaration();

}

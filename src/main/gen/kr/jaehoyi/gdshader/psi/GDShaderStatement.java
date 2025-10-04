// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderStatement extends PsiElement {

  @Nullable
  GDShaderConstantDeclaration getConstantDeclaration();

  @Nullable
  GDShaderControlStatement getControlStatement();

  @Nullable
  GDShaderExpressionStatement getExpressionStatement();

  @Nullable
  GDShaderLocalVariableDeclaration getLocalVariableDeclaration();

  @Nullable
  GDShaderReturnStatement getReturnStatement();

  @Nullable
  GDShaderSimpleStatement getSimpleStatement();

}

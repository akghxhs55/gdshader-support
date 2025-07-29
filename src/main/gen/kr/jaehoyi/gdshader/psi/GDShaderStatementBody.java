// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderStatementBody extends PsiElement {

  @Nullable
  GDShaderBlock getBlock();

  @Nullable
  GDShaderControlStatement getControlStatement();

  @Nullable
  GDShaderExpressionStatement getExpressionStatement();

  @Nullable
  GDShaderLocalVariableDeclaration getLocalVariableDeclaration();

  @Nullable
  GDShaderPreprocessorDirective getPreprocessorDirective();

  @Nullable
  GDShaderReturnStatement getReturnStatement();

  @Nullable
  GDShaderSimpleStatement getSimpleStatement();

}

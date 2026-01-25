// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsStatement extends PsiElement {

  @Nullable
  GdsConstantDeclaration getConstantDeclaration();

  @Nullable
  GdsControlStatement getControlStatement();

  @Nullable
  GdsExpressionStatement getExpressionStatement();

  @Nullable
  GdsFunctionCallStatement getFunctionCallStatement();

  @Nullable
  GdsLocalVariableDeclaration getLocalVariableDeclaration();

  @Nullable
  GdsReturnStatement getReturnStatement();

  @Nullable
  GdsSimpleStatement getSimpleStatement();

}

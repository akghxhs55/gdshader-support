// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static kr.jaehoyi.gdshader.psi.GDShaderTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import kr.jaehoyi.gdshader.psi.*;

public class GdshaderStatementImpl extends ASTWrapperPsiElement implements GdshaderStatement {

  public GdshaderStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdshaderVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdshaderVisitor) accept((GdshaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdshaderControlStatement getControlStatement() {
    return findChildByClass(GdshaderControlStatement.class);
  }

  @Override
  @Nullable
  public GdshaderExpressionStatement getExpressionStatement() {
    return findChildByClass(GdshaderExpressionStatement.class);
  }

  @Override
  @Nullable
  public GdshaderFunctionDeclaration getFunctionDeclaration() {
    return findChildByClass(GdshaderFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public GdshaderRenderModeDeclaration getRenderModeDeclaration() {
    return findChildByClass(GdshaderRenderModeDeclaration.class);
  }

  @Override
  @Nullable
  public GdshaderReturnStatement getReturnStatement() {
    return findChildByClass(GdshaderReturnStatement.class);
  }

  @Override
  @Nullable
  public GdshaderShaderTypeDeclaration getShaderTypeDeclaration() {
    return findChildByClass(GdshaderShaderTypeDeclaration.class);
  }

  @Override
  @Nullable
  public GdshaderSimpleStatement getSimpleStatement() {
    return findChildByClass(GdshaderSimpleStatement.class);
  }

  @Override
  @Nullable
  public GdshaderStencilModeDeclaration getStencilModeDeclaration() {
    return findChildByClass(GdshaderStencilModeDeclaration.class);
  }

  @Override
  @Nullable
  public GdshaderStructDeclaration getStructDeclaration() {
    return findChildByClass(GdshaderStructDeclaration.class);
  }

  @Override
  @Nullable
  public GdshaderUniformGroupDeclaration getUniformGroupDeclaration() {
    return findChildByClass(GdshaderUniformGroupDeclaration.class);
  }

  @Override
  @Nullable
  public GdshaderVariableDeclaration getVariableDeclaration() {
    return findChildByClass(GdshaderVariableDeclaration.class);
  }

}

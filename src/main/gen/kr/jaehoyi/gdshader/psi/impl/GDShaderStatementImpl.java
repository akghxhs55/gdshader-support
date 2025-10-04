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

public class GDShaderStatementImpl extends ASTWrapperPsiElement implements GDShaderStatement {

  public GDShaderStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GDShaderConstantDeclaration getConstantDeclaration() {
    return findChildByClass(GDShaderConstantDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderControlStatement getControlStatement() {
    return findChildByClass(GDShaderControlStatement.class);
  }

  @Override
  @Nullable
  public GDShaderExpressionStatement getExpressionStatement() {
    return findChildByClass(GDShaderExpressionStatement.class);
  }

  @Override
  @Nullable
  public GDShaderLocalVariableDeclaration getLocalVariableDeclaration() {
    return findChildByClass(GDShaderLocalVariableDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderReturnStatement getReturnStatement() {
    return findChildByClass(GDShaderReturnStatement.class);
  }

  @Override
  @Nullable
  public GDShaderSimpleStatement getSimpleStatement() {
    return findChildByClass(GDShaderSimpleStatement.class);
  }

}

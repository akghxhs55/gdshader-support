// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static kr.jaehoyi.gdshader.psi.GdsTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import kr.jaehoyi.gdshader.psi.*;

public class GdsStatementImpl extends ASTWrapperPsiElement implements GdsStatement {

  public GdsStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsConstantDeclaration getConstantDeclaration() {
    return findChildByClass(GdsConstantDeclaration.class);
  }

  @Override
  @Nullable
  public GdsControlStatement getControlStatement() {
    return findChildByClass(GdsControlStatement.class);
  }

  @Override
  @Nullable
  public GdsExpressionStatement getExpressionStatement() {
    return findChildByClass(GdsExpressionStatement.class);
  }

  @Override
  @Nullable
  public GdsFunctionCallStatement getFunctionCallStatement() {
    return findChildByClass(GdsFunctionCallStatement.class);
  }

  @Override
  @Nullable
  public GdsLocalVariableDeclaration getLocalVariableDeclaration() {
    return findChildByClass(GdsLocalVariableDeclaration.class);
  }

  @Override
  @Nullable
  public GdsReturnStatement getReturnStatement() {
    return findChildByClass(GdsReturnStatement.class);
  }

  @Override
  @Nullable
  public GdsSimpleStatement getSimpleStatement() {
    return findChildByClass(GdsSimpleStatement.class);
  }

}

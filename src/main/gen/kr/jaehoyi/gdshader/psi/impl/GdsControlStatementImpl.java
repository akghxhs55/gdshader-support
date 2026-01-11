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

public class GdsControlStatementImpl extends ASTWrapperPsiElement implements GdsControlStatement {

  public GdsControlStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitControlStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsDoWhileStatement getDoWhileStatement() {
    return findChildByClass(GdsDoWhileStatement.class);
  }

  @Override
  @Nullable
  public GdsForStatement getForStatement() {
    return findChildByClass(GdsForStatement.class);
  }

  @Override
  @Nullable
  public GdsIfStatement getIfStatement() {
    return findChildByClass(GdsIfStatement.class);
  }

  @Override
  @Nullable
  public GdsSwitchStatement getSwitchStatement() {
    return findChildByClass(GdsSwitchStatement.class);
  }

  @Override
  @Nullable
  public GdsWhileStatement getWhileStatement() {
    return findChildByClass(GdsWhileStatement.class);
  }

}

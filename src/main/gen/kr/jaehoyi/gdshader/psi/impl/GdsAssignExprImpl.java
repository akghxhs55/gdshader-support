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

public class GdsAssignExprImpl extends ASTWrapperPsiElement implements GdsAssignExpr {

  public GdsAssignExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitAssignExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsAssignExpr getAssignExpr() {
    return findChildByClass(GdsAssignExpr.class);
  }

  @Override
  @Nullable
  public GdsAssignmentOperator getAssignmentOperator() {
    return findChildByClass(GdsAssignmentOperator.class);
  }

  @Override
  @NotNull
  public GdsLogicOrExpr getLogicOrExpr() {
    return findNotNullChildByClass(GdsLogicOrExpr.class);
  }

}

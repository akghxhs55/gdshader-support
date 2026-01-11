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

public class GdsPostfixExprImpl extends ASTWrapperPsiElement implements GdsPostfixExpr {

  public GdsPostfixExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitPostfixExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdsExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdsExpression.class);
  }

  @Override
  @NotNull
  public List<GdsFunctionCall> getFunctionCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdsFunctionCall.class);
  }

  @Override
  @NotNull
  public GdsPrimary getPrimary() {
    return findNotNullChildByClass(GdsPrimary.class);
  }

  @Override
  @NotNull
  public List<GdsStructMemberNameRef> getStructMemberNameRefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdsStructMemberNameRef.class);
  }

}

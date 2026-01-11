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

public class GdsPrimaryImpl extends ASTWrapperPsiElement implements GdsPrimary {

  public GdsPrimaryImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitPrimary(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsExpression getExpression() {
    return findChildByClass(GdsExpression.class);
  }

  @Override
  @Nullable
  public GdsFunctionCall getFunctionCall() {
    return findChildByClass(GdsFunctionCall.class);
  }

  @Override
  @Nullable
  public GdsLiteral getLiteral() {
    return findChildByClass(GdsLiteral.class);
  }

  @Override
  @Nullable
  public GdsVariableNameRef getVariableNameRef() {
    return findChildByClass(GdsVariableNameRef.class);
  }

}

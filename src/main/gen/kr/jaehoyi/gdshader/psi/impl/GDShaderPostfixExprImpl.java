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

public class GDShaderPostfixExprImpl extends ASTWrapperPsiElement implements GDShaderPostfixExpr {

  public GDShaderPostfixExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitPostfixExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GDShaderArgumentList> getArgumentListList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GDShaderArgumentList.class);
  }

  @Override
  @NotNull
  public List<GDShaderExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GDShaderExpression.class);
  }

  @Override
  @NotNull
  public List<GDShaderFunctionName> getFunctionNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GDShaderFunctionName.class);
  }

  @Override
  @NotNull
  public GDShaderPrimary getPrimary() {
    return findNotNullChildByClass(GDShaderPrimary.class);
  }

  @Override
  @NotNull
  public List<GDShaderStructMemberName> getStructMemberNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GDShaderStructMemberName.class);
  }

}

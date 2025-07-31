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

public class GDShaderPrimaryImpl extends ASTWrapperPsiElement implements GDShaderPrimary {

  public GDShaderPrimaryImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitPrimary(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GDShaderExpression getExpression() {
    return findChildByClass(GDShaderExpression.class);
  }

  @Override
  @Nullable
  public GDShaderFunctionCall getFunctionCall() {
    return findChildByClass(GDShaderFunctionCall.class);
  }

  @Override
  @Nullable
  public GDShaderLiteral getLiteral() {
    return findChildByClass(GDShaderLiteral.class);
  }

  @Override
  @Nullable
  public GDShaderVariableName getVariableName() {
    return findChildByClass(GDShaderVariableName.class);
  }

}

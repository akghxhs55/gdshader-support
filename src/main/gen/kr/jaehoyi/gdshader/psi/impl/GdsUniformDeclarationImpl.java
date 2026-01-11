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

public class GdsUniformDeclarationImpl extends ASTWrapperPsiElement implements GdsUniformDeclaration {

  public GdsUniformDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitUniformDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdsArraySize> getArraySizeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdsArraySize.class);
  }

  @Override
  @Nullable
  public GdsExpression getExpression() {
    return findChildByClass(GdsExpression.class);
  }

  @Override
  @Nullable
  public GdsHintSection getHintSection() {
    return findChildByClass(GdsHintSection.class);
  }

  @Override
  @Nullable
  public GdsPrecision getPrecision() {
    return findChildByClass(GdsPrecision.class);
  }

  @Override
  @Nullable
  public GdsType getType() {
    return findChildByClass(GdsType.class);
  }

  @Override
  @NotNull
  public GdsUniformHeader getUniformHeader() {
    return findNotNullChildByClass(GdsUniformHeader.class);
  }

  @Override
  @Nullable
  public GdsVariableNameDecl getVariableNameDecl() {
    return findChildByClass(GdsVariableNameDecl.class);
  }

}

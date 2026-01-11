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

public class GdsParameterImpl extends ASTWrapperPsiElement implements GdsParameter {

  public GdsParameterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitParameter(this);
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
  public GdsParameterQualifier getParameterQualifier() {
    return findChildByClass(GdsParameterQualifier.class);
  }

  @Override
  @Nullable
  public GdsPrecision getPrecision() {
    return findChildByClass(GdsPrecision.class);
  }

  @Override
  @NotNull
  public GdsType getType() {
    return findNotNullChildByClass(GdsType.class);
  }

  @Override
  @NotNull
  public GdsVariableNameDecl getVariableNameDecl() {
    return findNotNullChildByClass(GdsVariableNameDecl.class);
  }

}

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

public class GdsFunctionNameDeclImpl extends ASTWrapperPsiElement implements GdsFunctionNameDecl {

  public GdsFunctionNameDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitFunctionNameDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public @NotNull String getName() {
    return GdsPsiImplUtil.getName(this);
  }

  @Override
  public @Nullable PsiElement setName(@NotNull String newName) {
    return GdsPsiImplUtil.setName(this, newName);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return GdsPsiImplUtil.getNameIdentifier(this);
  }

}

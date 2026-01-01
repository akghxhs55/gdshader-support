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
import com.intellij.psi.PsiReference;

public class GDShaderVariableNameRefImpl extends ASTWrapperPsiElement implements GDShaderVariableNameRef {

  public GDShaderVariableNameRefImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitVariableNameRef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public @NotNull PsiReference getReference() {
    return GDShaderPsiImplUtil.getReference(this);
  }

}

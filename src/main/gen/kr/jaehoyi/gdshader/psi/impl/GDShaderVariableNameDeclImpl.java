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

public class GDShaderVariableNameDeclImpl extends ASTWrapperPsiElement implements GDShaderVariableNameDecl {

  public GDShaderVariableNameDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitVariableNameDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public @NotNull String getName() {
    return GDShaderPsiImplUtil.getName(this);
  }

  @Override
  public @Nullable PsiElement setName(@NotNull String newName) {
    return GDShaderPsiImplUtil.setName(this, newName);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return GDShaderPsiImplUtil.getNameIdentifier(this);
  }

}

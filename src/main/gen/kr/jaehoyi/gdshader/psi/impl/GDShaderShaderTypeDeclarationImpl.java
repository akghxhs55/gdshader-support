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

public class GDShaderShaderTypeDeclarationImpl extends ASTWrapperPsiElement implements GDShaderShaderTypeDeclaration {

  public GDShaderShaderTypeDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitShaderTypeDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public GDShaderShaderTypeName getShaderTypeName() {
    return findNotNullChildByClass(GDShaderShaderTypeName.class);
  }

  @Override
  public @NotNull String getShaderType() {
    return GDShaderPsiImplUtil.getShaderType(this);
  }

}

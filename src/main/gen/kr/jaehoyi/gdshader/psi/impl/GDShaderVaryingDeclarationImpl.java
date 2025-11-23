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

public class GDShaderVaryingDeclarationImpl extends ASTWrapperPsiElement implements GDShaderVaryingDeclaration {

  public GDShaderVaryingDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitVaryingDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GDShaderArraySize> getArraySizeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GDShaderArraySize.class);
  }

  @Override
  @Nullable
  public GDShaderPrecision getPrecision() {
    return findChildByClass(GDShaderPrecision.class);
  }

  @Override
  @Nullable
  public GDShaderType getType() {
    return findChildByClass(GDShaderType.class);
  }

  @Override
  @Nullable
  public GDShaderVariableNameDecl getVariableNameDecl() {
    return findChildByClass(GDShaderVariableNameDecl.class);
  }

}

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

public class GDShaderVaryingVariableDeclarationImpl extends ASTWrapperPsiElement implements GDShaderVaryingVariableDeclaration {

  public GDShaderVaryingVariableDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitVaryingVariableDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GDShaderArraySize getArraySize() {
    return findChildByClass(GDShaderArraySize.class);
  }

  @Override
  @Nullable
  public GDShaderHints getHints() {
    return findChildByClass(GDShaderHints.class);
  }

  @Override
  @Nullable
  public GDShaderPrecision getPrecision() {
    return findChildByClass(GDShaderPrecision.class);
  }

  @Override
  @NotNull
  public GDShaderType getType() {
    return findNotNullChildByClass(GDShaderType.class);
  }

}

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

public class GDShaderHintImpl extends ASTWrapperPsiElement implements GDShaderHint {

  public GDShaderHintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitHint(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GDShaderEnumHint getEnumHint() {
    return findChildByClass(GDShaderEnumHint.class);
  }

  @Override
  @Nullable
  public GDShaderInstanceIndexHint getInstanceIndexHint() {
    return findChildByClass(GDShaderInstanceIndexHint.class);
  }

  @Override
  @Nullable
  public GDShaderRangeHint getRangeHint() {
    return findChildByClass(GDShaderRangeHint.class);
  }

  @Override
  @Nullable
  public GDShaderSimpleHint getSimpleHint() {
    return findChildByClass(GDShaderSimpleHint.class);
  }

}

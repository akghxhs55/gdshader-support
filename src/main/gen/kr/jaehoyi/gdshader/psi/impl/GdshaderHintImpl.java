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

public class GdshaderHintImpl extends ASTWrapperPsiElement implements GdshaderHint {

  public GdshaderHintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdshaderVisitor visitor) {
    visitor.visitHint(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdshaderVisitor) accept((GdshaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdshaderEnumHint getEnumHint() {
    return findChildByClass(GdshaderEnumHint.class);
  }

  @Override
  @Nullable
  public GdshaderRangeHint getRangeHint() {
    return findChildByClass(GdshaderRangeHint.class);
  }

  @Override
  @Nullable
  public GdshaderSimpleHint getSimpleHint() {
    return findChildByClass(GdshaderSimpleHint.class);
  }

}

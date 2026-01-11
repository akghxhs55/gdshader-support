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

public class GdsHintImpl extends ASTWrapperPsiElement implements GdsHint {

  public GdsHintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitHint(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsEnumHint getEnumHint() {
    return findChildByClass(GdsEnumHint.class);
  }

  @Override
  @Nullable
  public GdsInstanceIndexHint getInstanceIndexHint() {
    return findChildByClass(GdsInstanceIndexHint.class);
  }

  @Override
  @Nullable
  public GdsRangeHint getRangeHint() {
    return findChildByClass(GdsRangeHint.class);
  }

  @Override
  @Nullable
  public GdsSimpleHint getSimpleHint() {
    return findChildByClass(GdsSimpleHint.class);
  }

}

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

public class GdsLocalVariableDeclaratorImpl extends ASTWrapperPsiElement implements GdsLocalVariableDeclarator {

  public GdsLocalVariableDeclaratorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitLocalVariableDeclarator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsArraySize getArraySize() {
    return findChildByClass(GdsArraySize.class);
  }

  @Override
  @Nullable
  public GdsInitializer getInitializer() {
    return findChildByClass(GdsInitializer.class);
  }

  @Override
  @NotNull
  public GdsVariableNameDecl getVariableNameDecl() {
    return findNotNullChildByClass(GdsVariableNameDecl.class);
  }

}

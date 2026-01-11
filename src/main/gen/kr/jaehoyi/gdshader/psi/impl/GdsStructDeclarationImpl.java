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

public class GdsStructDeclarationImpl extends ASTWrapperPsiElement implements GdsStructDeclaration {

  public GdsStructDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitStructDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsStructBlock getStructBlock() {
    return findChildByClass(GdsStructBlock.class);
  }

  @Override
  @Nullable
  public GdsStructNameDecl getStructNameDecl() {
    return findChildByClass(GdsStructNameDecl.class);
  }

}

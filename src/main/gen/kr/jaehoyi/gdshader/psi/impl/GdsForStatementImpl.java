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

public class GdsForStatementImpl extends ASTWrapperPsiElement implements GdsForStatement {

  public GdsForStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitForStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsForCondition getForCondition() {
    return findChildByClass(GdsForCondition.class);
  }

  @Override
  @Nullable
  public GdsForInit getForInit() {
    return findChildByClass(GdsForInit.class);
  }

  @Override
  @Nullable
  public GdsForIteration getForIteration() {
    return findChildByClass(GdsForIteration.class);
  }

  @Override
  @Nullable
  public GdsStatementBody getStatementBody() {
    return findChildByClass(GdsStatementBody.class);
  }

}

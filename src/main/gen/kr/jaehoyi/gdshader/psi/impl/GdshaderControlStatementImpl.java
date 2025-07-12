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

public class GdshaderControlStatementImpl extends ASTWrapperPsiElement implements GdshaderControlStatement {

  public GdshaderControlStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdshaderVisitor visitor) {
    visitor.visitControlStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdshaderVisitor) accept((GdshaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdshaderDoWhileStatement getDoWhileStatement() {
    return findChildByClass(GdshaderDoWhileStatement.class);
  }

  @Override
  @Nullable
  public GdshaderForStatement getForStatement() {
    return findChildByClass(GdshaderForStatement.class);
  }

  @Override
  @Nullable
  public GdshaderIfStatement getIfStatement() {
    return findChildByClass(GdshaderIfStatement.class);
  }

  @Override
  @Nullable
  public GdshaderSwitchStatement getSwitchStatement() {
    return findChildByClass(GdshaderSwitchStatement.class);
  }

  @Override
  @Nullable
  public GdshaderWhileStatement getWhileStatement() {
    return findChildByClass(GdshaderWhileStatement.class);
  }

}

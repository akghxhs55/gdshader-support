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

public class GDShaderControlStatementImpl extends ASTWrapperPsiElement implements GDShaderControlStatement {

  public GDShaderControlStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitControlStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GDShaderDoWhileStatement getDoWhileStatement() {
    return findChildByClass(GDShaderDoWhileStatement.class);
  }

  @Override
  @Nullable
  public GDShaderForStatement getForStatement() {
    return findChildByClass(GDShaderForStatement.class);
  }

  @Override
  @Nullable
  public GDShaderIfStatement getIfStatement() {
    return findChildByClass(GDShaderIfStatement.class);
  }

  @Override
  @Nullable
  public GDShaderSwitchStatement getSwitchStatement() {
    return findChildByClass(GDShaderSwitchStatement.class);
  }

  @Override
  @Nullable
  public GDShaderWhileStatement getWhileStatement() {
    return findChildByClass(GDShaderWhileStatement.class);
  }

}

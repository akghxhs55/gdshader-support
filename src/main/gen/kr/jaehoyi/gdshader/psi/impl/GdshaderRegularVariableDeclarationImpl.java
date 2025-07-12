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

public class GdshaderRegularVariableDeclarationImpl extends ASTWrapperPsiElement implements GdshaderRegularVariableDeclaration {

  public GdshaderRegularVariableDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdshaderVisitor visitor) {
    visitor.visitRegularVariableDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdshaderVisitor) accept((GdshaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdshaderArraySize getArraySize() {
    return findChildByClass(GdshaderArraySize.class);
  }

  @Override
  @Nullable
  public GdshaderInitializer getInitializer() {
    return findChildByClass(GdshaderInitializer.class);
  }

  @Override
  @Nullable
  public GdshaderPrecision getPrecision() {
    return findChildByClass(GdshaderPrecision.class);
  }

  @Override
  @NotNull
  public GdshaderType getType() {
    return findNotNullChildByClass(GdshaderType.class);
  }

}

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

public class GdsTopLevelDeclarationImpl extends ASTWrapperPsiElement implements GdsTopLevelDeclaration {

  public GdsTopLevelDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdsVisitor visitor) {
    visitor.visitTopLevelDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdsVisitor) accept((GdsVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdsConstantDeclaration getConstantDeclaration() {
    return findChildByClass(GdsConstantDeclaration.class);
  }

  @Override
  @Nullable
  public GdsFunctionDeclaration getFunctionDeclaration() {
    return findChildByClass(GdsFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public GdsRenderModeDeclaration getRenderModeDeclaration() {
    return findChildByClass(GdsRenderModeDeclaration.class);
  }

  @Override
  @Nullable
  public GdsShaderTypeDeclaration getShaderTypeDeclaration() {
    return findChildByClass(GdsShaderTypeDeclaration.class);
  }

  @Override
  @Nullable
  public GdsStencilModeDeclaration getStencilModeDeclaration() {
    return findChildByClass(GdsStencilModeDeclaration.class);
  }

  @Override
  @Nullable
  public GdsStructDeclaration getStructDeclaration() {
    return findChildByClass(GdsStructDeclaration.class);
  }

  @Override
  @Nullable
  public GdsUniformDeclaration getUniformDeclaration() {
    return findChildByClass(GdsUniformDeclaration.class);
  }

  @Override
  @Nullable
  public GdsUniformGroupDeclaration getUniformGroupDeclaration() {
    return findChildByClass(GdsUniformGroupDeclaration.class);
  }

  @Override
  @Nullable
  public GdsVaryingDeclaration getVaryingDeclaration() {
    return findChildByClass(GdsVaryingDeclaration.class);
  }

}

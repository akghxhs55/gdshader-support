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

public class GDShaderTopLevelDeclarationImpl extends ASTWrapperPsiElement implements GDShaderTopLevelDeclaration {

  public GDShaderTopLevelDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GDShaderVisitor visitor) {
    visitor.visitTopLevelDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GDShaderVisitor) accept((GDShaderVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GDShaderConstantDeclaration getConstantDeclaration() {
    return findChildByClass(GDShaderConstantDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderFunctionDeclaration getFunctionDeclaration() {
    return findChildByClass(GDShaderFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderRenderModeDeclaration getRenderModeDeclaration() {
    return findChildByClass(GDShaderRenderModeDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderShaderTypeDeclaration getShaderTypeDeclaration() {
    return findChildByClass(GDShaderShaderTypeDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderStencilModeDeclaration getStencilModeDeclaration() {
    return findChildByClass(GDShaderStencilModeDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderStructDeclaration getStructDeclaration() {
    return findChildByClass(GDShaderStructDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderUniformGroupDeclaration getUniformGroupDeclaration() {
    return findChildByClass(GDShaderUniformGroupDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderUniformVariableDeclaration getUniformVariableDeclaration() {
    return findChildByClass(GDShaderUniformVariableDeclaration.class);
  }

  @Override
  @Nullable
  public GDShaderVaryingVariableDeclaration getVaryingVariableDeclaration() {
    return findChildByClass(GDShaderVaryingVariableDeclaration.class);
  }

}

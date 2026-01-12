// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import kr.jaehoyi.gdshader.model.FunctionSpec;

public interface GdsFunctionNameDecl extends PsiNameIdentifierOwner,  GdsFunction {

  @NotNull String getName();

  @Nullable PsiElement setName(@NotNull String newName);

  @Nullable PsiElement getNameIdentifier();

  @Nullable FunctionSpec getFunctionSpec();

}

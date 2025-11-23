// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GDShaderStructMember extends PsiElement {

  @NotNull
  List<GDShaderArraySize> getArraySizeList();

  @Nullable
  GDShaderPrecision getPrecision();

  @Nullable
  GDShaderStructMemberNameDecl getStructMemberNameDecl();

  @NotNull
  GDShaderType getType();

}

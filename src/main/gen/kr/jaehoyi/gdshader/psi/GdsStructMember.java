// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsStructMember extends PsiElement {

  @NotNull
  List<GdsArraySize> getArraySizeList();

  @Nullable
  GdsPrecision getPrecision();

  @Nullable
  GdsStructMemberNameDecl getStructMemberNameDecl();

  @NotNull
  GdsType getType();

}

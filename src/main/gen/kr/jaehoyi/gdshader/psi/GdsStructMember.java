// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdsStructMember extends PsiElement {

  @Nullable
  GdsArraySize getArraySize();

  @Nullable
  GdsPrecision getPrecision();

  @NotNull
  List<GdsStructMemberDeclarator> getStructMemberDeclaratorList();

  @NotNull
  GdsType getType();

}

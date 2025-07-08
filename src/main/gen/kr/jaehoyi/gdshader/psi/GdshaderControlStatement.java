// This is a generated file. Not intended for manual editing.
package kr.jaehoyi.gdshader.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdshaderControlStatement extends PsiElement {

  @Nullable
  GdshaderForStatement getForStatement();

  @Nullable
  GdshaderIfStatement getIfStatement();

  @Nullable
  GdshaderSwitchStatement getSwitchStatement();

  @Nullable
  GdshaderWhileStatement getWhileStatement();

}

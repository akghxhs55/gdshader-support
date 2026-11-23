package kr.jaehoyi.gdshader.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.lang.Language
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.search.SearchScope
import com.intellij.util.IncorrectOperationException
import kr.jaehoyi.gdshader.GdsLanguage
import kr.jaehoyi.gdshader.resolve.GdsPreprocessorDefinitions.GdsMacro
import javax.swing.Icon

// A #define is a single comment-like leaf, so this stands in for the macro name inside it.
class GdsMacroElement(
    val macro: GdsMacro,
) : FakePsiElement() {
    private val leaf: PsiElement get() = macro.element

    override fun getParent(): PsiElement = leaf

    override fun getContainingFile(): PsiFile = leaf.containingFile

    override fun getLanguage(): Language = GdsLanguage

    override fun getName(): String = macro.name

    override fun getText(): String = macro.name

    override fun getTextOffset(): Int = leaf.textRange.startOffset + macro.nameOffset

    override fun getTextRange(): TextRange = TextRange.from(textOffset, macro.name.length)

    override fun getTextLength(): Int = macro.name.length

    override fun getNavigationElement(): PsiElement = this

    override fun canNavigate(): Boolean = true

    override fun canNavigateToSource(): Boolean = true

    override fun isValid(): Boolean = leaf.isValid

    override fun isPhysical(): Boolean = leaf.isPhysical

    override fun isWritable(): Boolean = leaf.isWritable

    override fun getUseScope(): SearchScope = leaf.useScope

    override fun getIcon(open: Boolean): Icon = AllIcons.Nodes.Constant

    override fun getLocationString(): String? = containingFile?.name

    override fun setName(name: String): PsiElement {
        val node = leaf.node as? LeafElement ?: throw IncorrectOperationException("Cannot rename macro '${macro.name}'")
        val text = node.text
        val start = macro.nameOffset
        val newText = text.substring(0, start) + name + text.substring(start + macro.name.length)
        val newLeaf = node.replaceWithText(newText).psi
        return GdsMacroElement(macro.copy(name = name, element = newLeaf))
    }

    override fun isEquivalentTo(another: PsiElement?): Boolean = another == this

    override fun equals(other: Any?): Boolean = other is GdsMacroElement && other.macro == macro

    override fun hashCode(): Int = macro.hashCode()

    override fun toString(): String = "GDShaderMacro(${macro.name})"
}

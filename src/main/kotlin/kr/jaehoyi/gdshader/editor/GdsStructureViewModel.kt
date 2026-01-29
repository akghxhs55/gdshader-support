package kr.jaehoyi.gdshader.editor

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import kr.jaehoyi.gdshader.psi.*

class GdsStructureViewModel(editor: Editor?, psiFile: PsiFile) :
    StructureViewModelBase(psiFile, editor, GdsStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)

    override fun getSuitableClasses(): Array<Class<*>> = arrayOf(
        GdsShaderTypeDeclaration::class.java,
        GdsRenderModeDeclaration::class.java,
        GdsStencilModeDeclaration::class.java,
        GdsUniformGroupDeclaration::class.java,
        GdsUniformDeclaration::class.java,
        GdsConstantDeclarator::class.java,
        GdsVaryingDeclaration::class.java,
        GdsFunctionDeclaration::class.java,
        GdsStructDeclaration::class.java,
        GdsStructMember::class.java,
        GdsParameter::class.java,
    )

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        val value = element.value
        return value !is PsiFile
            && value !is GdsFunctionDeclaration
            && value !is GdsStructDeclaration
    }
}

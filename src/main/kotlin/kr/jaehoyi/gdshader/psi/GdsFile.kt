package kr.jaehoyi.gdshader.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.GdsFileType
import kr.jaehoyi.gdshader.GdsLanguage
import kr.jaehoyi.gdshader.resolve.GdsIncludeManager

class GdsFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, GdsLanguage) {
    
    override fun getFileType(): FileType = GdsFileType
    
    override fun toString(): String = "GDShader File"

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        val children = PsiTreeUtil.getChildrenOfType(this, GdsItem::class.java)
        if (children != null) {
            for (child in children) {
                if (!processor.execute(child, state)) {
                    return false
                }
            }
        }
        
        return GdsIncludeManager.processIncludedDeclarations(this, processor, state, lastParent, place)
    }
    
}
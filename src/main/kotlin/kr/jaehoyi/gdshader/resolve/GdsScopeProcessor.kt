package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import kr.jaehoyi.gdshader.psi.GdsItem

class GdsScopeProcessor<T : PsiElement>(
    private val targetType: Class<T>,
    private val processor: (element: T) -> Boolean
) : PsiScopeProcessor {

    override fun execute(element: PsiElement, state: ResolveState): Boolean {
        val targetElement = when (element) {
            is GdsItem -> extractDeclaration(element)
            else -> element
        }
        
        if (targetType.isInstance(targetElement)) {
            @Suppress("UNCHECKED_CAST")
            return processor(targetElement as T)
        }
        
        return true
    }
    
    private fun extractDeclaration(item: GdsItem): PsiElement? =
        item.topLevelDeclaration.let { top ->
            top.uniformDeclaration?.variableNameDecl
            ?: top.constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.firstOrNull()?.variableNameDecl
            ?: top.varyingDeclaration?.variableNameDecl
            ?: top.functionDeclaration?.functionNameDecl
            ?: top.structDeclaration?.structNameDecl
        }

    override fun <T> getHint(hintKey: Key<T?>): T? = null
    override fun handleEvent(event: PsiScopeProcessor.Event, associated: Any?) {}
    
}
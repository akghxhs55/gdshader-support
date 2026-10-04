package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import kr.jaehoyi.gdshader.psi.GdsItem

class GdsScopeProcessor<T : PsiElement>(
    private val targetType: Class<T>,
    private val startOffset: Int,
    private val originFile: PsiFile?,
    private val processor: (element: T) -> Boolean,
) : PsiScopeProcessor {
    override fun execute(
        element: PsiElement,
        state: ResolveState,
    ): Boolean {
        val declarations =
            when (element) {
                is GdsItem -> extractDeclarations(element)
                else -> listOf(element)
            }

        for (declaration in declarations) {
            val isSameFile = declaration.containingFile?.originalFile == originFile
            if (isSameFile && declaration.textOffset >= startOffset) continue

            if (targetType.isInstance(declaration) && !processor(targetType.cast(declaration))) {
                return false
            }
        }

        return true
    }

    private fun extractDeclarations(item: GdsItem): List<PsiElement> {
        val top = item.topLevelDeclaration
        val constants = top.constantDeclaration
        if (constants != null) {
            return constants.constantDeclaratorList
                ?.constantDeclaratorList
                .orEmpty()
                .map { it.variableNameDecl }
        }
        return listOfNotNull(
            top.uniformDeclaration?.variableNameDecl
                ?: top.varyingDeclaration?.variableNameDecl
                ?: top.functionDeclaration?.functionNameDecl
                ?: top.structDeclaration?.structNameDecl,
        )
    }

    override fun <T> getHint(hintKey: Key<T?>): T? = null

    override fun handleEvent(
        event: PsiScopeProcessor.Event,
        associated: Any?,
    ) {}
}

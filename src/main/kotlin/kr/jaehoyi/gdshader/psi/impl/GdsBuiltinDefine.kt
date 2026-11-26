package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.impl.light.LightElement
import com.intellij.util.IncorrectOperationException
import kr.jaehoyi.gdshader.GdsLanguage

class GdsBuiltinDefine(
    manager: PsiManager,
    private val defineName: String,
    val value: Int?,
) : LightElement(manager, GdsLanguage),
    PsiNamedElement {
    override fun getName(): String = defineName

    override fun getText(): String = defineName

    override fun setName(name: String): PsiElement = throw IncorrectOperationException("Cannot rename built-in define '$defineName'")

    override fun equals(other: Any?): Boolean = other is GdsBuiltinDefine && other.defineName == defineName && other.manager == manager

    override fun hashCode(): Int = defineName.hashCode()

    override fun toString(): String = "GDShaderBuiltinDefine($defineName)"
}

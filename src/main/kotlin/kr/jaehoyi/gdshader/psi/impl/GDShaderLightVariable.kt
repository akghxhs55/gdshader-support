package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.impl.light.LightElement
import kr.jaehoyi.gdshader.GDShaderLanguage
import kr.jaehoyi.gdshader.model.VariableSpec

class GDShaderLightVariable(
    manager: PsiManager,
    val variableSpec: VariableSpec
) : LightElement(manager, GDShaderLanguage), PsiNamedElement {

    override fun getName(): String = variableSpec.name
    override fun setName(name: String): PsiElement = this
    override fun toString(): String = "GDShaderLightVariable(${variableSpec.name})"
    
}
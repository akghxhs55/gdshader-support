package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.light.LightElement
import kr.jaehoyi.gdshader.GdsLanguage
import kr.jaehoyi.gdshader.model.VariableSpec
import kr.jaehoyi.gdshader.psi.GdsVariable

class GdsLightVariable(
    manager: PsiManager,
    override val variableSpec: VariableSpec
) : LightElement(manager, GdsLanguage), GdsVariable {

    override fun getName(): String = variableSpec.name
    override fun setName(name: String): PsiElement = this
    override fun toString(): String = "GDShaderLightVariable(${variableSpec.name})"
    
}
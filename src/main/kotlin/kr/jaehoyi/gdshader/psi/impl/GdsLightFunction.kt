package kr.jaehoyi.gdshader.psi.impl

import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.impl.light.LightElement
import kr.jaehoyi.gdshader.GdsLanguage
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.psi.GdsFunction

class GdsLightFunction(
    manager: PsiManager,
    override val functionSpec: FunctionSpec
) : LightElement(manager, GdsLanguage), GdsFunction {

    override fun getName(): String = functionSpec.name
    override fun setName(name: String): PsiNamedElement = this
    override fun toString(): String = "GDShaderLightFunction(${functionSpec.name})"
    
}
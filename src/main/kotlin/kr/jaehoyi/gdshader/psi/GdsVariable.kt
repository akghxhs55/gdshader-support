package kr.jaehoyi.gdshader.psi

import com.intellij.psi.PsiNamedElement
import kr.jaehoyi.gdshader.model.VariableSpec

interface GdsVariable : PsiNamedElement {
    
    val variableSpec: VariableSpec?
    
}

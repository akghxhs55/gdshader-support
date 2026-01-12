package kr.jaehoyi.gdshader.psi

import com.intellij.psi.PsiNamedElement
import kr.jaehoyi.gdshader.model.FunctionSpec

interface GdsFunction : PsiNamedElement {
    
    val functionSpec: FunctionSpec?
    
}
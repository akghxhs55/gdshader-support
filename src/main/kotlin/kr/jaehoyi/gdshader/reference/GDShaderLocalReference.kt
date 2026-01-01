package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.psi.GDShaderElementFactory
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderStatementBody

class GDShaderLocalReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange) {
    
    private val key: String = element.text

    override fun resolve(): PsiElement? {
        var currentElement = element
        while (true) {
            val parent = currentElement.parent ?: break
            
            val siblings = parent.children
            for (sibling in siblings) {
                if (sibling === currentElement) {
                    break
                }

                val decl = findDeclarationInElement(sibling, key)
                if (decl != null) {
                    return decl
                }
            }
            
            
            if (parent is GDShaderFunctionDeclaration) {
                val parameterList = parent.parameterList
                if (parameterList != null) {
                    for (param in parameterList.parameterList) {
                        val paramName = param.variableNameDecl.text
                        if (paramName == key) {
                            return param.variableNameDecl
                        }
                    }
                }
            }
            
            currentElement = parent
        }
        
        return null
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        val identifier = GDShaderElementFactory.createIdentifier(element.project, newElementName) ?: return null
        element.firstChild.replace(identifier)
        return element
    }

    private fun findDeclarationInElement(element: PsiElement, name: String): PsiElement? {
        if (element !is GDShaderStatementBody) {
            return null
        }

        val declaration = PsiTreeUtil.findChildOfType(element, GDShaderLocalVariableDeclaration::class.java) ?: return null

        for (declarator in declaration.localVariableDeclaratorList.localVariableDeclaratorList) {
            val declNameNode = declarator.variableNameDecl
            if (declNameNode.text == name) {
                return declNameNode
            }
        }

        return null
    }
    
}
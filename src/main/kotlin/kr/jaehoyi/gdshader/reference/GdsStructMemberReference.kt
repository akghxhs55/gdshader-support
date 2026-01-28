package kr.jaehoyi.gdshader.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import kr.jaehoyi.gdshader.model.StructType
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsPostfixExpr
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsStructMemberReference(
    element: GdsStructMemberNameRef,
    textRange: TextRange
) : PsiReferenceBase<GdsStructMemberNameRef>(element, textRange) {

    override fun resolve(): PsiElement? {
        val postfixExpr = element.parent as? GdsPostfixExpr
        if (postfixExpr == null) {
            return null
        }
        
        val previousType = GdsExpressionTypeInference.inferTypeBefore(postfixExpr, element)
        
        if (previousType is StructType) {
            val result = resolveStructMember(previousType, element.text)
            return result
        }
        
        return null
    }
    
    private fun resolveStructMember(structType: StructType, memberName: String): PsiElement? {
        val structDecl = resolveStruct(element, structType.name) ?: return null
        
        structDecl.structBlock?.structMemberList?.structMemberList?.forEach { member ->
            val nameDecl = member.structMemberNameDecl
            if (nameDecl?.text == memberName) {
                return nameDecl
            }
        }
        return null
    }
    
    private fun resolveStruct(element: PsiElement, name: String): GdsStructDeclaration? {
        var result: GdsStructDeclaration? = null
        GdsResolver.processStructDeclaration(element) { decl ->
            if (decl.name == name) {
                result = decl.parent as? GdsStructDeclaration
                false
            } else {
                true
            }
        }
        return result
    }
}

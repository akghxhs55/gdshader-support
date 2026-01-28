package kr.jaehoyi.gdshader.editor

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.GdsLexerAdapter
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsTokenSets
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls

class GdsFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner =
        DefaultWordsScanner(
            GdsLexerAdapter(),
            TokenSet.create(GdsTypes.IDENTIFIER),
            GdsTokenSets.COMMENTS,
            GdsTokenSets.STRING_LITERALS
        )

    override fun canFindUsagesFor(element: PsiElement): Boolean {
        return element is GdsVariableNameDecl ||
                element is GdsFunctionNameDecl ||
                element is GdsStructNameDecl ||
                element is GdsStructMemberNameDecl
    }

    override fun getHelpId(element: PsiElement): @NonNls String? = null

    override fun getType(element: PsiElement): @Nls String =
        when (element) {
            is GdsVariableNameDecl -> "Variable"
            is GdsFunctionNameDecl -> "Function"
            is GdsStructNameDecl -> "Struct"
            is GdsStructMemberNameDecl -> "Struct Member"
            else -> ""
        }

    override fun getDescriptiveName(element: PsiElement): @Nls String = 
        when (element) {
            is GdsVariableNameDecl -> element.name
            is GdsFunctionNameDecl -> element.name
            is GdsStructNameDecl -> element.name
            is GdsStructMemberNameDecl -> element.name
            else -> ""
        }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): @Nls String =
        getDescriptiveName(element)

}
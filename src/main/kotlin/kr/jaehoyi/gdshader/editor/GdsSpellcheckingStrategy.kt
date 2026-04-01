package kr.jaehoyi.gdshader.editor

import com.intellij.psi.PsiElement
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.Tokenizer
import kr.jaehoyi.gdshader.psi.GdsTokenSets

class GdsSpellcheckingStrategy : SpellcheckingStrategy() {
    override fun getTokenizer(element: PsiElement): Tokenizer<*> {
        val elementType = element.node.elementType
        if (GdsTokenSets.COMMENTS.contains(elementType)) {
            return TEXT_TOKENIZER
        }
        if (GdsTokenSets.STRING_LITERALS.contains(elementType)) {
            return TEXT_TOKENIZER
        }
        return super.getTokenizer(element)
    }
}

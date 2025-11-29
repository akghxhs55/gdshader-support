package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement

object GDShaderSemicolonInsertHandler : InsertHandler<LookupElement> {

    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val document = context.document
        val editor = context.editor
        
        val tailOffset = context.tailOffset
        
        val chars = document.charsSequence
        
        if (tailOffset < chars.length && chars[tailOffset] != ';') {
            editor.caretModel.moveToOffset(tailOffset + 1)
            return
        }
        
        document.insertString(tailOffset, ";")
        
        editor.caretModel.moveToOffset(tailOffset + 1)
    }
    
}
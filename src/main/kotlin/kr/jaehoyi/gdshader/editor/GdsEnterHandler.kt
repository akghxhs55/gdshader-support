package kr.jaehoyi.gdshader.editor

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.GdsFileType
import kr.jaehoyi.gdshader.psi.GdsStructBlock

class GdsEnterHandler : EnterHandlerDelegateAdapter() {

    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int?>,
        caretAdvance: Ref<Int?>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?
    ): EnterHandlerDelegate.Result {
        if (file.fileType != GdsFileType) {
            return EnterHandlerDelegate.Result.Continue
        }
        
        val offset = caretOffset.get() ?: return EnterHandlerDelegate.Result.Continue
        val text = editor.document.charsSequence
        
        if (offset < 1 || offset >= text.length) {
            return EnterHandlerDelegate.Result.Continue
        }
        
        val charBefore = text[offset - 1]
        val charAfter = text[offset]
        
        if (charBefore == '{' && charAfter == '}') {
            PsiDocumentManager.getInstance(file.project).commitDocument(editor.document)
            val element = file.findElementAt(offset - 1)
            if (PsiTreeUtil.getParentOfType(element, GdsStructBlock::class.java) != null) {
                if (offset + 1 >= text.length || text[offset + 1] != ';') {
                    editor.document.insertString(offset + 1, ";")
                }
            }
            return EnterHandlerDelegate.Result.Continue
        }
        
        if (offset < 2) {
            return EnterHandlerDelegate.Result.Continue
        }
        
        val isBetweenComment = text[offset - 2] == '/' && text[offset - 1] == '*' &&
                text[offset] == '*' && text[offset + 1] == '/'
        
        if (isBetweenComment) {
            editor.document.insertString(offset, "\n")
            return EnterHandlerDelegate.Result.Continue
        }
        
        return EnterHandlerDelegate.Result.Continue
    }
    
}
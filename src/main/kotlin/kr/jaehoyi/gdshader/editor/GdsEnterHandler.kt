package kr.jaehoyi.gdshader.editor

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import kr.jaehoyi.gdshader.GdsFileType

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
        
        if (offset < 2 || offset > text.length - 2) {
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
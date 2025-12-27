package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import kr.jaehoyi.gdshader.psi.GDShaderFile

class GDShaderCompletionTypedHandler : TypedHandlerDelegate() {

    override fun checkAutoPopup(charTyped: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file !is GDShaderFile) {
            return Result.CONTINUE
        }
        
        when (charTyped) {
            ',' -> AutoPopupController.getInstance(project).scheduleAutoPopup(editor)
        
            ' ' -> {
                val offset = editor.caretModel.offset
                if (offset < 2) {
                    return Result.CONTINUE
                }
                
                val document = editor.document
                val prevChar = document.charsSequence[offset - 1]
                if (prevChar == ',') {
                    AutoPopupController.getInstance(project).scheduleAutoPopup(editor)
                }
            }
        }
        
        return Result.CONTINUE
    }
    
}
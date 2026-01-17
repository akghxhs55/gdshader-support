package kr.jaehoyi.gdshader.editor

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import kr.jaehoyi.gdshader.GdsLanguage

class GdsTypedHandler : TypedHandlerDelegate() {
    
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.language !is GdsLanguage) {
            return Result.CONTINUE
        }
        
        val document = editor.document
        val offset = editor.caretModel.offset
        
        when (c) {
            ';' -> {
                PsiDocumentManager.getInstance(project).commitDocument(document)

                val lineNumber = document.getLineNumber(offset)
                val lineStartOffset = document.getLineStartOffset(lineNumber)

                CodeStyleManager.getInstance(project).adjustLineIndent(file, lineStartOffset)    
            }
            
            '*' -> {
                val text = document.charsSequence
                
                if (offset >= 2 && text[offset - 2] == '/' ) {
                    if (offset + 2 <= document.textLength && text.subSequence(offset, offset + 2) == "*/") {
                        return Result.CONTINUE
                    }
                    
                    document.insertString(offset, "*/")
                    
                    return Result.STOP
                }
            }
        }
        
        return Result.CONTINUE
    }
    
}
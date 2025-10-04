package kr.jaehoyi.gdshader.formatter

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import kr.jaehoyi.gdshader.GDShaderLanguage

class GDShaderTypedHandler : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.language !is GDShaderLanguage) {
            return Result.CONTINUE
        }
        
        if (c == ';') {
            val document = editor.document
            PsiDocumentManager.getInstance(project).commitDocument(document)

            val caretOffset = editor.caretModel.offset
            val lineNumber = document.getLineNumber(caretOffset)
            val lineStartOffset = document.getLineStartOffset(lineNumber)

            CodeStyleManager.getInstance(project).adjustLineIndent(file, lineStartOffset)
        }
        
        return Result.CONTINUE
    }
}
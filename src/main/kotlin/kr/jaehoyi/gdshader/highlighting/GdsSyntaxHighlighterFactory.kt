package kr.jaehoyi.gdshader.highlighting

import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class GdsSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?) =
        GdsSyntaxHighlighter()
    
}
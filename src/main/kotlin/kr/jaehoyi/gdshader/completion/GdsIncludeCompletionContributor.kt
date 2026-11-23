package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionInitializationContext
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

class GdsIncludeCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            psiElement(GdsTypes.PP_INCLUDE_LINE),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet,
                ) {
                    val position = parameters.position
                    val textBeforeCaret = position.text.substring(0, parameters.offset - position.textRange.startOffset)
                    val prefix = findPathPrefix(textBeforeCaret) ?: return

                    val pathResult = result.withPrefixMatcher(prefix)
                    if (isResPathPrefix(prefix)) {
                        addResPathCompletions(parameters.originalFile, prefix, pathResult)
                    } else {
                        addRelativePathCompletions(parameters.originalFile, prefix, pathResult)
                    }

                    result.stopHere()
                }
            },
        )
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        val document = context.editor.document
        val caretOffset = context.startOffset
        val lineNumber = document.getLineNumber(caretOffset)
        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)
        val chars = document.charsSequence

        val textBeforeCaret = chars.subSequence(lineStart, caretOffset).toString()
        if (!textBeforeCaret.trimStart().startsWith("#include")) return
        if (findPathPrefix(textBeforeCaret) == null) return

        // Replace the whole path on Tab, not just the identifier part.
        var pathEnd = caretOffset
        while (pathEnd < lineEnd && chars[pathEnd] != '"') {
            pathEnd++
        }
        context.offsetMap.addOffset(CompletionInitializationContext.IDENTIFIER_END_OFFSET, pathEnd)
    }

    private fun findPathPrefix(textBeforeCaret: String): String? {
        val openingQuote = textBeforeCaret.indexOf('"')
        if (openingQuote < 0) return null

        val prefix = textBeforeCaret.substring(openingQuote + 1)
        if (prefix.contains('"')) return null

        return prefix
    }

    private fun isResPathPrefix(prefix: String): Boolean =
        prefix.startsWith(GdsPathUtil.RES_PREFIX) ||
            GdsPathUtil.RES_PREFIX.startsWith(prefix) ||
            (!prefix.startsWith(".") && !prefix.contains('/'))

    private fun addResPathCompletions(
        contextFile: PsiFile,
        prefix: String,
        result: CompletionResultSet,
    ) {
        if (GdsPathUtil.RES_PREFIX.startsWith(prefix) && prefix != GdsPathUtil.RES_PREFIX) {
            result.addElement(GdsLookupElements.RES_PATH_PREFIX)
        }

        val resRoot = GdsPathUtil.findResRoot(contextFile) ?: return
        for (file in findIncludableFiles(contextFile, resRoot)) {
            val relativePath = VfsUtilCore.getRelativePath(file, resRoot) ?: continue
            result.addElement(GdsLookupElements.createFromIncludeFilePath(GdsPathUtil.RES_PREFIX + relativePath))
        }
    }

    private fun addRelativePathCompletions(
        contextFile: PsiFile,
        prefix: String,
        result: CompletionResultSet,
    ) {
        val currentDir = contextFile.originalFile.virtualFile?.parent ?: return
        val resRoot = GdsPathUtil.findResRoot(contextFile) ?: return
        val currentDirMarker = if (prefix.startsWith("./")) "./" else ""

        for (file in findIncludableFiles(contextFile, resRoot)) {
            val relativePath = VfsUtilCore.findRelativePath(currentDir, file, '/') ?: continue
            val path = if (relativePath.startsWith("..")) relativePath else currentDirMarker + relativePath
            result.addElement(GdsLookupElements.createFromIncludeFilePath(path))
        }
    }

    private fun findIncludableFiles(
        contextFile: PsiFile,
        resRoot: VirtualFile,
    ): List<VirtualFile> {
        val currentFile = contextFile.originalFile.virtualFile

        return GdsPathUtil
            .findIncludableFiles(resRoot, contextFile.project, currentFile)
            .filter { it != currentFile }
    }
}

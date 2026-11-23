package kr.jaehoyi.gdshader.resolve

import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveState
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsTypes

object GdsIncludeManager {
    private val INCLUDE_ENTRIES_KEY = Key.create<CachedValue<List<IncludeEntry>>>("GDS_INCLUDE_ENTRIES")

    // Files brought in by a single include line, including nested includes not already brought in by earlier lines.
    private class IncludeEntry(
        val offset: Int,
        val files: List<GdsFile>,
    )

    fun processIncludedDeclarations(
        currentFile: PsiFile,
        placeOffset: Int,
        processor: PsiScopeProcessor,
        state: ResolveState,
    ): Boolean {
        for (entry in getIncludeEntries(currentFile)) {
            if (entry.offset >= placeOffset) break
            for (file in entry.files) {
                if (!file.processOwnDeclarations(processor, state)) return false
            }
        }

        return true
    }

    fun getFilesIncludedBy(includeLine: PsiElement): List<GdsFile> {
        val offset = includeLine.textOffset
        return getIncludeEntries(includeLine.containingFile)
            .firstOrNull { it.offset == offset }
            ?.files
            .orEmpty()
    }

    private fun getIncludeEntries(file: PsiFile?): List<IncludeEntry> {
        if (file !is GdsFile) return emptyList()

        return CachedValuesManager.getCachedValue(file, INCLUDE_ENTRIES_KEY) {
            val visited = mutableSetOf<PsiFile>(file)
            val entries =
                findIncludeLines(file).map { line ->
                    val files = mutableListOf<GdsFile>()
                    resolveIncludeLine(file, line)?.let { collectIncludesRecursive(it, visited, files, 1) }
                    IncludeEntry(line.textOffset, files)
                }

            CachedValueProvider.Result.create(entries, PsiModificationTracker.MODIFICATION_COUNT)
        }
    }

    private fun collectIncludesRecursive(
        currentFile: PsiFile,
        visited: MutableSet<PsiFile>,
        result: MutableList<GdsFile>,
        depth: Int,
    ) {
        if (depth > 25) return
        if (currentFile.virtualFile?.extension != "gdshaderinc") return
        if (!visited.add(currentFile)) return

        if (currentFile is GdsFile) result += currentFile

        for (line in findIncludeLines(currentFile)) {
            val includedFile = resolveIncludeLine(currentFile, line) ?: continue
            collectIncludesRecursive(includedFile, visited, result, depth + 1)
        }
    }

    private fun resolveIncludeLine(
        file: PsiFile,
        line: PsiElement,
    ): PsiFile? {
        val includePath = GdsPathUtil.findIncludePath(line) ?: return null
        return GdsPathUtil.resolvePath(file, includePath.path)
    }

    private fun findIncludeLines(file: PsiFile): List<PsiElement> =
        SyntaxTraverser
            .psiTraverser(file)
            .filter { it.node.elementType == GdsTypes.PP_INCLUDE_LINE }
            .toList()
}

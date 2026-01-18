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

    private val INCLUDED_FILES_KEY = Key.create<CachedValue<Set<PsiFile>>>("GDS_INCLUDED_FILES")
    
    private val INCLUDE_PATH_REGEX = Regex("\"([^\"]+)\"")

    fun processIncludedDeclarations(
        currentFile: PsiFile,
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        val includedFiles = getIncludedFiles(currentFile)

        for (includedFile in includedFiles) {
            if (!includedFile.processDeclarations(processor, state, lastParent, place)) {
                return false
            }
        }

        return true
    }
    
    fun getIncludedFiles(file: PsiFile): Set<PsiFile> {
        if (file !is GdsFile) return emptySet()
        
        return CachedValuesManager.getCachedValue(file, INCLUDED_FILES_KEY) {
            val result = mutableSetOf<PsiFile>()
            collectIncludesRecursive(file, result, 0)
            
            result.remove(file)

            CachedValueProvider.Result.create(result, PsiModificationTracker.MODIFICATION_COUNT)
        }
    }

    private fun collectIncludesRecursive(
        currentFile: PsiFile,
        visited: MutableSet<PsiFile>,
        depth: Int
    ) {
        if (depth > 25) return

        if (!visited.add(currentFile)) return
        
        val includePaths = findIncludePathsInFile(currentFile)

        for (path in includePaths) {
            val includedFile = GdsPathUtil.resolvePath(currentFile, path) ?: continue

            val ext = includedFile.virtualFile?.extension
            if (ext != "gdshaderinc") continue

            collectIncludesRecursive(includedFile, visited, depth + 1)
        }
    }

    private fun findIncludePathsInFile(file: PsiFile): List<String> {
        val paths = mutableListOf<String>()
        
        val elements = SyntaxTraverser.psiTraverser(file)
            .filter { it.node.elementType == GdsTypes.PP_INCLUDE_LINE }
        
        for (element in elements) {
            val text = element.text
            
            val matchResult = INCLUDE_PATH_REGEX.find(text)
            if (matchResult != null) {
                paths.add(matchResult.groupValues[1])
            }
        }
        
        return paths
    }
    
}
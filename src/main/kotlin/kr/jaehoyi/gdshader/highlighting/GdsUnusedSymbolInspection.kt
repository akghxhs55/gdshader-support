package kr.jaehoyi.gdshader.highlighting

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.options.OptPane
import com.intellij.codeInspection.options.OptPane.checkbox
import com.intellij.codeInspection.options.OptPane.pane
import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.parentOfTypes
import kr.jaehoyi.gdshader.GdsBundle
import kr.jaehoyi.gdshader.GdsFileType
import kr.jaehoyi.gdshader.GdsIncludeFileType
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.psi.GdsBlock
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsParameterList
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVisitor
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives
import kr.jaehoyi.gdshader.resolve.GdsSearchScopes

class GdsUnusedSymbolInspection : GdsLocalizedInspection("inspection.unused.symbol.description") {
    @JvmField
    var reportIncludeTopLevelSymbols = false

    override fun getOptionsPane(): OptPane =
        pane(
            checkbox(
                "reportIncludeTopLevelSymbols",
                GdsBundle.message("inspection.unused.symbol.option.include.top.level"),
            ),
        )

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor {
        return object : GdsVisitor() {
            override fun visitVariableNameDecl(element: GdsVariableNameDecl) {
                val parent = element.parent
                if (parent !is GdsLocalVariableDeclarator &&
                    parent !is GdsConstantDeclarator &&
                    parent !is GdsParameter
                ) {
                    return
                }

                if (isIgnoredTopLevel(element)) return

                if (isUnused(element)) {
                    val kind =
                        when (parent) {
                            is GdsParameter -> GdsBundle.message("inspection.unused.symbol.message.parameter")
                            is GdsConstantDeclarator -> GdsBundle.message("inspection.unused.symbol.message.constant")
                            else -> GdsBundle.message("inspection.unused.symbol.message.variable")
                        }
                    holder.registerProblem(
                        element,
                        "$kind ${GdsBundle.message("inspection.unused.symbol.message", element.name)}",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    )
                }
            }

            override fun visitFunctionNameDecl(element: GdsFunctionNameDecl) {
                val parent = element.parent
                if (parent !is GdsFunctionDeclaration) return

                if (isProcessingFunction(element)) return

                if (isIgnoredTopLevel(element)) return

                if (isUnused(element)) {
                    val kind = GdsBundle.message("inspection.unused.symbol.message.function")
                    holder.registerProblem(
                        element,
                        "$kind ${GdsBundle.message("inspection.unused.symbol.message", element.name)}",
                        ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    )
                }
            }
        }
    }

    private fun isIgnoredTopLevel(element: PsiElement): Boolean =
        !reportIncludeTopLevelSymbols &&
            element.containingFile.viewProvider.virtualFile.fileType == GdsIncludeFileType &&
            element.parentOfTypes(GdsBlock::class, GdsParameterList::class) == null

    private fun isUnused(element: PsiNamedElement): Boolean {
        // Usages resolve to the built-in define, and the declaration is already an error.
        if (element.name in GdsConditionalDirectives.BUILTIN_DEFINES) return false
        val useScope = element.useScope
        if (useScope is LocalSearchScope) {
            return ReferencesSearch.search(element, useScope, false).findFirst() == null
        }

        // A LocalSearchScope search reads the file directly, so usages in the same file are found even when the
        // file is not indexed.
        val file = element.containingFile ?: return false
        if (ReferencesSearch.search(element, LocalSearchScope(file), false).findFirst() != null) return false

        // Only include files can be included, so symbols in a shader file are never used by other files.
        if (file.viewProvider.virtualFile.fileType == GdsFileType) return true

        val project = element.project
        if (DumbService.isDumb(project)) return false
        val scope = GdsSearchScopes.godotFiles(project)
        if (!isIndexed(file, element.name ?: return false, scope)) return false
        return ReferencesSearch.search(element, scope, false).findFirst() == null
    }

    // Searches in other files go through the word index, which knows nothing about unindexed files such as ones in
    // folders excluded from indexing or outside the solution directory. The query must use the same scope as the
    // search because the index answers queries over a single file even for unindexed files.
    private fun isIndexed(
        file: PsiFile,
        name: String,
        scope: GlobalSearchScope,
    ): Boolean {
        var found = false
        PsiSearchHelper.getInstance(file.project).processAllFilesWithWord(
            name,
            scope,
            {
                found = it == file
                !found
            },
            true,
        )
        return found
    }

    private fun isProcessingFunction(element: GdsFunctionNameDecl): Boolean {
        val functionContext = FunctionContext.fromText(element.name)
        val file = element.containingFile as? GdsFile ?: return false
        val shaderType = GdsPsiImplUtil.getShaderType(file) ?: return false
        val validFunctions = Builtins.PROCESSING_FUNCTIONS[shaderType] ?: return false
        return functionContext in validFunctions
    }
}

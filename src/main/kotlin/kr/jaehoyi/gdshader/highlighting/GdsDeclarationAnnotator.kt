package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructMember
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.resolve.GdsPathUtil

class GdsDeclarationAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when {
            element is GdsStructDeclaration -> {
                checkEmptyStruct(element, holder)
                checkDuplicateStruct(element, holder)
            }
            element is GdsFunctionDeclaration -> checkDuplicateFunction(element, holder)
            element is GdsUniformDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateFileLevelVariable(element.variableNameDecl, holder)
            }
            element is GdsVaryingDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateFileLevelVariable(element.variableNameDecl, holder)
            }
            element is GdsConstantDeclaration -> {
                if (element.parentOfType<GdsFunctionDeclaration>() == null) {
                    checkDuplicateConstantVariables(element, holder)
                }
            }
            element is GdsParameter -> checkDoubleArraySize(element.arraySizeList, holder)
            element is GdsStructMember -> checkDoubleArraySize(element.arraySizeList, holder)
            element is GdsLocalVariableDeclarator -> checkSplitDoubleArraySize(element, holder)
            element is GdsConstantDeclarator -> checkSplitDoubleArraySize(element, holder)
            element.node.elementType == GdsTypes.PP_INCLUDE_LINE -> checkIncludeLine(element, holder)
            element.node.elementType == GdsTypes.PP_UNKNOWN_LINE -> {
                holder.newAnnotation(HighlightSeverity.ERROR, "Unknown preprocessor directive '${element.text}'")
                    .range(element)
                    .create()
            }
        }
    }

    private fun checkEmptyStruct(element: GdsStructDeclaration, holder: AnnotationHolder) {
        val structBlock = element.structBlock ?: return
        if (structBlock.structMemberList.structMemberList.isEmpty()) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Empty structs are not allowed")
                .range(element)
                .create()
        }
    }

    // === Duplicate declaration checks ===

    private fun checkDuplicateFunction(element: GdsFunctionDeclaration, holder: AnnotationHolder) {
        val name = element.functionNameDecl.text
        val file = element.containingFile ?: return
        val allFunctions = PsiTreeUtil.findChildrenOfType(file, GdsFunctionDeclaration::class.java)
        val first = allFunctions.firstOrNull { it.functionNameDecl.text == name } ?: return
        if (first !== element) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(element.functionNameDecl)
                .create()
        }
    }

    private fun checkDuplicateStruct(element: GdsStructDeclaration, holder: AnnotationHolder) {
        val nameDecl = element.structNameDecl ?: return
        val name = nameDecl.text
        val file = element.containingFile ?: return
        val allStructs = PsiTreeUtil.findChildrenOfType(file, GdsStructDeclaration::class.java)
        val first = allStructs.firstOrNull { it.structNameDecl?.text == name } ?: return
        if (first !== element) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    private fun checkDuplicateFileLevelVariable(nameDecl: GdsVariableNameDecl?, holder: AnnotationHolder) {
        nameDecl ?: return
        val name = nameDecl.name
        val file = nameDecl.containingFile ?: return
        val allNames = collectFileLevelVariableNames(file)
        val first = allNames.firstOrNull { it.name == name } ?: return
        if (first !== nameDecl) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    private fun checkDuplicateConstantVariables(element: GdsConstantDeclaration, holder: AnnotationHolder) {
        val declarators = element.constantDeclaratorList?.constantDeclaratorList ?: return
        for (declarator in declarators) {
            checkDuplicateFileLevelVariable(declarator.variableNameDecl, holder)
        }
    }

    private fun collectFileLevelVariableNames(file: PsiElement): List<GdsVariableNameDecl> {
        val result = mutableListOf<GdsVariableNameDecl>()
        for (uniform in PsiTreeUtil.findChildrenOfType(file, GdsUniformDeclaration::class.java)) {
            uniform.variableNameDecl?.let { result.add(it) }
        }
        for (varying in PsiTreeUtil.findChildrenOfType(file, GdsVaryingDeclaration::class.java)) {
            varying.variableNameDecl?.let { result.add(it) }
        }
        for (constant in PsiTreeUtil.findChildrenOfType(file, GdsConstantDeclaration::class.java)) {
            if (constant.parentOfType<GdsFunctionDeclaration>() != null) continue
            constant.constantDeclaratorList?.constantDeclaratorList?.forEach {
                result.add(it.variableNameDecl)
            }
        }
        return result
    }

    // === Double array size checks ===

    private fun checkDoubleArraySize(arraySizes: List<*>, holder: AnnotationHolder) {
        if (arraySizes.size >= 2) {
            val second = arraySizes[1] as PsiElement
            holder.newAnnotation(HighlightSeverity.ERROR, "Array size is already defined")
                .range(second)
                .create()
        }
    }

    private fun checkSplitDoubleArraySize(element: PsiElement, holder: AnnotationHolder) {
        val declaratorArraySize = when (element) {
            is GdsLocalVariableDeclarator -> element.arraySize
            is GdsConstantDeclarator -> element.arraySize
            else -> return
        } ?: return

        val parentArraySize = when (val parentDecl = element.parent?.parent) {
            is GdsLocalVariableDeclaration -> parentDecl.arraySize
            is GdsConstantDeclaration -> parentDecl.arraySize
            else -> null
        }

        if (parentArraySize != null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Array size is already defined")
                .range(declaratorArraySize)
                .create()
        }
    }

    // === Include line checks ===

    private fun checkIncludeLine(element: PsiElement, holder: AnnotationHolder) {
        val text = element.text
        val regex = Regex("[\"']([^\"']*)[\"']")
        val matchResult = regex.find(text) ?: return
        val path = matchResult.groups[1]?.value ?: return

        if (!path.endsWith(".gdshaderinc")) {
            val pathRange = matchResult.groups[1]!!.range
            val startOffset = element.textRange.startOffset + pathRange.first
            val endOffset = element.textRange.startOffset + pathRange.last + 1
            holder.newAnnotation(HighlightSeverity.ERROR, "Only '.gdshaderinc' files can be included")
                .range(TextRange(startOffset, endOffset))
                .create()
            return
        }

        val resolved = GdsPathUtil.resolvePath(element.containingFile, path)
        if (resolved == null) {
            val pathRange = matchResult.groups[1]!!.range
            val startOffset = element.textRange.startOffset + pathRange.first
            val endOffset = element.textRange.startOffset + pathRange.last + 1
            holder.newAnnotation(HighlightSeverity.ERROR, "Cannot resolve file '$path'")
                .range(TextRange(startOffset, endOffset))
                .create()
        }
    }
}
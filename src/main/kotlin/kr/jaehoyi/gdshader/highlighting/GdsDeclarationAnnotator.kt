package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.psi.GdsBlockBody
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsInitializer
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructMember
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.resolve.GdsPathUtil
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsDeclarationAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when {
            element is GdsItem -> checkShaderTypeFirst(element, holder)
            element is GdsStructDeclaration -> {
                checkEmptyStruct(element, holder)
                checkDuplicateStruct(element, holder)
            }
            element is GdsFunctionDeclaration -> checkDuplicateFunction(element, holder)
            element is GdsUniformDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder)
                checkUniformInitializerType(element, holder)
            }
            element is GdsVaryingDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder)
            }
            element is GdsParameter -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder)
            }
            element is GdsStructMember -> checkDoubleArraySize(element.arraySizeList, holder)
            element is GdsLocalVariableDeclarator -> {
                checkSplitDoubleArraySize(element, holder)
                checkDuplicateLocalVariable(element.variableNameDecl, holder)
                checkInitializerType(element.initializer, GdsDataTypeFactory.createFromLocalVariableDeclaration(element), holder)
            }
            element is GdsConstantDeclarator -> {
                checkSplitDoubleArraySize(element, holder)
                if (element.parentOfType<GdsFunctionDeclaration>() != null) {
                    checkDuplicateLocalVariable(element.variableNameDecl, holder)
                } else {
                    checkDuplicateVariable(element.variableNameDecl, holder)
                }
                checkInitializerType(element.initializer, GdsDataTypeFactory.createFromConstantDeclaration(element), holder)
            }
            element.node.elementType == GdsTypes.PP_INCLUDE_LINE -> checkIncludeLine(element, holder)
            element.node.elementType == GdsTypes.PP_UNKNOWN_LINE -> {
                holder.newAnnotation(HighlightSeverity.ERROR, "Unknown preprocessor directive '${element.text}'")
                    .range(element)
                    .create()
            }
        }
    }

    // === shader_type must come first ===

    private fun checkShaderTypeFirst(element: GdsItem, holder: AnnotationHolder) {
        if (element.containingFile?.virtualFile?.extension == "gdshaderinc") return

        val topLevel = element.topLevelDeclaration
        if (topLevel.shaderTypeDeclaration != null) return

        val file = element.containingFile ?: return
        val allItems = PsiTreeUtil.findChildrenOfType(file, GdsItem::class.java)
        val firstNonShaderType = allItems.firstOrNull {
            it.topLevelDeclaration.shaderTypeDeclaration == null
        }

        if (firstNonShaderType !== element) return

        val hasShaderType = allItems.any {
            it.topLevelDeclaration.shaderTypeDeclaration != null &&
                    it.textOffset < element.textOffset
        }

        if (!hasShaderType) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Expected 'shader_type' before the first declaration")
                .range(element)
                .create()
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

    private fun checkDuplicateVariable(nameDecl: GdsVariableNameDecl?, holder: AnnotationHolder) {
        if (nameDecl == null) return
        val name = nameDecl.name
        if (collectVisibleVariables(nameDecl).any { it.name == name }) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    private fun checkDuplicateLocalVariable(nameDecl: GdsVariableNameDecl?, holder: AnnotationHolder) {
        if (nameDecl == null) return
        val name = nameDecl.name
        val myScope = PsiTreeUtil.getParentOfType(nameDecl, GdsBlockBody::class.java)

        val hasDuplicate = collectVisibleVariables(nameDecl)
            .filter { it.name == name }
            .any { element ->
                when (element) {
                    is GdsVariableNameDecl -> {
                        val parent = element.parent
                        if (parent is GdsLocalVariableDeclarator || parent is GdsConstantDeclarator) {
                            PsiTreeUtil.getParentOfType(element, GdsBlockBody::class.java) === myScope
                        }
                        else {
                            true
                        }
                    }
                    else -> true
                }
            }

        if (hasDuplicate) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    private fun collectVisibleVariables(position: PsiElement): List<GdsVariable> {
        val result = mutableListOf<GdsVariable>()
        GdsResolver.processVariableDeclaration(position) { element ->
            result += element
            true
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

    // === Initializer type checks ===

    private fun checkInitializerType(initializer: GdsInitializer?, declaredType: DataType?, holder: AnnotationHolder) {
        if (initializer == null || declaredType == null) return
        val expr = initializer.expression ?: return
        val exprType = GdsExpressionTypeInference.inferType(expr) ?: return

        if (declaredType.name != exprType.name) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Cannot assign a value of type '${exprType.name}' to type '${declaredType.name}'"
            ).range(initializer).create()
        }
    }

    private fun checkUniformInitializerType(element: GdsUniformDeclaration, holder: AnnotationHolder) {
        val expr = element.expression ?: return
        val declaredType = GdsDataTypeFactory.createFromUniformDeclaration(element) ?: return
        val exprType = GdsExpressionTypeInference.inferType(expr) ?: return

        if (declaredType.name != exprType.name) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Cannot assign a value of type '${exprType.name}' to type '${declaredType.name}'"
            ).range(expr).create()
        }
    }

    // === Include line checks ===

    private fun checkIncludeLine(element: PsiElement, holder: AnnotationHolder) {
        val text = element.text
        val regex = Regex("[\"']([^\"']*)[\"']")
        val matchResult = regex.find(text) ?: return
        val pathGroup = matchResult.groups[1] ?: return
        val path = pathGroup.value

        if (!path.endsWith(".gdshaderinc")) {
            val startOffset = element.textRange.startOffset + pathGroup.range.first
            val endOffset = element.textRange.startOffset + pathGroup.range.last + 1
            holder.newAnnotation(HighlightSeverity.ERROR, "Only '.gdshaderinc' files can be included")
                .range(TextRange(startOffset, endOffset))
                .create()
            return
        }

        val resolved = GdsPathUtil.resolvePath(element.containingFile, path)
        if (resolved == null) {
            val pathRange = pathGroup.range
            val startOffset = element.textRange.startOffset + pathRange.first
            val endOffset = element.textRange.startOffset + pathRange.last + 1
            holder.newAnnotation(HighlightSeverity.ERROR, "Cannot resolve file '$path'")
                .range(TextRange(startOffset, endOffset))
                .create()
        }
    }
}
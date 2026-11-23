package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.ResolveState
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FloatType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.IntType
import kr.jaehoyi.gdshader.model.UIntType
import kr.jaehoyi.gdshader.psi.GdsBlockBody
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsConstantEvaluator
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsInitializer
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructMember
import kr.jaehoyi.gdshader.psi.GdsStructMemberList
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsIncludeManager
import kr.jaehoyi.gdshader.resolve.GdsPathUtil
import kr.jaehoyi.gdshader.resolve.GdsResolver
import kr.jaehoyi.gdshader.resolve.GdsScopeProcessor

class GdsDeclarationAnnotator : Annotator {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.DECLARATION_VALIDATION)) return

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
            element is GdsStructMember -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateStructMember(element, holder)
            }
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
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Unknown preprocessor directive '${element.text}'")
                    .range(element)
                    .create()
            }
        }
    }

    // === shader_type must come first ===

    private fun checkShaderTypeFirst(
        element: GdsItem,
        holder: AnnotationHolder,
    ) {
        if (element.containingFile?.virtualFile?.extension == "gdshaderinc") return

        val topLevel = element.topLevelDeclaration
        if (topLevel.shaderTypeDeclaration != null) return

        val file = element.containingFile ?: return
        val allItems = PsiTreeUtil.findChildrenOfType(file, GdsItem::class.java)
        val firstNonShaderType =
            allItems.firstOrNull {
                it.topLevelDeclaration.shaderTypeDeclaration == null
            }

        if (firstNonShaderType !== element) return

        val hasShaderType =
            allItems.any {
                it.topLevelDeclaration.shaderTypeDeclaration != null &&
                    it.textOffset < element.textOffset
            }

        if (!hasShaderType) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Expected 'shader_type' before the first declaration")
                .range(element)
                .create()
        }
    }

    private fun checkEmptyStruct(
        element: GdsStructDeclaration,
        holder: AnnotationHolder,
    ) {
        val structBlock = element.structBlock ?: return
        val members = structBlock.structMemberList?.structMemberList ?: return
        if (members.isEmpty()) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Empty structs are not allowed")
                .range(element)
                .create()
        }
    }

    // === Duplicate declaration checks ===

    private fun checkDuplicateFunction(
        element: GdsFunctionDeclaration,
        holder: AnnotationHolder,
    ) {
        val nameDecl = element.functionNameDecl
        if (hasConflictingFunction(nameDecl, nameDecl)) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '${nameDecl.name}'")
                .range(nameDecl)
                .create()
        }
    }

    private fun hasConflictingFunction(
        nameDecl: GdsFunctionNameDecl,
        position: PsiElement,
    ): Boolean {
        val declaration = nameDecl.parent as? GdsFunctionDeclaration ?: return false
        val name = nameDecl.name
        val isProcessingFunction = isProcessingFunction(name, position)
        val signature = getParameterSignature(declaration)
        if (!isProcessingFunction && signature == null) return false

        var found = false
        GdsResolver.processFunctionDeclaration(position) { function ->
            if (function !is GdsFunctionNameDecl || function.name != name) return@processFunctionDeclaration true
            val other = function.parent as? GdsFunctionDeclaration ?: return@processFunctionDeclaration true
            found = isProcessingFunction || getParameterSignature(other) == signature
            !found
        }
        return found
    }

    private fun isProcessingFunction(
        name: String,
        position: PsiElement,
    ): Boolean {
        val shaderType = GdsPsiImplUtil.getShaderType(position) ?: return false
        val processingFunctions = Builtins.PROCESSING_FUNCTIONS[shaderType] ?: return false
        return FunctionContext.fromText(name) in processingFunctions
    }

    // Godot compares parameter types including array sizes, but ignores qualifiers and precision.
    // Returns null when a parameter type cannot be determined, since the comparison would be unreliable.
    private fun getParameterSignature(function: GdsFunctionDeclaration): List<String>? =
        function.parameterList?.parameterList.orEmpty().map { parameter ->
            val type = GdsDataTypeFactory.createFromParameter(parameter) ?: return null
            if (type is ArrayType && type.containerSize == null) return null
            type.name
        }

    private fun checkDuplicateStruct(
        element: GdsStructDeclaration,
        holder: AnnotationHolder,
    ) {
        val nameDecl = element.structNameDecl ?: return
        val name = nameDecl.name
        if (hasConflictingStruct(name, nameDecl)) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    private fun hasConflictingStruct(
        name: String,
        position: PsiElement,
    ): Boolean {
        var found = false
        GdsResolver.processStructDeclaration(position) { struct ->
            found = struct.name == name
            !found
        }
        return found
    }

    private fun checkDuplicateStructMember(
        element: GdsStructMember,
        holder: AnnotationHolder,
    ) {
        val nameDecl = element.structMemberNameDecl ?: return
        val name = nameDecl.text
        val memberList = element.parent as? GdsStructMemberList ?: return
        val first = memberList.structMemberList.firstOrNull { it.structMemberNameDecl?.text == name } ?: return
        if (first !== element) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    private fun checkDuplicateVariable(
        nameDecl: GdsVariableNameDecl?,
        holder: AnnotationHolder,
    ) {
        if (nameDecl == null) return
        val name = nameDecl.name
        if (hasConflictingVariable(name, nameDecl)) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    private fun hasConflictingVariable(
        name: String,
        position: PsiElement,
    ): Boolean = collectVisibleVariables(position).any { it.name == name }

    private fun checkDuplicateLocalVariable(
        nameDecl: GdsVariableNameDecl?,
        holder: AnnotationHolder,
    ) {
        if (nameDecl == null) return
        val name = nameDecl.name
        val myScope = PsiTreeUtil.getParentOfType(nameDecl, GdsBlockBody::class.java)

        val hasDuplicate =
            collectVisibleVariables(nameDecl)
                .filter { it.name == name }
                .any { element ->
                    when (element) {
                        is GdsVariableNameDecl -> {
                            val parent = element.parent
                            if (parent is GdsLocalVariableDeclarator || parent is GdsConstantDeclarator) {
                                PsiTreeUtil.getParentOfType(element, GdsBlockBody::class.java) === myScope
                            } else {
                                true
                            }
                        }
                        else -> true
                    }
                }

        if (hasDuplicate) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
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

    private fun checkDoubleArraySize(
        arraySizes: List<*>,
        holder: AnnotationHolder,
    ) {
        if (arraySizes.size >= 2) {
            val second = arraySizes[1] as? PsiElement ?: return
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Array size is already defined")
                .range(second)
                .create()
        }
    }

    private fun checkSplitDoubleArraySize(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        val declaratorArraySize =
            when (element) {
                is GdsLocalVariableDeclarator -> element.arraySize
                is GdsConstantDeclarator -> element.arraySize
                else -> return
            } ?: return

        val parentArraySize =
            when (val parentDecl = element.parent?.parent) {
                is GdsLocalVariableDeclaration -> parentDecl.arraySize
                is GdsConstantDeclaration -> parentDecl.arraySize
                else -> null
            }

        if (parentArraySize != null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Array size is already defined")
                .range(declaratorArraySize)
                .create()
        }
    }

    // === Initializer type checks ===

    private fun checkInitializerType(
        initializer: GdsInitializer?,
        declaredType: DataType?,
        holder: AnnotationHolder,
    ) {
        if (initializer == null || declaredType == null) return
        val expr = initializer.expression ?: return
        val exprType = GdsExpressionTypeInference.inferType(expr) ?: return

        if (declaredType.name != exprType.name) {
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Cannot assign a value of type '${exprType.name}' to type '${declaredType.name}'",
                ).range(initializer)
                .create()
        }
    }

    private fun checkUniformInitializerType(
        element: GdsUniformDeclaration,
        holder: AnnotationHolder,
    ) {
        val expr = element.expression ?: return
        val declaredType = GdsDataTypeFactory.createFromUniformDeclaration(element) ?: return
        val exprType = GdsExpressionTypeInference.inferType(expr) ?: return

        if (declaredType.name == exprType.name) return

        // Evaluation failure can also mean an unsupported constant expression.
        val value = GdsConstantEvaluator.evaluate(expr) ?: return
        val convertible =
            when (declaredType) {
                is FloatType if (exprType is IntType || exprType is UIntType) -> true
                is UIntType if exprType is IntType -> {
                    val integer = value as? Int ?: return
                    integer >= 0
                }

                is IntType if exprType is UIntType -> {
                    val integer = value as? Long ?: return
                    integer in 0L..Int.MAX_VALUE.toLong()
                }

                else -> false
            }

        if (!convertible) {
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Cannot assign a value of type '${exprType.name}' to type '${declaredType.name}'",
                ).range(expr)
                .create()
        }
    }

    // === Include line checks ===

    private fun checkIncludeLine(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        val includePath = GdsPathUtil.findIncludePath(element) ?: return
        val path = includePath.path
        val pathRange = includePath.rangeInElement.shiftRight(element.textRange.startOffset)

        if (!path.endsWith(".gdshaderinc")) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Only '.gdshaderinc' files can be included")
                .range(pathRange)
                .create()
            return
        }

        val resolved = GdsPathUtil.resolvePath(element.containingFile, path)
        if (resolved == null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Cannot resolve file '$path'")
                .range(pathRange)
                .create()
            return
        }

        checkIncludedRedefinition(element, pathRange, holder)
    }

    private fun checkIncludedRedefinition(
        element: PsiElement,
        pathRange: TextRange,
        holder: AnnotationHolder,
    ) {
        val redefined =
            GdsIncludeManager
                .getFilesIncludedBy(element)
                .asSequence()
                .flatMap { collectTopLevelDeclarations(it) }
                .firstOrNull { isRedefinedAt(it, element) } ?: return

        holder
            .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '${redefined.name}' in included file")
            .range(pathRange)
            .create()
    }

    private fun collectTopLevelDeclarations(file: GdsFile): List<PsiNamedElement> {
        val result = mutableListOf<PsiNamedElement>()
        file.processOwnDeclarations(
            GdsScopeProcessor(PsiNamedElement::class.java, 0, null) {
                result += it
                true
            },
            ResolveState.initial(),
        )
        return result
    }

    private fun isRedefinedAt(
        declaration: PsiNamedElement,
        position: PsiElement,
    ): Boolean {
        val name = declaration.name ?: return false
        return when (declaration) {
            is GdsFunctionNameDecl -> hasConflictingFunction(declaration, position)
            is GdsStructNameDecl -> hasConflictingStruct(name, position)
            is GdsVariable -> hasConflictingVariable(name, position)
            else -> false
        }
    }
}

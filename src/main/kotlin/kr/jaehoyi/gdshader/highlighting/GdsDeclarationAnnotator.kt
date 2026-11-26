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
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.acceptsValueOf
import kr.jaehoyi.gdshader.psi.GdsBlockBody
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsConstantReducer
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpression
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsInitializer
import kr.jaehoyi.gdshader.psi.GdsInitializerList
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructMember
import kr.jaehoyi.gdshader.psi.GdsStructMemberDeclarator
import kr.jaehoyi.gdshader.psi.GdsStructMemberList
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives
import kr.jaehoyi.gdshader.resolve.GdsIncludeManager
import kr.jaehoyi.gdshader.resolve.GdsPathUtil
import kr.jaehoyi.gdshader.resolve.GdsPreprocessorDefinitions
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
            element is GdsFunctionDeclaration -> {
                checkDuplicateFunction(element, holder)
                checkReturnArraySize(element, holder)
            }
            element is GdsUniformDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder, checkBuiltins = true)
                checkUniformInitializerType(element, holder)
            }
            element is GdsVaryingDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder, checkBuiltins = true)
            }
            element is GdsParameter -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder)
            }
            element is GdsStructMemberDeclarator -> {
                checkSplitDoubleArraySize(element, holder)
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
                    checkDuplicateVariable(element.variableNameDecl, holder, checkBuiltins = true)
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
            element.node.elementType in GdsConditionalDirectives.DIRECTIVES -> checkDirective(element, holder)
        }
    }

    // === Preprocessor directives ===

    private fun checkDirective(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        val file = element.containingFile ?: return
        val analysis = GdsConditionalDirectives.getAnalysis(file)
        val offset = element.textRange.startOffset
        val message =
            analysis.problems[offset]
                ?: when (element.node.elementType) {
                    GdsTypes.PP_ERROR_LINE ->
                        if (analysis.statusAt(offset) == GdsConditionalDirectives.Status.ACTIVE) errorDirectiveMessage(element) else null
                    GdsTypes.PP_DEFINE_LINE -> if (isMacroRedefinition(element)) "Macro redefinition." else null
                    GdsTypes.PP_UNDEF_LINE ->
                        if (UNDEF_NAME_REGEX.find(element.text)?.groupValues?.get(1) in GdsConditionalDirectives.BUILTIN_DEFINES) {
                            "Cannot use 'undef' on built-in define."
                        } else {
                            null
                        }
                    else -> null
                } ?: return

        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private fun errorDirectiveMessage(element: PsiElement): String =
        element.text
            .removePrefix("#error")
            .trim()
            .ifEmpty { "#error" }

    private fun isMacroRedefinition(element: PsiElement): Boolean {
        val macro = GdsPreprocessorDefinitions.parseMacro(element) ?: return false
        if (macro.name in GdsConditionalDirectives.BUILTIN_DEFINES) return true
        return GdsPreprocessorDefinitions.getVisibleMacros(element).any { other ->
            other.name == macro.name && other.element != element && !GdsConditionalDirectives.areExclusive(other.element, element)
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

    private fun checkReturnArraySize(
        element: GdsFunctionDeclaration,
        holder: AnnotationHolder,
    ) {
        val arraySize = element.arraySize ?: return
        if (arraySize.expression != null) return
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Unknown array size is forbidden in that context.")
            .range(arraySize)
            .create()
    }

    private fun checkDuplicateFunction(
        element: GdsFunctionDeclaration,
        holder: AnnotationHolder,
    ) {
        val nameDecl = element.functionNameDecl
        if (hasConflictingFunction(nameDecl, nameDecl) || isBuiltinName(nameDecl.name, nameDecl, includeFunctions = true)) {
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
            if (GdsConditionalDirectives.areExclusive(function, nameDecl)) return@processFunctionDeclaration true
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
            found = struct.name == name && !GdsConditionalDirectives.areExclusive(struct, position)
            !found
        }
        return found
    }

    private fun checkDuplicateStructMember(
        element: GdsStructMemberDeclarator,
        holder: AnnotationHolder,
    ) {
        val nameDecl = element.structMemberNameDecl
        val name = nameDecl.text
        val memberList = element.parent?.parent as? GdsStructMemberList ?: return
        val first =
            memberList.structMemberList
                .flatMap { it.structMemberDeclaratorList }
                .firstOrNull { it.structMemberNameDecl.text == name && !GdsConditionalDirectives.areExclusive(it, element) } ?: return
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
        checkBuiltins: Boolean = false,
    ) {
        if (nameDecl == null) return
        val name = nameDecl.name
        if (hasConflictingVariable(name, nameDecl) || (checkBuiltins && isBuiltinName(name, nameDecl, includeFunctions = false))) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '$name'")
                .range(nameDecl)
                .create()
        }
    }

    // Godot reserves the built-in variables of every processing function of the shader type in the
    // global scope, and the built-in function names for functions.
    private fun isBuiltinName(
        name: String,
        position: PsiElement,
        includeFunctions: Boolean,
    ): Boolean {
        val shaderType = GdsPsiImplUtil.getShaderType(position) ?: return false
        return name in Builtins.getVariableNames(shaderType) || (includeFunctions && name in Builtins.ENGINE_FUNCTION_NAMES)
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
            if (!GdsConditionalDirectives.areExclusive(element, position)) result += element
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
                is GdsStructMemberDeclarator -> element.arraySize
                else -> return
            } ?: return

        val parentArraySize =
            when (val parentDecl = element.parent?.parent) {
                is GdsLocalVariableDeclaration -> parentDecl.arraySize
                is GdsConstantDeclaration -> parentDecl.arraySize
                else -> (element.parent as? GdsStructMember)?.arraySize
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
        initializer.initializerList?.let {
            checkInitializerListElements(it, declaredType, holder)
            return
        }
        val expr = initializer.expression ?: return
        val exprType = GdsExpressionTypeInference.inferType(expr) ?: return

        val constructorSize = unsizedArrayConstructorSize(expr)
        if (constructorSize != null && checkArraySizeMatch(declaredType, constructorSize, initializer, holder)) return

        if (!declaredType.acceptsValueOf(exprType)) {
            holder
                .newAnnotation(
                    HighlightSeverity.ERROR,
                    "Cannot assign a value of type '${exprType.name}' to type '${declaredType.name}'",
                ).range(initializer)
                .create()
        }
    }

    private fun checkInitializerListElements(
        list: GdsInitializerList,
        declaredType: DataType,
        holder: AnnotationHolder,
    ) {
        val elementType = (declaredType as? ArrayType)?.elementType ?: return
        if (checkArraySizeMatch(declaredType, list.expressionList.size, list, holder)) return
        for (expression in list.expressionList) {
            val type = GdsExpressionTypeInference.inferType(expression) ?: continue
            if (elementType.acceptsValueOf(type)) continue
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Cannot assign a value of type '${type.name}' to type '${elementType.name}'")
                .range(expression)
                .create()
        }
    }

    private fun checkArraySizeMatch(
        declaredType: DataType,
        size: Int,
        element: PsiElement,
        holder: AnnotationHolder,
    ): Boolean {
        val expected = (declaredType as? ArrayType)?.containerSize ?: return false
        if (expected == size) return false
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Array size mismatch. Expected $expected elements (found $size).")
            .range(element)
            .create()
        return true
    }

    private fun unsizedArrayConstructorSize(expression: GdsExpression): Int? {
        val call = PsiTreeUtil.findChildOfType(expression, GdsFunctionCall::class.java) ?: return null
        if (call.textRange != expression.textRange || call.arraySize?.expression != null || call.arraySize == null) return null
        return call.argumentList?.initializerList?.size ?: 0
    }

    private fun checkUniformInitializerType(
        element: GdsUniformDeclaration,
        holder: AnnotationHolder,
    ) {
        val expr = element.expression ?: return
        // Godot rejects defaults on uniform arrays before looking at the value.
        if (element.arraySizeList.isNotEmpty()) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Setting default values to uniform arrays is not supported.")
                .range(expr)
                .create()
            return
        }
        val declaredType = GdsDataTypeFactory.createFromUniformDeclaration(element) ?: return
        // An expression without a known type is invalid for another reason.
        GdsExpressionTypeInference.inferType(expr) ?: return

        val message =
            when (val reduced = GdsConstantReducer.reduce(expr)) {
                GdsConstantReducer.Unknown -> return
                GdsConstantReducer.NotConstant -> "Expected constant expression after '='."
                is GdsConstantReducer.Constant -> {
                    if (GdsConstantReducer.convertConstant(reduced, declaredType)) return
                    "Can't convert constant to '${declaredType.name}'."
                }
            }

        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(expr)
            .create()
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

    private companion object {
        val UNDEF_NAME_REGEX = Regex("""^\s*#undef\s+([A-Za-z_][A-Za-z0-9_]*)""")
    }
}

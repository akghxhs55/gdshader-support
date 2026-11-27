package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.ResolveState
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.SamplerType
import kr.jaehoyi.gdshader.model.StructType
import kr.jaehoyi.gdshader.model.VoidType
import kr.jaehoyi.gdshader.model.acceptsValueOf
import kr.jaehoyi.gdshader.psi.GdsArraySize
import kr.jaehoyi.gdshader.psi.GdsBlockBody
import kr.jaehoyi.gdshader.psi.GdsCaseBody
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaratorList
import kr.jaehoyi.gdshader.psi.GdsConstantReducer
import kr.jaehoyi.gdshader.psi.GdsDataTypeFactory
import kr.jaehoyi.gdshader.psi.GdsExpression
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsForInit
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsFunctionNameRef
import kr.jaehoyi.gdshader.psi.GdsInitializer
import kr.jaehoyi.gdshader.psi.GdsInitializerList
import kr.jaehoyi.gdshader.psi.GdsItem
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsPrecision
import kr.jaehoyi.gdshader.psi.GdsPrimitiveType
import kr.jaehoyi.gdshader.psi.GdsStatementBody
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructMember
import kr.jaehoyi.gdshader.psi.GdsStructMemberDeclarator
import kr.jaehoyi.gdshader.psi.GdsStructMemberList
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsType
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameRef
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsMacroElement
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives
import kr.jaehoyi.gdshader.resolve.GdsIncludeManager
import kr.jaehoyi.gdshader.resolve.GdsMacroExpansion
import kr.jaehoyi.gdshader.resolve.GdsPathUtil
import kr.jaehoyi.gdshader.resolve.GdsPreprocessorDefinitions
import kr.jaehoyi.gdshader.resolve.GdsResolver
import kr.jaehoyi.gdshader.resolve.GdsScopeProcessor

class GdsDeclarationAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.DECLARATION_VALIDATION)) return

        checkBuiltinDefineName(element, holder)

        when {
            element is GdsItem -> checkShaderTypeFirst(element, holder)
            element is GdsType -> {
                checkSamplerType(element, holder)
                checkStructType(element, holder)
                checkVoidVariableType(element, holder)
            }
            element is GdsStructDeclaration -> {
                checkEmptyStruct(element, holder)
                checkDuplicateStruct(element, holder)
            }
            element is GdsFunctionDeclaration -> {
                val isRedefinition = checkDuplicateFunction(element, holder)
                checkReturnArraySize(element, holder)
                if (!isRedefinition) checkProcessingFunctionSignature(element, holder)
            }
            element is GdsUniformDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder, checkBuiltins = true, checkFunctions = true)
                checkUniformInitializerType(element, holder)
            }
            element is GdsVaryingDeclaration -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkDuplicateVariable(element.variableNameDecl, holder, checkBuiltins = true, checkFunctions = true)
            }
            element is GdsParameter -> {
                checkDoubleArraySize(element.arraySizeList, holder)
                checkParameterArraySize(element, holder)
                checkDuplicateVariable(element.variableNameDecl, holder)
            }
            element is GdsStructMemberDeclarator -> {
                checkSplitDoubleArraySize(element, holder)
                checkDuplicateStructMember(element, holder)
            }
            element is GdsLocalVariableDeclarator -> {
                checkSplitDoubleArraySize(element, holder)
                checkDuplicateLocalVariable(element.variableNameDecl, holder)
                val declaration = element.parent?.parent
                val declaredType =
                    GdsDataTypeFactory.createFromLocalVariableDeclaration(element)
                        ?: GdsDataTypeFactory.createFromForInit(element)
                val context =
                    ArrayInitializerContext(
                        initializer = element.initializer,
                        initializerPrecision = element.precision,
                        arraySize = element.arraySize ?: (declaration as? GdsLocalVariableDeclaration)?.arraySize,
                        declarationPrecision =
                            (declaration as? GdsLocalVariableDeclaration)?.precision ?: (declaration as? GdsForInit)?.precision,
                        declaredType = declaredType,
                        allowsExpression = true,
                    )
                // A void declaration is already reported on its type.
                if (declaredType !is VoidType && !checkArrayInitializerStart(context, holder)) {
                    checkInitializerType(element.initializer, declaredType, holder)
                }
            }
            element is GdsConstantDeclarator -> {
                checkSplitDoubleArraySize(element, holder)
                val isLocal = element.parentOfType<GdsFunctionDeclaration>() != null
                if (isLocal) {
                    checkDuplicateLocalVariable(element.variableNameDecl, holder)
                } else {
                    // Godot skips functions only for the first name of a global constant declaration.
                    val isFirstDeclarator = (element.parent as? GdsConstantDeclaratorList)?.constantDeclaratorList?.firstOrNull() === element
                    checkDuplicateVariable(element.variableNameDecl, holder, checkBuiltins = true, checkFunctions = !isFirstDeclarator)
                }
                val declaration = element.parent?.parent as? GdsConstantDeclaration
                val declaredType = GdsDataTypeFactory.createFromConstantDeclaration(element)
                val context =
                    ArrayInitializerContext(
                        initializer = element.initializer,
                        initializerPrecision = element.precision,
                        arraySize = element.arraySize ?: declaration?.arraySize,
                        declarationPrecision = declaration?.precision,
                        declaredType = declaredType,
                        allowsExpression = isLocal,
                    )
                if (!checkArrayInitializerStart(context, holder)) {
                    checkInitializerType(element.initializer, declaredType, holder)
                }
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

    // Godot's preprocessor replaces the name with a number before parsing, so the parser error of each position is reported.
    private fun checkBuiltinDefineName(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (element.text !in GdsConditionalDirectives.BUILTIN_DEFINES) return
        val message =
            when (element) {
                is GdsFunctionNameDecl -> "Expected a function name after type."
                is GdsStructNameDecl -> "Expected a struct identifier."
                is GdsStructMemberNameDecl -> "Expected an identifier or '['."
                is GdsStructMemberNameRef -> "Expected an identifier as a member."
                is GdsVariableNameDecl ->
                    when (element.parent) {
                        is GdsUniformDeclaration, is GdsVaryingDeclaration -> "Expected an identifier or '['."
                        is GdsParameter -> "Expected an identifier for argument name."
                        else -> "Expected an identifier or '[' after type."
                    }
                else -> return
            }
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
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

    private fun checkSamplerType(
        element: GdsType,
        holder: AnnotationHolder,
    ) {
        val message =
            when (val parent = element.parent) {
                is GdsFunctionDeclaration -> "Invalid function type (samplers are not allowed)."
                is GdsParameter -> {
                    val qualifier = parent.parameterQualifier?.node ?: return
                    if (qualifier.findChildByType(OUTPUT_QUALIFIERS) == null) return
                    "Opaque types cannot be output parameters."
                }
                is GdsLocalVariableDeclaration, is GdsForInit -> INVALID_VARIABLE_TYPE
                is GdsConstantDeclaration ->
                    if (parent.parentOfType<GdsFunctionDeclaration>() != null) {
                        INVALID_VARIABLE_TYPE
                    } else {
                        "Invalid constant type (samplers are not allowed)."
                    }
                is GdsStructMember -> null
                else -> return
            }
        val type = GdsDataTypeFactory.createFromType(element) as? SamplerType ?: return
        holder
            .newAnnotation(HighlightSeverity.ERROR, message ?: "A '${type.name}' data type is not allowed here.")
            .range(element)
            .create()
    }

    private fun checkStructType(
        element: GdsType,
        holder: AnnotationHolder,
    ) {
        val message =
            when (element.parent) {
                is GdsUniformDeclaration -> "The 'struct' data type is not supported for uniforms."
                is GdsVaryingDeclaration -> "The 'struct' data type is not allowed here."
                else -> return
            }
        if (GdsDataTypeFactory.createFromType(element) !is StructType) return
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private fun checkVoidVariableType(
        element: GdsType,
        holder: AnnotationHolder,
    ) {
        val message =
            when (element.parent) {
                // Godot does not treat 'void' as a variable type, so it parses the declaration as an expression.
                is GdsLocalVariableDeclaration, is GdsForInit -> "Void value not allowed in expression."
                is GdsParameter -> "Void type not allowed as argument."
                else -> return
            }
        if (GdsDataTypeFactory.createFromType(element) !is VoidType) return
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
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

    private fun checkParameterArraySize(
        element: GdsParameter,
        holder: AnnotationHolder,
    ) {
        val arraySize = element.arraySizeList.firstOrNull { it.expression == null } ?: return
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Unknown array size is forbidden in that context.")
            .range(arraySize)
            .create()
    }

    private fun checkProcessingFunctionSignature(
        element: GdsFunctionDeclaration,
        holder: AnnotationHolder,
    ) {
        val name = element.functionNameDecl.name
        if (!Builtins.isProcessingFunction(name, GdsPsiImplUtil.getShaderType(element))) return
        val parameterList = element.parameterList
        if (parameterList != null && parameterList.parameterList.isNotEmpty()) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Function '$name' expects no arguments.")
                .range(parameterList)
                .create()
        } else if (element.type.text != "void" || element.arraySize != null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Function '$name' must be of 'void' return type.")
                .range(element.type)
                .create()
        }
    }

    private fun checkDuplicateFunction(
        element: GdsFunctionDeclaration,
        holder: AnnotationHolder,
    ): Boolean {
        val nameDecl = element.functionNameDecl
        val name = nameDecl.name
        if (name in GdsConditionalDirectives.BUILTIN_DEFINES) return false
        val isRedefinition =
            hasConflictingFunction(nameDecl, nameDecl) ||
                isBuiltinName(name, nameDecl, includeFunctions = true) ||
                hasConflictingStruct(name, nameDecl) ||
                hasConflictingVariable(name, nameDecl)
        if (!isRedefinition) return false
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Redefinition of '${nameDecl.name}'")
            .range(nameDecl)
            .create()
        return true
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
        checkFunctions: Boolean = false,
    ) {
        if (nameDecl == null) return
        val name = nameDecl.name
        if (hasConflictingVariable(name, nameDecl) ||
            (checkBuiltins && isBuiltinName(name, nameDecl, includeFunctions = false)) ||
            (checkFunctions && hasConflictingCallableFunction(name, nameDecl))
        ) {
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

    private fun hasConflictingCallableFunction(
        name: String,
        position: PsiElement,
    ): Boolean {
        if (name in GdsConditionalDirectives.BUILTIN_DEFINES || isProcessingFunction(name, position)) return false
        var found = false
        GdsResolver.processFunctionDeclaration(position) { function ->
            found = function is GdsFunctionNameDecl && function.name == name && !GdsConditionalDirectives.areExclusive(function, position)
            !found
        }
        return found
    }

    private fun checkDuplicateLocalVariable(
        nameDecl: GdsVariableNameDecl?,
        holder: AnnotationHolder,
    ) {
        if (nameDecl == null) return
        val name = nameDecl.name
        val myScope = localScope(nameDecl)

        val hasDuplicate =
            collectVisibleVariables(nameDecl)
                .filter { it.name == name }
                .any { element ->
                    when (element) {
                        is GdsVariableNameDecl -> {
                            when (val parent = element.parent) {
                                is GdsLocalVariableDeclarator, is GdsConstantDeclarator -> localScope(element) === myScope
                                is GdsParameter -> myScope?.parent?.parent is GdsFunctionDeclaration
                                else -> true
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

    private fun localScope(element: PsiElement): PsiElement? {
        var current = element.parent
        while (current != null) {
            when (current) {
                is GdsBlockBody, is GdsForInit -> return current
                // Statements of a block or a case are statement bodies too, but they share the enclosing scope.
                is GdsStatementBody -> if (current.parent !is GdsBlockBody && current.parent !is GdsCaseBody) return current
            }
            current = current.parent
        }
        return null
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

    private fun checkArrayInitializerStart(
        declaration: ArrayInitializerContext,
        holder: AnnotationHolder,
    ): Boolean {
        val initializer = declaration.initializer ?: return false
        val precision = declaration.initializerPrecision
        val arraySize = declaration.arraySize
        if (arraySize == null) {
            precision ?: return false
            return report(precision, "Expected expression, found: '${precision.text}'.", holder)
        }
        val isUnsized = arraySize.expression == null
        if (isUnsized && precision != null) return report(initializer, "Expected a '{'.", holder, from = precision)
        if (initializer.initializerList != null) {
            precision ?: return false
            return report(initializer, "Expected data type after precision modifier.", holder, from = precision)
        }
        val first = firstExpandedToken(initializer) ?: return false

        val isIdentifier = first.node.elementType == GdsTypes.IDENTIFIER && first.text !in GdsConditionalDirectives.BUILTIN_DEFINES
        val message =
            when {
                precision != null -> constructorPrecisionError(declaration, first) ?: return false
                isIdentifier && declaration.allowsExpression -> return false
                isIdentifier && !isUnsized && GdsDataTypeFactory.createFromStructName(first, first.text) != null -> return false
                !isUnsized && first.parent is GdsPrimitiveType -> constructorPrecisionError(declaration, first) ?: return false
                isUnsized -> "Expected a '{'."
                else -> "Invalid data type for the array."
            }
        return report(initializer, message, holder, from = precision)
    }

    // In the `precision type[N](...)` form, Godot requires the same precision as the declaration.
    private fun constructorPrecisionError(
        declaration: ArrayInitializerContext,
        first: PsiElement,
    ): String? {
        val precision = declaration.initializerPrecision
        if (precision != null) {
            if (first.parent !is GdsPrimitiveType) return "Expected data type after precision modifier."
            if (first.text in BOOLEAN_TYPE_NAMES) return "The precision modifier cannot be used on boolean types."
        }
        if (precision?.text == declaration.declarationPrecision?.text) return null

        val expression = declaration.initializer?.expression
        val constructed = expression?.let(GdsExpressionTypeInference::inferType)?.name ?: first.text
        val declared = declaration.declaredType?.name ?: return null
        val from = listOfNotNull(precision?.text, constructed).joinToString(" ")
        val to = listOfNotNull(declaration.declarationPrecision?.text, declared).joinToString(" ")
        return "Cannot convert from '$from' to '$to'."
    }

    private fun report(
        element: PsiElement,
        message: String,
        holder: AnnotationHolder,
        from: PsiElement? = null,
    ): Boolean {
        val range = from?.let { TextRange(it.textRange.startOffset, element.textRange.endOffset) } ?: element.textRange
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(range)
            .create()
        return true
    }

    private class ArrayInitializerContext(
        val initializer: GdsInitializer?,
        val initializerPrecision: GdsPrecision?,
        val arraySize: GdsArraySize?,
        val declarationPrecision: GdsPrecision?,
        val declaredType: DataType?,
        val allowsExpression: Boolean,
    )

    private fun firstExpandedToken(initializer: GdsInitializer): PsiElement? {
        var first = PsiTreeUtil.getDeepestFirst(initializer)
        repeat(MAX_MACRO_EXPANSION_DEPTH) {
            val site =
                when (val parent = first.parent) {
                    is GdsVariableNameRef -> parent.takeIf { it.reference.resolve() is GdsMacroElement }
                    is GdsFunctionNameRef -> (parent.parent as? GdsFunctionCall)?.takeIf { parent.reference.resolve() is GdsMacroElement }
                    else -> null
                } ?: return first
            first = PsiTreeUtil.getDeepestFirst(GdsMacroExpansion.expand(site) ?: return null)
        }
        return null
    }

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
        const val INVALID_VARIABLE_TYPE = "Invalid variable type (samplers are not allowed)."
        const val MAX_MACRO_EXPANSION_DEPTH = 16
        val BOOLEAN_TYPE_NAMES = setOf("bool", "bvec2", "bvec3", "bvec4")
        val OUTPUT_QUALIFIERS = TokenSet.create(GdsTypes.ARG_OUT, GdsTypes.ARG_INOUT)
        val UNDEF_NAME_REGEX = Regex("""^\s*#undef\s+([A-Za-z_][A-Za-z0-9_]*)""")
    }
}

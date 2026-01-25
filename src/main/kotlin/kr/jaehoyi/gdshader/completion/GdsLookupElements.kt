package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.AddSpaceInsertHandler
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import kr.jaehoyi.gdshader.GdsIcons
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsVariableNameDecl

object GdsLookupElements {
    
    private const val PRIORITY_HIGHEST = 100.0
    private const val PRIORITY_HIGH = 90.0
    private const val PRIORITY_NORMAL = 50.0
    private const val PRIORITY_BUILTIN = 20.0
    private const val PRIORITY_TYPE = 10.0
    private const val PRIORITY_LOW = 0.0
    
    val SHADER_TYPE_KEYWORD = LookupElementBuilder.create("shader_type")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))

    val RENDER_MODE_KEYWORD = LookupElementBuilder.create("render_mode")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))

    val STENCIL_MODE_KEYWORD = LookupElementBuilder.create("stencil_mode")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))

    val UNIFORM_GROUP_KEYWORD = LookupElementBuilder.create("group_uniforms")
        .withBoldness(true)

    val GLOBAL_KEYWORD = LookupElementBuilder.create("global")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))

    val INSTANCE_KEYWORD = LookupElementBuilder.create("instance")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))

    val UNIFORM_KEYWORD = LookupElementBuilder.create("uniform")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val CONST_KEYWORD = LookupElementBuilder.create("const")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))

    val VARYING_KEYWORD = LookupElementBuilder.create("varying")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))

    val STRUCT_KEYWORD = LookupElementBuilder.create("struct")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val CONTROL_STATEMENT_STARTERS = listOf("if", "for", "while", "switch").map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler { context, _ ->
                val offset = context.tailOffset

                context.document.insertString(offset, " ()")
                context.editor.caretModel.moveToOffset(offset + 2)
            }
            .withPriority(PRIORITY_HIGH)
    }
    
    val DO_KEYWORD = LookupElementBuilder.create("do")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
        .withPriority(PRIORITY_HIGH)
    
    val ELSE_KEYWORD = LookupElementBuilder.create("else")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
        .withPriority(PRIORITY_HIGH)
    
    val RETURN_KEYWORD = LookupElementBuilder.create("return")
        .withBoldness(true)
        .withPriority(PRIORITY_HIGH)
    
    val DISCARD_KEYWORD = LookupElementBuilder.create("discard")
        .withBoldness(true)
        .withInsertHandler(GdsSemicolonInsertHandler)
        .withPriority(PRIORITY_HIGH)
    
    val BREAK_KEYWORD = LookupElementBuilder.create("break")
        .withBoldness(true)
        .withInsertHandler(GdsSemicolonInsertHandler)
        .withPriority(PRIORITY_HIGH)
    
    val CONTINUE_KEYWORD = LookupElementBuilder.create("continue")
        .withBoldness(true)
        .withInsertHandler(GdsSemicolonInsertHandler)
        .withPriority(PRIORITY_HIGH)
    
    val SHADER_TYPES = GdsKeywords.SHADER_TYPES.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
            .withInsertHandler(GdsSemicolonInsertHandler)
    }

    val RENDER_MODES = GdsKeywords.RENDER_MODES.values.flatten().map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
    }
    
    val STENCIL_MODES = GdsKeywords.STENCIL_MODES.values.flatten().map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
    }
    
    val PRECISIONS = GdsKeywords.PRECISIONS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_TYPE)
    }
    
    val DECLARABLE_BUILTIN_TYPES = Builtins.ALL_DATA_TYPE_LIST.filter { it.isDeclarable }.map { 
        LookupElementBuilder.create(it.presentationText)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Type)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_TYPE)
    }

    val OPAQUE_BUILTIN_TYPES = Builtins.ALL_DATA_TYPE_LIST.filter { it.isOpaque }.map {
        LookupElementBuilder.create(it.presentationText)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Type)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_TYPE)
    }
    
    val RETURNABLE_BUILTIN_TYPES = Builtins.ALL_DATA_TYPE_LIST.filter { it.isValidReturnType }.map { 
        LookupElementBuilder.create(it.presentationText)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Type)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_TYPE)
    }
    
    val BUILTIN_FUNCTIONS = Builtins.FUNCTIONS.mapValues {
        it.value.map { functionSpec ->
            LookupElementBuilder.create(functionSpec.name)
                .withBoldness(true)
                .withIcon(AllIcons.Nodes.Function)
                .appendTailText(
                    "(" + functionSpec.parameters.joinToString(", ") { param -> "${param.presentationTypeText} ${param.name}" } + ")",
                    true
                )
                .withTypeText(functionSpec.returnType.presentationText, true)
                .withInsertHandler(if (functionSpec.parameters.isEmpty()) ParenthesesInsertHandler.NO_PARAMETERS else ParenthesesInsertHandler.WITH_PARAMETERS)
                .withPriority(PRIORITY_BUILTIN)
        }
    }
    
    val BUILTIN_VARIABLES = Builtins.VARIABLES.mapValues {
        it.value.map { variableSpec ->
            val icon =
                if (variableSpec.isMutable)
                    AllIcons.Nodes.Variable
                else
                    AllIcons.Nodes.Constant
            
            LookupElementBuilder.create(variableSpec.name)
                .withBoldness(true)
                .withIcon(icon)
                .withTypeText(variableSpec.presentationTypeText, true)
                .withPriority(PRIORITY_BUILTIN)
        }
    }
    
    val PROCESSING_FUNCTIONS = Builtins.PROCESSING_FUNCTIONS.mapValues {
        it.value.map { functionContext ->
            LookupElementBuilder.create(functionContext.text)
                .withBoldness(true)
                .withIcon(AllIcons.Nodes.AbstractMethod)
                .appendTailText("()", true)
                .withTypeText("void", true)
                .withInsertHandler { context, _ ->
                    val offset = context.tailOffset
                    val textToInsert = "() {\n    \n}"

                    context.document.insertString(offset, textToInsert)
                    context.editor.caretModel.moveToOffset(offset + textToInsert.length - 2)
                }
                .withPriority(PRIORITY_NORMAL)
        }
    }

    val PROCESSING_FUNCTIONS_WITHOUT_RETURN_TYPE = Builtins.PROCESSING_FUNCTIONS.mapValues {
        it.value.map { functionContext ->
            LookupElementBuilder.create(functionContext.text)
                .withBoldness(true)
                .withIcon(AllIcons.Nodes.Template)
                .withTypeText("template")
                .withInsertHandler { context, element ->
                    val document = context.document
                    val editor = context.editor

                    val startOffset = context.startOffset
                    val tailOffset = context.tailOffset

                    val suffix = "() {\n    \n}"
                    document.insertString(tailOffset, suffix)

                    val prefix = "void "
                    document.insertString(startOffset, prefix)

                    val nameLength = element.lookupString.length
                    val cursorOffset = startOffset + prefix.length + nameLength + suffix.length - 2

                    editor.caretModel.moveToOffset(cursorOffset)
                }
                .withPriority(PRIORITY_NORMAL)
        }
    }
    
    val CONSTRUCTORS = Builtins.ALL_DATA_TYPE_LIST
        .filter { it.isInstantiable }
        .flatMap { type ->
            (type as Instantiable).getConstructors().map { constructorSpec ->
                LookupElementBuilder.create(constructorSpec, constructorSpec.name)
                    .withBoldness(true)
                    .withIcon(AllIcons.Nodes.Function)
                    .appendTailText(
                        "(" + constructorSpec.parameters.joinToString(", ") { param -> "${param.presentationTypeText} ${param.name}" } + ")",
                        true
                    )
                    .withInsertHandler(if (constructorSpec.parameters.isEmpty()) ParenthesesInsertHandler.NO_PARAMETERS else ParenthesesInsertHandler.WITH_PARAMETERS)
                    .withPriority(PRIORITY_LOW)
            }
        }
    
    val INTEGER_TYPE_CONSTRUCTORS = Builtins.ALL_DATA_TYPE_LIST
        .filter { it == IntType || it == UIntType }
        .flatMap { type ->
            (type as Instantiable).getConstructors().map { constructorSpec ->
                LookupElementBuilder.create(constructorSpec, constructorSpec.name)
                    .withBoldness(true)
                    .withIcon(AllIcons.Nodes.Function)
                    .appendTailText(
                        "(" + constructorSpec.parameters.joinToString(", ") { param -> "${param.presentationTypeText} ${param.name}" } + ")",
                        true
                    )
                    .withInsertHandler(if (constructorSpec.parameters.isEmpty()) ParenthesesInsertHandler.NO_PARAMETERS else ParenthesesInsertHandler.WITH_PARAMETERS)
                    .withPriority(PRIORITY_LOW)
            }
        }

    val BOOLEAN_LITERALS = GdsKeywords.BOOLEAN_LITERALS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_HIGH)
    }
    
    val UNIFORM_HINTS: Map<DataType, List<LookupElement>>
        = GdsKeywords.UNIFORM_HINTS.mapValues { entry ->
            entry.value.map { 
                when (it) {
                    "hint_enum" ->
                        LookupElementBuilder.create(it)
                            .withBoldness(true)
                            .appendTailText("(String1, String2, ...)", true)
                            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
                    
                    "hint_range" ->
                        LookupElementBuilder.create(it)
                            .withBoldness(true)
                            .appendTailText("(min, max[, step])", true)
                            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
                    
                    "instance_index" ->
                        LookupElementBuilder.create(it)
                            .withBoldness(true)
                            .appendTailText("(index)", true)
                            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
                    
                    else ->
                        LookupElementBuilder.create(it)
                            .withBoldness(true)
                }
            }
        }
    
    val INTERPOLATIONS = GdsKeywords.INTERPOLATIONS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_TYPE)
    }
    
    val IN_KEYWORD = LookupElementBuilder.create("in")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
        .withPriority(PRIORITY_LOW)
    
    val PARAMETER_QUALIFIERS = GdsKeywords.PARAMETER_QUALIFIERS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_LOW)
    }
    
    fun createFromVariableNameDecl(nameDecl: GdsVariableNameDecl): LookupElement? {
        val variableSpec = nameDecl.variableSpec ?: return null
        
        val icon = when (variableSpec) {
            is UniformSpec -> AllIcons.Nodes.Gvariable
            is ConstantSpec -> AllIcons.Nodes.Constant
            is VaryingSpec -> AllIcons.Nodes.Field
            is ParameterSpec -> AllIcons.Nodes.Parameter
            is LocalVariableSpec -> AllIcons.Nodes.Variable
        }
        
        val priority = when (variableSpec) {
            is UniformSpec -> PRIORITY_NORMAL
            is ConstantSpec -> PRIORITY_NORMAL
            is VaryingSpec -> PRIORITY_NORMAL
            is ParameterSpec -> PRIORITY_HIGHEST
            is LocalVariableSpec -> PRIORITY_HIGHEST
        }
        
        val builder = LookupElementBuilder.create(nameDecl, variableSpec.name)
            .withBoldness(true)
            .withTypeText(variableSpec.presentationTypeText, true)
            .withIcon(icon)
            .withPriority(priority)
        
        return builder
    }
    
    fun createFromFunctionNameDecl(nameDecl: GdsFunctionNameDecl): LookupElement? {
        val functionSpec = nameDecl.functionSpec ?: return null
        
        val builder = LookupElementBuilder.create(nameDecl, functionSpec.name)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Function)
            .appendTailText(
                "(" + functionSpec.parameters.joinToString(", ") { param -> "${param.presentationTypeText} ${param.name}" } + ")",
                true
            )
            .withTypeText(functionSpec.returnType.presentationText, true)
            .withInsertHandler(if (functionSpec.parameters.isEmpty()) ParenthesesInsertHandler.NO_PARAMETERS else ParenthesesInsertHandler.WITH_PARAMETERS)
            .withPriority(PRIORITY_NORMAL)
        
        return builder
    }
    
    fun createTypeDeclarationFromStructNameDecl(nameDecl: GdsStructNameDecl): LookupElement {
        val structName = nameDecl.name
        
        val builder = LookupElementBuilder.create(nameDecl, structName)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Class)
            .withInsertHandler(AddSpaceInsertHandler(true))
            .withPriority(PRIORITY_TYPE)
        
        return builder
    }
    
    fun createConstructorFromStructNameDecl(nameDecl: GdsStructNameDecl): LookupElement {
        val structName = nameDecl.name
        
        val builder = LookupElementBuilder.create(nameDecl, structName)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Function)
            .appendTailText("(...)", true)
            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
            .withPriority(PRIORITY_LOW)
        
        return builder
    }
    
    fun createFromIncludeFilePath(includeFilePath: String): LookupElement {
        val fileName = includeFilePath.substringAfterLast('/')
        
        val builder = LookupElementBuilder.create(includeFilePath)
            .withPresentableText(fileName)
            .withTypeText(includeFilePath.substringAfterLast("res://"), true)
            .withIcon(GdsIcons.GDSHADERINC)
            .withCaseSensitivity(false)
        
        return builder
    }
    
    private fun LookupElement.withPriority(priority: Double): LookupElement =
        PrioritizedLookupElement.withPriority(this, priority)
    
}
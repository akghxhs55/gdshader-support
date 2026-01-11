package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.AddSpaceInsertHandler
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.LocalVariableSpec
import kr.jaehoyi.gdshader.model.ParameterSpec
import kr.jaehoyi.gdshader.model.UniformSpec
import kr.jaehoyi.gdshader.model.VaryingSpec
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameDecl
import kr.jaehoyi.gdshader.psi.variableSpec

object GDShaderLookupElements {
    
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
    }
    
    val DO_KEYWORD = LookupElementBuilder.create("do")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val ELSE_KEYWORD = LookupElementBuilder.create("else")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val RETURN_KEYWORD = LookupElementBuilder.create("return")
        .withBoldness(true)
    
    val DISCARD_KEYWORD = LookupElementBuilder.create("discard")
        .withBoldness(true)
        .withInsertHandler(GDShaderSemicolonInsertHandler)
    
    val BREAK_KEYWORD = LookupElementBuilder.create("break")
        .withBoldness(true)
        .withInsertHandler(GDShaderSemicolonInsertHandler)
    
    val CONTINUE_KEYWORD = LookupElementBuilder.create("continue")
        .withBoldness(true)
        .withInsertHandler(GDShaderSemicolonInsertHandler)
    
    val SHADER_TYPES = GDShaderKeywords.SHADER_TYPES.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
            .withInsertHandler(GDShaderSemicolonInsertHandler)
    }

    val RENDER_MODES = GDShaderKeywords.RENDER_MODES.values.flatten().map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
    }
    
    val STENCIL_MODES = GDShaderKeywords.STENCIL_MODES.values.flatten().map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
    }
    
    val PRECISIONS = GDShaderKeywords.PRECISIONS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val DECLARABLE_BUILTIN_TYPES = Builtins.ALL_DATA_TYPE_LIST.filter { it.isDeclarable }.map { 
        LookupElementBuilder.create(it.presentationText)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Type)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }

    val OPAQUE_BUILTIN_TYPES = Builtins.ALL_DATA_TYPE_LIST.filter { it.isOpaque }.map {
        LookupElementBuilder.create(it.presentationText)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Type)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val RETURNABLE_BUILTIN_TYPES = Builtins.ALL_DATA_TYPE_LIST.filter { it.isValidReturnType }.map { 
        LookupElementBuilder.create(it.presentationText)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Type)
            .withInsertHandler(AddSpaceInsertHandler(true))
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
                .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
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
        }
    }
    
    val CONSTRUCTORS = Builtins.ALL_DATA_TYPE_LIST.filter { it.isInstantiable }.map { 
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Function)
            .appendTailText("(...)", true)
            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
    }
    
    val INTEGER_TYPES = listOf(
        LookupElementBuilder.create("int")
            .withBoldness(true),
        LookupElementBuilder.create("uint")
            .withBoldness(true)
    )

    val BOOLEAN_LITERALS = GDShaderKeywords.BOOLEAN_LITERALS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val UNIFORM_HINTS: Map<DataType, List<LookupElement>>
        = GDShaderKeywords.UNIFORM_HINTS.mapValues { entry ->
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
    
    val INTERPOLATIONS = GDShaderKeywords.INTERPOLATIONS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val IN_KEYWORD = LookupElementBuilder.create("in")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val PARAMETER_QUALIFIERS = GDShaderKeywords.PARAMETER_QUALIFIERS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    fun createFromNameDecl(nameDecl: GDShaderVariableNameDecl): LookupElement? {
        val variableSpec = nameDecl.variableSpec ?: return null
        
        val icon = when (variableSpec) {
            is UniformSpec -> AllIcons.Nodes.Gvariable
            is ConstantSpec -> AllIcons.Nodes.Constant
            is VaryingSpec -> AllIcons.Nodes.Field
            is ParameterSpec -> AllIcons.Nodes.Parameter
            is LocalVariableSpec -> AllIcons.Nodes.Variable
        }
        
        val builder = LookupElementBuilder.create(nameDecl, variableSpec.name)
            .withBoldness(true)
            .withTypeText(variableSpec.presentationTypeText, true)
            .withIcon(icon)
        
        return builder
    }
    
}
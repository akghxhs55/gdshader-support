package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.AddSpaceInsertHandler
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import kr.jaehoyi.gdshader.model.GDShaderBuiltins
import kr.jaehoyi.gdshader.model.DataType

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
    
    val CONTROL_STATEMENT_STARTERS = GDShaderKeywords.CONTROL_STATEMENT_STARTERS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
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
    
    val BUILTIN_TYPES = GDShaderKeywords.BUILTIN_TYPES.map { 
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Type)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val BUILTIN_FUNCTIONS = GDShaderBuiltins.GLOBAL_FUNCTIONS.map {
        LookupElementBuilder.create(it.name)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Function)
            .appendTailText(
                "(" + it.parameters.joinToString(", ") { param -> "${param.type} ${param.name}" } + ")", 
                true
            )
            .withTypeText(it.returnType.text, true)
            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
    }
    
    val CONSTRUCTORS = GDShaderKeywords.BUILTIN_TYPES.map { 
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
    
}
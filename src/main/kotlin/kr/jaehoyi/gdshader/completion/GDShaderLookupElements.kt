package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.AddSpaceInsertHandler
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons

object GDShaderLookupElements {
    
    val SHADER_TYPE_KEYWORD = LookupElementBuilder.create("shader_type")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val SHADER_TYPES = GDShaderKeywords.SHADER_TYPES.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
            .withInsertHandler(GDShaderSemicolonInsertHandler)
    }
    
    val RENDER_MODE_KEYWORD = LookupElementBuilder.create("render_mode")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val RENDER_MODES = GDShaderKeywords.RENDER_MODES.values.flatten().map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
    }
    
    val STENCIL_MODE_KEYWORD = LookupElementBuilder.create("stencil_mode")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val STENCIL_MODES = GDShaderKeywords.STENCIL_MODES.values.flatten().map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withIcon(AllIcons.Nodes.Enum)
    }
    
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
    
    val PRECISIONS = GDShaderKeywords.PRECISIONS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val BUILTIN_TYPES = GDShaderKeywords.BUILTIN_TYPES.map { 
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val BOOLEAN_LITERALS = GDShaderKeywords.BOOLEAN_LITERALS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
    val UNIFORM_HINTS = GDShaderKeywords.UNIFORM_HINTS.mapValues { entry ->
        entry.value.map { 
            when (it) {
                "hint_enum" ->
                    LookupElementBuilder.create(it)
                        .withBoldness(true)
                        .withInsertHandler(AddSpaceInsertHandler(true))
                        .appendTailText("(String1, String2, ...)", true)
                
                "hint_range" ->
                    LookupElementBuilder.create(it)
                        .withBoldness(true)
                        .withInsertHandler(AddSpaceInsertHandler(true))
                        .appendTailText("(min, max[, step])", true)
                
                "instance_index" ->
                    LookupElementBuilder.create(it)
                        .withBoldness(true)
                        .withInsertHandler(AddSpaceInsertHandler(true))
                        .appendTailText("(index)", true)
                
                else ->
                    LookupElementBuilder.create(it)
                        .withBoldness(true)
                        .withInsertHandler(AddSpaceInsertHandler(true))
            }
        }
    }
    
    val CONST_KEYWORD = LookupElementBuilder.create("const")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val VARYING_KEYWORD = LookupElementBuilder.create("varying")
        .withBoldness(true)
        .withInsertHandler(AddSpaceInsertHandler(true))
    
    val INTERPOLATIONS = GDShaderKeywords.INTERPOLATIONS.map {
        LookupElementBuilder.create(it)
            .withBoldness(true)
            .withInsertHandler(AddSpaceInsertHandler(true))
    }
    
}
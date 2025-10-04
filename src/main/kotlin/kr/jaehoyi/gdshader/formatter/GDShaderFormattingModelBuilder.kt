package kr.jaehoyi.gdshader.formatter

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import kr.jaehoyi.gdshader.GDShaderLanguage

class GDShaderFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement
        
        val block = GDShaderBlock(
            element.node,
            Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(),
            createSpacingBuilder(codeStyleSettings),
            Indent.getNoneIndent()
        )
        
        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            block,
            codeStyleSettings
        )
    }
    
    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder
        = SpacingBuilder(settings, GDShaderLanguage)
}
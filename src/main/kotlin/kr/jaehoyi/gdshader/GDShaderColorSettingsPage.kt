package kr.jaehoyi.gdshader

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class GDShaderColorSettingsPage : ColorSettingsPage {
    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Identifier", GDShaderSyntaxHighlighter.IDENTIFIER),
            AttributesDescriptor("Keyword", GDShaderSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("Number", GDShaderSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Hint", GDShaderSyntaxHighlighter.HINT),
            AttributesDescriptor("String", GDShaderSyntaxHighlighter.STRING),
            AttributesDescriptor("Block Comment", GDShaderSyntaxHighlighter.BLOCK_COMMENT),
            AttributesDescriptor("Line Comment", GDShaderSyntaxHighlighter.LINE_COMMENT),
            AttributesDescriptor("Operator", GDShaderSyntaxHighlighter.OPERATOR),
            AttributesDescriptor("Colon", GDShaderSyntaxHighlighter.COLON),
            AttributesDescriptor("Curly Bracket", GDShaderSyntaxHighlighter.CURLY_BRACKET),
            AttributesDescriptor("Period", GDShaderSyntaxHighlighter.PERIOD),
            AttributesDescriptor("Semicolon", GDShaderSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Parenthesis", GDShaderSyntaxHighlighter.PARENTHESIS),
            AttributesDescriptor("Bracket", GDShaderSyntaxHighlighter.BRACKET),
            AttributesDescriptor("Constant", GDShaderSyntaxHighlighter.CONSTANT),
            AttributesDescriptor("Type", GDShaderSyntaxHighlighter.TYPE),
            AttributesDescriptor("Preprocessor", GDShaderSyntaxHighlighter.PREPROCESSOR),
        )
    }

    override fun getIcon(): Icon? = GDShaderIcons.FILE
    override fun getHighlighter() = GDShaderSyntaxHighlighter()
    override fun getDemoText(): String = """
        // This is a comment
        shader_type canvas_item;
        
        #define MY_CONSTANT 42

        uniform vec4 color : source_color;
        uniform int name : hint_enum("Option1", "Option2", "Option3");

        void fragment() {
            vec4 texColor = texture(texture, UV);
            COLOR = texColor * color;
            int values[3] = { 1, 2, 3 };
            
            for (int i = 0; i < 10; i++) {
                COLOR.rgb += vec3(float(i) / 10.0);
                if (i % 2 == 0) {
                    continue;
                }
            }
            
            /* This is a block comment
               that spans multiple lines */
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String?, TextAttributesKey?>? = null
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors(): Array<out ColorDescriptor?> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "GDShader"
}
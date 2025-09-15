package kr.jaehoyi.gdshader.highlighter

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import kr.jaehoyi.gdshader.GDShaderIcons
import javax.swing.Icon

class GDShaderColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon = GDShaderIcons.FILE
    override fun getHighlighter() = GDShaderSyntaxHighlighter()
    override fun getDemoText(): String = """
        // This is a comment
        shader_type canvas_item;
        
        #define MY_CONSTANT 42

        uniform vec4 color : source_color;
        uniform int name : hint_enum("Option1", "Option2", "Option3");
        uniform sampler2D image : hint_default_white;
        
        const float offset = 0.5;
        
        varying vec2 uv;
        
        int foo(int a, float b) {
            return a + int(b);
        }
        
        struct MyStruct {
            vec2 position;
            vec3 color;
        };

        void fragment() {
            vec4 texColor = texture(image, UV);
            COLOR = texColor * color;
            int values[3] = { 1, 2, 3 };
            
            for (int i = 0; i < 10; i++) {
                COLOR.rgb += vec3(float(i) / 10.0);
                if (i % 2 == 0) {
                    values[i % 3] += foo(i, 0.5);
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

private val DESCRIPTORS = arrayOf(
    AttributesDescriptor("Identifier", GDShaderSyntaxHighlighter.IDENTIFIER),
    AttributesDescriptor("Keyword", GDShaderSyntaxHighlighter.KEYWORD),
    AttributesDescriptor("Number", GDShaderSyntaxHighlighter.NUMBER),
    AttributesDescriptor("Hint", GDShaderSyntaxHighlighter.UNIFORM_HINT),
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
    AttributesDescriptor("Local Variable", GDShaderSyntaxHighlighter.LOCAL_VARIABLE),
    AttributesDescriptor("Uniform Variable", GDShaderSyntaxHighlighter.UNIFORM_VARIABLE),
    AttributesDescriptor("Varying Variable", GDShaderSyntaxHighlighter.VARYING_VARIABLE),
    AttributesDescriptor("Function", GDShaderSyntaxHighlighter.FUNCTION),
    AttributesDescriptor("Parameter", GDShaderSyntaxHighlighter.PARAMETER),
    AttributesDescriptor("Struct", GDShaderSyntaxHighlighter.STRUCT),
    AttributesDescriptor("Primitive Type", GDShaderSyntaxHighlighter.TYPE),
    AttributesDescriptor("Preprocessor", GDShaderSyntaxHighlighter.PREPROCESSOR),
)
package kr.jaehoyi.gdshader.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import kr.jaehoyi.gdshader.GDShaderIcons
import javax.swing.Icon
import kotlin.collections.mapOf

class GDShaderColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon = GDShaderIcons.GDSHADER
    override fun getHighlighter() = GDShaderSyntaxHighlighter()
    override fun getDemoText(): String = """
        // This is a comment
        shader_type canvas_item;
        
        #define MY_CONSTANT 42

        uniform vec4 <uvar>color</uvar> : source_color;
        uniform int <uvar>name</uvar> : hint_enum("Option1", "Option2", "Option3");
        uniform sampler2D <uvar>image</uvar> : hint_default_white;
        
        const float <const>offset</const> = 0.5;
        
        varying vec2 <vvar>uv</vvar>;
        
        int <func>foo</func>(int <param>a</param>, float <param>b</param>) {
            return <param>a</param> + int(<param>b</param>);
        }
        
        struct <struct>MyStruct</struct> {
            vec2 <member>position</member>;
            vec3 <member>color</member>;
        };

        void <func>fragment</func>() {
            vec4 <lvar>texColor</lvar> = texture(image, UV);
            COLOR = <lvar>texColor</lvar> * <uvar>color</uvar>;
            int <lvar>values</lvar>[3] = { 1, 2, 3 };
            
            for (int <lvar>i</lvar> = 0; i < 10; i++) {
                COLOR.<member>rgb</member> += vec3(float(i) / 10.0);
                if (i % 2 == 0) {
                    <lvar>values</lvar>[i % 3] += <func>foo</func>(i, 0.5);
                    continue;
                }
            }
            
            /* This is a block comment
               that spans multiple lines */
        }
    """.trimIndent()
    
    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = mapOf(
        "lvar" to GDShaderSyntaxHighlighter.LOCAL_VARIABLE,
        "uvar" to GDShaderSyntaxHighlighter.UNIFORM_VARIABLE,
        "const" to GDShaderSyntaxHighlighter.CONSTANT,
        "vvar" to GDShaderSyntaxHighlighter.VARYING_VARIABLE,
        "func" to GDShaderSyntaxHighlighter.FUNCTION,
        "param" to GDShaderSyntaxHighlighter.PARAMETER,
        "struct" to GDShaderSyntaxHighlighter.STRUCT,
        "member" to GDShaderSyntaxHighlighter.STRUCT_MEMBER
    )
    
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors(): Array<out ColorDescriptor?> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "GDShader"
    
}

private val DESCRIPTORS = arrayOf(
    AttributesDescriptor("Identifiers//Local Variable", GDShaderSyntaxHighlighter.LOCAL_VARIABLE),
    AttributesDescriptor("Identifiers//Uniform Variable", GDShaderSyntaxHighlighter.UNIFORM_VARIABLE),
    AttributesDescriptor("Identifiers//Constant", GDShaderSyntaxHighlighter.CONSTANT),
    AttributesDescriptor("Identifiers//Varying Variable", GDShaderSyntaxHighlighter.VARYING_VARIABLE),
    AttributesDescriptor("Identifiers//Function", GDShaderSyntaxHighlighter.FUNCTION),
    AttributesDescriptor("Identifiers//Parameter", GDShaderSyntaxHighlighter.PARAMETER),
    AttributesDescriptor("Identifiers//Struct", GDShaderSyntaxHighlighter.STRUCT),
    AttributesDescriptor("Identifiers//Struct Member", GDShaderSyntaxHighlighter.STRUCT_MEMBER),
    AttributesDescriptor("Keyword", GDShaderSyntaxHighlighter.KEYWORD),
    AttributesDescriptor("Preprocessor", GDShaderSyntaxHighlighter.PREPROCESSOR),
    AttributesDescriptor("Hint", GDShaderSyntaxHighlighter.UNIFORM_HINT),
    AttributesDescriptor("Number", GDShaderSyntaxHighlighter.NUMBER),
    AttributesDescriptor("String", GDShaderSyntaxHighlighter.STRING),
    AttributesDescriptor("Primitive Type", GDShaderSyntaxHighlighter.TYPE),
    AttributesDescriptor("Comments//Line Comment", GDShaderSyntaxHighlighter.LINE_COMMENT),
    AttributesDescriptor("Comments//Block Comment", GDShaderSyntaxHighlighter.BLOCK_COMMENT),
    AttributesDescriptor("Braces and Operators//Operator", GDShaderSyntaxHighlighter.OPERATOR),
    AttributesDescriptor("Braces and Operators//Period", GDShaderSyntaxHighlighter.PERIOD),
    AttributesDescriptor("Braces and Operators//Colon", GDShaderSyntaxHighlighter.COLON),
    AttributesDescriptor("Braces and Operators//Semicolon", GDShaderSyntaxHighlighter.SEMICOLON),
    AttributesDescriptor("Braces and Operators//Parenthesis", GDShaderSyntaxHighlighter.PARENTHESIS),
    AttributesDescriptor("Braces and Operators//Curly Bracket", GDShaderSyntaxHighlighter.CURLY_BRACKET),
    AttributesDescriptor("Braces and Operators//Bracket", GDShaderSyntaxHighlighter.BRACKET),
)
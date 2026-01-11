package kr.jaehoyi.gdshader.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import kr.jaehoyi.gdshader.GdsIcons
import javax.swing.Icon
import kotlin.collections.mapOf

class GdsColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon = GdsIcons.GDSHADER
    override fun getHighlighter() = GdsSyntaxHighlighter()
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
            vec4 <lvar>texColor</lvar> = texture(image, <bconst>UV</bconst>);
            <bvar>COLOR</bvar> = <lvar>texColor</lvar> * <uvar>color</uvar>;
            int <lvar>values</lvar>[3] = { 1, 2, 3 };
            
            for (int <lvar>i</lvar> = 0; <lvar>i</lvar> < 10; <lvar>i</lvar>++) {
                <bvar>COLOR</bvar>.<member>rgb</member> += vec3(float(<lvar>i</lvar>) / 10.0);
                if (<lvar>i</lvar> % 2 == 0) {
                    <lvar>values</lvar>[i % 3] += <func>foo</func>(i, 0.5);
                    continue;
                }
            }
            
            /* This is a block comment
               that spans multiple lines */
        }
    """.trimIndent()
    
    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = mapOf(
        "lvar" to GdsSyntaxHighlighter.LOCAL_VARIABLE,
        "uvar" to GdsSyntaxHighlighter.UNIFORM_VARIABLE,
        "const" to GdsSyntaxHighlighter.CONSTANT,
        "vvar" to GdsSyntaxHighlighter.VARYING_VARIABLE,
        "func" to GdsSyntaxHighlighter.FUNCTION,
        "param" to GdsSyntaxHighlighter.PARAMETER,
        "struct" to GdsSyntaxHighlighter.STRUCT,
        "member" to GdsSyntaxHighlighter.STRUCT_MEMBER,
        "bvar" to GdsSyntaxHighlighter.BUILTIN_VARIABLE,
        "bconst" to GdsSyntaxHighlighter.BUILTIN_CONSTANT,
    )
    
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors(): Array<out ColorDescriptor?> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "GDShader"
    
}

private val DESCRIPTORS = arrayOf(
    AttributesDescriptor("Identifiers//Local Variable", GdsSyntaxHighlighter.LOCAL_VARIABLE),
    AttributesDescriptor("Identifiers//Uniform Variable", GdsSyntaxHighlighter.UNIFORM_VARIABLE),
    AttributesDescriptor("Identifiers//Constant", GdsSyntaxHighlighter.CONSTANT),
    AttributesDescriptor("Identifiers//Varying Variable", GdsSyntaxHighlighter.VARYING_VARIABLE),
    AttributesDescriptor("Identifiers//Function", GdsSyntaxHighlighter.FUNCTION),
    AttributesDescriptor("Identifiers//Parameter", GdsSyntaxHighlighter.PARAMETER),
    AttributesDescriptor("Identifiers//Struct", GdsSyntaxHighlighter.STRUCT),
    AttributesDescriptor("Identifiers//Struct Member", GdsSyntaxHighlighter.STRUCT_MEMBER),
    AttributesDescriptor("Identifiers//Builtin Variable", GdsSyntaxHighlighter.BUILTIN_VARIABLE),
    AttributesDescriptor("Identifiers//Builtin Constant", GdsSyntaxHighlighter.BUILTIN_CONSTANT),
    AttributesDescriptor("Keyword", GdsSyntaxHighlighter.KEYWORD),
    AttributesDescriptor("Preprocessor", GdsSyntaxHighlighter.PREPROCESSOR),
    AttributesDescriptor("Hint", GdsSyntaxHighlighter.UNIFORM_HINT),
    AttributesDescriptor("Number", GdsSyntaxHighlighter.NUMBER),
    AttributesDescriptor("String", GdsSyntaxHighlighter.STRING),
    AttributesDescriptor("Primitive Type", GdsSyntaxHighlighter.TYPE),
    AttributesDescriptor("Comments//Line Comment", GdsSyntaxHighlighter.LINE_COMMENT),
    AttributesDescriptor("Comments//Block Comment", GdsSyntaxHighlighter.BLOCK_COMMENT),
    AttributesDescriptor("Braces and Operators//Operator", GdsSyntaxHighlighter.OPERATOR),
    AttributesDescriptor("Braces and Operators//Period", GdsSyntaxHighlighter.PERIOD),
    AttributesDescriptor("Braces and Operators//Colon", GdsSyntaxHighlighter.COLON),
    AttributesDescriptor("Braces and Operators//Semicolon", GdsSyntaxHighlighter.SEMICOLON),
    AttributesDescriptor("Braces and Operators//Parenthesis", GdsSyntaxHighlighter.PARENTHESIS),
    AttributesDescriptor("Braces and Operators//Curly Bracket", GdsSyntaxHighlighter.CURLY_BRACKET),
    AttributesDescriptor("Braces and Operators//Bracket", GdsSyntaxHighlighter.BRACKET),
)
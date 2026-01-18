@file:Suppress("DialogTitleCapitalization")

package kr.jaehoyi.gdshader.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import kr.jaehoyi.gdshader.GdsBundle
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
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.local.variable"), GdsSyntaxHighlighter.LOCAL_VARIABLE),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.uniform.variable"), GdsSyntaxHighlighter.UNIFORM_VARIABLE),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.constant"), GdsSyntaxHighlighter.CONSTANT),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.varying.variable"), GdsSyntaxHighlighter.VARYING_VARIABLE),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.function"), GdsSyntaxHighlighter.FUNCTION),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.parameter"), GdsSyntaxHighlighter.PARAMETER),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.struct"), GdsSyntaxHighlighter.STRUCT),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.struct.member"), GdsSyntaxHighlighter.STRUCT_MEMBER),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.builtin.variable"), GdsSyntaxHighlighter.BUILTIN_VARIABLE),
    AttributesDescriptor(GdsBundle.message("color.settings.identifiers.builtin.constant"), GdsSyntaxHighlighter.BUILTIN_CONSTANT),
    AttributesDescriptor(GdsBundle.message("color.settings.keyword"), GdsSyntaxHighlighter.KEYWORD),
    AttributesDescriptor(GdsBundle.message("color.settings.preprocessor"), GdsSyntaxHighlighter.PREPROCESSOR),
    AttributesDescriptor(GdsBundle.message("color.settings.hint"), GdsSyntaxHighlighter.UNIFORM_HINT),
    AttributesDescriptor(GdsBundle.message("color.settings.number"), GdsSyntaxHighlighter.NUMBER),
    AttributesDescriptor(GdsBundle.message("color.settings.string"), GdsSyntaxHighlighter.STRING),
    AttributesDescriptor(GdsBundle.message("color.settings.primitive.type"), GdsSyntaxHighlighter.TYPE),
    AttributesDescriptor(GdsBundle.message("color.settings.comments.line"), GdsSyntaxHighlighter.LINE_COMMENT),
    AttributesDescriptor(GdsBundle.message("color.settings.comments.block"), GdsSyntaxHighlighter.BLOCK_COMMENT),
    AttributesDescriptor(GdsBundle.message("color.settings.braces.operator"), GdsSyntaxHighlighter.OPERATOR),
    AttributesDescriptor(GdsBundle.message("color.settings.braces.period"), GdsSyntaxHighlighter.PERIOD),
    AttributesDescriptor(GdsBundle.message("color.settings.braces.colon"), GdsSyntaxHighlighter.COLON),
    AttributesDescriptor(GdsBundle.message("color.settings.braces.semicolon"), GdsSyntaxHighlighter.SEMICOLON),
    AttributesDescriptor(GdsBundle.message("color.settings.braces.parenthesis"), GdsSyntaxHighlighter.PARENTHESIS),
    AttributesDescriptor(GdsBundle.message("color.settings.braces.curly.bracket"), GdsSyntaxHighlighter.CURLY_BRACKET),
    AttributesDescriptor(GdsBundle.message("color.settings.braces.bracket"), GdsSyntaxHighlighter.BRACKET),
)
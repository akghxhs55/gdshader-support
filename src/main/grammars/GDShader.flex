package kr.jaehoyi.gdshader.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import kr.jaehoyi.gdshader.psi.GdsTypes;
import com.intellij.psi.TokenType;

%%

%public
%class GdsLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

Whitespace = [ \t\r\n]+
LineComment = "//"[^\r\n]*
BlockComment = "/*"([^*]|\*+[^*/])*"*/"

Identifier = [a-zA-Z_][a-zA-Z0-9_]*
HexUintConstant = 0[xX][0-9a-fA-F]+[uU]
HexIntConstant = 0[xX][0-9a-fA-F]+
DecimalUintConstant = [0-9]+[uU]
DecimalIntConstant = [0-9]+
FloatConstant =
	([0-9]+\.[0-9]*([eE][+-]?[0-9]+)?[fF]?) |
    (\.[0-9]+([eE][+-]?[0-9]+)?[fF]?) |
    ([0-9]+[eE][+-]?[0-9]+[fF]?) |
    ([0-9]+[fF])
StringConstant = \"([^\\\"\n]|\\.)*\"
UnterminatedStringConstant = \"([^\\\"\n]|\\.)*

PreprocessorLine = ([^\\\r\n]*(\\\\[ \t]*[\r\n])?)*

%state IN_BLOCK_COMMENT

%%

<YYINITIAL> {
	"/*"						{ yybegin(IN_BLOCK_COMMENT); return GdsTypes.BLOCK_COMMENT_START; }

	{LineComment} 				{ return GdsTypes.LINE_COMMENT; }
	
	"true" 						{ return GdsTypes.TRUE; }
	"false" 					{ return GdsTypes.FALSE; }
	{FloatConstant} 			{ return GdsTypes.FLOAT_CONSTANT; }
	{HexUintConstant} 			{ return GdsTypes.UINT_CONSTANT; }
	{HexIntConstant} 			{ return GdsTypes.INT_CONSTANT; }
	{DecimalUintConstant} 		{ return GdsTypes.UINT_CONSTANT; }
	{DecimalIntConstant} 		{ return GdsTypes.INT_CONSTANT; }
	{StringConstant} 			{ return GdsTypes.STRING_CONSTANT; }
	{UnterminatedStringConstant} { return GdsTypes.UNTERMINATED_STRING_CONSTANT; }
	
	"[" 						{ return GdsTypes.BRACKET_OPEN; }
	"]" 						{ return GdsTypes.BRACKET_CLOSE; }
	"{" 						{ return GdsTypes.CURLY_BRACKET_OPEN; }
	"}" 						{ return GdsTypes.CURLY_BRACKET_CLOSE; }
	"(" 						{ return GdsTypes.PARENTHESIS_OPEN; }
	")" 						{ return GdsTypes.PARENTHESIS_CLOSE; }
	"?" 						{ return GdsTypes.QUESTION; }
	"," 						{ return GdsTypes.COMMA; }
	":" 						{ return GdsTypes.COLON; }
	";" 						{ return GdsTypes.SEMICOLON; }
	"." 						{ return GdsTypes.PERIOD; }
	
	"==" 						{ return GdsTypes.OP_EQUAL; }
	"!=" 						{ return GdsTypes.OP_NOT_EQUAL; }
	"<" 						{ return GdsTypes.OP_LESS; }
	"<=" 						{ return GdsTypes.OP_LESS_EQUAL; }
	">" 						{ return GdsTypes.OP_GREATER; }
	">=" 						{ return GdsTypes.OP_GREATER_EQUAL; }
	"&&" 						{ return GdsTypes.OP_AND; }
	"||" 						{ return GdsTypes.OP_OR; }
	"!" 						{ return GdsTypes.OP_NOT; }
	"+" 						{ return GdsTypes.OP_ADD; }
	"-" 						{ return GdsTypes.OP_SUB; }
	"*" 						{ return GdsTypes.OP_MUL; }
	"/" 						{ return GdsTypes.OP_DIV; }
	"%" 						{ return GdsTypes.OP_MOD; }
	"<<" 						{ return GdsTypes.OP_SHIFT_LEFT; }
	">>" 						{ return GdsTypes.OP_SHIFT_RIGHT; }
	"=" 						{ return GdsTypes.OP_ASSIGN; }
	"+=" 						{ return GdsTypes.OP_ASSIGN_ADD; }
	"-=" 						{ return GdsTypes.OP_ASSIGN_SUB; }
	"*=" 						{ return GdsTypes.OP_ASSIGN_MUL; }
	"/=" 						{ return GdsTypes.OP_ASSIGN_DIV; }
	"%=" 						{ return GdsTypes.OP_ASSIGN_MOD; }
	"<<="	 					{ return GdsTypes.OP_ASSIGN_SHIFT_LEFT; }
	">>="	 					{ return GdsTypes.OP_ASSIGN_SHIFT_RIGHT; }
	"&=" 						{ return GdsTypes.OP_ASSIGN_BIT_AND; }
	"|=" 						{ return GdsTypes.OP_ASSIGN_BIT_OR; }
	"^=" 						{ return GdsTypes.OP_ASSIGN_BIT_XOR; }
	"&" 						{ return GdsTypes.OP_BIT_AND; }
	"|" 						{ return GdsTypes.OP_BIT_OR; }
	"^" 						{ return GdsTypes.OP_BIT_XOR; }
	"~" 						{ return GdsTypes.OP_BIT_INVERT; }
	"++" 						{ return GdsTypes.OP_INCREMENT; }
	"--" 						{ return GdsTypes.OP_DECREMENT; }
	
	"if"	 					{ return GdsTypes.CF_IF; }
	"else"	 					{ return GdsTypes.CF_ELSE; }
	"for" 						{ return GdsTypes.CF_FOR; }
	"while"						{ return GdsTypes.CF_WHILE; }
	"do" 						{ return GdsTypes.CF_DO; }
	"switch" 					{ return GdsTypes.CF_SWITCH; }
	"case" 						{ return GdsTypes.CF_CASE; }
	"default" 					{ return GdsTypes.CF_DEFAULT; }
	"break" 					{ return GdsTypes.CF_BREAK; }
	"continue" 					{ return GdsTypes.CF_CONTINUE; }
	"return" 					{ return GdsTypes.CF_RETURN; }
	"discard" 					{ return GdsTypes.CF_DISCARD; }
	
	"shader_type" 				{ return GdsTypes.SHADER_TYPE; }
	"render_mode" 		 		{ return GdsTypes.RENDER_MODE; }
	"stencil_mode" 				{ return GdsTypes.STENCIL_MODE; }
	"const" 					{ return GdsTypes.CONST; }
	"struct" 					{ return GdsTypes.STRUCT; }
	"lowp" 						{ return GdsTypes.PRECISION_LOW; }
	"mediump" 					{ return GdsTypes.PRECISION_MEDIUM; }
	"highp" 					{ return GdsTypes.PRECISION_HIGH; }
	"uniform" 					{ return GdsTypes.UNIFORM; }
	"group_uniforms" 			{ return GdsTypes.UNIFORM_GROUP; }
	"instance" 					{ return GdsTypes.INSTANCE; }
	"global" 					{ return GdsTypes.GLOBAL; }
	"varying" 					{ return GdsTypes.VARYING; }
	"in" 						{ return GdsTypes.ARG_IN; }
	"out" 						{ return GdsTypes.ARG_OUT; }
	"inout" 					{ return GdsTypes.ARG_INOUT; }
	"flat"	 					{ return GdsTypes.INTERPOLATION_FLAT; }
	"smooth" 					{ return GdsTypes.INTERPOLATION_SMOOTH; }
	
	"void" 						{ return GdsTypes.TYPE_VOID; }
	"bool" 						{ return GdsTypes.TYPE_BOOL; }
	"bvec2" 					{ return GdsTypes.TYPE_BVEC2; }
	"bvec3" 					{ return GdsTypes.TYPE_BVEC3; }
	"bvec4" 					{ return GdsTypes.TYPE_BVEC4; }
	"int" 						{ return GdsTypes.TYPE_INT; }
	"ivec2" 					{ return GdsTypes.TYPE_IVEC2; }
	"ivec3" 					{ return GdsTypes.TYPE_IVEC3; }
	"ivec4" 					{ return GdsTypes.TYPE_IVEC4; }
	"uint" 						{ return GdsTypes.TYPE_UINT; }
	"uvec2" 					{ return GdsTypes.TYPE_UVEC2; }
	"uvec3" 					{ return GdsTypes.TYPE_UVEC3; }
	"uvec4" 					{ return GdsTypes.TYPE_UVEC4; }
	"float" 					{ return GdsTypes.TYPE_FLOAT; }
	"vec2" 						{ return GdsTypes.TYPE_VEC2; }
	"vec3" 						{ return GdsTypes.TYPE_VEC3; }
	"vec4" 						{ return GdsTypes.TYPE_VEC4; }
	"mat2" 						{ return GdsTypes.TYPE_MAT2; }
	"mat3" 						{ return GdsTypes.TYPE_MAT3; }
	"mat4" 						{ return GdsTypes.TYPE_MAT4; }
	"sampler2D" 				{ return GdsTypes.TYPE_SAMPLER2D; }
	"isampler2D" 				{ return GdsTypes.TYPE_ISAMPLER2D; }
	"usampler2D" 				{ return GdsTypes.TYPE_USAMPLER2D; }
	"sampler3D" 				{ return GdsTypes.TYPE_SAMPLER3D; }
	"isampler3D" 				{ return GdsTypes.TYPE_ISAMPLER3D; }
	"usampler3D" 				{ return GdsTypes.TYPE_USAMPLER3D; }
	"samplerCube" 				{ return GdsTypes.TYPE_SAMPLERCUBE; }
	"samplerCubeArray" 			{ return GdsTypes.TYPE_SAMPLERCUBEARRAY; }
	"samplerExternalOES" 		{ return GdsTypes.TYPE_SAMPLEREXT; }
	
	"hint_default_white" 		{ return GdsTypes.HINT_DEFAULT_WHITE_TEXTURE; }
	"hint_default_black" 		{ return GdsTypes.HINT_DEFAULT_BLACK_TEXTURE; }
	"hint_default_transparent"  { return GdsTypes.HINT_DEFAULT_TRANSPARENT_TEXTURE; }
	"hint_normal" 				{ return GdsTypes.HINT_NORMAL_TEXTURE; }
	"hint_roughness_normal"		{ return GdsTypes.HINT_ROUGHNESS_NORMAL_TEXTURE; }
	"hint_roughness_r" 			{ return GdsTypes.HINT_ROUGHNESS_R; }
	"hint_roughness_g" 			{ return GdsTypes.HINT_ROUGHNESS_G; }
	"hint_roughness_b" 			{ return GdsTypes.HINT_ROUGHNESS_B; }
	"hint_roughness_a" 			{ return GdsTypes.HINT_ROUGHNESS_A; }
	"hint_roughness_gray" 		{ return GdsTypes.HINT_ROUGHNESS_GRAY; }
	"hint_anisotropy" 			{ return GdsTypes.HINT_ANISOTROPY_TEXTURE; }
	"source_color" 				{ return GdsTypes.HINT_SOURCE_COLOR; }
	"color_conversion_disabled"  { return GdsTypes.HINT_COLOR_CONVERSION_DISABLED; }
	"hint_range" 				{ return GdsTypes.HINT_RANGE; }
	"hint_enum" 				{ return GdsTypes.HINT_ENUM; }
	"instance_index" 			{ return GdsTypes.HINT_INSTANCE_INDEX; }
	"hint_screen_texture" 		{ return GdsTypes.HINT_SCREEN_TEXTURE; }
	"hint_normal_roughness_texture" { return GdsTypes.HINT_NORMAL_ROUGHNESS_TEXTURE; }
	"hint_depth_texture" 		{ return GdsTypes.HINT_DEPTH_TEXTURE; }
	"filter_nearest" 			{ return GdsTypes.FILTER_NEAREST; }
	"filter_linear" 			{ return GdsTypes.FILTER_LINEAR; }
	"filter_nearest_mipmap" 	{ return GdsTypes.FILTER_NEAREST_MIPMAP; }
	"filter_linear_mipmap" 		{ return GdsTypes.FILTER_LINEAR_MIPMAP; }
	"filter_nearest_mipmap_anisotropic" { return GdsTypes.FILTER_NEAREST_MIPMAP_ANISOTROPIC; }
	"filter_linear_mipmap_anisotropic" { return GdsTypes.FILTER_LINEAR_MIPMAP_ANISOTROPIC; }
	"repeat_enable" 			{ return GdsTypes.REPEAT_ENABLE; }
	"repeat_disable" 			{ return GdsTypes.REPEAT_DISABLE; }
	
	"#define"{PreprocessorLine}  	{ return GdsTypes.PP_DEFINE_LINE; }
	"#undef"{PreprocessorLine}   	{ return GdsTypes.PP_UNDEF_LINE; }
	"#else"{PreprocessorLine}		{ return GdsTypes.PP_ELSE_LINE; }
	"#elif"{PreprocessorLine}   	{ return GdsTypes.PP_ELIF_LINE; }
	"#endif"{PreprocessorLine}   	{ return GdsTypes.PP_ENDIF_LINE; }
	"#ifdef"{PreprocessorLine}   	{ return GdsTypes.PP_IFDEF_LINE; }
	"#ifndef"{PreprocessorLine}  	{ return GdsTypes.PP_IFNDEF_LINE; }
	"#if"{PreprocessorLine}      	{ return GdsTypes.PP_IF_LINE; }
	"#error"{PreprocessorLine}   	{ return GdsTypes.PP_ERROR_LINE; }
	"#include"{PreprocessorLine} 	{ return GdsTypes.PP_INCLUDE_LINE; }
	"#pragma"{PreprocessorLine}  	{ return GdsTypes.PP_PRAGMA_LINE; }
	  
	{Whitespace} 				{ return TokenType.WHITE_SPACE; }
	{Identifier} 				{ return GdsTypes.IDENTIFIER; }
		  
	. 							{ return TokenType.BAD_CHARACTER; }
}

<IN_BLOCK_COMMENT> {
	"*/"						{ yybegin(YYINITIAL); return GdsTypes.BLOCK_COMMENT_END; }
	[^*]+						{ return GdsTypes.BLOCK_COMMENT_CONTENT; }
	"*"							{ return GdsTypes.BLOCK_COMMENT_CONTENT; }
}

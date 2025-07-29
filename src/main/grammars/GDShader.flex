package kr.jaehoyi.gdshader.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import kr.jaehoyi.gdshader.psi.GDShaderTypes;
import com.intellij.psi.TokenType;

%%

%public
%class GDShaderLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

Whitespace = [ \t\r\n]+
LineComment = "//"[^\r\n]*
BlockComment = "/*"([^*]|\*+[^*/])*"*/"

Identifier = [a-zA-Z_][a-zA-Z0-9_]*
FloatConstant =
	([0-9]+\.[0-9]*([eE][+-]?[0-9]+)?[fF]?) |
    (\.[0-9]+([eE][+-]?[0-9]+)?[fF]?) |
    ([0-9]+[eE][+-]?[0-9]+[fF]?) |
    ([0-9]+[fF])
IntConstant = 0|[1-9][0-9]*
UintConstant = 0|[1-9][0-9]*[uU]
StringConstant = \"([^\\\"\n]|\\.)*\"

PreprocessorLine = ([^\\\r\n]*(\\[ \t]*[\r\n])?)*

%%

{LineComment} 				{ return GDShaderTypes.LINE_COMMENT; }
{BlockComment} 				{ return GDShaderTypes.BLOCK_COMMENT; }

"true" 						{ return GDShaderTypes.TRUE; }
"false" 					{ return GDShaderTypes.FALSE; }
{FloatConstant} 			{ return GDShaderTypes.FLOAT_CONSTANT; }
{IntConstant} 				{ return GDShaderTypes.INT_CONSTANT; }
{UintConstant} 				{ return GDShaderTypes.UINT_CONSTANT; }
{StringConstant} 			{ return GDShaderTypes.STRING_CONSTANT; }

"[" 						{ return GDShaderTypes.BRACKET_OPEN; }
"]" 						{ return GDShaderTypes.BRACKET_CLOSE; }
"{" 						{ return GDShaderTypes.CURLY_BRACKET_OPEN; }
"}" 						{ return GDShaderTypes.CURLY_BRACKET_CLOSE; }
"(" 						{ return GDShaderTypes.PARENTHESIS_OPEN; }
")" 						{ return GDShaderTypes.PARENTHESIS_CLOSE; }
"?" 						{ return GDShaderTypes.QUESTION; }
"," 						{ return GDShaderTypes.COMMA; }
":" 						{ return GDShaderTypes.COLON; }
";" 						{ return GDShaderTypes.SEMICOLON; }
"." 						{ return GDShaderTypes.PERIOD; }

"==" 						{ return GDShaderTypes.OP_EQUAL; }
"!=" 						{ return GDShaderTypes.OP_NOT_EQUAL; }
"<" 						{ return GDShaderTypes.OP_LESS; }
"<=" 						{ return GDShaderTypes.OP_LESS_EQUAL; }
">" 						{ return GDShaderTypes.OP_GREATER; }
">=" 						{ return GDShaderTypes.OP_GREATER_EQUAL; }
"&&" 						{ return GDShaderTypes.OP_AND; }
"||" 						{ return GDShaderTypes.OP_OR; }
"!" 						{ return GDShaderTypes.OP_NOT; }
"+" 						{ return GDShaderTypes.OP_ADD; }
"-" 						{ return GDShaderTypes.OP_SUB; }
"*" 						{ return GDShaderTypes.OP_MUL; }
"/" 						{ return GDShaderTypes.OP_DIV; }
"%" 						{ return GDShaderTypes.OP_MOD; }
"<<" 						{ return GDShaderTypes.OP_SHIFT_LEFT; }
">>" 						{ return GDShaderTypes.OP_SHIFT_RIGHT; }
"=" 						{ return GDShaderTypes.OP_ASSIGN; }
"+=" 						{ return GDShaderTypes.OP_ASSIGN_ADD; }
"-=" 						{ return GDShaderTypes.OP_ASSIGN_SUB; }
"*=" 						{ return GDShaderTypes.OP_ASSIGN_MUL; }
"/=" 						{ return GDShaderTypes.OP_ASSIGN_DIV; }
"%=" 						{ return GDShaderTypes.OP_ASSIGN_MOD; }
"<<="	 					{ return GDShaderTypes.OP_ASSIGN_SHIFT_LEFT; }
">>="	 					{ return GDShaderTypes.OP_ASSIGN_SHIFT_RIGHT; }
"&" 						{ return GDShaderTypes.OP_BIT_AND; }
"|" 						{ return GDShaderTypes.OP_BIT_OR; }
"^" 						{ return GDShaderTypes.OP_BIT_XOR; }
"~" 						{ return GDShaderTypes.OP_BIT_INVERT; }
"++" 						{ return GDShaderTypes.OP_INCREMENT; }
"--" 						{ return GDShaderTypes.OP_DECREMENT; }

"if"	 					{ return GDShaderTypes.CF_IF; }
"else"	 					{ return GDShaderTypes.CF_ELSE; }
"for" 						{ return GDShaderTypes.CF_FOR; }
"while"						{ return GDShaderTypes.CF_WHILE; }
"do" 						{ return GDShaderTypes.CF_DO; }
"switch" 					{ return GDShaderTypes.CF_SWITCH; }
"case" 						{ return GDShaderTypes.CF_CASE; }
"default" 					{ return GDShaderTypes.CF_DEFAULT; }
"break" 					{ return GDShaderTypes.CF_BREAK; }
"continue" 					{ return GDShaderTypes.CF_CONTINUE; }
"return" 					{ return GDShaderTypes.CF_RETURN; }
"discard" 					{ return GDShaderTypes.CF_DISCARD; }

"shader_type" 				{ return GDShaderTypes.SHADER_TYPE; }
"render_mode" 		 		{ return GDShaderTypes.RENDER_MODE; }
"stencil_mode" 				{ return GDShaderTypes.STENCIL_MODE; }
"const" 					{ return GDShaderTypes.CONST; }
"struct" 					{ return GDShaderTypes.STRUCT; }
"lowp" 						{ return GDShaderTypes.PRECISION_LOW; }
"mediump" 					{ return GDShaderTypes.PRECISION_MEDIUM; }
"highp" 					{ return GDShaderTypes.PRECISION_HIGH; }
"uniform" 					{ return GDShaderTypes.UNIFORM; }
"group_uniforms" 			{ return GDShaderTypes.UNIFORM_GROUP; }
"instance" 					{ return GDShaderTypes.INSTANCE; }
"global" 					{ return GDShaderTypes.GLOBAL; }
"varying" 					{ return GDShaderTypes.VARYING; }
"in" 						{ return GDShaderTypes.ARG_IN; }
"out" 						{ return GDShaderTypes.ARG_OUT; }
"inout" 					{ return GDShaderTypes.ARG_INOUT; }
"flat"	 					{ return GDShaderTypes.INTERPOLATION_FLAT; }
"smooth" 					{ return GDShaderTypes.INTERPOLATION_SMOOTH; }

"void" 						{ return GDShaderTypes.TYPE_VOID; }
"bool" 						{ return GDShaderTypes.TYPE_BOOL; }
"bvec2" 					{ return GDShaderTypes.TYPE_BVEC2; }
"bvec3" 					{ return GDShaderTypes.TYPE_BVEC3; }
"bvec4" 					{ return GDShaderTypes.TYPE_BVEC4; }
"int" 						{ return GDShaderTypes.TYPE_INT; }
"ivec2" 					{ return GDShaderTypes.TYPE_IVEC2; }
"ivec3" 					{ return GDShaderTypes.TYPE_IVEC3; }
"ivec4" 					{ return GDShaderTypes.TYPE_IVEC4; }
"uint" 						{ return GDShaderTypes.TYPE_UINT; }
"uvec2" 					{ return GDShaderTypes.TYPE_UVEC2; }
"uvec3" 					{ return GDShaderTypes.TYPE_UVEC3; }
"uvec4" 					{ return GDShaderTypes.TYPE_UVEC4; }
"float" 					{ return GDShaderTypes.TYPE_FLOAT; }
"vec2" 						{ return GDShaderTypes.TYPE_VEC2; }
"vec3" 						{ return GDShaderTypes.TYPE_VEC3; }
"vec4" 						{ return GDShaderTypes.TYPE_VEC4; }
"mat2" 						{ return GDShaderTypes.TYPE_MAT2; }
"mat3" 						{ return GDShaderTypes.TYPE_MAT3; }
"mat4" 						{ return GDShaderTypes.TYPE_MAT4; }
"sampler2D" 				{ return GDShaderTypes.TYPE_SAMPLER2D; }
"isampler2D" 				{ return GDShaderTypes.TYPE_ISAMPLER2D; }
"usampler2D" 				{ return GDShaderTypes.TYPE_USAMPLER2D; }
"sampler3D" 				{ return GDShaderTypes.TYPE_SAMPLER3D; }
"isampler3D" 				{ return GDShaderTypes.TYPE_ISAMPLER3D; }
"usampler3D" 				{ return GDShaderTypes.TYPE_USAMPLER3D; }
"samplerCube" 				{ return GDShaderTypes.TYPE_SAMPLERCUBE; }
"samplerCubeArray" 			{ return GDShaderTypes.TYPE_SAMPLERCUBEARRAY; }
"samplerExternalOES" 		{ return GDShaderTypes.TYPE_SAMPLEREXT; }

"hint_default_white" 		{ return GDShaderTypes.HINT_DEFAULT_WHITE_TEXTURE; }
"hint_default_black" 		{ return GDShaderTypes.HINT_DEFAULT_BLACK_TEXTURE; }
"hint_default_transparent"  { return GDShaderTypes.HINT_DEFAULT_TRANSPARENT_TEXTURE; }
"hint_normal" 				{ return GDShaderTypes.HINT_NORMAL_TEXTURE; }
"hint_roughness_normal"		{ return GDShaderTypes.HINT_ROUGHNESS_NORMAL_TEXTURE; }
"hint_roughness_r" 			{ return GDShaderTypes.HINT_ROUGHNESS_R; }
"hint_roughness_g" 			{ return GDShaderTypes.HINT_ROUGHNESS_G; }
"hint_roughness_b" 			{ return GDShaderTypes.HINT_ROUGHNESS_B; }
"hint_roughness_a" 			{ return GDShaderTypes.HINT_ROUGHNESS_A; }
"hint_roughness_gray" 		{ return GDShaderTypes.HINT_ROUGHNESS_GRAY; }
"hint_anisotropy" 			{ return GDShaderTypes.HINT_ANISOTROPY_TEXTURE; }
"source_color" 				{ return GDShaderTypes.HINT_SOURCE_COLOR; }
"color_coversion_disabled"  { return GDShaderTypes.HINT_COLOR_CONVERSION_DISABLED; }
"hint_range" 				{ return GDShaderTypes.HINT_RANGE; }
"hint_enum" 				{ return GDShaderTypes.HINT_ENUM; }
"instance_index" 			{ return GDShaderTypes.HINT_INSTANCE_INDEX; }
"hint_screen_texture" 		{ return GDShaderTypes.HINT_SCREEN_TEXTURE; }
"hint_normal_roughness_texture" { return GDShaderTypes.HINT_NORMAL_ROUGHNESS_TEXTURE; }
"hint_depth_texture" 		{ return GDShaderTypes.HINT_DEPTH_TEXTURE; }
"filter_nearest" 			{ return GDShaderTypes.FILTER_NEAREST; }
"filter_linear" 			{ return GDShaderTypes.FILTER_LINEAR; }
"filter_nearest_mipmap" 	{ return GDShaderTypes.FILTER_NEAREST_MIPMAP; }
"filter_linear_mipmap" 		{ return GDShaderTypes.FILTER_LINEAR_MIPMAP; }
"filter_nearest_mipmap_anisotropic" { return GDShaderTypes.FILTER_NEAREST_MIPMAP_ANISOTROPIC; }
"filter_linear_mipmap_anisotropic" { return GDShaderTypes.FILTER_LINEAR_MIPMAP_ANISOTROPIC; }
"repeat_enable" 			{ return GDShaderTypes.REPEAT_ENABLE; }
"repeat_disable" 			{ return GDShaderTypes.REPEAT_DISABLE; }

"#define"{PreprocessorLine}  	{ return GDShaderTypes.PP_DEFINE_LINE; }
"#undef"{PreprocessorLine}   	{ return GDShaderTypes.PP_UNDEF_LINE; }
"#else"{PreprocessorLine}		{ return GDShaderTypes.PP_ELSE_LINE; }
"#elif"{PreprocessorLine}   	{ return GDShaderTypes.PP_ELIF_LINE; }
"#endif"{PreprocessorLine}   	{ return GDShaderTypes.PP_ENDIF_LINE; }
"#ifdef"{PreprocessorLine}   	{ return GDShaderTypes.PP_IFDEF_LINE; }
"#ifndef"{PreprocessorLine}  	{ return GDShaderTypes.PP_IFNDEF_LINE; }
"#if"{PreprocessorLine}      	{ return GDShaderTypes.PP_IF_LINE; }
"#error"{PreprocessorLine}   	{ return GDShaderTypes.PP_ERROR_LINE; }
"#include"{PreprocessorLine} 	{ return GDShaderTypes.PP_INCLUDE_LINE; }
"#pragma"{PreprocessorLine}  	{ return GDShaderTypes.PP_PRAGMA_LINE; }
  
{Whitespace} 				{ return TokenType.WHITE_SPACE; }
{Identifier} 				{ return GDShaderTypes.IDENTIFIER; }
      
. 							{ return TokenType.BAD_CHARACTER; }

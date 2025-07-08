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

Whitespace = [\ \t\r\n]+

LineComment = "//"[^\r\n]*
BlockComment = "/*"([^*]|\*+[^*/])*"*/"

%%


<YYINITIAL> {
	{Whitespace}				{ return TokenType.WHITE_SPACE; }
	
	{LineComment} 				{ return GDShaderTypes.LINE_COMMENT; }
	{BlockComment} 				{ return GDShaderTypes.BLOCK_COMMENT; }
      
    "true" 						{ return GDShaderTypes.TRUE; }
	"false" 					{ return GDShaderTypes.FALSE; }
      
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
}

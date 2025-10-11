package kr.jaehoyi.gdshader

import com.intellij.lexer.FlexAdapter
import kr.jaehoyi.gdshader.lexer.GDShaderLexer

class GDShaderLexerAdapter : FlexAdapter(GDShaderLexer(null)) 
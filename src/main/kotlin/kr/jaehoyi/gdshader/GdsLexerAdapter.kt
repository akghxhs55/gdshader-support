package kr.jaehoyi.gdshader

import com.intellij.lexer.FlexAdapter
import kr.jaehoyi.gdshader.lexer.GdsLexer

class GdsLexerAdapter : FlexAdapter(GdsLexer(null)) 
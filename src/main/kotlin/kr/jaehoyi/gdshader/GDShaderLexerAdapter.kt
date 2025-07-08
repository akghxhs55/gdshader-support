package kr.jaehoyi.gdshader

import com.intellij.lexer.FlexAdapter
import kr.jaehoyi.gdshader.lexer.GDShaderLexer

object GDShaderLexerAdapter : FlexAdapter(GDShaderLexer(null)) {
}
package kr.jaehoyi.gdshader.psi.impl

import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderVariableDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVariableDeclarator

object GDShaderPsiImplUtil {
    fun getName(element: GDShaderVariableDeclarator): String? {
        val id = element.firstChild
        return id?.text
    }
    
    fun getArraySize(element: GDShaderVariableDeclarator): Int {
        val sizePsi = element.arraySize
        return sizePsi?.text?.toIntOrNull() ?: 0
    }
}
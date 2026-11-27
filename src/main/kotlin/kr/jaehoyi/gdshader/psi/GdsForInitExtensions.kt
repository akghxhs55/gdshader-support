package kr.jaehoyi.gdshader.psi

val GdsForInit.isConstant: Boolean
    get() = node.findChildByType(GdsTypes.CONST) != null

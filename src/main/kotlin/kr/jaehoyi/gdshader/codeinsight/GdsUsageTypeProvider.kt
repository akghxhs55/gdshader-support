package kr.jaehoyi.gdshader.codeinsight

import com.intellij.psi.PsiElement
import com.intellij.usages.impl.rules.UsageType
import com.intellij.usages.impl.rules.UsageTypeProvider
import kr.jaehoyi.gdshader.psi.*

import kr.jaehoyi.gdshader.GdsBundle

class GdsUsageTypeProvider : UsageTypeProvider {

    override fun getUsageType(element: PsiElement): UsageType? {
        return when (element) {
            is GdsFunctionNameRef -> FUNCTION_CALL
            is GdsStructNameRef -> getStructUsageType(element)
            is GdsStructMemberNameRef -> STRUCT_MEMBER_ACCESS
            else -> null
        }
    }

    private fun getStructUsageType(element: GdsStructNameRef): UsageType {
        val type = element.parent as? GdsType ?: return TYPE_REFERENCE
        return if (type.parent is GdsFunctionCall) CONSTRUCTOR_CALL else TYPE_REFERENCE
    }
}

private val FUNCTION_CALL = UsageType { GdsBundle.message("usage.type.function.call") }
private val CONSTRUCTOR_CALL = UsageType { GdsBundle.message("usage.type.constructor.call") }
private val TYPE_REFERENCE = UsageType { GdsBundle.message("usage.type.type.reference") }
private val STRUCT_MEMBER_ACCESS = UsageType { GdsBundle.message("usage.type.struct.member.access") }

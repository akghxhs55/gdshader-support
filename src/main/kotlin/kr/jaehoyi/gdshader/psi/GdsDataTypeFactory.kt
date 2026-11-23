package kr.jaehoyi.gdshader.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.resolve.GdsResolver

object GdsDataTypeFactory {
    fun createFromUniformDeclaration(declaration: GdsUniformDeclaration): DataType? {
        val typeNode = declaration.type ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeNode) ?: return null

        return withArraySize(baseType, declaration.arraySizeList.firstOrNull())
    }

    fun createFromConstantDeclaration(declarator: GdsConstantDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GdsConstantDeclaration ?: return null

        val typeNode = declaration.type ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeNode) ?: return null

        return withArraySize(baseType, declarator.arraySize ?: declaration.arraySize)
    }

    fun createFromVaryingDeclaration(declaration: GdsVaryingDeclaration): DataType? {
        val typeNode = declaration.type ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeNode) ?: return null

        return withArraySize(baseType, declaration.arraySizeList.firstOrNull())
    }

    fun createFromLocalVariableDeclaration(declarator: GdsLocalVariableDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GdsLocalVariableDeclaration ?: return null

        val baseType = getBaseType(declaration.precision?.text, declaration.type) ?: return null

        return withArraySize(baseType, declarator.arraySize ?: declaration.arraySize)
    }

    fun createFromForInit(declarator: GdsLocalVariableDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GdsForInit ?: return null

        val baseType = getBaseType(declaration.precision?.text, declaration.type) ?: return null
        return withArraySize(baseType, declarator.arraySize)
    }

    fun createFromParameter(parameter: GdsParameter): DataType? {
        val typeNode = parameter.type
        val baseType = getBaseType(parameter.precision?.text, typeNode) ?: return null

        return withArraySize(baseType, parameter.arraySizeList.firstOrNull())
    }

    fun createFromFunctionDeclaration(declaration: GdsFunctionDeclaration): DataType? {
        val typeNode = declaration.type
        return getBaseType(declaration.precision?.text, typeNode)
    }

    fun createFromType(typeNode: GdsType): DataType? = getBaseType(null, typeNode)

    fun createFromStructDeclaration(declaration: GdsStructDeclaration): StructType? = createStructType(declaration, mutableSetOf())

    fun createFromStructName(
        context: PsiElement,
        name: String,
    ): StructType? {
        var result: StructType? = null
        GdsResolver.processStructDeclaration(context) { decl ->
            if (decl.name != name) return@processStructDeclaration true
            result = (decl.parent as? GdsStructDeclaration)?.let { createFromStructDeclaration(it) }
            false
        }
        return result
    }

    private fun getBaseType(
        precisionText: String?,
        typeNode: GdsType,
        visiting: MutableSet<GdsStructDeclaration> = mutableSetOf(),
    ): DataType? {
        val typeText = typeNode.text
        var baseType = Builtins.getType(typeText)

        if (baseType == null) {
            baseType = resolveStructType(typeNode, visiting)
        }

        if (baseType == null) return null

        if (precisionText != null) {
            val precision = Precision.fromText(precisionText)
            baseType = baseType.withPrecision(precision)
        }
        return baseType
    }

    private fun resolveStructType(
        typeNode: GdsType,
        visiting: MutableSet<GdsStructDeclaration>,
    ): StructType? {
        val structRef = typeNode.structNameRef ?: return null
        val structDecl = structRef.reference.resolve() as? GdsStructNameDecl ?: return null
        val structDeclaration = structDecl.parent as? GdsStructDeclaration ?: return null

        return createStructType(structDeclaration, visiting)
    }

    private fun createStructType(
        structDeclaration: GdsStructDeclaration,
        visiting: MutableSet<GdsStructDeclaration>,
    ): StructType? {
        val structName = structDeclaration.structNameDecl?.text ?: return null
        // A self-referencing member (`struct A { A a; };`) resolves to its own declaration.
        if (!visiting.add(structDeclaration)) return null

        try {
            val structBlock = structDeclaration.structBlock
            val members = mutableMapOf<String, DataType>()
            var hasUnresolvedMembers = structBlock == null || PsiTreeUtil.hasErrorElements(structBlock)

            structBlock?.structMemberList?.structMemberList?.forEach { member ->
                val memberName = member.structMemberNameDecl?.text
                val memberBaseType = getBaseType(member.precision?.text, member.type, visiting)
                if (memberName == null || memberBaseType == null || memberName in members) {
                    hasUnresolvedMembers = true
                    return@forEach
                }

                val memberType = withArraySize(memberBaseType, member.arraySizeList.firstOrNull())
                if (memberType is ArrayType && memberType.containerSize == null) {
                    hasUnresolvedMembers = true
                }
                members[memberName] = memberType
            }

            return StructType(structName, members, hasUnresolvedMembers)
        } finally {
            visiting.remove(structDeclaration)
        }
    }

    private fun withArraySize(
        baseType: DataType,
        arraySize: GdsArraySize?,
    ): DataType =
        if (arraySize != null) {
            ArrayType(baseType, GdsConstantEvaluator.evaluateArraySize(arraySize))
        } else {
            baseType
        }
}

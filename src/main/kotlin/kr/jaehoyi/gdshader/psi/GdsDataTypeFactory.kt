package kr.jaehoyi.gdshader.psi

import kr.jaehoyi.gdshader.model.*

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
            val members = mutableMapOf<String, DataType>()
            structDeclaration.structBlock?.structMemberList?.structMemberList?.forEach { member ->
                val memberName = member.structMemberNameDecl?.text ?: return@forEach

                val memberTypeNode = member.type
                val memberBaseType = getBaseType(member.precision?.text, memberTypeNode, visiting) ?: return@forEach

                members[memberName] = withArraySize(memberBaseType, member.arraySizeList.firstOrNull())
            }

            return StructType(structName, members)
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

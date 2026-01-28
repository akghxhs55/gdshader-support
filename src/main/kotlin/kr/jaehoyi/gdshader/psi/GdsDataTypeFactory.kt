package kr.jaehoyi.gdshader.psi

import kr.jaehoyi.gdshader.model.*

object GdsDataTypeFactory {
    
    fun createFromUniformDeclaration(declaration: GdsUniformDeclaration): DataType? {
        val typeNode = declaration.type ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeNode) ?: return null
        
        val arraySizeList = declaration.arraySizeList
        
        return if (!arraySizeList.isEmpty()) {
            val arraySize = parseArraySize(arraySizeList.first())
            if (arraySize != null) {
                ArrayType(baseType, arraySize)
            } else {
                baseType
            }
        } else {
            baseType
        }
    }
    
    fun createFromConstantDeclaration(declarator: GdsConstantDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GdsConstantDeclaration ?: return null
        
        val typeNode = declaration.type ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeNode) ?: return null
        
        val typeArraySizeNode = declaration.arraySize
        val varArraySizeNode = declarator.arraySize
        
        val arraySize = when {
            varArraySizeNode != null -> parseArraySize(varArraySizeNode)
            typeArraySizeNode != null -> parseArraySize(typeArraySizeNode)
            else -> null
        }
        
        return if (arraySize != null) {
            ArrayType(baseType, arraySize)
        } else {
            baseType
        }
    }
    
    fun createFromVaryingDeclaration(declaration: GdsVaryingDeclaration): DataType? {
        val typeNode = declaration.type ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeNode) ?: return null
        
        val arraySizeList = declaration.arraySizeList
        
        return if (!arraySizeList.isEmpty()) {
            val arraySize = parseArraySize(arraySizeList.first())
            if (arraySize != null) {
                ArrayType(baseType, arraySize)
            } else {
                baseType
            }
        } else {
            baseType
        }
    }
    
    fun createFromLocalVariableDeclaration(declarator: GdsLocalVariableDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GdsLocalVariableDeclaration ?: return null
        
        val baseType = getBaseType(declaration.precision?.text, declaration.type) ?: return null
        
        val typeArraySizeNode = declaration.arraySize
        val varArraySizeNode = declarator.arraySize
        
        val arraySize = when {
            varArraySizeNode != null -> parseArraySize(varArraySizeNode)
            typeArraySizeNode != null -> parseArraySize(typeArraySizeNode)
            else -> null
        }
        
        return if (arraySize != null) {
            ArrayType(baseType, arraySize)
        } else {
            baseType
        }
    }
    
    fun createFromForInit(declarator: GdsLocalVariableDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GdsForInit ?: return null
        
        return getBaseType(declaration.precision?.text, declaration.type)
    }
    
    fun createFromParameter(parameter: GdsParameter): DataType? {
        val typeNode = parameter.type
        val baseType = getBaseType(parameter.precision?.text, typeNode) ?: return null
        
        val arraySizeList = parameter.arraySizeList
        
        return if (!arraySizeList.isEmpty()) {
            val arraySize = parseArraySize(arraySizeList.first())
            if (arraySize != null) {
                ArrayType(baseType, arraySize)
            } else {
                baseType
            }
        } else {
            baseType
        }
    }
    
    fun createFromFunctionDeclaration(declaration: GdsFunctionDeclaration): DataType? {
        val typeNode = declaration.type
        return getBaseType(declaration.precision?.text, typeNode)
    }
    
    private fun getBaseType(precisionText: String?, typeNode: GdsType): DataType? {
        val typeText = typeNode.text
        var baseType = Builtins.getType(typeText)
        
        if (baseType == null) {
            baseType = resolveStructType(typeNode)
        }
        
        if (baseType == null) return null

        if (precisionText != null) {
            val precision = Precision.fromText(precisionText)
            baseType = baseType.withPrecision(precision)
        }
        return baseType
    }
    
    private fun resolveStructType(typeNode: GdsType): DataType? {
        val structRef = typeNode.structNameRef ?: return null
        val structDecl = structRef.reference.resolve() as? GdsStructNameDecl ?: return null
        val structDeclaration = structDecl.parent as? GdsStructDeclaration ?: return null
        
        val members = mutableMapOf<String, DataType>()
        structDeclaration.structBlock?.structMemberList?.structMemberList?.forEach { member ->
             val memberName = member.structMemberNameDecl?.text ?: return@forEach
             
             val memberTypeNode = member.type
             val memberBaseType = getBaseType(member.precision?.text, memberTypeNode) ?: return@forEach
             
             val arraySizeList = member.arraySizeList
             val finalType = if (arraySizeList.isNotEmpty()) {
                 val arraySize = parseArraySize(arraySizeList.first())
                 if (arraySize != null) {
                     ArrayType(memberBaseType, arraySize)
                 } else {
                     memberBaseType
                 }
             } else {
                 memberBaseType
             }
             members[memberName] = finalType
        }
        
        return StructType(structDecl.text, members)
    }
    
    private fun parseArraySize(node: GdsArraySize): Int? {
        return node.text.filter { it.isDigit() }.toIntOrNull()
    }
    
}
package kr.jaehoyi.gdshader.psi

import kr.jaehoyi.gdshader.model.ArrayType
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.Precision
import kr.jaehoyi.gdshader.model.withPrecision

object GdsDataTypeFactory {
    
    fun createFromUniformDeclaration(declaration: GDShaderUniformDeclaration): DataType? {
        val typeText = declaration.type?.text ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeText) ?: return null
        
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
    
    fun createFromConstantDeclaration(declarator: GDShaderConstantDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GDShaderConstantDeclaration ?: return null
        
        val typeText = declaration.type?.text ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeText) ?: return null
        
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
    
    fun createFromVaryingDeclaration(declaration: GDShaderVaryingDeclaration): DataType? {
        val typeText = declaration.type?.text ?: return null
        val baseType = getBaseType(declaration.precision?.text, typeText) ?: return null
        
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
    
    fun createFromLocalVariableDeclaration(declarator: GDShaderLocalVariableDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GDShaderLocalVariableDeclaration ?: return null
        
        val baseType = getBaseType(declaration.precision?.text, declaration.type.text) ?: return null
        
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
    
    fun createFromForInit(declarator: GDShaderLocalVariableDeclarator): DataType? {
        val declaration = declarator.parent?.parent as? GDShaderForInit ?: return null
        
        return getBaseType(declaration.precision?.text, declaration.type.text)
    }
    
    fun createFromParameter(parameter: GDShaderParameter): DataType? {
        val typeText = parameter.type.text ?: return null
        val baseType = getBaseType(parameter.precision?.text, typeText) ?: return null
        
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
    
    fun createFromFunctionDeclaration(declaration: GDShaderFunctionDeclaration): DataType? {
        val typeText = declaration.type.text ?: return null
        return getBaseType(declaration.precision?.text, typeText)
    }
    
    private fun getBaseType(precisionText: String?, typeText: String): DataType? {
        var baseType = Builtins.getType(typeText) ?: return null
        if (precisionText != null) {
            val precision = Precision.fromText(precisionText)
            baseType = baseType.withPrecision(precision)
        }
        return baseType
    }
    
    private fun parseArraySize(node: GDShaderArraySize): Int? {
        return node.text.filter { it.isDigit() }.toIntOrNull()
    }
    
}
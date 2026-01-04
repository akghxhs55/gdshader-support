package kr.jaehoyi.gdshader.psi

import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.InterpolationQualifier
import kr.jaehoyi.gdshader.model.ParameterQualifier
import kr.jaehoyi.gdshader.model.Precision
import kr.jaehoyi.gdshader.model.StorageQualifier
import kr.jaehoyi.gdshader.model.VariableSpec

val GDShaderVariableNameDecl.variableSpec: VariableSpec?
    get() {
        val declarator = this.parent ?: return null
        return when (declarator) {
            is GDShaderUniformDeclaration -> {
                val type = declarator.type?.text?.let { DataType.fromText(it) }
                    ?: return null
                val precision = declarator.precision?.text?.let { Precision.fromText(it) }
                    ?: Precision.DEFAULT
                val array = declarator.arraySizeList.isNotEmpty()
                val storageQualifier = when (declarator.uniformHeader.text) {
                    "uniform" -> StorageQualifier.UNIFORM
                    "global uniform" -> StorageQualifier.GLOBAL_UNIFORM
                    "instance uniform" -> StorageQualifier.INSTANCE_UNIFORM
                    else -> StorageQualifier.UNIFORM
                }
                
                VariableSpec(
                    name = this.name,
                    type = type,
                    precision = precision,
                    array = array,
                    storageQualifier = storageQualifier,
                )
            }
            
            is GDShaderConstantDeclarator -> {
                val parent = declarator.parent?.parent as? GDShaderConstantDeclaration ?: return null
                val type = parent.type?.text?.let { DataType.fromText(it) }
                    ?: return null
                val precision = parent.precision?.text?.let { Precision.fromText(it) }
                    ?: Precision.DEFAULT
                val array = declarator.arraySize != null || parent.arraySize != null
                
                VariableSpec(
                    name = this.name,
                    type = type,
                    precision = precision,
                    array = array,
                    storageQualifier = StorageQualifier.CONSTANT,
                )
            }
            
            is GDShaderVaryingDeclaration -> {
                val type = declarator.type?.text?.let { DataType.fromText(it) }
                    ?: return null
                val precision = declarator.precision?.text?.let { Precision.fromText(it) }
                    ?: Precision.DEFAULT
                val array = declarator.arraySizeList.isNotEmpty()
                val interpolationQualifier = when (declarator.interpolationQualifier?.text) {
                    "smooth" -> InterpolationQualifier.SMOOTH
                    "flat" -> InterpolationQualifier.FLAT
                    else -> InterpolationQualifier.DEFAULT
                }
                
                VariableSpec(
                    name = this.name,
                    type = type,
                    precision = precision,
                    array = array,
                    storageQualifier = StorageQualifier.VARYING,
                    interpolationQualifier = interpolationQualifier
                )
            }
            
            is GDShaderLocalVariableDeclarator -> {
                val declaratorList = declarator.parent as? GDShaderLocalVariableDeclaratorList ?: return null
                when (declaratorList.parent) {
                    is GDShaderLocalVariableDeclaration -> {
                        val declaration = declaratorList.parent as? GDShaderLocalVariableDeclaration ?: return null
                        val type = declaration.type.text?.let { DataType.fromText(it) }
                            ?: return null
                        val precision = declaration.precision?.text?.let { Precision.fromText(it) }
                            ?: Precision.DEFAULT
                        val array = declarator.arraySize != null || declaration.arraySize != null

                        VariableSpec(
                            name = this.name,
                            type = type,
                            precision = precision,
                            array = array,
                            storageQualifier = StorageQualifier.LOCAL,
                        )
                    }

                    is GDShaderForInit -> {
                        val forInit = declaratorList.parent as? GDShaderForInit ?: return null
                        val type = forInit.type.text?.let { DataType.fromText(it) }
                            ?: return null
                        val precision = forInit.precision?.text?.let { Precision.fromText(it) }
                            ?: Precision.DEFAULT
                        val array = declarator.arraySize != null

                        VariableSpec(
                            name = this.name,
                            type = type,
                            precision = precision,
                            array = array,
                            storageQualifier = StorageQualifier.LOCAL,
                        )
                    }

                    else -> null
                }
            }
            
            is GDShaderParameter -> {
                val type = declarator.type.text?.let { DataType.fromText(it) }
                    ?: return null
                val precision = declarator.precision?.text?.let { Precision.fromText(it) }
                    ?: Precision.DEFAULT
                val array = declarator.arraySize != null
                val parameterQualifier = when (declarator.parameterQualifier?.text) {
                    "in" -> ParameterQualifier.IN
                    "out" -> ParameterQualifier.OUT
                    "inout" -> ParameterQualifier.INOUT
                    "const" -> ParameterQualifier.CONST
                    "const in" -> ParameterQualifier.CONST_IN
                    else -> ParameterQualifier.NONE
                }
                
                VariableSpec(
                    name = this.name,
                    type = type,
                    precision = precision,
                    array = array,
                    storageQualifier = StorageQualifier.PARAMETER,
                    parameterQualifier = parameterQualifier,
                )
            }
            
            else -> null
        }
    }

package kr.jaehoyi.gdshader.psi

import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.InterpolationQualifier
import kr.jaehoyi.gdshader.model.LocalVariableSpec
import kr.jaehoyi.gdshader.model.ParameterQualifier
import kr.jaehoyi.gdshader.model.ParameterSpec
import kr.jaehoyi.gdshader.model.UniformQualifier
import kr.jaehoyi.gdshader.model.UniformSpec
import kr.jaehoyi.gdshader.model.VariableSpec
import kr.jaehoyi.gdshader.model.VaryingSpec

val GdsVariableNameDecl.variableSpec: VariableSpec?
    get() {
        val declarator = this.parent ?: return null
        return when (declarator) {
            is GdsUniformDeclaration -> {
                val type = GdsDataTypeFactory.createFromUniformDeclaration(declarator) ?: return null
                val qualifierText = declarator.uniformHeader.uniformQualifier?.text
                val qualifier = UniformQualifier.fromText(qualifierText)

                UniformSpec(
                    name = this.name,
                    type = type,
                    qualifier = qualifier,
                )
            }
            
            is GdsConstantDeclarator -> {
                val type = GdsDataTypeFactory.createFromConstantDeclaration(declarator) ?: return null

                ConstantSpec(
                    name = this.name,
                    type = type,
                )
            }
            
            is GdsVaryingDeclaration -> {
                val type = GdsDataTypeFactory.createFromVaryingDeclaration(declarator) ?: return null
                val qualifierText = declarator.interpolationQualifier?.text
                val qualifier = InterpolationQualifier.fromText(qualifierText)

                VaryingSpec(
                    name = this.name,
                    type = type,
                    qualifier = qualifier,
                )
            }
            
            is GdsLocalVariableDeclarator -> {
                val declaratorList = declarator.parent as? GdsLocalVariableDeclaratorList ?: return null
                val parent = declaratorList.parent ?: return null
                when (parent) {
                    is GdsLocalVariableDeclaration -> {
                        val type = GdsDataTypeFactory.createFromLocalVariableDeclaration(declarator) ?: return null

                        LocalVariableSpec(
                            name = this.name,
                            type = type,
                        )
                    }

                    is GdsForInit -> {
                        val type = GdsDataTypeFactory.createFromForInit(declarator) ?: return null

                        LocalVariableSpec(
                            name = this.name,
                            type = type,
                        )
                    }

                    else -> null
                }
            }
            
            is GdsParameter -> {
                val type = GdsDataTypeFactory.createFromParameter(declarator) ?: return null
                val qualifierText = declarator.parameterQualifier?.text
                val qualifier = ParameterQualifier.fromText(qualifierText)

                ParameterSpec(
                    name = this.name,
                    type = type,
                    qualifier = qualifier,
                )
            }
            
            else -> null
        }
    }

val GdsFunctionNameDecl.functionSpec: FunctionSpec?
    get() {
        val declaration = this.parent as? GdsFunctionDeclaration ?: return null
        val returnType = GdsDataTypeFactory.createFromFunctionDeclaration(declaration) ?: return null
        val parameterList = declaration.parameterList ?: return null
        val parameters = parameterList.parameterList.mapNotNull { param ->
            val nameDecl = param.variableNameDecl
            nameDecl.variableSpec as ParameterSpec
        }
        
        return FunctionSpec(
            name = this.name,
            returnType = returnType,
            parameters = parameters,
        )
    }

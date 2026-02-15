package kr.jaehoyi.gdshader.codeinsight

import com.intellij.lang.parameterInfo.*
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

class GdsParameterInfoHandler : ParameterInfoHandler<GdsArgumentList, FunctionSpec> {

    override fun findElementForParameterInfo(context: CreateParameterInfoContext): GdsArgumentList? {
        val element = context.file.findElementAt(context.offset) ?: return null

        var argumentList = PsiTreeUtil.getParentOfType(element, GdsArgumentList::class.java)
        
        if (argumentList == null) {
            var functionCall = PsiTreeUtil.getParentOfType(element, GdsFunctionCall::class.java)
            
            if (functionCall == null) {
                var prev: PsiElement? = element
                while (prev != null && prev !is GdsFile) {
                    val elementType = prev.node.elementType
                    if (elementType == GdsTypes.PARENTHESIS_OPEN) {
                        functionCall = prev.parent as? GdsFunctionCall
                        if (functionCall != null) break
                    }
                    if (elementType == GdsTypes.SEMICOLON || 
                        elementType == GdsTypes.CURLY_BRACKET_OPEN || 
                        elementType == GdsTypes.CURLY_BRACKET_CLOSE ||
                        elementType == GdsTypes.PARENTHESIS_CLOSE) {
                        break
                    }
                    prev = PsiTreeUtil.prevLeaf(prev)
                }
            }
            

            argumentList = functionCall?.argumentList
        }
        

        
        if (argumentList == null) return null
        
        val candidates = resolveCandidates(argumentList)

        
        if (candidates.isNotEmpty()) {
            context.itemsToShow = candidates.toTypedArray()
            return argumentList
        }
        return null
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): GdsArgumentList? {
        val element = context.file.findElementAt(context.offset) ?: return null
        var argumentList = PsiTreeUtil.getParentOfType(element, GdsArgumentList::class.java)
        
        if (argumentList == null) {
            var functionCall = PsiTreeUtil.getParentOfType(element, GdsFunctionCall::class.java)
            
            if (functionCall == null) {
                var prev: PsiElement? = element
                while (prev != null && prev !is GdsFile) {
                    val elementType = prev.node.elementType
                    if (elementType == GdsTypes.PARENTHESIS_OPEN) {
                        functionCall = prev.parent as? GdsFunctionCall
                        if (functionCall != null) break
                    }
                    if (elementType == GdsTypes.SEMICOLON || 
                        elementType == GdsTypes.CURLY_BRACKET_OPEN || 
                        elementType == GdsTypes.CURLY_BRACKET_CLOSE ||
                        elementType == GdsTypes.PARENTHESIS_CLOSE) {
                        break
                    }
                    prev = PsiTreeUtil.prevLeaf(prev)
                }
            }
            
            argumentList = functionCall?.argumentList
        }
        
        return argumentList
    }

    override fun showParameterInfo(element: GdsArgumentList, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset, this)
    }

    override fun updateParameterInfo(argumentList: GdsArgumentList, context: UpdateParameterInfoContext) {
        val offset = context.offset
        val index = ParameterInfoUtils.getCurrentParameterIndex(argumentList.node, offset, GdsTypes.COMMA)
        context.setCurrentParameter(index)
    }

    override fun updateUI(p: FunctionSpec, context: ParameterInfoUIContext) {
        val buffer = StringBuilder()
        
        var highlightStart = -1
        var highlightEnd = -1
        
        val currentParamIndex = context.currentParameterIndex
        
        for ((i, param) in p.parameters.withIndex()) {
            if (i > 0) buffer.append(", ")
            
            val start = buffer.length
            buffer.append(param.type.presentationText).append(" ").append(param.name)
            
            if (i == currentParamIndex) {
                highlightStart = start
                highlightEnd = buffer.length
            }
        }
        
        if (p.parameters.isEmpty()) {
            buffer.append("<no parameters>")
        }
        
        context.setupUIComponentPresentation(
            buffer.toString(),
            highlightStart,
            highlightEnd,
            !context.isUIComponentEnabled,
            false,
            false,
            context.defaultParameterColor
        )
    }
    
    private fun resolveCandidates(argumentList: GdsArgumentList): List<FunctionSpec> {
        val functionCall = argumentList.parent as? GdsFunctionCall ?: return emptyList()
        
        functionCall.type?.let { typeNode ->
            val typeText = typeNode.text
            val baseType = Builtins.getType(typeText) ?: resolveStructType(typeNode) ?: return emptyList()
            
            if (baseType is Instantiable) {
                return baseType.getConstructors()
            }
        }
        
        functionCall.functionNameRef?.let { nameRef ->
            val functionName = nameRef.text
            
            val resolved = nameRef.reference.resolve()
            if (resolved is GdsFunctionNameDecl) {
                val functionDecl = resolved.parent as? GdsFunctionDeclaration
                createFunctionSpec(functionDecl)?.let { return listOf(it) }
            }
            
            val shaderType = GdsPsiImplUtil.getShaderType(functionCall) ?: return emptyList()
            val functionContext = GdsPsiImplUtil.getFunctionContext(functionCall) ?: return emptyList()
            
            return Builtins.getFunctions(shaderType, functionContext, functionName) ?: emptyList()
        }
        
        return emptyList()
    }
    
    private fun resolveStructType(typeNode: GdsType): DataType? {
        val structRef = typeNode.structNameRef ?: return null
        val structDecl = structRef.reference.resolve() as? GdsStructNameDecl ?: return null
        val structDeclaration = structDecl.parent as? GdsStructDeclaration ?: return null
        
        val members = mutableMapOf<String, DataType>()
        structDeclaration.structBlock?.structMemberList?.structMemberList?.forEach { member ->
             val memberName = member.structMemberNameDecl?.text ?: return@forEach
             val memberTypeText = member.type.text
             val memberType = Builtins.getType(memberTypeText) ?: return@forEach
             members[memberName] = memberType
        }
        
        return StructType(structDecl.text, members)
    }

    private fun createFunctionSpec(declaration: GdsFunctionDeclaration?): FunctionSpec? {
        if (declaration == null) return null
        
        val functionName = declaration.functionNameDecl.text
        val returnType = GdsDataTypeFactory.createFromFunctionDeclaration(declaration) ?: VoidType
        
        val parameters = declaration.parameterList?.parameterList?.mapNotNull { param ->
            val paramName = param.variableNameDecl.text
            val paramType = GdsDataTypeFactory.createFromParameter(param) ?: return@mapNotNull null
            ParameterSpec(paramName, paramType)
        } ?: emptyList()
        
        return FunctionSpec(functionName, returnType, parameters)
    }
}
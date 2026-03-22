package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.Instantiable
import kr.jaehoyi.gdshader.model.StructType
import kr.jaehoyi.gdshader.psi.GdsExpressionTypeInference
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver
import kr.jaehoyi.gdshader.resolve.GdsResolver

class GdsFunctionCallAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdsFunctionCall) return
        if (element.containingFile?.virtualFile?.extension == "gdshaderinc") return

        element.type?.let { typeNode ->
            checkConstructorCall(element, typeNode.text, holder)
            return
        }

        element.functionNameRef?.let { nameRef ->
            checkFunctionCall(element, nameRef.text, holder)
        }
    }

    private fun checkConstructorCall(
        functionCall: GdsFunctionCall,
        typeName: String,
        holder: AnnotationHolder
    ) {
        val baseType = Builtins.getType(typeName) ?: return
        if (baseType !is Instantiable) return
        if (functionCall.arraySize != null) return

        val constructors = baseType.getConstructors()
        if (constructors.isEmpty()) return

        val argCount = getArgumentCount(functionCall)
        checkArguments(constructors, argCount, typeName, functionCall, holder)
    }

    private fun checkFunctionCall(
        functionCall: GdsFunctionCall,
        functionName: String,
        holder: AnnotationHolder
    ) {
        val candidates = collectCandidates(functionCall, functionName)
        if (candidates.isEmpty()) return

        val argCount = getArgumentCount(functionCall)
        checkArguments(candidates, argCount, functionName, functionCall, holder)
    }

    private fun collectCandidates(functionCall: GdsFunctionCall, functionName: String): List<FunctionSpec> {
        val nameRef = functionCall.functionNameRef ?: return emptyList()
        val resolved = nameRef.reference.resolve()

        if (resolved is GdsFunctionNameDecl) {
            val spec = resolved.functionSpec ?: return emptyList()
            return listOf(spec)
        }

        if (resolved is GdsFunction) {
            val shaderType = GdsPsiImplUtil.getShaderType(functionCall)
            val functionContext = GdsPsiImplUtil.getFunctionContext(functionCall)
            if (shaderType != null && functionContext != null) {
                return Builtins.getFunctions(shaderType, functionContext, functionName) ?: emptyList()
            }
            val spec = resolved.functionSpec ?: return emptyList()
            return listOf(spec)
        }

        val structType = resolveStructType(functionCall, functionName)
        if (structType is Instantiable) {
            return structType.getConstructors()
        }

        return emptyList()
    }

    private fun resolveStructType(element: PsiElement, name: String): DataType? {
        var result: DataType? = null
        GdsResolver.processStructDeclaration(element) { decl ->
            if (decl.name == name) {
                val structDeclaration = decl.parent as? GdsStructDeclaration
                if (structDeclaration != null) {
                    val members = mutableMapOf<String, DataType>()
                    structDeclaration.structBlock?.structMemberList?.structMemberList?.forEach { member ->
                        val memberName = member.structMemberNameDecl?.text ?: return@forEach
                        val memberType = Builtins.getType(member.type.text) ?: return@forEach
                        members[memberName] = memberType
                    }
                    result = StructType(name, members)
                }
                false
            } else {
                true
            }
        }
        return result
    }

    private fun getArgumentCount(functionCall: GdsFunctionCall): Int {
        val argList = functionCall.argumentList ?: return 0
        return argList.initializerList.size
    }

    private fun collectArgumentTypes(functionCall: GdsFunctionCall): List<DataType> {
        val argList = functionCall.argumentList ?: return emptyList()
        return argList.initializerList.mapNotNull { initializer ->
            initializer.expression?.let { GdsExpressionTypeInference.inferType(it) }
        }
    }

    private fun checkArguments(
        candidates: List<FunctionSpec>,
        argCount: Int,
        name: String,
        functionCall: GdsFunctionCall,
        holder: AnnotationHolder
    ) {
        val countMatches = candidates.filter { spec ->
            val requiredCount = spec.parameters.count { !it.isOptional }
            val totalCount = spec.parameters.size
            argCount in requiredCount..totalCount
        }

        if (countMatches.isEmpty()) {
            annotateArgumentCount(candidates, argCount, name, functionCall, holder)
            return
        }

        val argTypes = collectArgumentTypes(functionCall)
        if (argTypes.size != argCount) return

        val resolved = GdsOverloadResolver.resolveOverload(countMatches, argTypes)
        if (resolved != null) return

        val bestCandidate = countMatches.firstOrNull() ?: return
        for (i in argTypes.indices) {
            if (i >= bestCandidate.parameters.size) break
            val paramType = bestCandidate.parameters[i].type
            val argType = argTypes[i]
            if (paramType != argType) {
                val signature = formatSignature(name, bestCandidate)
                val argListElement = functionCall.argumentList ?: return
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "No matching function for '$signature' call: argument ${i + 1} should be ${paramType.name} but is ${argType.name}"
                ).range(argListElement).create()
                return
            }
        }
    }

    private fun annotateArgumentCount(
        candidates: List<FunctionSpec>,
        argCount: Int,
        name: String,
        functionCall: GdsFunctionCall,
        holder: AnnotationHolder
    ) {
        val minRequired = candidates.minOf { it.parameters.count { p -> !p.isOptional } }
        val maxTotal = candidates.maxOf { it.parameters.size }
        val tooFew = argCount < minRequired

        val bestCandidate = if (tooFew) {
            candidates.minByOrNull { it.parameters.count { p -> !p.isOptional } } ?: return
        } else {
            candidates.maxByOrNull { it.parameters.size } ?: return
        }

        val signature = formatSignature(name, bestCandidate)
        val bound = if (tooFew) "at least $minRequired" else "at most $maxTotal"

        val argListElement = functionCall.argumentList ?: return
        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Too ${if (tooFew) "few" else "many"} arguments for '$signature' call. Expected $bound but received $argCount."
        ).range(argListElement).create()
    }

    private fun formatSignature(name: String, spec: FunctionSpec): String {
        val params = spec.parameters.joinToString(", ") { it.type.name }
        return "$name($params)"
    }
}

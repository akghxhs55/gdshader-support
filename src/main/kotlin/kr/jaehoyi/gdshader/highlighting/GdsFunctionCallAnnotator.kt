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
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionCall
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
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
        checkArgumentCount(constructors, argCount, typeName, functionCall, holder)
    }

    private fun checkFunctionCall(
        functionCall: GdsFunctionCall,
        functionName: String,
        holder: AnnotationHolder
    ) {
        val candidates = collectCandidates(functionCall, functionName)
        if (candidates.isEmpty()) return

        val argCount = getArgumentCount(functionCall)
        checkArgumentCount(candidates, argCount, functionName, functionCall, holder)
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

    private fun checkArgumentCount(
        candidates: List<FunctionSpec>,
        argCount: Int,
        name: String,
        functionCall: GdsFunctionCall,
        holder: AnnotationHolder
    ) {
        val anyCountMatches = candidates.any { spec ->
            val requiredCount = spec.parameters.count { !it.isOptional }
            val totalCount = spec.parameters.size
            argCount in requiredCount..totalCount
        }

        if (anyCountMatches) return

        val minRequired = candidates.minOf { it.parameters.count { p -> !p.isOptional } }
        val tooFew = argCount < minRequired

        val validCounts = candidates.flatMap { spec ->
            val required = spec.parameters.count { !it.isOptional }
            val total = spec.parameters.size
            (required..total).toList()
        }.distinct().sorted()

        val expected = formatExpectedCounts(validCounts)

        val argListElement = functionCall.argumentList ?: return
        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Too ${if (tooFew) "few" else "many"} arguments for '$name': expected $expected, got $argCount"
        )
            .range(argListElement)
            .create()
    }

    private fun formatExpectedCounts(counts: List<Int>): String {
        if (counts.isEmpty()) return "0"
        if (counts.size == 1) return "${counts.first()}"

        // Collapse consecutive ranges: [2,3,4,6] -> "2..4 or 6"
        val ranges = mutableListOf<IntRange>()
        var start = counts.first()
        var end = start
        for (i in 1 until counts.size) {
            if (counts[i] == end + 1) {
                end = counts[i]
            } else {
                ranges.add(start..end)
                start = counts[i]
                end = start
            }
        }
        ranges.add(start..end)

        return ranges.joinToString(" or ") { range ->
            if (range.first == range.last) "${range.first}" else "${range.first}..${range.last}"
        }
    }
}

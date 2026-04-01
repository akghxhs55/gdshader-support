package kr.jaehoyi.gdshader.codeinsight

import com.intellij.codeInsight.hints.declarative.HintFormat
import com.intellij.codeInsight.hints.declarative.InlayHintsCollector
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider
import com.intellij.codeInsight.hints.declarative.InlayTreeSink
import com.intellij.codeInsight.hints.declarative.InlineInlayPosition
import com.intellij.codeInsight.hints.declarative.SharedBypassCollector
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import kr.jaehoyi.gdshader.model.*
import kr.jaehoyi.gdshader.psi.*
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

class GdsInlayHintsProvider : InlayHintsProvider {
    override fun createCollector(
        file: PsiFile,
        editor: Editor,
    ): InlayHintsCollector = Collector()

    private class Collector : SharedBypassCollector {
        override fun collectFromElement(
            element: PsiElement,
            sink: InlayTreeSink,
        ) {
            if (element !is GdsArgumentList) return

            val functionCall = element.parent as? GdsFunctionCall ?: return
            val arguments = element.initializerList
            if (arguments.isEmpty()) return

            val paramNames = resolveParameterNames(functionCall, element) ?: return

            for (i in arguments.indices) {
                if (i >= paramNames.size) break
                val paramName = paramNames[i] ?: continue
                val argument = arguments[i]
                sink.addPresentation(
                    position = InlineInlayPosition(argument.textRange.startOffset, relatedToPrevious = false),
                    hintFormat = HintFormat.default,
                ) {
                    text("$paramName:")
                }
            }
        }

        private fun resolveParameterNames(
            functionCall: GdsFunctionCall,
            argumentList: GdsArgumentList,
        ): List<String?>? {
            val argCount = argumentList.initializerList.size

            functionCall.type?.let { typeNode ->
                val baseType = Builtins.getType(typeNode.text) ?: resolveStructType(typeNode) ?: return null
                if (baseType is Instantiable) {
                    val best = pickBestOverload(baseType.getConstructors(), argCount) ?: return null
                    return best.parameters.map { it.name }
                }
                return null
            }

            functionCall.functionNameRef?.let { nameRef ->
                val resolved = nameRef.reference.resolve()
                if (resolved is GdsFunctionNameDecl) {
                    val decl = resolved.parent as? GdsFunctionDeclaration
                    return decl?.parameterList?.parameterList?.map { it.variableNameDecl.text }
                }

                val shaderType = GdsPsiImplUtil.getShaderType(functionCall) ?: return null
                val functionContext = GdsPsiImplUtil.getFunctionContext(functionCall) ?: return null
                val candidates = Builtins.getFunctions(shaderType, functionContext, nameRef.text) ?: return null
                val best = pickBestOverload(candidates, argCount) ?: return null
                return best.parameters.map { it.name }
            }

            return null
        }

        private fun resolveStructType(typeNode: GdsType): DataType? {
            val structRef = typeNode.structNameRef ?: return null
            val structDecl = structRef.reference.resolve() as? GdsStructNameDecl ?: return null
            val structDeclaration = structDecl.parent as? GdsStructDeclaration ?: return null

            val members = mutableMapOf<String, DataType>()
            structDeclaration.structBlock?.structMemberList?.structMemberList?.forEach { member ->
                val memberName = member.structMemberNameDecl?.text ?: return@forEach
                val memberType = Builtins.getType(member.type.text) ?: return@forEach
                members[memberName] = memberType
            }

            return StructType(structDecl.text, members)
        }

        private fun pickBestOverload(
            candidates: List<FunctionSpec>,
            argCount: Int,
        ): FunctionSpec? =
            candidates.firstOrNull { spec ->
                val required = spec.parameters.count { !it.isOptional }
                argCount in required..spec.parameters.size
            } ?: candidates.firstOrNull()
    }
}

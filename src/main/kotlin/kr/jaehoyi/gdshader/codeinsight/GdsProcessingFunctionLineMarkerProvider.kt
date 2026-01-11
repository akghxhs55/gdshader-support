package kr.jaehoyi.gdshader.codeinsight

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.GdsBundle
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil

class GdsProcessingFunctionLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element.node.elementType != GdsTypes.IDENTIFIER) {
            return null
        }
        
        if (element.parent !is GdsFunctionNameDecl) {
            return null
        }
        
        val functionContext = FunctionContext.fromText(element.text)
        
        val file = element.containingFile as? GdsFile ?: return null
        val shaderType = GdsPsiImplUtil.getShaderType(file) ?: return null
        
        val validProcessingFunctions = Builtins.PROCESSING_FUNCTIONS[shaderType] ?: return null
        if (functionContext !in validProcessingFunctions) {
            return null
        }
        
        return LineMarkerInfo(
            element,
            element.textRange,
            AllIcons.Gutter.OverridingMethod,
            { GdsBundle.message("gdshader.processing.function.tooltip", functionContext.text) },
            { _, _ -> openDocumentation(shaderType, functionContext) },
            GutterIconRenderer.Alignment.LEFT,
            { GdsBundle.message("gdshader.processing.function.accessible.name", functionContext.text) }
        )
    }
    
    private fun openDocumentation(shaderType: ShaderType, functionContext: FunctionContext) {
        val baseUrl = "https://docs.godotengine.org/en/stable/tutorials/shaders/shader_reference"
        val shaderTypeUrl = when (shaderType) {
            ShaderType.SPATIAL -> "spatial_shader.html"
            ShaderType.CANVAS_ITEM -> "canvas_item_shader.html"
            ShaderType.PARTICLES -> "particle_shader.html"
            ShaderType.SKY -> "sky_shader.html"
            ShaderType.FOG -> "fog_shader.html"
        }
        val functionUrl = when (functionContext) {
            FunctionContext.VERTEX -> "vertex-built-ins"
            FunctionContext.FRAGMENT -> "fragment-built-ins"
            FunctionContext.LIGHT -> "light-built-ins"
            FunctionContext.START -> "start-built-ins"
            FunctionContext.PROCESS -> "process-built-ins"
            FunctionContext.SKY -> "sky-built-ins"
            FunctionContext.FOG -> "fog-built-ins"
            else -> return
        }
        
        val fullUrl = "$baseUrl/$shaderTypeUrl#$functionUrl"
        BrowserUtil.browse(fullUrl)
    }
    
}
package kr.jaehoyi.gdshader.codeinsight

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.GDShaderBundle
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.GDShaderBuiltins
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderFunctionNameDecl
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.impl.GDShaderPsiImplUtil

class GDShaderProcessingFunctionLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element.node.elementType != GDShaderTypes.IDENTIFIER) {
            return null
        }
        
        if (element.parent !is GDShaderFunctionNameDecl) {
            return null
        }
        
        val functionContext = FunctionContext.fromText(element.text) ?: return null
        
        val file = element.containingFile as? GDShaderFile ?: return null
        val shaderType = GDShaderPsiImplUtil.getShaderType(file) ?: return null
        
        val validProcessingFunctions = GDShaderBuiltins.PROCESSING_FUNCTIONS[shaderType] ?: return null
        if (functionContext !in validProcessingFunctions) {
            return null
        }
        
        return LineMarkerInfo(
            element,
            element.textRange,
            AllIcons.Gutter.OverridingMethod,
            { GDShaderBundle.message("gdshader.processing.function.tooltip", functionContext.text) },
            { _, _ -> openDocumentation(shaderType, functionContext) },
            GutterIconRenderer.Alignment.LEFT,
            { GDShaderBundle.message("gdshader.processing.function.accessible.name", functionContext.text) }
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
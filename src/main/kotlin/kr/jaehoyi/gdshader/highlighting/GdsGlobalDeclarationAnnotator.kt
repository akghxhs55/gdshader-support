package kr.jaehoyi.gdshader.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.completion.GdsKeywords
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.MatrixType
import kr.jaehoyi.gdshader.model.SamplerType
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.psi.GdsHint
import kr.jaehoyi.gdshader.psi.GdsRenderModeName
import kr.jaehoyi.gdshader.psi.GdsShaderTypeDeclaration
import kr.jaehoyi.gdshader.psi.GdsShaderTypeName
import kr.jaehoyi.gdshader.psi.GdsStencilModeName
import kr.jaehoyi.gdshader.psi.GdsTypes
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsUniformGroupDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsConditionalDirectives

class GdsGlobalDeclarationAnnotator :
    Annotator,
    DumbAware {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (!GdsInspectionUtil.isEnabled(element, GdsInspectionUtil.DECLARATION_VALIDATION)) return

        when (element) {
            is GdsShaderTypeName -> checkShaderType(element, holder)
            is GdsShaderTypeDeclaration -> checkSecondShaderType(element, holder)
            is GdsRenderModeName -> checkMode(element, isStencil = false, holder)
            is GdsStencilModeName -> checkMode(element, isStencil = true, holder)
            is GdsUniformGroupDeclaration -> checkUniformGroup(element, holder)
            is GdsUniformDeclaration -> {
                checkUniformQualifier(element, holder)
                checkHints(element, holder)
            }
        }
    }

    private fun checkShaderType(
        element: GdsShaderTypeName,
        holder: AnnotationHolder,
    ) {
        if (ShaderType.fromText(element.text) != null) return
        val validTypes = ShaderType.entries.joinToString(", ") { "'${it.text}'" }
        error(holder, element, "Invalid shader type. Valid types are: $validTypes")
    }

    private fun checkSecondShaderType(
        element: GdsShaderTypeDeclaration,
        holder: AnnotationHolder,
    ) {
        val first = PsiTreeUtil.findChildrenOfType(element.containingFile, GdsShaderTypeDeclaration::class.java).firstOrNull()
        if (first != null && first !== element) error(holder, element, "Shader type is already defined.")
    }

    // Godot matches a mode by prefix, so for example 'unshaded_foo' is accepted as 'unshaded'.
    private fun checkMode(
        element: PsiElement,
        isStencil: Boolean,
        holder: AnnotationHolder,
    ) {
        val file = element.containingFile ?: return
        val kind = if (isStencil) "stencil" else "render"
        val modeClass = if (isStencil) GdsStencilModeName::class.java else GdsRenderModeName::class.java
        val infos = modeInfos(file, isStencil)

        val seen = mutableSetOf<String>()
        val groups = mutableMapOf<String, String>()
        var referenceValue: Long? = null

        val modes = PsiTreeUtil.findChildrenOfType(file, modeClass).filter { !GdsConditionalDirectives.areExclusive(it, element) }
        for (current in modes) {
            val text = current.text
            val message =
                if (current.node.firstChildNode?.elementType == GdsTypes.INT_CONSTANT) {
                    val value = text.toLongOrNull() ?: continue
                    when {
                        referenceValue != null -> "Duplicated stencil mode reference value: '$value'."
                        value > 255 -> "Stencil mode reference value cannot be greater than 255: '$value'."
                        else -> {
                            referenceValue = value
                            null
                        }
                    }
                } else if (!seen.add(text)) {
                    "Duplicated $kind mode: '$text'."
                } else {
                    val info = infos.firstOrNull { info -> text.startsWith(info.name) && (info.options.isEmpty() || text.removePrefix(info.name + "_") in info.options) }
                    when {
                        info == null -> "Invalid $kind mode: '$text'."
                        info.options.isEmpty() -> null
                        info.name in groups -> "Redefinition of $kind mode: '$text'. The '${info.name}' mode has already been set to '${groups[info.name]}'."
                        else -> {
                            groups[info.name] = text
                            null
                        }
                    }
                }
            if (current === element) {
                message?.let { error(holder, element, it) }
                return
            }
        }
    }

    private fun modeInfos(
        file: PsiFile,
        isStencil: Boolean,
    ): List<GdsKeywords.ModeInfo> {
        val byType = if (isStencil) GdsKeywords.STENCIL_MODE_INFOS else GdsKeywords.RENDER_MODE_INFOS
        val shaderType = GdsPsiImplUtil.getShaderType(file)
        return if (shaderType != null) byType[shaderType].orEmpty() else byType.values.flatten()
    }

    private fun checkUniformGroup(
        element: GdsUniformGroupDeclaration,
        holder: AnnotationHolder,
    ) {
        val names = element.uniformGroupNameList
        if (names.isNotEmpty()) {
            if (element.node.findChildByType(GdsTypes.PERIOD)?.let { it.startOffset < names.first().textRange.startOffset } == true) {
                error(holder, element, "Expected an uniform group identifier.")
            }
            return
        }
        if (element.node.findChildByType(GdsTypes.PERIOD) != null) {
            error(holder, element, "Expected an uniform group identifier.")
            return
        }

        val previous =
            PsiTreeUtil
                .findChildrenOfType(element.containingFile, GdsUniformGroupDeclaration::class.java)
                .takeWhile { it !== element }
                .lastOrNull()
        if (previous == null || previous.uniformGroupNameList.isEmpty()) {
            error(holder, element, "Group needs to be opened before.")
        }
    }

    private fun checkUniformQualifier(
        element: GdsUniformDeclaration,
        holder: AnnotationHolder,
    ) {
        val qualifier = element.uniformHeader.uniformQualifier ?: return
        val type = element.type?.text?.let { Builtins.getType(it) }
        val isArray = element.arraySizeList.isNotEmpty()
        val message =
            when (qualifier.text) {
                "instance" -> {
                    val shaderType = GdsPsiImplUtil.getShaderType(element)
                    when {
                        shaderType != null && shaderType != ShaderType.SPATIAL && shaderType != ShaderType.CANVAS_ITEM ->
                            "Uniform instances are not yet implemented for '${shaderType.text}' shaders."
                        type is SamplerType -> "The 'SCOPE_INSTANCE' qualifier is not supported for sampler types."
                        type is MatrixType -> "The 'SCOPE_INSTANCE' qualifier is not supported for matrix types."
                        isArray -> "The 'SCOPE_INSTANCE' qualifier is not supported for uniform arrays."
                        tooManyInstanceUniforms(element) ->
                            "Too many 'instance' uniforms in shader, maximum supported is $MAX_INSTANCE_UNIFORM_INDICES."
                        else -> null
                    }
                }
                "global" -> if (isArray) "The 'SCOPE_GLOBAL' qualifier is not supported for uniform arrays." else null
                else -> null
            } ?: return
        error(holder, qualifier, message)
    }

    private fun checkHints(
        element: GdsUniformDeclaration,
        holder: AnnotationHolder,
    ) {
        val hints = element.hintSection?.hintList?.hintList ?: return
        val type = Builtins.getType(element.type?.text ?: return) ?: return
        val isSampler = type is SamplerType
        val isArray = element.arraySizeList.isNotEmpty()
        val shaderType = GdsPsiImplUtil.getShaderType(element)

        var hint: String? = null
        var filter: String? = null
        var repeat: String? = null
        var sourceColorOnSampler = false
        var instanceIndex = false

        for (current in hints) {
            val name = hintName(current)
            val message =
                when {
                    isArray && name !in ARRAY_HINTS -> "This hint is not supported for uniform arrays."
                    name == "source_color" && type != Builtins.VEC3 && type != Builtins.VEC4 && !isSampler ->
                        "Source color hint is for 'vec3', 'vec4' or sampler types only."
                    name == "source_color" && isSampler ->
                        if (sourceColorOnSampler) "Duplicated hint: 'source_color'." else null.also { sourceColorOnSampler = true }
                    name == "color_conversion_disabled" && type != Builtins.VEC3 && type != Builtins.VEC4 ->
                        "Source color conversion disabled hint is for 'vec3', 'vec4'."
                    name == "color_conversion_disabled" && hint != "source_color" ->
                        "Hint 'color_conversion_disabled' should be preceded by 'source_color'."
                    name == "color_conversion_disabled" -> null.also { hint = name }
                    name == "hint_range" && type != Builtins.FLOAT && type != Builtins.INT -> "Range hint is for 'float' and 'int' only."
                    name == "hint_enum" && type != Builtins.INT -> "Enum hint is for 'int' only."
                    name == "instance_index" && instanceIndex -> "Can only specify 'instance_index' once."
                    name == "instance_index" ->
                        instanceIndexMessage(current).also { instanceIndex = true }
                    name == "hint_screen_texture" && shaderType != null && shaderType != ShaderType.SPATIAL && shaderType != ShaderType.CANVAS_ITEM ->
                        "'hint_screen_texture' is not supported in '${shaderType.text}' shaders."
                    name == "hint_normal_roughness_texture" && shaderType != null && shaderType != ShaderType.SPATIAL ->
                        "'hint_normal_roughness_texture' is not supported in '${shaderType.text}' shaders."
                    name == "hint_depth_texture" && shaderType != null && shaderType != ShaderType.SPATIAL ->
                        "'hint_depth_texture' is not supported in '${shaderType.text}' shaders."
                    name.startsWith("hint_blit_source") && shaderType != null && shaderType != ShaderType.TEXTURE_BLIT ->
                        "'hint_blit_source' is not supported in '${shaderType.text}' shaders."
                    (name in SAMPLER_HINTS || name.startsWith("filter_") || name.startsWith("repeat_")) && !isSampler ->
                        "This hint is only for sampler types."
                    name.startsWith("filter_") -> conflict("filter mode", name, filter).also { if (filter == null) filter = name }
                    name.startsWith("repeat_") -> conflict("repeat mode", name, repeat).also { if (repeat == null) repeat = name }
                    name == "source_color" || name in VALUE_HINTS || name in SAMPLER_HINTS ->
                        conflict("hint", name, hint).also { if (hint == null) hint = name }
                    else -> null
                }
            if (message != null) {
                error(holder, current, message)
                return
            }
        }
    }

    private fun instanceIndexMessage(hint: GdsHint): String? {
        val constant = hint.instanceIndexHint?.node?.findChildByType(TokenSet.create(GdsTypes.INT_CONSTANT, GdsTypes.UINT_CONSTANT)) ?: return null
        val digits = constant.text.trimEnd('u', 'U')
        val value = if (digits.startsWith("0x", ignoreCase = true)) digits.substring(2).toLongOrNull(16) else digits.toLongOrNull()
        return if (value == null || value >= MAX_INSTANCE_UNIFORM_INDICES) {
            "Allowed instance uniform indices must be within [0..${MAX_INSTANCE_UNIFORM_INDICES - 1}] range."
        } else {
            null
        }
    }

    private fun hasInstanceIndex(element: GdsUniformDeclaration): Boolean =
        element.hintSection
            ?.hintList
            ?.hintList
            ?.any { it.instanceIndexHint != null } == true

    private fun tooManyInstanceUniforms(element: GdsUniformDeclaration): Boolean {
        if (hasInstanceIndex(element)) return false
        val autoIndexed =
            PsiTreeUtil
                .findChildrenOfType(element.containingFile, GdsUniformDeclaration::class.java)
                .takeWhile { it !== element }
                .count { it.uniformHeader.uniformQualifier?.text == "instance" && !hasInstanceIndex(it) }
        return autoIndexed >= MAX_INSTANCE_UNIFORM_INDICES
    }

    private fun conflict(
        kind: String,
        name: String,
        previous: String?,
    ): String? =
        when (previous) {
            null -> null
            name -> "Duplicated $kind: '$name'."
            else -> "Redefinition of $kind: '$name'. The $kind has already been set to '$previous'."
        }

    private fun hintName(hint: GdsHint): String = hint.text.substringBefore('(').trim()

    private fun error(
        holder: AnnotationHolder,
        element: PsiElement,
        message: String,
    ) {
        holder
            .newAnnotation(HighlightSeverity.ERROR, message)
            .range(element)
            .create()
    }

    private companion object {
        // MAX_INSTANCE_UNIFORM_INDICES in servers/rendering/shader_language.h (Godot 4.7.2)
        const val MAX_INSTANCE_UNIFORM_INDICES = 16

        val ARRAY_HINTS =
            setOf(
                "source_color",
                "color_conversion_disabled",
                "repeat_disable",
                "repeat_enable",
                "filter_linear",
                "filter_linear_mipmap",
                "filter_linear_mipmap_anisotropic",
                "filter_nearest",
                "filter_nearest_mipmap",
                "filter_nearest_mipmap_anisotropic",
            )

        val VALUE_HINTS = setOf("hint_range", "hint_enum")

        val SAMPLER_HINTS =
            setOf(
                "hint_default_black",
                "hint_default_white",
                "hint_default_transparent",
                "hint_normal",
                "hint_roughness_normal",
                "hint_roughness_r",
                "hint_roughness_g",
                "hint_roughness_b",
                "hint_roughness_a",
                "hint_roughness_gray",
                "hint_anisotropy",
                "hint_screen_texture",
                "hint_normal_roughness_texture",
                "hint_depth_texture",
                "hint_blit_source0",
                "hint_blit_source1",
                "hint_blit_source2",
                "hint_blit_source3",
            )
    }
}

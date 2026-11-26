package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.InterpolationQualifier
import kr.jaehoyi.gdshader.model.ParameterQualifier
import kr.jaehoyi.gdshader.model.Precision
import kr.jaehoyi.gdshader.model.SamplerType
import kr.jaehoyi.gdshader.model.ShaderType

object GdsKeywords {
    val SHADER_TYPES = ShaderType.entries.map { it.text }.toSet()

    data class ModeInfo(
        val name: String,
        val options: List<String> = emptyList(),
    ) {
        val values: List<String> get() = if (options.isEmpty()) listOf(name) else options.map { "${name}_$it" }
    }

    // Mirrors the modes and stencil_modes in servers/rendering/shader_types.cpp (Godot 4.7.2).
    val RENDER_MODE_INFOS: Map<ShaderType, List<ModeInfo>> =
        mapOf(
            ShaderType.SPATIAL to
                listOf(
                    ModeInfo("blend", listOf("mix", "add", "sub", "mul", "premul_alpha")),
                    ModeInfo("depth_draw", listOf("opaque", "always", "never")),
                    ModeInfo("depth_prepass_alpha"),
                    ModeInfo("depth_test", listOf("default", "disabled", "inverted")),
                    ModeInfo("sss_mode_skin"),
                    ModeInfo("cull", listOf("back", "front", "disabled")),
                    ModeInfo("unshaded"),
                    ModeInfo("wireframe"),
                    ModeInfo("diffuse", listOf("lambert", "lambert_wrap", "burley", "toon")),
                    ModeInfo("specular", listOf("schlick_ggx", "toon", "disabled")),
                    ModeInfo("skip_vertex_transform"),
                    ModeInfo("world_vertex_coords"),
                    ModeInfo("ensure_correct_normals"),
                    ModeInfo("shadows_disabled"),
                    ModeInfo("ambient_light_disabled"),
                    ModeInfo("shadow_to_opacity"),
                    ModeInfo("vertex_lighting"),
                    ModeInfo("particle_trails"),
                    ModeInfo("alpha_to_coverage"),
                    ModeInfo("alpha_to_coverage_and_one"),
                    ModeInfo("debug_shadow_splits"),
                    ModeInfo("fog_disabled"),
                    ModeInfo("specular_occlusion_disabled"),
                ),
            ShaderType.CANVAS_ITEM to
                listOf(
                    ModeInfo("skip_vertex_transform"),
                    ModeInfo("blend", listOf("mix", "add", "sub", "mul", "premul_alpha", "disabled")),
                    ModeInfo("unshaded"),
                    ModeInfo("light_only"),
                    ModeInfo("world_vertex_coords"),
                ),
            ShaderType.PARTICLES to
                listOf(
                    ModeInfo("collision_use_scale"),
                    ModeInfo("disable_force"),
                    ModeInfo("disable_velocity"),
                    ModeInfo("keep_data"),
                ),
            ShaderType.SKY to
                listOf(
                    ModeInfo("use_half_res_pass"),
                    ModeInfo("use_quarter_res_pass"),
                    ModeInfo("disable_fog"),
                    ModeInfo("use_debanding"),
                ),
            ShaderType.FOG to listOf(),
            ShaderType.TEXTURE_BLIT to listOf(ModeInfo("blend", listOf("mix", "add", "sub", "mul", "disabled"))),
        )

    val STENCIL_MODE_INFOS: Map<ShaderType, List<ModeInfo>> =
        ShaderType.entries.associateWith { shaderType ->
            if (shaderType == ShaderType.SPATIAL) {
                listOf(
                    ModeInfo("read"),
                    ModeInfo("write"),
                    ModeInfo("write_depth_fail"),
                    ModeInfo("compare", listOf("always", "less", "equal", "less_or_equal", "greater", "not_equal", "greater_or_equal")),
                )
            } else {
                listOf()
            }
        }

    val RENDER_MODES: Map<ShaderType, Set<String>> = RENDER_MODE_INFOS.mapValues { (_, infos) -> infos.flatMap { it.values }.toSet() }

    val STENCIL_MODES: Map<ShaderType, Set<String>> = STENCIL_MODE_INFOS.mapValues { (_, infos) -> infos.flatMap { it.values }.toSet() }

    // Mirrors the COMPLETION_HINT case of ShaderLanguage::complete_code.
    private val SAMPLER_FILTER_AND_REPEAT_HINTS =
        listOf(
            "filter_linear",
            "filter_linear_mipmap",
            "filter_linear_mipmap_anisotropic",
            "filter_nearest",
            "filter_nearest_mipmap",
            "filter_nearest_mipmap_anisotropic",
            "repeat_enable",
            "repeat_disable",
        )

    private val SAMPLER_HINTS =
        SAMPLER_FILTER_AND_REPEAT_HINTS +
            listOf(
                "hint_anisotropy",
                "hint_default_black",
                "hint_default_white",
                "hint_default_transparent",
                "hint_normal",
                "hint_roughness_a",
                "hint_roughness_b",
                "hint_roughness_g",
                "hint_roughness_gray",
                "hint_roughness_normal",
                "hint_roughness_r",
                "hint_screen_texture",
                "hint_normal_roughness_texture",
                "hint_depth_texture",
                "source_color",
            )

    fun getUniformHints(
        type: DataType,
        isArray: Boolean,
    ): List<String> {
        val hints =
            when {
                type == Builtins.VEC3 || type == Builtins.VEC4 -> listOf("source_color", "color_conversion_disabled")
                isArray && (type == Builtins.INT || type == Builtins.FLOAT) -> emptyList()
                type == Builtins.INT -> listOf("hint_range", "hint_enum")
                type == Builtins.FLOAT -> listOf("hint_range")
                type is SamplerType && isArray -> SAMPLER_FILTER_AND_REPEAT_HINTS + "source_color"
                type is SamplerType -> SAMPLER_HINTS
                else -> emptyList()
            }
        return if (isArray) hints else hints + "instance_index"
    }

    val PRECISIONS =
        Precision.entries
            .filter { it != Precision.DEFAULT }
            .map { it.text }
            .toSet()

    val INTERPOLATIONS = InterpolationQualifier.entries.map { it.text }.toSet()

    val BOOLEAN_LITERALS = setOf("true", "false")

    val PARAMETER_QUALIFIERS = ParameterQualifier.entries.map { it.text }.toSet()
}

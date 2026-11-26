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

    val RENDER_MODES =
        mapOf(
            ShaderType.SPATIAL to
                setOf(
                    "blend_mix",
                    "blend_add",
                    "blend_sub",
                    "blend_mul",
                    "blend_premul_alpha",
                    "depth_draw_opaque",
                    "depth_draw_always",
                    "depth_draw_never",
                    "depth_prepass_alpha",
                    "depth_test_default",
                    "depth_test_disabled",
                    "depth_test_inverted",
                    "specular_occlusion_disabled",
                    "sss_mode_skin",
                    "cull_back",
                    "cull_front",
                    "cull_disabled",
                    "unshaded",
                    "wireframe",
                    "debug_shadow_splits",
                    "diffuse_burley",
                    "diffuse_lambert",
                    "diffuse_lambert_wrap",
                    "diffuse_toon",
                    "specular_schlick_ggx",
                    "specular_toon",
                    "specular_disabled",
                    "skip_vertex_transform",
                    "world_vertex_coords",
                    "ensure_correct_normals",
                    "shadows_disabled",
                    "ambient_light_disabled",
                    "shadow_to_opacity",
                    "vertex_lighting",
                    "particle_trails",
                    "alpha_to_coverage",
                    "alpha_to_coverage_and_one",
                    "fog_disabled",
                ),
            ShaderType.CANVAS_ITEM to
                setOf(
                    "blend_mix",
                    "blend_add",
                    "blend_sub",
                    "blend_mul",
                    "blend_premul_alpha",
                    "blend_disabled",
                    "unshaded",
                    "light_only",
                    "skip_vertex_transform",
                    "world_vertex_coords",
                ),
            ShaderType.PARTICLES to setOf("keep_data", "disable_force", "disable_velocity", "collision_use_scale"),
            ShaderType.SKY to setOf("use_half_res_pass", "use_quarter_res_pass", "disable_fog", "use_debanding"),
            ShaderType.FOG to setOf(),
            ShaderType.TEXTURE_BLIT to setOf("blend_mix", "blend_add", "blend_sub", "blend_mul", "blend_disabled"),
        )

    val STENCIL_MODES =
        mapOf(
            ShaderType.SPATIAL to
                setOf(
                    "read",
                    "write",
                    "write_depth_fail",
                    "compare_always",
                    "compare_less",
                    "compare_equal",
                    "compare_not_equal",
                    "compare_less_or_equal",
                    "compare_greater",
                    "compare_greater_or_equal",
                ),
            ShaderType.CANVAS_ITEM to setOf(),
            ShaderType.PARTICLES to setOf(),
            ShaderType.SKY to setOf(),
            ShaderType.FOG to setOf(),
            ShaderType.TEXTURE_BLIT to setOf(),
        )

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

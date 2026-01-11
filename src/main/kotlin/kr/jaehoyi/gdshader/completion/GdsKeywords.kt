package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.InterpolationQualifier
import kr.jaehoyi.gdshader.model.ParameterQualifier
import kr.jaehoyi.gdshader.model.Precision
import kr.jaehoyi.gdshader.model.ShaderType

object GdsKeywords {
    
    val SHADER_TYPES = ShaderType.entries.map { it.text }.toSet()
    
    val RENDER_MODES = mapOf(
        ShaderType.SPATIAL to setOf("blend_mix", "blend_add", "blend_sub", "blend_mul", "blend_premul_alpha",
            "depth_draw_opaque", "depth_draw_always", "depth_draw_never", "depth_prepass_alpha", "depth_test_default",
            "depth_test_disabled", "depth_test_inverted", "specular_occlusion_disabled", "sss_mode_skin", "cull_back",
            "cull_front", "cull_disabled", "unshaded", "wireframe", "debug_shadow_splits", "diffuse_burley",
            "diffuse_lambert", "diffuse_lambert_wrap", "diffuse_toon", "specular_schlick_ggx", "specular_toon", 
            "specular_disabled", "skip_vertex_transform", "world_vertex_coords", "ensure_correct_normals", 
            "shadows_disabled", "ambient_light_disabled", "shadow_to_opacity", "vertex_lighting", "particle_trails",
            "alpha_to_coverage", "alpha_to_coverage_and_one", "fog_disabled"),
        ShaderType.CANVAS_ITEM to setOf("blend_mix", "blend_add", "blend_sub", "blend_mul", "blend_premul_alpha",
            "blend_disabled", "unshaded", "light_only", "skip_vertex_transform", "world_vertex_coords"),
        ShaderType.PARTICLES to setOf("keep_data", "disable_force", "disable_velocity", "collision_use_scale"),
        ShaderType.SKY to setOf("use_half_res_pass", "use_quarter_res_pass", "disable_fog"),
        ShaderType.FOG to setOf()
    )
    
    val STENCIL_MODES = mapOf(
        ShaderType.SPATIAL to setOf("read", "write", "write_depth_fail", "compare_always", "compare_less", "compare_equal",
            "compare_less_or_equal", "compare_greater", "compare_greater_or_equal"),
        ShaderType.CANVAS_ITEM to setOf(),
        ShaderType.PARTICLES to setOf(),
        ShaderType.SKY to setOf(),
        ShaderType.FOG to setOf()
    )
    
    val UNIFORM_HINTS = mapOf(
        Builtins.VEC3 to setOf("instance_index", "source_color", "color_conversion_disabled"),
        Builtins.VEC4 to setOf("instance_index", "source_color", "color_conversion_disabled"),
        Builtins.INT to setOf("instance_index", "hint_enum", "hint_range"),
        Builtins.FLOAT to setOf("instance_index", "hint_range"),
        Builtins.SAMPLER2D to setOf("instance_index", "source_color", "hint_normal", "hint_default_white",
            "hint_default_black", "hint_default_transparent", "hint_anisotropy", "hint_roughness_r", "hint_roughness_g",
            "hint_roughness_b", "hint_roughness_a", "hint_roughness_normal", "hint_roughness_gray", "filter_nearest",
            "filter_linear", "filter_nearest_mipmap", "filter_linear_mipmap", "hint_nearest_mipmap_anisotropic",
            "hint_linear_mipmap_anisotropic", "repeat_enable", "repeat_disable", "hint_screen_texture",
            "hint_depth_texture", "hint_normal_roughness_texture")
    )
    
    val PRECISIONS = Precision.entries.filter { it != Precision.DEFAULT }.map { it.text }.toSet()
    
    val INTERPOLATIONS = InterpolationQualifier.entries.map { it.text }.toSet()
    
    val BOOLEAN_LITERALS = setOf("true", "false")
    
    val PARAMETER_QUALIFIERS = ParameterQualifier.entries.map { it.text }.toSet()
    
}
package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.util.GDShaderDataType

object GDShaderKeywords {
    
    val SHADER_TYPES = setOf("spatial", "canvas_item", "particles", "sky", "fog")
    
    val RENDER_MODES = mapOf(
        "spatial" to setOf("blend_mix", "blend_add", "blend_sub", "blend_mul", "blend_premul_alpha",
            "depth_draw_opaque", "depth_draw_always", "depth_draw_never", "depth_prepass_alpha", "depth_test_default",
            "depth_test_disabled", "depth_test_inverted", "specular_occlusion_disabled", "sss_mode_skin", "cull_back",
            "cull_front", "cull_disabled", "unshaded", "wireframe", "debug_shadow_splits", "diffuse_burley",
            "diffuse_lambert", "diffuse_lambert_wrap", "diffuse_toon", "specular_schlick_ggx", "specular_toon", 
            "specular_disabled", "skip_vertex_transform", "world_vertex_coords", "ensure_correct_normals", 
            "shadows_disabled", "ambient_light_disabled", "shadow_to_opacity", "vertex_lighting", "particle_trails",
            "alpha_to_coverage", "alpha_to_coverage_and_one", "fog_disabled"),
        "canvas_item" to setOf("blend_mix", "blend_add", "blend_sub", "blend_mul", "blend_premul_alpha",
            "blend_disabled", "unshaded", "light_only", "skip_vertex_transform", "world_vertex_coords"),
        "particles" to setOf("keep_data", "disable_force", "disable_velocity", "collision_use_scale"),
        "sky" to setOf("use_half_res_pass", "use_quarter_res_pass", "disable_fog"),
        "fog" to setOf()
    )
    
    val STENCIL_MODES = mapOf(
        "spatial" to setOf("read", "write", "write_depth_fail", "compare_always", "compare_less", "compare_equal",
            "compare_less_or_equal", "compare_greater", "compare_greater_or_equal"),
        "canvas_item" to setOf(),
        "particles" to setOf(),
        "sky" to setOf(),
        "fog" to setOf()
    )
    
    val UNIFORM_HINTS = mapOf(
        GDShaderDataType.VEC3 to setOf("instance_index", "source_color", "color_conversion_disabled"),
        GDShaderDataType.VEC4 to setOf("instance_index", "source_color", "color_conversion_disabled"),
        GDShaderDataType.INT to setOf("instance_index", "hint_enum", "hint_range"),
        GDShaderDataType.FLOAT to setOf("instance_index", "hint_range"),
        GDShaderDataType.SAMPLER2D to setOf("instance_index", "source_color", "hint_normal", "hint_default_white",
            "hint_default_black", "hint_default_transparent", "hint_anisotropy", "hint_roughness_r", "hint_roughness_g",
            "hint_roughness_b", "hint_roughness_a", "hint_roughness_normal", "hint_roughness_gray", "filter_nearest",
            "filter_linear", "filter_nearest_mipmap", "filter_linear_mipmap", "hint_nearest_mipmap_anisotropic",
            "hint_linear_mipmap_anisotropic", "repeat_enable", "repeat_disable", "hint_screen_texture",
            "hint_depth_texture", "hint_normal_roughness_texture")
    )
    
    val BUILTIN_TYPES = GDShaderDataType.entries.filter { it.isInstantiable }.map { it.text }
    
    val PRECISIONS = setOf("highp", "mediump", "lowp")
    
    val INTERPOLATIONS = setOf("flat", "smooth")
    
    val BOOLEAN_LITERALS = setOf("true", "false")
    
    val PARAMETER_QUALIFIERS = setOf("in", "out", "inout")
    
    val CONTROL_STATEMENT_STARTERS = setOf(
        "if", "for", "while", "do", "switch"
    )
    
}
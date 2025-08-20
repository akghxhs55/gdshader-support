package kr.jaehoyi.gdshader

import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration

object GDShaderUtil {
    val shaderTypes = setOf("spatial", "canvas_item", "particles", "sky", "fog")
    
    val renderModeMap = mapOf(
        "spatial" to setOf("blend_mix", "blend_add", "blend_sub", "blend_mul", "blend_premul_alpha",
            "depth_draw_opaque", "depth_draw_always", "depth_draw_never", "depth_prepass_alpha", "depth_test_disabled",
            "sss_mode_skin", "cull_back", "cull_front", "cull_disabled", "unshaded", "wireframe", "debug_shadow_splits",
            "diffuse_burley", "diffuse_lambert", "diffuse_lambert_wrap", "diffuse_toon", "specular_schlick_ggx",
            "specular_toon", "specular_disabled", "skip_vertex_transform", "world_vertex_coords", "ensure_correct_normals",
            "shadows_disabled", "ambient_light_disabled", "shadow_to_opacity", "vertex_lighting", "particle_trails",
            "alpha_to_coverage", "alpha_to_coverage_and_one", "fog_disabled"),
        "canvas_item" to setOf("blend_mix", "blend_add", "blend_sub", "blend_mul", "blend_premul_alpha",
            "blend_disabled", "unshaded", "light_only", "skip_vertex_transform", "world_vertex_coords"),
        "particles" to setOf("keep_data", "disable_force", "disable_velocity", "collision_use_scale"),
        "sky" to setOf("use_half_res_pass", "use_quarter_res_pass", "disable_fog"),
        "fog" to setOf()
    )
    
    val uniformHintMap = mapOf(
        "vec3" to setOf("instance_index", "source_color", "color_conversion_disabled"),
        "vec4" to setOf("instance_index", "source_color", "color_conversion_disabled"),
        "int" to setOf("instance_index", "hint_enum", "hint_range"),
        "float" to setOf("instance_index", "hint_range"),
        "sampler2D" to setOf("instance_index", "source_color", "hint_normal", "hint_default_white",
            "hint_default_black", "hint_default_transparent", "hint_anisotropy", "hint_roughness_r", "hint_roughness_g",
            "hint_roughness_b", "hint_roughness_a", "hint_roughness_normal", "hint_roughness_gray", "filter_nearest",
            "filter_linear", "filter_nearest_mipmap", "filter_linear_mipmap", "hint_nearest_mipmap_anisotropic",
            "hint_linear_mipmap_anisotropic", "repeat_enabled", "repeat_disabled", "hint_screen_texture",
            "hint_depth_texture", "hint_normal_roughness_texture")
    )
    
    val builtinTypes = setOf(
        "void", "bool", "bvec2", "bvec3", "bvec4", "int", "ivec2", "ivec3", "ivec4", "uint", "uvec2", "uvec3", "uvec4",
        "float", "vec2", "vec3", "vec4", "mat2", "mat3", "mat4", "sampler2D", "isampler2D", "usampler2D",
        "sampler2DArray", "isampler2DArray", "usampler2DArray", "sampler3D", "isampler3D", "usampler3D",
        "samplerCube", "samplerCubeArray", "samplerExternalOES"
    )
    
    fun getAvailableShaderTypes(): Set<String> = shaderTypes
    fun validateShaderName(element: GDShaderShaderTypeDeclaration): Boolean = element.shaderType in shaderTypes
}
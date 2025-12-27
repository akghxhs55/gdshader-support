package kr.jaehoyi.gdshader.model

enum class DataType(
    val text: String,
    val isInstantiable: Boolean = true
) {

    VOID("void"),
    BOOL("bool"),
    BVEC2("bvec2"),
    BVEC3("bvec3"),
    BVEC4("bvec4"),
    INT("int"),
    IVEC2("ivec2"),
    IVEC3("ivec3"),
    IVEC4("ivec4"),
    UINT("uint"),
    UVEC2("uvec2"),
    UVEC3("uvec3"),
    UVEC4("uvec4"),
    FLOAT("float"),
    VEC2("vec2"),
    VEC3("vec3"),
    VEC4("vec4"),
    MAT2("mat2"),
    MAT3("mat3"),
    MAT4("mat4"),
    SAMPLER2D("sampler2D"),
    ISAMPLER2D("isampler2D"),
    USAMPLER2D("usampler2D"),
    SAMPLER2DARRAY("sampler2DArray"),
    ISAMPLER2DARRAY("isampler2DArray"),
    USAMPLER2DARRAY("usampler2DArray"),
    SAMPLER3D("sampler3D"),
    ISAMPLER3D("isampler3D"),
    USAMPLER3D("usampler3D"),
    SAMPLERCUBE("samplerCube"),
    SAMPLERCUBEARRAY("samplerCubeArray"),
    SAMPLEREXTERNALOES("samplerExternalOES"),
    
    VEC_TYPE("vec_type", isInstantiable = false),
    VEC_INT_TYPE("vec_int_type", isInstantiable = false),
    VEC_UINT_TYPE("vec_uint_type", isInstantiable = false),
    VEC_BOOL_TYPE("vec_bool_type", isInstantiable = false),
    MAT_TYPE("mat_type", isInstantiable = false),
    GVEC4_TYPE("gvec4_type", isInstantiable = false),
    GSAMPLER2D("gsampler2D", isInstantiable = false),
    GSAMPLER2DARRAY("gsampler2DArray", isInstantiable = false),
    GSAMPLER3D("gsampler3D", isInstantiable = false);
    
    companion object {
        private val textToDataTypeMap = entries.associateBy { it.text }
        
        fun fromText(text: String): DataType? =
            textToDataTypeMap[text]
    }
    
}
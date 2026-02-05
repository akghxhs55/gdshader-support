import json

TYPE_MAP = {
    "vec_type": "VEC_TYPE",
    "vec_int_type": "VEC_INT_TYPE",
    "vec_uint_type": "VEC_UINT_TYPE",
    "vec_bool_type": "VEC_BOOL_TYPE",
    "mat_type": "MAT_TYPE",
    "gvec4_type": "GVEC4_TYPE",
    "gsampler2D": "GSAMPLER2D",
    "gsampler2DArray": "GSAMPLER2DARRAY",
    "gsampler3D": "GSAMPLER3D",
    "float": "FLOAT",
    "int": "INT",
    "uint": "UINT",
    "bool": "BOOL",
    "void": "VOID",
    "vec2": "VEC2", "vec3": "VEC3", "vec4": "VEC4",
    "bvec2": "BVEC2", "bvec3": "BVEC3", "bvec4": "BVEC4",
    "ivec2": "IVEC2", "ivec3": "IVEC3", "ivec4": "IVEC4",
    "uvec2": "UVEC2", "uvec3": "UVEC3", "uvec4": "UVEC4",
    "mat2": "MAT2", "mat3": "MAT3", "mat4": "MAT4",
    "sampler2D": "SAMPLER2D", "isampler2D": "ISAMPLER2D", "usampler2D": "USAMPLER2D",
    "sampler2DArray": "SAMPLER2DARRAY", "isampler2DArray": "ISAMPLER2DARRAY", "usampler2DArray": "USAMPLER2DARRAY",
    "sampler3D": "SAMPLER3D", "isampler3D": "ISAMPLER3D", "usampler3D": "USAMPLER3D",
    "samplerCube": "SAMPLERCUBE",
    "samplerCubeArray": "SAMPLERCUBEARRAY",
    "samplerExternalOES": "SAMPLEREXTERNALOES"
}

def map_type(t):
    return TYPE_MAP.get(t, f"/* UNKNOWN: {t} */ VOID")

def escape_string(s):
    # Kotlin normal string escaping
    return s.replace('\\', '\\\\').replace('"', '\\"').replace('$', '\\$')

def escape_raw_string(s):
    # Kotlin raw string escaping (only $ needs handling if strictly safe, but mostly fine)
    # We replace $ with ${'$'}
    return s.replace('$', '${"$"}')

def generate_kotlin():
    json_path = "build/generated/shader_functions.json"
    with open(json_path, 'r', encoding='utf-8') as f:
        functions = json.load(f)

    print("    private val globalFunctions = listOf(")
    
    for i, func in enumerate(functions):
        name = func['name']
        ret_type = map_type(func['return_type'])
        
        params_code = []
        param_docs = {}
        for p in func['parameters']:
            p_type = map_type(p['type'])
            p_name = p['name']
            params_code.append(f'ParameterSpec("{p_name}", {p_type})')
            param_docs[p_name] = p['description']
        
        params_str = ",\n                ".join(params_code)
        if params_str:
            params_str = f"""
            parameters = listOf(
                {params_str}
            ),"""
        else:
            params_str = ""

        desc = escape_raw_string(func['description'])
        ret_desc = escape_string(func['return_description'])
        link = escape_string(func['link'])
        
        param_docs_str = ""
        if param_docs:
            entries = []
            for k, v in param_docs.items():
                safe_v = escape_string(v)
                entries.append(f'"{k}" to "{safe_v}"')
            param_docs_str = ",\n                params = mapOf(\n                    " + ",\n                    ".join(entries) + "\n                )"
        
        code = f"""        FunctionSpec(
            name = "{name}",
            returnType = {ret_type},{params_str}
            description = functionDoc(
                description = \"\"\"{desc}\"\""{param_docs_str},
                returns = "{ret_desc}",
                link = "{link}"
            )
        )"""
        
        print(code, end="")
        if i < len(functions) - 1:
            print(",")
        print()

    print("    )")

if __name__ == "__main__":
    generate_kotlin()

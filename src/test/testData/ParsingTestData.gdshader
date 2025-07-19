// Simple GDShader file example

shader_type canvas_item;
render_mode skip_vertex_transform, blend_add;

#define MY_CONST 42
#ifdef DEBUG
    #define DEBUG_MODE
#endif

group_uniforms MyUniformGroup;
uniform vec4 color : source_color = vec4(1.0, 0.5, 0.5, 1.0);
uniform int alpha : hint_range(0.0, 1.0);
uniform int mode : hint_enum("A", "B", "C");
uniform float brightness = 1.5;
uniform sampler2D tex;

const int SIZE = 4;
varying highp float v_value;
const float global_value = 10.0, threshold = 5.0;

struct Data {
    vec3 position;
    vec3 normal;
};

void fragment() {
    vec2 uv = UV;
    vec4 texColor = texture(tex, uv);
    COLOR = texColor * color * brightness;
    
    int arr[SIZE] = { 1, 2, 3, 4 };
    Data d;
    d.position = vec3(uv, 0.0);
    d.normal = vec3(0.0, 1.0, 0.0);
    
    for (int i = 0; i < SIZE; i++) {
        if (arr[i] > 2) {
            COLOR.rgb += vec3(float(i) / 10.0);
            continue;
        } else {
            COLOR.a -= 0.1;
        }
    }
    
    switch (mode) {
        case 0: COLOR *= 0.5; break;
        case 1: COLOR *= 2.0; break;
        default: COLOR = vec4(1.0); break;
    }
    
    while (COLOR.a > 0.0) {
        COLOR.a -= 0.05;
        if (COLOR.a < 0.1) break;
    }
    
    float temp = 0.0;
    do {
        temp += 1.0;
    } while (global_value < 20.0);
    
    float cond = (mode == 1) ? 2.0 : -2.0;
}

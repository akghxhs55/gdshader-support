uniform vec4 albedo;

global uniform float brightness;
instance uniform float roughness;

uniform highp sampler2D albedoMap;

uniform vec3 light_color = vec3(1.0, 1.0, 1.0);

uniform sampler2D screen_texture : hint_screen_texture;
uniform sampler2D depth_texture : filter_nearest, repeat_enable;

uniform float metallic : hint_range(0.0, 1.0) = 0.0;

uniform int blend_mode : hint_enum("Mix", "Add", "Sub");

uniform int instance_id : instance_index(0);

uniform vec4 palette[16];

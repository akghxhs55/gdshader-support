struct Light {
	vec3 position;
	vec4 color;
	float intensity;
};

struct Material {
	lowp vec4 albedo;
	highp float roughness;
	float properties[4];
};

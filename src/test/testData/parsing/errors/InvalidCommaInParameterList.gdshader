float h(,) {
	return 1.0;
}

float k(float a,,) {
	return a;
}

void fragment() {
	float x = k(1.0);
}

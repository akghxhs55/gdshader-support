struct S {
	float a;
};

void fragment() {
	S s;
	S t = S(1.0);
	s.a = t.a;
}

package kr.jaehoyi.gdshader.model

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kr.jaehoyi.gdshader.resolve.GdsOverloadResolver
import org.junit.Test

class BuiltinsTest {
    private fun resolve(
        name: String,
        vararg argumentTypes: DataType,
        shaderType: ShaderType = ShaderType.SPATIAL,
    ): FunctionSpec? {
        val candidates = Builtins.getFunctions(shaderType, FunctionContext.FRAGMENT, name) ?: return null
        return GdsOverloadResolver.resolveFunctionOverload(candidates, argumentTypes.toList())
    }

    @Test
    fun `test reflect and refract accept every float vector size`() {
        for (type in listOf(VectorType.VEC2, VectorType.VEC3, VectorType.VEC4)) {
            assertNotNull(resolve("reflect", type, type))
            assertNotNull(resolve("refract", type, type, FloatType.DEFAULT))
        }
        assertNull(resolve("reflect", FloatType.DEFAULT, FloatType.DEFAULT))
    }

    @Test
    fun `test vector relational functions accept int uint and bool vectors`() {
        assertEquals(VectorType.BVEC3, resolve("equal", VectorType.IVEC3, VectorType.IVEC3)?.returnType)
        assertEquals(VectorType.BVEC2, resolve("lessThan", VectorType.UVEC2, VectorType.UVEC2)?.returnType)
        assertEquals(VectorType.BVEC4, resolve("notEqual", VectorType.BVEC4, VectorType.BVEC4)?.returnType)
        assertNull(resolve("lessThan", VectorType.BVEC2, VectorType.BVEC2))
        assertNull(resolve("equal", FloatType.DEFAULT, FloatType.DEFAULT))
    }

    @Test
    fun `test bitfieldInsert accepts int`() {
        assertEquals(IntType, resolve("bitfieldInsert", IntType, IntType, IntType, IntType)?.returnType)
        assertEquals(VectorType.IVEC2, resolve("bitfieldInsert", VectorType.IVEC2, VectorType.IVEC2, IntType, IntType)?.returnType)
    }

    @Test
    fun `test textureGrad accepts sampler3D with vec3 derivatives`() {
        assertNotNull(resolve("textureGrad", SamplerType.SAMPLER3D, VectorType.VEC3, VectorType.VEC3, VectorType.VEC3))
        assertNull(resolve("textureGrad", SamplerType.SAMPLER3D, VectorType.VEC3, VectorType.VEC2, VectorType.VEC2))
    }

    @Test
    fun `test bitfieldExtract uint keeps its own description`() {
        val spec = requireNotNull(resolve("bitfieldExtract", UIntType, IntType, IntType))
        val description = requireNotNull(spec.description)
        assertTrue(description.contains("Extracts a subset of the bits"))
        assertFalse(description.contains("Inserts"))
    }

    @Test
    fun `test numbered built-ins replace doc placeholders`() {
        for (i in 1..6) {
            assertNotNull(Builtins.getVariable(ShaderType.PARTICLES, FunctionContext.PROCESS, "USERDATA$i"))
        }
        for (i in 0..3) {
            assertNotNull(Builtins.getVariable(ShaderType.SKY, FunctionContext.SKY, "LIGHT${i}_COLOR"))
        }
        assertNull(Builtins.getVariable(ShaderType.PARTICLES, FunctionContext.PROCESS, "USERDATAX"))
        assertNull(Builtins.getVariable(ShaderType.SKY, FunctionContext.SKY, "LIGHTX_COLOR"))
    }

    @Test
    fun `test built-in variable names and types match the engine`() {
        assertNotNull(Builtins.getVariable(ShaderType.SPATIAL, FunctionContext.FRAGMENT, "SSS_TRANSMITTANCE_BOOST"))
        assertNull(Builtins.getVariable(ShaderType.SPATIAL, FunctionContext.FRAGMENT, "SSS_TRANSMISSION_BOOST"))
        assertEquals(FloatType.DEFAULT, Builtins.getVariable(ShaderType.FOG, FunctionContext.FOG, "SDF")?.type)
        assertEquals(FloatType.DEFAULT, Builtins.getVariable(ShaderType.PARTICLES, FunctionContext.PROCESS, "COLLISION_DEPTH")?.type)
        assertEquals(VectorType.VEC3, Builtins.getVariable(ShaderType.PARTICLES, FunctionContext.PROCESS, "COLLISION_NORMAL")?.type)
    }

    @Test
    fun `test engine-only built-ins are present`() {
        assertEquals(FloatType.DEFAULT, Builtins.getVariable(ShaderType.SPATIAL, FunctionContext.VERTEX, "EXPOSURE")?.type)
        assertEquals(BoolType, Builtins.getVariable(ShaderType.SPATIAL, FunctionContext.LIGHT, "LIGHT_IS_AREA")?.type)
        assertEquals(VectorType.VEC3, Builtins.getVariable(ShaderType.SPATIAL, FunctionContext.LIGHT, "LIGHT_AREA_DIFFUSE_MULTIPLIER")?.type)
        assertEquals(VectorType.VEC3, Builtins.getVariable(ShaderType.SPATIAL, FunctionContext.LIGHT, "LIGHT_AREA_SPECULAR_MULTIPLIER")?.type)
        assertNull(Builtins.getVariable(ShaderType.SPATIAL, FunctionContext.FRAGMENT, "LIGHT_IS_AREA"))
    }
}

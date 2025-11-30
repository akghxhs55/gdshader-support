package kr.jaehoyi.gdshader.completion

import kr.jaehoyi.gdshader.GDShaderUtil

class UniformCompletionTest : BaseCompletionTest() {

    override val testPath = "completion/uniform"
    
    fun testUniformKeywords() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "uniform", "global", "instance")
    }
    
    fun testAfterModifier() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "uniform")
        assertDoesntContain(completions, "global", "instance", "shader_type", "void")
    }
    
    fun testAfterUniform() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "bool", "void", "highp")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type")
    }
    
    fun testAfterPrecision() {
        val completions = getCompletionsForTestFile()
        assertContainsElements(completions, "int", "float", "bool", "void")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "highp", "mediump", "lowp")
    }
    
    fun testAfterType() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testAfterVariableName() {
        val completions = getCompletionsForTestFile()
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForVec3() {
        val completions = getCompletionsForTestFile()
        
        val expectedHints = requireNotNull(GDShaderUtil.uniformHintKeywords["vec3"])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForVec4() {
        val completions = getCompletionsForTestFile()
        
        val expectedHints = requireNotNull(GDShaderUtil.uniformHintKeywords["vec4"])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForInt() {
        val completions = getCompletionsForTestFile()
        
        val expectedHints = requireNotNull(GDShaderUtil.uniformHintKeywords["int"])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForFloat() {
        val completions = getCompletionsForTestFile()
        
        val expectedHints = requireNotNull(GDShaderUtil.uniformHintKeywords["float"])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForSampler2D() {
        val completions = getCompletionsForTestFile()
        
        val expectedHints = requireNotNull(GDShaderUtil.uniformHintKeywords["sampler2D"])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testChainedHint() {
        val completions = getCompletionsForTestFile()
        
        assertContainsElements(completions, "instance_index")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
}
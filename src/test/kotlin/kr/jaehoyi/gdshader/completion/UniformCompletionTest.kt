package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.model.Builtins

class UniformCompletionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = false
    }

    override fun tearDown() {
        CodeInsightSettings.getInstance().AUTOCOMPLETE_ON_CODE_COMPLETION = true
        super.tearDown()
    }
    
    fun testUniformKeywords() {
        myFixture.configureByText("test.gdshader", """
            <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "uniform", "global", "instance")
    }
    
    fun testAfterModifier() {
        myFixture.configureByText("test.gdshader", """
            global <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "uniform")
        assertDoesntContain(completions, "global", "instance", "shader_type", "void")
    }
    
    fun testAfterUniform() {
        myFixture.configureByText("test.gdshader", """
            uniform <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "bool", "highp")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "void")
    }
    
    fun testAfterPrecision() {
        myFixture.configureByText("test.gdshader", """
            uniform highp <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "int", "float", "bool")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "highp", "mediump", "lowp", "void")
    }
    
    fun testAfterType() {
        myFixture.configureByText("test.gdshader", """
            uniform vec4 <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testAfterVariableName() {
        myFixture.configureByText("test.gdshader", """
            uniform vec4 myVar <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForVec3() {
        myFixture.configureByText("test.gdshader", """
            uniform vec3 myVar : <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.VEC3])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForVec4() {
        myFixture.configureByText("test.gdshader", """
            uniform vec4 myVar : <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.VEC4])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range", "hint_normal", "hint_default_white")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForInt() {
        myFixture.configureByText("test.gdshader", """
            uniform int myVar : <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.INT])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForFloat() {
        myFixture.configureByText("test.gdshader", """
            uniform float myVar : <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.FLOAT])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_normal", "hint_default_white", "filter_nearest", "filter_linear")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testHintForSampler2D() {
        myFixture.configureByText("test.gdshader", """
            uniform sampler2D myVar : <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        val expectedHints = requireNotNull(GdsKeywords.UNIFORM_HINTS[Builtins.SAMPLER2D])
        assertContainsElements(completions, expectedHints.toList())
        assertDoesntContain(completions, "hint_enum", "hint_range")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testChainedHint() {
        myFixture.configureByText("test.gdshader", """
            uniform sampler2D myVar : source_color, <caret>
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertContainsElements(completions, "instance_index")
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp")
    }
    
    fun testInsideRangeHintParameter() {
        myFixture.configureByText("test.gdshader", """
            uniform float u_roundness : hint_range(0.0, <caret>);
        """.trimIndent())
        myFixture.completeBasic()

        val completions = requireNotNull(myFixture.lookupElementStrings)
        
        assertDoesntContain(completions, "uniform", "global", "instance", "shader_type", "int", "float", "bool", "void", "highp", "mediump", "lowp", "hint_range", "hint_enum", "hint_normal", "hint_default_white", "filter_nearest", "filter_linear", "instance_index")
    }
    
}
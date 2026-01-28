package kr.jaehoyi.gdshader.completion

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsPostfixCompletionTest : BasePlatformTestCase() {

    fun testStructMemberCompletion() {
        val code = """
            shader_type spatial;
            
            struct MyStruct {
                float my_field_a;
                vec3 my_field_b;
            };
            
            void fragment() {
                MyStruct s;
                s.<caret>
            }
        """
        myFixture.configureByText("test.gdshader", code)
        myFixture.completeBasic()
        
        val strings = myFixture.lookupElementStrings
        val nonNullStrings = requireNotNull(strings) { "Completion list should not be null" }
        assertTrue("Should contain 'my_field_a'", nonNullStrings.contains("my_field_a"))
        assertTrue("Should contain 'my_field_b'", nonNullStrings.contains("my_field_b"))
    }

    fun testVectorSwizzleCompletion() {
        val code = """
            shader_type spatial;
            
            void fragment() {
                vec3 v = vec3(1.0);
                v.<caret>
            }
        """
        myFixture.configureByText("test.gdshader", code)
        myFixture.completeBasic()
        
        val strings = myFixture.lookupElementStrings
        val nonNullStrings = requireNotNull(strings) { "Completion list should not be null" }
        
        assertTrue("Should contain 'x'", nonNullStrings.contains("x"))
        assertTrue("Should contain 'y'", nonNullStrings.contains("y"))
        assertTrue("Should contain 'z'", nonNullStrings.contains("z"))
        assertFalse("Should NOT contain 'w' (vec3)", nonNullStrings.contains("w"))
        
        assertTrue("Should contain 'r'", nonNullStrings.contains("r"))
        assertFalse("Should NOT contain 'a'", nonNullStrings.contains("a"))
        
        assertTrue("Should contain 'xy'", nonNullStrings.contains("xy"))
        assertTrue("Should contain 'rgb'", nonNullStrings.contains("rgb"))
        assertTrue("Should contain 'xyz'", nonNullStrings.contains("xyz"))
    }
    
    fun testIncompleteStructMemberCompletion() {
        val code = """
            shader_type spatial;
            struct S { float foo; float bar; };
            void main() {
                S s;
                s.<caret>
            }
        """
        myFixture.configureByText("test.gdshader", code)
        myFixture.completeBasic()
        
        val strings = myFixture.lookupElementStrings
        val nonNullStrings = requireNotNull(strings) { "Completion list should not be null" }
        assertTrue("Should contain 'foo'", nonNullStrings.contains("foo"))
        assertTrue("Should contain 'bar'", nonNullStrings.contains("bar"))
    }
}

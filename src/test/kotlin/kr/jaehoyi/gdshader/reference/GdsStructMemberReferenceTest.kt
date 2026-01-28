package kr.jaehoyi.gdshader.reference

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameDecl
import kr.jaehoyi.gdshader.psi.GdsStructMemberNameRef

class GdsStructMemberReferenceTest : BasePlatformTestCase() {

    fun testStructMemberReference() {
        val code = """
            shader_type spatial;
            
            struct MyStruct {
                float my_field;
            };
            
            void fragment() {
                MyStruct s;
                s.my_field = 1.0;
            }
        """
        myFixture.configureByText("test.gdshader", code)
        
        val file = myFixture.file
        val refs = PsiTreeUtil.findChildrenOfType(file, GdsStructMemberNameRef::class.java)
        val referenceElement = refs.find { it.text == "my_field" }
        val nonNullReferenceElement = requireNotNull(referenceElement) { "Reference element not found" }
        
        val resolved = nonNullReferenceElement.reference.resolve()
        assertNotNull("Reference not resolved", resolved)
        assertTrue("Resolved element should be GdsStructMemberNameDecl", resolved is GdsStructMemberNameDecl)
        assertEquals("my_field", (resolved as GdsStructMemberNameDecl).name)
    }

    fun testNestedStructMemberReference() {
        val code = """
            shader_type spatial;
            
            struct Inner {
                float val;
            };
            
            struct Outer {
                Inner inner;
            };
            
            void fragment() {
                Outer o;
                float v = o.inner.val;
            }
        """
        myFixture.configureByText("test.gdshader", code)
        
        val file = myFixture.file
        val refs = PsiTreeUtil.findChildrenOfType(file, GdsStructMemberNameRef::class.java)
        
        val referenceElement = refs.find { it.text == "val" }
        val nonNullReferenceElement = requireNotNull(referenceElement) { "Reference element 'val' not found" }
        
        val resolved = nonNullReferenceElement.reference.resolve()
        assertNotNull("Reference 'val' not resolved", resolved)
        assertTrue("Resolved element should be GdsStructMemberNameDecl", resolved is GdsStructMemberNameDecl)
        assertEquals("val", (resolved as GdsStructMemberNameDecl).name)
    }
}

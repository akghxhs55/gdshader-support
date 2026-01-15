package kr.jaehoyi.gdshader.reference

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kr.jaehoyi.gdshader.psi.GdsFile

class GdsIncludeReferenceTest : BasePlatformTestCase() {
    
    fun `test absolute path reference`() {
        val targetFile = myFixture.configureByText("utils.gdshaderinc", "// utility functions")
        
        myFixture.configureByText("main.gdshader", """)
            #include "res://<caret>utils.gdshaderinc"
        """.trimIndent())
        
        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())
        
        val resolvedElement = reference.resolve()
        
        assertInstanceOf(resolvedElement, GdsFile::class.java)
        assertEquals(targetFile, resolvedElement)
        assertEquals("utils.gdshaderinc", (resolvedElement as PsiFile).name)
    }
    
    fun `test relative path reference`() {
        val targetFile = myFixture.addFileToProject("shaders/common.gdshaderinc", "// common shader functions")
        
        val mainFile = myFixture.addFileToProject("shaders/main.gdshader", """)
            #include "common.gdshaderinc"
        """.trimIndent())
        
        myFixture.configureFromExistingVirtualFile(mainFile.virtualFile)
        
        myFixture.editor.caretModel.moveToOffset(mainFile.text.indexOf("common.gdshaderinc") + 1)
        
        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())
        
        val resolvedElement = reference.resolve()
        
        assertInstanceOf(resolvedElement, GdsFile::class.java)
        assertEquals(targetFile, resolvedElement)
        assertEquals("common.gdshaderinc", (resolvedElement as PsiFile).name)
    }
    
    fun `test parent directory relative path reference`() {
        val targetFile = myFixture.addFileToProject("shaders/includes/math.gdshaderinc", "// math functions")
        
        val mainFile = myFixture.addFileToProject("shaders/main.gdshader", """)
            #include "includes/math.gdshaderinc"
        """.trimIndent())
        
        myFixture.configureFromExistingVirtualFile(mainFile.virtualFile)
        
        myFixture.editor.caretModel.moveToOffset(mainFile.text.indexOf("includes/math.gdshaderinc") + 1)
        
        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())
        
        val resolvedElement = reference.resolve()
        
        assertInstanceOf(resolvedElement, GdsFile::class.java)
        assertEquals(targetFile, resolvedElement)
        assertEquals("math.gdshaderinc", (resolvedElement as PsiFile).name)
    }
    
    fun `test non-existing file reference`() {
        myFixture.configureByText("main.gdshader", """
            #include "non<caret>_existing_file.gdshaderinc"
        """.trimIndent())
        
        val reference = requireNotNull(myFixture.getReferenceAtCaretPosition())
        
        val resolvedElement = reference.resolve()
        
        assertNull(resolvedElement)
    }
    
}
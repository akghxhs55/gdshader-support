package kr.jaehoyi.gdshader.reference

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsIncludeRefactoringTest : BasePlatformTestCase() {
    // === Rename ===

    fun `test renaming included file keeps res path directories`() {
        val target = myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("main.gdshader", "res://shaders/common.gdshaderinc")

        myFixture.renameElement(target, "util.gdshaderinc")

        assertIncludes(main, "res://shaders/util.gdshaderinc")
    }

    fun `test renaming included file keeps relative path directories`() {
        val target = myFixture.addFileToProject("shaders/lib/common.gdshaderinc", "")
        val main = addIncluder("shaders/main.gdshader", "lib/common.gdshaderinc")

        myFixture.renameElement(target, "util.gdshaderinc")

        assertIncludes(main, "lib/util.gdshaderinc")
    }

    fun `test renaming directory updates res path`() {
        val target = myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("main.gdshader", "res://shaders/common.gdshaderinc")

        myFixture.renameElement(target.containingDirectory, "fx")

        assertIncludes(main, "res://fx/common.gdshaderinc")
    }

    fun `test renaming directory updates relative path`() {
        val target = myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("materials/main.gdshader", "../shaders/common.gdshaderinc")

        myFixture.renameElement(target.containingDirectory, "fx")

        assertIncludes(main, "../fx/common.gdshaderinc")
    }

    // === Move included file ===

    fun `test moving included file updates res path`() {
        val target = myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("main.gdshader", "res://shaders/common.gdshaderinc")

        move(target, directory("lib/math"))

        assertIncludes(main, "res://lib/math/common.gdshaderinc")
    }

    fun `test moving included file updates relative path`() {
        val target = myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("shaders/main.gdshader", "./common.gdshaderinc")

        move(target, directory("lib"))

        assertIncludes(main, "../lib/common.gdshaderinc")
    }

    fun `test moving directory of included file updates res path`() {
        val target = myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("main.gdshader", "res://shaders/common.gdshaderinc")

        move(target.containingDirectory, directory("lib"))

        assertIncludes(main, "res://lib/shaders/common.gdshaderinc")
    }

    // === Move including file ===

    fun `test moving including file updates relative path`() {
        myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("shaders/main.gdshader", "common.gdshaderinc")

        move(main, directory("materials"))

        assertIncludes(main, "../shaders/common.gdshaderinc")
    }

    fun `test moving including file keeps res path`() {
        myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("shaders/main.gdshader", "res://shaders/common.gdshaderinc")

        move(main, directory("materials"))

        assertIncludes(main, "res://shaders/common.gdshaderinc")
    }

    fun `test moving including and included files together keeps relative path`() {
        val target = myFixture.addFileToProject("shaders/common.gdshaderinc", "")
        val main = addIncluder("shaders/main.gdshader", "common.gdshaderinc")

        move(arrayOf(main, target), directory("lib"))

        assertIncludes(main, "common.gdshaderinc")
    }

    fun `test moving included file outside godot project leaves path unchanged`() {
        myFixture.addFileToProject("game/project.godot", "")
        val target = myFixture.addFileToProject("game/shaders/common.gdshaderinc", "")
        val main = addIncluder("game/main.gdshader", "res://shaders/common.gdshaderinc")

        move(target, directory("outside"))

        assertIncludes(main, "res://shaders/common.gdshaderinc")
    }

    private fun addIncluder(
        path: String,
        includePath: String,
    ): PsiFile = myFixture.addFileToProject(path, includeLine(includePath))

    private fun includeLine(includePath: String): String = "#include \"$includePath\""

    private fun assertIncludes(
        file: PsiFile,
        includePath: String,
    ) {
        assertEquals(includeLine(includePath), file.text)
    }

    private fun directory(path: String): PsiDirectory = myFixture.addFileToProject("$path/.keep", "").containingDirectory

    private fun move(
        element: PsiElement,
        targetDirectory: PsiDirectory,
    ) = move(arrayOf(element), targetDirectory)

    private fun move(
        elements: Array<PsiElement>,
        targetDirectory: PsiDirectory,
    ) {
        MoveFilesOrDirectoriesProcessor(project, elements, targetDirectory, true, false, false, null, null).run()
    }
}

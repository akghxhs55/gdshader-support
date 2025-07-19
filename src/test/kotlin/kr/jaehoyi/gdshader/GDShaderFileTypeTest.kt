package kr.jaehoyi.gdshader

import com.intellij.openapi.fileTypes.FileTypeManager
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class GDShaderFileTypeTest : StringSpec({
    "GDShaderFileTypes should be registered" {
        val fileType = FileTypeManager.getInstance().getFileTypeByExtension("gdshader")
        fileType.name shouldBe "GDShader"
    }
})
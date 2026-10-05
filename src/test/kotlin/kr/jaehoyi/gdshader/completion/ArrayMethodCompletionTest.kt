package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.lookup.LookupElementPresentation

class ArrayMethodCompletionTest : GdsCompletionTestBase() {
    fun `test known and unknown arrays offer length`() {
        for (size in listOf("3", "", "MISSING")) {
            configure("float values[$size]; int count = values.<caret>;")
            val items = myFixture.completeBasic()
            val length = requireNotNull(items).single { it.lookupString == "length" }
            val presentation = LookupElementPresentation()
            length.renderElement(presentation)
            assertEquals("()", presentation.tailText)
            assertEquals("int", presentation.typeText)
        }
    }

    fun `test nested array member offers length`() {
        myFixture.configureByText(
            "test.gdshader",
            """
            shader_type spatial;
            struct Data { float values[3]; };
            void fragment() {
                Data items[2];
                int count = items[0].values.<caret>;
            }
            """.trimIndent(),
        )
        assertContainsElements(completeAndGetStrings(), "length")
    }

    fun `test length completion inserts parentheses and places caret after call`() {
        configure("float values[3]; int count = values.len<caret>;")
        assertContainsElements(completeAndGetStrings(), "length")
        myFixture.finishLookup('\n')
        myFixture.checkResult("shader_type spatial;\nvoid fragment() { float values[3]; int count = values.length()<caret>; }")
    }

    fun `test length completion reuses existing parentheses`() {
        configure("float values[3]; int count = values.len<caret>();")
        assertContainsElements(completeAndGetStrings(), "length")
        myFixture.finishLookup('\n')
        myFixture.checkResult("shader_type spatial;\nvoid fragment() { float values[3]; int count = values.length()<caret>; }")
    }

    fun `test non arrays do not offer array method`() {
        for (declaration in listOf("float values;", "vec3 values;", "float values[3];")) {
            val receiver = if (declaration.contains("[")) "values[0]" else "values"
            configure("$declaration $receiver.<caret>;")
            val items = myFixture.completeBasic().orEmpty()
            assertFalse(items.any { it.lookupString == "length" })
        }
    }

    private fun configure(body: String) {
        myFixture.configureByText("test.gdshader", "shader_type spatial;\nvoid fragment() { $body }")
    }
}

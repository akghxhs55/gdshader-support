package kr.jaehoyi.gdshader.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsParserUtil : GeneratedParserUtilBase() {
    companion object {
        /**
         * Guards `local_variable_declaration` against reading two unrelated identifiers on
         * consecutive lines as "struct type + variable name".
         *
         * While a statement is being typed, the file often looks like this:
         *
         *     foo
         *     COLOR = vec4(1.0);
         *
         * Without this check it parses as a declaration of a local variable `COLOR` of struct type `foo`,
         * which re-colours `foo` as a struct and every later `COLOR` as a local variable.
         *
         * Returns false only for `IDENTIFIER <line break> IDENTIFIER`; built-in types are unambiguous
         * and are never rejected.
         */
        @JvmStatic
        @Suppress("UNUSED_PARAMETER")
        fun structTypeNameOnSameLine(
            builder: PsiBuilder,
            level: Int,
        ): Boolean {
            if (builder.tokenType != GdsTypes.IDENTIFIER || builder.lookAhead(1) != GdsTypes.IDENTIFIER) return true

            // Only whitespace and comments can sit between the two identifiers.
            var steps = 1
            while (true) {
                val tokenType = builder.rawLookup(steps) ?: return true
                if (tokenType == GdsTypes.IDENTIFIER) break
                steps++
            }

            val gapStart = builder.rawTokenTypeStart(1)
            val gapEnd = builder.rawTokenTypeStart(steps)
            return !builder.originalText.subSequence(gapStart, gapEnd).contains('\n')
        }
    }
}

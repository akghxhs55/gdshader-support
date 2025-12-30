package kr.jaehoyi.gdshader.codeinsight

import java.awt.Color
import java.util.Locale

object GDShaderColorUtil {
    
    fun extractColorFromText(text: String): Color? {
        val regex = Regex("""vec([34])\s*\(\s*([-0-9.,\s]+)\s*\)""")
        val match = regex.find(text) ?: return null

        val type = match.groupValues[1]
        val content = match.groupValues[2]

        val args = content.split(",").mapNotNull { it.trim().toFloatOrNull() }

        when (args.size) {
            1 -> {
                val s = (args[0] * 255).toInt().coerceIn(0, 255)
                return Color(s, s, s, 255)
            }
            3 -> {
                val r = (args[0] * 255).toInt().coerceIn(0, 255)
                val g = (args[1] * 255).toInt().coerceIn(0, 255)
                val b = (args[2] * 255).toInt().coerceIn(0, 255)
                return Color(r, g, b, 255)
            }
            4 -> if (type == "4") {
                val r = (args[0] * 255).toInt().coerceIn(0, 255)
                val g = (args[1] * 255).toInt().coerceIn(0, 255)
                val b = (args[2] * 255).toInt().coerceIn(0, 255)
                val a = (args[3] * 255).toInt().coerceIn(0, 255)
                return Color(r, g, b, a)
            }
        }

        return null
    }

    fun convertColorToVecString(color: Color, isVec4: Boolean): String {
        val r = formatFloat(color.red / 255.0f)
        val g = formatFloat(color.green / 255.0f)
        val b = formatFloat(color.blue / 255.0f)

        return if (isVec4) {
            val a = formatFloat(color.alpha / 255.0f)
            "vec4($r, $g, $b, $a)"
        } else {
            "vec3($r, $g, $b)"
        }
    }
    
    fun isColorVariableName(name: String): Boolean {
        val lowerName = name.lowercase(Locale.getDefault())
        if (colorExcludeKeywords.any { it in lowerName }) {
            return false
        }
        if (colorIncludeKeywords.any { it in lowerName }) {
            return true
        }
        val words = lowerName.split(Regex("[_\\d]+"))
        if (words.any { it in colorNames }) {
            return true
        }
        return false
    }

    private fun formatFloat(value: Float): String {
        val formatted = String.format(Locale.US, "%.3f", value)
        val trimmed = formatted.trimEnd('0')

        return if (trimmed.endsWith(".")) "${trimmed}0" else trimmed
    }
    
}

private val colorExcludeKeywords = setOf(
    "pos", "position", "dir", "direction", "uv", "size", "scale", "index", "id", "count", "time", "depth", "normal"
)

private val colorIncludeKeywords = setOf(
    "color", "colour", "col", "albedo", "tint", "rgb", "rgba", "emission", "light", "shadow", "bg", "fg", "background", "foreground", "diffuse", "ambient", "specular", "emissive", "tint", "hue", "saturation", "brightness"
)

private val colorNames = setOf(
    "red", "green", "blue", "yellow", "cyan", "magenta", "black", "white", "gray", "grey", "orange", "purple", "pink", "brown", "lime", "teal", "navy", "maroon", "olive", "silver", "gold"
)

package kr.jaehoyi.gdshader.documentation

import com.intellij.psi.tree.IElementType
import kr.jaehoyi.gdshader.psi.GdsTypes

object GdsHintDocumentation {
    val HINT_DOCS: Map<IElementType, String> = mapOf(
        GdsTypes.HINT_RANGE to "Restricted to values in a range (with min/max/step).",
        GdsTypes.HINT_ENUM to "Displays int input as a dropdown widget in the editor.",
        GdsTypes.HINT_DEFAULT_WHITE_TEXTURE to "As value or albedo color, default to opaque white.",
        GdsTypes.HINT_DEFAULT_BLACK_TEXTURE to "As value or albedo color, default to opaque black.",
        GdsTypes.HINT_DEFAULT_TRANSPARENT_TEXTURE to "As value or albedo color, default to transparent black.",
        GdsTypes.HINT_NORMAL_TEXTURE to "Used as normalmap.",
        GdsTypes.HINT_ROUGHNESS_NORMAL_TEXTURE to  "Used for roughness limiter on import (attempts reducing specular aliasing). <code>_normal</code> is a normal map that guides the roughness limiter, with roughness increasing in areas that have high-frequency detail.",
        GdsTypes.HINT_ROUGHNESS_R to "Used for roughness limiter on import (attempts reducing specular aliasing). <code>_normal</code> is a normal map that guides the roughness limiter, with roughness increasing in areas that have high-frequency detail.",
        GdsTypes.HINT_ROUGHNESS_G to "Used for roughness limiter on import (attempts reducing specular aliasing). <code>_normal</code> is a normal map that guides the roughness limiter, with roughness increasing in areas that have high-frequency detail.",
        GdsTypes.HINT_ROUGHNESS_B to "Used for roughness limiter on import (attempts reducing specular aliasing). <code>_normal</code> is a normal map that guides the roughness limiter, with roughness increasing in areas that have high-frequency detail.",
        GdsTypes.HINT_ROUGHNESS_A to "Used for roughness limiter on import (attempts reducing specular aliasing). <code>_normal</code> is a normal map that guides the roughness limiter, with roughness increasing in areas that have high-frequency detail.",
        GdsTypes.HINT_ROUGHNESS_GRAY to  "Used for roughness limiter on import (attempts reducing specular aliasing). <code>_normal</code> is a normal map that guides the roughness limiter, with roughness increasing in areas that have high-frequency detail.",
        GdsTypes.HINT_ANISOTROPY_TEXTURE to "As flowmap, default to right.",
        GdsTypes.HINT_SOURCE_COLOR to "Used as color.",
        GdsTypes.HINT_COLOR_CONVERSION_DISABLED to "Disables automatic color conversion.",
        GdsTypes.HINT_SCREEN_TEXTURE to "Texture is the screen texture.",
        GdsTypes.HINT_NORMAL_ROUGHNESS_TEXTURE to "Texture is the normal roughness texture (only supported in Forward+).",
        GdsTypes.HINT_DEPTH_TEXTURE to "Texture is the depth texture.",
        GdsTypes.FILTER_NEAREST to "Enabled specified texture filtering.",
        GdsTypes.FILTER_LINEAR to "Enabled specified texture filtering.",
        GdsTypes.FILTER_NEAREST_MIPMAP to "Enabled specified texture filtering.",
        GdsTypes.FILTER_LINEAR_MIPMAP to "Enabled specified texture filtering.",
        GdsTypes.FILTER_NEAREST_MIPMAP_ANISOTROPIC to "Enabled specified texture filtering.",
        GdsTypes.FILTER_LINEAR_MIPMAP_ANISOTROPIC to "Enabled specified texture filtering.",
        GdsTypes.REPEAT_ENABLE to "Enabled texture repeating.",
        GdsTypes.REPEAT_DISABLE to "Enabled texture repeating."
    )
}

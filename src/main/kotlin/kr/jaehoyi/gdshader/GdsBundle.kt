package kr.jaehoyi.gdshader

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.GDShaderBundle"

object GdsBundle {
    private val bundle = DynamicBundle(GdsBundle::class.java, BUNDLE)

    @JvmStatic
    fun message(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any,
    ): String = bundle.getMessage(key, *params)

    @JvmStatic
    fun messagePointer(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any,
    ) = bundle.getLazyMessage(key, *params)
}

package kr.jaehoyi.gdshader

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class GdsIcons {

    companion object {
        @JvmField
        val GDSHADER: Icon = IconLoader.getIcon("/icons/gdshader.svg", GdsIcons::class.java)

        @JvmField
        val GDSHADERINC: Icon = IconLoader.getIcon("/icons/gdshaderinc.svg", GdsIcons::class.java)

        @JvmField val SHADER_TYPE: Icon = AllIcons.Nodes.Tag
        @JvmField val RENDER_MODE: Icon = AllIcons.Nodes.Plugin
        @JvmField val STENCIL_MODE: Icon = AllIcons.Nodes.Plugin
        @JvmField val UNIFORM: Icon = AllIcons.Nodes.Variable
        @JvmField val VARYING: Icon = AllIcons.Nodes.Variable
        @JvmField val UNIFORM_GROUP: Icon = AllIcons.Nodes.Folder
        @JvmField val CONSTANT: Icon = AllIcons.Nodes.Constant
        @JvmField val FUNCTION: Icon = AllIcons.Nodes.Function
        @JvmField val STRUCT: Icon = AllIcons.Nodes.Class
        @JvmField val STRUCT_MEMBER: Icon = AllIcons.Nodes.Field
        @JvmField val PARAMETER: Icon = AllIcons.Nodes.Parameter
    }
}

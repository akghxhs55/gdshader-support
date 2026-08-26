package kr.jaehoyi.gdshader

import com.intellij.ide.AppLifecycleListener

class GdsAppLifecycleListener : AppLifecycleListener {
    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        GdsNestingRulesSync.syncRules()
    }
}

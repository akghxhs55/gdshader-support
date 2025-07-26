package kr.jaehoyi.gdshader

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider

class GDShaderNestingRulesProvider : ProjectViewNestingRulesProvider {
    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        consumer.addNestingRule(".gdshader", "gdshader.uid")
    }
}
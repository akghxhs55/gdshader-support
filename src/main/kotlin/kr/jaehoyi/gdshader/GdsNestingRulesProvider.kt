package kr.jaehoyi.gdshader

import com.intellij.ide.projectView.ProjectViewNestingRulesProvider

class GdsNestingRulesProvider : ProjectViewNestingRulesProvider {
    
    override fun addFileNestingRules(consumer: ProjectViewNestingRulesProvider.Consumer) {
        consumer.addNestingRule(".gdshader", ".gdshader.uid")
        consumer.addNestingRule(".gdshaderinc", ".gdshaderinc.uid")
    }
    
}
package kr.jaehoyi.gdshader

import com.intellij.ide.projectView.impl.ProjectViewFileNestingService
import com.intellij.ide.projectView.impl.ProjectViewFileNestingService.NestingRule

object GdsNestingRulesSync {
    fun ownRules(): List<NestingRule> {
        val collected = mutableListOf<NestingRule>()
        GdsNestingRulesProvider().addFileNestingRules { parent, child ->
            collected.add(NestingRule(parent, child))
        }
        return collected
    }

    fun mergeMissingRules(
        existing: List<NestingRule>,
        own: List<NestingRule>,
    ): List<NestingRule> {
        val merged = LinkedHashSet<NestingRule>(existing)
        merged.addAll(own)
        return merged.toList()
    }

    fun syncRules() {
        val service = ProjectViewFileNestingService.getInstance()
        val existing = service.rules
        val merged = mergeMissingRules(existing, ownRules())
        if (merged.size != existing.size) {
            service.setRules(merged)
        }
    }
}

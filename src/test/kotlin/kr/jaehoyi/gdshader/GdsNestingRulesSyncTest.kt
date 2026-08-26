package kr.jaehoyi.gdshader

import com.intellij.ide.projectView.impl.ProjectViewFileNestingService
import com.intellij.ide.projectView.impl.ProjectViewFileNestingService.NestingRule
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsNestingRulesSyncTest : BasePlatformTestCase() {
    private var originalRules: List<NestingRule>? = null

    private fun service(): ProjectViewFileNestingService = ProjectViewFileNestingService.getInstance()

    override fun setUp() {
        super.setUp()
        originalRules = service().rules.toList()
    }

    override fun tearDown() {
        try {
            originalRules?.let { service().setRules(it) }
        } catch (e: Throwable) {
            addSuppressedException(e)
        } finally {
            super.tearDown()
        }
    }

    private fun hasRule(
        parent: String,
        child: String,
    ): Boolean = service().rules.any { it.parentFileSuffix == parent && it.childFileSuffix == child }

    fun `test sync adds missing include rule`() {
        service().setRules(listOf(NestingRule(".gdshader", ".gdshader.uid")))

        GdsNestingRulesSync.syncRules()

        assertTrue("Expected .gdshaderinc nesting rule after sync", hasRule(".gdshaderinc", ".gdshaderinc.uid"))
        assertTrue("Existing rule must survive sync", hasRule(".gdshader", ".gdshader.uid"))
    }

    fun `test sync is idempotent`() {
        service().setRules(listOf(NestingRule(".gdshader", ".gdshader.uid")))
        GdsNestingRulesSync.syncRules()
        val countAfterFirstRun = service().rules.size

        GdsNestingRulesSync.syncRules()

        assertEquals("Second sync must not duplicate rules", countAfterFirstRun, service().rules.size)
    }

    fun `test sync keeps user defined rules`() {
        val userRule = NestingRule(".custom", ".custom.generated")
        service().setRules(
            listOf(
                NestingRule(".gdshader", ".gdshader.uid"),
                userRule,
            ),
        )

        GdsNestingRulesSync.syncRules()

        assertTrue("User defined rule must be preserved", service().rules.contains(userRule))
    }

    fun `test synced rules come from the plugin provider`() {
        val collected = mutableListOf<NestingRule>()
        GdsNestingRulesProvider().addFileNestingRules { parent, child ->
            collected.add(NestingRule(parent, child))
        }

        val ownRules = GdsNestingRulesSync.ownRules()

        assertEquals("Own rules must mirror the registered provider", collected, ownRules)
    }
}

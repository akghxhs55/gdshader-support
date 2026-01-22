package kr.jaehoyi.gdshader.editor

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import kr.jaehoyi.gdshader.GdsFileType
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsItem

class GdsChooseByNameContributor : ChooseByNameContributor {

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<out String> {
        val names = mutableListOf<String>()
        
        val virtualFiles = FileTypeIndex.getFiles(GdsFileType, GlobalSearchScope.projectScope(project))
        
        for (virtualFile in virtualFiles) {
            val file = PsiManager.getInstance(project).findFile(virtualFile) as? GdsFile ?: continue
            
            val items = PsiTreeUtil.getChildrenOfType(file, GdsItem::class.java) ?: continue
            
            for (item in items) {
                val topLevel = item.topLevelDeclaration
                
                topLevel.uniformDeclaration?.variableNameDecl?.name?.let { names.add(it) }
                topLevel.constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.forEach { it.variableNameDecl.name.let { name -> names.add(name) } }
                topLevel.varyingDeclaration?.variableNameDecl?.name?.let { names.add(it) }
                topLevel.functionDeclaration?.functionNameDecl?.name?.let { names.add(it) }
                topLevel.structDeclaration?.structNameDecl?.name?.let { names.add(it) }
            }
        }
        
        return names.toTypedArray()
    }

    override fun getItemsByName(name: String, pattern: String, project: Project, includeNonProjecItems: Boolean): Array<out NavigationItem> {
        val result = mutableListOf<NavigationItem>()
        
        val virtualFiles = FileTypeIndex.getFiles(GdsFileType, GlobalSearchScope.projectScope(project))
        
        for (virtualFile in virtualFiles) {
            val file = PsiManager.getInstance(project).findFile(virtualFile) as? GdsFile ?: continue
            
            val items = PsiTreeUtil.getChildrenOfType(file, GdsItem::class.java) ?: continue
            
            for (item in items) {
                val topLevel = item.topLevelDeclaration
                
                topLevel.uniformDeclaration?.variableNameDecl?.let { if (it.name == name) result.add(it as NavigationItem) }
                topLevel.constantDeclaration?.constantDeclaratorList?.constantDeclaratorList?.forEach { if (it.variableNameDecl.name == name) result.add(it.variableNameDecl as NavigationItem) }
                topLevel.varyingDeclaration?.variableNameDecl?.let { if (it.name == name) result.add(it as NavigationItem) }
                topLevel.functionDeclaration?.functionNameDecl?.let { if (it.name == name) result.add(it as NavigationItem) }
                topLevel.structDeclaration?.structNameDecl?.let { if (it.name == name) result.add(it as NavigationItem) }
            }
        }
        
        return result.toTypedArray()
    }
    
}
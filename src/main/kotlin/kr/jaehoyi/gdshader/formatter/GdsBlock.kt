package kr.jaehoyi.gdshader.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder,
    private val myIndent: Indent
) : AbstractBlock(node, wrap, alignment) {
    
    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode
        var isFirstChild = true
        
        while (child != null) {
            if (child.text.isNotBlank()) {
                val block = GdsBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    null,
                    spacingBuilder,
                    calculateIndentFor(child, isFirstChild)
                )
                blocks.add(block)
                
                isFirstChild = false
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent 
        = myIndent

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val nodeType = myNode.elementType
        
        if (nodeType in INDENT_TRIGGER_BLOCKS ||
            nodeType in INDENT_TRIGGER_STATEMENTS) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        }
        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? 
        = spacingBuilder.getSpacing(this, child1, child2)
    
    override fun isLeaf(): Boolean 
        = myNode.firstChildNode == null

    private fun calculateIndentFor(childNode: ASTNode, isFirstChild: Boolean) : Indent {
        val parentType = myNode.elementType
        val childType = childNode.elementType
        
        if (childType == GdsTypes.CURLY_BRACKET_OPEN ||
            childType == GdsTypes.CURLY_BRACKET_CLOSE ||
            (parentType == GdsTypes.DO_WHILE_STATEMENT && childType == GdsTypes.CF_WHILE)) {
            return Indent.getNoneIndent()
        }
        
        if (parentType in INDENT_TRIGGER_BLOCKS) {
            return Indent.getNormalIndent()
        }
        
        val containsBlock = childNode.findChildByType(GdsTypes.BLOCK) != null
        
        if (parentType in INDENT_TRIGGER_STATEMENTS &&
            !isFirstChild &&
            !containsBlock) {
            return Indent.getNormalIndent()
        }
        
        return Indent.getNoneIndent()
    }
    
}

private val INDENT_TRIGGER_BLOCKS = TokenSet.create(
    GdsTypes.BLOCK,
    GdsTypes.STRUCT_BLOCK,
    GdsTypes.SWITCH_BLOCK
)

private val INDENT_TRIGGER_STATEMENTS = TokenSet.create(
    GdsTypes.IF_STATEMENT,
    GdsTypes.ELSE_CLAUSE,
    GdsTypes.FOR_STATEMENT,
    GdsTypes.WHILE_STATEMENT,
    GdsTypes.DO_WHILE_STATEMENT,
    GdsTypes.RETURN_STATEMENT,
    GdsTypes.SHADER_TYPE_DECLARATION,
    GdsTypes.RENDER_MODE_DECLARATION,
    GdsTypes.STENCIL_MODE_DECLARATION,
    GdsTypes.UNIFORM_GROUP_DECLARATION,
    GdsTypes.UNIFORM_DECLARATION,
    GdsTypes.CONSTANT_DECLARATION,
    GdsTypes.VARYING_DECLARATION,
    GdsTypes.LOCAL_VARIABLE_DECLARATION
)

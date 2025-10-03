package kr.jaehoyi.gdshader.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {
    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            if (child.text.isNotBlank()) {
                val block = GDShaderBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    null,
                    spacingBuilder
                )
                blocks.add(block)
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent? {
        if (myNode.elementType == GDShaderTypes.CURLY_BRACKET_OPEN ||
            myNode.elementType == GDShaderTypes.CURLY_BRACKET_CLOSE) {
            return Indent.getNoneIndent()
        }
        
        val parentType = myNode.treeParent?.elementType
        
        if (parentType == GDShaderTypes.BLOCK_BODY ||
            parentType == GDShaderTypes.STRUCT_MEMBER_LIST ||
            parentType == GDShaderTypes.SWITCH_BODY) {
            return Indent.getNormalIndent()
        }
        
        if (parentType == GDShaderTypes.IF_STATEMENT ||
            parentType == GDShaderTypes.FOR_STATEMENT ||
            parentType == GDShaderTypes.DO_WHILE_STATEMENT ||
            parentType == GDShaderTypes.WHILE_STATEMENT) {
            return Indent.getNormalIndent()
        }
        
        return Indent.getNoneIndent()
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val nodeType = myNode.elementType
        
        if (nodeType == GDShaderTypes.BLOCK ||
            nodeType == GDShaderTypes.STRUCT_DECLARATION ||
            nodeType == GDShaderTypes.SWITCH_BLOCK) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        }
        
        if (nodeType == GDShaderTypes.IF_STATEMENT ||
            nodeType == GDShaderTypes.FOR_STATEMENT ||
            nodeType == GDShaderTypes.DO_WHILE_STATEMENT ||
            nodeType == GDShaderTypes.WHILE_STATEMENT) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        }
        
        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? 
        = spacingBuilder.getSpacing(this, child1, child2)
    
    override fun isLeaf(): Boolean 
        = myNode.firstChildNode == null
}
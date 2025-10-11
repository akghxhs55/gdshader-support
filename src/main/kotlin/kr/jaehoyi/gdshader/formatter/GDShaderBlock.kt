package kr.jaehoyi.gdshader.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.psi.GDShaderTypes

class GDShaderBlock(
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
                val block = GDShaderBlock(
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
        
        if (childType == GDShaderTypes.CURLY_BRACKET_OPEN ||
            childType == GDShaderTypes.CURLY_BRACKET_CLOSE ||
            (parentType == GDShaderTypes.DO_WHILE_STATEMENT && childType == GDShaderTypes.CF_WHILE)) {
            return Indent.getNoneIndent()
        }
        
        if (parentType in INDENT_TRIGGER_BLOCKS) {
            return Indent.getNormalIndent()
        }
        
        val containsBlock = childNode.findChildByType(GDShaderTypes.BLOCK) != null
        
        if (parentType in INDENT_TRIGGER_STATEMENTS &&
            !isFirstChild &&
            !containsBlock) {
            return Indent.getNormalIndent()
        }
        
        return Indent.getNoneIndent()
    }
    
}

private val INDENT_TRIGGER_BLOCKS = TokenSet.create(
    GDShaderTypes.BLOCK,
    GDShaderTypes.STRUCT_BLOCK,
    GDShaderTypes.SWITCH_BLOCK
)

private val INDENT_TRIGGER_STATEMENTS = TokenSet.create(
    GDShaderTypes.IF_STATEMENT,
    GDShaderTypes.ELSE_CLAUSE,
    GDShaderTypes.FOR_STATEMENT,
    GDShaderTypes.WHILE_STATEMENT,
    GDShaderTypes.DO_WHILE_STATEMENT,
    GDShaderTypes.RETURN_STATEMENT,
    GDShaderTypes.SHADER_TYPE_DECLARATION,
    GDShaderTypes.RENDER_MODE_DECLARATION,
    GDShaderTypes.STENCIL_MODE_DECLARATION,
    GDShaderTypes.UNIFORM_GROUP_DECLARATION,
    GDShaderTypes.UNIFORM_DECLARATION,
    GDShaderTypes.CONSTANT_DECLARATION,
    GDShaderTypes.VARYING_DECLARATION,
    GDShaderTypes.LOCAL_VARIABLE_DECLARATION
)

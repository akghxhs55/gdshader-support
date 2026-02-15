package kr.jaehoyi.gdshader.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.TokenSet
import kr.jaehoyi.gdshader.psi.GdsTypes

class GdsBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val myIndent: Indent?,
    private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE && child.textRange.length > 0) {
                if (child.elementType == GdsTypes.STATEMENT_BODY ||
                    child.elementType == GdsTypes.HINT_SECTION) {

                    var grandChild = child.firstChildNode
                    while (grandChild != null) {
                        if (grandChild.elementType != TokenType.WHITE_SPACE && grandChild.textRange.length > 0) {
                            val isFirst = blocks.isEmpty()
                            val indent = computeIndent(grandChild, isFirst)

                            blocks.add(GdsBlock(
                                grandChild,
                                Wrap.createWrap(WrapType.NONE, false),
                                null,
                                indent,
                                spacingBuilder
                            ))
                        }
                        grandChild = grandChild.treeNext
                    }
                } else {
                    val isFirst = blocks.isEmpty()
                    val indent = computeIndent(child, isFirst)
                    blocks.add(GdsBlock(child, Wrap.createWrap(WrapType.NONE, false), null, indent, spacingBuilder))
                }
            }
            child = child.treeNext
        }
        return blocks
    }

    override fun getIndent(): Indent? = myIndent
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = spacingBuilder.getSpacing(this, child1, child2)
    override fun isLeaf(): Boolean = myNode.firstChildNode == null

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val type = myNode.elementType

        if (type in BLOCKS || type in BODY_CONTAINERS || type == GdsTypes.INITIALIZER_LIST) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        }
        if (type in CONTAINERS || type in EXPRESSIONS) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        }
        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    private fun computeIndent(child: ASTNode, isFirst: Boolean): Indent {
        val parentType = myNode.elementType
        val childType = child.elementType

        if (parentType == GdsTypes.ITEM) return Indent.getNoneIndent()
        if (childType in BRACKETS) return Indent.getNoneIndent()
        
        if (parentType in BLOCKS && childType in BODY_CONTAINERS) return Indent.getNoneIndent()
        if (parentType in BODY_CONTAINERS) return Indent.getNormalIndent()
        if (parentType == GdsTypes.INITIALIZER_LIST) return Indent.getNormalIndent()

        if (parentType in CONTAINERS) return Indent.getNormalIndent()

        if (parentType in EXPRESSIONS) {
            if (isFirst) {
                return Indent.getNoneIndent()
            }
            return Indent.getContinuationIndent()
        }

        if (parentType in CONTROL_STATEMENTS) {
            if (childType != GdsTypes.PARENTHESIS_OPEN &&
                childType != GdsTypes.PARENTHESIS_CLOSE &&
                childType !in KEYWORDS &&
                childType !in BLOCKS) {
                return Indent.getNormalIndent()
            }
        }

        return Indent.getNoneIndent()
    }

    companion object {
        private val BLOCKS = TokenSet.create(
            GdsTypes.BLOCK,
            GdsTypes.STRUCT_BLOCK,
            GdsTypes.SWITCH_BLOCK
        )

        private val BODY_CONTAINERS = TokenSet.create(
            GdsTypes.BLOCK_BODY,
            GdsTypes.CASE_BODY,
            GdsTypes.STRUCT_MEMBER_LIST,
            GdsTypes.SWITCH_BODY
        )

        private val CONTAINERS = TokenSet.create(
            GdsTypes.PARAMETER_LIST,
            GdsTypes.ARGUMENT_LIST,
            GdsTypes.PARENTHESIS_OPEN,
            GdsTypes.PARENTHESIS_CLOSE
        )

        private val EXPRESSIONS = TokenSet.create(
            GdsTypes.EXPRESSION,
            GdsTypes.CONDITIONAL_EXPR,
            GdsTypes.ASSIGN_EXPR,
            GdsTypes.LOGIC_OR_EXPR,
            GdsTypes.LOGIC_AND_EXPR,
            GdsTypes.ADDITIVE_EXPR,
            GdsTypes.MULTIPLICATIVE_EXPR
        )

        private val CONTROL_STATEMENTS = TokenSet.create(
            GdsTypes.IF_STATEMENT,
            GdsTypes.ELSE_CLAUSE,
            GdsTypes.FOR_STATEMENT,
            GdsTypes.WHILE_STATEMENT,
            GdsTypes.DO_WHILE_STATEMENT
        )

        private val BRACKETS = TokenSet.create(
            GdsTypes.CURLY_BRACKET_OPEN, GdsTypes.CURLY_BRACKET_CLOSE,
            GdsTypes.PARENTHESIS_OPEN, GdsTypes.PARENTHESIS_CLOSE,
            GdsTypes.BRACKET_OPEN, GdsTypes.BRACKET_CLOSE
        )

        private val KEYWORDS = TokenSet.create(
            GdsTypes.CF_IF, GdsTypes.CF_ELSE, GdsTypes.CF_FOR, GdsTypes.CF_WHILE, GdsTypes.CF_DO
        )
    }
}
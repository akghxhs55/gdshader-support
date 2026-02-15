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

        for (child in myNode.children()) {
            if (child.elementType in TRANSPARENT_NODES) {
                for (grandChild in child.children()) {
                    blocks.addBlock(grandChild)
                }
            } else {
                blocks.addBlock(child)
            }
        }

        return blocks
    }

    override fun getIndent(): Indent? = myIndent
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = spacingBuilder.getSpacing(this, child1, child2)
    override fun isLeaf(): Boolean = myNode.firstChildNode == null

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val indent = when (myNode.elementType) {
            in BLOCKS, in BODY_CONTAINERS, GdsTypes.INITIALIZER_LIST -> Indent.getNormalIndent()
            in CONTAINERS, in PARENTHESIZED, in EXPRESSIONS -> Indent.getNormalIndent()
            in CONTROL_STATEMENTS, GdsTypes.FUNCTION_CALL -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
        return ChildAttributes(indent, null)
    }

    private fun computeIndent(child: ASTNode, isFirst: Boolean): Indent {
        val parentType = myNode.elementType
        val childType = child.elementType

        if (parentType == GdsTypes.ITEM) return Indent.getNoneIndent()
        if (childType in BRACKETS) return Indent.getNoneIndent()

        return when (parentType) {
            in BLOCKS -> if (childType in BODY_CONTAINERS) Indent.getNoneIndent() else Indent.getNormalIndent()
            in BODY_CONTAINERS -> Indent.getNormalIndent()
            GdsTypes.INITIALIZER_LIST -> Indent.getNormalIndent()
            in CONTAINERS -> Indent.getNormalIndent()
            in PARENTHESIZED -> if (isFirst) Indent.getNoneIndent() else Indent.getNormalIndent()
            in EXPRESSIONS -> if (isFirst) Indent.getNoneIndent() else Indent.getContinuationIndent()
            in CONTROL_STATEMENTS -> {
                if (childType !in BRACKETS && childType !in KEYWORDS && childType !in BLOCKS) {
                    Indent.getNormalIndent()
                } else {
                    Indent.getNoneIndent()
                }
            }
            else -> Indent.getNoneIndent()
        }
    }

    private fun MutableList<Block>.addBlock(node: ASTNode) {
        val indent = computeIndent(node, isEmpty())
        add(GdsBlock(node, Wrap.createWrap(WrapType.NONE, false), null, indent, spacingBuilder))
    }

    companion object {
        private fun ASTNode.children(): Sequence<ASTNode> = generateSequence(firstChildNode) { it.treeNext }
            .filter { it.elementType != TokenType.WHITE_SPACE && it.textRange.length > 0 }

        private val TRANSPARENT_NODES = TokenSet.create(
            GdsTypes.STATEMENT_BODY,
            GdsTypes.STATEMENT,
            GdsTypes.HINT_SECTION
        )

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
            GdsTypes.LOCAL_VARIABLE_DECLARATOR_LIST,
            GdsTypes.CONSTANT_DECLARATOR_LIST,
            GdsTypes.RENDER_MODE_DECLARATOR_LIST,
            GdsTypes.STENCIL_MODE_DECLARATOR_LIST,
            GdsTypes.HINT_LIST
        )

        private val PARENTHESIZED = TokenSet.create(
            GdsTypes.ENUM_HINT,
            GdsTypes.RANGE_HINT,
            GdsTypes.INSTANCE_INDEX_HINT
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

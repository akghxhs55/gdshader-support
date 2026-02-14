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

                // [수정] HINT_SECTION도 투명하게 처리 (Flatten)
                // 이렇게 해야 COLON이 상위 블록(UNIFORM_DECLARATION)에 직접 노출되어 Spacing 규칙이 먹힘
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
                    // ... 기존 일반 로직 ...
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

        // 엔터 쳤을 때 커서 위치 계산
        // [수정] BLOCK_BODY 등 실제 내용을 담는 컨테이너도 처리
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

        // 1. 블록 ({...}) 내부 내용물 -> Normal
        if (parentType in BLOCKS && childType in BODY_CONTAINERS) return Indent.getNoneIndent()
        if (parentType in BODY_CONTAINERS) return Indent.getNormalIndent()
        if (parentType == GdsTypes.INITIALIZER_LIST) return Indent.getNormalIndent()

        // 2. 괄호 컨테이너 (파라미터 등) -> Normal
        if (parentType in CONTAINERS) return Indent.getNormalIndent()

        // 3. [핵심 수정] 표현식 (Expression) 처리
        // 수식이 깊게 중첩되어도 첫 번째 요소(왼쪽 항)는 들여쓰기를 하지 않아야 누적되지 않습니다.
        if (parentType in EXPRESSIONS) {
            if (isFirst) {
                return Indent.getNoneIndent() // 첫 번째 자식은 들여쓰기 없음
            }
            return Indent.getContinuationIndent() // 두 번째 이후(연산자, 우항)는 줄바꿈 시 들여쓰기
        }

        // 4. 제어문 (if, for 등) 하위 구문
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
            GdsTypes.CF_IF, GdsTypes.CF_ELSE, GdsTypes.CF_FOR, GdsTypes.CF_WHILE
        )
    }
}
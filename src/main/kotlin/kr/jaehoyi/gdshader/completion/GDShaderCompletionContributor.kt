package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PlatformPatterns.or
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderBlockBody
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderDoWhileStatement
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderForStatement
import kr.jaehoyi.gdshader.psi.GDShaderFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderHintList
import kr.jaehoyi.gdshader.psi.GDShaderIfStatement
import kr.jaehoyi.gdshader.psi.GDShaderRenderModeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderStencilModeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderStructDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderSwitchStatement
import kr.jaehoyi.gdshader.psi.GDShaderTokenSets
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVariableNameRef
import kr.jaehoyi.gdshader.psi.GDShaderVaryingDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderWhileStatement
import kr.jaehoyi.gdshader.model.DataType

class GDShaderCompletionContributor : CompletionContributor() {
    
    init {
        extendShaderTypeDeclaration()
        extendRenderModeDeclaration()
        extendStencilModeDeclaration()
        extendUniformGroupDeclaration()
        extendUniformDeclaration()
        extendConstantDeclaration()
        extendVaryingDeclaration()
        extendFunctionDeclaration()
        extendStructDeclaration()
        extendStatement()
    }

    private fun extendShaderTypeDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().inside(GDShaderShaderTypeDeclaration::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position

                    // shader_type_declaration ::= SHADER_TYPE shader_type_name SEMICOLON

                    // 1.
                    if (position.parent is GDShaderFile) {
                        result.addElement(GDShaderLookupElements.SHADER_TYPE_KEYWORD)
                        return
                    }

                    // 2. After SHADER_TYPE
                    result.addAllElements(GDShaderLookupElements.SHADER_TYPES)
                }
            }
        )
    
    private fun extendRenderModeDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().inside(GDShaderRenderModeDeclaration::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position

                    // render_mode_declaration ::= RENDER_MODE render_mode_declarator_list SEMICOLON

                    // 1.
                    if (position.parent is GDShaderFile) {
                        result.addElement(GDShaderLookupElements.RENDER_MODE_KEYWORD)
                        return
                    }

                    // 2. After RENDER_MODE
                    result.addAllElements(GDShaderLookupElements.RENDER_MODES)
                }
            }
        )
    
    private fun extendStencilModeDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().inside(GDShaderStencilModeDeclaration::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position

                    // stencil_mode_declaration ::= STENCIL_MODE stencil_mode_declarator_list SEMICOLON

                    // 1.
                    if (position.parent is GDShaderFile) {
                        result.addElement(GDShaderLookupElements.STENCIL_MODE_KEYWORD)
                        return
                    }

                    // 2. After STENCIL_MODE
                    result.addAllElements(GDShaderLookupElements.STENCIL_MODES)
                }
            }
        )
    
    private fun extendUniformGroupDeclaration() =
        extend(
            CompletionType.BASIC,
            GDShaderPatterns.TOP_LEVEL,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    // uniform_group_declaration ::= UNIFORM_GROUP uniform_group_name? (PERIOD uniform_group_name)* SEMICOLON

                    // 1.
                    result.addElement(GDShaderLookupElements.UNIFORM_GROUP_KEYWORD)
                }
            }
        )
    
    private fun extendUniformDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().afterLeaf(psiElement(GDShaderTypes.GLOBAL)),
                psiElement().afterLeaf(psiElement(GDShaderTypes.INSTANCE)),
                psiElement().inside(GDShaderUniformDeclaration::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = PsiTreeUtil.prevVisibleLeaf(position)

                    // uniform_declaration ::= uniform_header precision? type array_size? variable_name_decl array_size? hint_section? (OP_ASSIGN expression)? SEMICOLON

                    // 1.
                    if (position.parent is GDShaderFile) {
                        result.addElement(GDShaderLookupElements.GLOBAL_KEYWORD)
                        result.addElement(GDShaderLookupElements.INSTANCE_KEYWORD)
                        result.addElement(GDShaderLookupElements.UNIFORM_KEYWORD)
                        return
                    }

                    if (prevLeaf == null) {
                        return
                    }

                    // 2. After modifier (GLOBAL / INSTANCE)
                    if (prevLeaf.elementType == GDShaderTypes.GLOBAL || prevLeaf.elementType == GDShaderTypes.INSTANCE) {
                        result.addElement(GDShaderLookupElements.UNIFORM_KEYWORD)
                        return
                    }

                    // 3. After UNIFORM
                    if (prevLeaf.elementType == GDShaderTypes.UNIFORM) {
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }

                    // 4. After precision
                    if (GDShaderKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }

                    // 5. After COLON
                    if (prevLeaf.elementType == GDShaderTypes.COLON) {
                        val uniformDeclaration = position.parentOfType<GDShaderUniformDeclaration>()
                        val typeText = uniformDeclaration?.type?.text ?: return
                        val type = DataType.fromText(typeText) ?: return

                        result.addAllElements(
                            GDShaderLookupElements.UNIFORM_HINTS[type] ?: emptyList()
                        )
                        return
                    }
                    
                    // 6. After COMMA
                    if (prevLeaf.elementType == GDShaderTypes.COMMA) {
                        if (PsiTreeUtil.prevVisibleLeaf(prevLeaf)?.parentOfType<GDShaderHintList>() == null) {
                            return
                        }
                        
                        val uniformDeclaration = position.parentOfType<GDShaderUniformDeclaration>()
                        val typeText = uniformDeclaration?.type?.text ?: return
                        val type = DataType.fromText(typeText) ?: return

                        result.addAllElements(
                            GDShaderLookupElements.UNIFORM_HINTS[type] ?: emptyList()
                        )
                        return
                    }
                    
                    // 6. Inside initializer expression
                    if (position.parent.elementType == GDShaderTypes.VARIABLE_NAME_REF) {
                        result.addExpressionCompletions()
                    }
                }
            }
        )
    
    private fun extendConstantDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().inside(GDShaderBlockBody::class.java)
                    .andOr(
                        psiElement().afterLeaf(psiElement(GDShaderTypes.CURLY_BRACKET_OPEN)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.SEMICOLON))
                    ),
                psiElement().inside(GDShaderConstantDeclaration::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = PsiTreeUtil.prevVisibleLeaf(position)

                    // constant_declaration ::= CONST precision? type array_size? constant_declarator_list SEMICOLON

                    // 1.
                    if (position.parent is GDShaderFile ||
                        (position.parentOfType<GDShaderBlock>() != null && (prevLeaf.elementType == GDShaderTypes.CURLY_BRACKET_OPEN || prevLeaf.elementType == GDShaderTypes.SEMICOLON))
                    ) {
                        result.addElement(GDShaderLookupElements.CONST_KEYWORD)
                        return
                    }

                    if (prevLeaf == null) {
                        return
                    }

                    // 2. After CONST
                    if (prevLeaf.elementType == GDShaderTypes.CONST) {
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }

                    // 3. After precision
                    if (GDShaderKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    // 4. Inside initializer expression
                    if (position.parent.elementType == GDShaderTypes.VARIABLE_NAME_REF) {
                        result.addExpressionCompletions()
                    }
                }
            }
        )
        
    private fun extendVaryingDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().inside(GDShaderVaryingDeclaration::class.java)
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = PsiTreeUtil.prevVisibleLeaf(position)

                    // varying_declaration ::= VARYING (INTERPOLATION_FLAT | INTERPOLATION_SMOOTH)? precision? type array_size? variable_name_decl array_size? SEMICOLON

                    // 1.
                    if (position.parent is GDShaderFile) {
                        result.addElement(GDShaderLookupElements.VARYING_KEYWORD)
                        return
                    }

                    if (prevLeaf == null) {
                        return
                    }

                    // 2. After VARYING
                    if (prevLeaf.elementType == GDShaderTypes.VARYING) {
                        result.addAllElements(GDShaderLookupElements.INTERPOLATIONS)
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }

                    // 3. After interpolation modifier
                    if (GDShaderKeywords.INTERPOLATIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }

                    // 4. After precision modifier
                    if (GDShaderKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                }
            }
        )
    
    private fun extendFunctionDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().withParent(GDShaderFile::class.java).and(GDShaderPatterns.AFTER_PRECISION),
                psiElement().inside(GDShaderFunctionDeclaration::class.java)
                    .andNot(psiElement().inside(GDShaderBlock::class.java))
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = PsiTreeUtil.prevVisibleLeaf(position)

                    // function_declaration ::= (precision? type) function_name_decl PARENTHESIS_OPEN parameter_list? PARENTHESIS_CLOSE block

                    // 1.
                    if (position.parent is GDShaderFile && !GDShaderKeywords.PRECISIONS.contains(prevLeaf?.text)) {
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    // 2. After precision
                    if (prevLeaf != null && GDShaderKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    if (position.parentOfType<GDShaderFunctionDeclaration>() == null) {
                        return
                    }
                    
                    // 3. Inside parameter list, after COMMA or PARENTHESIS_OPEN
                    if (prevLeaf.elementType == GDShaderTypes.COMMA || prevLeaf.elementType == GDShaderTypes.PARENTHESIS_OPEN) {
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        result.addAllElements(GDShaderLookupElements.PARAMETER_QUALIFIERS)
                        result.addElement(GDShaderLookupElements.CONST_KEYWORD)
                        return
                    }
                    
                    // 4. Inside parameter list, after CONST
                    if (prevLeaf.elementType == GDShaderTypes.CONST) {
                        result.addElement(GDShaderLookupElements.IN_KEYWORD)
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    // 5. Inside parameter list, after parameter qualifier
                    if (GDShaderKeywords.PARAMETER_QUALIFIERS.contains(prevLeaf?.text)) {
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    // 6. Inside parameter list, after precision
                    if (GDShaderKeywords.PRECISIONS.contains(prevLeaf?.text))
                    {
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    // Inside array size
                    if (prevLeaf.elementType == GDShaderTypes.BRACKET_OPEN) {
                        result.addAllElements(GDShaderLookupElements.INTEGER_TYPES)
                    }
                }
            }
        )
    
    private fun extendStructDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GDShaderPatterns.TOP_LEVEL,
                psiElement().inside(GDShaderStructDeclaration::class.java)
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = PsiTreeUtil.prevVisibleLeaf(position)
                    
                    // struct_declaration ::= STRUCT struct_name_decl struct_block SEMICOLON
                    
                    // 1.
                    if (position.parent is GDShaderFile) {
                        result.addElement(GDShaderLookupElements.STRUCT_KEYWORD)
                        return
                    }
                    
                    if (position.parentOfType<GDShaderStructDeclaration>() == null) {
                        return
                    }
                    
                    // 2. After CURLY_BRACKET_OPEN or SEMICOLON
                    if (prevLeaf.elementType == GDShaderTypes.CURLY_BRACKET_OPEN || prevLeaf.elementType == GDShaderTypes.SEMICOLON) {
                        result.addAllElements(GDShaderLookupElements.PRECISIONS)
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    // 3. After precision
                    if (GDShaderKeywords.PRECISIONS.contains(prevLeaf?.text)) {
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        return
                    }
                    
                    // 4. Inside array size
                    if (prevLeaf.elementType == GDShaderTypes.BRACKET_OPEN) {
                        result.addAllElements(GDShaderLookupElements.INTEGER_TYPES)
                    }
                }
            }
        )

    private fun extendStatement() =
        extend(
            CompletionType.BASIC,
            or(
                psiElement().inside(GDShaderBlock::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position

                    if (position.parentOfType<GDShaderVariableNameRef>() != null) {
                        result.addExpressionCompletions()
                    }

                    val prevLeaf = PsiTreeUtil.prevVisibleLeaf(position) ?: return
                    
                    when (prevLeaf.elementType) {
                        GDShaderTypes.CURLY_BRACKET_OPEN,
                        GDShaderTypes.CF_ELSE,
                        GDShaderTypes.CF_DO -> {
                            result.addCommonStatementCompletions(position)
                        }

                        GDShaderTypes.CURLY_BRACKET_CLOSE -> {
                            result.addCommonStatementCompletions(position)
                            
                            val prevStatement = prevLeaf.parentOfType<GDShaderIfStatement>() ?: return

                            if (position.parentOfType<GDShaderIfStatement>() != prevStatement) {
                                result.addElement(GDShaderLookupElements.ELSE_KEYWORD)
                                return
                            }
                        }

                        GDShaderTypes.SEMICOLON -> {
                            if (prevLeaf.parent is GDShaderForStatement) {
                                result.addExpressionCompletions()
                            } else {
                                result.addCommonStatementCompletions(position)
                            }
                        }

                        GDShaderTypes.COLON -> {
                            if (prevLeaf.parent.elementType == GDShaderTypes.CASE_CLAUSE) {
                                result.addCommonStatementCompletions(position)
                                result.addElement(GDShaderLookupElements.BREAK_KEYWORD)
                            } else {
                                result.addExpressionCompletions()
                            }
                        }

                        GDShaderTypes.PARENTHESIS_CLOSE -> {
                            if (prevLeaf.parent.elementType == GDShaderTypes.IF_STATEMENT ||
                                prevLeaf.parent.elementType == GDShaderTypes.FOR_STATEMENT ||
                                prevLeaf.parent.elementType == GDShaderTypes.WHILE_STATEMENT
                            ) {
                                result.addCommonStatementCompletions(position)
                            }
                        }

                        GDShaderTypes.BRACKET_OPEN,
                        GDShaderTypes.CF_CASE,
                        GDShaderTypes.CF_RETURN,
                        GDShaderTypes.QUESTION,
                        in GDShaderTokenSets.OPERATORS -> {
                            result.addExpressionCompletions()
                        }

                        GDShaderTypes.PARENTHESIS_OPEN -> {
                            if (prevLeaf.parent.elementType == GDShaderTypes.FOR_STATEMENT) {
                                result.addAllElements(GDShaderLookupElements.PRECISIONS)
                                result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                            } else {
                                result.addExpressionCompletions()
                            }
                        }
                    }
                }
            }
        )
    
    private fun CompletionResultSet.addCommonStatementCompletions(position: PsiElement) {
        println(GDShaderLookupElements.BUILTIN_TYPES.map { it.lookupString })
        
        this.addAllElements(GDShaderLookupElements.CONTROL_STATEMENT_STARTERS)
        this.addElement(GDShaderLookupElements.RETURN_KEYWORD)
        this.addElement(GDShaderLookupElements.DISCARD_KEYWORD)
        this.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
        this.addAllElements(GDShaderLookupElements.BUILTIN_FUNCTIONS)
        this.addAllElements(GDShaderLookupElements.PRECISIONS)
        this.addElement(GDShaderLookupElements.CONST_KEYWORD)
        
        if (position.parentOfType<GDShaderForStatement>() != null ||
            position.parentOfType<GDShaderWhileStatement>() != null ||
            position.parentOfType<GDShaderDoWhileStatement>() != null
        ) {
            this.addElement(GDShaderLookupElements.BREAK_KEYWORD)
            this.addElement(GDShaderLookupElements.CONTINUE_KEYWORD)
        }
        
        if (position.parentOfType<GDShaderSwitchStatement>() != null) {
            this.addElement(GDShaderLookupElements.BREAK_KEYWORD)
        }
    }
    
    private fun CompletionResultSet.addExpressionCompletions() {
        this.addAllElements(GDShaderLookupElements.CONSTRUCTORS)
        this.addAllElements(GDShaderLookupElements.BUILTIN_FUNCTIONS)
        this.addAllElements(GDShaderLookupElements.BOOLEAN_LITERALS)
    }
    
}
package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PlatformPatterns.or
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.*
import kr.jaehoyi.gdshader.model.Builtins
import kr.jaehoyi.gdshader.model.DataType
import kr.jaehoyi.gdshader.model.FunctionContext
import kr.jaehoyi.gdshader.model.MemberAccessible
import kr.jaehoyi.gdshader.model.ShaderType
import kr.jaehoyi.gdshader.model.StructType
import kr.jaehoyi.gdshader.model.VectorType
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable
import kr.jaehoyi.gdshader.psi.impl.GdsPsiImplUtil
import kr.jaehoyi.gdshader.resolve.GdsResolver
import kotlin.collections.plusAssign


class GdsCompletionContributor : CompletionContributor() {
    
    init {
        extendShaderTypeDeclaration()
        extendRenderModeDeclaration()
        extendStencilModeDeclaration()
        extendUniformGroupDeclaration()
        extendUniformDeclaration()
        extendConstantDeclaration()
        extendVaryingDeclaration()
        extendFunctionDeclaration()
        extendProcessingFunctionDeclaration()
        extendStructDeclaration()
        extendStatement()
        extendPostfixExpression()
        extendPreprocessorDirective()
    }

    private fun extendShaderTypeDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement(GdsTypes.IDENTIFIER).inside(GdsShaderTypeDeclaration::class.java),
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
                    if (position.parent is GdsFile) {
                        result.addElement(GdsLookupElements.SHADER_TYPE_KEYWORD)
                        return
                    }

                    // 2. After SHADER_TYPE
                    result.addAllElements(GdsLookupElements.SHADER_TYPES)
                }
            }
        )
    
    private fun extendRenderModeDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement().inside(GdsRenderModeDeclaration::class.java),
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
                    if (position.parent is GdsFile) {
                        result.addElement(GdsLookupElements.RENDER_MODE_KEYWORD)
                        return
                    }

                    // 2. After RENDER_MODE
                    result.addAllElements(GdsLookupElements.RENDER_MODES)
                }
            }
        )
    
    private fun extendStencilModeDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement().inside(GdsStencilModeDeclaration::class.java),
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
                    if (position.parent is GdsFile) {
                        result.addElement(GdsLookupElements.STENCIL_MODE_KEYWORD)
                        return
                    }

                    // 2. After STENCIL_MODE
                    result.addAllElements(GdsLookupElements.STENCIL_MODES)
                }
            }
        )
    
    private fun extendUniformGroupDeclaration() =
        extend(
            CompletionType.BASIC,
            GdsPatterns.TOP_LEVEL,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    // uniform_group_declaration ::= UNIFORM_GROUP uniform_group_name? (PERIOD uniform_group_name)* SEMICOLON

                    // 1.
                    result.addElement(GdsLookupElements.UNIFORM_GROUP_KEYWORD)
                }
            }
        )
    
    private fun extendUniformDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement().afterLeaf(psiElement(GdsTypes.GLOBAL)),
                psiElement().afterLeaf(psiElement(GdsTypes.INSTANCE)),
                psiElement().inside(GdsUniformDeclaration::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = getPrevCodeLeaf(position)

                    // uniform_declaration ::= uniform_header precision? type array_size? variable_name_decl array_size? hint_section? (OP_ASSIGN expression)? SEMICOLON

                    // 1.
                    if (position.parent is GdsFile) {
                        result.addElement(GdsLookupElements.GLOBAL_KEYWORD)
                        result.addElement(GdsLookupElements.INSTANCE_KEYWORD)
                        result.addElement(GdsLookupElements.UNIFORM_KEYWORD)
                        return
                    }

                    if (prevLeaf == null) {
                        return
                    }

                    // 2. After modifier (GLOBAL / INSTANCE)
                    if (prevLeaf.elementType == GdsTypes.GLOBAL || prevLeaf.elementType == GdsTypes.INSTANCE) {
                        result.addElement(GdsLookupElements.UNIFORM_KEYWORD)
                        return
                    }

                    // 3. After UNIFORM
                    if (prevLeaf.elementType == GdsTypes.UNIFORM) {
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        result.addAllElements(GdsLookupElements.OPAQUE_BUILTIN_TYPES)
                        return
                    }

                    // 4. After precision
                    if (GdsKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        result.addAllElements(GdsLookupElements.OPAQUE_BUILTIN_TYPES)
                        return
                    }

                    // 5. After COLON
                    if (prevLeaf.elementType == GdsTypes.COLON) {
                        val uniformDeclaration = position.parentOfType<GdsUniformDeclaration>()
                        val typeText = uniformDeclaration?.type?.text ?: return
                        val type = Builtins.getType(typeText) ?: return

                        result.addAllElements(
                            GdsLookupElements.UNIFORM_HINTS[type] ?: emptyList()
                        )
                        return
                    }
                    
                    // 6. After COMMA
                    if (prevLeaf.elementType == GdsTypes.COMMA) {
                        if (position.parentOfType<GdsHintSection>() == null || prevLeaf.parentOfType<GdsHint>() != null) {
                            return
                        }
                        
                        val uniformDeclaration = position.parentOfType<GdsUniformDeclaration>()
                        val typeText = uniformDeclaration?.type?.text ?: return
                        val type = Builtins.getType(typeText) ?: return

                        result.addAllElements(
                            GdsLookupElements.UNIFORM_HINTS[type] ?: emptyList()
                        )
                        return
                    }
                    
                    // 6. Inside initializer expression
                    if (position.parent.elementType == GdsTypes.VARIABLE_NAME_REF) {
                        result.addAllElements(getExpressionCompletions(position))
                    }
                }
            }
        )
    
    private fun extendConstantDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement().inside(GdsBlockBody::class.java)
                    .andOr(
                        psiElement().afterLeaf(psiElement(GdsTypes.CURLY_BRACKET_OPEN)),
                        psiElement().afterLeaf(psiElement(GdsTypes.SEMICOLON))
                    ),
                psiElement().inside(GdsConstantDeclaration::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = getPrevCodeLeaf(position)

                    // constant_declaration ::= CONST precision? type array_size? constant_declarator_list SEMICOLON

                    // 1.
                    if (position.parent is GdsFile ||
                        (position.parentOfType<GdsBlock>() != null && (prevLeaf.elementType == GdsTypes.CURLY_BRACKET_OPEN || prevLeaf.elementType == GdsTypes.SEMICOLON))
                    ) {
                        result.addElement(GdsLookupElements.CONST_KEYWORD)
                        return
                    }

                    if (prevLeaf == null) {
                        return
                    }

                    // 2. After CONST
                    if (prevLeaf.elementType == GdsTypes.CONST) {
                        val completions = arrayListOf<LookupElement>()
                        completions += GdsLookupElements.PRECISIONS
                        completions += GdsLookupElements.DECLARABLE_BUILTIN_TYPES
                        GdsResolver.processStructDeclaration(position) { element ->
                            completions += GdsLookupElements.createTypeDeclarationFromStructNameDecl(element)
                            return@processStructDeclaration true
                        }
                        result.addAllElements(completions)
                        return
                    }

                    // 3. After precision
                    if (GdsKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        return
                    }
                    
                    // 4. Inside initializer expression
                    if (position.parent.elementType == GdsTypes.VARIABLE_NAME_REF) {
                        result.addAllElements(getExpressionCompletions(position))
                    }
                }
            }
        )
        
    private fun extendVaryingDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement().inside(GdsVaryingDeclaration::class.java)
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = getPrevCodeLeaf(position)

                    // varying_declaration ::= VARYING (INTERPOLATION_FLAT | INTERPOLATION_SMOOTH)? precision? type array_size? variable_name_decl array_size? SEMICOLON

                    // 1.
                    if (position.parent is GdsFile) {
                        result.addElement(GdsLookupElements.VARYING_KEYWORD)
                        return
                    }

                    if (prevLeaf == null) {
                        return
                    }

                    // 2. After VARYING
                    if (prevLeaf.elementType == GdsTypes.VARYING) {
                        result.addAllElements(GdsLookupElements.INTERPOLATIONS)
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        return
                    }

                    // 3. After interpolation modifier
                    if (GdsKeywords.INTERPOLATIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        return
                    }

                    // 4. After precision modifier
                    if (GdsKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        return
                    }
                }
            }
        )
    
    private fun extendFunctionDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement().withParent(GdsFile::class.java),
                psiElement().inside(GdsFunctionDeclaration::class.java)
                    .andNot(psiElement().inside(GdsBlock::class.java))
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = getPrevCodeLeaf(position)

                    // function_declaration ::= (precision? type) function_name_decl PARENTHESIS_OPEN parameter_list? PARENTHESIS_CLOSE block

                    // 1.
                    if (GdsPatterns.TOP_LEVEL.accepts(position)) {
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.RETURNABLE_BUILTIN_TYPES)
                        return
                    }
                    
                    if (prevLeaf == null) {
                        return
                    }
                    
                    // 2. After precision
                    if (position.parent !is GdsFunctionDeclaration &&
                        GdsKeywords.PRECISIONS.contains(prevLeaf.text)
                    ) {
                        result.addAllElements(GdsLookupElements.RETURNABLE_BUILTIN_TYPES)
                        return
                    }
                    
                    // 3. After return type
                    if (position.parentOfType<GdsFunctionDeclaration>() == null &&
                        prevLeaf.text == "void"
                    ) {
                        val file = position.containingFile as? GdsFile ?: return
                        val shaderType = GdsPsiImplUtil.getShaderType(file) ?: ShaderType.SPATIAL
                        result.addAllElements(GdsLookupElements.PROCESSING_FUNCTIONS[shaderType] ?: emptyList())
                        return
                    }

                    if (position.parentOfType<GdsFunctionDeclaration>() == null) {
                        return
                    }
                    
                    // 4. Inside the parameter list, after COMMA or PARENTHESIS_OPEN
                    if (prevLeaf.elementType == GdsTypes.COMMA || prevLeaf.elementType == GdsTypes.PARENTHESIS_OPEN) {
                        result.addElement(GdsLookupElements.CONST_KEYWORD)
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        result.addAllElements(GdsLookupElements.OPAQUE_BUILTIN_TYPES)
                        result.addAllElements(GdsLookupElements.PARAMETER_QUALIFIERS)
                        return
                    }
                    
                    // 5. Inside the parameter list, after CONST
                    if (prevLeaf.elementType == GdsTypes.CONST) {
                        result.addElement(GdsLookupElements.IN_KEYWORD)
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        result.addAllElements(GdsLookupElements.OPAQUE_BUILTIN_TYPES)
                        return
                    }
                    
                    // 6. Inside parameter list, after parameter qualifier
                    if (GdsKeywords.PARAMETER_QUALIFIERS.contains(prevLeaf.text)) {
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        result.addAllElements(GdsLookupElements.OPAQUE_BUILTIN_TYPES)
                        return
                    }
                    
                    // 7. Inside parameter list, after precision
                    if (GdsKeywords.PRECISIONS.contains(prevLeaf.text)) {
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        result.addAllElements(GdsLookupElements.OPAQUE_BUILTIN_TYPES)
                        return
                    }
                    
                    // 8. Inside array size
                    if (prevLeaf.elementType == GdsTypes.BRACKET_OPEN) {
                        result.addAllElements(GdsLookupElements.INTEGER_TYPE_CONSTRUCTORS)
                    }
                }
            }
        )
    
    private fun extendProcessingFunctionDeclaration() =
        extend(
            CompletionType.BASIC,
            GdsPatterns.TOP_LEVEL,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    
                    val file = position.containingFile as? GdsFile ?: return
                    val shaderType = GdsPsiImplUtil.getShaderType(file) ?: return
                    
                    result.addAllElements(GdsLookupElements.PROCESSING_FUNCTIONS_WITHOUT_RETURN_TYPE[shaderType] ?: emptyList())
                }
            }
        )
    
    private fun extendStructDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                GdsPatterns.TOP_LEVEL,
                psiElement().inside(GdsStructDeclaration::class.java)
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val prevLeaf = getPrevCodeLeaf(position)
                    
                    // struct_declaration ::= STRUCT struct_name_decl struct_block SEMICOLON
                    
                    // 1.
                    if (position.parent is GdsFile) {
                        result.addElement(GdsLookupElements.STRUCT_KEYWORD)
                        return
                    }
                    
                    if (position.parentOfType<GdsStructDeclaration>() == null) {
                        return
                    }
                    
                    // 2. After CURLY_BRACKET_OPEN or SEMICOLON
                    if (prevLeaf.elementType == GdsTypes.CURLY_BRACKET_OPEN || prevLeaf.elementType == GdsTypes.SEMICOLON) {
                        result.addAllElements(GdsLookupElements.PRECISIONS)
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        GdsResolver.processStructDeclaration(position) { element ->
                            result.addElement(GdsLookupElements.createTypeDeclarationFromStructNameDecl(element))
                            return@processStructDeclaration true
                        }
                        return
                    }
                    
                    // 3. After precision
                    if (GdsKeywords.PRECISIONS.contains(prevLeaf?.text)) {
                        result.addAllElements(GdsLookupElements.DECLARABLE_BUILTIN_TYPES)
                        GdsResolver.processStructDeclaration(position) { element ->
                            result.addElement(GdsLookupElements.createTypeDeclarationFromStructNameDecl(element))
                            return@processStructDeclaration true
                        }
                        return
                    }
                    
                    // 4. Inside array size
                    if (prevLeaf.elementType == GdsTypes.BRACKET_OPEN) {
                        result.addAllElements(GdsLookupElements.INTEGER_TYPE_CONSTRUCTORS)
                    }
                }
            }
        )

    private fun extendStatement() =
        extend(
            CompletionType.BASIC,
            or(
                psiElement().inside(GdsBlock::class.java),
            ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val completions = arrayListOf<LookupElement>()

                    val position = parameters.position
                    val prevLeaf = getPrevCodeLeaf(position) ?: return

                    if (position.parentOfType<GdsVariableNameRef>() != null) {
                        completions += getExpressionCompletions(position)
                    }

                    when (prevLeaf.elementType) {
                        GdsTypes.CURLY_BRACKET_OPEN,
                        GdsTypes.CF_ELSE,
                        GdsTypes.CF_DO -> {
                            completions += getStatementCompletions(position)
                        }

                        GdsTypes.CURLY_BRACKET_CLOSE -> {
                            completions += getStatementCompletions(position)

                            val prevStatement = prevLeaf.parentOfType<GdsIfStatement>()

                            if (position.parentOfType<GdsIfStatement>() != prevStatement) {
                                completions += GdsLookupElements.ELSE_KEYWORD
                            }
                        }

                        GdsTypes.SEMICOLON -> {
                            completions += 
                                if (prevLeaf.parent is GdsForStatement) {
                                    getExpressionCompletions(position)
                                } else {
                                    getStatementCompletions(position)
                                }
                        }

                        GdsTypes.COLON -> {
                            if (prevLeaf.parent.elementType == GdsTypes.CASE_CLAUSE) {
                                completions += getStatementCompletions(position)
                                completions += GdsLookupElements.BREAK_KEYWORD
                            } else {
                                completions += getExpressionCompletions(position)
                            }
                        }

                        GdsTypes.PARENTHESIS_CLOSE -> {
                            if (prevLeaf.parent.elementType == GdsTypes.IF_STATEMENT ||
                                prevLeaf.parent.elementType == GdsTypes.FOR_STATEMENT ||
                                prevLeaf.parent.elementType == GdsTypes.WHILE_STATEMENT
                            ) {
                                completions += getStatementCompletions(position)
                            }
                        }

                        GdsTypes.BRACKET_OPEN,
                        GdsTypes.CF_CASE,
                        GdsTypes.CF_RETURN,
                        GdsTypes.QUESTION,
                        in GdsTokenSets.OPERATORS -> {
                            completions += getExpressionCompletions(position)
                        }
                        
                        GdsTypes.COMMA -> {
                            val nextLeaf = PsiTreeUtil.nextVisibleLeaf(position) ?: return
                            if (nextLeaf.elementType == GdsTypes.PARENTHESIS_CLOSE) {
                                completions += getExpressionCompletions(position)
                            }
                        }

                        GdsTypes.PARENTHESIS_OPEN -> {
                            if (prevLeaf.parent.elementType == GdsTypes.FOR_STATEMENT) {
                                completions += GdsLookupElements.PRECISIONS
                                completions += GdsLookupElements.DECLARABLE_BUILTIN_TYPES
                            } else {
                                completions += getExpressionCompletions(position)
                            }
                        }
                    }
                    
                    result.addAllElements(completions)
                }
            }
        )

    private fun extendPostfixExpression() =
        extend(
            CompletionType.BASIC,
            psiElement().afterLeaf(psiElement(GdsTypes.PERIOD)),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val dot = PsiTreeUtil.prevVisibleLeaf(position)
                    if (dot == null || dot.node.elementType != GdsTypes.PERIOD) return

                    var baseType: DataType? = null
                    
                    val postfixExpr = dot.parent as? GdsPostfixExpr
                    if (postfixExpr != null) {
                        baseType = GdsExpressionTypeInference.inferTypeBefore(postfixExpr, dot)
                    } else {
                        var curr = PsiTreeUtil.prevVisibleLeaf(dot) ?: return
                        val chain = mutableListOf<String>()
                        var startExpr: GdsExpression? = null
                        
                        while (true) {
                            if (curr.node.elementType == GdsTypes.IDENTIFIER) {
                                val prevDot = PsiTreeUtil.prevVisibleLeaf(curr)
                                if (prevDot != null && prevDot.node.elementType == GdsTypes.PERIOD) {
                                    chain.add(0, curr.text)
                                    curr = PsiTreeUtil.prevVisibleLeaf(prevDot) ?: break
                                    continue
                                } else {
                                    startExpr = PsiTreeUtil.getParentOfType(curr, GdsExpression::class.java)
                                    break
                                }
                            } else {
                                startExpr = PsiTreeUtil.getParentOfType(curr, GdsExpression::class.java)
                                break
                            }
                        }
                        
                        if (startExpr != null) {
                            var currentType = GdsExpressionTypeInference.inferType(startExpr)
                            for (memberName in chain) {
                                currentType = if (currentType is MemberAccessible) {
                                    currentType.resolveMember(memberName)
                                } else {
                                    null
                                }
                                if (currentType == null) break
                            }
                            baseType = currentType
                        } else if (curr.node.elementType == GdsTypes.IDENTIFIER) {
                            val name = curr.text
                            var currentType: DataType? = null
                            
                            GdsResolver.processVariableDeclaration(curr) { variable ->
                                if (variable.name == name) {
                                    currentType = variable.variableSpec?.type
                                    return@processVariableDeclaration false
                                }
                                true
                            }
                            
                            if (currentType != null) {
                                for (memberName in chain) {
                                    currentType = if (currentType is MemberAccessible) {
                                        (currentType as MemberAccessible).resolveMember(memberName)
                                    } else {
                                        null
                                    }
                                    if (currentType == null) break
                                }
                                baseType = currentType
                            }
                        }
                    }

                    if (baseType == null) return

                    val completions = when (baseType) {
                        is StructType -> {
                            baseType.members.map { (name, type) ->
                                GdsLookupElements.createStructMember(name, type)
                            }
                        }
                        is VectorType -> getVectorSwizzles(baseType)
                        else -> emptyList()
                    }

                    result.addAllElements(completions)
                }
            }
        )

    private fun extendPreprocessorDirective() =
        extend(
            CompletionType.BASIC,
            psiElement(GdsTypes.PP_UNKNOWN_LINE),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val text = position.text
                    
                    if (!text.startsWith("#")) return
                    
                    result.addAllElements(GdsLookupElements.PREPROCESSOR_DIRECTIVES)
                }
            }
        )

    private fun getVectorSwizzles(vectorType: VectorType): List<LookupElement> {
        val size = vectorType.containerSize
        val components = mutableListOf<String>()

        val sets = listOf(
            listOf("x", "y", "z", "w"),
            listOf("r", "g", "b", "a"),
            listOf("s", "t", "p", "q")
        )

        for (set in sets) {
            val validComponents = set.take(size)

            components.addAll(validComponents)

            if (size >= 2) {
                for (i in 0 until size - 1) {
                    components.add(validComponents[i] + validComponents[i + 1])
                }
            }
            if (size >= 3) {
                for (i in 0 until size - 2) {
                    components.add(validComponents[i] + validComponents[i + 1] + validComponents[i + 2])
                }
            }
            if (size >= 4) {
                components.add(validComponents.joinToString(""))
            }
        }

        return components.map {
            val resultType = if (it.length == 1) {
                vectorType.elementType
            } else {
                VectorType.of(vectorType.elementType, it.length) ?: vectorType
            }
            
            GdsLookupElements.createVectorSwizzle(it, resultType)
        }
    }
    
    private fun getStatementCompletions(position: PsiElement): List<LookupElement> {
        val completions = arrayListOf<LookupElement>()
        
        completions += GdsLookupElements.CONTROL_STATEMENT_STARTERS
        completions += GdsLookupElements.DO_KEYWORD
        completions += GdsLookupElements.RETURN_KEYWORD
        completions += GdsLookupElements.DISCARD_KEYWORD
        completions += GdsLookupElements.DECLARABLE_BUILTIN_TYPES

        GdsResolver.processStructDeclaration(position) { element ->
            completions += GdsLookupElements.createTypeDeclarationFromStructNameDecl(element)
            return@processStructDeclaration true
        }
        
        completions += GdsLookupElements.PRECISIONS
        completions += GdsLookupElements.CONST_KEYWORD
        
        if (position.parentOfType<GdsForStatement>() != null ||
            position.parentOfType<GdsWhileStatement>() != null ||
            position.parentOfType<GdsDoWhileStatement>() != null
        ) {
            completions += GdsLookupElements.BREAK_KEYWORD
            completions += GdsLookupElements.CONTINUE_KEYWORD
        }
        
        if (position.parentOfType<GdsSwitchStatement>() != null) {
            completions += GdsLookupElements.BREAK_KEYWORD
        }
        
        return completions + getExpressionCompletions(position)
    }
    
    private fun getExpressionCompletions(position: PsiElement): List<LookupElement> {
        val file = position.containingFile as? GdsFile ?: return emptyList()
        val shaderType = GdsPsiImplUtil.getShaderType(file) ?: ShaderType.SPATIAL
        val functionContext = GdsPsiImplUtil.getFunctionContext(position)
        
        val completions = arrayListOf<LookupElement>()
        
        completions += GdsLookupElements.BUILTIN_FUNCTIONS[shaderType to FunctionContext.COMMON] ?: emptyList()
        completions += GdsLookupElements.BUILTIN_FUNCTIONS[shaderType to functionContext] ?: emptyList()

        GdsResolver.processVariableDeclaration(position) { element ->
            when (element) {
                is GdsLightVariable -> return@processVariableDeclaration false
                is GdsVariableNameDecl -> completions += GdsLookupElements.createFromVariableNameDecl(element) ?: return@processVariableDeclaration true
            }
            return@processVariableDeclaration true
        }

        GdsResolver.processFunctionDeclaration(position) { element ->
            when (element) {
                is GdsLightFunction -> return@processFunctionDeclaration false
                is GdsFunctionNameDecl -> completions += GdsLookupElements.createFromFunctionNameDecl(element) ?: return@processFunctionDeclaration true
            }
            return@processFunctionDeclaration true
        }

        GdsResolver.processStructDeclaration(position) { element ->
            completions += GdsLookupElements.createConstructorFromStructNameDecl(element)
            return@processStructDeclaration true
        }

        completions += GdsLookupElements.BUILTIN_VARIABLES[shaderType to FunctionContext.COMMON] ?: emptyList()
        completions += GdsLookupElements.BUILTIN_VARIABLES[shaderType to functionContext] ?: emptyList()

        completions += GdsLookupElements.CONSTRUCTORS
        completions += GdsLookupElements.BOOLEAN_LITERALS
        
        return completions
    }
    
    private fun getPrevCodeLeaf(position: PsiElement): PsiElement? {
        var prevLeaf = PsiTreeUtil.prevVisibleLeaf(position) ?: return null

        while (prevLeaf.elementType in GdsTokenSets.COMMENTS) {
            prevLeaf = PsiTreeUtil.prevVisibleLeaf(prevLeaf) ?: return null
        }

        return prevLeaf
    }
    
}
package kr.jaehoyi.gdshader.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PlatformPatterns.or
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext
import kr.jaehoyi.gdshader.psi.GDShaderBlock
import kr.jaehoyi.gdshader.psi.GDShaderBlockBody
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderRenderModeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderStencilModeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVaryingDeclaration

class GDShaderCompletionContributor : CompletionContributor() {

    init {
        extendShaderTypeDeclaration()
        
        extendRenderModeDeclaration()

        extendStencilModeDeclaration()

        extendUniformGroupDeclaration()

        extendUniformDeclaration()

        extendConstantDeclaration()

        extendVaryingDeclaration()
    }

    private fun extendShaderTypeDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                psiElement().withParent(GDShaderFile::class.java),
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
                psiElement().withParent(GDShaderFile::class.java),
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
                psiElement().withParent(GDShaderFile::class.java),
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
            psiElement().withParent(GDShaderFile::class.java),
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
                psiElement().withParent(GDShaderFile::class.java),
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

                    // 5. After COLON / COMMA
                    if (prevLeaf.elementType == GDShaderTypes.COLON || prevLeaf.elementType == GDShaderTypes.COMMA) {
                        val uniformDeclaration = position.parentOfType<GDShaderUniformDeclaration>()
                        val type = uniformDeclaration?.type?.text

                        result.addAllElements(
                            GDShaderLookupElements.UNIFORM_HINTS[type] ?: emptyList()
                        )
                        return
                    }
                    
                    // 6. Inside initializer expression
                    if (position.parent.elementType == GDShaderTypes.VARIABLE_NAME_REF) {
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        result.addAllElements(GDShaderLookupElements.BOOLEAN_LITERALS)
                    }
                }
            }
        )
    
    private fun extendConstantDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                psiElement().withParent(GDShaderFile::class.java),
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
                        result.addAllElements(GDShaderLookupElements.BUILTIN_TYPES)
                        result.addAllElements(GDShaderLookupElements.BOOLEAN_LITERALS)
                    }
                }
            }
        )
        
    private fun extendVaryingDeclaration() =
        extend(
            CompletionType.BASIC,
            or(
                psiElement().withParent(GDShaderFile::class.java),
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
    
}
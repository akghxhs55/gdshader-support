package kr.jaehoyi.gdshader.completion

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.or
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import kr.jaehoyi.gdshader.psi.GDShaderBlockBody
import kr.jaehoyi.gdshader.psi.GDShaderConstantDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderFile
import kr.jaehoyi.gdshader.psi.GDShaderRenderModeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderShaderTypeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderStencilModeDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderTypes
import kr.jaehoyi.gdshader.psi.GDShaderUniformDeclaration
import kr.jaehoyi.gdshader.psi.GDShaderVaryingDeclaration

object GDShaderPatterns {
    
    val SHADER_TYPE_DECLARATION: ElementPattern<PsiElement> =
        or(
            psiElement().withParent(GDShaderFile::class.java),
            psiElement().inside(GDShaderShaderTypeDeclaration::class.java),
        )
    
    val RENDER_TYPE_DECLARATION: ElementPattern<PsiElement> =
        or(
            psiElement().withParent(GDShaderFile::class.java),
            psiElement().inside(GDShaderRenderModeDeclaration::class.java),
        )
    
    val STENCIL_MODE_DECLARATION: ElementPattern<PsiElement> =
        or(
            psiElement().withParent(GDShaderFile::class.java),
            psiElement().inside(GDShaderStencilModeDeclaration::class.java),
        )
    
    val UNIFORM_GROUP_DECLARATION: ElementPattern<PsiElement> =
        psiElement().withParent(GDShaderFile::class.java)
    
    val UNIFORM_DECLARATION: ElementPattern<PsiElement> =
        or(
            psiElement().withParent(GDShaderFile::class.java),
            psiElement().afterLeaf(psiElement(GDShaderTypes.GLOBAL)),
            psiElement().afterLeaf(psiElement(GDShaderTypes.INSTANCE)),
            psiElement().inside(GDShaderUniformDeclaration::class.java),
        )
    
    val CONSTANT_DECLARATION: ElementPattern<PsiElement> =
        or(
            psiElement().withParent(GDShaderFile::class.java),
            psiElement().inside(GDShaderBlockBody::class.java)
                .andOr(
                    psiElement().afterLeaf(psiElement(GDShaderTypes.CURLY_BRACKET_OPEN)),
                        psiElement().afterLeaf(psiElement(GDShaderTypes.SEMICOLON))
                ),
            psiElement().inside(GDShaderConstantDeclaration::class.java),
        )
    
    val VARYING_DECLARATION: ElementPattern<PsiElement> =
        or(
            psiElement().withParent(GDShaderFile::class.java),
            psiElement().inside(GDShaderVaryingDeclaration::class.java),
            
        )
    
}
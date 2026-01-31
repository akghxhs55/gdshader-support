package kr.jaehoyi.gdshader.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.ColorUtil
import kr.jaehoyi.gdshader.highlighting.GdsSyntaxHighlighter
import kr.jaehoyi.gdshader.model.ConstantSpec
import kr.jaehoyi.gdshader.model.FunctionSpec
import kr.jaehoyi.gdshader.model.InterpolationQualifier
import kr.jaehoyi.gdshader.model.LocalVariableSpec
import kr.jaehoyi.gdshader.model.ParameterQualifier
import kr.jaehoyi.gdshader.model.ParameterSpec
import kr.jaehoyi.gdshader.model.UniformQualifier
import kr.jaehoyi.gdshader.model.UniformSpec
import kr.jaehoyi.gdshader.model.VariableSpec
import kr.jaehoyi.gdshader.model.VaryingSpec
import kr.jaehoyi.gdshader.psi.GdsBlockBody
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaration
import kr.jaehoyi.gdshader.psi.GdsConstantDeclarator
import kr.jaehoyi.gdshader.psi.GdsConstantDeclaratorList
import kr.jaehoyi.gdshader.psi.GdsFile
import kr.jaehoyi.gdshader.psi.GdsFunction
import kr.jaehoyi.gdshader.psi.GdsFunctionDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaration
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclarator
import kr.jaehoyi.gdshader.psi.GdsLocalVariableDeclaratorList
import kr.jaehoyi.gdshader.psi.GdsParameter
import kr.jaehoyi.gdshader.psi.GdsStructBlock
import kr.jaehoyi.gdshader.psi.GdsStructDeclaration
import kr.jaehoyi.gdshader.psi.GdsStructNameDecl
import kr.jaehoyi.gdshader.psi.GdsUniformDeclaration
import kr.jaehoyi.gdshader.psi.GdsVariable
import kr.jaehoyi.gdshader.psi.GdsVaryingDeclaration
import kr.jaehoyi.gdshader.psi.impl.GdsLightFunction
import kr.jaehoyi.gdshader.psi.impl.GdsLightVariable

class GdsDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element == null) return null
        return when (element) {
            is GdsLightFunction -> generateBuiltinFunctionDoc(element)
            is GdsLightVariable -> generateBuiltinVariableDoc(element)
            is GdsFunction -> generateFunctionDoc(element)
            is GdsVariable -> generateVariableDoc(element)
            is GdsStructNameDecl -> generateStructDoc(element)
            else -> null
        }
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element == null) return null
        return when (element) {
            is GdsLightFunction -> getFunctionSignature(element.functionSpec)
            is GdsLightVariable -> getVariableSignature(element.variableSpec)
            is GdsFunction -> element.functionSpec?.let { getFunctionSignature(it) }
            is GdsVariable -> element.variableSpec?.let { getVariableSignature(it) }
            is GdsStructNameDecl -> {
                val keyword = colorize("struct", GdsSyntaxHighlighter.KEYWORD)
                val name = colorize(element.name, GdsSyntaxHighlighter.STRUCT)
                "$keyword $name"
            }
            else -> null
        }
    }

    private fun generateBuiltinFunctionDoc(element: GdsLightFunction): String {
        val spec = element.functionSpec
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append(getFunctionSignature(spec))
        sb.append(DocumentationMarkup.DEFINITION_END)

        if (spec.description != null) {
            sb.append(DocumentationMarkup.CONTENT_START)
            sb.append(spec.description)
            sb.append(DocumentationMarkup.CONTENT_END)
        }
        return sb.toString()
    }

    private fun generateBuiltinVariableDoc(element: GdsLightVariable): String {
        val spec = element.variableSpec
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append(getVariableSignature(spec))
        sb.append(DocumentationMarkup.DEFINITION_END)

        if (spec.description != null) {
            sb.append(DocumentationMarkup.CONTENT_START)
            sb.append(spec.description)
            sb.append(DocumentationMarkup.CONTENT_END)
        }
        return sb.toString()
    }

    private fun generateFunctionDoc(element: GdsFunction): String? {
        val spec = element.functionSpec ?: return null
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append(getFunctionSignature(spec))
        sb.append(DocumentationMarkup.DEFINITION_END)

        val declaration = PsiTreeUtil.getParentOfType(element, GdsFunctionDeclaration::class.java)
        val comment = findDocumentationComment(declaration)
        if (comment != null) {
            sb.append(DocumentationMarkup.CONTENT_START)
            sb.append(formatDocComment(comment))
            sb.append(DocumentationMarkup.CONTENT_END)
        }
        return sb.toString()
    }

    private fun generateVariableDoc(element: GdsVariable): String? {
        val spec = element.variableSpec ?: return null
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append(getVariableSignature(spec))
        sb.append(DocumentationMarkup.DEFINITION_END)

        val targetElement = when (val parent = element.parent) {
            is GdsUniformDeclaration -> parent
            is GdsVaryingDeclaration -> parent
            is GdsConstantDeclarator -> (parent.parent as? GdsConstantDeclaratorList)?.parent as? GdsConstantDeclaration
            is GdsLocalVariableDeclarator -> (parent.parent as? GdsLocalVariableDeclaratorList)?.parent as? GdsLocalVariableDeclaration
            is GdsParameter -> parent
            else -> element
        }

        if (targetElement != null) {
            val comment = findDocumentationComment(targetElement)
            if (comment != null) {
                sb.append(DocumentationMarkup.CONTENT_START)
                sb.append(formatDocComment(comment))
                sb.append(DocumentationMarkup.CONTENT_END)
            }
        }
        return sb.toString()
    }

    private fun generateStructDoc(element: GdsStructNameDecl): String {
        val sb = StringBuilder()
        val keyword = colorize("struct", GdsSyntaxHighlighter.KEYWORD)
        val name = colorize(element.name, GdsSyntaxHighlighter.STRUCT)

        sb.append(DocumentationMarkup.DEFINITION_START)
        sb.append("$keyword $name")
        sb.append(DocumentationMarkup.DEFINITION_END)

        val declaration = PsiTreeUtil.getParentOfType(element, GdsStructDeclaration::class.java)
        val comment = findDocumentationComment(declaration)
        if (comment != null) {
            sb.append(DocumentationMarkup.CONTENT_START)
            sb.append(formatDocComment(comment))
            sb.append(DocumentationMarkup.CONTENT_END)
        }
        return sb.toString()
    }

    private fun getFunctionSignature(spec: FunctionSpec): String {
        val returnType = colorize(spec.returnType.presentationText, GdsSyntaxHighlighter.TYPE)
        val name = colorize(spec.name, GdsSyntaxHighlighter.FUNCTION)

        val params = spec.parameters.joinToString(", ") { param ->
            val qualifierText = if (param.qualifier != ParameterQualifier.IN) {
                colorize(param.qualifier.text, GdsSyntaxHighlighter.KEYWORD) + " "
            } else ""

            val type = colorize(param.type.presentationText, GdsSyntaxHighlighter.TYPE)
            val paramName = colorize(param.name, GdsSyntaxHighlighter.PARAMETER)

            "$qualifierText$type $paramName"
        }
        return "$returnType $name($params)"
    }

    private fun getVariableSignature(spec: VariableSpec): String {
        val type = colorize(spec.type.presentationText, GdsSyntaxHighlighter.TYPE)

        val nameKey = when (spec) {
            is UniformSpec -> GdsSyntaxHighlighter.UNIFORM_VARIABLE
            is VaryingSpec -> GdsSyntaxHighlighter.VARYING_VARIABLE
            is ConstantSpec -> GdsSyntaxHighlighter.CONSTANT
            is ParameterSpec -> GdsSyntaxHighlighter.PARAMETER
            is LocalVariableSpec -> GdsSyntaxHighlighter.LOCAL_VARIABLE
        }
        val name = colorize(spec.name, nameKey)

        val prefix = when (spec) {
            is UniformSpec -> {
                val q = if (spec.qualifier != UniformQualifier.NORMAL) colorize(
                    spec.qualifier.text,
                    GdsSyntaxHighlighter.KEYWORD
                ) + " " else ""
                "${q}${colorize("uniform", GdsSyntaxHighlighter.KEYWORD)} "
            }

            is VaryingSpec -> {
                val q = if (spec.qualifier != InterpolationQualifier.SMOOTH) colorize(
                    spec.qualifier.text,
                    GdsSyntaxHighlighter.KEYWORD
                ) + " " else ""
                "${q}${colorize("varying", GdsSyntaxHighlighter.KEYWORD)} "
            }

            is ConstantSpec -> "${colorize("const", GdsSyntaxHighlighter.KEYWORD)} "
            is ParameterSpec -> {
                if (spec.qualifier != ParameterQualifier.IN) colorize(
                    spec.qualifier.text,
                    GdsSyntaxHighlighter.KEYWORD
                ) + " " else ""
            }

            is LocalVariableSpec -> ""
        }

        return "$prefix$type $name"
    }

    private fun findDocumentationComment(element: PsiElement?): String? {
        if (element == null) return null
        
        var anchor = element
        // Traverse up to find the element that is a direct child of a block or file
        while (anchor != null) {
            val parent = anchor.parent
            if (parent is GdsFile || parent is GdsBlockBody || parent is GdsStructBlock) {
                break
            }
            if (anchor.prevSibling != null && anchor.prevSibling !is PsiWhiteSpace && anchor.prevSibling !is PsiComment) {
                 // Stop if we hit a significant sibling that is not the comment we are looking for.
                 // This prevents jumping over other declarations.
                 break
            }
            anchor = parent
        }
        
        if (anchor == null) return null

        var current = anchor.prevSibling
        val comments = mutableListOf<String>()

        while (current != null) {
            if (current is PsiWhiteSpace) {
                if (current.text.count { it == '\n' } > 1) break
                current = current.prevSibling
                continue
            }
            if (current is PsiComment) {
                comments.add(0, current.text)
                current = current.prevSibling
                continue
            }
            break
        }

        if (comments.isEmpty()) return null
        return comments.joinToString("\n")
    }

    private fun formatDocComment(comment: String): String {
        return comment.lines().joinToString("<br>") { line ->
            line.trim()
                .removePrefix("//")
                .removePrefix("/*")
                .removeSuffix("*/")
                .trim()
                .replace("<", "&lt;")
                .replace(">", "&gt;")
        }
    }

    private fun colorize(text: String, key: TextAttributesKey): String {
        val scheme = EditorColorsManager.getInstance().globalScheme
        val attributes = scheme.getAttributes(key)
        val color = attributes?.foregroundColor
        val hexColor = if (color != null) ColorUtil.toHtmlColor(color) else null

        val escapedText = text.replace("<", "&lt;").replace(">", "&gt;")
        return if (hexColor != null) {
            "<span style=\"color:$hexColor\">$escapedText</span>"
        } else {
            escapedText
        }
    }
}
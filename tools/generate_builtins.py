import argparse
import html
import json
import os
import re
import sys
import urllib.request
from dataclasses import dataclass, field
from itertools import product
from typing import Optional

TOOLS_DIR = os.path.dirname(os.path.abspath(__file__))

ENGINE_TYPES = {
    "VOID": "void",
    "BOOL": "bool", "BVEC2": "bvec2", "BVEC3": "bvec3", "BVEC4": "bvec4",
    "INT": "int", "IVEC2": "ivec2", "IVEC3": "ivec3", "IVEC4": "ivec4",
    "UINT": "uint", "UVEC2": "uvec2", "UVEC3": "uvec3", "UVEC4": "uvec4",
    "FLOAT": "float", "VEC2": "vec2", "VEC3": "vec3", "VEC4": "vec4",
    "MAT2": "mat2", "MAT3": "mat3", "MAT4": "mat4",
    "SAMPLER2D": "sampler2D", "ISAMPLER2D": "isampler2D", "USAMPLER2D": "usampler2D",
    "SAMPLER2DARRAY": "sampler2DArray", "ISAMPLER2DARRAY": "isampler2DArray", "USAMPLER2DARRAY": "usampler2DArray",
    "SAMPLER3D": "sampler3D", "ISAMPLER3D": "isampler3D", "USAMPLER3D": "usampler3D",
    "SAMPLERCUBE": "samplerCube", "SAMPLERCUBEARRAY": "samplerCubeArray", "SAMPLEREXT": "samplerExternalOES",
}

KOTLIN_TYPES = {
    "void": "VOID",
    "bool": "BOOL", "bvec2": "BVEC2", "bvec3": "BVEC3", "bvec4": "BVEC4",
    "int": "INT", "ivec2": "IVEC2", "ivec3": "IVEC3", "ivec4": "IVEC4",
    "uint": "UINT", "uvec2": "UVEC2", "uvec3": "UVEC3", "uvec4": "UVEC4",
    "float": "FLOAT", "vec2": "VEC2", "vec3": "VEC3", "vec4": "VEC4",
    "mat2": "MAT2", "mat3": "MAT3", "mat4": "MAT4",
    "sampler2D": "SAMPLER2D", "isampler2D": "ISAMPLER2D", "usampler2D": "USAMPLER2D",
    "sampler2DArray": "SAMPLER2DARRAY", "isampler2DArray": "ISAMPLER2DARRAY", "usampler2DArray": "USAMPLER2DARRAY",
    "sampler3D": "SAMPLER3D", "isampler3D": "ISAMPLER3D", "usampler3D": "USAMPLER3D",
    "samplerCube": "SAMPLERCUBE", "samplerCubeArray": "SAMPLERCUBEARRAY", "samplerExternalOES": "SAMPLEREXTERNALOES",
    "vec_type": "VEC_TYPE", "vec_int_type": "VEC_INT_TYPE", "vec_uint_type": "VEC_UINT_TYPE",
    "vec_bool_type": "VEC_BOOL_TYPE", "mat_type": "MAT_TYPE", "gvec4_type": "GVEC4_TYPE",
    "gsampler2D": "GSAMPLER2D", "gsampler2DArray": "GSAMPLER2DARRAY", "gsampler3D": "GSAMPLER3D",
}

# Mirrors GdsOverloadResolver.matchesAliasType.
ALIAS_DOMAINS = {
    "vec_type": ["float", "vec2", "vec3", "vec4"],
    "vec_int_type": ["int", "ivec2", "ivec3", "ivec4"],
    "vec_uint_type": ["uint", "uvec2", "uvec3", "uvec4"],
    "vec_bool_type": ["bool", "bvec2", "bvec3", "bvec4"],
    "mat_type": ["mat2", "mat3", "mat4"],
    "gvec4_type": ["vec4", "ivec4", "uvec4"],
    "gsampler2D": ["sampler2D", "isampler2D", "usampler2D"],
    "gsampler2DArray": ["sampler2DArray", "isampler2DArray", "usampler2DArray"],
    "gsampler3D": ["sampler3D", "isampler3D", "usampler3D"],
}

GENERIC_SAMPLERS = {"gsampler2D", "gsampler2DArray", "gsampler3D"}
SIZED_ALIASES = {"vec_type": "", "vec_int_type": "i", "vec_uint_type": "u", "vec_bool_type": "b"}


def warn(message):
    print(f"warning: {message}", file=sys.stderr)



def read_source(local_dir, repository, ref, path):
    if local_dir:
        with open(os.path.join(local_dir, path), encoding="utf-8") as f:
            return f.read()
    url = f"https://raw.githubusercontent.com/{repository}/{ref}/{path}"
    with urllib.request.urlopen(url) as response:
        return response.read().decode("utf-8")



@dataclass
class EngineFunction:
    name: str
    return_type: str
    arg_types: tuple
    arg_names: tuple


@dataclass
class EngineVariable:
    shader_mode: str
    function: str
    name: str
    type: str
    read_only: bool
    has_value: bool


def parse_engine_functions(source):
    table = re.search(r"builtin_func_defs\[\] = \{(.*?)\n\};", source, re.S).group(1)
    entry_re = re.compile(
        r'\{ "(\w+)", TYPE_(\w+), \{ ([^}]*) \}, \{ ([^}]*) \}, (TAG_\w+), (?:true|false) \}'
    )
    functions = []
    for m in entry_re.finditer(table):
        name, ret, args, names, tag = m.groups()
        if tag != "TAG_GLOBAL":
            continue
        arg_types = [t.strip()[len("TYPE_"):] for t in args.split(",")]
        arg_types = tuple(ENGINE_TYPES[t] for t in arg_types if t != "VOID")
        arg_names = tuple(n.strip().strip('"') for n in names.split(","))
        functions.append(EngineFunction(name, ENGINE_TYPES[ret], arg_types, arg_names[:len(arg_types)]))

    type_names = set(ENGINE_TYPES.values())
    functions = [f for f in functions if f.name not in type_names]

    out_args = {}
    out_table = re.search(r"builtin_func_out_args\[\] = \{(.*?)\n\};", source, re.S).group(1)
    for m in re.finditer(r'\{ "(\w+)", \{ ([^}]*) \} \}', out_table):
        out_args[m.group(1)] = {int(i) for i in m.group(2).split(",") if int(i) >= 0}

    frag_table = re.search(r"frag_only_func_defs\[\] = \{(.*?)\n\};", source, re.S).group(1)
    frag_only = set(re.findall(r'\{ "(\w+)" \}', frag_table))

    return functions, out_args, frag_only


def parse_engine_variables(source):
    variable_re = re.compile(
        r'shader_modes\[RSE::SHADER_(\w+)\]\.functions\["(\w+)"\]\.built_ins\["(\w+)"\] = '
        r'(?:(constt|constvt)\()?ShaderLanguage::TYPE_(\w+)'
    )
    variables = []
    for m in variable_re.finditer(source):
        mode, function, name, wrapper, type_name = m.groups()
        variables.append(EngineVariable(
            shader_mode=mode,
            function=function,
            name=name,
            type=ENGINE_TYPES[type_name],
            read_only=wrapper is not None,
            has_value=wrapper == "constvt",
        ))
    return variables



class InlineFormatter:
    INLINE_RE = re.compile(
        r"``(?P<code>.+?)``"
        r"|:ref:`(?P<ref>[^`]+)`"
        r"|\*\*(?P<bold>[^*]+?)\*\*"
        r"|\*(?P<em>[^*\s][^*]*?)\*"
        r"|`(?P<interp>[^`]+)`_{0,2}"
        r"|\|(?P<sub>[\w-]+)\|"
    )

    def __init__(self, docs_url, labels, substitutions):
        self.docs_url = docs_url
        self.labels = labels
        self.substitutions = substitutions

    def format(self, text):
        text = text.replace("\\ ", "")
        out = []
        pos = 0
        for m in self.INLINE_RE.finditer(text):
            out.append(html.escape(text[pos:m.start()], quote=False))
            if m.group("code") is not None:
                out.append(f"<code>{html.escape(m.group('code'), quote=False)}</code>")
            elif m.group("ref") is not None:
                out.append(self._ref(m.group("ref")))
            elif m.group("bold") is not None:
                out.append(f"<b>{html.escape(m.group('bold'), quote=False)}</b>")
            elif m.group("em") is not None:
                out.append(f"<i>{html.escape(m.group('em'), quote=False)}</i>")
            elif m.group("interp") is not None:
                out.append(self._interpreted(m.group("interp")))
            else:
                name = m.group("sub")
                out.append(self.substitutions.get(name, html.escape(name, quote=False)))
            pos = m.end()
        out.append(html.escape(text[pos:], quote=False))
        return "".join(out)

    def _interpreted(self, content):
        link = re.fullmatch(r"(.*?)\s*<(https?://[^>]+)>", content)
        if link:
            return self._anchor(link.group(2), link.group(1) or link.group(2))
        return f"<i>{html.escape(content, quote=False)}</i>"

    def _ref(self, content):
        explicit = re.fullmatch(r"(.*?)\s*<([^>]+)>", content, re.S)
        text, target = (explicit.group(1), explicit.group(2)) if explicit else (None, content)
        target = target.strip()

        if target.startswith("class_"):
            class_name, _, member = target[len("class_"):].partition("_")
            url = f"{self.docs_url}/classes/class_{class_name.lower()}.html"
            if member:
                url += "#" + self._anchor_id(target)
            return self._anchor(url, text or class_name)

        label = self.labels.get(target)
        if label is None:
            return html.escape(text or target, quote=False)
        page, title = label
        return self._anchor(f"{self.docs_url}/{page}.html#{self._anchor_id(target)}", text or title or target)

    @staticmethod
    def _anchor_id(target):
        return re.sub(r"[_/.]", "-", target.lower())

    @staticmethod
    def _anchor(url, text):
        return f'<a href="{html.escape(url)}">{html.escape(text.strip(), quote=False)}</a>'


def collect_labels(rst_files):
    labels = {}
    for page, content in rst_files.items():
        lines = content.splitlines()
        for i, line in enumerate(lines):
            m = re.match(r"^\.\. _([\w-]+):\s*$", line)
            if not m:
                continue
            title = None
            following = [l for l in lines[i + 1:i + 6] if l.strip() and not l.startswith(".. ")]
            if len(following) >= 2 and re.fullmatch(r"[=\-~^\"'`#*+]{3,}", following[1].strip()):
                title = following[0].strip()
            labels[m.group(1)] = (page, title)
    return labels


def join_paragraphs(lines):
    text = " ".join(line if line else "\n" for line in lines)
    paragraphs = [p.strip() for p in re.split(r"\s*\n\s*", text)]
    return [p for p in paragraphs if p]



@dataclass
class DocParam:
    name: str
    type: str
    qualifier: Optional[str]
    optional: bool


@dataclass
class DocFunction:
    name: str
    return_type: str
    params: list
    description: str = ""
    param_docs: dict = field(default_factory=dict)
    returns: str = ""
    link: str = ""

    def arity_range(self):
        required = sum(1 for p in self.params if not p.optional)
        return range(required, len(self.params) + 1)


class FunctionDocParser:
    SIGNATURE_RE = re.compile(r"^(\S+)\s+\*\*(\w+)\*\*\s*\((.*)\)\s*$")
    ANCHOR_RE = re.compile(r":ref:`🔗<shader_func_(\w+)>`")

    def __init__(self, content, formatter):
        self.lines = content.splitlines()
        self.formatter = formatter

    def parse(self):
        functions = []
        chunk = []
        for line in self.lines:
            if line.strip() == "----":
                functions.extend(self._chunk(chunk))
                chunk = []
            else:
                chunk.append(line)
        functions.extend(self._chunk(chunk))
        return functions

    def _chunk(self, lines):
        signatures = []
        body_start = None
        for i, line in enumerate(lines):
            if not line.strip().startswith(".. rst-class:: classref-method"):
                continue
            j = next((k for k in range(i + 1, len(lines)) if lines[k].strip()), None)
            if j is None:
                continue
            signature = self._signature(lines[j].strip())
            if signature:
                signatures.append(signature)
            body_start = j + 1

        names = {s.name for s, _ in signatures}
        kept = []
        for signature, anchor in signatures:
            if anchor and anchor != signature.name and anchor in names:
                warn(f"docs: ignoring signature '{signature.name}' listed under '{anchor}'")
            else:
                kept.append(signature)
        signatures = kept
        if not signatures:
            return []

        body = self._body(lines[body_start:])
        result = []
        for signature in signatures:
            signature.description = body["description"]
            signature.param_docs = body["param_docs"]
            signature.returns = body["returns"]
            signature.link = body["link"]
            result.append(signature)
        return result

    def _signature(self, line):
        anchor = self.ANCHOR_RE.search(line)
        line = self.ANCHOR_RE.sub("", line).replace("\\", "").replace("|", "").strip()
        m = self.SIGNATURE_RE.match(line)
        if not m:
            return None
        return_type, name, params = m.groups()
        return DocFunction(name, return_type, self._params(params)), anchor.group(1) if anchor else None

    @staticmethod
    def _params(text):
        params = []
        optional = False
        for segment in re.split(r",|(?=\[)", text):
            if segment.lstrip().startswith("["):
                optional = True
            words = segment.replace("[", " ").replace("]", " ").split()
            if len(words) < 2:
                continue
            qualifier = words[0] if words[0] in ("in", "out", "inout", "const") else None
            if qualifier:
                words = words[1:]
            params.append(DocParam(words[-1], " ".join(words[:-1]), qualifier, optional))
        return params

    def _body(self, lines):
        sections = {"description": [], "returns": []}
        param_lines = {}
        target = sections["description"]
        link = ""
        code = None
        code_indent = None

        def flush_code():
            nonlocal code, code_indent
            if code is not None:
                while code and not code[0].strip():
                    code.pop(0)
                while code and not code[-1].strip():
                    code.pop()
                if code:
                    indent = min(len(l) - len(l.lstrip()) for l in code if l.strip())
                    block = "\n".join(l[indent:] for l in code)
                    target.append(("code", block))
            code = None
            code_indent = None

        for line in lines:
            stripped = line.strip()
            if code is not None:
                indent = len(line) - len(line.lstrip())
                if not stripped:
                    code.append("")
                    continue
                if code_indent is None:
                    code_indent = indent
                if indent >= code_indent:
                    code.append(line)
                    continue
                flush_code()

            if stripped.startswith(".. rst-class::"):
                break
            if not stripped:
                target.append("")
                continue
            if stripped == "::" or stripped.startswith(".. code-block::"):
                code = []
                continue
            if stripped.endswith("::") and not stripped.startswith(".."):
                target.append(stripped[:-1])
                code = []
                continue

            param = re.match(r"^:param\s+(\w+):\s*(.*)$", stripped)
            if param:
                target = param_lines.setdefault(param.group(1), [])
                if param.group(2):
                    target.append(param.group(2))
                continue
            if stripped.startswith(":return:"):
                target = sections["returns"]
                rest = stripped[len(":return:"):].strip()
                if rest:
                    target.append(rest)
                continue
            if re.match(r"^https?://\S+$", stripped):
                link = stripped
                continue
            target.append(stripped)
        flush_code()

        return {
            "description": self._render(sections["description"]),
            "param_docs": {k: self._render(v) for k, v in param_lines.items()},
            "returns": self._render(sections["returns"]),
            "link": link,
        }

    def _render(self, items):
        blocks = []
        text_lines = []

        def flush_text():
            for paragraph in join_paragraphs(text_lines):
                directive = re.match(r"^\.\.\s+(\w+)::\s*(.*)$", paragraph)
                if directive:
                    rest = self.formatter.format(directive.group(2))
                    blocks.append(f"<b>{directive.group(1).capitalize()}:</b> {rest}".rstrip())
                else:
                    blocks.append(self.formatter.format(paragraph))
            text_lines.clear()

        for item in items:
            if isinstance(item, tuple):
                flush_text()
                blocks.append(f"<pre><code>{html.escape(item[1], quote=False)}</code></pre>")
            elif item.startswith(("- ", "* ")):
                text_lines.extend(["", "• " + item[2:]])
            else:
                text_lines.append(item)
        flush_text()
        return "<br>".join(blocks)



@dataclass
class DocVariable:
    name: str
    type: str
    qualifier: Optional[str]
    description: str


def parse_grid_tables(lines):
    heading = None
    i = 0
    while i < len(lines):
        line = lines[i]
        if i + 1 < len(lines) and line.strip() and re.fullmatch(r"-{3,}", lines[i + 1].strip()) \
                and not line.startswith(("+", "|")):
            heading = line.strip()
        if line.startswith("+-") and i + 1 < len(lines) and lines[i + 1].startswith("|"):
            columns = [m.start() for m in re.finditer(r"\+", line)]
            rows = []
            current = None
            while i < len(lines) and lines[i].startswith(("+", "|")):
                row_line = lines[i]
                if row_line.startswith("+"):
                    if current is not None:
                        rows.append(current)
                    current = [[] for _ in columns[:-1]]
                    if "=" in row_line:
                        rows = []
                else:
                    for c in range(len(columns) - 1):
                        cell = row_line[columns[c] + 1:columns[c + 1]].strip()
                        current[c].append(cell)
                i += 1
            yield heading, [[cell for cell in row] for row in rows if any(any(c) for c in row)]
            continue
        i += 1


def parse_variable_docs(content, formatter, section_contexts):
    docs = {}
    cell_re = re.compile(r"^(?:(in|out|inout)\s+)?(\w+)\s+\*\*(\w+)\*\*$")
    for heading, rows in parse_grid_tables(content.splitlines()):
        contexts = section_contexts.get(heading)
        if contexts is None:
            continue
        for row in rows:
            if len(row) < 2:
                continue
            m = cell_re.match(" ".join(l for l in row[0] if l))
            if not m:
                continue
            qualifier, type_name, name = m.groups()
            description = " ".join(join_paragraphs(row[1]))
            for context in contexts:
                docs.setdefault((context, name), DocVariable(
                    name, type_name, qualifier, formatter.format(description)))
    return docs


def find_variable_doc(docs, context, name):
    doc = docs.get((context, name))
    if doc:
        return doc
    for (doc_context, doc_name), candidate in docs.items():
        if doc_context == context and "X" in doc_name:
            pattern = "".join(r"\d" if ch == "X" else re.escape(ch) for ch in doc_name)
            if re.fullmatch(pattern, name):
                return candidate
    return None



def expand_plugin(doc):
    """Expands a doc signature the way the plugin's overload resolver interprets it."""
    aliases = sorted({p.type for p in doc.params if p.type in ALIAS_DOMAINS})
    expansion = {}
    required = sum(1 for p in doc.params if not p.optional)
    for arity in range(required, len(doc.params) + 1):
        params = doc.params[:arity]
        used = [a for a in aliases if any(p.type == a for p in params)]
        for values in product(*(ALIAS_DOMAINS[a] for a in used)):
            binding = dict(zip(used, values))
            types = tuple(binding.get(p.type, p.type) for p in params)
            expansion[types] = derive_return(doc.return_type, params, types)
    return expansion


def derive_return(return_type, params, types):
    """Mirrors GdsOverloadResolver.substituteReturnType."""
    if return_type not in ALIAS_DOMAINS:
        return return_type
    for param, concrete in zip(params, types):
        if param.type == return_type:
            return concrete
        if return_type == "gvec4_type" and param.type in GENERIC_SAMPLERS:
            return {"": "vec4", "i": "ivec4", "u": "uvec4"}[concrete[0] if concrete[0] in "iu" else ""]
    return None


def coupled_match(doc, function):
    """Whether the doc signature describes the engine overload, reading aliases the way the docs mean them."""
    if len(function.arg_types) not in doc.arity_range():
        return False
    pairs = [(p.type, t) for p, t in zip(doc.params, function.arg_types)] + [(doc.return_type, function.return_type)]
    size = None
    prefix = None
    for doc_type, concrete in pairs:
        if doc_type == concrete:
            continue
        if doc_type not in ALIAS_DOMAINS or concrete not in ALIAS_DOMAINS[doc_type]:
            return False
        if doc_type in SIZED_ALIASES:
            n = ALIAS_DOMAINS[doc_type].index(concrete)
            if size is not None and size != n:
                return False
            size = n
        elif doc_type in GENERIC_SAMPLERS or doc_type == "gvec4_type":
            p = concrete[0] if concrete[0] in "iu" else ""
            if prefix is not None and prefix != p:
                return False
            prefix = p
    return True


@dataclass
class Spec:
    name: str
    return_type: str
    params: list  # (name, type, qualifier, optional)
    doc: Optional[DocFunction]


def build_function_specs(engine_functions, out_args, docs_by_name):
    by_name = {}
    for function in engine_functions:
        overloads = by_name.setdefault(function.name, [])
        if not any(o.arg_types == function.arg_types for o in overloads):
            overloads.append(function)

    specs = []
    for name, overloads in by_name.items():
        docs = docs_by_name.get(name, [])
        if not docs:
            warn(f"docs: no documentation for function '{name}'")
        engine_map = {o.arg_types: o.return_type for o in overloads}
        outs = out_args.get(name, set())

        covered_by = {}
        alias_specs = []
        for doc in docs:
            expansion = expand_plugin(doc)
            exact = all(engine_map.get(types) == ret for types, ret in expansion.items())
            qualifiers_match = all((p.qualifier == "out") == (i in outs) for i, p in enumerate(doc.params))
            if not (exact and qualifiers_match):
                continue
            if any(types not in covered_by for types in expansion):
                spec = Spec(name, doc.return_type, [
                    (p.name, p.type, "OUT" if i in outs else None, p.optional) for i, p in enumerate(doc.params)
                ], doc)
                alias_specs.append(spec)
                for types in expansion:
                    covered_by.setdefault(types, spec)

        concrete = []
        for overload in overloads:
            if overload.arg_types in covered_by:
                continue
            doc = next((d for d in docs if coupled_match(d, overload)), None)
            if doc is None:
                doc = next((d for d in docs if len(overload.arg_types) in d.arity_range()), None)
                if doc is not None:
                    warn(f"docs: '{name}{overload.arg_types}' has no matching doc signature, using a sibling's")
            if doc is None and docs:
                doc = docs[0]
            use_doc_names = doc is not None and len(overload.arg_types) in doc.arity_range()
            params = []
            for i, arg_type in enumerate(overload.arg_types):
                param_name = doc.params[i].name if use_doc_names else overload.arg_names[i]
                params.append((param_name, arg_type, "OUT" if i in outs else None, False))
            concrete.append(Spec(name, overload.return_type, params, doc))

        merged = []
        for spec in concrete:
            longer = next((
                other for other in concrete
                if other is not spec
                and other.return_type == spec.return_type
                and len(other.params) == len(spec.params) + 1
                and [p[1] for p in other.params[:-1]] == [p[1] for p in spec.params]
                and other.doc is not None
                and len(other.params) - 1 < len(other.doc.params)
                and other.doc.params[len(other.params) - 1].optional
            ), None)
            if longer is not None:
                last = longer.params[-1]
                longer.params[-1] = (last[0], last[1], last[2], True)
            else:
                merged.append(spec)

        emitted = set()
        for overload in overloads:
            spec = covered_by.get(overload.arg_types)
            if spec is None:
                spec = next((s for s in merged if s.params and tuple(p[1] for p in s.params) == overload.arg_types
                             or not s.params and not overload.arg_types), None)
            if spec is not None and id(spec) not in emitted:
                emitted.add(id(spec))
                specs.append(spec)
        for spec in alias_specs + merged:
            if id(spec) not in emitted:
                specs.append(spec)

    engine_names = set(by_name)
    for name in docs_by_name:
        if name not in engine_names:
            warn(f"docs: function '{name}' is not a built-in in the engine")
    return specs



def kotlin_string(value):
    escaped = value.replace("\\", "\\\\").replace('"', '\\"').replace("$", "\\$").replace("\n", "\\n")
    return f'"{escaped}"'


class KotlinWriter:
    def __init__(self):
        self.lines = []
        self.used_types = set()

    def type(self, name):
        kotlin = KOTLIN_TYPES[name]
        self.used_types.add(kotlin)
        return kotlin

    def emit(self, indent, text):
        self.lines.append("    " * indent + text)

    def function_spec(self, spec, indent):
        self.emit(indent, "FunctionSpec(")
        self.emit(indent + 1, f"name = {kotlin_string(spec.name)},")
        self.emit(indent + 1, f"returnType = {self.type(spec.return_type)},")
        if spec.params:
            self.emit(indent + 1, "parameters =")
            self.emit(indent + 2, "listOf(")
            for name, type_name, qualifier, optional in spec.params:
                args = [kotlin_string(name), self.type(type_name)]
                if qualifier:
                    args.append(f"qualifier = ParameterQualifier.{qualifier}")
                if optional:
                    args.append("isOptional = true")
                self.emit(indent + 3, f"ParameterSpec({', '.join(args)}),")
            self.emit(indent + 2, "),")
        else:
            self.emit(indent + 1, "parameters = emptyList(),")
        doc = spec.doc
        if doc is not None:
            self.emit(indent + 1, "description =")
            self.emit(indent + 2, "functionDoc(")
            self.emit(indent + 3, f"description = {kotlin_string(doc.description)},")
            param_docs = [(n, doc.param_docs.get(n, "")) for n, _, _, _ in spec.params]
            param_docs = [(n, d) for n, d in param_docs if d]
            if param_docs:
                self.emit(indent + 3, "params =")
                self.emit(indent + 4, "mapOf(")
                for n, d in param_docs:
                    self.emit(indent + 5, f"{kotlin_string(n)} to {kotlin_string(d)},")
                self.emit(indent + 4, "),")
            if doc.returns:
                self.emit(indent + 3, f"returns = {kotlin_string(doc.returns)},")
            if doc.link:
                self.emit(indent + 3, f"link = {kotlin_string(doc.link)},")
            self.emit(indent + 2, "),")
        self.emit(indent, "),")

    def function_list(self, name, specs):
        self.emit(1, f"val {name}: List<FunctionSpec> =")
        self.emit(2, "listOf(")
        for spec in specs:
            self.function_spec(spec, 3)
        self.emit(2, ")")
        self.lines.append("")

    def variables(self, groups):
        self.emit(1, "val variables: Map<Pair<ShaderType, FunctionContext>, List<VariableSpec>> =")
        self.emit(2, "mapOf(")
        for (shader_type, context), variables in groups.items():
            self.emit(3, f"ShaderType.{shader_type} to FunctionContext.{context} to")
            self.emit(4, "listOf(")
            for kind, name, type_name, qualifier, description in variables:
                self.emit(5, f"{kind}(")
                self.emit(6, f"name = {kotlin_string(name)},")
                self.emit(6, f"type = {self.type(type_name)},")
                if qualifier:
                    self.emit(6, f"qualifier = ParameterQualifier.{qualifier},")
                if description:
                    self.emit(6, f"description = {kotlin_string(description)},")
                self.emit(5, "),")
            self.emit(4, "),")
        self.emit(2, ")")
        self.lines.append("")

    def render(self, header):
        imports = [f"import kr.jaehoyi.gdshader.model.Builtins.{t}" for t in sorted(self.used_types)]
        body = "\n".join(self.lines).rstrip() + "\n"
        return "\n".join(header) + "\n\npackage kr.jaehoyi.gdshader.model\n\n" + "\n".join(imports) + \
            "\n\ninternal object GeneratedBuiltins {\n" + body + FUNCTION_DOC + "}\n"


FUNCTION_DOC = """
    private fun functionDoc(
        description: String,
        params: Map<String, String> = emptyMap(),
        returns: String? = null,
        link: String? = null,
    ): String {
        val sb = StringBuilder()
        sb.append(description)

        if (params.isNotEmpty()) {
            sb.append("<br><br><b>Parameters:</b>")
            sb.append("<table cellspacing='0' cellpadding='0' border='0'>")
            params.forEach { (name, desc) ->
                sb.append(
                    "<tr><td valign='top' style='white-space:nowrap'><code>$name</code>&nbsp;&nbsp;</td><td valign='top'>$desc</td></tr>",
                )
            }
            sb.append("</table>")
        }

        if (returns != null) {
            sb.append("<br><b>Returns:</b><br>")
            sb.append(returns)
        }

        if (link != null) {
            sb.append("<br><br><a href=\\"$link\\">Online Reference</a>")
        }

        return sb.toString()
    }
"""



def build_variables(engine_variables, variable_docs, shader_modes):
    constant_names = {v.name for v in engine_variables if v.has_value}
    groups = {}
    for variable in engine_variables:
        shader_type = shader_modes.get(variable.shader_mode)
        if shader_type is None:
            continue
        context = "COMMON" if variable.function in ("global", "constants") else variable.function.upper()
        doc = find_variable_doc(variable_docs.get(shader_type, {}), context, variable.name)
        if doc is None:
            warn(f"docs: no documentation for {shader_type}/{context} built-in '{variable.name}'")
        elif doc.type != variable.type:
            warn(f"docs: {shader_type}/{context} '{variable.name}' is {doc.type} in docs, {variable.type} in engine")

        if variable.name in constant_names:
            kind, qualifier = "ConstantSpec", None
        elif variable.read_only:
            kind, qualifier = "ParameterSpec", None
        else:
            kind = "ParameterSpec"
            qualifier = "OUT" if doc is not None and doc.qualifier == "out" else "INOUT"
            if doc is not None and doc.qualifier not in ("out", "inout"):
                warn(f"docs: {shader_type}/{context} '{variable.name}' is writable but documented as "
                     f"'{doc.qualifier or 'no qualifier'}'")
        if variable.read_only and doc is not None and doc.qualifier in ("out", "inout"):
            warn(f"docs: {shader_type}/{context} '{variable.name}' is read-only but documented as '{doc.qualifier}'")

        entries = groups.setdefault((shader_type, context), [])
        if any(e[1] == variable.name for e in entries):
            continue
        entries.append((kind, variable.name, variable.type, qualifier, doc.description if doc else ""))

    for shader_type, docs in variable_docs.items():
        for (context, name) in docs:
            names = {e[1] for e in groups.get((shader_type, context), [])}
            pattern = "".join(r"\d" if ch == "X" else re.escape(ch) for ch in name)
            if name not in names and not any(re.fullmatch(pattern, n) for n in names):
                warn(f"docs: {shader_type}/{context} '{name}' is not a built-in in the engine")
    return groups


def main():
    parser = argparse.ArgumentParser(description="Generate GeneratedBuiltins.kt from Godot sources.")
    parser.add_argument("--godot-dir", help="Local Godot checkout at the configured ref (downloads otherwise)")
    parser.add_argument("--docs-dir", help="Local godot-docs checkout at the configured ref (downloads otherwise)")
    parser.add_argument("--config", default=os.path.join(TOOLS_DIR, "config.json"))
    args = parser.parse_args()

    with open(args.config, encoding="utf-8") as f:
        config = json.load(f)
    godot, docs = config["godot"], config["docs"]

    def godot_file(path):
        return read_source(args.godot_dir, godot["repository"], godot["ref"], path)

    def docs_file(name):
        return read_source(args.docs_dir, docs["repository"], docs["ref"], f"{docs['directory']}/{name}")

    engine_functions, out_args, frag_only = parse_engine_functions(godot_file(godot["shader_language"]))
    engine_variables = parse_engine_variables(godot_file(godot["shader_types"]))

    rst_files = {f"{docs['directory']}/{name[:-len('.rst')]}": docs_file(name) for name in docs["label_files"]}
    functions_page = f"{docs['directory']}/{docs['functions'][:-len('.rst')]}"
    substitutions = {
        "componentwise": f'<a href="{docs["url"]}/{functions_page}.html#shading-componentwise">'
                         f"Component-wise Function</a>.",
    }
    formatter = InlineFormatter(docs["url"], collect_labels(rst_files), substitutions)

    docs_by_name = {}
    for doc in FunctionDocParser(rst_files[functions_page], formatter).parse():
        docs_by_name.setdefault(doc.name, []).append(doc)
    specs = build_function_specs(engine_functions, out_args, docs_by_name)

    variable_docs = {}
    for file_name, shader_type in docs["variables"].items():
        content = rst_files[f"{docs['directory']}/{file_name[:-len('.rst')]}"]
        variable_docs[shader_type] = parse_variable_docs(content, formatter, config["variable_sections"])
    variables = build_variables(engine_variables, variable_docs, config["shader_modes"])

    writer = KotlinWriter()
    writer.function_list("globalFunctions", [s for s in specs if s.name not in frag_only])
    writer.function_list("fragmentOnlyFunctions", [s for s in specs if s.name in frag_only])
    writer.variables(variables)
    header = [
        "// Generated by tools/generate_builtins.py. Do not edit by hand.",
        f"// Sources: {godot['repository']}@{godot['ref']}, {docs['repository']}@{docs['ref']}",
    ]
    output = os.path.normpath(os.path.join(TOOLS_DIR, config["output"]))
    with open(output, "w", encoding="utf-8", newline="\n") as f:
        f.write(writer.render(header))
    print(f"Wrote {len(specs)} functions and {sum(len(v) for v in variables.values())} variables to {output}")


if __name__ == "__main__":
    main()

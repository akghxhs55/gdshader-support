import json
import os
import re
import requests

class ShaderDocParser:
    def __init__(self, rst_content, substitutions):
        self.lines = rst_content.splitlines()
        self.subs = substitutions

    def parse(self):
        functions = []
        chunk = []
        for line in self.lines:
            if line.strip() == "----":
                if chunk:
                    functions.extend(self._process_chunk(chunk))
                    chunk = []
            else:
                chunk.append(line)
        if chunk:
            functions.extend(self._process_chunk(chunk))
        return functions

    def _process_chunk(self, lines):
        signatures = []
        last_sig_idx = -1

        for i, line in enumerate(lines):
            if line.strip().startswith(".. rst-class:: classref-method"):
                for j in range(i + 1, len(lines)):
                    sig_line = lines[j].strip()
                    if sig_line:
                        spec = self._parse_signature(sig_line)
                        if spec:
                            signatures.append(spec)
                            last_sig_idx = j
                        i = j
                        break
        
        if not signatures:
            return []

        body_spec = self._parse_body(lines[last_sig_idx + 1:])
        
        results = []
        for sig in signatures:
            func = sig.copy()
            func.update({
                'description': body_spec['description'],
                'return_description': body_spec['return_description'],
                'link': body_spec['link']
            })
            
            for param in func['parameters']:
                param['description'] = body_spec['param_docs'].get(param['name'], "")
            
            results.append(func)
        return results

    def _parse_signature(self, line):
        clean_sig = re.sub(r':ref:`.*?`', '', line).strip()
        match = re.match(r'^(.+?)\s+\*\*(.+?)\*\*(?:\\)?\s*\((.*)\)', clean_sig)
        if not match:
            return None

        return {
            "name": match.group(2).strip(),
            "return_type": match.group(1).replace('|', '').strip(),
            "parameters": self._parse_params(match.group(3).strip())
        }

    def _parse_params(self, params_str):
        if not params_str:
            return []
        
        params = []
        # Remove brackets [ , type name ] -> , type name
        args = params_str.replace('[', '').replace(']', '').split(',')
        
        for arg in args:
            clean_arg = arg.replace('\\', '').replace('|', '').strip()
            parts = clean_arg.split()
            if len(parts) >= 2:
                params.append({
                    "name": parts[-1],
                    "type": " ".join(parts[:-1])
                })
        return params

    def _parse_body(self, lines):
        desc_lines, return_lines = [], []
        param_docs = {}
        link = ""
        
        current_section = "desc"
        current_param = None
        
        in_code = False
        code_indent = None
        code_buf = []

        def flush_code():
            nonlocal in_code, code_indent, code_buf
            if code_buf:
                html = f"<pre><code>{chr(10).join(code_buf)}</code></pre>"
                if current_section == "desc": desc_lines.append(html)
                elif current_section == "param" and current_param: param_docs[current_param].append(html)
                elif current_section == "return": return_lines.append(html)
            in_code = False
            code_indent = None
            code_buf = []

        for line in lines:
            stripped = line.strip()
            
            if in_code:
                if not stripped:
                    code_buf.append("")
                    continue
                
                indent = len(line) - len(line.lstrip())
                if code_indent is None: code_indent = indent
                
                if indent >= code_indent:
                    code_buf.append(stripped)
                    continue
                else:
                    flush_code()

            if stripped.startswith(".. rst-class:: classref-item-separator") or \
               stripped.startswith(".. rst-class:: classref-section-separator") or \
               stripped == "----":
                break

            if not stripped:
                target = desc_lines if current_section == "desc" else \
                         param_docs[current_param] if (current_section == "param" and current_param) else \
                         return_lines if current_section == "return" else None
                if target is not None: target.append("")
                continue

            if stripped == "::" or stripped.startswith(".. code-block::"):
                in_code = True
                continue
            elif stripped.endswith("::") and not stripped.lower().startswith(".."):
                # "as follows::" -> "as follows:" + code block
                text = stripped[:-2] + ":"
                target = desc_lines if current_section == "desc" else \
                         param_docs[current_param] if (current_section == "param" and current_param) else \
                         return_lines if current_section == "return" else None
                if target is not None: target.append(text)
                in_code = True
                continue

            param_match = re.match(r'^:param\s+(\w+):', stripped)
            if param_match:
                current_section = "param"
                current_param = param_match.group(1)
                content = stripped[param_match.end():].strip()
                if current_param not in param_docs: param_docs[current_param] = []
                if content: param_docs[current_param].append(content)
            elif stripped.startswith(":return:"):
                current_section = "return"
                content = stripped[len(":return:"):].strip()
                if content: return_lines.append(content)
            elif stripped.startswith(("http://", "https://")):
                link = stripped
            else:
                if current_section == "desc": desc_lines.append(stripped)
                elif current_section == "param" and current_param:
                    if current_param not in param_docs: param_docs[current_param] = []
                    param_docs[current_param].append(stripped)
                elif current_section == "return": return_lines.append(stripped)

        flush_code()

        return {
            "description": self._format_text(desc_lines),
            "param_docs": {k: self._format_text(v) for k, v in param_docs.items()},
            "return_description": self._format_text(return_lines),
            "link": link
        }

    def _format_text(self, lines):
        # Join lines: empty strings become <br>
        text = " ".join([line if line else "<br>" for line in lines])
        text = text.replace(" <br> ", "<br>").replace(" <br>", "<br>").replace("<br> ", "<br>").strip()
        if text.startswith("<br>"): text = text[4:].strip()
        if text.endswith("<br>"): text = text[:-4].strip()

        # 1. Inline Code: ``code`` -> <code>code</code>
        text = re.sub(r'``(.*?)``', r'<code>\1</code>', text)
        
        # 2. Directives: .. note:: -> <br><b>Note:</b>
        text = re.sub(r'\.\.\s+(\w+)::', r'<br><b>\1:</b>', text, flags=re.IGNORECASE)
        
        # 3. Interpreted Text: `text` -> <i>text</i> or `text<target>` -> <i>text</i>
        # Assumes double backticks are already handled
        def replace_interpreted(match):
            content = match.group(1)
            if '<' in content: content = content.split('<')[0].strip()
            return f"<i>{content}</i>"
        text = re.sub(r'`([^`]+)`', replace_interpreted, text)

        # 4. Refs: :ref:`text<target>` -> text
        def replace_ref(match):
            content = match.group(1)
            if '<' in content: return content.split('<')[0].strip()
            return content
        text = re.sub(r':ref:`(.*?)`', replace_ref, text)

        for key, val in self.subs.items():
            text = text.replace(key, val)
            
        return text

if __name__ == "__main__":
    with open("config.json", 'r', encoding='utf-8') as f:
        config = json.load(f)

    all_functions = []
    for source in config['sources']:
        try:
            print(f"Fetching: {source['url']}")
            resp = requests.get(source['url'])
            resp.raise_for_status()
            
            parser = ShaderDocParser(resp.text, config.get('substitutions', {}))
            parsed = parser.parse()
            all_functions.extend(parsed)
            print(f"Parsed {len(parsed)} functions.")
        except Exception as e:
            print(f"Error processing {source['url']}: {e}")

    output_path = config['output']['json_path']
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(all_functions, f, indent=2, ensure_ascii=False)
        print(f"Saved to {output_path}")
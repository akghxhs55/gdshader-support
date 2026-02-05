# GDShader Builtin Functions Generator

This directory contains tools to extract GDShader builtin function specifications from the Godot documentation and generate Kotlin code for the plugin.

## Prerequisites

- Python 3.x
- `requests` module: `pip install requests`

## Workflow

### 1. Extract Specifications

The `extract_function_specs.py` script downloads the raw ReStructuredText (RST) documentation from the Godot repository and parses it into a structured JSON file.

**Configuration:** `config.json`
- `sources`: List of RST file URLs to process.
- `substitutions`: RST substitutions map (e.g., `|componentwise|`).
- `output`: Path where the JSON file will be saved.

**Command:**
```bash
python3 extract_function_specs.py
```
**Output:** `build/generated/shader_functions.json`

### 2. Generate Kotlin Code

The `generate_kotlin_builtins.py` script reads the generated JSON file and produces a Kotlin code fragment containing the `FunctionSpec` definitions.

**Command:**
```bash
python3 generate_kotlin_builtins.py > builtins_fragment.kt
```

### 3. Apply to Codebase

Copy the content of `builtins_fragment.kt` and replace the `globalFunctions` list in `src/main/kotlin/kr/jaehoyi/gdshader/model/Builtins.kt`.

## File Structure

- `extract_function_specs.py`: Main parser logic.
- `generate_kotlin_builtins.py`: Kotlin code generator.
- `config.json`: Configuration file.
- `build/generated/`: Directory where intermediate JSON artifacts are stored.

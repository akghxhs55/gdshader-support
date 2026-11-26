# GDShader Builtins Generator

`generate_builtins.py` generates `src/main/kotlin/kr/jaehoyi/gdshader/model/GeneratedBuiltins.kt`, which holds the built-in functions and built-in variables used by `Builtins.kt`.

## Sources

- **Godot engine** (ground truth for what exists): function signatures from `builtin_func_defs`, `builtin_func_out_args` and `frag_only_func_defs` in `servers/rendering/shader_language.cpp`; built-in variables, their types and read-only flags from `servers/rendering/shader_types.cpp`.
- **godot-docs** (descriptions only): `shader_functions.rst` and the per-shader-type pages in `tutorials/shaders/shader_reference`.

The versions are pinned in `config.json` (`godot.ref`, `docs.ref`). Update them when a new stable Godot version is released.

## How the output is built

- A documented signature that uses alias types (`vec_type`, `gsampler2D`, ...) is kept as-is only if the plugin's overload resolver would read it as exactly the engine's overload set. Otherwise the engine overloads are emitted with concrete types and the closest documented signature supplies the description.
- Variable descriptions are matched by shader type, section and name. Placeholders such as `USERDATAX` or `LIGHTX_COLOR` describe the numbered engine built-ins (`USERDATA1`..`USERDATA6`, `LIGHT0_COLOR`..`LIGHT3_COLOR`).
- Writable variables take `out` or `inout` from the docs, read-only ones become `in`, and built-ins with a constant value become `ConstantSpec`.
- Stage functions (`texture_sdf`, `emit_subparticle`, ...) and processing functions are still written by hand in `Builtins.kt`.

Mismatches between the engine and the docs (missing descriptions, different types, removed built-ins) are printed as warnings.

## Usage

Requires Python 3.9+ with no extra packages.

```bash
python3 tools/generate_builtins.py
```

This downloads the pinned files from GitHub. To use local checkouts at the pinned refs instead:

```bash
python3 tools/generate_builtins.py --godot-dir ~/godot-src --docs-dir ~/godot-docs-src
```

Run `./gradlew ktlintCheck test` after regenerating.

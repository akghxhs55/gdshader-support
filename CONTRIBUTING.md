# Contributing

Thank you for your interest in contributing to the GDShader JetBrains plugin!

## Prerequisites

- **JDK 17+**
- **IntelliJ IDEA** with the following plugins installed:
  - [Grammar-Kit](https://plugins.jetbrains.com/plugin/6606-grammar-kit) — for generating the parser and lexer from grammar files
  - [PsiViewer](https://plugins.jetbrains.com/plugin/227-psiviewer) — useful for inspecting the PSI tree during development

## Build & Run

```bash
# Build the project
./gradlew build

# Run all tests
./gradlew test

# Run all checks (tests + ktlint + etc.)
./gradlew check

# Launch a sandbox IDE with the plugin installed
./gradlew runIde

# Verify plugin compatibility with target IDEs
./gradlew verifyPlugin

# Check code style
./gradlew ktlintCheck

# Auto-fix code style issues
./gradlew ktlintFormat
```

## Project Structure

```
src/main/
├── grammars/               # Grammar definitions
│   ├── GDShader.bnf        #   Parser grammar (Grammar-Kit BNF)
│   └── GDShader.flex        #   Lexer rules (JFlex)
├── gen/                    # Generated code (do NOT edit manually)
│   ├── lexer/              #   Generated lexer
│   ├── parser/             #   Generated parser
│   └── psi/                #   Generated PSI interfaces and implementations
├── kotlin/kr/jaehoyi/gdshader/
│   ├── model/              # Type system, built-in definitions
│   ├── psi/                # PSI utilities, type inference, constant evaluation
│   ├── resolve/            # Symbol resolution and scope processing
│   ├── reference/          # Reference providers (variable, function, struct)
│   ├── completion/         # Code completion contributors
│   ├── highlighting/       # Syntax highlighting, annotators, inspections
│   ├── editor/             # Editor features (folding, structure view, find usages, etc.)
│   ├── formatter/          # Code formatting
│   ├── codeinsight/        # Parameter info, inlay hints, color pickers
│   ├── documentation/      # Documentation providers
│   └── refactoring/        # Rename refactoring
└── resources/
    ├── META-INF/plugin.xml # Plugin extension registrations
    └── messages/           # Localization resource bundles

src/test/
├── kotlin/                 # Test classes
└── testData/               # Test fixture files (.gdshader, .txt)
```

## Modifying the Grammar

The parser and lexer are generated from grammar files using Grammar-Kit.

1. Edit `src/main/grammars/GDShader.bnf` (parser rules) or `GDShader.flex` (lexer rules)
2. In IntelliJ IDEA, right-click the `.bnf` file → **Generate Parser Code**, or the `.flex` file → **Run JFlex Generator**
3. Generated files will appear in `src/main/gen/`

> **Important**: Never edit files in `src/main/gen/` directly. They will be overwritten on regeneration.

## Adding a New Feature

1. Implement the feature in the appropriate package under `src/main/kotlin/`
2. Register the extension in `src/main/resources/META-INF/plugin.xml`
3. Add user-facing strings to `src/main/resources/messages/GDShaderBundle.properties`
4. Write tests in `src/test/kotlin/` (with test data in `src/test/testData/` if needed)
5. Run `./gradlew ktlintFormat` before committing

## Code Style

This project uses [ktlint](https://pinterest.github.io/ktlint/) for code style enforcement. Rules are configured in `.editorconfig`.

Run `./gradlew ktlintFormat` to auto-fix most style issues before committing.

## Testing

Tests are written using the IntelliJ Platform test framework, extending `BasePlatformTestCase` (JUnit 3 style).

- Test classes go in `src/test/kotlin/`, mirroring the main source package structure
- Test data files (`.gdshader` fixtures) go in `src/test/testData/`
- Run tests with `./gradlew test`

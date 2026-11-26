import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("org.jetbrains.intellij.platform") version "2.18.1"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
}

group = "kr.jaehoyi"
version = "1.3.0"

val pluginVerifierIdeVersions =
    providers
        .gradleProperty("pluginVerifierIdeVersion")
        .map { listOf(it) }
        .getOrElse(listOf("2024.2", "latest.release"))

sourceSets {
    main {
        java.srcDirs("src/main/gen")
        kotlin.srcDirs("src/main/gen")
    }
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2")
        plugin("PsiViewer", "242.4697")

        testFramework(TestFrameworkType.Platform)
    }

    intellijPlatformTesting {
        runIde {
            register("runIdeSpecific") {
                type = IntelliJPlatformType.Rider
                version = "2026.3"
                useInstaller = false
            }
        }
    }

    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.13.4")
}

intellijPlatform {
    pluginVerification {
        ides {
            pluginVerifierIdeVersions.forEach { version ->
                create(IntelliJPlatformType.Rider, version) {
                    useInstaller = false
                }
            }
        }
    }

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
        }

        changeNotes =
            """
            <ul>
              <li>Validate function calls in include files (#20)</li>
              <li>Aligned struct constructor call with Godot</li>
              <li>Aligned uniform initialization logic with Godot</li>
              <li>Aligned array initializers and argument checks with Godot</li>
              <li>Fixed struct member resolution</li>
              <li>Fixed constructor call symbol resolution (#21)</li>
              <li>Fixed null pointer exception when open macro documentation (#23)</li>
              <li>Generated built-in functions and variables from the Godot 4.7.2 engine source</li>
              <li>Added `texture_blit` shader type</li>
              <li>Added conditional preprocessor directive evaluation</li>
              <li>Aligned built-in function overload resolution with Godot</li>
              <li>Aligned grammar with Godot (multiple struct members, `hint_range` expressions, comma lists in `for`, line continuation)</li>
              <li>Aligned render modes, stencil modes, uniform hints and file templates with Godot</li>
              <li>Added Godot validations for declarations, shader stages, varyings, constants and literals</li>
              <li>Support highlighting and inspections in unindexed files (#22)</li>
              <li>Support built-in preprocessor defines</li>
              <li>Report unresolved references as errors</li>
              <li>Added option to report unused top-level symbols in include files</li>
              <li>Report unused uniforms, varyings and structs</li>
              <li>Added unused symbol check suppression</li>
            </ul>
            """.trimIndent()
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    withType<Test> {
        useJUnitPlatform()
    }
}

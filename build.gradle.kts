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
version = "1.1.1"

val pluginVerifierIdeVersion = providers.gradleProperty("pluginVerifierIdeVersion").orElse("latest.release")

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
        intellijIdeaCommunity("2025.1.2")
        plugin("PsiViewer", "2025.1")

        testFramework(TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.13.4")
}

intellijPlatform {
    pluginVerification {
        ides {
            create(IntelliJPlatformType.Rider, pluginVerifierIdeVersion) {
                useInstaller = false
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
              <li>Fix to parse chained assignments left to right</li>
              <li>Fix type inference for constant conversion and expression chains</li>
              <li>Add Godot 4.6 and 4.7 builtins</li>
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

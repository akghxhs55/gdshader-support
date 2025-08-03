import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "kr.jaehoyi"
version = "0.2.1-beta"

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
    intellijPlatform.rider("2025.1.2")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "231"
        }

        changeNotes = """
            <h2>0.2.1-beta</h2>
            <ul>
              <li>Lowered minimum required IDE version to 2023.1</li>
            </ul>

            <h2>0.2.0-beta</h2>
            <ul>
              <li>Added creation of new GDShader files from templates</li>
              <li>Improved syntax highlighting</li>
              <li>Improved grammar support</li>
            </ul>

            <h2>0.1.0-beta</h2>
            <ul>
              <li>Initial release</li>
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
}

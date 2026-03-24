import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "kr.jaehoyi"
version = "0.8.0-beta"

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
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.13.4")
}

intellijPlatform {
    pluginVerification {
        ides {
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2024.2")
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3")
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2025.1")
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2025.2")
        }
    }

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
        }
        
        changeNotes = """
            <ul>
              <li>Added syntax validations for various contexts</li>
              <li>Added spell checking support</li>
              <li>Added code inspection for unused variables and functions</li>
              <li>Added quick fix for removing unused symbols</li>
            </ul>
        """.trimIndent()
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
    
    withType<Test> {
        useJUnitPlatform()
    }
}

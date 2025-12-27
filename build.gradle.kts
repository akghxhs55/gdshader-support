import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "kr.jaehoyi"
version = "0.3.2-beta"

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
    pluginConfiguration {
        changeNotes = """
            <ul>
              <li>Added support for Godot 4.5 (Stencil Mode)</li>
              <li>Added file type support for GDShader include files (.gdshaderinc)</li>
              <li>Improved grammar for various contexts</li>
              <li>Improved syntax highlighting</li>
              <li>Fix bugs</li>
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

import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "kr.jaehoyi"
version = "0.6.0-beta"

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
        ideaVersion {
            sinceBuild = "231"
        }
        
        changeNotes = """
            <ul>
              <li>Added parameter info support</li>
              <li>Added struct member reference and completion support</li>
              <li>Added structure view feature</li>
              <li>Added breadcrumbs support</li>
              <li>Added structure-aware navigation bar feature</li>
              <li>Added documentation feature</li>
              <li>Minor feature improvements</li>
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

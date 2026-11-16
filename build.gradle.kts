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
version = "1.2.1"

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

//    intellijPlatformTesting {
//        runIde {
//            register("runIdeSpecific") {
//                type = IntelliJPlatformType.Rider
//                version = "2026.3"
//                useInstaller = false
//            }
//        }
//    }

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
              <li>Add file creation template for empty .gdshader file</li>
              <li>Fix builtin resolving logic in .gdshaderinc files (#16)</li>
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

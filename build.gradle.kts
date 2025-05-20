import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.testkit.jacoco.plugin) apply false
}

subprojects {
    println("Setup child project: ${project.name}")

    println("  > Applying common plugins...")
    apply {
        plugin(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
        plugin(rootProject.libs.plugins.testkit.jacoco.plugin.get().pluginId)
    }

    println("  > Adding common dependencies...")
    dependencies {
        "testImplementation"(rootProject.libs.mockk)
    }

    println("  > Configure Kotlin JVM...")
    configure<KotlinJvmProjectExtension> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)

        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of("21"))
            vendor.set(JvmVendorSpec.BELLSOFT)
        }
    }

    println("  > Configure Test Suites...")
    configure<TestingExtension> {
        suites {
            // Configure the built-in test suite
            val test by getting(JvmTestSuite::class) {
                // Use Kotlin Test test framework
                useKotlinTest(rootProject.libs.plugins.kotlin.jvm.get().version.requiredVersion)
            }
        }
    }

    println("  > Configure Jacoco Coverage Tool...")
    configure<JacocoPluginExtension> {
        toolVersion = rootProject.libs.jacoco.get().version!!
        reportsDirectory = layout.buildDirectory.dir("reposts/coverage")
    }

    println("  > Configure Test Task...")
    tasks.named("test") {
        finalizedBy(tasks.named("jacocoTestReport"))
    }

    println("  > Configure Jacoco Report Task...")
    tasks.withType<JacocoReport> {
        dependsOn(tasks.named("test"))

        reports {
            xml.required = true
            xml.outputLocation = layout.buildDirectory.file("reports/coverage/coverage.xml")

            csv.required = false
            //csv.outputLocation = layout.buildDirectory.file("reports/coverage/coverage.csv")

            html.required = true
            html.outputLocation = layout.buildDirectory.dir("reports/coverage/html")
        }

        val classDir = layout.buildDirectory.dir("classes/kotlin/main")
        val execData = layout.buildDirectory.file("jacoco/test.exec")
        val sourceDirs = files("src/main/kotlin")

        classDirectories.setFrom(
            classDir.map {
                fileTree(it) {
                    exclude("**/generated/**")
                }
            }
        )
        sourceDirectories.setFrom(sourceDirs)
        executionData.setFrom(execData)
    }

    println("The project ${project.name} is configured")
}

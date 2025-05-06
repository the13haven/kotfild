import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import pl.allegro.tech.build.axion.release.domain.hooks.HookContext
import pl.allegro.tech.build.axion.release.domain.preRelease

group = "com.the13haven"
project.version = scmVersion.version

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.axion.release.plugin)
    alias(libs.plugins.testkit.jacoco.plugin)
}

dependencies {
    ksp(project(":ksp"))
}

kotlin {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of("21"))
        vendor.set(JvmVendorSpec.BELLSOFT)
    }
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test test framework
            useKotlinTest(libs.plugins.kotlin.jvm.get().version.requiredVersion)
        }
    }
}

jacoco {
    toolVersion = libs.jacoco.get().version!!

    reportsDirectory = layout.buildDirectory.dir("reposts/coverage")
}

scmVersion {
    localOnly.set(false)
    useHighestVersion.set(true)
    versionIncrementer("incrementPatch")
    releaseOnlyOnReleaseBranches = true

    tag {
        prefix.set("v")
        initialVersion { _, _ -> "0.0.0" }
    }

    repository {
        type.set("git")
    }

    nextVersion {
        suffix.set("SNAPSHOT")
        separator.set("-")
    }

    checks {
        uncommittedChanges.set(true)
        aheadOfRemote.set(true)
        snapshotDependencies.set(true)
    }

    hooks {
        preRelease {
            fileUpdate {
                encoding = "utf-8"
                file("README.md")
                pattern = { previousVersion: String, _: HookContext -> "version \"$previousVersion\"" }
                replacement = { currentVersion: String, _: HookContext -> "version \"$currentVersion\"" }
            }
            commit { releaseVersion, _ -> "Release v${releaseVersion}" }
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required = true
        xml.outputLocation = layout.buildDirectory.file("reports/coverage/coverage.xml")

        csv.required = false
        //csv.outputLocation = layout.buildDirectory.file("reports/coverage/coverage.csv")

        html.required = true
        html.outputLocation = layout.buildDirectory.dir("reports/coverage/html")
    }
}

import pl.allegro.tech.build.axion.release.domain.hooks.HookContext
import pl.allegro.tech.build.axion.release.domain.preRelease

group = "com.the13haven"
project.version = scmVersion.version

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.axion.release.plugin)
}

dependencies {
    ksp(project(":ksp"))
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

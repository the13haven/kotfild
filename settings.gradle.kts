rootProject.name = "kotfild"

include(":ksp")
include(":lib")

dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenLocal()
        mavenCentral()
    }

    pluginManagement {
        repositories {
            mavenLocal()
            mavenCentral()
            gradlePluginPortal()
        }
    }
}

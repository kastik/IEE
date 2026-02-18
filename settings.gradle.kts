pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IEE"
include(":app")
include(":core:data")
include(":core:domain")
include(":core:datastore")
include(":core:database")
include(":core:network")
include(":feature:home")
include(":feature:auth")
include(":feature:announcement")
include(":feature:settings")
include(":core:model")
include(":core:datastore-proto")
include(":feature:profile")
include(":core:analytics")
include(":feature:search")
include(":core:designsystem")
include(":benchmark")
include(":core:notifications")
include(":feature:licenses")
include(":core:ui")
include(":core:downloader")
include(":core:common")
include(":core:work")
include(":core:crashlytics")
include(":core:config")

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
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.google.android.gms.oss-licenses-plugin") {
                useModule("com.google.android.gms:oss-licenses-plugin:0.10.9")
            }
        }
    }
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
include(":core:testing")
include(":core:analytics")
include(":feature:search")
include(":core:designsystem")
include(":benchmark")
include(":core:notifications")
include(":feature:licenses")

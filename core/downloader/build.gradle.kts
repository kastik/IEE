import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.downloader"

    flavorDimensions += "environment"
    productFlavors {
        maybeCreate("local").apply {
            dimension = "environment"
        }
        maybeCreate("production").apply {
            dimension = "environment"
            isDefault = true
        }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
}

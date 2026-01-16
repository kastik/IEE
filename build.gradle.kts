plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.aboutLibraries) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.crashlytics.gradlePlugin) apply false
    alias(libs.plugins.performance.gradlePlugin) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.kotlinSerialization) apply false
}

/*
allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-stdlib")) {
                useVersion("2.2.21") // Or your preferred stable version (e.g., 1.9.24, 2.1.0)
                because("Force stable Kotlin stdlib to avoid incompatible metadata from 2.2.x")
            }
        }
    }
}

 */
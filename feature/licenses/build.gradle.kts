plugins {
    alias(libs.plugins.kastik.library.compose)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.aboutLibraries)
}

android {
    namespace = "com.kastik.apps.feature.licenses"
}
dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.core)
    implementation(libs.aboutlibraries.compose.m3)
}
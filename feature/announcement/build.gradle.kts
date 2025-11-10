plugins {
    alias(libs.plugins.kastik.library.compose)
    alias(libs.plugins.kastik.hilt)
}

android {
    namespace = "com.kastik.apps.announcement"
}
dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}
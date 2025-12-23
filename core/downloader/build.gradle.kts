plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}

android {
    namespace = "com.kastik.apps.core.downloader"
}

dependencies {
    implementation(project(":core:domain"))
}
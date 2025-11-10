plugins {
    alias(libs.plugins.kastik.library)
}

android {
    namespace = "com.kastik.testing"
}

dependencies {
    implementation(project(":core:database"))
    implementation(libs.room.runtime)
    implementation(libs.kotlinx.coroutines.core)
}
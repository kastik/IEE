plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}

android {
    namespace = "com.kastik.apps.domain"
}

dependencies {
    //TODO Find a way to remove this
    implementation(libs.paging.common)
    implementation(project(":core:model"))
}
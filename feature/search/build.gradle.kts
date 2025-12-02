plugins {
    alias(libs.plugins.kastik.library.compose)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.kastik.apps.feature.search"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:designsystem"))
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}
plugins {
    alias(libs.plugins.kastik.library.compose)
    alias(libs.plugins.kastik.hilt)
}

android {
    namespace = "com.kastik.apps.feature.home"
}
dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}
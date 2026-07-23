import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.feature)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.feature.home"
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(libs.accompanist.permissions)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}

import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.kastik.feature)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.feature.settings"
}
dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:notifications"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(libs.accompanist.permissions)
}
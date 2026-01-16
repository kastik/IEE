import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.feature)
    alias(libs.plugins.aboutLibraries)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.feature.licenses"
}
dependencies {
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.core)
    implementation(libs.aboutlibraries.compose.m3)
}
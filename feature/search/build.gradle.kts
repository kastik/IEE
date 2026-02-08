import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.feature)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.feature.search"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}
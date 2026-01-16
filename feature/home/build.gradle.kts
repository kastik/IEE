import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.feature)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.feature.home"
}
dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(libs.accompanist.permissions)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}
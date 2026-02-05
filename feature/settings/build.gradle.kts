import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.feature)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.feature.settings"
}
dependencies {
    implementation(project(":core:common"))
    implementation(libs.accompanist.permissions)
}
import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}
configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.config"
}


dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:crashlytics"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config)

}
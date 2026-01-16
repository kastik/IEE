import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.datastore"
}

dependencies {
    api(project(":core:datastore-proto"))
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
}
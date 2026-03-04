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
    api(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    testFixturesImplementation(libs.androidx.test.core.ktx)
}
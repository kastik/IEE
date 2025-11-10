plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}

android {
    namespace = "com.kastik.apps.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}
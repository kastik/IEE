plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.kastik.apps.network"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
}
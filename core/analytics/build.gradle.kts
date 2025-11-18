plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}
android {
    namespace = "com.kastik.apps.core.analytics"
}


dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
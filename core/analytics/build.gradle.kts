plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}
android {
    namespace = "com.kastik.apps.core.analytics"
}


dependencies {
    releaseImplementation(platform(libs.firebase.bom))
    releaseImplementation(libs.firebase.analytics)
}
import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}
configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.analytics"
}


dependencies {
    releaseImplementation(platform(libs.firebase.bom))
    releaseImplementation(libs.firebase.analytics)
}
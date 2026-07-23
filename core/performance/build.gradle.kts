import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.performance"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.performance) {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
}

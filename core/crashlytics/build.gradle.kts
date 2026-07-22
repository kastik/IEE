import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)

}
configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.crashlytics"

    buildTypes {
        release {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "true"
        }
        debug {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = "false"
        }
    }

    testFixtures {
        enable = true
    }

}


dependencies {
    releaseImplementation(platform(libs.firebase.bom))
    releaseImplementation(libs.firebase.crashlytics)
    releaseImplementation(libs.firebase.crashlytics.ndk)
}
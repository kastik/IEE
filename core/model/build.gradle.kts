import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.model"
}
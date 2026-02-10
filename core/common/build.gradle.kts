import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
}
configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.common"
}
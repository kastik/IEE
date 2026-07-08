import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.feature)
}

configure<LibraryExtension> {
    namespace = "com.kastik.feature.onboarding"
}


dependencies {
    implementation(project(":core:common"))
    implementation(libs.androidx.activity.compose)
}
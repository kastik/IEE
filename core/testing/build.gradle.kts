import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.library.testing)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.testing"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(libs.core.ktx)

    ksp(libs.room.compiler)
    implementation(libs.io.mockk)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.room.runtime)
    implementation(libs.paging.common)
    implementation(libs.paging.common)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.robolectric)
    implementation(libs.junit.platform.runner)
}
@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.library.testing)
    alias(libs.plugins.kastik.hilt)
}

configure<LibraryExtension> {
    namespace = "com.kastik.apps.core.data"
}


dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":core:notifications"))
    testImplementation(project(":core:testing"))

    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    implementation(libs.room.runtime)
    implementation(libs.paging.common)
    implementation(libs.retrofit)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.runner)
    testImplementation(libs.io.mockk)
    testImplementation(libs.robolectric)
}